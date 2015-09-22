package genesis.block;

import java.util.*;

import org.apache.commons.lang3.tuple.*;

import com.google.common.collect.*;

import genesis.block.tileentity.TileEntityGenesisFlowerPot;
import genesis.client.*;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.ItemStackKey;
import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.eventhandler.Event.*;

public class BlockGenesisFlowerPot extends BlockFlowerPot
{
	public static interface IFlowerPotPlant
	{
		public int getColorMultiplier(ItemStack contents, IBlockAccess world, BlockPos pos);
	}
	
	public static class PropertyContents extends PropertyHelper
	{
		protected final Map<ItemStackKey, String> values;
		
		public PropertyContents(String name, Map<ItemStackKey, String> values)
		{
			super(name, Pair.class);
			
			this.values = values;
		}
		
		@Override
		public Collection<ItemStackKey> getAllowedValues()
		{
			Set<ItemStackKey> keySet = values.keySet();
			return keySet;
		}
		
		@Override
		public String getName(Comparable value)
		{
			return values.get(value);
		}
	}
	
	private static final Map<ItemStackKey, ItemStackKey> PAIR_MAP = Maps.newHashMap();
	
	/**
	 * Used to get the same exact instance of ItemStackKey for an ItemStack, because BlockState$StateImplementation doesn't handle new instances for default property values in withProperty.
	 */
	public static ItemStackKey getStackKey(ItemStack stack)
	{
		if (stack == null)
		{
			return null;
		}
		
		ItemStackKey newKey = new ItemStackKey(stack);
		
		if (PAIR_MAP.containsKey(newKey))
		{
			return PAIR_MAP.get(newKey);
		}
		
		PAIR_MAP.put(newKey, newKey);
		return newKey;
	}
	
	private final Map<ItemStackKey, String> stacksToNames = new LinkedHashMap<ItemStackKey, String>();
	private final Map<ItemStackKey, IFlowerPotPlant> stacksToCustoms = new HashMap<ItemStackKey, IFlowerPotPlant>();
	
	protected PropertyContents contentsProp;
	
	public BlockGenesisFlowerPot()
	{
		super();
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void registerPlantForPot(ItemStack stack, String name)
	{
		stacksToNames.put(getStackKey(stack), name);
	}
	
	public String getPlantName(ItemStack stack)
	{
		return stacksToNames.get(getStackKey(stack));
	}
	
	public boolean isPlantRegistered(ItemStack stack)
	{
		return getPlantName(stack) != null;
	}
	
	public void registerPlantCustoms(ItemStack stack, IFlowerPotPlant customs)
	{
		stacksToCustoms.put(getStackKey(stack), customs);
	}
	
	public IFlowerPotPlant getPlantCustoms(ItemStack stack)
	{
		return stacksToCustoms.get(getStackKey(stack));
	}
	
	public <O extends ObjectType<?, ?>, V extends IMetadata> void registerPlantsForPot(VariantsOfTypesCombo<O, V> combo, O type, IFlowerPotPlant customs)
	{
		for (V variant : combo.getValidVariants(type))
		{
			ItemStack stack = combo.getStack(type, variant);
			registerPlantForPot(stack, type.getVariantName(variant));
			registerPlantCustoms(stack, customs);
		}
	}
	
	public <V extends IMetadata, B extends Block, I extends Item> void registerPlantsForPot(VariantsCombo<V, B, I> combo, IFlowerPotPlant customs)
	{
		registerPlantsForPot(combo, combo.soleType, customs);
	}
	
	public TileEntityGenesisFlowerPot createTileEntity(World world, IBlockState state)
	{
		return new TileEntityGenesisFlowerPot();
	}
	
	/**
	 * To be called after all plants for this pot have been registered.
	 */
	public void afterAllRegistered()
	{
		contentsProp = new PropertyContents("contents", stacksToNames);
		blockState = new BlockState(this, contentsProp);
		setDefaultState(blockState.getBaseState());
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int metadata)
	{
		return getDefaultState();
	}
	
	public TileEntityGenesisFlowerPot getTileEntity(IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		
		if (te instanceof TileEntityGenesisFlowerPot)
		{
			return (TileEntityGenesisFlowerPot) te;
		}
		
		return null;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntityGenesisFlowerPot pot = getTileEntity(world, pos);
		
		if (pot != null)
		{
			ItemStackKey key = getStackKey(pot.getContents());
			
			if (stacksToNames.containsKey(key))
			{
				state = state.withProperty(contentsProp, key);
			}
		}
		
		return state;
	}
	
	@Override
	public int colorMultiplier(IBlockAccess world, BlockPos pos, int renderPass)
	{
		TileEntityGenesisFlowerPot pot = getTileEntity(world, pos);
		
		if (pot != null)
		{
			IFlowerPotPlant customs = getPlantCustoms(pot.getContents());
			
			if (customs != null)
			{
				return customs.getColorMultiplier(pot.getContents(), world, pos);
			}
		}
		
		return super.colorMultiplier(world, pos, renderPass);
	}
	
	@SubscribeEvent
	public void onBlockInteracted(PlayerInteractEvent event)
	{
		if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
		{
			return;
		}

		ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
		
		if (stack == null)
		{
			return;
		}
		
		World world = event.world;
		BlockPos pos = event.pos;
		
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		if (block != Blocks.flower_pot)
		{
			return;
		}
		
		state = Blocks.flower_pot.getActualState(state, world, pos);
		EnumFlowerType contents = (EnumFlowerType) state.getValue(BlockFlowerPot.CONTENTS);
		
		if (contents != EnumFlowerType.EMPTY)
		{
			return;
		}
		
		if (isPlantRegistered(stack))
		{
			world.setBlockState(pos, getDefaultState());
			
			TileEntityGenesisFlowerPot pot = getTileEntity(world, pos);
			
			if (pot != null)
			{
				pot.setContents(stack);
				
				event.useBlock = Result.DENY;
				event.useItem = Result.DENY;
				
				EntityPlayer player = event.entityPlayer;
				
				if (world.isRemote)	// We must send a packet to the server telling it that the player right clicked or else it won't place the plant in the flower pot.
				{
					Minecraft mc = GenesisClient.getMC();
					EntityPlayerSP spPlayer = mc.thePlayer;
					
					if (spPlayer == player)
					{
						Vec3 hitVec = mc.objectMouseOver.hitVec;
						hitVec = hitVec.subtract(pos.getX(), pos.getY(), pos.getZ());
						Packet packet = new C08PacketPlayerBlockPlacement(pos, event.face.getIndex(), stack, (float) hitVec.xCoord, (float) hitVec.yCoord, (float) hitVec.zCoord);
						spPlayer.sendQueue.addToSendQueue(packet);
						
						spPlayer.swingItem();
						event.setCanceled(true);
					}
				}
				
				if (!player.capabilities.isCreativeMode)
				{
					stack.stackSize--;
				}
			}
			else
			{
				world.setBlockState(pos, state);
			}
		}
	}
}
