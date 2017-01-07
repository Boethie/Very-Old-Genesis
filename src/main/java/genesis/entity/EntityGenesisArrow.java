package genesis.entity;

import genesis.combo.variant.EnumArrowShaft;
import genesis.combo.variant.EnumToolMaterial;
import genesis.common.GenesisItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityGenesisArrow extends EntityTippedArrow implements IEntityAdditionalSpawnData
{
	private ItemStack stack;
    
	public EntityGenesisArrow(World worldIn)
	{
		super(worldIn);
		stack = GenesisItems.ARROWS.getStack(EnumArrowShaft.WOOD, EnumToolMaterial.BLACK_FLINT);
	}

	public EntityGenesisArrow(World worldIn, EntityLivingBase shooter, ItemStack stack)
	{
		super(worldIn, shooter);
		this.stack = stack;
		// Copied just for completeness' sake, probably won't do anything
		//setPotionEffect(stack);	Removed by FirEmerald since the code it calls checks against the item, and therefor has no effect here
	}

	@Override
	public void setAim(Entity shooter, float pitch, float yaw, float unused, float velocity, float spread)
	{
		float mass = GenesisItems.ARROWS.getVariant(stack).getMass();
		setDamage(getDamage() * mass);
		super.setAim(shooter, pitch, yaw, unused, velocity / mass, spread / mass);
	}

	@Override
	public ItemStack getArrowStack()
	{
		return stack;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag)
	{
		super.writeEntityToNBT(tag);
		NBTTagCompound stackTag = new NBTTagCompound();
		stack.writeToNBT(stackTag);
		tag.setTag("stack", stackTag);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag)
	{
		super.readEntityFromNBT(tag);
		stack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("stack"));
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		ByteBufUtils.writeItemStack(buffer, stack);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		stack = ByteBufUtils.readItemStack(additionalData);
	}
}
