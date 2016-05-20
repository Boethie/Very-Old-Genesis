package genesis.combo.variant;

import java.util.*;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.EnumPlantType;

import static genesis.combo.variant.EnumPlant.PlantType.*;
import static net.minecraftforge.common.EnumPlantType.*;

public enum EnumPlant implements IPlantMetadata<EnumPlant>
{
	// Plants
	COOKSONIA("cooksonia", plant().soil(Plains, Desert)),
	BARAGWANATHIA("baragwanathia", plant().soil(Plains, Desert).biomeColor(true)),
	SCIADOPHYTON("sciadophyton", plant().soil(Plains, Desert)),
	PSILOPHYTON("psilophyton", plant().soil(Plains, Desert)),
	NOTHIA("nothia", plant().soil(Plains, Desert)),
	RHYNIA("rhynia", plant().soil(Plains, Desert)),
	APOLDIA("apoldia", plant().soil(Plains, Desert).biomeColor(true)),
	LEPACYCLOTES("lepacyclotes", plant().soil(Plains, Desert).water(2)),
	SANMIGUELIA("sanmiguelia", plant().soil(Plains, Desert).water(2)),
	ARCHAEAMPHORA("archaeamphora", plant()),
	MABELIA("mabelia", plant()),
	PALAEOASTER("palaeoaster", plant()),
	ASTEROXYLON("asteroxylon", plant().soil(Plains, Desert).biomeColor(true).bothSizes()),
	AETHOPHYLLUM("aethophyllum", plant().soil(Plains, Desert).shearable(true).largeOnly()),
	
	// Ferns
	RHACOPHYTON("rhacophyton", fern().soil(Plains, Desert).largeOnly()),
	ZYGOPTERIS("zygopteris", fern()),
	WACHTLERIA("wachtleria", fern().soil(Plains, Desert)),
	PHLEBOPTERIS("phlebopteris", fern()),
	TODITES("todites", fern()),
	RUFFORDIA("ruffordia", fern()),
	ASTRALOPTERIS("astralopteris", fern()),
	MATONIDIUM("matonidium", fern()),
	DRYOPTERIS("dryopteris", fern());
	
	final String name;
	final String unlocalizedName;
	final PlantType type;
	final boolean small;
	final boolean large;
	final boolean biomeColor;
	final EnumPlantType[] soils;
	final boolean shearable;
	final boolean replaceable;
	final int waterDistance;
	
	EnumPlant(String name, String unlocalizedName, Props props)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.type = props.type;
		this.small = props.small;
		this.large = props.large;
		this.biomeColor = props.biomeColor;
		this.shearable = props.shearable;
		this.replaceable = props.replaceable;
		this.soils = props.soils;
		this.waterDistance = props.waterDistance;
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
	
	public boolean hasSmall()
	{
		return small;
	}
	
	public boolean hasLarge()
	{
		return large;
	}
	
	public boolean shouldUseBiomeColor()
	{
		return biomeColor;
	}
	
	@Override
	public int getColorMultiplier(IBlockAccess world, BlockPos pos)
	{
		if (!shouldUseBiomeColor())
			return 0xFFFFFF;
		
		if (world == null || pos == null)
			return ColorizerGrass.getGrassColor(0.5, 1);
		
		return BiomeColorHelper.getGrassColorAtPos(world, pos);
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
	public boolean isReplaceable(IBlockAccess world, BlockPos pos)
	{
		return replaceable;
	}
	
	@Override
	public EnumPlantType[] getSoilTypes()
	{
		return soils;
	}
	
	@Override
	public int getWaterDistance()
	{
		return waterDistance;
	}
	
	public enum PlantType
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
	
	private static final class Props
	{
		PlantType type;
		boolean small = true;
		boolean large = false;
		boolean biomeColor = false;
		boolean shearable = false;
		boolean replaceable = false;
		EnumPlantType[] soils = {EnumPlantType.Plains};
		int waterDistance = -1;
		
		private Props(PlantType type)
		{
			this.type = type;
		}
		
		private Props soil(EnumPlantType... soils)
		{
			this.soils = soils;
			return this;
		}
		
		private Props sizes(boolean small, boolean large)
		{
			this.small = small;
			this.large = large;
			return this;
		}
		
		private Props largeOnly()
		{
			return sizes(false, true);
		}
		
		private Props bothSizes()
		{
			return sizes(true, true);
		}
		
		private Props water(int distance)
		{
			this.waterDistance = distance;
			return this;
		}
		
		private Props biomeColor(boolean biomeColor)
		{
			this.biomeColor = biomeColor;
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
