package genesis.metadata;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableSet;

import genesis.common.GenesisItems;
import genesis.util.random.IntRange;
import genesis.util.random.drops.*;
import genesis.util.random.drops.blocks.BlockDrops;
import genesis.util.random.drops.blocks.BlockMultiDrop;
import genesis.util.random.drops.blocks.BlockStackDrop;
import genesis.util.random.drops.blocks.VariantDrop;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public enum EnumOre implements IOreVariant
{
	QUARTZ("quartz", 1, 4.2F, 5.0F, IntRange.create(0, 1), 0.05F),
	ZIRCON("zircon", 1, 4.2F, 5.0F, IntRange.create(0, 2), 0.1F),
	GARNET("garnet", 1, 4.2F, 5.0F, IntRange.create(0, 2), 0.1F),
	HEMATITE("hematite", 1, 4.2F, 5.0F, IntRange.create(0, 1), 0.05F),
	MANGANESE("manganese", 1, 4.2F, 5.0F, IntRange.create(0, 1), 0.05F),
	MALACHITE("malachite", 1, 4.2F, 5.0F, IntRange.create(1, 2), 0.2F),
	AZURITE("azurite", 1, 4.2F, 5.0F, IntRange.create(1, 2), 0.2F),
	OLIVINE("olivine", 1, 4.2F, 5.0F, IntRange.create(1, 3), 0.3F),
	FLINT("flint", 1, 1.5F, 4.35F, IntRange.create(0, 1), 0,
			new BlockDrops(
				new BlockMultiDrop(
					new BlockStackDrop(GenesisItems.nodules.getStack(EnumNodule.BROWN_FLINT), 1),
					new BlockStackDrop(GenesisItems.nodules.getStack(EnumNodule.BLACK_FLINT), 1)
				)
			)),
	MARCASITE("marcasite", 1, 1.5F, 4.35F, IntRange.create(0, 1), 0.05F,
			new BlockDrops(GenesisItems.nodules.getStack(EnumNodule.MARCASITE), 1));
	
	public static ImmutableSet<EnumOre> noDrops = ImmutableSet.of(FLINT, MARCASITE);
	
	/**
	 * Called from the combo that owns these variants, because otherwise the reference loop (Combo -> Enum -> Combo...) will cause a runtime error.
	 */
	public static void setDrops(OreBlocks combo)
	{
		BlockDrops drops = new BlockDrops(VariantDrop.create(combo, OreBlocks.DROP, 1));
		
		for (EnumOre ore : values())
		{
			if (ore.drops == null)
			{
				ore.setDrops(drops);
			}
		}
	}
	
	final String name;
	final String unlocName;
	final int harvestLevel;
	final float hardness;
	final float resistance;
	final IntRange dropExperience;
	final float smeltExperience;
	
	BlockDrops drops;
	
	EnumOre(String name, String unlocName, int harvestLevel, float hardness, float resistance, IntRange dropExperience, float smeltExperience)
	{
		this.name = name;
		this.harvestLevel = harvestLevel;
		this.unlocName = unlocName;
		this.hardness = hardness;
		this.resistance = resistance;
		this.dropExperience = dropExperience;
		this.smeltExperience = smeltExperience;
	}
	
	EnumOre(String name, String unlocName, int harvestLevel, float hardness, float resistance, IntRange dropExperience, float smeltExperience, BlockDrops drops)
	{
		this(name, unlocName, harvestLevel, hardness, resistance, dropExperience, smeltExperience);
		
		this.drops = drops;
	}
	
	EnumOre(String name, int harvestLevel, float hardness, float resistance, IntRange dropExperience, float smeltExperience)
	{
		this(name, name, harvestLevel, hardness, resistance, dropExperience, smeltExperience);
	}
	
	EnumOre(String name, int harvestLevel, float hardness, float resistance, IntRange dropExperience, float smeltExperience, BlockDrops drops)
	{
		this(name, name, harvestLevel, hardness, resistance, dropExperience, smeltExperience, drops);
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
		return unlocName;
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
	
	@Override
	public List<ItemStack> getDrops(IBlockState state, Random rand)
	{
		return drops.getDrops(state, rand);
	}
}
