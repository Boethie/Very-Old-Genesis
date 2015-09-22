package genesis.metadata;

import java.util.*;

import com.google.common.collect.Sets;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public enum EnumPlant implements IPlantMetadata
{
	COOKSONIA("cooksonia"), BARAGWANATHIA("baragwanathia"), SCIADOPHYTON("sciadophyton"), PSILOPHYTON("psilophyton"), NOTHIA("nothia"),
	RHYNIA("rhynia"), ARCHAEAMPHORA("archaeamphora"), MABELIA("mabelia"), PALAEOASTER("palaeoaster"), ASTEROXYLON("asteroxylon", true);
	
	public static final Set<EnumPlant> SINGLES;
	public static final Set<EnumPlant> DOUBLES;
	
	static
	{
		Set<EnumPlant> singlesSet = EnumSet.allOf(EnumPlant.class);
		SINGLES = Sets.immutableEnumSet(singlesSet);
		
		DOUBLES = Sets.immutableEnumSet(ASTEROXYLON);
	}
	
	final String name;
	final String unlocalizedName;
	final boolean useBiomeColor;
	
	EnumPlant(String name, String unlocalizedName, boolean useBiomeColor)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.useBiomeColor = useBiomeColor;
	}
	
	EnumPlant(String name, String unlocalizedName)
	{
		this(name, unlocalizedName, false);
	}
	
	EnumPlant(String name, boolean useBiomeColor)
	{
		this(name, name, useBiomeColor);
	}
	
	EnumPlant(String name)
	{
		this(name, name);
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
	
	public boolean shouldUseBiomeColor()
	{
		return useBiomeColor;
	}
	
	@Override
	public int getColorMultiplier(IBlockAccess world, BlockPos pos)
	{
		return shouldUseBiomeColor() ? BiomeColorHelper.getGrassColorAtPos(world, pos) : 16777215;
	}
	
	@Override
	public int getRenderColor()
	{
		return shouldUseBiomeColor() ? ColorizerGrass.getGrassColor(0.5, 1) : 16777215;
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
