package genesis.combo;

import com.google.common.collect.ImmutableList;
import genesis.block.BlockGenesisSlab;
import genesis.combo.variant.EnumSlab;
import genesis.item.ItemGenesisSlab;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized.Section;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class SlabBlocks extends VariantsOfTypesCombo<EnumSlab>
{
	public static final SlabObjectType ROCK =
			new SlabObjectType("rock_slab", Section.SLAB + Section.ROCK, Material.ROCK, SoundType.STONE);

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
