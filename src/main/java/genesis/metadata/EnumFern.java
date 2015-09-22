package genesis.metadata;

import net.minecraft.util.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;

public enum EnumFern implements IPlantMetadata
{
	ZYGOPTERIS("zygopteris"), PHLEBOPTERIS("phlebopteris"), RUFFORDIA("ruffordia"), ASTRALOPTERIS("astralopteris"), MATONIDIUM("matonidium");
	
	final String name;
	final String unlocalizedName;
	
	EnumFern(String name)
	{
		this(name, name);
	}
	
	EnumFern(String name, String unlocalizedName)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
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
	public int getColorMultiplier(IBlockAccess world, BlockPos pos)
	{
		return BiomeColorHelper.getGrassColorAtPos(world, pos);
	}
	
	@Override
	public int getRenderColor()
	{
		return ColorizerGrass.getGrassColor(0.5, 1);
	}
}
