package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.util.random.drops.blocks.BlockDrops;
import genesis.util.WorldUtils;

import java.util.List;

import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockAccess;

public class BlockGenesisOre extends BlockOre
{
	protected int minExp;
	protected int maxExp;
	protected BlockDrops drop;

	public BlockGenesisOre(float hardness, float resistance, int maxExp, int harvestLevel)
	{
		this(hardness, resistance, 0, maxExp, harvestLevel);
	}

	public BlockGenesisOre(float hardness, float resistance, int minExp, int maxExp, int harvestLevel)
	{
		this.minExp = minExp;
		this.maxExp = maxExp;
		
		setHardness(hardness);
		setResistance(resistance);
		setHarvestLevel("pickaxe", harvestLevel);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
	}
	
	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
	{
		return MathHelper.getRandomIntegerInRange(WorldUtils.getWorldRandom(world, RANDOM), minExp, maxExp);
	}
	
	public int getMinExperience()
	{
		return minExp;
	}
	
	public int getMaxExperience()
	{
		return maxExp;
	}
	
	public BlockDrops getDrop()
	{
		return drop;
	}
	
	public BlockGenesisOre setDrops(BlockDrops drop)
	{
		this.drop = drop;
		return this;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return getDrop().getDrops(state, WorldUtils.getWorldRandom(world, RANDOM));
	}
}
