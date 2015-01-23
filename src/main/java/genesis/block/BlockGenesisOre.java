package genesis.block;

import genesis.common.GenesisCreativeTabs;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGenesisOre extends BlockOre
{
	private final int minExp;
	private final int maxExp;
	private ItemStack drop; // drop.stackSize is minQuantity
	private int maxQuantity = 0;

	public BlockGenesisOre(float hardness, float resistance, int maxExp, int harvestLevel)
	{
		this(hardness, resistance, 0, maxExp, harvestLevel);
	}

	public BlockGenesisOre(float hardness, float resistance, int minExp, int maxExp, int harvestLevel)
	{
		this.minExp = minExp;
		this.maxExp = maxExp;
		setHardness(hardness);
		setResistance(resistance);
		setHarvestLevel("pickaxe", harvestLevel);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
	}

	@Override
	public int getExpDrop(IBlockAccess world, BlockPos pos, int fortune)
	{
		return MathHelper.getRandomIntegerInRange(world instanceof World ? ((World) world).rand : RANDOM, getMinExp(), getMaxExp());
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return getDrop() != null ? getDrop().getItem() : null;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return MathHelper.getRandomIntegerInRange(random, getMinQuantity(), getMaxQuantity());
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return getDrop() != null ? getDrop().getMetadata() : 0;
	}

	public int getMinExp()
	{
		return minExp;
	}

	public int getMaxExp()
	{
		return maxExp;
	}

	public ItemStack getDrop()
	{
		return drop;
	}

	public BlockGenesisOre setDrop(Block blockDrop)
	{
		return setDrop(new ItemStack(blockDrop));
	}

	public BlockGenesisOre setDrop(Item itemDrop)
	{
		return setDrop(new ItemStack(itemDrop));
	}

	public BlockGenesisOre setDrop(ItemStack drop)
	{
		this.drop = drop;
		return this;
	}

	public int getMinQuantity()
	{
		return getDrop() != null ? getDrop().stackSize : 0;
	}

	public BlockGenesisOre setMinQuantity(int minQuantity)
	{
		if (getDrop() != null)
		{
			getDrop().stackSize = minQuantity;
		}

		return this;
	}

	public int getMaxQuantity()
	{
		return maxQuantity;
	}

	public BlockGenesisOre setMaxQuantity(int maxQuantity)
	{
		this.maxQuantity = maxQuantity;
		return this;
	}

	public BlockGenesisOre setQuantity(int minQuantity, int maxQuantity)
	{
		setMinQuantity(minQuantity);
		setMaxQuantity(maxQuantity);
		return this;
	}
}
