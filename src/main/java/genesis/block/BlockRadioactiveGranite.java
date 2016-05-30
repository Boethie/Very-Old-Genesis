package genesis.block;

import java.util.List;
import java.util.Random;

import genesis.common.GenesisPotions;
import genesis.util.AABBUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRadioactiveGranite extends BlockGenesisRock
{
	public BlockRadioactiveGranite()
	{
		super(2.7F, 7.0F, 2);
		setLightLevel(0.1F);
		setTickRandomly(true);
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (!world.isRemote)
		{
			AxisAlignedBB bb = AABBUtils.create(pos).expandXyz(3.0D);
			List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
			
			for(EntityLivingBase entity : entities)
			{
				int duration = rand.nextInt(10) == 0 ? MathHelper.getRandomIntegerInRange(rand, 200, 400) : 200;
				int amplifier = rand.nextInt(20) == 0 ? 1 : 0;
				entity.addPotionEffect(new PotionEffect(GenesisPotions.radiation, duration, amplifier));
			}
		}
	}
	
	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
	{
		if(rand.nextBoolean())
			spawnParticles(world, pos);
	}
	
	private void spawnParticles(World world, BlockPos pos)
	{
		Random random = world.rand;
		double x = (double) pos.getX();
		double y = (double) pos.getY();
		double z = (double) pos.getZ();
		double d0 = 0.0625D;
		
		for (int i = 0; i < 6; ++i)
		{
			double particleX = (double) ((float) x + random.nextFloat());
			double particleY = (double) ((float) y + random.nextFloat());
			double particleZ = (double) ((float) z + random.nextFloat());
			
			if (i == 0 && !world.getBlockState(pos.up()).isOpaqueCube())
			{
				particleY = y + d0 + 1.0D;
			}
			
			if (i == 1 && !world.getBlockState(pos.down()).isOpaqueCube())
			{
				particleY = y - d0;
			}
			
			if (i == 2 && !world.getBlockState(pos.south()).isOpaqueCube())
			{
				particleZ = z + d0 + 1.0D;
			}
			
			if (i == 3 && !world.getBlockState(pos.north()).isOpaqueCube())
			{
				particleZ = z - d0;
			}
			
			if (i == 4 && !world.getBlockState(pos.east()).isOpaqueCube())
			{
				particleX = x + d0 + 1.0D;
			}
			
			if (i == 5 && !world.getBlockState(pos.west()).isOpaqueCube())
			{
				particleX = x - d0;
			}
			
			if (particleX < x || particleX > x + 1.0D || particleY < 0.0D || particleY > y + 1.0D || particleZ < z || particleZ > z + 1.0D)
			{
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particleX, particleY, particleZ, 0.0D, 0.0D, 0.0D);
			}
		}
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		spawnParticles(world, pos);
		spawnParticles(world, pos);
		
		if (!world.isRemote)
		{
			AxisAlignedBB bb = AABBUtils.create(pos).expandXyz(5.0D);
			List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
			
			for(EntityLivingBase entity : entities)
			{
				int duration = MathHelper.getRandomIntegerInRange(world.rand, 250, 400);
				int amplifier = 1;
				entity.addPotionEffect(new PotionEffect(GenesisPotions.radiation, duration, amplifier));
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
