package genesis.event;

import genesis.common.Genesis;
import genesis.common.GenesisGuiHandler;
import genesis.common.GenesisPotions;
import genesis.common.sounds.GenesisSoundEvents;
import genesis.world.GenesisWorldData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class GenesisEventHandler
{
	@SubscribeEvent
	public void onSleep(PlayerSleepInBedEvent event)
	{
	}
	
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
		World world = entity.getEntityWorld();
		
		if (!entity.worldObj.isRemote)
		{
			if (entity.isPotionActive(GenesisPotions.radiation))
			{
				int timeLeft = entity.getActivePotionEffect(GenesisPotions.radiation).getDuration();
				
				entity.addPotionEffect(new PotionEffect(MobEffects.confusion, timeLeft, 0, true, false));
				entity.addPotionEffect(new PotionEffect(MobEffects.poison, timeLeft, 0, true, false));
			}
		}
		
		if (entity.getEntityWorld().getRainStrength(1) > 0)
		{
			BiomeGenBase biomegenbase = world.getBiomeGenForCoords(entity.getPosition());
			
			if (!(biomegenbase.canRain() || biomegenbase.getEnableSnow()))
            {
				if (entity.ticksExisted % 30 == 0)
				{
					if(world.canSeeSky(entity.getPosition()))
					{
						if (entity instanceof EntityPlayer)
						{
							world.playSound((EntityPlayer)entity, entity.getPosition(), GenesisSoundEvents.environment_dust_storm, SoundCategory.WEATHER, 2, 0.9F + world.rand.nextFloat() * 0.2F);
						}
					
						if(!world.isRemote)
						{
							entity.motionX+=MathHelper.getRandomDoubleInRange(world.rand, -4, 4);
							entity.motionZ+=MathHelper.getRandomDoubleInRange(world.rand, -4, 4);
							entity.fallDistance+=world.rand.nextFloat()+0.5F;
						}
					}
				}
            }
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event)
	{
		EntityPlayer entity = event.player;
		World world = entity.getEntityWorld();
		if(event.phase == TickEvent.Phase.START)
		{
			if (entity.getEntityWorld().getRainStrength(1) > 0)
			{
				BiomeGenBase biomegenbase = world.getBiomeGenForCoords(entity.getPosition());
			
				if (!(biomegenbase.canRain() || biomegenbase.getEnableSnow()))
				{
						if(world.canSeeSky(entity.getPosition()))
						{	
							if(!world.isRemote)
							{
								double pushDist = entity.isSneaking() ? 0.5 : 1;
								
								entity.motionX+=MathHelper.getRandomDoubleInRange(world.rand, -pushDist, pushDist);
								entity.motionZ+=MathHelper.getRandomDoubleInRange(world.rand, -pushDist, pushDist);
								
								entity.fallDistance+=0.05F;
							}
						}
				}
			}
		}
	}
}
