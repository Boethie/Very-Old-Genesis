package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.metadata.IMetadata;
import genesis.util.Constants.Unlocalized;
import genesis.util.range.RandomDrop;
import genesis.util.WorldUtils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGenesisOre extends BlockOre
{
	protected int minExp;
	protected int maxExp;
	protected RandomDrop drop;

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
	public int getExpDrop(IBlockAccess world, BlockPos pos, int fortune)
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
	
	public RandomDrop getDrop()
	{
		return drop;
	}
	
	public BlockGenesisOre setDrop(RandomDrop drop)
	{
		this.drop = drop;
		return this;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return Collections.singletonList(getDrop().getRandomStackDrop(WorldUtils.getWorldRandom(world, RANDOM)));
	}
}
