package genesis.item;

import genesis.common.GenesisCreativeTabs;
import genesis.common.GenesisItems;
import genesis.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemGenesisBucket extends Item
{
	private Block isFull;
	public ItemGenesisBucket(Block block)
	{
		this.maxStackSize = 1;
		if (block == Blocks.air)
		{
			this.maxStackSize = 16;
		}
		this.isFull = block;
		setCreativeTab(GenesisCreativeTabs.MISC);
	}

	public boolean tryPlaceContainedLiquid(World worldIn, BlockPos pos)
	{
		if (this.isFull == Blocks.air)
		{
			return false;
		}
		else
		{
			Material material = worldIn.getBlockState(pos).getBlock().getMaterial();
			boolean flag = !material.isSolid();

			if (!worldIn.isAirBlock(pos) && !flag)
			{
				return false;
			}
			else
			{
				if (worldIn.provider.doesWaterVaporize() && this.isFull == Blocks.flowing_water)
				{
					int i = pos.getX();
					int j = pos.getY();
					int k = pos.getZ();
					worldIn.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

					for (int l = 0; l < 8; ++l)
					{
						worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
					}
				}
				else
				{
					if (!worldIn.isRemote && flag && !material.isLiquid())
					{
						worldIn.destroyBlock(pos, true);
					}

					worldIn.setBlockState(pos, this.isFull.getDefaultState(), 3);
				}

				return true;
			}
		}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		boolean flag = this.isFull == Blocks.air;
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, flag);
		if (movingobjectposition == null)
		{
			return false;
		}
		else
		{

			if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
			{
				BlockPos blockpos = movingobjectposition.getBlockPos();

				if (!worldIn.isBlockModifiable(playerIn, blockpos))
				{
					return false;
				}

				if (flag)
				{
					if (!playerIn.canPlayerEdit(blockpos.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, stack))
					{
						return false;
					}

					IBlockState iblockstate = worldIn.getBlockState(blockpos);
					Material material = iblockstate.getBlock().getMaterial();

					if (material == Material.water && ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0)
					{
						worldIn.setBlockToAir(blockpos);
						playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
						if (playerIn.getHeldItem().stackSize <= 1 && !playerIn.capabilities.isCreativeMode)
						{
							playerIn.getHeldItem().setItem(GenesisItems.ceramic_bucket_water);
						}
						else if (!playerIn.capabilities.isCreativeMode)
						{
							--playerIn.getHeldItem().stackSize;
							playerIn.inventory.addItemStackToInventory(new ItemStack(GenesisItems.ceramic_bucket_water));
						}
						return true;
					}
				}
				else
				{
					if (this.isFull == Blocks.air)
					{
						return false;
					}
					IBlockState iblockstate = worldIn.getBlockState(blockpos);
					if (iblockstate.getBlock() instanceof BlockCauldron) {
						((BlockCauldron) iblockstate.getBlock()).setWaterLevel(worldIn, blockpos, iblockstate, 3);
						playerIn.getHeldItem().setItem(GenesisItems.ceramic_bucket);
						return true;
					}
					BlockPos blockpos1 = blockpos.offset(movingobjectposition.sideHit);

					if (!playerIn.canPlayerEdit(blockpos1, movingobjectposition.sideHit, stack))
					{
						return false;
					}

					if (this.tryPlaceContainedLiquid(worldIn, blockpos1) && !playerIn.capabilities.isCreativeMode)
					{
						playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
						playerIn.getHeldItem().setItem(GenesisItems.ceramic_bucket);
						return true;
					}
				}
			}

			return false;
		}
	}

	private ItemStack fillBucket(ItemStack emptyBuckets, EntityPlayer player, Item fullBucket)
	{
		if (player.capabilities.isCreativeMode)
		{
			return emptyBuckets;
		}
		else if (--emptyBuckets.stackSize <= 0)
		{
			return new ItemStack(fullBucket);
		}
		else
		{
			if (!player.inventory.addItemStackToInventory(new ItemStack(fullBucket)))
			{
				player.dropPlayerItemWithRandomChoice(new ItemStack(fullBucket, 1, 0), false);
			}

			return emptyBuckets;
		}
	}

	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target)
	{
		if (stack.getItem().equals(GenesisItems.ceramic_bucket) && target instanceof EntityCow)
		{
			if (stack.stackSize <= 1)
			{
				stack.setItem(GenesisItems.ceramic_bucket_milk);
			}
			else
			{
				--stack.stackSize;
				playerIn.inventory.addItemStackToInventory(new ItemStack(GenesisItems.ceramic_bucket_milk));
			}
		}
		
		return true;
	}

	@Override
	public ItemGenesisBucket setUnlocalizedName(String unlocalizedName)
	{
		super.setUnlocalizedName(Constants.PREFIX + "misc." + unlocalizedName);

		return this;
	}
	
	@Override
    public ItemGenesisBucket setContainerItem(Item containerItem)
    {
    	super.setContainerItem(containerItem);
    	
    	return this;
    }
}
