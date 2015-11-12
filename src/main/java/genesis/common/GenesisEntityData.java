package genesis.common;

import genesis.util.*;

import java.util.*;
import java.util.Map.Entry;

import com.google.common.collect.*;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class GenesisEntityData implements IExtendedEntityProperties
{
	public static class EventHandler
	{
		public static EventHandler INSTANCE = new EventHandler();
		
		@SubscribeEvent
		public void onEntityConstructed(EntityConstructing event)
		{
			if (isEntityRegistered(event.entity))
			{
				event.entity.registerExtendedProperties(Constants.MOD_ID, new GenesisEntityData());
			}
		}
	}
	
	public static void register()
	{
		MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);
	}
	
	public static interface EntityProperty<T>
	{
		public String getName();
		public void writeToNBT(T value, NBTTagCompound compound);
		public T readFromNBT(NBTTagCompound compound);
		public T getDefaultValue();
	}
	
	public static abstract class EntityPropertyBase<T> implements EntityProperty<T>
	{
		protected final String name;
		protected final T defaultValue;
		protected final boolean writeToNBT;
		
		public EntityPropertyBase(String name, T defaultValue, boolean writeToNBT)
		{
			this.name = name;
			this.defaultValue = defaultValue;
			this.writeToNBT = writeToNBT;
		}
		
		public String getName()
		{
			return name;
		}
		
		public T getDefaultValue()
		{
			return defaultValue;
		}
		
		public abstract void doWriteToNBT(T value, NBTTagCompound compound);
		public abstract T doReadFromNBT(NBTTagCompound compound);
		
		@Override
		public void writeToNBT(T value, NBTTagCompound compound)
		{
			if (writeToNBT)
			{
				doWriteToNBT(value, compound);
			}
		}
		
		@Override
		public T readFromNBT(NBTTagCompound compound)
		{
			return writeToNBT ? doReadFromNBT(compound) : null;
		}
	}
	
	public static class IntegerEntityProperty extends EntityPropertyBase<Integer>
	{
		public IntegerEntityProperty(String name, Integer defaultValue, boolean write)
		{
			super(name, defaultValue, write);
		}
		
		@Override
		public void doWriteToNBT(Integer value, NBTTagCompound compound)
		{
			compound.setInteger(getName(), value);
		}
		
		@Override
		public Integer doReadFromNBT(NBTTagCompound compound)
		{
			return compound.getInteger(getName());
		}
	}
	
	private static final Multimap<Class<? extends Entity>, EntityProperty<?>> properties = HashMultimap.create();

	public static <T> void registerProperty(Class<? extends Entity> entityClass, EntityProperty<T> property)
	{
		properties.put(entityClass, property);
	}
	
	public static Collection<EntityProperty<?>> getProperties(Class<? extends Entity> entityClass)
	{
		if (!properties.containsKey(entityClass))
		{
			for (Entry<Class<? extends Entity>, Collection<EntityProperty<?>>> check : properties.asMap().entrySet())
			{
				if (check.getKey().isAssignableFrom(entityClass))
				{
					for (EntityProperty<?> property : check.getValue())
					{
						properties.put(entityClass, property);
					}
				}
			}
		}
		
		return properties.get(entityClass);
	}

	public static Collection<EntityProperty<?>> getProperties(Entity entity)
	{
		return getProperties(entity.getClass());
	}
	
	public static boolean isEntityRegistered(Class<? extends Entity> entityClass)
	{
		return !getProperties(entityClass).isEmpty();
	}
	
	public static boolean isEntityRegistered(Entity entity)
	{
		return isEntityRegistered(entity.getClass());
	}
	
	public static boolean isPropertyRegisteredToEntity(Class<? extends Entity> entityClass, EntityProperty<?> property)
	{
		return isEntityRegistered(entityClass) && getProperties(entityClass).contains(property);
	}
	
	public static boolean isPropertyRegisteredToEntity(Entity entity, EntityProperty<?> property)
	{
		return isPropertyRegisteredToEntity(entity.getClass(), property);
	}
	
	public static GenesisEntityData getData(Entity entity)
	{
		if (!isEntityRegistered(entity))
		{
			throw new RuntimeException("Cannot get properties for entity " + entity + " as no properties have been registered for that type.");
		}
		
		return (GenesisEntityData) entity.getExtendedProperties(Constants.MOD_ID);
	}
	
	public static <T> T getValue(Entity entity, EntityProperty<T> property)
	{
		return getData(entity).getValue(property);
	}
	
	public static <T> void setValue(Entity entity, EntityProperty<T> property, T value)
	{
		getData(entity).setValue(property, value);
	}
	
	private Entity entity;
	private final Map<EntityProperty<?>, Object> dataMap = Maps.newHashMap();
	
	@Override
	public void init(Entity entity, World world)
	{
		this.entity = entity;

		for (EntityProperty<?> property : getProperties(entity))
		{
			dataMap.put(property, property.getDefaultValue());
		}
	}
	
	@SuppressWarnings("unchecked")
	protected <T> void saveNBTData(NBTTagCompound compound, EntityProperty<T> property, Object value)
	{
		property.writeToNBT((T) value, compound);
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		for (EntityProperty<?> property : getProperties(entity))
		{
			saveNBTData(compound, property, dataMap.get(property));
		}
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		for (EntityProperty<?> property : getProperties(entity.getClass()))
		{
			Object value = property.readFromNBT(compound);
			
			if (value != null)
			{
				dataMap.put(property, value);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(EntityProperty<T> property)
	{
		return (T) dataMap.get(property);
	}
	
	public <T> void setValue(EntityProperty<T> property, T value)
	{
		if (!isPropertyRegisteredToEntity(entity, property))
		{
			throw new IllegalArgumentException("Cannot set property " + property + " for entity " + entity + "," +
					"as the property has not been registered to that entity class.");
		}
		
		dataMap.put(property, value);
	}
}
