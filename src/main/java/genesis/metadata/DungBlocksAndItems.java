package genesis.metadata;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.EnumHelper;
import genesis.block.*;
import genesis.client.GenesisSounds;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemBlockMulti;
import genesis.item.ItemMulti;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.FuelHandler;

public class DungBlocksAndItems extends VariantsOfTypesCombo<ObjectType, EnumDung>
{
	public static final ObjectType<BlockGenesisVariants, ItemBlockMulti> DUNG_BLOCK =
			new ObjectType<BlockGenesisVariants, ItemBlockMulti>("dung_block", "dung", BlockGenesisVariants.class, null)
			{
				@Override
				public void afterConstructed(BlockGenesisVariants block, ItemBlockMulti item, List<IMetadata> variants)
				{
					super.afterConstructed(block, item, variants);
					
					BlockGenesisVariants dung = (BlockGenesisVariants) block;
					
					dung.setHardness(0.5F);
					dung.setStepSound(GenesisSounds.DUNG);
					
					dung.clearDrops();
					dung.addDrop(GenesisBlocks.dungs.DUNG, 4, 4);
					
					Blocks.fire.setFireInfo(dung, 5, 5);
					
					FuelHandler.setBurnTime(item,
							TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.log)) * 4, true);
				}
			}.setUseSeparateVariantJsons(false).setNamePosition(ObjectNamePosition.PREFIX).setBlockArguments(Material.ground);
	public static final ObjectType<Block, ItemMulti> DUNG = new ObjectType<Block, ItemMulti>("dung", null, null)
			{
				@Override
				public void afterConstructed(Block block, ItemMulti item, List<IMetadata> variants)
				{
					super.afterConstructed(block, item, variants);
					
					FuelHandler.setBurnTime(item,
							TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.log)), true);
				}
			}
			.setCreativeTab(GenesisCreativeTabs.MATERIALS).setNamePosition(ObjectNamePosition.PREFIX);
	
	public DungBlocksAndItems()
	{
		super(new ObjectType[] {DUNG_BLOCK, DUNG}, EnumDung.values());
	}
}
