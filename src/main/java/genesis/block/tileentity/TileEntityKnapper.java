package genesis.block.tileentity;

import java.util.*;

import com.google.common.collect.ImmutableSet;

import io.netty.buffer.ByteBuf;

import genesis.common.Genesis;
import genesis.common.sounds.GenesisSoundEvents;
import genesis.block.tileentity.crafting.*;
import genesis.block.tileentity.crafting.KnappingRecipeRegistry.*;
import genesis.block.tileentity.gui.ContainerKnapper;
import genesis.util.*;
import genesis.util.Constants.Unlocalized;

import gnu.trove.map.hash.TObjectIntHashMap;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.WorldServer;

import net.minecraftforge.fml.common.network.simpleimpl.*;

public class TileEntityKnapper extends TileEntityInventoryBase implements ISlotsKnapping, ITickable
{
	public class KnappingState
	{
		private final Set<EntityPlayer> knappingPlayers = new HashSet<>();
		private int progress;

		public KnappingState()
		{
			reset();
		}

		public void reset()
		{
			removeKnappingPlayers();
			setProgress(0);
		}

		public boolean isKnapping()
		{
			return !knappingPlayers.isEmpty();
		}

		public boolean isPlayerKnapping(EntityPlayer player)
		{
			return knappingPlayers.contains(player);
		}

		public Set<EntityPlayer> getKnappingPlayers()
		{
			return ImmutableSet.copyOf(knappingPlayers);
		}

		public boolean addKnappingPlayer(EntityPlayer player)
		{
			return knappingPlayers.add(player);
		}

		public boolean removeKnappingPlayer(EntityPlayer player)
		{
			return knappingPlayers.remove(player);
		}

		public void removeKnappingPlayers()
		{
			knappingPlayers.clear();
		}

		public int getMaxProgress()
		{
			IMaterialData data = KnappingRecipeRegistry.getMaterialData(getKnappingRecipeMaterial());

			if (data != null)
			{
				return data.getDestroyTime();
			}

			return -1;
		}

		public int getProgress()
		{
			return progress;
		}

		public void setProgress(int value)
		{
			progress = value;
		}

		public boolean isKnapped()
		{
			return getProgress() >= getMaxProgress();
		}

		public boolean iterateProgress()
		{
			if (isKnapped())
			{
				return true;
			}

			setProgress(getProgress() + getKnappingPlayers().size());
			return isKnapped();
		}

		public float getProgressFloat()
		{
			return getProgress() / (float) getMaxProgress();
		}

		public void writeToNBT(NBTTagCompound compound)
		{
			compound.setInteger("progress", getProgress());
		}

		public void readFromNBT(NBTTagCompound compound)
		{
			setProgress(compound.getInteger("progress"));
		}

		@Override
		public String toString()
		{
			return "progress=" + getProgress() + ",players=" + Stringify.stringifyIterable(getKnappingPlayers());
		}
	}

	public static final int SLOTS_CRAFTING_W = 3;
	public static final int SLOTS_CRAFTING_H = 3;
	public static final int SLOTS_CRAFTING_COUNT = SLOTS_CRAFTING_W * SLOTS_CRAFTING_H;
	public static final int SLOTS_CRAFTING_START = 0;
	public static final int SLOTS_CRAFTING_END = SLOTS_CRAFTING_START + SLOTS_CRAFTING_COUNT - 1;
	public static final int SLOT_KNAP_MATERIAL = 9;
	public static final int SLOT_KNAP_MATERIAL_LOCKED = 10;
	public static final int SLOT_KNAP_TOOL = 11;
	public static final int SLOT_KNAP_TOOL_DAMAGED = 12;
	public static final int SLOT_OUTPUT_MAIN = 13;
	public static final int SLOT_OUTPUT_WASTE = 14;
	public static final int SLOT_COUNT = SLOTS_CRAFTING_COUNT + 6;

	public static final int SOUND_TIME = 5;

	protected KnappingState[] knappingStates = new KnappingState[SLOTS_CRAFTING_COUNT];
	protected boolean knappingLocked = false;

	protected TObjectIntHashMap<EntityPlayer> soundTimers = new TObjectIntHashMap<>();

	public TileEntityKnapper()
	{
		super(SLOT_COUNT, true);

		for (int i = 0; i < knappingStates.length; i++)
		{
			knappingStates[i] = new KnappingState();
		}
	}

	@Override
	public BlockKnapper getBlockType()
	{
		super.getBlockType();
		return (BlockKnapper) blockType;
	}

	protected boolean incrementTimer(EntityPlayer player)
	{
		int value = soundTimers.get(player) - 1;

		if (value <= 0)
		{
			soundTimers.put(player, SOUND_TIME);
			return true;
		}

		soundTimers.put(player, value);
		return false;
	}

	@Override
	public void update()
	{
		boolean changed = false;

		for (KnappingState state : knappingStates)
		{
			if (state.isKnapping())
			{
				state.getKnappingPlayers().stream().filter(this::incrementTimer).forEach(player ->
								player.playSound(GenesisSoundEvents.PLAYER_KNAPPING_HIT, 2, 0.9F + worldObj.rand.nextFloat() * 0.2F));

				if (state.iterateProgress())
				{	// Has been knapped.
					changed = true;

					if (!worldObj.isRemote)
					{
						// Damage the knapping tool.
						ItemStack usingTool = getKnappingToolDamaged();

						if (usingTool == null)
						{
							ItemStack newTool = getKnappingTool();

							if (newTool != null)
							{
								if (newTool.stackSize <= 0)
								{
									setKnappingTool(null);
								}
								else
								{
									usingTool = newTool.splitStack(1);
									setKnappingToolDamaged(usingTool);

									if (newTool.stackSize == 0)
									{
										setKnappingTool(null);
									}
								}
							}
						}

						usingTool = getKnappingRecipeTool();

						if (usingTool != null && usingTool.attemptDamageItem(1, worldObj.rand))
						{
							setKnappingToolDamaged(null);
							/*worldObj.playSoundEffect(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
									Constants.ASSETS_PREFIX + "crafting.knapping_tool_break",
									2, 0.8F + worldObj.rand.nextFloat() * 0.4F);*/
						}

						// Add waste items to the output waste slot.
						IMaterialData data = KnappingRecipeRegistry.getMaterialData(getKnappingRecipeMaterial());

						if (data != null)
						{
							ItemStack addWaste = data.getWaste(worldObj.rand);

							if (addWaste != null)
							{
								ItemStack waste = getOutputWaste();

								if (waste == null)
								{
									setOutputWaste(addWaste.copy());
								}
								else
								{
									ItemStack drop = null;

									if (waste.isItemEqual(addWaste))
									{
										int maxStack = addWaste.getMaxStackSize();

										int count = waste.stackSize + addWaste.stackSize;
										waste.stackSize = Math.min(count, maxStack);

										if (waste.stackSize < count)
										{
											drop = addWaste.copy();
											drop.stackSize = count - waste.stackSize;
										}
									}
									else
									{
										drop = addWaste.copy();
									}

									if (drop != null)
									{
										Set<EntityPlayer> players = state.getKnappingPlayers();
										Iterator<EntityPlayer> playerIter = players.iterator();
										int random = worldObj.rand.nextInt(players.size());
										EntityPlayer randomPlayer = null;

										for (int i = 0; i <= random; i++)
										{
											randomPlayer = playerIter.next();
										}

										randomPlayer.dropItem(drop, false, false);
									}
								}
							}
						}
					}

					// Stop people knapping this slot.
					state.removeKnappingPlayers();
					// Update the output.
					updateRecipeOutput();
				}
			}
		}

		if (changed)
		{
			boolean allBroken = true;

			for (KnappingState state : knappingStates)
			{
				if (!state.isKnapped())
				{
					allBroken = false;
					break;
				}
			}

			if (allBroken)
			{
				resetKnappingState();
			}
		}
	}

	public ItemStack getKnappingMaterial()
	{
		return getStackInSlot(SLOT_KNAP_MATERIAL);
	}

	public void setKnappingMaterial(ItemStack stack)
	{
		setInventorySlotContents(SLOT_KNAP_MATERIAL, stack);
	}

	public ItemStack getKnappingMaterialLocked()
	{
		return getStackInSlot(SLOT_KNAP_MATERIAL_LOCKED);
	}

	public void setKnappingMaterialLocked(ItemStack stack)
	{
		setInventorySlotContents(SLOT_KNAP_MATERIAL_LOCKED, stack);
	}

	@Override
	public ItemStack getKnappingRecipeMaterial()
	{
		ItemStack lockedMaterial = getKnappingMaterialLocked();
		return lockedMaterial != null ? lockedMaterial : getKnappingMaterial();
	}

	public ItemStack getKnappingTool()
	{
		return getStackInSlot(SLOT_KNAP_TOOL);
	}

	public void setKnappingTool(ItemStack stack)
	{
		setInventorySlotContents(SLOT_KNAP_TOOL, stack);
	}

	public ItemStack getKnappingToolDamaged()
	{
		return getStackInSlot(SLOT_KNAP_TOOL_DAMAGED);
	}

	public void setKnappingToolDamaged(ItemStack stack)
	{
		setInventorySlotContents(SLOT_KNAP_TOOL_DAMAGED, stack);
	}

	@Override
	public ItemStack getKnappingRecipeTool()
	{
		ItemStack damagedTool = getKnappingToolDamaged();
		return damagedTool != null ? damagedTool : getKnappingTool();
	}

	public ItemStack getOutputMain()
	{
		return getStackInSlot(SLOT_OUTPUT_MAIN);
	}

	public ItemStack setOutputMain(ItemStack stack)
	{
		ItemStack old = getOutputMain();
		setInventorySlotContents(SLOT_OUTPUT_MAIN, stack);
		return old;
	}

	public ItemStack getOutputWaste()
	{
		return getStackInSlot(SLOT_OUTPUT_WASTE);
	}

	public ItemStack setOutputWaste(ItemStack stack)
	{
		ItemStack old = getOutputWaste();
		setInventorySlotContents(SLOT_OUTPUT_WASTE, stack);
		return old;
	}

	public void resetKnappingState()
	{
		for (KnappingState knappingState : knappingStates)
		{
			knappingState.reset();
		}

		setKnappingMaterialLocked(null);
		setKnappingSlotsLocked(false);
		updateRecipeOutput();
	}

	public void updateRecipeOutput()
	{
		boolean knapping = isKnappingEnabled();

		if (!knapping || !knappingLocked)
		{
			boolean set;
			ItemStack output;

			if (knapping)
			{
				output = KnappingRecipeRegistry.getRecipeOutput(this, this);
				set = getOutputMain() == null || output == null;
			}
			else
			{
				output = CraftingManager.getInstance().findMatchingRecipe(getCraftingInventory(), worldObj);
				set = true;
			}

			if (set)
			{
				setOutputMain(output == null ? null : output.copy());
			}
		}
	}

	@Override
	public void markDirty()
	{
		super.markDirty();

		updateRecipeOutput();
	}

	public void stopAllKnapping()
	{
		for (KnappingState state : knappingStates)
		{
			state.removeKnappingPlayers();
		}
	}

	public void setKnappingSlotsLocked(boolean value)
	{
		knappingLocked = value;

		if (knappingLocked)
		{
			stopAllKnapping();
		}
	}

	public boolean areKnappingSlotsLocked()
	{
		return knappingLocked;
	}

	@Override
	public int getKnappingWidth()
	{
		return SLOTS_CRAFTING_W;
	}

	@Override
	public int getHeight()
	{
		return SLOTS_CRAFTING_H;
	}

	protected int getKnappingSlot(int x, int y)
	{
		return y * getKnappingWidth() + x;
	}

	public KnappingState getKnappingSlotState(int index)
	{
		if (index < 0 || index >= SLOTS_CRAFTING_COUNT)
		{
			return null;
		}

		return knappingStates[index];
	}

	@Override
	public KnappingState getKnappingSlotState(int x, int y)
	{
		return getKnappingSlotState(getKnappingSlot(x, y));
	}

	public boolean isKnappingEnabled()
	{
		return KnappingRecipeRegistry.shouldShowKnapping(this, this);
	}

	public static class KnappingSlotMessage implements IMessage
	{
		public BlockPos pos;
		public int index;

		public KnappingSlotMessage() {}

		public KnappingSlotMessage(BlockPos pos, int index)
		{
			this.pos = pos;
			this.index = index;
		}

		@Override
		public void toBytes(ByteBuf buf)
		{
			buf.writeInt(pos.getX());
			buf.writeInt(pos.getY());
			buf.writeInt(pos.getZ());

			buf.writeByte(index);
		}

		@Override
		public void fromBytes(ByteBuf buf)
		{
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());

			index = buf.readByte();
		}

		public static class Handler implements IMessageHandler<KnappingSlotMessage, IMessage>
		{
			@Override
			public IMessage onMessage(final KnappingSlotMessage message, final MessageContext ctx)
			{
				final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				final WorldServer world = (WorldServer) player.worldObj;
				world.addScheduledTask(() -> BlockKnapper.getTileEntity(world, message.pos).switchBreakingSlot(player, message.index));

				return null;
			}
		}
	}

	public Collection<KnappingState> stopKnapping(EntityPlayer player)
	{
		List<KnappingState> states = new ArrayList<>();

		for (KnappingState state : knappingStates)
			if (state.removeKnappingPlayer(player))
				states.add(state);

		return states;
	}

	public boolean switchBreakingSlot(EntityPlayer player, int index)
	{
		boolean changed = false;
		index -= SLOTS_CRAFTING_START;

		if (index != -1)
		{
			if (index < SLOTS_CRAFTING_START || index > SLOTS_CRAFTING_END)
			{	// Check that the index is within the crafting grid.
				index = -1;
			}
			else if (!isKnappingEnabled() || knappingLocked || !KnappingRecipeRegistry.isKnappingTool(getKnappingRecipeTool()))
			{	// Prevent knapping when the items necessary to knap aren't there.
				index = -1;
			}
			else if (getKnappingSlotState(index).isKnapped())
			{
				index = -1;
			}
		}

		// Move material into the locked material slot.
		if (index != -1)
		{
			ItemStack sourceMaterial = getKnappingMaterial();

			if (sourceMaterial != null)
			{
				if (getKnappingMaterialLocked() == null)
				{
					IMaterialData data = KnappingRecipeRegistry.getMaterialData(sourceMaterial);

					if (data != null)
					{
						int amount = data.getCountUsed(worldObj.rand);

						if (sourceMaterial.stackSize >= amount)
						{
							ItemStack lockedMaterial = sourceMaterial.splitStack(amount);
							setKnappingMaterialLocked(lockedMaterial);
						}
					}
				}

				if (sourceMaterial.stackSize == 0)
				{
					setKnappingMaterial(null);
				}
			}
		}

		// If a material was not placed in the locked slot, prevent knapping.
		if (index != -1 && getKnappingMaterialLocked() == null)
		{
			index = -1;
		}

		KnappingState state = index == -1 ? null : getKnappingSlotState(index);
		boolean wasKnapping = state != null && state.isPlayerKnapping(player);

		int stopped = stopKnapping(player).size();

		// Check if the player is changing knapping slots so we can sync this to the server.
		if (stopped == 0)
		{
			changed = state != null;
		}
		else
		{
			if (stopped >= 2)
				changed = true;
			else if (stopped == 1)
				changed = !wasKnapping;

			if (state == null)
				soundTimers.put(player, 0);
		}

		if (state != null)
			state.addKnappingPlayer(player);

		if (changed && worldObj.isRemote)
		{
			KnappingSlotMessage message = new KnappingSlotMessage(pos, index);
			Genesis.network.sendToServer(message);
		}

		return changed;
	}

	public void dropItems()
	{
		resetKnappingState();

		for (int i = 0; i < inventory.length; i++)
		{
			WorldUtils.spawnItemsAt(worldObj, pos, WorldUtils.DropType.CONTAINER, getStackInSlot(i));
			setInventorySlotContents(i, null);
		}
	}

	@Override
	protected void readVisualData(NBTTagCompound compound, boolean save)
	{
		super.readVisualData(compound, save);

		NBTTagList tagList = compound.getTagList("Items", 10);

		for (int i = 0; i < tagList.tagCount(); ++i)
		{
			NBTTagCompound itemCompound = (NBTTagCompound) tagList.get(i);
			byte slot = itemCompound.getByte("Slot");

			if (slot >= 0 && slot < inventory.length)
			{
				inventory[slot] = ItemStack.loadItemStackFromNBT(itemCompound);

				if (itemCompound.hasKey("knappingState") && slot >= SLOTS_CRAFTING_START && slot <= SLOTS_CRAFTING_END)
				{
					KnappingState knappingState = new KnappingState();
					knappingState.readFromNBT(itemCompound.getCompoundTag("knappingState"));
					knappingStates[slot - SLOTS_CRAFTING_START] = knappingState;
				}
			}
		}

		knappingLocked = compound.getBoolean("knappingLocked");

		updateRecipeOutput();
	}

	@Override
	protected void writeVisualData(NBTTagCompound compound, boolean save)
	{
		super.writeVisualData(compound, save);

		NBTTagList itemList = new NBTTagList();
		int slot = 0;

		for (ItemStack stack : inventory)
		{
			if (slot != SLOT_OUTPUT_MAIN)
			{
				NBTTagCompound itemComp = new NBTTagCompound();
				itemComp.setByte("Slot", (byte) slot);

				if (slot >= SLOTS_CRAFTING_START && slot <= SLOTS_CRAFTING_END)
				{
					NBTTagCompound knappingComp = new NBTTagCompound();
					getKnappingSlotState(slot - SLOTS_CRAFTING_START).writeToNBT(knappingComp);
					itemComp.setTag("knappingState", knappingComp);
				}

				if (stack != null)
				{
					stack.writeToNBT(itemComp);
				}

				itemList.appendTag(itemComp);
			}

			slot++;
		}

		compound.setTag("Items", itemList);

		compound.setBoolean("knappingLocked", knappingLocked);
	}

	public InventoryCrafting getCraftingInventory()
	{
		return new DummyCraftingInventory();
	}

	public class DummyCraftingInventory extends InventoryCrafting
	{
		public DummyCraftingInventory()
		{
			super(null, SLOTS_CRAFTING_W, SLOTS_CRAFTING_H);
		}

		protected int getParentSlotIndex(int index)
		{
			return TileEntityKnapper.SLOTS_CRAFTING_START + index;
		}

		@Override
		public String getName()
		{
			return TileEntityKnapper.this.getName();
		}

		@Override
		public boolean hasCustomName()
		{
			return TileEntityKnapper.this.hasCustomName();
		}

		@Override
		public ITextComponent getDisplayName()
		{
			return TileEntityKnapper.this.getDisplayName();
		}

		@Override
		public int getSizeInventory()
		{
			return TileEntityKnapper.SLOTS_CRAFTING_W * TileEntityKnapper.SLOTS_CRAFTING_H;
		}

		@Override
		public ItemStack getStackInSlot(int index)
		{
			return TileEntityKnapper.this.getStackInSlot(getParentSlotIndex(index));
		}

		@Override
		public ItemStack decrStackSize(int index, int count)
		{
			return TileEntityKnapper.this.decrStackSize(getParentSlotIndex(index), count);
		}

		@Override
		public ItemStack removeStackFromSlot(int index)
		{
			return TileEntityKnapper.this.removeStackFromSlot(getParentSlotIndex(index));
		}

		@Override
		public void setInventorySlotContents(int index, ItemStack stack)
		{
			TileEntityKnapper.this.setInventorySlotContents(getParentSlotIndex(index), stack);
		}

		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack)
		{
			return TileEntityKnapper.this.isItemValidForSlot(getParentSlotIndex(index), stack);
		}

		@Override
		public int getInventoryStackLimit()
		{
			return TileEntityKnapper.this.getInventoryStackLimit();
		}

		@Override
		public void markDirty()
		{
			TileEntityKnapper.this.markDirty();
		}

		@Override
		public boolean isUseableByPlayer(EntityPlayer player)
		{
			return TileEntityKnapper.this.isUseableByPlayer(player);
		}

		@Override
		public void openInventory(EntityPlayer player)
		{
			TileEntityKnapper.this.openInventory(player);
		}

		@Override
		public void closeInventory(EntityPlayer player)
		{
			TileEntityKnapper.this.closeInventory(player);
		}

		@Override
		public int getField(int id)
		{
			return TileEntityKnapper.this.getField(id);
		}

		@Override
		public void setField(int id, int value)
		{
			TileEntityKnapper.this.setField(id, value);
		}

		@Override
		public int getFieldCount()
		{
			return TileEntityKnapper.this.getFieldCount();
		}

		@Override
		public void clear()
		{
			TileEntityKnapper.this.clear();
		}
	}

	public IInventory getCraftingOutput()
	{
		return new DummyCraftingOutput();
	}

	public class DummyCraftingOutput implements IInventory
	{
		public DummyCraftingOutput()
		{
		}

		protected int getParentSlotIndex(int index)
		{
			return SLOT_OUTPUT_MAIN;
		}

		@Override
		public String getName()
		{
			return TileEntityKnapper.this.getName();
		}

		@Override
		public boolean hasCustomName()
		{
			return TileEntityKnapper.this.hasCustomName();
		}

		@Override
		public ITextComponent getDisplayName()
		{
			return TileEntityKnapper.this.getDisplayName();
		}

		@Override
		public int getSizeInventory()
		{
			return 1;
		}

		@Override
		public ItemStack getStackInSlot(int index)
		{
			return TileEntityKnapper.this.getStackInSlot(getParentSlotIndex(index));
		}

		@Override
		public ItemStack decrStackSize(int index, int count)
		{
			return TileEntityKnapper.this.decrStackSize(getParentSlotIndex(index), count);
		}

		@Override
		public ItemStack removeStackFromSlot(int index)
		{
			return TileEntityKnapper.this.removeStackFromSlot(getParentSlotIndex(index));
		}

		@Override
		public void setInventorySlotContents(int index, ItemStack stack)
		{
			TileEntityKnapper.this.setInventorySlotContents(getParentSlotIndex(index), stack);
		}

		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack)
		{
			return TileEntityKnapper.this.isItemValidForSlot(getParentSlotIndex(index), stack);
		}

		@Override
		public int getInventoryStackLimit()
		{
			return TileEntityKnapper.this.getInventoryStackLimit();
		}

		@Override
		public void markDirty()
		{
			TileEntityKnapper.this.markDirty();
		}

		@Override
		public boolean isUseableByPlayer(EntityPlayer player)
		{
			return TileEntityKnapper.this.isUseableByPlayer(player);
		}

		@Override
		public void openInventory(EntityPlayer player)
		{
			TileEntityKnapper.this.openInventory(player);
		}

		@Override
		public void closeInventory(EntityPlayer player)
		{
			TileEntityKnapper.this.closeInventory(player);
		}

		@Override
		public int getField(int id)
		{
			return TileEntityKnapper.this.getField(id);
		}

		@Override
		public void setField(int id, int value)
		{
			TileEntityKnapper.this.setField(id, value);
		}

		@Override
		public int getFieldCount()
		{
			return TileEntityKnapper.this.getFieldCount();
		}

		@Override
		public void clear()
		{
			TileEntityKnapper.this.clear();
		}
	}

	@Override
	public String getName()
	{
		return Unlocalized.CONTAINER_UI + "workbench";
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		// TODO: Actual requirements
		return true;
	}
}
