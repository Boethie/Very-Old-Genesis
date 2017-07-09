package genesis.event;

import genesis.block.BlockSmoker;
import genesis.common.Genesis;
import genesis.common.GenesisGuiHandler;
import genesis.common.GenesisPotions;
import genesis.util.blocks.IAquaticBlock;
import genesis.util.blocks.ISitOnBlock;
import genesis.world.GenesisWorldData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class GenesisEventHandler
{
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event)
	{
		if (event.phase == Phase.START)
		{
			World world = event.world;

			if (world.getGameRules().getBoolean("doDaylightCycle"))
			{
				GenesisWorldData data = GenesisWorldData.get(world);

				if (data != null)
				{
					data.setTime(data.getTime() + 1);
				}
			}
		}
	}

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event)
	{
		GuiScreen gui = event.getGui();
		if (gui != null && gui.getClass() == GuiAchievements.class)
		{
			event.setCanceled(true);
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			BlockPos pos = player.getPosition();
			player.openGui(Genesis.instance, GenesisGuiHandler.GENESIS_ACHIEVEMENT_ID, player.worldObj, pos.getX(), pos.getY(), pos.getZ());
		}
	}

	@SubscribeEvent
	public void onLivingTick(LivingUpdateEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();

		if (entity != null)
		{
			World world = entity.worldObj;

			if (world.isRemote)
			{
				return;
			}

			PotionEffect effect = entity.getActivePotionEffect(GenesisPotions.RADIATION);
			if (effect != null)
			{
				int timeLeft = effect.getDuration();

				entity.addPotionEffect(new PotionEffect(MobEffects.POISON, timeLeft, 0, true, false));
				if (effect.getAmplifier() >= 5)
				{
					entity.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, timeLeft, 0, true, false));
				}
			}

			BlockPos pos = entity.getPosition();
			if (entity.getAge() % 10 == 0 && world.getBlockState(pos).getMaterial().isLiquid())
			{
				for (int i = 1; i <= 2; i++)
				{
					if (world.getBlockState(pos.down(i)).getBlock() instanceof BlockSmoker)
					{
						entity.attackEntityFrom(DamageSource.inFire, 1.0F);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onFillBucket(FillBucketEvent event)
	{
		World world = event.getWorld();
		RayTraceResult target = event.getTarget();
		BlockPos targetPos = target.getBlockPos();
		boolean replaceable = world.getBlockState(targetPos).getBlock().isReplaceable(world, targetPos);
		BlockPos pos = replaceable && target.sideHit == EnumFacing.UP ? targetPos : targetPos.offset(target.sideHit);
		if (world.getBlockState(pos).getBlock() instanceof IAquaticBlock)
		{
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onHarvestBlockDrops(BlockEvent.HarvestDropsEvent event)
	{
		World world = event.getWorld();

		if (!world.isRemote)
		{
			BlockPos abovePos = event.getPos().up();
			IBlockState above = world.getBlockState(abovePos);
			Block aboveBlock = above.getBlock();

			if (aboveBlock instanceof ISitOnBlock)
			{
				aboveBlock.dropBlockAsItem(world, abovePos, above, 0);
				world.setBlockState(event.getPos(), ((ISitOnBlock) aboveBlock).getReplacement(world, abovePos, above));
			}
		}
	}
}
