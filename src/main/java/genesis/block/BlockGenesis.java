package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.util.Constants;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class BlockGenesis extends Block
{
	private Item itemDropped = Item.getItemFromBlock(Blocks.air);
	private int quantityDropped = -1;

	public BlockGenesis(Material material)
	{
		super(material);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
	}

	@Override
	public Block setUnlocalizedName(String name)
	{
		return super.setUnlocalizedName(Constants.PREFIX + name);
	}

	@Override
    public int quantityDropped(Random random)
    {
        return getQuantityDropped() > -1 ? getQuantityDropped() : super.quantityDropped(random);
    }

	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return getItemDropped() != Item.getItemFromBlock(Blocks.air) ? getItemDropped() : super.getItemDropped(state, rand, fortune);
    }

	public Item getItemDropped()
	{
		return itemDropped;
	}

	public BlockGenesis setItemDropped(Item itemDropped)
	{
		this.itemDropped = itemDropped;
		return this;
	}

	public int getQuantityDropped()
	{
		return quantityDropped;
	}

	public BlockGenesis setQuantityDropped(int quantityDropped)
	{
		this.quantityDropped = quantityDropped;
		return this;
	}
}
