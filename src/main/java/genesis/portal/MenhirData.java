package genesis.portal;

import java.util.Iterator;

import genesis.block.tileentity.portal.BlockMenhir;
import genesis.block.tileentity.portal.EnumGlyph;
import genesis.block.tileentity.portal.TileEntityMenhirGlyph;
import genesis.block.tileentity.portal.TileEntityMenhirReceptacle;
import genesis.combo.variant.EnumMenhirPart;
import genesis.common.GenesisBlocks;
import genesis.util.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class MenhirData implements Iterable<MenhirEntry>
{
	protected IBlockAccess world;
	protected BlockPos bottom;
	protected BlockPos top;
	protected EnumFacing facing;
	protected BlockPos glyph;
	protected TileEntityMenhirGlyph glyphTE;
	protected BlockPos receptacle;
	protected TileEntityMenhirReceptacle receptacleTE;
	
	public MenhirData(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		world = WorldUtils.getFakeWorld(world, pos, state);
		this.world = world;
		
		this.facing = BlockMenhir.getFacing(state);
		
		bottom = pos;
		
		for (MenhirEntry pair : new MenhirIterator(world, pos, false))
		{
			bottom = pair;
		}
		
		refresh();
	}
	
	public MenhirData(IBlockAccess world, BlockPos pos)
	{
		this(world, pos, world.getBlockState(pos));
	}
	
	public void refresh()
	{
		top = null;
		glyph = null;
		glyphTE = null;
		receptacle = null;
		receptacleTE = null;
	}
	
	public BlockPos getBottomPos()
	{
		return bottom;
	}
	
	public EnumFacing getFacing()
	{
		return facing;
	}
	
	public BlockPos getGlyphPos()
	{
		if (glyph == null && GenesisBlocks.menhirs.getVariant(world.getBlockState(bottom)) == EnumMenhirPart.GLYPH)
		{
			glyph = bottom;
		}
		
		return glyph;
	}
	
	public TileEntityMenhirGlyph getGlyphTE()
	{
		return glyphTE == null && getGlyphPos() != null ? glyphTE = BlockMenhir.getGlyphTileEntity(world, getGlyphPos()) : glyphTE;
	}
	
	public EnumGlyph getGlyph()
	{
		return getGlyphTE() == null ? EnumGlyph.NONE : getGlyphTE().getGlyph();
	}
	
	public BlockPos getReceptaclePos()
	{
		if (receptacle == null)
		{
			for (MenhirEntry pair : new MenhirIterator(world, bottom, true))
			{
				if (pair.getValue() == EnumMenhirPart.RECEPTACLE)
				{
					receptacle = pair;
					break;
				}
			}
		}
		
		return receptacle;
	}
	
	public TileEntityMenhirReceptacle getReceptacleTE()
	{
		return receptacleTE == null && getReceptaclePos() != null ? receptacleTE = BlockMenhir.getReceptacleTileEntity(world, getReceptaclePos()) : receptacleTE;
	}
	
	public ItemStack getReceptacleItem()
	{
		return getReceptacleTE() == null ? null : getReceptacleTE().getReceptacleItem();
	}
	
	public boolean isReceptacleActive()
	{
		return getReceptacleTE() == null ? false : getReceptacleTE().isReceptacleActive();
	}
	
	public BlockPos getTop()
	{
		if (top == null)
		{
			for (MenhirEntry pair : new MenhirIterator(world, bottom, true))
			{
				top = pair;
			}
		}
		
		return top;
	}
	
	@Override
	public Iterator<MenhirEntry> iterator()
	{
		return new MenhirIterator(world, getBottomPos(), true);
	}
	
	@Override
	public String toString()
	{
		return "menhir[at: " + getBottomPos() + ", facing: " + getFacing() + ", glyph: " + getGlyph() + ", receptacle: " + getReceptaclePos() + ", active: " + isReceptacleActive() + ", item: " + getReceptacleItem() + "]";
	}
}