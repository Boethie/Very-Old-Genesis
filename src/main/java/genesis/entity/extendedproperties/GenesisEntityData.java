package genesis.entity.extendedproperties;

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
	
	public static final String COMPOUND_KEY = Constants.MOD_ID + "EntityData";
	
	private static final Multimap<Class<? extends Entity>, EntityProperty<?>> ENTITY_PROPERTIES = HashMultimap.create();
	
	public static NBTTagCompound getGenesisCompound(NBTTagCompound compound)
	{
		if (!compound.hasKey(COMPOUND_KEY))
		{
			compound.setTag(COMPOUND_KEY, new NBTTagCompound());
		}
		
		return compound.getCompoundTag(COMPOUND_KEY);
	}

	public static <T> void registerProperty(Class<? extends Entity> entityClass, EntityProperty<T> property)
	{
		ENTITY_PROPERTIES.put(entityClass, property);
	}
	
	public static Collection<EntityProperty<?>> getProperties(Class<? extends Entity> entityClass)
	{
		if (!ENTITY_PROPERTIES.containsKey(entityClass))
		{
			Collection<EntityProperty<?>> properties = Sets.newHashSet();
			
			for (Entry<Class<? extends Entity>, Collection<EntityProperty<?>>> check : ENTITY_PROPERTIES.asMap().entrySet())
			{
				if (check.getKey().isAssignableFrom(entityClass))
				{
					for (EntityProperty<?> property : check.getValue())
					{
						properties.add(property);
					}
				}
			}
			
			ENTITY_PROPERTIES.putAll(entityClass, properties);
			return properties;
		}
		
		return ENTITY_PROPERTIES.get(entityClass);
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
			if (!dataMap.containsKey(property))
			{
				dataMap.put(property, property.getDefaultValue());
			}
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
		NBTTagCompound genesisCompound = getGenesisCompound(compound);
		
		for (EntityProperty<?> property : getProperties(entity))
		{
			NBTTagCompound propertyCompound = genesisCompound.getCompoundTag(property.getName());
			saveNBTData(propertyCompound, property, dataMap.get(property));
			genesisCompound.setTag(property.getName(), propertyCompound);
		}
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		NBTTagCompound genesisCompound = getGenesisCompound(compound);
		
		for (EntityProperty<?> property : getProperties(entity.getClass()))
		{
			Object value = property.readFromNBT(genesisCompound.getCompoundTag(property.getName()));
			
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
