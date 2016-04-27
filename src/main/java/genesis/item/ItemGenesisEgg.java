package genesis.item;

import java.lang.reflect.Constructor;

import genesis.common.GenesisBlocks;
import genesis.entity.fixed.EntityFixed;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class ItemGenesisEgg<T extends EntityFixed> extends ItemGenesis
{
	public final Class<T> entityClass;
	private final Constructor<T> entityConstructor;
	
	public ItemGenesisEgg(Class<T> entityClass)
	{
		this.entityClass = entityClass;
		
		try
		{
			entityConstructor = entityClass.getConstructor(World.class);
		}
		catch (ReflectiveOperationException e)
		{
			throw new RuntimeException("Could not construct egg entity from class " + entityClass + " to spawn.", e);
		}
	}
	
	@Override
	public ItemGenesisEgg<T> setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(name);
		return this;
	}
	
	protected void setData(T entity, ItemStack stack, EntityPlayer player, World world,
			BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world,
			BlockPos pos, EnumHand hand, EnumFacing side,
			float hitX, float hitY, float hitZ)
	{
		if (stack.stackSize > 0)
		{
			IBlockState state = world.getBlockState(pos);
			
			if (state.getBlock() == GenesisBlocks.calamites)
			{
				if (!world.isRemote)
				{
					T entity;
					
					try
					{
						entity = entityConstructor.newInstance(world);
					}
					catch (ReflectiveOperationException e)
					{
						throw new RuntimeException("Could not construct egg entity from class " + entityClass + " to spawn.", e);
					}
					
					entity.setLocationAndAngles(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ, 0, 0);
					entity.setFixedTo(pos);
					setData(entity, stack, player, world, pos, side, hitX, hitY, hitZ);
					
					world.spawnEntityInWorld(entity);
					
					stack.stackSize--;
				}
				
				return EnumActionResult.SUCCESS;
			}
		}
		
		return EnumActionResult.FAIL;
	}
}
