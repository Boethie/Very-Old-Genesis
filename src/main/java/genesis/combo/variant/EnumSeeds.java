package genesis.combo.variant;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;

import genesis.common.GenesisBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.*;

public enum EnumSeeds implements IMetadata<EnumSeeds>, IFood
{
	ARAUCARIOXYLON_SEEDS("araucarioxylon_seeds", "araucarioxylon", 1, 0.2F, () -> null),
	ODONTOPTERIS_SEEDS("odontopteris_seeds", "odontopteris", 1, 0.4F,
			() -> GenesisBlocks.odontopteris.getDefaultState()),
	PROGRAMINIS_SEEDS("programinis_seeds", "programinis",
			() -> GenesisBlocks.programinis.getDefaultState()),
	ZINGIBEROPSIS_RHIZOME("zingiberopsis_rhizome", "zingiberopsisRhizome", 2, 1.4F,
			() -> GenesisBlocks.zingiberopsis.getDefaultState()),
	PROTOTAXITES_FLESH("prototaxites_flesh", "prototaxitesFlesh", 1, 0.8F,
			() -> GenesisBlocks.prototaxites.getDefaultState(),
			new PotionEffect(MobEffects.hunger, 300),
			new PotionEffect(MobEffects.confusion, 300),
			new PotionEffect(MobEffects.poison, 300));
	
	final String name;
	final String unlocalizedName;
	final int food;
	final float saturation;
	final Supplier<IBlockState> placed;
	final List<PotionEffect> effects;
	
	EnumSeeds(String name, String unlocalizedName, int food, float saturation, Supplier<IBlockState> placed, PotionEffect... effects)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.food = food;
		this.saturation = saturation;
		this.placed = placed;
		this.effects = ImmutableList.copyOf(effects);
	}
	
	EnumSeeds(String name, String unlocalizedName, Supplier<IBlockState> placed, PotionEffect... effects)
	{
		this(name, unlocalizedName, 0, 0, placed, effects);
	}
	
	EnumSeeds(String name, int food, float saturation, Supplier<IBlockState> placed, PotionEffect... effects)
	{
		this(name, name, food, saturation, placed, effects);
	}
	
	EnumSeeds(String name, Supplier<IBlockState> placed, PotionEffect... effects)
	{
		this(name, name, placed, effects);
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
	public int getFoodAmount()
	{
		return food;
	}
	
	@Override
	public float getSaturationModifier()
	{
		return saturation;
	}
	
	@Override
	public Iterable<PotionEffect> getEffects()
	{
		return effects;
	}
	
	public IBlockState getPlacedState()
	{
		return placed.get();
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
