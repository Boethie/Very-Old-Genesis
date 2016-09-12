package genesis.world.biome.decorate;

import java.util.*;

import genesis.combo.*;
import genesis.combo.variant.*;
import genesis.common.GenesisBlocks;
import genesis.util.functional.WorldBlockMatcher;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenDebris extends WorldGenDecorationBase
{
	private static final IBlockState[] EMPTY = new IBlockState[0];
	
	private final IBlockState[] debris;
	
	public WorldGenDebris(IBlockState... debris)
	{
		super(WorldBlockMatcher.STANDARD_AIR,
				(s, w, p) -> s.getBlock().isSideSolid(s, w, p, EnumFacing.UP));
		
		setPatchCount(8);
		
		this.debris = debris;
	}
	
	public WorldGenDebris(EnumDebrisOther... randomDebris)
	{
		this(Arrays.stream(randomDebris)
				.map(GenesisBlocks.DEBRIS::getBlockState)
				.toArray(IBlockState[]::new));
	}
	
	public WorldGenDebris()
	{
		this(EMPTY);
	}
	
	protected int hArea = 5;
	protected int vArea = 3;
	protected IBlockState[] states = null;
	
	private static void add(List<IBlockState> states, IBlockState state)
	{
		if (!states.contains(state))
			states.add(state);
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		if (debris.length == 0)
		{
			ArrayList<IBlockState> stateList = new ArrayList<>();
			
			for (BlockPos checkPos
					: BlockPos.getAllInBoxMutable(
							pos.add(-hArea, -vArea, -hArea),
							pos.add(hArea, vArea, hArea)))
			{
				IBlockState checkState = world.getBlockState(checkPos);
				
				if (checkState.getBlock() == GenesisBlocks.CALAMITES)
				{
					add(stateList, GenesisBlocks.DEBRIS.getBlockState(EnumDebrisOther.CALAMITES));
				}
				else
				{
					TreeBlocksAndItems.SubsetData data = GenesisBlocks.TREES.getSubsetData(checkState.getBlock());
					
					if (data != null)
					{
						EnumTree variant = checkState.getValue(data.variantProperty);
						
						if (variant.hasDebris())
							add(stateList, GenesisBlocks.DEBRIS.getBlockState(variant));
					}
				}
			}
			
			states = stateList.toArray(new IBlockState[0]);
		}
		else if (debris.length > 0)
		{
			states = new IBlockState[]{debris[rand.nextInt(debris.length)]};
		}
		
		boolean success = false;
		
		if (states != null && states.length > 0)
			success = super.generate(world, rand, pos);
		
		states = null;
		return success;
	}
	
	@Override
	public boolean place(World world, Random rand, BlockPos pos)
	{
		if (states == null)
			return generate(world, rand, pos);
		
		return setAirBlock(world, pos, states[rand.nextInt(states.length)]);
	}
	
	/*private boolean generateDebris(World world, Random rand, BlockPos pos, int distanceX, int distanceY, int distanceZ, boolean generateAdditional)
	{
		boolean willGenerate = false;
		
		if (!(world.getBlockState(pos).getBlock() == GenesisBlocks.moss || world.getBlockState(pos).getBlock() == Blocks.DIRT))
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
