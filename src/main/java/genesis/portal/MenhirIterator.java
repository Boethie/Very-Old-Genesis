package genesis.portal;

import java.util.Iterator;

import genesis.block.tileentity.portal.BlockMenhir;
import genesis.combo.variant.EnumMenhirPart;
import genesis.common.GenesisBlocks;
import genesis.util.SimpleIterator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class MenhirIterator extends SimpleIterator<MenhirEntry> implements Iterable<MenhirEntry>
{
	protected final IBlockAccess world;
	protected final MenhirEntry start;
	protected final EnumFacing facing;
	protected final int up;
	
	protected MenhirIterator(IBlockAccess world, MenhirEntry start, EnumFacing facing, int up)
	{
		super(false);
		
		this.world = world;
		this.start = start;
		this.up = up;
		this.facing = facing;
	}
	
	public MenhirIterator(IBlockAccess world, BlockPos start, IBlockState startState, boolean up)
	{
		this(world,
				GenesisBlocks.menhirs.containsState(startState) ? new MenhirEntry(start, GenesisBlocks.menhirs.getVariant(startState)) : null,
				BlockMenhir.getFacing(startState), up ? 1 : -1);
	}
	
	public MenhirIterator(IBlockAccess world, BlockPos start, boolean up)
	{
		this(world, start, world.getBlockState(start), up);
	}
	
	@Override
	protected MenhirEntry computeNext()
	{
		if (getCurrent() == null)
		{
			return start;
		}
		
		EnumMenhirPart curPart = getCurrent().getValue();
		
		BlockPos checkPos = getCurrent().up(up);
		IBlockState checkState = world.getBlockState(checkPos);
		EnumMenhirPart checkPart = GenesisBlocks.menhirs.getVariant(checkState);
		
		if (checkPart != null &&
			BlockMenhir.getFacing(checkState) == facing &&
			((checkPart == curPart && curPart.canStack()) || checkPart == getCurrent().getValue().getOffset(up)))
		{
			return new MenhirEntry(checkPos, checkPart);
		}
		
		return null;
	}
	
	@Override
	public Iterator<MenhirEntry> iterator()
	{
		return new MenhirIterator(world, start, facing, up);
	}
}
