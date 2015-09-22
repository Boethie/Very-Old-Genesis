package genesis.metadata;

import java.util.*;

import net.minecraft.util.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;

public enum EnumPlant implements IPlantMetadata
{
	COOKSONIA("cooksonia"), BARAGWANATHIA("baragwanathia"), SCIADOPHYTON("sciadophyton"), PSILOPHYTON("psilophyton"), NOTHIA("nothia"),
	RHYNIA("rhynia"), ARCHAEAMPHORA("archaeamphora"), MABELIA("mabelia"), PALAEOASTER("palaeoaster"), ASTEROXYLON("asteroxylon", true);
	
	public static final Set<EnumPlant> NO_SINGLES = Collections.emptySet();
	public static final Set<EnumPlant> DOUBLES = EnumSet.of(ASTEROXYLON);
	
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
}
