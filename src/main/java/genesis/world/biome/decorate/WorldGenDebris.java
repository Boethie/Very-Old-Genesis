package genesis.world.biome.decorate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumDebrisOther;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenDebris extends WorldGenDecorationBase
{
	private List<IBlockState> additionalDebris = new ArrayList<IBlockState>();
	
	public WorldGenDebris addAdditional(IBlockState... states)
	{
		for (int i = 0; i < states.length; ++ i)
			additionalDebris.add(states[i]);
		
		return this;
	}
	
	@Override
	protected boolean doGenerate(World world, Random random, BlockPos pos)
	{
		do
		{
			IBlockState state = world.getBlockState(pos);
			
			if (!state.getBlock().isAir(state, world, pos) && !state.getBlock().isLeaves(state, world, pos))
				break;
			
			pos = pos.down();
		}
		while (pos.getY() > 0);
		
		boolean willGenerate = false;
		
		int debrisCount = this.getPatchSize();
		
		if (debrisCount <= 1)
			debrisCount = 10;
		
		for (int i = 0; i < debrisCount; ++i)
		{
			if (generateDebris(world, random, pos.add(2 - random.nextInt(5), 0, 2 - random.nextInt(5)), 5, 3, 5, (i == 0)))
			{
				willGenerate = true;
			}
		}
		
		return willGenerate;
	}
	
	private boolean generateDebris(World world, Random rand, BlockPos pos, int distanceX, int distanceY, int distanceZ, boolean generateAdditional)
	{
		boolean willGenerate = false;
		
		if (!(world.getBlockState(pos).getBlock() == GenesisBlocks.moss || world.getBlockState(pos).getBlock() == Blocks.dirt))
			return false;
		
		IBlockState stateAbove = world.getBlockState(pos.up());
		
		if (!stateAbove.getBlock().isAir(stateAbove, world, pos))
			return false;
		
		IBlockState wood;
		EnumTree variant = null;
		IBlockState debris = null;
		
		BlockPos debrisPos = pos.up();
		
		found:
			for (int x = -distanceX; x <= distanceX; ++x)
			{
				for (int z = -distanceZ; z <= distanceZ; ++z)
				{
					for (int y = -distanceY; y <= distanceY; ++y)
					{
						wood = world.getBlockState(debrisPos.add(x, y, z));
						
						if (wood == GenesisBlocks.calamites.getDefaultState())
						{
							if (Math.abs(distanceX) > 2 || Math.abs(distanceY) > 2)
								return false;
							
							debris = GenesisBlocks.debris.getBlockState(EnumDebrisOther.CALAMITES);
							willGenerate = true;
							break found;
						}
						else if (GenesisBlocks.trees.isStateOf(wood, TreeBlocksAndItems.LOG))
						{
							variant = GenesisBlocks.trees.getVariant(wood);
							
							if (variant.hasDebris())
							{
								debris = GenesisBlocks.debris.getBlockState(variant);
								willGenerate = true;
								break found;
							}
						}
					}
				}
			}
		
		if (additionalDebris.size() > 0 && rand.nextInt(118) == 0 && generateAdditional)
		{
			debris = additionalDebris.get(rand.nextInt(additionalDebris.size()));
			willGenerate = true;
		}
		
		if (willGenerate && debris != null)
		{
			setBlockInWorld(world, debrisPos, debris);
		}
		
		return willGenerate;
	}
}
