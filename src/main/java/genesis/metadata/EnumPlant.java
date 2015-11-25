package genesis.metadata;

import java.util.*;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public enum EnumPlant implements IPlantMetadata<EnumPlant>
{
	// Plants
	COOKSONIA("cooksonia", false, false),
	BARAGWANATHIA("baragwanathia", false, false),
	SCIADOPHYTON("sciadophyton", false, false),
	PSILOPHYTON("psilophyton", false, false),
	HORNEOPHYTON("horneophyton", false, false),
	AGLAOPHYTON("aglaophyton", false, false),
	NOTHIA("nothia", false, false),
	RHYNIA("rhynia", false, false),
	ARCHAEAMPHORA("archaeamphora", false, false),
	MABELIA("mabelia", false, false),
	PALAEOASTER("palaeoaster", false, false),
	ASTEROXYLON("asteroxylon", false, true),
	
	// Ferns
	RHACOPHYTON("rhacophyton", true, true),
	ZYGOPTERIS("zygopteris", true, true),
	PHLEBOPTERIS("phlebopteris", true, true),
	RUFFORDIA("ruffordia", true, true),
	ASTRALOPTERIS("astralopteris", true, true),
	MATONIDIUM("matonidium", true, true);

	public static final Set<EnumPlant> PLANTS;
	public static final Set<EnumPlant> FERNS;
	public static final Set<EnumPlant> SINGLES;
	public static final Set<EnumPlant> DOUBLES;
	
	static
	{
		ImmutableSet.Builder<EnumPlant> plantsBuilder = ImmutableSet.builder();
		ImmutableSet.Builder<EnumPlant> fernsBuilder = ImmutableSet.builder();
		
		for (EnumPlant plant : values())
		{
			(!plant.isFern ? plantsBuilder : fernsBuilder).add(plant);
		}
		
		PLANTS = plantsBuilder.build();
		FERNS = fernsBuilder.build();
		
		Set<EnumPlant> singlesSet = EnumSet.allOf(EnumPlant.class);
		singlesSet.remove(RHACOPHYTON);
		SINGLES = Sets.immutableEnumSet(singlesSet);
		
		DOUBLES = Sets.immutableEnumSet(ASTEROXYLON, RHACOPHYTON);
	}
	
	final String name;
	final String unlocalizedName;
	final boolean biomeColor;
	final boolean isFern;
	
	EnumPlant(String name, String unlocalizedName, boolean isFern, boolean biomeColor)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.biomeColor = biomeColor;
		this.isFern = isFern;
	}
	
	EnumPlant(String name, boolean isFern, boolean biomeColor)
	{
		this(name, name, isFern, biomeColor);
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
	
	public boolean isFern()
	{
		return isFern;
	}
	
	public boolean shouldUseBiomeColor()
	{
		return biomeColor;
	}
	
	@Override
	public int getColorMultiplier(IBlockAccess world, BlockPos pos)
	{
		return shouldUseBiomeColor() ? BiomeColorHelper.getGrassColorAtPos(world, pos) : 0xFFFFFF;
	}
	
	@Override
	public int getRenderColor()
	{
		return shouldUseBiomeColor() ? ColorizerGrass.getGrassColor(0.5, 1) : 0xFFFFFF;
	}
	
	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
	{
		return false;
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, List<ItemStack> normalDrop)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean isReplaceable(World world, BlockPos pos)
	{
		return false;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, Random rand, List<ItemStack> normalDrop)
	{
		return normalDrop;
	}
}
