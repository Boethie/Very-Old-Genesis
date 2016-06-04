package genesis.event;

import genesis.common.Genesis;
import genesis.common.GenesisGuiHandler;
import genesis.common.GenesisPotions;
import genesis.world.GenesisWorldData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
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
		EntityLivingBase elb = event.getEntityLiving();
		
		if(elb != null && !elb.worldObj.isRemote)
		{
			if(elb.isPotionActive(GenesisPotions.radiation))
			{
				elb.addPotionEffect(new PotionEffect(Potion.potionRegistry.getObject(new ResourceLocation("glowing")), 100, 0));
				elb.addPotionEffect(new PotionEffect(Potion.potionRegistry.getObject(new ResourceLocation("nausea")), 100, 0));
			}
		}
	}
}
