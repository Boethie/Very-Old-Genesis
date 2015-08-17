package genesis.common;

import genesis.entity.*;
import genesis.entity.fixed.EntityMeganeuraEgg;
import genesis.entity.flying.*;
import genesis.util.Constants;
import net.minecraft.entity.*;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.*;

public class GenesisEntities
{
	protected static int modID = 0;
	
	public static void registerEntities()
	{
		registerEntity(EntityMeganeura.class, "Meganeura", 160, 1, true, 0, 0);
		registerEntity(EntityMeganeuraEgg.class, "MeganeuraEgg", 160, Integer.MAX_VALUE, false);
	}
	
	protected static void registerEntity(Class<? extends Entity> clazz, String name, int trackRange, int trackFrequency, boolean trackVelocity)
	{
		EntityRegistry.registerGlobalEntityID(clazz, name, EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(clazz, name, modID++, Genesis.instance, trackRange, trackFrequency, trackVelocity);
	}
	
	protected static void registerEntity(Class<? extends Entity> clazz, String name)
	{
		registerEntity(clazz, name, 80, Integer.MAX_VALUE, false);
	}
	
	protected static void registerEntity(Class<? extends Entity> clazz, String name, int trackRange, int trackFrequency, boolean trackVelocity, int primaryColor, int secondaryColor)
	{
		registerEntity(clazz, name, trackRange, trackFrequency, trackVelocity);
		EntityRegistry.registerEgg(clazz, primaryColor, secondaryColor);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerEntityRenderers()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityMeganeura.class, new EntityMeganeura.Render());
		RenderingRegistry.registerEntityRenderingHandler(EntityMeganeuraEgg.class, new EntityMeganeuraEgg.EggRender());
	}
}
