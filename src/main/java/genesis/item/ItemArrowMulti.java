package genesis.item;

import genesis.combo.ObjectType;
import genesis.combo.VariantsOfTypesCombo;
import genesis.combo.variant.IArrowMetadata;
import genesis.common.GenesisCreativeTabs;
import genesis.entity.EntityGenesisArrow;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

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
		return new EntityGenesisArrow(world, shooter, stack.copy());
	}
}
