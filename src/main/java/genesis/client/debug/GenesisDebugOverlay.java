package genesis.client.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class GenesisDebugOverlay
{
	private static final boolean DEOBF = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

	public static void register()
	{
		MinecraftForge.EVENT_BUS.register(new GenesisDebugOverlay());
	}

	@SubscribeEvent
	public void afterGameRendered(RenderTickEvent event)
	{
		if (DEOBF && event.phase == Phase.END)
		{
			Minecraft mc = Minecraft.getMinecraft();

			if (mc.theWorld == null)
			{
				mc.fontRendererObj.drawString(Integer.toString(Minecraft.getDebugFPS()), 2, 2,
						mc.currentScreen instanceof GuiMainMenu ? 0x222222 : 0xDDDDDD);
			}
		}
	}
}
