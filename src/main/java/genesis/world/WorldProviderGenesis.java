package genesis.world;

import genesis.common.GenesisBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderGenesis extends WorldProvider
{
	private float[] colorsSunriseSunset = new float[4];
	
    protected void generateLightBrightnessTable()
    {
        float f = -0.0F;
        
        for (int i = 0; i <= 15; ++i)
        {
            float f1 = 1.0F - (float)i / 15.0F;
            this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
        }
    }
    
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
    public float calculateCelestialAngle(long par1, float par3)
	{
		int j = (int) (par1 % 34000L);
		float f1 = ((float) j + par3) / 34000.0F - 0.25F;
		
		if (f1 < 0.0F)
			++f1;
		
		if (f1 > 1.0F)
			--f1;
		
		float f2 = f1;
		f1 = 1.0F - (float) ((Math.cos((double) f1 * Math.PI) + 1.0D) / 2.0D);
		f1 = f2 + (f1 - f2) / 3.0F;
		return f1;
	}
	
	@Override
    public int getMoonPhase(long par1)
	{
		return (int) (par1 / 34000L % 8L + 8L) % 8;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float par1, float par2)
	{
		float f2 = 0.4F;
		float f3 = MathHelper.cos(par1 * (float) Math.PI * 2.0F) - 0.0F;
		float f4 = -0.0F;
		
		if (f3 >= f4 - f2 && f3 <= f4 + f2)
		{
			float f5 = (f3 - f4) / f2 * 0.5F + 0.5F;
			float f6 = 1.0F - (1.0F - MathHelper.sin(f5 * (float) Math.PI)) * 0.99F;
			f6 *= f6;
			this.colorsSunriseSunset[0] = f5 * 0.3F + 0.7F;
			this.colorsSunriseSunset[1] = f5 * f5 * 0.7F + 0.2F;
			this.colorsSunriseSunset[2] = f5 * f5 * 0.0F + 0.2F;
			this.colorsSunriseSunset[3] = f6;
			return this.colorsSunriseSunset;
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
	{
		long time = worldObj.getWorldTime()%34000;
		
		double red = 0.29411764705882352941176470588235D;
		double green = 0.47450980392156862745098039215686D;
		double blue = 0.1960784313725490196078431372549D;
		//Green sky everywhere - To change if we ever need different color by biome
		/*
		if (getBiomeGenForCoords(new BlockPos(cameraEntity.posX, cameraEntity.posY, cameraEntity.posZ)) instanceof BiomeGenBaseGenesis)
		{
			BiomeGenBaseGenesis biome = (BiomeGenBaseGenesis)getBiomeGenForCoords(new BlockPos(cameraEntity.posX, cameraEntity.posY, cameraEntity.posZ));
			red = biome.getSkyColor().xCoord;
			green = biome.getSkyColor().yCoord;
			blue = biome.getSkyColor().zCoord;
		}
		*/
		if (time > 20000 && time <= 31000)
		{
			red = 0;
			green = 0;
			blue = 0;
		}
		else
		{
			if (time > 17000 && time <= 20000)
			{
				float percent = 1.0F - (((float)time - 17000F) / 3000F);
				
				if (percent < 0.0F)
					percent = 0.0F;
				
				red *= percent;
				green *= percent;
				blue *= percent;
			}
			
			if (time > 31000 && time <= 34000)
			{
				float percent = (((float)time - 31000F) / 3000F);
				
				if (percent > 1.0F)
					percent = 1.0F;
				
				red *= percent;
				green *= percent;
				blue *= percent;
			}
		}
		
		return new Vec3(red, green, blue);
	}
	
	@Override
    public void setWorldTime(long time)
	{
		worldObj.getWorldInfo().setWorldTime(time);
	}
	
	@Override
    public long getWorldTime()
	{
		return worldObj.getWorldInfo().getWorldTime();
	}
	
	@Override
	public boolean canCoordinateBeSpawn(int x, int z)
    {
        return worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)) == GenesisBlocks.moss;
    }
}
