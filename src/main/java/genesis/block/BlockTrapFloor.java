package genesis.block;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTrapFloor extends BlockGenesis
{
	public BlockTrapFloor()
	{
		super(Material.grass);
		setCreativeTab(GenesisCreativeTabs.MECHANISMS);
		setBlockBounds(0, 9/16F, 0, 1, 1, 1);
		setStepSound(soundTypeGrass);
		useNeighborBrightness = true;
	}
	
	@Override
	public boolean isFullCube()
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 30;
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
		return this.getBlockColor();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
	{
		return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}
}
