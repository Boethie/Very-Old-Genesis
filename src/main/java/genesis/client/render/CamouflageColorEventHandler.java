package genesis.client.render;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CamouflageColorEventHandler
{
	public static int color = 0x007f00;
	
	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Pre event)
	{
		EntityPlayer player = event.getEntityPlayer();
		BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
		color = BiomeColorHelper.getGrassColorAtPos(player.worldObj, pos);
	}
}
