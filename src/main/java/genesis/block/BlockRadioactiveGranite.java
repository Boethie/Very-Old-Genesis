package genesis.block;

import java.util.List;
import java.util.Random;

import genesis.common.GenesisPotions;
import genesis.util.Constants.Unlocalized;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
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
		this.setLightLevel(0.1F);
		setUnlocalizedName(Unlocalized.ROCK + "radioactiveGranite");
		setTickRandomly(true);
	}


	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
		
		if(worldIn.isRemote)return;
		
		AxisAlignedBB bb = new AxisAlignedBB(pos.getX()-3,pos.getY()-3, pos.getZ()-3,pos.getX()+3, pos.getY()+3, pos.getZ()+3);
		
		List<EntityLivingBase> lp = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, bb);
		
		for(EntityLivingBase p : lp)
		{
			p.addPotionEffect(new PotionEffect(GenesisPotions.radiation, rand.nextInt(10)==0 ? MathHelper.getRandomIntegerInRange(rand, 200, 400) : 200, rand.nextInt(20)==0 ? 1 : 0));
		}
	}
	
	public int quantityDropped(Random r)
	{
		return 0;
	}
	
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
		if(rand.nextBoolean())
			spawnParticles(worldIn, pos);
    }
	
	private void spawnParticles(World worldIn, BlockPos pos)
    {
        Random random = worldIn.rand;
        double d0 = 0.0625D;

        for (int i = 0; i < 6; ++i)
        {
            double d1 = (double)((float)pos.getX() + random.nextFloat());
            double d2 = (double)((float)pos.getY() + random.nextFloat());
            double d3 = (double)((float)pos.getZ() + random.nextFloat());

            if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube())
            {
                d2 = (double)pos.getY() + d0 + 1.0D;
            }

            if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube())
            {
                d2 = (double)pos.getY() - d0;
            }

            if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube())
            {
                d3 = (double)pos.getZ() + d0 + 1.0D;
            }

            if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube())
            {
                d3 = (double)pos.getZ() - d0;
            }

            if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube())
            {
                d1 = (double)pos.getX() + d0 + 1.0D;
            }

            if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube())
            {
                d1 = (double)pos.getX() - d0;
            }

            if (d1 < (double)pos.getX() || d1 > (double)(pos.getX() + 1) || d2 < 0.0D || d2 > (double)(pos.getY() + 1) || d3 < (double)pos.getZ() || d3 > (double)(pos.getZ() + 1))
            {
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d1, d2, d3, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }
    }
	
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		spawnParticles(worldIn, pos);
		spawnParticles(worldIn, pos);
		
		if(worldIn.isRemote)return;
		
		Random rand = worldIn.rand;
		
		AxisAlignedBB bb = new AxisAlignedBB(pos.getX()-5, pos.getY()-5, pos.getZ()-5, pos.getX()+5, pos.getY()+5, pos.getZ()+5);
		
		List<EntityLivingBase> lp = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, bb);
		
		for(EntityLivingBase p : lp)
		{
			p.addPotionEffect(new PotionEffect(GenesisPotions.radiation, MathHelper.getRandomIntegerInRange(rand, 250, 400), 1));
		}

        super.breakBlock(worldIn, pos, state);
    }
	
	public int tickRate(World worldIn)
    {
		return 5;
    }
	
	public boolean requiresUpdates()
    {
        return true;
    }
}
