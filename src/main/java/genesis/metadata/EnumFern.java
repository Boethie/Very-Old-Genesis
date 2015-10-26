package genesis.metadata;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public enum EnumFern implements IPlantMetadata
{
	RHACOPHYTON("rhacophyton"), ZYGOPTERIS("zygopteris"), PHLEBOPTERIS("phlebopteris"), RUFFORDIA("ruffordia"), ASTRALOPTERIS("astralopteris"),
	MATONIDIUM("matonidium");
	
	public static final Set<EnumFern> SINGLES;
	public static final Set<EnumFern> DOUBLES;
	
	static
	{
		Set<EnumFern> singlesSet = EnumSet.allOf(EnumFern.class);
		singlesSet.remove(RHACOPHYTON);
		SINGLES = Sets.immutableEnumSet(singlesSet);
		
		DOUBLES = Sets.immutableEnumSet(RHACOPHYTON);
	}
	
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
	
	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, List<ItemStack> normalDrop)
	{
		return normalDrop;
	}
	
	@Override
	public boolean isReplaceable(World world, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, Random rand, List<ItemStack> normalDrop)
	{
		return Collections.emptyList();
	}
}
