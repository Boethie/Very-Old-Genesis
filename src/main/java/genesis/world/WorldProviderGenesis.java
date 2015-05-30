package genesis.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderGenesis extends WorldProvider
{

	@Override
	public String getDimensionName()
	{
		return "Genesis";
	}

	@Override
	public String getInternalNameSuffix()
	{
		return "";
	}

	@Override
	public String getWelcomeMessage()
	{
		return EnumChatFormatting.ITALIC + "You feel yourself forgetting your knowledge of crafting...";
	}

	@Override
	protected void registerWorldChunkManager()
	{
		this.worldChunkMgr = new WorldChunkManagerGenesis(this.worldObj);
	}

	@Override
	public IChunkProvider createChunkGenerator()
	{
		return new ChunkGeneratorGenesis(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.worldObj.getWorldInfo().getGeneratorOptions());
	}

	@Override
	public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
	{
		return new Vec3(0.29411764705882352941176470588235D, 0.47450980392156862745098039215686D, 0.1960784313725490196078431372549);
	}

}
