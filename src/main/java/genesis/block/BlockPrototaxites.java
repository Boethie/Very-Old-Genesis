package genesis.block;

import genesis.common.*;
import genesis.metadata.EnumMaterial;
import genesis.util.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPrototaxites extends BlockGenesis
{
	public BlockPrototaxites()
	{
		super(Material.wood);
		
		setDefaultState(getBlockState().getBaseState());
		
		setHardness(0.75F);
		setTickRandomly(true);
		setStepSound(GenesisSounds.MUSHROOM);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setHarvestLevel("axe", 0);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return Collections.singletonList(GenesisItems.materials.getStack(EnumMaterial.PROTOTAXITES_FLESH));
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, BlockCactus.AGE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(BlockCactus.AGE).intValue();
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		BlockPos topBlock = pos.up();
		
		if (world.isAirBlock(topBlock))
		{
			int size = 1;
			
			while (world.getBlockState(pos.down(size)).getBlock() == this)
			{
				++size;
			}
			
			if (size < 8)
			{
				int age = state.getValue(BlockCactus.AGE).intValue();
				
				if (age == 15)
				{
					world.setBlockState(topBlock, getDefaultState());
					IBlockState ageReset = state.withProperty(BlockCactus.AGE, 0);
					world.setBlockState(pos, ageReset, 4);
					onNeighborBlockChange(world, topBlock, ageReset, this);
				}
				else if (rand.nextInt(4) > 0)
				{
					world.setBlockState(pos, state.withProperty(BlockCactus.AGE, age + 1), 4);
				}
			}
		}
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return super.canPlaceBlockAt(worldIn, pos) ? canBlockStay(worldIn, pos) : false;
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if (!canBlockStay(worldIn, pos))
		{
			worldIn.destroyBlock(pos, true);
		}
	}
	
	public boolean canBlockStay(World worldIn, BlockPos pos)
	{
		Block block = worldIn.getBlockState(pos.down()).getBlock();
		
		for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
		{
			if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == GenesisBlocks.prototaxites)
			{
				return false;
			}
		}
		
		return (block == GenesisBlocks.prototaxites) || (block == GenesisBlocks.prototaxites_mycelium);
	}
}
