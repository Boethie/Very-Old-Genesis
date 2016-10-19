package genesis.common;

import genesis.client.model.entity.ClassRenderFactory;
import genesis.client.model.entity.RenderMeganeura;
import genesis.client.render.RenderGenesisArrow;
import genesis.entity.EntityGenesisArrow;
import genesis.entity.fixed.EntityMeganeuraEgg;
import genesis.entity.living.flying.EntityMeganeura;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GenesisEntities
{
	protected static int modID = 0;

	protected static void registerEntity(Class<? extends Entity> clazz, String name, int trackRange, int trackFrequency, boolean trackVelocity)
	{
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

	public static void registerEntities()
	{
		registerEntity(EntityMeganeura.class, "meganeura", 160, 1, true, 0xB5C457, 0x7E8B3A);
		DataSerializers.registerSerializer(EntityMeganeura.STATE.getSerializer());
		registerEntity(EntityMeganeuraEgg.class, "meganeuraEgg", 160, Integer.MAX_VALUE, false);
		registerEntity(EntityGenesisArrow.class, "arrow", 160, Integer.MAX_VALUE, false);
	}

	@SideOnly(Side.CLIENT)
	public static void registerEntityRenderers()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityMeganeura.class, ClassRenderFactory.create(RenderMeganeura.class));
		RenderingRegistry.registerEntityRenderingHandler(EntityMeganeuraEgg.class, ClassRenderFactory.create(EntityMeganeuraEgg.EggRender.class));
		RenderingRegistry.registerEntityRenderingHandler(EntityGenesisArrow.class, ClassRenderFactory.create(RenderGenesisArrow.class));
	}
}
