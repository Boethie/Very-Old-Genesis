package genesis.combo.variant;

import com.google.common.collect.ImmutableSet;

import genesis.combo.OreBlocks;
import genesis.common.GenesisItems;
import genesis.util.random.drops.blocks.*;
import genesis.util.random.drops.blocks.BlockWeightedRandomDrop.WeightedRandomBlockDrop;
import genesis.util.random.i.IntRange;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Random;

public enum EnumOre implements IOreVariant<EnumOre>
{
	QUARTZ("quartz", 1, 4.2F, 15, IntRange.create(0, 2), 0.2F),
	AMETHYST("amethyst"),
	ZIRCON("zircon", 1, 4.2F, 15, IntRange.create(0, 2), 0.2F),
	GARNET("garnet", 1, 4.2F, 15, IntRange.create(0, 2), 0.2F),
	HEMATITE("hematite", 1, 4.2F, 15, IntRange.create(0, 2), 0.7F),
	MANGANESE("manganese", 1, 4.2F, 15, IntRange.create(1, 5), 0.2F),
	MALACHITE("malachite", 1, 4.2F, 15, IntRange.create(2, 5), 0.2F),
	AZURITE("azurite", 1, 4.2F, 15, IntRange.create(2, 5), 0.2F),
	OLIVINE("olivine", 1, 4.2F, 15, IntRange.create(3, 7), 1),
	BLACK_DIAMOND("black_diamond", "blackDiamond", 1, 2, 15, IntRange.create(3, 7), 1),
	FLINT("flint", 1, 1.5F, 13.05F, IntRange.create(0, 1), 0, Type.ORE),
	MARCASITE("marcasite", 1, 1.5F, 13.05F, IntRange.create(0, 1), 0.1F, Type.ORE);

	public enum Type
	{
		BOTH, ORE, DROP
	}

	public static final ImmutableSet<EnumOre> NO_ORES = ImmutableSet.of(AMETHYST);
	public static final ImmutableSet<EnumOre> NO_DROPS = ImmutableSet.of(FLINT, MARCASITE);

	/**
	 * Called from the combo that owns these variants, because otherwise the reference loop (Combo -> Enum -> Combo...) will cause a runtime error.
	 */
	public static void setDrops(OreBlocks combo)
	{
		QUARTZ.setDrops(new BlockDrops(
				new BlockWeightedRandomDrop(
						new WeightedRandomBlockDrop(VariantDrop.create(combo, OreBlocks.DROP, 1), 49),
						new WeightedRandomBlockDrop(StaticVariantDrop.create(combo, OreBlocks.DROP, AMETHYST, 1), 1)
				)
		));
		FLINT.setDrops(new BlockDrops(
				new BlockMultiDrop(
					new BlockStackDrop(GenesisItems.NODULES.getStack(EnumNodule.BROWN_FLINT), 1),
					new BlockStackDrop(GenesisItems.NODULES.getStack(EnumNodule.BLACK_FLINT), 1)
				)
			));
		MARCASITE.setDrops(new BlockDrops(GenesisItems.NODULES.getStack(EnumNodule.MARCASITE), 1));

		BlockDrops drops = new BlockDrops(VariantDrop.create(combo, OreBlocks.DROP, 1));

		for (EnumOre ore : values())
		{
			if (NO_ORES.contains(ore))
				continue;

			if (ore.drops == null)
			{
				ore.setDrops(drops);
			}
		}
	}

	final String name;
	final String unlocalizedName;
	final int harvestLevel;
	final float hardness;
	final float resistance;
	final IntRange dropExperience;
	final float smeltExperience;
	final Type type;

	BlockDrops drops;

	EnumOre(String name, String unlocalizedName,
			int harvestLevel, float hardness, float resistance,
			IntRange dropExperience, float smeltExperience,
			Type type)
	{
		this.name = name;
		this.harvestLevel = harvestLevel;
		this.unlocalizedName = unlocalizedName;
		this.hardness = hardness;
		this.resistance = resistance;
		this.dropExperience = dropExperience;
		this.smeltExperience = smeltExperience;
		this.type = type;
	}

	EnumOre(String name,
			int harvestLevel, float hardness, float resistance,
			IntRange dropExperience, float smeltExperience,
			Type type)
	{
		this(name, name, harvestLevel, hardness, resistance, dropExperience, smeltExperience, type);
	}

	EnumOre(String name, String unlocalizedName)
	{
		this(name, name, 0, 0, 0, IntRange.create(0), 0, Type.DROP);
	}

	EnumOre(String name)
	{
		this(name, name);
	}

	EnumOre(String name, String unlocalizedName,
			int harvestLevel, float hardness, float resistance,
			IntRange dropExperience, float smeltExperience)
	{
		this(name, unlocalizedName, harvestLevel, hardness, resistance, dropExperience, smeltExperience, Type.BOTH);
	}

	EnumOre(String name,
			int harvestLevel, float hardness, float resistance,
			IntRange dropExperience, float smeltExperience)
	{
		this(name, name, harvestLevel, hardness, resistance, dropExperience, smeltExperience);
	}

	protected void setDrops(BlockDrops drops)
	{
		this.drops = drops;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}

	@Override
	public int getHarvestLevel()
	{
		return harvestLevel;
	}

	@Override
	public float getHardness()
	{
		return hardness;
	}

	@Override
	public float getExplosionResistance()
	{
		return resistance;
	}

	@Override
	public IntRange getDropExperience()
	{
		return dropExperience;
	}

	@Override
	public float getSmeltingExperience()
	{
		return smeltExperience;
	}

	public boolean hasOre()
	{
		return type != Type.DROP;
	}

	public boolean hasDrop()
	{
		return type != Type.ORE;
	}

	@Override
	public List<ItemStack> getDrops(IBlockState state, Random rand)
	{
		return drops.getDrops(state, rand);
	}
}
