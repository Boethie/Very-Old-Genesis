package genesis.world.biome.decorate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

public class WorldGenDung extends WorldGenDecorationBase
{
	private final EnumDung dungType;
	private List<Block> allowedBlocks = new ArrayList<Block>();
	
	public WorldGenDung(EnumDung type)
	{
		super(WorldBlockMatcher.STANDARD_AIR_WATER,
				(s, w, p) -> s.getBlock() == Blocks.dirt
				|| s.getBlock() == Blocks.grass
				|| s.getBlock() == GenesisBlocks.moss
				|| GenesisBlocks.silt.isStateOf(s, SiltBlocks.SILT));
		
		dungType = type;
		
		allowedBlocks.add(Blocks.dirt);
		allowedBlocks.add(Blocks.grass);
		allowedBlocks.add(GenesisBlocks.moss);
		allowedBlocks.add(GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.SILT));
	}
	
	@Override
	public boolean place(World world, Random rand, BlockPos pos)
	{
		if (!allowedBlocks.contains(world.getBlockState(pos.down()).getBlock()))
			return false;
		
		int maxHeight = 4 + rand.nextInt(16);
		
		BlockPos curPos = pos;
		
		placeColumn(maxHeight, curPos, world, rand);
		
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
					h -= (1 + rand.nextInt(7));
					if (h > 0)
					{
						curPos = pos.add(i * x, 0, i * z);
						if (
								allowedBlocks.contains(world.getBlockState(curPos.down()).getBlock())
								&& world.isAirBlock(curPos))
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
		
		return true;
	}
	
	private void placeColumn(int maxHeight, BlockPos curPos, World world, Random rand)
	{
		int wholeBlocks = (int)MathHelper.floor_float((float)maxHeight / 8.0F);
		
		checkAndPlaceBase(maxHeight, curPos.north(), world, rand);
		checkAndPlaceBase(maxHeight, curPos.south(), world, rand);
		checkAndPlaceBase(maxHeight, curPos.east(), world, rand);
		checkAndPlaceBase(maxHeight, curPos.west(), world, rand);
		
		for (int i = 0; i < wholeBlocks; ++i)
		{
			this.setBlock(world, curPos, GenesisBlocks.dungs.getBlockState(DungBlocksAndItems.DUNG_BLOCK, dungType).withProperty(BlockDung.HEIGHT, BlockDung.MAX_HEIGHT));
			curPos = curPos.up();
		}
		
		if (maxHeight - (wholeBlocks * 8) > 0)
			this.setBlock(world, curPos, GenesisBlocks.dungs.getBlockState(DungBlocksAndItems.DUNG_BLOCK, dungType).withProperty(BlockDung.HEIGHT, maxHeight - (wholeBlocks * 8)));
	}
	
	private void checkAndPlaceBase(int maxHeight, BlockPos curPos, World world, Random rand)
	{
		if (
				allowedBlocks.contains(world.getBlockState(curPos.down()).getBlock())
				&& world.isAirBlock(curPos)
				&& rand.nextInt(6) == 0)
		{
			int splatHeight = rand.nextInt(maxHeight > 6 ? 6 : maxHeight);
			
			if (splatHeight > 0)
				this.setBlock(world, curPos, GenesisBlocks.dungs.getBlockState(DungBlocksAndItems.DUNG_BLOCK, dungType).withProperty(BlockDung.HEIGHT, splatHeight));
		}
	}
}
