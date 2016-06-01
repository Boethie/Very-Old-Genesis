package genesis.combo;

import com.google.common.collect.ImmutableList;

import genesis.block.*;
import genesis.combo.variant.EnumSilt;
import genesis.item.*;
import genesis.util.Constants;
import genesis.util.ReflectionUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class SiltBlocks extends VariantsOfTypesCombo<EnumSilt>
{
	public static final ObjectType<EnumSilt, BlockSilt, ItemBlockMulti<EnumSilt>> SILT =
			ObjectType.createBlock(EnumSilt.class, "silt", BlockSilt.class);
	public static final ObjectType<EnumSilt, BlockGenesisVariants<EnumSilt>, ItemBlockMulti<EnumSilt>> CRACKED_SILT =
			new ObjectType<>(EnumSilt.class, "cracked_silt", "cracked.silt", ReflectionUtils.convertClass(BlockGenesisVariants.class), null);
	public static final ObjectType<EnumSilt, BlockSiltstone, ItemBlockMulti<EnumSilt>> SILTSTONE =
			ObjectType.createBlock(EnumSilt.class, "siltstone", "rock.siltstone", BlockSiltstone.class);
	
	static
	{
		SILT.setVariantNameFunction((v) -> v == EnumSilt.SILT ? SILT.getName() : SILT.getName() + "_" + v.getName())
				.setUseSeparateVariantJsons(false);
		CRACKED_SILT.setVariantNameFunction((v) -> v == EnumSilt.SILT ? CRACKED_SILT.getName() : CRACKED_SILT.getName() + "_" + v.getName())
				.setConstructedFunction((b, i) -> b.setHardness(0.6F))
				.setUseSeparateVariantJsons(false)
				.setBlockArguments(Material.sand, SoundType.SAND);
		SILTSTONE.setVariantNameFunction((v) -> v == EnumSilt.SILT ? SILTSTONE.getName() : SILTSTONE.getName() + "_" + v.getName())
				.setUseSeparateVariantJsons(false);
	}
	
	public SiltBlocks()
	{
		super("silt", ImmutableList.of(SILT, CRACKED_SILT, SILTSTONE),
				EnumSilt.class, ImmutableList.copyOf(EnumSilt.values()));
		
		setNames(Constants.MOD_ID, Constants.Unlocalized.PREFIX);
	}
}
