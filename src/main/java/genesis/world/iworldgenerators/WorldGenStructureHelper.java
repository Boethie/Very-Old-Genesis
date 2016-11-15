package genesis.world.iworldgenerators;

import javax.annotation.Nullable;

import genesis.util.Constants;
import net.minecraft.block.Block;
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
				.setReplacedBlock((Block)null)
				.setIgnoreStructureBlock(true);
		
		template.addBlocksToWorldChunk(world, pos.up(), placementsettings);
		
		world.markChunkDirty(pos, null);
		
		return true;
	}
	
	public enum StructureType
	{
		HUT1("Hut1", null, 6, 7, 8)
		;
		
		private String name;
		private EnumFacing offset;
		private int xSize;
		private int ySize;
		private int zSize;
		
		private StructureType(String name, @Nullable EnumFacing offset, int xSize, int ySize, int zSize)
		{
			this.name = name;
			this.offset = offset;
			this.xSize = xSize;
			this.ySize = ySize;
			this.zSize = zSize;
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
	}
}
