package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public abstract class WorldGenTreeBase extends WorldGenAbstractTree {

	//protected BlockAndMeta leaves;
	//protected BlockAndMeta wood;
	protected int type;
	protected int minHeight;
	protected int maxHeight;
	protected boolean notifyFlag;
	protected World world;
	protected Random random;
	public IBlockState wood;
	public IBlockState leaves;

	// this array is the 8 directions of x and y, used for palm trees.
	protected int[][] directions = {{1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}};

	/**
	 * Constructor - sets up tree variables
	 *
	 * @param leaves block and metadata for leaves blocks.
	 * @param wood   block and metadata for wood blocks.
	 * @param notify whether or not to notify blocks of the tree being grown.
	 *               Generally false for world generation, true for saplings.
	 */
	public WorldGenTreeBase(IBlockState wood, IBlockState leaves, boolean notify) {
		super(notify);
		this.leaves = leaves;
		this.wood = wood;
		notifyFlag = notify;
	}

	@Override
	public abstract boolean generate(World world, Random random, BlockPos pos);

	// UTILITY GENERATORS - LEAVES, BRANCHES, TRUNKS

	// generates a circular disk of leaves around a coordinate block, only
	// overwriting air blocks.
	protected void generateLeafLayerCircle(World world, Random random, double radius, BlockPos pos) {
		for (int x = (int) Math.ceil(pos.getX() - radius); x <= (int) Math.ceil(pos.getX() + radius); x++) {
			for (int z = (int) Math.ceil(pos.getZ() - radius); z <= (int) Math.ceil(pos.getZ() + radius); z++) {
				double xfr = z - pos.getZ();
				double zfr = x - pos.getX();

				if (xfr * xfr + zfr * zfr <= radius * radius) {
					setBlockInWorld(new BlockPos(x, pos.getY(), z), leaves);
				}
			}
		}
	}

	// generates a circular disk of leaves around a coordinate block, only
	// overwriting air blocks.
	// noise means the outer block has a 50% chance of generating
	protected void generateLeafLayerCircleNoise(World world, Random random, double radius, BlockPos pos) {
		for (int x = (int) Math.ceil(pos.getX() - radius); x <= (int) Math.ceil(pos.getX() + radius); x++) {
			for (int z = (int) Math.ceil(pos.getZ() - radius); z <= (int) Math.ceil(pos.getZ() + radius); z++) {
				double xfr = z - pos.getZ();
				double zfr = x - pos.getX();

				if (xfr * xfr + zfr * zfr <= radius * radius) {
					if (xfr * xfr + zfr * zfr <= (radius - 1) * (radius - 1) || random.nextInt(2) == 0) {
						setBlockInWorld(new BlockPos(x, pos.getY(), z), leaves);
					}
				}
			}
		}
	}

	// generates a circular disk of wood around a coordinate block, only
	// overwriting air and leaf blocks.
	protected void generateWoodLayerCircle(World world, Random random, double radius, BlockPos pos) {
		for (int x = (int) Math.ceil(pos.getX() - radius); x <= (int) Math.ceil(pos.getX() + radius); x++) {
			for (int z = (int) Math.ceil(pos.getZ() - radius); z <= (int) Math.ceil(pos.getZ() + radius); z++) {
				double xfr = z - pos.getZ();
				double zfr = x - pos.getX();

				if (xfr * xfr + zfr * zfr <= radius * radius) {
					setBlockInWorld(new BlockPos(x, pos.getY(), z), wood);
				}
			}
		}
	}

	// generate a branch, can be any direction
	// startHeight is absolute, not relative to the tree.
	// dir = direction: 0 = north (+z) 1 = east (+x) 2 = south 3 = west
	protected BlockPos generateStraightBranch(World world, Random random, int length, BlockPos pos, int dir) {
		int direction = -1;
		if (dir < 2) {
			direction = 1;
		}
		if (dir % 2 == 0) {
			// generates branch
			for (int i = 1; i <= length; i++) {
				setBlockInWorld(pos.add(i*direction, i, 0), wood.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X)/*wood.block, GenesisTreeBlocks.getLogMetadataForDirection(wood.metadata, ForgeDirection.NORTH)*/);
			}
			return pos.add(length * direction, length, 0);
		} else {
			for (int i = 1; i <= length; i++) {
				setBlockInWorld(pos.add(0, i, i * direction), wood.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z)/*wood.block, GenesisTreeBlocks.getLogMetadataForDirection(wood.metadata, ForgeDirection.EAST)*/);
			}
			return pos.add(0, length, length * direction);
		}
	}

	// same as GenerateStraightBranch but downward (for Arau tree)
	protected BlockPos generateStraightBranchDown(World world, Random random, int length, BlockPos pos, int dir) {
		int direction = -1;
		if (dir < 2) {
			direction = 1;
		}
		if (dir % 2 == 0) {
			// generates branch
			for (int i = 1; i <= length; i++) {
				setBlockInWorld(pos.add(i * direction, -i, 0), wood.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X)/*wood.block, GenesisTreeBlocks.getLogMetadataForDirection(wood.metadata, ForgeDirection.NORTH)*/);
			}
			return pos.add(length * direction, -length, 0);
		} else {
			for (int i = 1; i <= length; i++) {
				setBlockInWorld(pos.add(0, -i, i * direction), wood.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z)/*wood.block, GenesisTreeBlocks.getLogMetadataForDirection(wood.metadata, ForgeDirection.EAST)*/);
			}
			return pos.add(0, -length, length * direction);
		}
	}

	protected void setBlockInWorld(BlockPos pos, IBlockState blockState) {
		try {
			if (blockState.getBlock() == wood.getBlock() && (world.isAirBlock(pos) || world.getBlockState(pos).getBlock().getMaterial().isReplaceable() || world.getBlockState(pos).getBlock().isLeaves(world, pos))) {
				if (notifyFlag) {
					world.setBlockState(pos, blockState, 3);
				} else {
					world.setBlockState(pos, blockState, 2);
				}
			} else if (blockState.getBlock() == leaves.getBlock() && world.isAirBlock(pos)) {
				if (notifyFlag) {
					world.setBlockState(pos, blockState, 3);
				} else {
					world.setBlockState(pos, blockState, 2);
				}
			}
		} catch (RuntimeException e) {
			if (e.getMessage().equals("Already decorating!!")) {
				System.out.println("Error: Tree block couldn't generate!");
			}
		}
	}

	/**
	 * Returns true if there are no blocks except for air and leaves in the given area.
	 * 
	 * @param pos The middle position
	 * @param radius The distance in the x and z directions
	 * @param height The height
	 */
	protected boolean isCubeClear(BlockPos pos, int radius, int height) {
		try {
			for (int i = pos.getX() - radius; i <= pos.getX() + radius; i++) {
				for (int j = pos.getZ() - radius; j <= pos.getZ() + radius; j++) {
					for (int k = pos.getY(); k <= pos.getY() + height; k++) {
						if (k >= 0 && k < 256) {
							BlockPos thisPos = new BlockPos(i, k ,j);
							Block block = world.getBlockState(thisPos).getBlock();

							if (!block.isAir(world, thisPos) && !block.isLeaves(world, thisPos))
							{
								return false;
							}
						}
						else
						{
							return false;
						}
					}
				}
			}

			return true;
		}
		catch(RuntimeException e) {
			if (e.getMessage().equals("Already decorating!!")) {
				System.out.println("Error while checking if cube is clear!");
				e.printStackTrace();
			}
		}

		return false;

	}

	/**
	 * Returns true if there are no blocks except for air, water, and leaves in the given area.
	 * 
	 * @param pos The middle position
	 * @param radius The distance in the x and z directions
	 * @param height The height
	 */
	protected boolean isCubeClearWater(BlockPos pos, int radius, int height) {
		try {
			for (int i = pos.getX() - radius; i <= pos.getX() + radius; i++) {
				for (int j = pos.getZ() - radius; j <= pos.getZ() + radius; j++) {
					for (int k = pos.getY(); k <= pos.getY() + height; k++) {
						if (k >= 0 && k < 256) {
							BlockPos thisPos = new BlockPos(i, k ,j);
							Block block = world.getBlockState(thisPos).getBlock();

							if (!block.isAir(world, thisPos) && !block.isLeaves(world, thisPos) && block.getMaterial() != Material.water)
							{
								return false;
							}
						}
						else
						{
							return false;
						}
					}
				}
			}

			return true;
		}
		catch(RuntimeException e) {
			if (e.getMessage().equals("Already decorating!!")) {
				System.out.println("Error while checking if cube is clear!");
				e.printStackTrace();
			}
		}

		return false;
	}

	// finds top block for the given x,z position (excluding leaves)
	protected int findTopBlock(int x, int z) {
		BlockPos pos = new BlockPos(x, 0, z);
		int y = 256;
		for (boolean var6 = false; (world.isAirBlock(pos.add(0, y, 0)) || world.getBlockState(pos.add(0, y, 0)).getBlock().isLeaves(world, pos.add(0, y, 0))) && y > 0; --y) {
			;
		}
		return y;
	}

	//Returns the block direction, for the block state. (Copied from the WorldGenBigTree class)
	private BlockLog.EnumAxis func_175938_b(BlockPos p_175938_1_, BlockPos p_175938_2_)
	{
		BlockLog.EnumAxis enumaxis = BlockLog.EnumAxis.Y;
		int i = Math.abs(p_175938_2_.getX() - p_175938_1_.getX());
		int j = Math.abs(p_175938_2_.getZ() - p_175938_1_.getZ());
		int k = Math.max(i, j);

		if (k > 0)
		{
			if (i == k)
			{
				enumaxis = BlockLog.EnumAxis.X;
			}
			else if (j == k)
			{
				enumaxis = BlockLog.EnumAxis.Z;
			}
		}

		return enumaxis;
	}

	//Added by Tmtravlr; TODO: we should probably make this depend on the sapling for the tree...
	public boolean canTreeGrow(Block block) {
		return block == Blocks.dirt || block == GenesisBlocks.moss;
	}
}
