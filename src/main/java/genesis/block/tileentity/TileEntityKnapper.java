package genesis.block.tileentity;

import java.util.*;

import com.google.common.collect.ImmutableSet;

import io.netty.buffer.ByteBuf;
import genesis.common.Genesis;
import genesis.entity.extendedproperties.GenesisEntityData;
import genesis.entity.extendedproperties.IntegerEntityProperty;
import genesis.block.tileentity.crafting.*;
import genesis.block.tileentity.crafting.KnappingRecipeRegistry.*;
import genesis.block.tileentity.gui.ContainerKnapper;
import genesis.util.Constants;
import genesis.util.Stringify;
import genesis.util.WorldUtils;
import genesis.util.Constants.Unlocalized;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TileEntityKnapper extends TileEntityLockable implements ISlotsKnapping, ITickable
{
	public class KnappingState
	{
		private final Set<EntityPlayer> knappingPlayers = new HashSet<EntityPlayer>();
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
			
			setProgress(getProgress() + 1);
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
	
	public static final IntegerEntityProperty KNAPPING_TIME = new IntegerEntityProperty("knappingTime", 0, false);
	
	protected ItemStack[] inventory = new ItemStack[SLOT_COUNT];
	
	protected KnappingState[] knappingStates = new KnappingState[SLOTS_CRAFTING_COUNT];
	protected EntityPlayer knappingPlayer = null;
	protected boolean knappingLocked = false;
	
	public String customName;
	
	public TileEntityKnapper()
	{
		super();
		
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
	
	@Override
	public void update()
	{
		boolean changed = false;
		
		for (KnappingState state : knappingStates)
		{
			if (state.isKnapping())
			{
				for (EntityPlayer player : state.getKnappingPlayers())
				{
					int time = GenesisEntityData.getValue(player, KNAPPING_TIME);
					
					if (time % 5 == 0)
					{
						player.playSound(Constants.ASSETS_PREFIX + "crafting.knapping_hit", 2, 0.9F + worldObj.rand.nextFloat() * 0.2F);
					}
					
					GenesisEntityData.setValue(player, KNAPPING_TIME, time + 1);
				}
				
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
							worldObj.playSoundEffect(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, Constants.ASSETS_PREFIX + "crafting.knapping_tool_break", 2, 0.8F + worldObj.rand.nextFloat() * 0.4F);
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
		if (!isKnappingEnabled() || !knappingLocked)
		{
			boolean set = true;
			ItemStack output = null;
			
			if (isKnappingEnabled())
			{
				output = KnappingRecipeRegistry.getRecipeOutput(this, this);
				set = getOutputMain() == null || output == null;
				set = true;
			}
			else
			{
				output = CraftingManager.getInstance().findMatchingRecipe(getCraftingInventory(), worldObj);
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
		
		public KnappingSlotMessage()
		{
		}
		
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
				world.addScheduledTask(new Runnable()
				{
					@Override
					public void run()
					{
						TileEntityKnapper te = (TileEntityKnapper) world.getTileEntity(message.pos);
						te.switchBreakingSlot(player, message.index);
					}
				});
				
				return null;
			}
		}
	}
	
	public int stopKnapping(EntityPlayer player)
	{
		int out = 0;
		
		for (KnappingState state : knappingStates)
		{
			if (state.removeKnappingPlayer(player))
			{
				out++;
			}
		}
		
		return out;
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
		
		int amtStopped = stopKnapping(player);
		
		// Check if the player is changing knapping slots so we can sync this to the server.
		if (amtStopped >= 2)
		{
			changed = true;
		}
		else if (amtStopped == 1)
		{
			changed = !wasKnapping;
		}
		else
		{
			changed = state != null;
		}
		
		if (state != null)
		{
			state.addKnappingPlayer(player);
		}
		else
		{
			GenesisEntityData.setValue(player, KNAPPING_TIME, 0);
		}
		
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
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		NBTTagList tagList = compound.getTagList("items", 10);
		inventory = new ItemStack[getSizeInventory()];
		
		for (int i = 0; i < tagList.tagCount(); ++i)
		{
			NBTTagCompound itemCompound = (NBTTagCompound) tagList.get(i);
			byte slot = itemCompound.getByte("slot");
			
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
		
		if (compound.hasKey("customName"))
		{
			customName = compound.getString("customName");
		}
		
		updateRecipeOutput();
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		NBTTagList itemList = new NBTTagList();
		int slot = 0;
		
		for (ItemStack stack : inventory)
		{
			if (slot != SLOT_OUTPUT_MAIN)
			{
				NBTTagCompound itemComp = new NBTTagCompound();
				itemComp.setByte("slot", (byte) slot);
				
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
		
		compound.setTag("items", itemList);
		
		compound.setBoolean("knappingLocked", knappingLocked);
		
		if (hasCustomName())
		{
			compound.setString("customName", customName);
		}
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
			return TileEntityKnapper.this.SLOTS_CRAFTING_START + index;
		}
		
		@Override
		public String getCommandSenderName()
		{
			return TileEntityKnapper.this.getCommandSenderName();
		}
		
		@Override
		public boolean hasCustomName()
		{
			return TileEntityKnapper.this.hasCustomName();
		}
		
		@Override
		public IChatComponent getDisplayName()
		{
			return TileEntityKnapper.this.getDisplayName();
		}
		
		@Override
		public int getSizeInventory()
		{
			return TileEntityKnapper.this.SLOTS_CRAFTING_W * TileEntityKnapper.this.SLOTS_CRAFTING_H;
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
		public ItemStack getStackInSlotOnClosing(int index)
		{
			return TileEntityKnapper.this.getStackInSlotOnClosing(getParentSlotIndex(index));
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
		public String getCommandSenderName()
		{
			return TileEntityKnapper.this.getCommandSenderName();
		}
		
		@Override
		public boolean hasCustomName()
		{
			return TileEntityKnapper.this.hasCustomName();
		}
		
		@Override
		public IChatComponent getDisplayName()
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
		public ItemStack getStackInSlotOnClosing(int index)
		{
			return TileEntityKnapper.this.getStackInSlotOnClosing(getParentSlotIndex(index));
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
	public String getCommandSenderName()
	{
		return hasCustomName() ? customName : Unlocalized.CONTAINER_UI + "workbench";
	}
	
	@Override
	public boolean hasCustomName()
	{
		return customName != null && customName.length() > 0;
	}
	
	@Override
	public int getSizeInventory()
	{
		return inventory.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory[index];
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		ItemStack stack = getStackInSlot(slot);
		
		if (stack != null)
		{
			if (stack.stackSize <= amount)
			{
				setInventorySlotContents(slot, null);
			}
			else
			{
				stack = stack.splitStack(amount);
				
				if (stack.stackSize <= 0)
				{
					setInventorySlotContents(slot, null);
				}
			}
		}
		
		return stack;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		ItemStack stack = getStackInSlot(slot);
		
		if (stack != null)
		{
			setInventorySlotContents(slot, null);
		}
		
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		inventory[slot] = stack;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
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
	
	@Override
	public void clear()
	{
		for (int i = 0; i < this.inventory.length; ++i)
		{
			this.inventory[i] = null;
		}
	}
	
	@Override
	public int getField(int id)
	{
		return 0;
	}
	
	@Override
	public void setField(int id, int value)
	{
	}
	
	@Override
	public int getFieldCount()
	{
		return 0;
	}
	
	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer player)
	{
		return new ContainerKnapper(player, this);
	}
	
	@Override
	public String getGuiID()
	{
		return Constants.ASSETS_PREFIX + "workbench";
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{
	}
}
