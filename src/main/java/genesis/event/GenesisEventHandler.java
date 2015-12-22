package genesis.event;

import genesis.world.GenesisWorldData;
import net.minecraft.world.World;
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
			GenesisWorldData data = GenesisWorldData.get(world);
			
			if (data != null)
			{
				data.setTime(data.getTime() + 1);
			}
		}
	}
}
