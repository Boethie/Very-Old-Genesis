package genesis.world.layer;

import java.lang.reflect.Field;
import java.util.ArrayList;

import genesis.common.GenesisBiomes;
import genesis.world.biome.BiomeGenesis;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerDebug extends GenLayer
{
	private final ArrayList<BiomeGenesis> biomes;
	private int scaleBits;
	
	public GenLayerDebug(int scaleBits)
	{
		super(0);
		this.scaleBits = scaleBits;
		
		this.biomes = new ArrayList<>();
		// use reflection to get all biomes
		for (Field fld : GenesisBiomes.class.getDeclaredFields())
		{
			if (BiomeGenesis.class.isAssignableFrom(fld.getType()))
			{
				try
				{
					BiomeGenesis b = (BiomeGenesis) fld.get(null);
					if (b != null)
					{
						this.biomes.add(b);
					}
					
				} catch (IllegalAccessException e)
				{
					throw new Error(e);
				}
			}
		}
	}
	
	@Override
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	{
		int[] biomes = IntCache.getIntCache(areaWidth * areaHeight);
		for (int offY = 0; offY < areaHeight; ++offY)
		{
			for (int offX = 0; offX < areaWidth; ++offX)
			{
				int index = (offX + areaX) >> scaleBits;
				index = Math.floorMod(index, this.biomes.size());
				BiomeGenesis biome = this.biomes.get(index);
				biomes[offX + offY * areaWidth] = Biome.getIdForBiome(biome);
			}
		}
		return biomes;
	}
}
