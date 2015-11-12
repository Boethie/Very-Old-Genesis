package genesis.block;

import com.google.common.collect.Lists;
import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

import static genesis.common.GenesisBlocks.prototaxites_mycelium;
import static net.minecraft.init.Blocks.*;

/**
 * Created by Vorquel on 11/10/15.
 */
public class BlockRoots extends BlockGenesis
{
	public static final PropertyBool END = PropertyBool.create("end");
	private List<Integer> oreDictionarySupportList = Lists.newArrayList();
	private List<Block> blockSupportList = Lists.newArrayList();
	
	public BlockRoots()
	{
		super(Material.vine);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		System.out.println("hello1");
		setDefaultState(blockState.getBaseState().withProperty(END, true));
		System.out.println("hello2");
		setHardness(0.5F);
	}
	
	public void init()
	{
		OreDictionary.registerOre("dirt", dirt);
		OreDictionary.registerOre("blockDirt", dirt);
		OreDictionary.registerOre("blockMycelium", mycelium);
		OreDictionary.registerOre("blockMycelium", prototaxites_mycelium);
		
		oreDictionarySupportList.add(OreDictionary.getOreID("logWood"));
		oreDictionarySupportList.add(OreDictionary.getOreID("blockMycelium"));

		blockSupportList.add(grass);
		blockSupportList.add(dirt);
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, END);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(END).equals(true) ? 1 : 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return blockState.getBaseState().withProperty(END, meta == 1);
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return type.equals("axe");
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if (worldIn.isRemote)
			return;
		if (canSupport(worldIn, pos.up()))
		{
			if (worldIn.getBlockState(pos.down()).getBlock() == this)
			{
				updateStateIfNecessary(worldIn, pos, state, true);
			}
			else
			{
				updateStateIfNecessary(worldIn, pos, state, false);
			}
		}
		else
		{
			worldIn.setBlockToAir(pos);
		}
	}
	
	private boolean canSupport(World worldIn, BlockPos up)
	{
		if (up.getY() > worldIn.getHeight())
		{
			return false;
		}
		IBlockState state = worldIn.getBlockState(up);
		ItemStack stack = state.getBlock().getPickBlock(null, worldIn, up, null);
		int[] ids = stack == null ? new int[0] : OreDictionary.getOreIDs(stack);
		return true;
	}
	
	private void updateStateIfNecessary(World worldIn, BlockPos pos, IBlockState state, Comparable newState)
	{
		if(newState != state.getValue(END))
		{
			worldIn.setBlockState(pos, blockState.getBaseState().withProperty(END, newState));
		}
	}
}
