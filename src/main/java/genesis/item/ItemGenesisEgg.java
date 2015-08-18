package genesis.item;

import java.lang.reflect.Constructor;

import genesis.common.GenesisBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemGenesisEgg<T extends Entity> extends ItemGenesis
{
	public final Class<T> entityClass;
	
	public ItemGenesisEgg(Class<T> entityClass)
	{
		this.entityClass = entityClass;
	}
	
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
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,
			BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
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
						Constructor<T> constructor = entityClass.getConstructor(World.class);
						entity = constructor.newInstance(world);
					}
					catch (ReflectiveOperationException e)
					{
						throw new RuntimeException("Could not construct egg entity from class " + entityClass + " to spawn.", e);
					}
					
					entity.setPositionAndUpdate(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
					setData(entity, stack, player, world, pos, side, hitX, hitY, hitZ);
					
					world.spawnEntityInWorld(entity);
					stack.stackSize--;
				}
				
				return true;
			}
		}
		
		return false;
	}
}
