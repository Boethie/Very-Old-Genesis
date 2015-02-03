package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenLakesGenesis extends WorldGenerator {
	private Block liquid;

	public WorldGenLakesGenesis(Block p_i45455_1_) {
		this.liquid = p_i45455_1_;
	}

	public boolean generate(World world, Random rand, BlockPos pos) {
		pos = pos.add(0, -8, 0);

		for (pos = pos.add(-8, 0, 0); pos.getY() > 5 && world.isAirBlock(pos); pos = pos.add(0, -1, 0)) {
			;
		}

		if (pos.getY() <= 4) {
			return false;
		} else {
			pos.add(0, -4, 0);
			boolean[] aboolean = new boolean[2048];
			int l = rand.nextInt(4) + 4;
			int i1;

			for (i1 = 0; i1 < l; ++i1) {
				double d0 = rand.nextDouble() * 6.0D + 3.0D;
				double d1 = rand.nextDouble() * 4.0D + 2.0D;
				double d2 = rand.nextDouble() * 6.0D + 3.0D;
				double d3 = rand.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
				double d4 = rand.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
				double d5 = rand.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

				for (int k1 = 1; k1 < 15; ++k1) {
					for (int l1 = 1; l1 < 15; ++l1) {
						for (int i2 = 1; i2 < 7; ++i2) {
							double d6 = ((double) k1 - d3) / (d0 / 2.0D);
							double d7 = ((double) i2 - d4) / (d1 / 2.0D);
							double d8 = ((double) l1 - d5) / (d2 / 2.0D);
							double d9 = d6 * d6 + d7 * d7 + d8 * d8;

							if (d9 < 1.0D) {
								aboolean[(k1 * 16 + l1) * 8 + i2] = true;
							}
						}
					}
				}
			}

			int j1;
			int j2;
			boolean flag;

			for (i1 = 0; i1 < 16; ++i1) {
				for (j2 = 0; j2 < 16; ++j2) {
					for (j1 = 0; j1 < 8; ++j1) {
						flag = !aboolean[(i1 * 16 + j2) * 8 + j1] && (i1 < 15 && aboolean[((i1 + 1) * 16 + j2) * 8 + j1] || i1 > 0 && aboolean[((i1 - 1) * 16 + j2) * 8 + j1] || j2 < 15 && aboolean[(i1 * 16 + j2 + 1) * 8 + j1] || j2 > 0 && aboolean[(i1 * 16 + (j2 - 1)) * 8 + j1] || j1 < 7 && aboolean[(i1 * 16 + j2) * 8 + j1 + 1] || j1 > 0 && aboolean[(i1 * 16 + j2) * 8 + (j1 - 1)]);

						if (flag) {
							Block block = world.getBlockState(pos.add(i1, j1, j2)).getBlock();
							Material material = block.getMaterial();

							if (j1 >= 4 && material.isLiquid()) {
								return false;
							}

							if (j1 < 4 && !material.isSolid() && block != this.liquid) {
								return false;
							}
						}
					}
				}
			}

			for (i1 = 0; i1 < 16; ++i1) {
				for (j2 = 0; j2 < 16; ++j2) {
					for (j1 = 0; j1 < 8; ++j1) {
						if (aboolean[(i1 * 16 + j2) * 8 + j1]) {
							world.setBlockState(pos.add(i1, j1, j2), j1 >= 4 ? Blocks.air.getDefaultState() : this.liquid.getDefaultState(), 2);
						}
					}
				}
			}

			for (i1 = 0; i1 < 16; ++i1) {
				for (j2 = 0; j2 < 16; ++j2) {
					for (j1 = 4; j1 < 8; ++j1) {
						if (aboolean[(i1 * 16 + j2) * 8 + j1] && world.getBlockState(pos.add(i1, j1-1, j2)).getBlock() == Blocks.dirt && world.getLightFor(EnumSkyBlock.SKY, pos.add(i1, j1, j2)) > 0) {
							BiomeGenBase biomegenbase = world.getBiomeGenForCoords(pos.add(i1, 0, j2));

							world.setBlockState(pos.add(i1, j1-1, j2), GenesisBlocks.moss.getDefaultState(), 2);
						}
					}
				}
			}

			if (this.liquid.getMaterial() == Material.lava) {
				for (i1 = 0; i1 < 16; ++i1) {
					for (j2 = 0; j2 < 16; ++j2) {
						for (j1 = 0; j1 < 8; ++j1) {
							flag = !aboolean[(i1 * 16 + j2) * 8 + j1] && (i1 < 15 && aboolean[((i1 + 1) * 16 + j2) * 8 + j1] || i1 > 0 && aboolean[((i1 - 1) * 16 + j2) * 8 + j1] || j2 < 15 && aboolean[(i1 * 16 + j2 + 1) * 8 + j1] || j2 > 0 && aboolean[(i1 * 16 + (j2 - 1)) * 8 + j1] || j1 < 7 && aboolean[(i1 * 16 + j2) * 8 + j1 + 1] || j1 > 0 && aboolean[(i1 * 16 + j2) * 8 + (j1 - 1)]);

							if (flag && (j1 < 4 || rand.nextInt(2) != 0) && world.getBlockState(pos.add(i1, j1, j2)).getBlock().getMaterial().isSolid()) {
								world.setBlockState(pos.add(i1, j1, j2), GenesisBlocks.komatiite.getDefaultState(), 2);
							}
						}
					}
				}
			}

			if (this.liquid.getMaterial() == Material.water) {
				for (i1 = 0; i1 < 16; ++i1) {
					for (j2 = 0; j2 < 16; ++j2) {
						for (j1 = 0; j1 < 8; ++j1) {
							flag = !aboolean[(i1 * 16 + j2) * 8 + j1] && (i1 < 15 && aboolean[((i1 + 1) * 16 + j2) * 8 + j1] || i1 > 0 && aboolean[((i1 - 1) * 16 + j2) * 8 + j1] || j2 < 15 && aboolean[(i1 * 16 + j2 + 1) * 8 + j1] || j2 > 0 && aboolean[(i1 * 16 + (j2 - 1)) * 8 + j1] || j1 < 7 && aboolean[(i1 * 16 + j2) * 8 + j1 + 1] || j1 > 0 && aboolean[(i1 * 16 + j2) * 8 + (j1 - 1)]);

							if (flag && (j1 < 4 || rand.nextInt(2) != 0) && world.getBlockState(pos.add(i1, j1, j2)).getBlock().getMaterial().isSolid()) {
								if (world.getBlockState(pos.add(i1, j1+1, j2)).getBlock().getMaterial() != Material.water) {
									if(rand.nextInt(10) == 0) {
										world.setBlockState(pos.add(i1, j1, j2), GenesisBlocks.mossy_granite.getDefaultState(), 2);
									}
									if (world.isAirBlock(pos.add(i1, j1+1, j2))) {
										if (rand.nextInt(20) == 0) {
											world.setBlockState(pos.add(i1, j1+1, j2), GenesisBlocks.mossy_granite.getDefaultState(), 2);
										} else if (rand.nextInt(2) == 0) {
											world.setBlockState(pos.add(i1, j1, j2), GenesisBlocks.moss.getDefaultState(), 2);
											if (world.isAirBlock(pos.add(i1, j1+2, j2)) && rand.nextInt(3) == 0) {
												//TODO: Add the code for the plants below when they're added.
												//world.setBlockState(pos.add(i1, j1+1, j2), GenesisBlocks.asteroxylon, 1, 2);
														//world.setBlockState(pos.add(i1, j1+2, j2),, GenesisBlocks.asteroxylon_top, 1, 2);
											} else {
												//world.setBlock(pos.add(i1, j1+1, j2), GenesisBlocks.asteroxylon, 0, 2);
											}
										}
									}
								} else {
									world.setBlockState(pos.add(i1, j1, j2), Blocks.dirt.getDefaultState(), 2);
								}
							}
						}

						byte b0 = 4;

						if (world.canBlockFreeze(pos.add(i1, b0, j2), false)) {
							world.setBlockState(pos.add(i1, b0, j2), Blocks.ice.getDefaultState(), 2);
						}
					}
				}
			}

			return true;
		}
	}
}