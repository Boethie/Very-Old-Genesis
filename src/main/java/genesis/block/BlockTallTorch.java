package genesis.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import genesis.common.GenesisCreativeTabs;

import java.util.Random;

/**
 * Created by Simeon on 9/14/2015.
 */
public class BlockTallTorch extends Block
{
		public static IProperty PART_PROPERTY = PropertyEnum.create("part", PART.class);

		public BlockTallTorch()
		{
				super(Material.circuits);
				setDefaultState(this.blockState.getBaseState().withProperty(PART_PROPERTY, PART.BOTTOM));
				this.setTickRandomly(true);
				setBlockBounds(0.4f, 0, 0.4f, 0.6f, 1, 0.6f);
				setLightLevel(.9375F);
				setHardness(0.0F);
				setStepSound(soundTypeWood);
				this.setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		}

		public boolean isOpaqueCube()
		{
				return false;
		}

		public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
		{
				return true;
		}

		public boolean isFullCube()
		{
				return false;
		}

		public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
		{
				return pos.getY() >= worldIn.getHeight() - 1 ? false : World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.up());
		}

		/**
		 * Get the Item that this Block should drop when harvested.
		 *
		 * @param fortune the level of the Fortune enchantment on the player's tool
		 */
		public Item getItemDropped(IBlockState state, Random rand, int fortune)
		{
				return state.getValue(PART_PROPERTY) == PART.TOP ? null : super.getItemDropped(state, rand, fortune);
		}

		/**
		 * Convert the given metadata into a BlockState for this Block
		 */
		@Override
		public IBlockState getStateFromMeta(int meta)
		{
				IBlockState iblockstate = this.getDefaultState();

				switch (meta)
				{
				case 1:
						iblockstate = iblockstate.withProperty(PART_PROPERTY, PART.TOP);
						break;
				default:
						iblockstate = iblockstate.withProperty(PART_PROPERTY, PART.BOTTOM);
				}

				return iblockstate;
		}

		/**
		 * Convert the BlockState into the correct metadata value
		 */
		@Override
		public int getMetaFromState(IBlockState state)
		{
				if (state.getValue(PART_PROPERTY) == PART.TOP)
				{
						return 1;
				}
				return 0;
		}

		public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
		{
				if (state.getValue(PART_PROPERTY) == PART.BOTTOM)
				{
						boolean destroyed = false;

						IBlockState topBlockState = worldIn.getBlockState(pos.up());
						if (topBlockState.getBlock() != this)
						{
								worldIn.setBlockToAir(pos);
								destroyed = true;
						}

						if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()))
						{
								worldIn.setBlockToAir(pos);
								destroyed = true;

								if (topBlockState.getBlock() == this)
								{
										worldIn.setBlockToAir(pos.up());
								}
						}

						if (destroyed)
						{
								if (!worldIn.isRemote)
								{
										this.dropBlockAsItem(worldIn, pos, state, 0);
								}
						}
				}
				else
				{
						IBlockState topBlockState = worldIn.getBlockState(pos.down());
						if (topBlockState.getBlock() != this)
						{
								worldIn.setBlockToAir(pos);
						}
						else if (neighborBlock != this)
						{
								this.onNeighborBlockChange(worldIn, pos.down(), topBlockState, neighborBlock);
						}
				}
		}

		@SideOnly(Side.CLIENT)
		public EnumWorldBlockLayer getBlockLayer()
		{
				return EnumWorldBlockLayer.CUTOUT;
		}

		@SideOnly(Side.CLIENT)
		public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
		{
				if (state.getValue(PART_PROPERTY) == PART.TOP)
				{
						double d0 = (double) pos.getX() + 0.5D;
						double d1 = (double) pos.getY() + 0.7D;
						double d2 = (double) pos.getZ() + 0.5D;

						worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
						worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
				}
		}

		protected BlockState createBlockState()
		{
				return new BlockState(this, new IProperty[] { PART_PROPERTY });
		}

		public enum PART implements IStringSerializable
		{
				TOP("top"),
				BOTTOM("bottom");
				private final String name;

				PART(String name)
				{
						this.name = name;
				}

				public String toString()
				{
						return this.name;
				}

				public String getName()
				{
						return this.name;
				}
		}
}
