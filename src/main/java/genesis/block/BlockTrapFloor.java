package genesis.block;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTrapFloor extends BlockGenesis
{
	protected static final AxisAlignedBB BB = new AxisAlignedBB(0, 0.5625, 0, 1, 0.8, 1);
	
	public BlockTrapFloor()
	{
		super(Material.grass, SoundType.PLANT);
		
		setHardness(1.5F);
		
		setResistance(0.3F);
		
		setCreativeTab(GenesisCreativeTabs.MECHANISMS);
		
		useNeighborBrightness = true;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return BB;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
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
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	public void drop(BlockPos pos, World world)
	{
		if(world.isRemote)
			return;
		
		IBlockState iblockstate = world.getBlockState(pos);
		
		if (iblockstate.getBlock() != this)
			return;
		
		world.playAuxSFXAtEntity(null, 2001, pos, Block.getStateId(iblockstate));
		
		world.setBlockState(pos, Blocks.air.getDefaultState(), 1 | 2);
		
		world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, 0, 0, 0, new int[]{Block.getStateId(iblockstate)});
		world.spawnParticle(EnumParticleTypes.BLOCK_DUST, pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, 0, 0, 0, new int[]{Block.getStateId(iblockstate)});
		
		for (EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			BlockPos pos1 = pos.offset(facing);
			
			if (world.getBlockState(pos1).getBlock() == this)
			{
				((BlockTrapFloor)world.getBlockState(pos1).getBlock()).drop(pos1, world);
			}
		}
	}
	
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);

        if (worldIn.isBlockPowered(pos))
        {
            drop(pos, worldIn);
        }
    }
	
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (worldIn.isBlockPowered(pos))
        {
            drop(pos, worldIn);
        }
    }
	
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
		if (entityIn instanceof EntityLivingBase || (entityIn instanceof EntityFallingBlock && ((EntityFallingBlock)entityIn).getBlock().getBlock() == Blocks.anvil))
        {
			drop(pos, worldIn);
        }
    }
	
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
		if (entityIn instanceof EntityLivingBase || (entityIn instanceof EntityFallingBlock && ((EntityFallingBlock)entityIn).getBlock().getBlock() == Blocks.anvil))
        {
			drop(pos, worldIn);
        }
    }
}
