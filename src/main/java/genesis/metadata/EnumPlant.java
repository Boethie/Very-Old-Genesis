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
import net.minecraftforge.common.EnumPlantType;

import static genesis.metadata.EnumPlant.PlantType.*;

public enum EnumPlant implements IPlantMetadata<EnumPlant>
{
	// Plants
	COOKSONIA("cooksonia", plant()),
	BARAGWANATHIA("baragwanathia", plant()),
	SCIADOPHYTON("sciadophyton", plant()),
	PSILOPHYTON("psilophyton", plant()),
	HORNEOPHYTON("horneophyton", plant()),
	AGLAOPHYTON("aglaophyton", plant()),
	NOTHIA("nothia", plant()),
	RHYNIA("rhynia", plant()),
	ARCHAEAMPHORA("archaeamphora", plant()),
	MABELIA("mabelia", plant()),
	PALAEOASTER("palaeoaster", plant()),
	ASTEROXYLON("asteroxylon", plant().biomeColor(true)),
	
	// Ferns
	RHACOPHYTON("rhacophyton", fern()),
	ZYGOPTERIS("zygopteris", fern()),
	PHLEBOPTERIS("phlebopteris", fern()),
	RUFFORDIA("ruffordia", fern()),
	ASTRALOPTERIS("astralopteris", fern()),
	MATONIDIUM("matonidium", fern());

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
			switch (plant.getType())
			{
			case PLANT:
				plantsBuilder.add(plant);
				break;
			case FERN:
				fernsBuilder.add(plant);
				break;
			}
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
	final PlantType type;
	final boolean biomeColor;
	final EnumPlantType[] soils;
	final boolean shearable;
	final boolean replaceable;
	
	EnumPlant(String name, String unlocalizedName, Props props)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.type = props.type;
		this.biomeColor = props.biomeColor;
		this.shearable = props.shearable;
		this.replaceable = props.replaceable;
		this.soils = props.soils;
	}
	
	EnumPlant(String name, Props props)
	{
		this(name, name, props);
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
	
	public PlantType getType()
	{
		return type;
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
		return shearable;
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, List<ItemStack> normalDrop)
	{
		return shearable ? normalDrop : Collections.<ItemStack>emptyList();
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, Random rand, List<ItemStack> normalDrop)
	{
		return shearable ? Collections.<ItemStack>emptyList() : normalDrop;
	}
	
	@Override
	public boolean isReplaceable(World world, BlockPos pos)
	{
		return replaceable;
	}
	
	@Override
	public EnumPlantType[] getSoilTypes()
	{
		return soils;
	}
	
	public static enum PlantType
	{
		PLANT, FERN;
	}
	
	// Initialization helpers.
	private static Props plant()
	{
		return new Props(PLANT);
	}
	
	private static Props fern()
	{
		return new Props(FERN).biomeColor(true).shearable(true).replaceable(true);
	}
	
	@SuppressWarnings("unused")
	private static class Props
	{
		PlantType type;
		boolean biomeColor = false;
		boolean shearable = false;
		boolean replaceable = false;
		EnumPlantType[] soils = {EnumPlantType.Plains};
		
		private Props(PlantType type)
		{
			this.type = type;
		}
		
		private Props biomeColor(boolean biomeColor)
		{
			this.biomeColor = biomeColor;
			return this;
		}
		
		private Props soil(EnumPlantType... soils)
		{
			this.soils = soils;
			return this;
		}
		
		private Props shearable(boolean shearable)
		{
			this.shearable = shearable;
			return this;
		}
		
		private Props replaceable(boolean replaceable)
		{
			this.replaceable = replaceable;
			return this;
		}
	}
}
