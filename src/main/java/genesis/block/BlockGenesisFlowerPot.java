package genesis.block;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.*;

import genesis.client.GenesisClient;
import genesis.metadata.IMetadata;
import genesis.metadata.VariantsCombo;
import genesis.metadata.VariantsOfTypesCombo;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
			List<Pair<Item, Integer>> list = Lists.newArrayList(keySet);
			return keySet;
		}

		@Override
		public String getName(Comparable value)
		{
			return values.get(value);
		}
	}
	
	public static final LinkedHashMap<Pair<Item, Integer>, Pair<Item, Integer>> PAIR_MAP = new LinkedHashMap();
	
	/**
	 * Used to get the same exact instance of Pair for an ItemStack pair, because BlockState$StateImplementation is stupid.
	 */
	public static Pair<Item, Integer> getPair(Item item, int meta)
	{
		Pair<Item, Integer> pair = Pair.of(item, meta);
		
		if (PAIR_MAP.containsKey(pair))
		{
			pair = PAIR_MAP.get(pair);
		}
		else
		{
			PAIR_MAP.put(pair, pair);
		}
		
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
	
	protected final HashMap<Pair<Item, Integer>, String> stacksToNames = new HashMap();
	protected PropertyContents contentsProp;
	
	public BlockGenesisFlowerPot()
	{
		super();
		
		MinecraftForge.EVENT_BUS.register(this);
		
		setUnlocalizedName(Constants.ASSETS + "flowerPot");
	}
	
	public void registerPlantForPot(ItemStack stack, String name)
	{
		stacksToNames.put(getPairForStack(stack), name);
	}
	
	public void registerPlantsForPot(VariantsOfTypesCombo combo, ObjectType type)
	{
		List<IMetadata> variants = combo.getValidVariants(type);
		
		for (IMetadata variant : variants)
		{
			registerPlantForPot(combo.getStack(type, variant), type.getVariantName(variant));
		}
	}
	
	public void registerPlantsForPot(VariantsCombo combo)
	{
		registerPlantsForPot(combo, combo.soleType);
	}

	/**
	 * To be called after all plants for this pot have been registered.
	 */
	public void afterAllRegistered()
	{
		//registerPlantForPot(new ItemStack(Item.getItemFromBlock(this)), "empty");
		
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
			Pair<Item, Integer> key = Pair.of(tePot.getFlowerPotItem(), tePot.getFlowerPotData());
			
			if (stacksToNames.containsKey(key))
			{
				state = state.withProperty(contentsProp, key);
			}
		}
		
		return state;
	}
	
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
		
		if (block == Blocks.flower_pot)
		{
			BlockFlowerPot pot = (BlockFlowerPot) block;
			state = pot.getActualState(state, world, pos);
			EnumFlowerType contents = (EnumFlowerType) state.getValue(pot.CONTENTS);
			
			if (contents == EnumFlowerType.EMPTY)
			{
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
						
						if (world.isRemote)	// We must send a packet to the server telling it that the player right clicked or else it won't place the plant in the flower pot. Bad Forge. D:
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
	}
}
