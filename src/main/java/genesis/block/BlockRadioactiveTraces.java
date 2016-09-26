package genesis.block;

import genesis.common.GenesisPotions;
import genesis.util.AABBUtils;
import genesis.util.FacingHelpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockRadioactiveTraces extends BlockGenesisRock
{
	public BlockRadioactiveTraces()
	{
		super(4.2F, 15);
		setTickRandomly(true);
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (!world.isRemote)
		{
			AxisAlignedBB bb = AABBUtils.create(pos).expandXyz(3);
			List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
			
			for(EntityLivingBase entity : entities)
			{
				int duration = rand.nextInt(10) == 0 ? MathHelper.getRandomIntegerInRange(rand, 200, 400) : 200;
				int amplifier = rand.nextInt(20) == 0 ? 1 : 0;
				entity.addPotionEffect(new PotionEffect(GenesisPotions.RADIATION, duration, amplifier));
			}
		}
	}
	
	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}
	
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
	{
		if (rand.nextInt(2) == 0)
		{
			spawnParticles(world, pos, rand, 1);
		}
	}
	
	private static void spawnParticles(World world, BlockPos pos, Random rand, int count)
	{
		Vec3d center = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
		double outset = 0.5625;
		
		for (EnumFacing face : EnumFacing.VALUES)
		{
			if (!world.getBlockState(pos.offset(face)).isOpaqueCube())
			{
				Vec3d sideCoord = center
						.add(new Vec3d(face.getDirectionVec()).scale(outset));
				Vec3d u = new Vec3d(FacingHelpers.getU(face));
				Vec3d v = new Vec3d(FacingHelpers.getV(face));
				
				for (int i = 0; i < count; i++)
				{
					Vec3d particle = sideCoord
							.add(u.scale(rand.nextDouble() - 0.5))
							.add(v.scale(rand.nextDouble() - 0.5));
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particle.xCoord, particle.yCoord, particle.zCoord, 0, 0, 0);
				}
			}
		}
	}
	
	@Override
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager particleManager)
	{
		spawnParticles(world, pos, world.rand, 2);
		return false;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if (!world.isRemote)
		{
			AxisAlignedBB bb = AABBUtils.create(pos).expandXyz(5);
			List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
			
			for (EntityLivingBase entity : entities)
			{
				int duration = MathHelper.getRandomIntegerInRange(world.rand, 250, 400);
				int amplifier = 1;
				entity.addPotionEffect(new PotionEffect(GenesisPotions.RADIATION, duration, amplifier));
			}
		}
	}
	
	@Override
	public int tickRate(World world)
	{
		return 5;
	}
	
	@Override
	public boolean requiresUpdates()
	{
		return true;
	}
}
