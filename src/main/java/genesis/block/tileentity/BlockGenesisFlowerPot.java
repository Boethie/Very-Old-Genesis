package genesis.block.tileentity;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.*;

import genesis.combo.*;
import genesis.combo.variant.IMetadata;
import genesis.util.ItemStackKey;

import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.common.*;

import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class BlockGenesisFlowerPot extends BlockFlowerPot
{
	public interface IFlowerPotPlant
	{
		int getColorMultiplier(ItemStack contents, IBlockAccess world, BlockPos pos);
	}
	
	public static class PropertyContents extends PropertyHelper<ItemStackKey>
	{
		protected final BiMap<ItemStackKey, String> values;
		
		public PropertyContents(String name, BiMap<ItemStackKey, String> values)
		{
			super(name, ItemStackKey.class);
			
			this.values = values;
		}
		
		@Override
		public Collection<ItemStackKey> getAllowedValues()
		{
			Set<ItemStackKey> keySet = values.keySet();
			return keySet;
		}
		
		@Override
		public String getName(ItemStackKey value)
		{
			return values.get(value);
		}
		
		@Override
		public Optional<ItemStackKey> parseValue(String value)
		{
			if (values.containsValue(value))
				return Optional.of(values.inverse().get(value));
			
			return Optional.absent();
		}
	}
	
	private static final Map<ItemStackKey, ItemStackKey> PAIR_MAP = Maps.newHashMap();
	
	/**
	 * Used to get the same exact instance of ItemStackKey for an ItemStack, because BlockStateContainer$StateImplementation doesn't handle new instances for default property values in withProperty.
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
	
	private static final BiMap<ItemStackKey, String> stacksToNames = HashBiMap.create();//new LinkedHashMap<ItemStackKey, String>();
	private static final Map<ItemStackKey, IFlowerPotPlant> stacksToCustoms = new HashMap<ItemStackKey, IFlowerPotPlant>();
	
	public static void registerPlantForPot(ItemStack stack, String name)
	{
		stacksToNames.put(getStackKey(stack), name);
	}
	
	public static String getPlantName(ItemStack stack)
	{
		return stacksToNames.get(getStackKey(stack));
	}
	
	public static boolean isPlantRegistered(ItemStack stack)
	{
		return getPlantName(stack) != null;
	}
	
	public static void registerPlantCustoms(ItemStack stack, IFlowerPotPlant customs)
	{
		stacksToCustoms.put(getStackKey(stack), customs);
	}
	
	public static IFlowerPotPlant getPlantCustoms(ItemStack stack)
	{
		return stacksToCustoms.get(getStackKey(stack));
	}
	
	public static <V extends IMetadata<V>> void registerPlantsForPot(VariantsOfTypesCombo<V> combo, ObjectType<V, ?, ?> type, IFlowerPotPlant customs)
	{
		for (V variant : combo.getValidVariants(type))
		{
			ItemStack stack = combo.getStack(type, variant);
			registerPlantForPot(stack, type.getVariantName(variant));
			registerPlantCustoms(stack, customs);
		}
	}
	
	public static <V extends IMetadata<V>> void registerPlantsForPot(VariantsCombo<V, ?, ?> combo, IFlowerPotPlant customs)
	{
		registerPlantsForPot(combo, combo.getObjectType(), customs);
	}
	
	protected PropertyContents contentsProp;
	
	public BlockGenesisFlowerPot()
	{
		super();
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
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
		blockState = new BlockStateContainer(this, contentsProp);
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
	
	@SubscribeEvent
	public void onBlockInteracted(RightClickBlock event)
	{
		ItemStack stack = event.getItemStack();
		
		if (stack == null)
			return;
		
		if (event.getEntityPlayer().isSneaking())
			return;
		
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		
		IBlockState state = world.getBlockState(pos);
		
		if (state.getBlock() != Blocks.flower_pot)
			return;
		
		state = Blocks.flower_pot.getActualState(state, world, pos);
		
		if (state.getValue(BlockFlowerPot.CONTENTS) != EnumFlowerType.EMPTY)
			return;
		
		if (isPlantRegistered(stack))
		{
			world.setBlockState(pos, getDefaultState());
			event.setUseItem(Result.DENY);
			
			//TODO: Hopefully in future we can set the contents in here so the plant doesn't flicker.
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
			EntityPlayer player, EnumHand hand, ItemStack heldStack,
			EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (isPlantRegistered(heldStack))
		{
			TileEntityGenesisFlowerPot pot = getOrCreateTileEntity(world, pos);
			
			if (pot.getContents() != null)
				return false;
			
			ItemStack potStack;
			
			if (player.capabilities.isCreativeMode)
			{
				potStack = heldStack.copy();
				potStack.stackSize = 1;
			}
			else
			{
				potStack = heldStack.splitStack(1);
			}
			
			pot.setContents(potStack);
			return true;
		}
		
		return false;
	}
	
	public TileEntityGenesisFlowerPot getOrCreateTileEntity(World world, BlockPos pos)
	{
		TileEntityGenesisFlowerPot pot = getTileEntity(world, pos);
		
		if (pot != null)
			return pot;
		
		pot = createTileEntity(world, world.getBlockState(pos));
		world.setTileEntity(pos, pot);
		return pot;
	}
	
	public static TileEntityGenesisFlowerPot getTileEntity(IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		
		if (te instanceof TileEntityGenesisFlowerPot)
		{
			return (TileEntityGenesisFlowerPot) te;
		}
		
		return null;
	}
}
