package genesis.block.tileentity.portal;

import java.util.Iterator;

import org.apache.commons.lang3.tuple.Pair;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumMenhirPart;
import genesis.util.SimpleIterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class MenhirIterator extends SimpleIterator<Pair<EnumMenhirPart, BlockPos>> implements Iterable<Pair<EnumMenhirPart, BlockPos>>
{
	protected final IBlockAccess world;
	protected final Pair<EnumMenhirPart, BlockPos> start;
	protected final EnumFacing facing;
	protected final int up;
	
	protected MenhirIterator(IBlockAccess world, Pair<EnumMenhirPart, BlockPos> start, EnumFacing facing, int up)
	{
		super(false);
		
		this.world = world;
		this.start = start;
		this.up = up;
		this.facing = facing;
	}
	
	public MenhirIterator(IBlockAccess world, BlockPos start, IBlockState startState, boolean up)
	{
		this(world, Pair.of(GenesisBlocks.menhirs.getVariant(startState), start), BlockMenhir.getFacing(startState), up ? 1 : -1);
	}
	
	public MenhirIterator(IBlockAccess world, BlockPos start, boolean up)
	{
		this(world, start, world.getBlockState(start), up);
	}
	
	@Override
	protected Pair<EnumMenhirPart, BlockPos> computeNext()
	{
		if (getCurrent() == null)
		{
			return start;
		}
		
		EnumMenhirPart curPart = getCurrent().getLeft();
		
		BlockPos checkPos = getCurrent().getRight().up(up);
		IBlockState checkState = world.getBlockState(checkPos);
		EnumMenhirPart checkPart = GenesisBlocks.menhirs.getVariant(checkState);
		
		if (checkPart != null &&
			BlockMenhir.getFacing(checkState) == facing &&
			((checkPart == curPart && curPart.canStack()) || checkPart == getCurrent().getLeft().getOffset(up)))
		{
			return Pair.of(checkPart, checkPos);
		}
		
		return null;
	}
	
	@Override
	public Iterator<Pair<EnumMenhirPart, BlockPos>> iterator()
	{
		return new MenhirIterator(world, start, facing, up);
	}
}
