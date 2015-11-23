package genesis.portal;

import java.util.Map;

import genesis.metadata.EnumMenhirPart;
import net.minecraft.util.BlockPos;

public class MenhirEntry extends BlockPos implements Map.Entry<BlockPos, EnumMenhirPart>
{
	final EnumMenhirPart part;
	
	public MenhirEntry(BlockPos pos, EnumMenhirPart part)
	{
		super(pos.getX(), pos.getY(), pos.getZ());
		
		this.part = part;
	}
	
	@Override
	public BlockPos getKey()
	{
		return this;
	}
	
	@Override
	public EnumMenhirPart getValue()
	{
		return part;
	}
	
	@Override
	public EnumMenhirPart setValue(EnumMenhirPart value)
	{
		throw new UnsupportedOperationException("setValue");
	}
}