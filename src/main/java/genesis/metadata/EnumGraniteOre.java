package genesis.metadata;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import genesis.block.BlockGenesisOre;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisItems;
import genesis.util.Constants.Unlocalized;
import genesis.util.random.IntRange;
import genesis.util.random.drops.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum EnumGraniteOre implements IOreVariant
{
	QUARTZ("quartz", 4.2F, 5.0F, IntRange.create(0, 1), 0.05F),
	ZIRCON("zircon", 4.2F, 5.0F, IntRange.create(0, 2), 0.1F),
	GARNET("garnet", 4.2F, 5.0F, IntRange.create(0, 2), 0.1F),
	HEMATITE("hematite", 4.2F, 5.0F, IntRange.create(0, 1), 0.05F),
	MANGANESE("manganese", 4.2F, 5.0F, IntRange.create(0, 1), 0.05F),
	MALACHITE("malachite", 4.2F, 5.0F, IntRange.create(1, 2), 0.2F),
	OLIVINE("olivine", 4.2F, 5.0F, IntRange.create(1, 3), 0.3F);
	/**
	 * Called from the combo that owns these variants, because otherwise the reference loop (Combo -> Enum -> Combo...) will cause a runtime error.
	 */
	public static void setDrops(GraniteOreBlocks combo)
	{
		BlockDrops drops = new BlockDrops(VariantDrop.create(combo, GraniteOreBlocks.DROP, 1));
		
		for (EnumGraniteOre ore : values())
		{
			ore.setDrops(drops);
		}
	}
	
	final String name;
	final String unlocName;
	final float hardness;
	final float resistance;
	final IntRange dropExperience;
	final float smeltExperience;
	
	BlockDrops drops;
	
	EnumGraniteOre(String name, String unlocName, float hardness, float resistance, IntRange dropExperience, float smeltExperience)
	{
		this.name = name;
		this.unlocName = unlocName;
		this.hardness = hardness;
		this.resistance = resistance;
		this.dropExperience = dropExperience;
		this.smeltExperience = smeltExperience;
	}
	
	EnumGraniteOre(String name, float hardness, float resistance, IntRange dropExperience, float smeltExperience)
	{
		this(name, name, hardness, resistance, dropExperience, smeltExperience);
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
