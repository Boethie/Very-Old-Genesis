package genesis.world;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import genesis.block.tileentity.portal.BlockMenhir;
import genesis.block.tileentity.portal.BlockMenhir.EnumGlyph;
import genesis.common.Genesis;
import genesis.common.GenesisBlocks;
import genesis.metadata.EnumMenhirPart;
import genesis.util.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterGenesis extends Teleporter
{
	protected final WorldServer world;
	protected final Map<BlockPos, PortalPosition> cache = Maps.newHashMap();
	protected final Random random;
	
	public TeleporterGenesis(WorldServer world)
	{
		super(world);
		
		this.world = world;
		random = new Random(world.getSeed());
		
		if (!world.customTeleporters.contains(this))
		{
			world.customTeleporters.add(this);
		}
	}
	
	@Override
	public void placeInPortal(Entity entity, float rotationYaw)
	{
		if (!placeInExistingPortal(entity, rotationYaw))
		{
			makePortal(entity);
			placeInExistingPortal(entity, rotationYaw);
		}
	}
	
	@Override
	public boolean placeInExistingPortal(Entity entity, float rotationYaw)
	{
		BlockPos center = entity.getPosition();
		
		BlockPos portalPos = null;
		
		if (cache.containsKey(center))
		{
			portalPos = cache.get(center);
			
			if (world.getBlockState(portalPos).getBlock() != GenesisBlocks.portal)
			{
				portalPos = null;
			}
		}
		
		if (portalPos == null)
		{
			double portalDistance = -1;
			
			for (BlockPos pos : WorldUtils.getAreaWithHeight(center, 128, 0, world.getActualHeight()))
			{
				double checkDistance = pos.distanceSq(center);
				
				if ((portalDistance < 0 || checkDistance < portalDistance) && world.getBlockState(pos).getBlock() == GenesisBlocks.portal)
				{
					portalDistance = checkDistance;
					portalPos = pos;
				}
			}
		}
		else
		{
			Genesis.logger.info("Cached portal found from location " + center + " at destination location " + portalPos);
		}
		
		if (portalPos != null)
		{
			cache.put(center, new PortalPosition(portalPos, world.getTotalWorldTime()));
			Genesis.logger.info("Caching portal position from location " + center + " to " + portalPos);
			
			/* TODO: Decide whether to keep this or make it float you down through the same force that carries you into the portal
			while (world.isAirBlock(portalPos))
			{
				portalPos = portalPos.down();
			}
			
			portalPos = portalPos.up();*/
			
			entity.setLocationAndAngles(portalPos.getX() + 0.5, portalPos.getY() - entity.getEyeHeight() / 2, portalPos.getZ() + 0.5, entity.rotationYaw, entity.rotationPitch);
			return true;
		}
		
		return false;
	}
	
	public void placeMenhir(BlockPos pos, EnumFacing facing, EnumGlyph glyph)
	{
		world.setBlockState(pos, GenesisBlocks.menhirs.getBlockState(EnumMenhirPart.GLYPH).withProperty(BlockMenhir.FACING, facing));
		BlockMenhir.getGlyphTileEntity(world, pos).setGlyph(glyph);
		world.setBlockState(pos = pos.up(), GenesisBlocks.menhirs.getBlockState(EnumMenhirPart.RECEPTACLE).withProperty(BlockMenhir.FACING, facing));
		world.setBlockState(pos = pos.up(), GenesisBlocks.menhirs.getBlockState(EnumMenhirPart.TOP).withProperty(BlockMenhir.FACING, facing));
	}
	
	public boolean makePortal(BlockPos pos)
	{
		final int portalHeight = 3;
		final int portalRadius = 3;
		
		double distance = -1;
		BlockPos portalPos = null;
		
		nextCenter:
		for (BlockPos checkCenter : WorldUtils.getAreaWithHeight(pos, 64, 0, world.getActualHeight()))
		{
			int startR = 0;
			
			for (EnumFacing dir : EnumFacing.HORIZONTALS)
			{
				for (int r = startR; r <= portalRadius; r++)
				{
					for (int h = -1; h <= portalHeight; h++)
					{
						BlockPos checkPos = checkCenter.offset(dir, r).up(h);
						IBlockState checkState = world.getBlockState(checkPos);
						
						if (h < 0 ?
							!checkState.getBlock().isSideSolid(world, checkPos, EnumFacing.UP) :
							(h == 0) ? !world.canSeeSky(checkPos) : false ||
							checkState.getBlock().getMaterial().isLiquid() ||
							!checkState.getBlock().isReplaceable(world, checkPos))
						{
							continue nextCenter;
						}
					}
				}
				
				startR = 1;
			}
			
			double checkDistance = pos.distanceSq(checkCenter);
			
			if (distance < 0 || checkDistance < distance)
			{
				distance = checkDistance;
				portalPos = checkCenter;
			}
		}
		
		if (portalPos == null)
		{
			portalPos = new BlockPos(pos.getX(), world.getActualHeight(), pos.getZ());
			
			while (world.isAirBlock(portalPos))
			{
				portalPos = portalPos.down();
			}
			
			portalPos = portalPos.up(2);
			
			for (BlockPos padPos : WorldUtils.getAreaWithHeight(portalPos, portalRadius, portalPos.getY() - 1, portalPos.getY() - 1))
			{
				world.setBlockState(padPos, GenesisBlocks.moss.getDefaultState());
			}
		}
		
		if (portalPos != null)
		{
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
				placeMenhir(portalPos.offset(dir, portalRadius), dir.getOpposite(), glyph);
			}
			
			world.setBlockState(portalPos.up(portalHeight), GenesisBlocks.portal.getDefaultState());
		}
		
		return true;
	}
	
	@Override
	public boolean makePortal(Entity entity)
	{
		makePortal(entity.getPosition());
		return true;
	}
	
	@Override
	public void removeStalePortalLocations(long worldTime)
	{
		if (worldTime % 100 == 0)
		{
			long killTime = worldTime - 600;
			Iterator<Map.Entry<BlockPos, PortalPosition>> iterator = cache.entrySet().iterator();
			
			while (iterator.hasNext())
			{
				Map.Entry<BlockPos, PortalPosition> entry = iterator.next();
				
				if (entry.getValue().lastUpdateTime < killTime)
				{
					iterator.remove();
					Genesis.logger.info("Removed portal position from " + entry.getKey() + " to destination " + entry.getValue() + ".");
				}
			}
		}
	}
}
