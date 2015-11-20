package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumSilt;
import genesis.metadata.SiltBlocks;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenPatch;
import genesis.world.biome.decorate.WorldGenPebbles;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.gen.feature.WorldGenTreeBjuvia;
import genesis.world.gen.feature.WorldGenTreeVoltzia;
import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

public class BiomeGenRedDesert extends BiomeGenBaseGenesis
{
	public BiomeGenRedDesert(int id)
	{
		super(id);
		setBiomeName("Red Desert");
		setTemperatureRainfall(2.0F, 0.0F);
		setDisableRain();
		setHeight(0.125F, 0.05F);
		topBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
		fillerBlock = GenesisBlocks.silt.getBlockState(SiltBlocks.SILT, EnumSilt.RED_SILT);
		
		this.theBiomeDecorator.grassPerChunk = 0;
		this.theBiomeDecorator.sandPerChunk = 0;
		this.theBiomeDecorator.sandPerChunk2 = 0;
		
		addDecoration(new WorldGenPatch().addBlocks(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT)).addAllowedBlocks(GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.RED_SILT).getBlockState()).setCountPerChunk(7));
		addDecoration(new WorldGenMossStages().addAllowedBlocks(GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.RED_SILT).getBlockState(), Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT).getBlock().getBlockState()).setCountPerChunk(35));
		
		addDecoration(new WorldGenPebbles().setWaterRequired(false).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders().setWaterRequired(false).setMaxHeight(4).setRarity(5).addBlocks(GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT)).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders().setRarity(85).setWaterRequired(false).setMaxHeight(2).addBlocks(GenesisBlocks.octaedrite.getDefaultState()).setCountPerChunk(1));
		
		addTree(new WorldGenTreeBjuvia(4, 6, true).setTreeCountPerChunk(1).setRarity(12));
		addTree(new WorldGenTreeVoltzia(5, 8, true).setTreeCountPerChunk(1).setRarity(14));
	}
	
	@Override
	public float getFogDensity(int x, int y, int z)
	{
		return 0.68F;
	}
	
	@Override
	public Vec3 getFogColor()
	{
		float red = 0.917647059F;
		float green = 0.650980392F;
		float blue = 0.309803922F;
		
		return new Vec3(red, green, blue);
	}
}
