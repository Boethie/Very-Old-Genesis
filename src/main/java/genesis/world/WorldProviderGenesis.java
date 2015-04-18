package genesis.world;

import net.minecraft.util.EnumChatFormatting;
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
		return EnumChatFormatting.ITALIC+"You feel yourself forgetting your knowledge of crafting...";
	}
	
	@Override
	protected void registerWorldChunkManager()
	{
		this.worldChunkMgr = new ChunkManagerGenesis(this.worldObj);
	}

	@Override
	public IChunkProvider createChunkGenerator()
	{
		return new ChunkGeneratorGenesis(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.worldObj.getWorldInfo().getGeneratorOptions());
	}

}
