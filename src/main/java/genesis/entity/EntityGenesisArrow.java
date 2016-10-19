package genesis.entity;

import genesis.combo.variant.EnumArrowShaft;
import genesis.combo.variant.EnumToolMaterial;
import genesis.common.GenesisItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityGenesisArrow extends EntityTippedArrow {
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
		setPotionEffect(stack);
	}

	@Override
	public void setAim(Entity shooter, float pitch, float yaw, float unused, float velocity, float spread)
	{
		float mass = GenesisItems.ARROWS.getVariant(stack).getMass();
		setDamage(getDamage() * mass);
		super.setAim(shooter, pitch, yaw, unused, velocity / mass, spread / mass);
	}

	@Override
	protected ItemStack getArrowStack() {
		return stack;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_writeEntityToNBT_1_) {
		super.writeEntityToNBT(p_writeEntityToNBT_1_);
	}
}
