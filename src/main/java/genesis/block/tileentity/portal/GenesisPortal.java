package genesis.block.tileentity.portal;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;

import genesis.block.tileentity.portal.BlockMenhir.EnumGlyph;
import genesis.common.GenesisBlocks;
import genesis.metadata.EnumMenhirPart;
import genesis.util.SimpleIterator;
import genesis.util.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class GenesisPortal
{
	public static class MenhirIterator extends SimpleIterator<Pair<EnumMenhirPart, BlockPos>>
	{
		private final IBlockAccess world;
		private final Pair<EnumMenhirPart, BlockPos> start;
		private final int up;
		private final EnumFacing facing;
		
		public MenhirIterator(IBlockAccess world, BlockPos start, IBlockState startState, boolean up)
		{
			super(false);
			
			this.world = world;
			this.start = Pair.of(GenesisBlocks.menhirs.getVariant(startState), start);
			this.up = up ? 1 : -1;
			this.facing = BlockMenhir.getFacing(startState);
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
	}
	
	public static class MenhirIterable implements Iterable<Pair<EnumMenhirPart, BlockPos>>
	{
		private final IBlockAccess world;
		private final BlockPos start;
		private final IBlockState startState;
		private final boolean up;
		
		public MenhirIterable(IBlockAccess world, BlockPos start, IBlockState startState, boolean up)
		{
			this.world = world;
			this.start = start;
			this.startState = startState;
			this.up = up;
		}
		
		public MenhirIterable(IBlockAccess world, BlockPos start, boolean up)
		{
			this(world, start, null, up);
		}
		
		@Override
		public Iterator<Pair<EnumMenhirPart, BlockPos>> iterator()
		{
			return startState == null ? new MenhirIterator(world, start, up) : new MenhirIterator(world, start, startState, up);
		}
	}
	
	public static int MENHIR_MIN_DISTANCE = 1;
	public static int MENHIR_DEFAULT_DISTANCE = 3;
	public static int MENHIR_MAX_DISTANCE = 3;
	public static int PORTAL_HEIGHT = 3;
	
	public static class MenhirData
	{
		protected IBlockAccess world;
		protected BlockPos bottom;
		//protected IBlockState bottomState;
		protected BlockPos top;
		protected EnumFacing facing;
		protected BlockPos glyph;
		protected TileEntityMenhirGlyph glyphTE;
		protected BlockPos receptacle;
		protected TileEntityMenhirReceptacle receptacleTE;
		
		public MenhirData(IBlockAccess world, BlockPos pos, IBlockState state)
		{
			world = WorldUtils.getFakeWorld(world, new Function<BlockPos, IBlockState>()
			{
				@Override
				public IBlockState apply(BlockPos input)
				{
					if (pos.equals(input))
					{
						return state;
					}
					
					return null;
				}
			});
			this.world = world;
			
			this.facing = BlockMenhir.getFacing(state);
			
			bottom = pos;
			
			for (Pair<EnumMenhirPart, BlockPos> pair : new MenhirIterable(world, pos, false))
			{
				bottom = pair.getRight();
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
				for (Pair<EnumMenhirPart, BlockPos> pair : new MenhirIterable(world, bottom, true))
				{
					if (pair.getLeft() == EnumMenhirPart.RECEPTACLE)
					{
						receptacle = pair.getRight();
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
				for (Pair<EnumMenhirPart, BlockPos> pair : new MenhirIterable(world, bottom, true))
				{
					top = pair.getRight();
				}
			}
			
			return top;
		}
		
		public String toString()
		{
			return "menhir[at: " + getBottomPos() + ", facing: " + getFacing() + ", glyph: " + getGlyph() + ", receptacle: " + getReceptaclePos() + ", active: " + isReceptacleActive() + ", item: " + getReceptacleItem() + "]";
		}
	}
	
	protected final IBlockAccess world;
	protected BlockPos center;
	protected BlockPos portal;
	protected final Map<EnumFacing, MenhirData> menhirs = new EnumMap<EnumFacing, MenhirData>(EnumFacing.class);
	protected final Map<EnumFacing, MenhirData> menhirsView = Collections.unmodifiableMap(menhirs);
	
	/**
	 * Automatically generates data about a portal from the position of a menhir or the center of the portal.
	 */
	public GenesisPortal(IBlockAccess world, BlockPos pos)
	{
		this.world = world;
		
		IBlockState state = world.getBlockState(pos);
		
		if (GenesisBlocks.menhirs.hasState(state))
		{
			BlockPos menhirCenter = findCenterFromMenhir(pos, state);
			center = menhirCenter == null ? pos : menhirCenter;
		}
		else
		{
			center = pos.down(PORTAL_HEIGHT);
		}
		
		refresh();
	}
	
	/**
	 * Automatically generates data about a portal from the position of a menhir or the center of the portal.
	 */
	public GenesisPortal(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		this.world = world;
		BlockPos menhirCenter = findCenterFromMenhir(pos, state);
		center = menhirCenter == null ? pos : menhirCenter;
		refresh();
	}
	
	protected BlockPos findCenterFromMenhir(BlockPos pos, IBlockState state)
	{
		/*BlockPos below = pos.down();
		IBlockState belowState = world.getBlockState(below);
		
		if (GenesisBlocks.menhirs.hasState(belowState) && BlockMenhir.getFacing(belowState) == state)
		{
			pos = new MenhirData(world, below, state).getBottomPos();
		}*/
		
		MenhirData startMenhir = new MenhirData(world, pos, state);
		pos = startMenhir.getBottomPos();
		EnumFacing facing = startMenhir.getFacing();
		
		BlockPos centerPos = null;
		
		perpendicular:
		for (int forward = MENHIR_MIN_DISTANCE; forward <= MENHIR_MAX_DISTANCE; forward++)
		{
			BlockPos curCenter = pos.offset(facing, forward);
			
			for (int side = MENHIR_MIN_DISTANCE; side <= MENHIR_MAX_DISTANCE; side++)
			{
				for (int mult = -1; mult <= 1; mult += 2)
				{
					BlockPos checkPos = curCenter.offset(facing.rotateY(), side * mult);
					IBlockState checkState = world.getBlockState(checkPos);
					
					if (GenesisBlocks.menhirs.hasState(checkState) && BlockMenhir.getFacing(checkState) == facing.rotateYCCW())
					{
						centerPos = curCenter;
						break perpendicular;
					}
				}
			}
		}
		
		if (centerPos == pos)
		{
			for (int forward = MENHIR_MIN_DISTANCE * 2; forward <= MENHIR_MAX_DISTANCE * 2; forward++)
			{
				BlockPos checkPos = pos.offset(facing, forward);
				IBlockState checkState = world.getBlockState(checkPos);
				
				if (GenesisBlocks.menhirs.hasState(checkState) && BlockMenhir.getFacing(checkState) == facing.getOpposite())
				{
					centerPos = pos.offset(facing, forward / 2);
				}
			}
		}
		
		return centerPos;
	}
	
	public void refresh()
	{
		portal = null;
		menhirs.clear();
	}
	
	public BlockPos getCenterPosition()
	{
		return center;
	}
	
	public BlockPos getPortalPosition()
	{
		return portal == null ? center.up(PORTAL_HEIGHT) : portal;
	}
	
	protected void activatePortal(World world)
	{
		if (world.getBlockState(getPortalPosition()).getBlock().isReplaceable(world, getPortalPosition()))
		{
			world.setBlockState(getPortalPosition(), GenesisBlocks.portal.getDefaultState());
		}
	}
	
	protected void deactivatePortal(World world)
	{
		if (world.getBlockState(getPortalPosition()).getBlock() == GenesisBlocks.portal)
		{
			world.setBlockToAir(getPortalPosition());
		}
	}
	
	public void updatePortalStatus(World world)
	{
		Set<EnumGlyph> glyphs = EnumSet.noneOf(EnumGlyph.class);
		
		for (MenhirData menhir : getMenhirs().values())
		{
			if (menhir != null && menhir.isReceptacleActive())
			{
				glyphs.add(menhir.getGlyph());
			}
		}
		
		if (glyphs.size() == 4 && !glyphs.contains(EnumGlyph.NONE))
		{
			activatePortal(world);
		}
		else
		{
			deactivatePortal(world);
		}
	}
	
	public Map<EnumFacing, MenhirData> getMenhirs()
	{
		for (EnumFacing direction : EnumFacing.HORIZONTALS)
		{
			getMenhir(direction);
		}
		
		return Collections.unmodifiableMap(menhirs);
	}
	
	public MenhirData getMenhir(EnumFacing direction)
	{
		if (!menhirs.containsKey(direction))
		{
			menhirs.put(direction, null);
			
			for (int forward = MENHIR_MIN_DISTANCE; forward <= MENHIR_MAX_DISTANCE; forward++)
			{
				BlockPos checkPos = center.offset(direction, forward);
				IBlockState checkState = world.getBlockState(checkPos);
				
				if (GenesisBlocks.menhirs.hasState(checkState) && BlockMenhir.getFacing(checkState) == direction.getOpposite())
				{
					menhirs.put(direction, new MenhirData(world, checkPos));
					break;
				}
			}
		}
		
		return menhirs.get(direction);
	}
	
	public int getDistance(EnumFacing direction)
	{
		if (getMenhir(direction) == null)
		{
			return -1;
		}
		
		BlockPos off = getMenhir(direction).getBottomPos().subtract(getCenterPosition());
		return direction.getFrontOffsetX() * off.getX() + direction.getFrontOffsetY() * off.getY() + direction.getFrontOffsetZ() * off.getZ();
	}
	
	public void duplicatePortal(World newWorld, BlockPos newCenter)
	{
		BlockPos posDiff = newCenter.subtract(center);
		
		for (MenhirData menhir : getMenhirs().values())
		{
			for (Pair<EnumMenhirPart, BlockPos> from : new MenhirIterable(world, menhir.getBottomPos(), true))
			{
				BlockPos newPos = from.getRight().add(posDiff);
				newWorld.setBlockState(newPos, world.getBlockState(from.getRight()));
				TileEntity te = world.getTileEntity(from.getRight());
				
				if (te != null)
				{
					NBTTagCompound compound = new NBTTagCompound();
					te.writeToNBT(compound);
					newWorld.setTileEntity(newPos, TileEntity.createAndLoadEntity(compound));
				}
			}
		}
		
		new GenesisPortal(newWorld, newCenter.up(PORTAL_HEIGHT)).updatePortalStatus(newWorld);
	}
	
	public String toString()
	{
		return "portal at " + getCenterPosition() + " with menhirs " + getMenhirs();
	}
}
