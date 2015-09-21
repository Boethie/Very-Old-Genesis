package genesis.metadata;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import genesis.block.*;
import genesis.common.*;
import genesis.item.*;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.Constants.Unlocalized;
import genesis.util.FuelHandler;

@SuppressWarnings("rawtypes")
public class DungBlocksAndItems extends VariantsOfTypesCombo<ObjectType, EnumDung>
{
	public static final ObjectType<BlockGenesisVariants, ItemBlockMulti> DUNG_BLOCK =
			new ObjectType<BlockGenesisVariants, ItemBlockMulti>("dung_block", "dung", BlockGenesisVariants.class, null)
			{
				@Override
				public void afterConstructed(BlockGenesisVariants block, ItemBlockMulti item, List<? extends IMetadata> variants)
				{
					super.afterConstructed(block, item, variants);
					
					block.setHardness(0.5F);
					block.setStepSound(GenesisSounds.DUNG);
					
					block.clearDrops();
					block.addDrop(DUNG, 4, 4);
					
					Blocks.fire.setFireInfo(block, 5, 5);
					
					FuelHandler.setBurnTime(item,
							TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.log)) * 4, true);
				}
			}.setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.PREFIX).setBlockArguments(Material.ground);
	public static final ObjectType<Block, ItemDung> DUNG = new ObjectType<Block, ItemDung>("dung", Unlocalized.Section.MATERIAL + "dung", null, ItemDung.class)
			{
				@Override
				public void afterConstructed(Block block, ItemDung item, List<? extends IMetadata> variants)
				{
					super.afterConstructed(block, item, variants);
					FuelHandler.setBurnTime(item, TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.log)), true);
				}
			}
			.setCreativeTab(GenesisCreativeTabs.MATERIALS).setNamePosition(ObjectNamePosition.PREFIX);
	
	public DungBlocksAndItems()
	{
		super(new ObjectType[] {DUNG_BLOCK, DUNG}, EnumDung.values());
		
		setUnlocalizedPrefix(Unlocalized.PREFIX);
	}
}
