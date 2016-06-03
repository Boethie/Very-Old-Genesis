package genesis.combo;

import com.google.common.collect.ImmutableList;
import genesis.block.BlockGenesisSlab;
import genesis.block.BlockGenesisSlab.EnumHalf;
import genesis.combo.variant.EnumSlab;
import genesis.item.ItemGenesisSlab;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized.Section;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class SlabBlocks extends VariantsOfTypesCombo<EnumSlab>
{
	public static final SlabObjectType ROCK =
			new SlabObjectType("rock_slab", Section.SLAB + Section.ROCK, Material.rock, SoundType.STONE);

	static
	{
		ROCK.setUseSeparateVariantJsons(false);
	}

	public SlabBlocks()
	{
		super("slabs", ImmutableList.of(ROCK),
				EnumSlab.class, ImmutableList.copyOf(EnumSlab.values()));

		setNames(Constants.MOD_ID, Constants.Unlocalized.PREFIX);
	}
	
	/**
	 * Gets a double slab block state from the specified {@link SlabObjectType}, with the variant specified.
	 */
	public IBlockState getDoubleSlabState(SlabObjectType type, EnumSlab variant)
	{
		return getBlockState(type, variant).withProperty(BlockGenesisSlab.HALF, EnumHalf.BOTH);
	}

	/**
	 * Gets a top slab block state from the specified {@link SlabObjectType}, with the variant specified.
	 */
	public IBlockState getTopSlabState(SlabObjectType type, EnumSlab variant)
	{
		return getBlockState(type, variant).withProperty(BlockGenesisSlab.HALF, EnumHalf.TOP);
	}

	/**
	 * Gets a bottom slab block state from the specified {@link SlabObjectType}, with the variant specified.
	 */
	public IBlockState getBottomSlabState(SlabObjectType type, EnumSlab variant)
	{
		return getBlockState(type, variant).withProperty(BlockGenesisSlab.HALF, EnumHalf.BOTTOM);
	}

	public static class SlabObjectType extends ObjectType<EnumSlab, BlockGenesisSlab, ItemGenesisSlab>
	{
		public SlabObjectType(String name, String unlocalizedName, Material material, SoundType sound)
		{
			super(EnumSlab.class, name, unlocalizedName, BlockGenesisSlab.class, ItemGenesisSlab.class);

			setBlockArguments(material, sound);
			setVariantFilter((v) -> material == v.getMaterial());
		}

		public SlabObjectType(String name, Material material, SoundType sound)
		{
			this(name, name, material, sound);
		}
	}
}
