package genesis.block.tileentity.portal;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import genesis.block.tileentity.portal.BlockMenhir.EnumGlyph;
import genesis.common.Genesis;
import genesis.common.GenesisBlocks;
import genesis.metadata.EnumMenhirPart;
import genesis.util.SimpleIterator;
import genesis.util.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.EnumFacing.AxisDirection;
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
	
	// Values for external things
	public static final byte PORTAL_CHECK_TIME = 5;
	
	public static class MenhirData
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
	
	public static boolean isBlockingPortal(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return GenesisBlocks.menhirs.hasState(state) || state.getBlock().getLightOpacity(world, pos) >= 15;
	}
	
	public static boolean isBlockingPortal(IBlockAccess world, BlockPos pos)
	{
		return isBlockingPortal(world, pos, world.getBlockState(pos));
	}
	
	protected final IBlockAccess world;
	protected final BlockPos center;
	protected BlockPos portal;
	protected final Map<EnumFacing, MenhirData> menhirs = new EnumMap<EnumFacing, MenhirData>(EnumFacing.class);
	protected final Map<EnumFacing, MenhirData> menhirsView = Collections.unmodifiableMap(menhirs);
	
	public static GenesisPortal fromPortalBlock(IBlockAccess world, BlockPos pos)
	{
		return new GenesisPortal(world, pos.down(PORTAL_HEIGHT));
	}
	
	public static GenesisPortal fromCenterBlock(IBlockAccess world, BlockPos pos)
	{
		return new GenesisPortal(world, pos);
	}
	
	public static GenesisPortal fromMenhirBlock(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return new GenesisPortal(world, pos, state);
	}
	
	public static GenesisPortal fromMenhirBlock(IBlockAccess world, BlockPos pos)
	{
		return fromMenhirBlock(world, pos, null);
	}
	
	/**
	 * Provides data about a portal from the position of the center of the portal.
	 */
	protected GenesisPortal(IBlockAccess world, BlockPos center)
	{
		this.world = world;
		this.center = center;
		refresh();
	}
	
	/**
	 * Provides data about a portal from the position of a menhir.
	 */
	protected GenesisPortal(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		this.world = WorldUtils.getFakeWorld(world, pos, state);
		BlockPos menhirCenter = findCenterFromMenhir(pos, state);
		center = menhirCenter == null ? pos : menhirCenter;
		refresh();
	}
	
	protected boolean isValidMenhir(BlockPos pos, IBlockState state, EnumFacing facing, int y)
	{
		if (GenesisBlocks.menhirs.hasState(state) && BlockMenhir.getFacing(state) == facing)
		{
			if (y == new MenhirData(world, pos).getBottomPos().getY())
			{
				return true;
			}
		}
		
		return false;
	}
	
	protected boolean isValidMenhir(BlockPos pos, IBlockState state, EnumFacing facing)
	{
		return isValidMenhir(pos, state, facing, getCenterPosition().getY());
	}
	
	protected BlockPos findCenterFromMenhir(BlockPos pos, IBlockState state)
	{
		MenhirData startMenhir = new MenhirData(world, pos);
		pos = startMenhir.getBottomPos();
		EnumFacing facing = startMenhir.getFacing();
		
		BlockPos centerPos = pos;
		int y = centerPos.getY();
		
		forward:
		for (int forward = 1; forward <= MENHIR_MAX_DISTANCE; forward++)
		{
			BlockPos curCenter = pos.offset(facing, forward);
			
			if (isBlockingPortal(world, curCenter))
			{
				break forward;
			}
			else if (forward >= MENHIR_MIN_DISTANCE)
			{
				for (AxisDirection direction : AxisDirection.values())
				{
					for (int side = 0; side <= MENHIR_MAX_DISTANCE; side++)
					{
						BlockPos checkPos = curCenter.offset(facing.rotateY(), side * direction.getOffset());
						IBlockState checkState = world.getBlockState(checkPos);

						if (side >= MENHIR_MIN_DISTANCE && isValidMenhir(checkPos, checkState, facing.rotateYCCW(), y))
						{
							centerPos = curCenter;
							break forward;
						}
						else if (isBlockingPortal(world, checkPos, checkState))
						{
							break;
						}
					}
				}
			}
		}
		
		if (centerPos == pos)
		{
			for (int forward = 0; forward <= MENHIR_MAX_DISTANCE * 2; forward++)
			{
				BlockPos checkPos = pos.offset(facing, forward);
				IBlockState checkState = world.getBlockState(checkPos);
				
				if (forward >= MENHIR_MIN_DISTANCE * 2 && isValidMenhir(checkPos, checkState, facing.getOpposite(), y))
				{
					centerPos = pos.offset(facing, forward / 2);
					break;
				}
				else if (isBlockingPortal(world, checkPos, checkState))
				{
					break;
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
	
	public boolean updatePortalStatus(World world)
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
			return true;
		}
		
		deactivatePortal(world);
		return false;
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
			
			for (int forward = 0; forward <= MENHIR_MAX_DISTANCE; forward++)
			{
				BlockPos checkPos = center.offset(direction, forward);
				IBlockState checkState = world.getBlockState(checkPos);
				
				if (forward >= MENHIR_MIN_DISTANCE && isValidMenhir(checkPos, checkState, direction.getOpposite()))
				{
					MenhirData menhir = new MenhirData(world, checkPos);
					menhirs.put(direction, menhir);
					break;
				}
				
				if (isBlockingPortal(world, checkPos, checkState))
				{
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
		Genesis.logger.info("Duplicating portal " + this + " to world " + newWorld + " at pos " + newCenter + ".");
		BlockPos posDiff = newCenter.subtract(center);
		
		for (MenhirData menhir : getMenhirs().values())
		{
			if (menhir != null)
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
						TileEntity newTE = TileEntity.createAndLoadEntity(compound);
						newWorld.removeTileEntity(newPos);
						newWorld.setTileEntity(newPos, newTE);
					}
				}
			}
		}
		
		GenesisPortal.fromCenterBlock(newWorld, newCenter).updatePortalStatus(newWorld);
	}
	
	public String toString()
	{
		return "portal at " + getCenterPosition() + " with menhirs " + getMenhirs();
	}
}
