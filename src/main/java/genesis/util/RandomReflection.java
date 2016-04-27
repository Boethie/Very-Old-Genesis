package genesis.util;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class RandomReflection
{
	private static final Field blockHitDelay = ReflectionHelper.findField(PlayerControllerMP.class, "blockHitDelay", "field_78781_i");
	
	public static int getBlockHitDelay()
	{
		try
		{
			return blockHitDelay.getInt(Minecraft.getMinecraft().playerController);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static void setBlockHitDelay(int value)
	{
		try
		{
			blockHitDelay.setInt(Minecraft.getMinecraft().playerController, value);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
}
