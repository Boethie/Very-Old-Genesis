package genesis.block;

import java.util.Random;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDoubleAsteroxylon extends BlockBush
{
	public static final PropertyBool TOP = PropertyBool.create("top");

	public BlockDoubleAsteroxylon()
	{
		setDefaultState(blockState.getBaseState().withProperty(TOP, false));
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	{
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return super.canPlaceBlockAt(world, pos) && world.isAirBlock(pos.up());
	}

	@Override
	protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state)
	{
		if (!canBlockStay(world, pos, state))
		{
			boolean isTop = (Boolean) state.getValue(TOP);
			BlockPos top = isTop ? pos : pos.up();
			BlockPos bottom = isTop ? pos.down() : pos;
			Block topBlock = isTop ? this : world.getBlockState(top).getBlock();
			Block bottomBlock = isTop ? world.getBlockState(bottom).getBlock() : this;

			if (!isTop)
			{
				dropBlockAsItem(world, pos, state, 0); //Forge move above the setting to air.
			}

			if (topBlock == this)
			{
				world.setBlockState(top, Blocks.air.getDefaultState(), 3);
			}

			if (bottomBlock == this)
			{
				world.setBlockState(bottom, Blocks.air.getDefaultState(), 3);
			}
		}
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
	{
		if (state.getBlock() != this)
		{
			return super.canBlockStay(world, pos, state); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
		}
		else if ((Boolean) state.getValue(TOP))
		{
			return world.getBlockState(pos.down()).getBlock() == this;
		}
		else
		{
			IBlockState topState = world.getBlockState(pos.up());
			return topState.getBlock() == this && super.canBlockStay(world, pos, topState);
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		if ((Boolean) state.getValue(TOP))
		{
			return null;
		}
		else
		{
			return super.getItemDropped(state, rand, fortune);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		return ColorizerGrass.getGrassColor(0.5D, 1.0D);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state)
	{
		return getBlockColor();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, BlockPos pos, int renderPass)
	{
		return BiomeColorHelper.getGrassColorAtPos(world, pos);
	}

	public void placeAt(World world, BlockPos bottomPos, int flags)
	{
		world.setBlockState(bottomPos, getDefaultState().withProperty(TOP, false), flags);
		world.setBlockState(bottomPos.up(), getDefaultState().withProperty(TOP, true), flags);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		world.setBlockState(pos.up(), getDefaultState().withProperty(TOP, true), 2);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		if ((Boolean) state.getValue(TOP))
		{
			if (world.getBlockState(pos.down()).getBlock() == this)
			{
				if (!player.capabilities.isCreativeMode)
				{
					world.destroyBlock(pos.down(), true);
				}
				else
				{
					world.setBlockToAir(pos.down());
				}
			}
		}
		else if (player.capabilities.isCreativeMode && world.getBlockState(pos.up()).getBlock() == this)
		{
			world.setBlockState(pos.up(), Blocks.air.getDefaultState(), 2);
		}

		super.onBlockHarvested(world, pos, state, player);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return meta > 0 ? getDefaultState().withProperty(TOP, true) : getDefaultState().withProperty(TOP, false);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		if ((Boolean) state.getValue(TOP))
		{
			IBlockState bottomState = world.getBlockState(pos.down());

			if (bottomState.getBlock() == this)
			{
				state = bottomState;
			}
		}

		return state;
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (Boolean) state.getValue(TOP) ? 1 : 0;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] {TOP});
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Block.EnumOffsetType getOffsetType()
	{
		return Block.EnumOffsetType.XZ;
	}

	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		//Forge: Break both parts on the client to prevent the top part flickering as default type for a few frames.
		IBlockState state = world.getBlockState(pos);
		
		if (state.getBlock() == this && !((Boolean) state.getValue(TOP)) && world.getBlockState(pos.up()).getBlock() == this)
		{
			world.setBlockToAir(pos.up());
		}
		
		return world.setBlockToAir(pos);
	}
}
