package genesis.block;

import java.util.*;

import org.apache.commons.lang3.tuple.*;

import com.google.common.collect.*;

import genesis.client.*;
import genesis.common.*;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.util.*;
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
import net.minecraftforge.fml.relauncher.*;

public class BlockGenesisFlowerPot extends BlockFlowerPot
{
	public static class PropertyContents extends PropertyHelper
	{
		protected final Map<Pair<Item, Integer>, String> values;
		
		public PropertyContents(String name, Map<Pair<Item, Integer>, String> values)
		{
			super(name, Pair.class);
			
			this.values = values;
		}

		@Override
		public Collection getAllowedValues()
		{
			Set<Pair<Item, Integer>> keySet = values.keySet();
			return keySet;
		}

		@Override
		public String getName(Comparable value)
		{
			return values.get(value);
		}
	}
	
	public static final Table<Item, Integer, Pair<Item, Integer>> PAIR_MAP = HashBasedTable.create();
	
	/**
	 * Used to get the same exact instance of Pair for an ItemStack pair, because BlockState$StateImplementation doesn't handle new instances for default property values in withProperty.
	 */
	public static Pair<Item, Integer> getPair(Item item, int meta)
	{
		if (PAIR_MAP.contains(item, meta))
		{
			return PAIR_MAP.get(item, meta);
		}
		
		Pair<Item, Integer> pair = Pair.of(item, meta);
		PAIR_MAP.put(item, meta, pair);
		return pair;
	}
	
	public static Pair<Item, Integer> getPairForStack(ItemStack stack)
	{
		return getPair(stack.getItem(), stack.getMetadata());
	}
	
	public static ItemStack getStackFromPair(Pair<Item, Integer> pair)
	{
		return new ItemStack(pair.getLeft(), 1, pair.getRight());
	}
	
	protected final LinkedHashMap<Pair<Item, Integer>, String> stacksToNames = new LinkedHashMap<Pair<Item, Integer>, String>();
	protected final VanillaPotActivationHandler handler = new VanillaPotActivationHandler();
	
	protected PropertyContents contentsProp;
	
	public BlockGenesisFlowerPot()
	{
		super();
		
		MinecraftForge.EVENT_BUS.register(handler);
	}
	
	public void registerPlantForPot(ItemStack stack, String name)
	{
		stacksToNames.put(getPairForStack(stack), name);
	}
	
	public <O extends ObjectType<?, ?>, V extends IMetadata> void registerPlantsForPot(VariantsOfTypesCombo<O, V> combo, O type)
	{
		List<V> variants = combo.getValidVariants(type);
		
		for (V variant : variants)
		{
			registerPlantForPot(combo.getStack(type, variant), type.getVariantName(variant));
		}
	}
	
	public <V extends IMetadata, B extends Block, I extends Item> void registerPlantsForPot(VariantsCombo<V, B, I> combo)
	{
		registerPlantsForPot(combo, combo.soleType);
	}
	
	protected class VanillaPotActivationHandler
	{
		@SubscribeEvent
		public void onBlockInteracted(PlayerInteractEvent event)
		{
			if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
			{
				return;
			}

			ItemStack held = event.entityPlayer.getCurrentEquippedItem();
			
			if (held == null)
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
			
			BlockFlowerPot pot = (BlockFlowerPot) block;
			state = pot.getActualState(state, world, pos);
			EnumFlowerType contents = (EnumFlowerType) state.getValue(pot.CONTENTS);
			
			if (contents != EnumFlowerType.EMPTY)
			{
				return;
			}
			
			if (stacksToNames.containsKey(getPairForStack(held)))
			{
				world.setBlockState(pos, getDefaultState());
				
				TileEntity te = world.getTileEntity(pos);
				
				if (te instanceof TileEntityFlowerPot)
				{
					TileEntityFlowerPot tePot = (TileEntityFlowerPot) te;
					tePot.setFlowerPotData(held.getItem(), held.getMetadata());
					
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
							Packet packet = new C08PacketPlayerBlockPlacement(pos, event.face.getIndex(), held, (float) hitVec.xCoord, (float) hitVec.yCoord, (float) hitVec.zCoord);
							spPlayer.sendQueue.addToSendQueue(packet);
							
							spPlayer.swingItem();
							event.setCanceled(true);
						}
					}
					
					if (!player.capabilities.isCreativeMode)
					{
						held.stackSize--;
					}
				}
				else
				{
					world.setBlockState(pos, state);
				}
			}
		}
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
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		
		if (te instanceof TileEntityFlowerPot)
		{
			TileEntityFlowerPot tePot = (TileEntityFlowerPot) te;
			Pair<Item, Integer> key = getPair(tePot.getFlowerPotItem(), tePot.getFlowerPotData());
			
			if (stacksToNames.containsKey(key))
			{
				state = state.withProperty(contentsProp, key);
			}
		}
		
		return state;
	}
}
