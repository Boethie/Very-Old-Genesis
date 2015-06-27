package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.util.FuelHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class BlockPeat extends BlockGenesis
{
	public BlockPeat()
	{
		super(Material.ground);
		setStepSound(Block.soundTypeGravel);
		setHarvestLevel("shovel", 0);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		setHardness(1.0F);
		setUnlocalizedName("peat");
		Blocks.fire.setFireInfo(this, 5, 5);
		FuelHandler.setBurnTime(this, TileEntityFurnace.getItemBurnTime(new ItemStack(Items.coal)) / 4, false);
	}
}
