package genesis.combo;

import com.google.common.collect.ImmutableList;
import genesis.block.BlockGenesisSlab;
import genesis.block.BlockGenesisSlab.EnumHalf;
import genesis.combo.variant.EnumSlabMaterial;
import genesis.combo.variant.SlabTypes;
import genesis.combo.variant.SlabTypes.SlabType;
import genesis.util.Constants.Unlocalized.Section;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSlab;

public class SlabBlocks extends VariantsOfTypesCombo<SlabType>
{
	public static final SlabObjectType ROCK =
			new SlabObjectType("rock_slab", Section.SLAB + Section.ROCK, Material.rock, SoundType.STONE);

	private static final ImmutableList<SlabObjectType> TYPES = ImmutableList.of(ROCK);

	static
	{
		ROCK.setUseSeparateVariantJsons(false);
	}

	public SlabBlocks()
	{
		super("slabs", TYPES, SlabType.class, SlabTypes.getAll());
	}

	public SlabObjectType getObjectType(SlabType variant)
	{
		for (SlabObjectType type : TYPES)
		{
			if (containsVariant(type, variant))
			{
				return type;
			}
		}

		throw new IllegalArgumentException("Unknown SlabType " + variant);
	}
	
	/**
	 * Gets a slab block state from the specified {@link SlabObjectType}, with the material and half specified.
	 */
	public IBlockState getSlabState(SlabObjectType type, EnumSlabMaterial material, EnumHalf half)
	{
		return getBlockState(type, SlabTypes.getSlabType(material, half));
	}
	
	/**
	 * Gets a double slab block state from the specified {@link SlabObjectType}, with the material specified.
	 */
	public IBlockState getDoubleSlabState(SlabObjectType type, EnumSlabMaterial material)
	{
		return getSlabState(type, material, EnumHalf.BOTH);
	}

	/**
	 * Gets a top slab block state from the specified {@link SlabObjectType}, with the material specified.
	 */
	public IBlockState getTopSlabState(SlabObjectType type, EnumSlabMaterial material)
	{
		return getSlabState(type, material, EnumHalf.TOP);
	}

	/**
	 * Gets a bottom slab block state from the specified {@link SlabObjectType}, with the material specified.
	 */
	public IBlockState getBottomSlabState(SlabObjectType type, EnumSlabMaterial material)
	{
		return getSlabState(type, material, EnumHalf.BOTTOM);
	}
	
	public static class SlabObjectType extends ObjectType<SlabType, BlockGenesisSlab, ItemSlab>
	{
		public final Material material;

		public SlabObjectType(String name, String unlocalizedName, Material material, SoundType sound)
		{
			super(SlabType.class, name, unlocalizedName, BlockGenesisSlab.class, null);

			this.material = material;

			setBlockArguments(material, sound);
			setVariantFilter((v) -> material == v.material.getBaseState().getMaterial());
		}
		
		public SlabObjectType(String name, Material material, SoundType sound)
		{
			this(name, name, material, sound);
		}
		
		@Override
		public SlabObjectType setTypeNamePosition(TypeNamePosition namePosition)
		{
			super.setTypeNamePosition(namePosition);
			return this;
		}
		
		@Override
		public SlabObjectType setCreativeTab(CreativeTabs tab)
		{
			super.setCreativeTab(tab);
			return this;
		}
		
		@Override
		public SlabObjectType setUseVariantAsRegistryName(boolean use)
		{
			super.setUseVariantAsRegistryName(use);
			return this;
		}
	}
}
