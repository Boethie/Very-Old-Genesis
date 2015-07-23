package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumAquaticPlant;
import genesis.metadata.EnumCoral;
import genesis.world.biome.decorate.BiomeDecoratorGenesis;
import genesis.world.biome.decorate.WorldGenAquaticPlants;
import genesis.world.biome.decorate.WorldGenCorals;

import java.util.Random;

import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenShallowOcean extends BiomeGenBaseGenesis
{
	IBlockState oceanFloor = GenesisBlocks.ooze.getDefaultState();
	
	public BiomeGenShallowOcean(int id)
	{
		super(id);
		this.biomeName = "Shallow Ocean";
		this.minHeight = -.8F;
		this.maxHeight = 0.1F;
		this.waterColorMultiplier = 0x008d49;
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenCorals(2, 5, EnumCoral.HALYSITES).setCountPerChunk(70));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenCorals(2, 5, EnumCoral.HELIOLITES).setCountPerChunk(70));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenCorals(2, 5, EnumCoral.FAVOSITES).setCountPerChunk(70));
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setGenerateInGroup(true, 7).setPlantType(EnumAquaticPlant.BANGIOMORPHA).setCountPerChunk(900));
		
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.HAZELLA).setCountPerChunk(900));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.DIAONIELLA).setCountPerChunk(800));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.CHANCELLORIA).setCountPerChunk(800));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.PIRANIA).setCountPerChunk(700));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.VAUXIA).setCountPerChunk(700));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.PTERIDINIUM).setCountPerChunk(700));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.CHARNIA, EnumAquaticPlant.CHARNIA_TOP).setCountPerChunk(700));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.ERNIETTA).setCountPerChunk(700));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.WAPKIA).setCountPerChunk(600));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setGenerateInGroup(true, 6).setPlantType(EnumAquaticPlant.MARPOLIA).setCountPerChunk(600));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setGenerateInGroup(true, 6).setPlantType(EnumAquaticPlant.MARGERETIA).setCountPerChunk(600));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.DINOMISCHUS).setCountPerChunk(500));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setPlantType(EnumAquaticPlant.ECHMATOCRINUS).setCountPerChunk(500));
		((BiomeDecoratorGenesis) this.theBiomeDecorator).decorations.add(new WorldGenAquaticPlants().setGenerateInGroup(true, 6).setPlantType(EnumAquaticPlant.GRYPANIA).setCountPerChunk(500));
	}
	
	@Override
	public final void generateBiomeTerrain(World worldIn, Random p_180628_2_, ChunkPrimer p_180628_3_, int p_180628_4_, int p_180628_5_, double p_180628_6_)
    {
        IBlockState iblockstate = this.topBlock;
        IBlockState iblockstate1 = this.fillerBlock;
        int k = -1;
        int l = (int)(p_180628_6_ / 3.0D + 3.0D + p_180628_2_.nextDouble() * 0.25D);
        int i1 = p_180628_4_ & 15;
        int j1 = p_180628_5_ & 15;

        for (int k1 = 255; k1 >= 0; --k1)
        {
            if (k1 <= p_180628_2_.nextInt(5))
            {
                p_180628_3_.setBlockState(j1, k1, i1, Blocks.bedrock.getDefaultState());
            }
            else
            {
                IBlockState iblockstate2 = p_180628_3_.getBlockState(j1, k1, i1);

                if (iblockstate2.getBlock().getMaterial() == Material.air)
                {
                    k = -1;
                }
                else if (iblockstate2.getBlock() == Blocks.stone)
                {
                    if (k == -1)
                    {
                        if (l <= 0)
                        {
                            iblockstate = null;
                            iblockstate1 = Blocks.stone.getDefaultState();
                        }
                        else if (k1 >= 59 && k1 <= 64)
                        {
                            iblockstate = this.topBlock;
                            iblockstate1 = this.fillerBlock;
                        }

                        if (k1 < 63 && (iblockstate == null || iblockstate.getBlock().getMaterial() == Material.air))
                        {
                            if (this.getFloatTemperature(new BlockPos(p_180628_4_, k1, p_180628_5_)) < 0.15F)
                            {
                                iblockstate = Blocks.ice.getDefaultState();
                            }
                            else
                            {
                                iblockstate = Blocks.water.getDefaultState();
                            }
                        }

                        k = l;

                        if (k1 >= 62)
                        {
                            p_180628_3_.setBlockState(j1, k1, i1, iblockstate);
                        }
                        else if (k1 < 56 - l)
                        {
                            iblockstate = null;
                            iblockstate1 = Blocks.stone.getDefaultState();
                            p_180628_3_.setBlockState(j1, k1, i1, oceanFloor);
                        }
                        else
                        {
                            p_180628_3_.setBlockState(j1, k1, i1, iblockstate1);
                        }
                    }
                    else if (k > 0)
                    {
                        --k;
                        p_180628_3_.setBlockState(j1, k1, i1, iblockstate1);

                        if (k == 0 && iblockstate1.getBlock() == Blocks.sand)
                        {
                            k = p_180628_2_.nextInt(4) + Math.max(0, k1 - 63);
                            iblockstate1 = iblockstate1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? Blocks.red_sandstone.getDefaultState() : Blocks.sandstone.getDefaultState();
                        }
                    }
                }
            }
        }
    }
	
	@Override
	public Vec3 getSkyColor()
	{
		return new Vec3(0.294117647D, 0.474509804D, 0.501960784D);
	}
}
