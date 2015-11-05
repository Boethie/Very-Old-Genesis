package genesis.block.tileentity.portal;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import genesis.block.tileentity.portal.BlockMenhir.EnumGlyph;
import genesis.common.Genesis;
import genesis.common.GenesisBlocks;
import genesis.metadata.EnumMenhirPart;
import genesis.util.SimpleIterator;
import genesis.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.world.*;

public class GenesisPortal
{
	public static final int MENHIR_MIN_DISTANCE = 1;
	public static final int MENHIR_DEFAULT_DISTANCE = 3;
	public static final int MENHIR_MAX_DISTANCE = 3;
	public static final int PORTAL_HEIGHT = 3;
	
	// Values for external things
	public static final byte PORTAL_CHECK_TIME = 5;
	
	public static boolean isBlockingPortal(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return GenesisBlocks.menhirs.hasState(state) || state.getBlock().getLightOpacity(world, pos) >= 15;
	}
	
	public static boolean isBlockingPortal(IBlockAccess world, BlockPos pos)
	{
		return isBlockingPortal(world, pos, world.getBlockState(pos));
	}
	
	protected final IBlockAccess world;
	protected BlockPos center;
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
	
	public IBlockAccess getWorld()
	{
		return world;
	}
	
	public BlockPos getCenterPosition()
	{
		return center;
	}
	
	public void setCenterPosition(BlockPos pos)
	{
		center = pos;
		refresh();
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
	
	public void placeMenhir(World world, BlockPos pos, EnumFacing facing, EnumGlyph glyph)
	{
		world.setBlockState(pos, GenesisBlocks.menhirs.getBlockState(EnumMenhirPart.GLYPH).withProperty(BlockMenhir.FACING, facing));
		BlockMenhir.getGlyphTileEntity(world, pos).setGlyph(glyph);
		world.setBlockState(pos = pos.up(), GenesisBlocks.menhirs.getBlockState(EnumMenhirPart.RECEPTACLE).withProperty(BlockMenhir.FACING, facing));
		BlockMenhir.getReceptacleTileEntity(world, pos).setContainedItem(glyph.getDefaultActivator());
		world.setBlockState(pos = pos.up(), GenesisBlocks.menhirs.getBlockState(EnumMenhirPart.TOP).withProperty(BlockMenhir.FACING, facing));
	}
	
	public int getDistanceWithDefault(EnumFacing direction, int def)
	{
		int distance = getDistance(direction);
		return distance == -1 ? def : distance;
	}
	
	public void setPlacementPosition(World world)
	{
		double distance = -1;
		BlockPos portalPos = null;
		
		nextCenter:
		for (BlockPos checkCenter : WorldUtils.getAreaWithHeight(getCenterPosition(), 64, 0, world.getActualHeight()))
		{
			int startR = 0;
			
			for (EnumFacing dir : EnumFacing.HORIZONTALS)
			{
				int menhirDistance = getDistanceWithDefault(dir, MENHIR_DEFAULT_DISTANCE);
				
				for (int r = startR; r <= menhirDistance; r++)
				{
					for (int h = -1; h <= PORTAL_HEIGHT; h++)
					{
						BlockPos checkPos = checkCenter.offset(dir, r).up(h);
						Block checkBlock = world.getBlockState(checkPos).getBlock();
						
						if (r < menhirDistance)
						{	// Check line to menhir
							if (h == 0)
							{	// Blocks at the same level as the bottoms of the menhirs
								if (!world.canSeeSky(checkPos) || GenesisPortal.isBlockingPortal(world, checkPos))
								{
									continue nextCenter;
								}
							}
						}
						else
						{	// Check menhir position
							if (h == -1)
							{
								if (!World.doesBlockHaveSolidTopSurface(world, checkPos))
								{	// Block below menhir.
									continue nextCenter;
								}
							}
							else if (checkBlock.getMaterial().isLiquid() || !checkBlock.isReplaceable(world, checkPos))
							{	// Blocks in the way of the menhir
								continue nextCenter;
							}
						}
					}
				}
				
				startR = 1;
			}
			
			double checkDistance = getCenterPosition().distanceSq(checkCenter);
			
			if (distance < 0 || checkDistance < distance)
			{
				distance = checkDistance;
				portalPos = checkCenter;
			}
		}
		
		if (portalPos == null)
		{
			portalPos = new BlockPos(getCenterPosition().getX(), world.getActualHeight(), getCenterPosition().getZ());
			
			while (world.isAirBlock(portalPos))
			{
				portalPos = portalPos.down();
			}
			
			portalPos = portalPos.up(2);

			BlockPos start = portalPos.down();
			BlockPos end = start;
			
			for (EnumFacing dir : EnumFacing.HORIZONTALS)
			{
				if (dir.getAxisDirection().getOffset() <= 0)
				{
					start.offset(dir, getDistanceWithDefault(dir, MENHIR_DEFAULT_DISTANCE));
				}
				else
				{
					end.offset(dir, getDistanceWithDefault(dir, MENHIR_DEFAULT_DISTANCE));
				}
			}
			
			for (BlockPos padPos : WorldUtils.getArea(start, end))
			{
				world.setBlockState(padPos, GenesisBlocks.moss.getDefaultState());
			}
		}
		
		setCenterPosition(portalPos);
	}
	
	public void makePortal(World world, Random random)
	{
		setPlacementPosition(world);
		
		EnumSet<EnumGlyph> glyphs = EnumSet.allOf(EnumGlyph.class);
		glyphs.remove(EnumGlyph.NONE);
		
		// Place a menhir on each horizontal direction.
		for (EnumFacing dir : EnumFacing.HORIZONTALS)
		{
			// Get random glyph from the set.
			EnumGlyph glyph = null;
			int glyphIndex = random.nextInt(glyphs.size());
			Iterator<EnumGlyph> glyphsIter = glyphs.iterator();
			
			for (int i = 0; i <= glyphIndex; i++)
			{
				glyph = glyphsIter.next();
			}
			
			glyphs.remove(glyph);
			
			// Place the menhir.
			placeMenhir(world,
					getCenterPosition().offset(dir, getDistanceWithDefault(dir, MENHIR_DEFAULT_DISTANCE)),
					dir.getOpposite(), glyph);
		}
		
		refresh();
		updatePortalStatus(world);
	}
	
	public void duplicatePortal(World world, GenesisPortal fromPortal)
	{
		Genesis.logger.info("Duplicating portal " + fromPortal + " to portal " + this + ".");
		BlockPos posDiff = getCenterPosition().subtract(fromPortal.getCenterPosition());
		IBlockAccess fromWorld = fromPortal.getWorld();
		
		for (MenhirData menhir : fromPortal.getMenhirs().values())
		{
			if (menhir != null)
			{
				for (Pair<EnumMenhirPart, BlockPos> from : new MenhirIterator(fromWorld, menhir.getBottomPos(), true))
				{
					BlockPos newPos = from.getRight().add(posDiff);
					world.setBlockState(newPos, fromWorld.getBlockState(from.getRight()));
					TileEntity te = fromWorld.getTileEntity(from.getRight());
					
					if (te != null)
					{
						NBTTagCompound compound = new NBTTagCompound();
						te.writeToNBT(compound);
						TileEntity newTE = TileEntity.createAndLoadEntity(compound);
						world.removeTileEntity(newPos);
						world.setTileEntity(newPos, newTE);
					}
					
					world.markBlockForUpdate(newPos);
				}
			}
		}
		
		refresh();
		updatePortalStatus(world);
	}
	
	public String toString()
	{
		return "portal at " + getCenterPosition() + " with menhirs " + getMenhirs();
	}
}
