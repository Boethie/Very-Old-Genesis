package genesis.world.biome.decorate;

import genesis.block.BlockDung;
import genesis.combo.DungBlocksAndItems;
import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumDung;
import genesis.combo.variant.EnumSilt;
import genesis.common.GenesisBlocks;
import genesis.util.functional.WorldBlockMatcher;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenDung extends WorldGenDecorationBase
{
	private final EnumDung dungType;
	private List<Block> allowedBlocks = new ArrayList<>();

	public WorldGenDung(EnumDung type)
	{
		super(WorldBlockMatcher.STANDARD_AIR_WATER,
				(s, w, p) -> s.getBlock() == Blocks.DIRT
				|| s.getBlock() == Blocks.GRASS
				|| s.getBlock() == GenesisBlocks.MOSS
				|| GenesisBlocks.SILT.isStateOf(s, SiltBlocks.SILT));

		dungType = type;

		allowedBlocks.add(Blocks.DIRT);
		allowedBlocks.add(Blocks.GRASS);
		allowedBlocks.add(GenesisBlocks.MOSS);
		allowedBlocks.add(GenesisBlocks.SILT.getBlock(SiltBlocks.SILT, EnumSilt.SILT));
	}

	@Override
	public boolean place(World world, Random rand, BlockPos pos)
	{
		if (!allowedBlocks.contains(world.getBlockState(pos.down()).getBlock()))
			return false;

		if (!world.isAirBlock(pos))
			return false;

		int maxHeight = 7 + rand.nextInt(3);

		BlockPos curPos = pos;

		placeColumn(maxHeight, curPos, world, rand, false);

		for (int x = -1; x <= 1; ++x)
		{
			for (int z = -1; z <= 1; ++z)
			{
				if (x == 0 && z == 0)
					break;

				int spreadLength = 1;
				int h = maxHeight;

				for (int i = 1; i <= spreadLength; ++i)
				{
					h -= (2 + rand.nextInt(3));
					if (h > 0)
					{
						curPos = pos.add(i * x, 0, i * z);
						if (
								allowedBlocks.contains(world.getBlockState(curPos.down()).getBlock())
								&& (world.isAirBlock(curPos) || GenesisBlocks.DUNGS.isStateOf(world.getBlockState(curPos), dungType)))
						{
							placeColumn(h, curPos, world, rand);
						}
						else
						{
							break;
						}
					}
				}
			}
		}

		int h = maxHeight - (2 + rand.nextInt(3));
		placeColumn(h, pos.south(), world, rand);

		return true;
	}

	private void placeColumn(int maxHeight, BlockPos curPos, World world, Random rand)
	{
		this.placeColumn(maxHeight, curPos, world, rand, true);
	}

	private void placeColumn(int maxHeight, BlockPos curPos, World world, Random rand, boolean placeBase)
	{
		int wholeBlocks = MathHelper.floor_float((float)maxHeight / 8.0F);

		if (placeBase)
		{
			checkAndPlaceBase(maxHeight, curPos.north(), world, rand);
			checkAndPlaceBase(maxHeight, curPos.south(), world, rand);
			checkAndPlaceBase(maxHeight, curPos.east(), world, rand);
			checkAndPlaceBase(maxHeight, curPos.west(), world, rand);
		}

		for (int i = 0; i < wholeBlocks; ++i)
		{
			this.setBlock(world, curPos, GenesisBlocks.DUNGS.getBlockState(DungBlocksAndItems.DUNG_BLOCK, dungType).withProperty(BlockDung.HEIGHT, BlockDung.MAX_HEIGHT));
			curPos = curPos.up();
		}

		if (maxHeight - (wholeBlocks * 8) > 0)
			this.setBlock(world, curPos, GenesisBlocks.DUNGS.getBlockState(DungBlocksAndItems.DUNG_BLOCK, dungType).withProperty(BlockDung.HEIGHT, maxHeight - (wholeBlocks * 8)));
	}

	private void checkAndPlaceBase(int maxHeight, BlockPos curPos, World world, Random rand)
	{
		if (
				allowedBlocks.contains(world.getBlockState(curPos.down()).getBlock())
				&& world.isAirBlock(curPos)
				&& rand.nextInt(3) == 0)
		{
			int splatHeight = 1 + rand.nextInt(maxHeight > 2 ? 2 : maxHeight);

			if (splatHeight > 0)
				this.setBlock(world, curPos, GenesisBlocks.DUNGS.getBlockState(DungBlocksAndItems.DUNG_BLOCK, dungType).withProperty(BlockDung.HEIGHT, splatHeight));
		}
	}
}
