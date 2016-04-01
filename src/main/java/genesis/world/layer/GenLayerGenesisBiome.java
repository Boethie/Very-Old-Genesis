package genesis.world.layer;

import static net.minecraftforge.common.BiomeManager.BiomeType.COOL;
import static net.minecraftforge.common.BiomeManager.BiomeType.DESERT;
import static net.minecraftforge.common.BiomeManager.BiomeType.ICY;
import static net.minecraftforge.common.BiomeManager.BiomeType.WARM;

import java.util.ArrayList;
import java.util.List;

import genesis.world.biome.BiomeManagerGenesis;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;

public class GenLayerGenesisBiome extends GenLayerGenesis
{
	private final List<BiomeEntry>[] allowedBiomes;
	
	public GenLayerGenesisBiome(long seed, GenLayer parentGenLayer)
	{
		super(seed);
		
		@SuppressWarnings("unchecked")
		List<BiomeEntry>[] biomes = new ArrayList[BiomeType.values().length];
		
		for (BiomeType type : BiomeType.values())
		{
			biomes[type.ordinal()] = BiomeManagerGenesis.getEntries(type);
		}
		
		this.allowedBiomes = biomes;
		this.parent = parentGenLayer;
	}
	
	/*
	 * @Override
	 * public int[] getInts(int par1, int par2, int par3, int par4)
	 * {
	 * int[] aint1 = IntCache.getIntCache(par3 * par4);
	 * 
	 * for (int i1 = 0; i1 < par4; ++i1)
	 * for (int j1 = 0; j1 < par3; ++j1)
	 * {
	 * initChunkSeed(j1 + par1, i1 + par2);
	 * BiomeGenBase biome = allowedBiomes[nextInt(allowedBiomes.length)];
	 * aint1[j1 + i1 * par3] = biome.biomeID;
	 * }
	 * return aint1;
	 * }
	 */
	
	@Override
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	{
		int[] randomTypes = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
		int[] out = IntCache.getIntCache(areaWidth * areaHeight);
		
		for (int offY = 0; offY < areaHeight; ++offY)
		{
			for (int offX = 0; offX < areaWidth; ++offX)
			{
				this.initChunkSeed(offX + areaX, offY + areaY);
				int type = randomTypes[offX + offY * areaWidth];
				//int l1 = (k1 & 3840) >> 8;
				type &= -3841;
				
				if (isBiomeOceanic(type))
					out[offX + offY * areaWidth] = type;
				else
					out[offX + offY * areaWidth] = BiomeGenBase.getIdForBiome(getWeightedBiomeEntry(WARM).biome);
				
				BiomeEntry biome = null;
				
				switch (type)
				{
				case 1:
					biome = getWeightedBiomeEntry(DESERT);
					break;
				case 2:
					biome = getWeightedBiomeEntry(WARM);
					break;
				case 3:
					biome = getWeightedBiomeEntry(COOL);
					break;
				case 4:
					biome = getWeightedBiomeEntry(ICY);
					break;
				}
				
				if (biome != null)
					out[offX + offY * areaWidth] = BiomeGenBase.getIdForBiome(biome.biome);
			}
		}
		
		return out;
	}
	
	protected BiomeEntry getWeightedBiomeEntry(BiomeManager.BiomeType type)
	{
		List<BiomeEntry> biomeList = this.allowedBiomes[type.ordinal()];
		
		if (biomeList.isEmpty())
			return null;
		
		int totalWeight = WeightedRandom.getTotalWeight(biomeList);
		int weight = BiomeManager.isTypeListModded(type) ? this.nextInt(totalWeight) : this.nextInt(totalWeight / 10) * 10;
		return WeightedRandom.getRandomItem(biomeList, weight);
	}
}
