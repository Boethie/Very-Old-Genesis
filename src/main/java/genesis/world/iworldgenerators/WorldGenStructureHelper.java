package genesis.world.iworldgenerators;

import javax.annotation.Nullable;

import genesis.util.Constants;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class WorldGenStructureHelper
{
	public static boolean spawnStructure(World world, BlockPos pos, StructureType type, Mirror mirror, Rotation rotation)
	{
		if (world.isRemote)
			return false;
		
		WorldServer worldserver = (WorldServer)world;
		TemplateManager templatemanager = worldserver.getStructureTemplateManager();
		ResourceLocation structLocation = new ResourceLocation(Constants.MOD_ID, type.toString());
		Template template = templatemanager.getTemplate(null, structLocation);
		
		if (template == null)
			return false;
		
		PlacementSettings placementsettings = (new PlacementSettings())
				.setMirror(mirror)
				.setRotation(rotation)
				.setIgnoreEntities(false)
				.setChunk((ChunkPos)null)
				.setReplacedBlock(Blocks.AIR)
				.setIgnoreStructureBlock(true);
		
		pos = pos.up();
		
		template.addBlocksToWorldChunk(world, pos, placementsettings);
		
		world.markChunkDirty(pos, null);
		
		return true;
	}
	
	public enum StructureType
	{
		HUT1("hut1", null, 6, 7, 8, new Mirror[] {Mirror.NONE}, new Rotation[] {Rotation.NONE}, null),
		METASEQUOIA_HOUSE("metasequoia_house", EnumFacing.DOWN, 7, 6, 9, new Mirror[] {Mirror.NONE}, new Rotation[] {Rotation.NONE}, new Vec3d(-3, 0, 3))
		
		;
		
		private String name;
		private EnumFacing offset;
		private int xSize;
		private int ySize;
		private int zSize;
		
		private Mirror[] mirror;
		private Rotation[] rotation;
		
		private Vec3d secondOffset;
		
		private StructureType(String name, @Nullable EnumFacing offset, int xSize, int ySize, int zSize, Mirror[] mirror, Rotation[] rotation, @Nullable Vec3d secondOffset)
		{
			this.name = name;
			this.offset = offset;
			this.xSize = xSize;
			this.ySize = ySize;
			this.zSize = zSize;
			
			this.mirror = mirror;
			this.rotation = rotation;
			
			this.secondOffset = secondOffset;
		}
		
		@Override
		public String toString()
		{
			return this.name;
		}
		
		public EnumFacing getOffse()
		{
			return this.offset;
		}
		
		public Vec3d getBounds()
		{
			return new Vec3d(this.xSize, this.ySize, this.zSize);
		}
		
		public Mirror[] getMirror()
		{
			return this.mirror;
		}
		
		public Rotation[] getRotation()
		{
			return this.rotation;
		}
		
		public Vec3d getSecondOffset()
		{
			return this.secondOffset;
		}
	}
}
