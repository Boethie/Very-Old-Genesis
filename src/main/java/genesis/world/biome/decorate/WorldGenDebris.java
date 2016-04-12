package genesis.world.biome.decorate;

import java.util.*;

import com.google.common.collect.*;

import genesis.combo.*;
import genesis.combo.variant.*;
import genesis.common.GenesisBlocks;
import genesis.util.MiscUtils;
import genesis.util.WorldBlockMatcher;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenDebris extends WorldGenDecorationBase
{
	private final List<IBlockState> randomDebris;
	
	public WorldGenDebris(ImmutableList<IBlockState> randomDebris)
	{
		super(WorldBlockMatcher.STANDARD_AIR,
				(s, w, p) -> s.getBlock().isSideSolid(s, w, p, EnumFacing.UP));
		
		setPatchCount(8);
		
		this.randomDebris = randomDebris;
	}
	
	public WorldGenDebris(IBlockState... randomDebris)
	{
		this(ImmutableList.copyOf(randomDebris));
	}
	
	public WorldGenDebris(EnumDebrisOther... randomDebris)
	{
		this(MiscUtils.fluentIterable(randomDebris).transform((v) -> GenesisBlocks.debris.getBlockState(v)).toList());
	}
	
	public WorldGenDebris()
	{
		this(ImmutableList.of());
	}
	
	protected int hArea = 5;
	protected int vArea = 3;
	protected IBlockState[] placingStates;
	
	private static void add(List<IBlockState> states, IBlockState state)
	{
		if (!states.contains(state))
			states.add(state);
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		ArrayList<IBlockState> states = new ArrayList<>();
		
		for (BlockPos checkPos
				: BlockPos.getAllInBoxMutable(
						pos.add(-hArea, -vArea, -hArea),
						pos.add(hArea, vArea, hArea)))
		{
			IBlockState checkState = world.getBlockState(checkPos);
			
			if (checkState.getBlock() == GenesisBlocks.calamites)
			{
				add(states, GenesisBlocks.debris.getBlockState(EnumDebrisOther.CALAMITES));
			}
			else
			{
				TreeBlocksAndItems.SubsetData data = GenesisBlocks.trees.getSubsetData(checkState.getBlock());
				
				if (data != null)
				{
					EnumTree variant = checkState.getValue(data.variantProperty);
					
					if (variant.hasDebris())
						add(states, GenesisBlocks.debris.getBlockState(variant));
				}
			}
		}
		
		if (states.isEmpty() && !randomDebris.isEmpty())
		{
			add(states, randomDebris.get(rand.nextInt(randomDebris.size())));
		}
		
		placingStates = states.toArray(new IBlockState[0]);
		boolean success = false;
		
		if (placingStates.length > 0)
			success = super.generate(world, rand, pos);
		
		placingStates = null;
		return success;
	}
	
	@Override
	public boolean place(World world, Random rand, BlockPos pos)
	{
		if (placingStates == null)
			return generate(world, rand, pos);
		
		return setBlockInWorld(world, pos, placingStates[rand.nextInt(placingStates.length)]);
	}
	
	/*private boolean generateDebris(World world, Random rand, BlockPos pos, int distanceX, int distanceY, int distanceZ, boolean generateAdditional)
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
		
		if (randomDebris.size() > 0 && rand.nextInt(118) == 0 && generateAdditional)
		{
			debris = randomDebris.get(rand.nextInt(randomDebris.size()));
			willGenerate = true;
		}
		
		if (willGenerate && debris != null)
		{
		}
		
		return willGenerate;
	}*/
}
