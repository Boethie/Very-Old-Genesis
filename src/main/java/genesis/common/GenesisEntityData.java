package genesis.common;

import genesis.common.GenesisEntityData.IntegerEntityProperty;
import genesis.util.*;

import java.util.*;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.collect.*;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.*;
import net.minecraftforge.fml.common.eventhandler.*;

@SuppressWarnings({"rawtypes", "unchecked"})
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
	
	public static void registerHandler()
	{
		MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);
	}
	
	public static interface EntityProperty<T>
	{
		public String getName();
		public void writeToNBT(T value, NBTTagCompound compound);
		public T readFromNBT(NBTTagCompound compound);
	}
	
	public static abstract class NamedEntityProperty<T> implements EntityProperty<T>
	{
		private final String name;
		
		public NamedEntityProperty(String name)
		{
			this.name = name;
		}
		
		public String getName()
		{
			return name;
		}
	}
	
	public static class IntegerEntityProperty extends NamedEntityProperty<Integer>
	{
		public IntegerEntityProperty(String name)
		{
			super(name);
		}
		
		@Override
		public void writeToNBT(Integer value, NBTTagCompound compound)
		{
			compound.setInteger(getName(), value);
		}
		
		@Override
		public Integer readFromNBT(NBTTagCompound compound)
		{
			return compound.getInteger(getName());
		}
	}
	
	public static class PropertySettings<T>
	{
		public final EntityProperty<T> property;
		public final T defaultValue;
		public final boolean saveToNBT;
		
		public PropertySettings(EntityProperty<T> property, T defaultValue, boolean saveToNBT)
		{
			this.property = property;
			this.defaultValue = defaultValue;
			this.saveToNBT = saveToNBT;
		}
	}
	
	private static final Table<Class<? extends Entity>, EntityProperty, PropertySettings> properties = HashBasedTable.create();

	public static <T> void registerProperty(Class<? extends Entity> entityClass, EntityProperty<T> property, T defaultValue, boolean saveToNBT)
	{
		properties.put(entityClass, property, new PropertySettings<T>(property, defaultValue, saveToNBT));
	}
	
	public static Map<EntityProperty, PropertySettings> getPropertyMap(Class<? extends Entity> entityClass)
	{
		if (properties.containsRow(entityClass))
		{
			return properties.row(entityClass);
		}
		
		for (Entry<Class<? extends Entity>, Map<EntityProperty, PropertySettings>> check : properties.rowMap().entrySet())
		{
			if (check.getKey().isAssignableFrom(check.getKey()))
			{
				for (Entry<EntityProperty, PropertySettings> entry : check.getValue().entrySet())
				{
					properties.put(entityClass, entry.getKey(), entry.getValue());
				}
				
				return check.getValue();
			}
		}
		
		return null;
	}

	public static Map<EntityProperty, PropertySettings> getPropertyMap(Entity entity)
	{
		return getPropertyMap(entity.getClass());
	}
	
	public static boolean isEntityRegistered(Class<? extends Entity> entityClass)
	{
		Map<EntityProperty, PropertySettings> propertyMap = getPropertyMap(entityClass);
		return propertyMap != null && !propertyMap.isEmpty();
	}
	
	public static boolean isEntityRegistered(Entity entity)
	{
		return isEntityRegistered(entity.getClass());
	}
	
	public static boolean isPropertyRegisteredToEntity(Class<? extends Entity> entityClass, EntityProperty property)
	{
		return isEntityRegistered(entityClass) && getPropertyMap(entityClass).containsKey(property);
	}
	
	public static boolean isPropertyRegisteredToEntity(Entity entity, EntityProperty property)
	{
		return isPropertyRegisteredToEntity(entity.getClass(), property);
	}
	
	public static GenesisEntityData getProperties(Entity entity)
	{
		if (!isEntityRegistered(entity))
		{
			throw new RuntimeException("Cannot get properties for entity " + entity + " as no properties have been registered for that type.");
		}
		
		GenesisEntityData properties = (GenesisEntityData) entity.getExtendedProperties(Constants.MOD_ID);
		return properties;
	}
	
	public static <T> T getValue(Entity entity, EntityProperty<T> property)
	{
		return getProperties(entity).getValue(property);
	}
	
	public static <T> void setValue(Entity entity, EntityProperty<T> property, T value)
	{
		getProperties(entity).setValue(property, value);
	}
	
	private Entity entity;
	private final Map<EntityProperty, Object> dataMap = Maps.newHashMap();
	
	@Override
	public void init(Entity entity, World world)
	{
		this.entity = entity;

		for (Entry<EntityProperty, PropertySettings> entry : getPropertyMap(entity).entrySet())
		{
			dataMap.put(entry.getKey(), entry.getValue().defaultValue);
		}
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		for (Entry<EntityProperty, PropertySettings> entry : getPropertyMap(entity).entrySet())
		{
			if (entry.getValue().saveToNBT)
			{
				entry.getKey().writeToNBT(dataMap.get(entry.getKey()), compound);
			}
		}
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		for (Entry<EntityProperty, PropertySettings> entry : getPropertyMap(entity.getClass()).entrySet())
		{
			if (entry.getValue().saveToNBT)
			{
				dataMap.put(entry.getKey(), entry.getKey().readFromNBT(compound));
			}
		}
	}
	
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
