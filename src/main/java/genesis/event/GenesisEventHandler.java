package genesis.event;

import genesis.block.BlockSmoker;
import genesis.common.Genesis;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisGuiHandler;
import genesis.common.GenesisPotions;
import genesis.world.GenesisWorldData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
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
		if (event.getGui() != null && event.getGui().getClass() == GuiAchievements.class)
		{
			event.setCanceled(true);
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			player.openGui(Genesis.instance, GenesisGuiHandler.GENESIS_ACHIEVEMENT_ID, player.worldObj, player.getPosition().getX(),
					player.getPosition().getY(), player.getPosition().getZ());
		}
	}

	@SubscribeEvent
	public void onLivingTick(LivingUpdateEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();

		if (entity != null && !entity.worldObj.isRemote)
		{
			PotionEffect effect = entity.getActivePotionEffect(GenesisPotions.RADIATION);
			if (effect != null)
			{
				int timeLeft = effect.getDuration();

				entity.addPotionEffect(new PotionEffect(MobEffects.POISON, timeLeft, 0, true, false));
				if (effect.getAmplifier() >= 5) {
					entity.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, timeLeft, 0, true, false));
				}
			}

			if (entity.getAge() % 10 == 0 && entity.worldObj.getBlockState(entity.getPosition()).getMaterial().isLiquid() && (entity.worldObj.getBlockState(entity.getPosition().down()).getBlock() instanceof BlockSmoker || entity.worldObj.getBlockState(entity.getPosition().down(2)).getBlock() instanceof BlockSmoker))
				entity.attackEntityFrom(DamageSource.inFire, 1.0F);
		}
	}

	@SubscribeEvent
	public void bucketUse(FillBucketEvent event)
	{
		boolean flag = event.getWorld().getBlockState(event.getTarget().getBlockPos()).getBlock().isReplaceable(event.getWorld(), event.getTarget().getBlockPos());
		BlockPos pos = flag && event.getTarget().sideHit == EnumFacing.UP ? event.getTarget().getBlockPos() : event.getTarget().getBlockPos().offset(event.getTarget().sideHit);
		if (event.getWorld().getBlockState(pos).getBlock() == GenesisBlocks.SMOKER)
			event.setCanceled(true);
	}
}
