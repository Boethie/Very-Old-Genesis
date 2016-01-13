package genesis.client.model.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ClassRenderFactory<T extends Entity> implements IRenderFactory<T>
{
	public static <T extends Entity> ClassRenderFactory<T> create(Class<? extends Render<? super T>> renderClass)
	{
		return new ClassRenderFactory<T>(renderClass);
	}
	
	public final Class<? extends Render<? super T>> renderClass;
	
	public ClassRenderFactory(Class<? extends Render<? super T>> renderClass)
	{
		this.renderClass = renderClass;
	}
	
	@Override
	public Render<? super T> createRenderFor(RenderManager manager)
	{
		try
		{
			return renderClass.getConstructor(RenderManager.class).newInstance(manager);
		}
		catch (ReflectiveOperationException e)
		{
			throw new RuntimeException("Failed to construct an entity renderer of type " + renderClass + ".", e);
		}
	}
}
