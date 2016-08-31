package genesis.item;

import java.util.List;

import genesis.combo.*;
import genesis.combo.variant.IArrowMetadata;
import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemArrowMulti<V extends IArrowMetadata<V>> extends ItemArrow
{
	public final VariantsOfTypesCombo<V> owner;
	
	protected final List<V> variants;
	protected final ObjectType<V, Block, ? extends ItemArrowMulti<V>> type;
	
	public ItemArrowMulti(VariantsOfTypesCombo<V> owner,
			ObjectType<V, Block, ? extends ItemArrowMulti<V>> type,
			List<V> variants, Class<V> variantClass)
	{
		super();
		
		this.owner = owner;
		this.type = type;
		this.variants = variants;
		
		setHasSubtypes(true);
		
		setCreativeTab(GenesisCreativeTabs.COMBAT);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return owner.getUnlocalizedName(stack, super.getUnlocalizedName(stack));
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems)
	{
		owner.fillSubItems(type, variants, subItems);
	}
	
	@Override
	public EntityArrow createArrow(World world, ItemStack stack, EntityLivingBase shooter)
	{
		float mass = owner.getVariant(stack).getMass();
		ItemStack copy = stack.copy();
		EntityTippedArrow arrow = new EntityTippedArrow(world, shooter)
		{
			@Override
			public void setAim(Entity shooter, float pitch, float yaw, float unused, float velocity, float spread)
			{
				setDamage(getDamage() * mass);
				super.setAim(shooter, pitch, yaw, unused, velocity / mass, spread / mass);
			}
			
			@Override
			protected ItemStack getArrowStack()
			{
				return copy;
			}
		};
		arrow.setPotionEffect(stack);
		return arrow;
	}
}
