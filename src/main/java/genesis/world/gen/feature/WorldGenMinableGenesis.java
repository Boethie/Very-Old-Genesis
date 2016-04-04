package genesis.world.gen.feature;

import java.util.Random;

import genesis.common.GenesisBlocks;
import genesis.util.random.i.IntRange;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.google.common.base.Predicate;

public class WorldGenMinableGenesis extends WorldGenMinable
{
	public final IBlockState ore;
	public final IntRange count;
	public final Predicate<IBlockState> target;
	
	public WorldGenMinableGenesis(IBlockState ore, int minCount, int maxCount)
	{
		this(ore, minCount, maxCount, GenesisBlocks.granite);
	}
	
	public WorldGenMinableGenesis(IBlockState ore, int minCount, int maxCount, Block targetBlock)
	{
		this(ore, minCount, maxCount, BlockStateMatcher.forBlock(targetBlock));
	}
	
	public WorldGenMinableGenesis(IBlockState ore, int minCount, int maxCount, Predicate<IBlockState> target)
	{
		super(ore, minCount, target);
		this.ore = ore;
		this.count = IntRange.create(minCount, maxCount);
		this.target = target;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		//TODO AT: WorldGenMinable.numberOfBlocks
		//numberOfBlocks = count.getRandom(rand);
		//boolean generate = super.generate(world, rand, pos);
		//numberOfBlocks = count.min;
		//return generate;
		int numberOfBlocks = count.get(rand);
		
		float angle = rand.nextFloat() * (float) Math.PI;
		float vecX = MathHelper.sin(angle);
		float vecY = MathHelper.cos(angle);
		
		double startX = (pos.getX() + 8) - vecX * numberOfBlocks / 8;
		double endX = (pos.getX() + 8) + vecX * numberOfBlocks / 8;
		
		double startZ = (pos.getZ() + 8) - vecY * numberOfBlocks / 8;
		double endZ = (pos.getZ() + 8) + vecY * numberOfBlocks / 8;
		
		double startY = pos.getY() + rand.nextInt(3) - 2;
		double endY = pos.getY() + rand.nextInt(3) - 2;
		
		for (int i = 0; i < numberOfBlocks; ++i)
		{
			float part = i / (float) numberOfBlocks;
			double partX = endX + (startX - endX) * part;
			double partY = endY + (startY - endY) * part;
			double partZ = endZ + (startZ - endZ) * part;
			
			double d9 = rand.nextDouble() * numberOfBlocks / 16;
			double d10 = (MathHelper.sin((float) Math.PI * part) + 1) * d9 + 1;
			int j = MathHelper.floor_double(partX - d10 / 2);
			int k = MathHelper.floor_double(partY - d10 / 2);
			int l = MathHelper.floor_double(partZ - d10 / 2);
			int i1 = MathHelper.floor_double(partX + d10 / 2);
			int j1 = MathHelper.floor_double(partY + d10 / 2);
			int k1 = MathHelper.floor_double(partZ + d10 / 2);
			
			for (int x = j; x <= i1; ++x)
			{
				double d12 = (x + 0.5D - partX) / (d10 / 2.0D);
				
				if (d12 * d12 < 1.0D)
				{
					for (int y = k; y <= j1; ++y)
					{
						double d13 = (y + 0.5D - partY) / (d10 / 2.0D);
						
						if (d12 * d12 + d13 * d13 < 1.0D)
						{
							for (int z = l; z <= k1; ++z)
							{
								double d14 = (z + 0.5D - partZ) / (d10 / 2.0D);
								
								if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D)
								{
									BlockPos blockpos1 = new BlockPos(x, y, z);
									IBlockState state = world.getBlockState(blockpos1);
									
									if (state.getBlock().isReplaceableOreGen(state, world, blockpos1, target))
									{
										world.setBlockState(blockpos1, ore, 2);
									}
								}
							}
						}
					}
				}
			}
		}
		
		return true;
	}
}
