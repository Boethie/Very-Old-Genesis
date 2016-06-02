package genesis.item;

import genesis.block.BlockGenesisSlab.EnumHalf;
import genesis.combo.ObjectType;
import genesis.combo.SlabBlocks.SlabObjectType;
import genesis.combo.VariantsCombo;
import genesis.combo.variant.EnumSlabMaterial;
import genesis.combo.variant.SlabTypes;
import genesis.combo.variant.SlabTypes.SlabType;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGenesisSlab extends Item
{
	public final VariantsCombo<EnumSlabMaterial, Block, ItemGenesisSlab> owner;
	public final ObjectType<EnumSlabMaterial, Block, ItemGenesisSlab> type;

	public final List<EnumSlabMaterial> variants;

	public ItemGenesisSlab(VariantsCombo<EnumSlabMaterial, Block, ItemGenesisSlab> owner,
			ObjectType<EnumSlabMaterial, Block, ItemGenesisSlab> type,
			List<EnumSlabMaterial> variants, Class<EnumSlabMaterial> variantClass)
	{
		this.owner = owner;
		this.type = type;

		this.variants = variants;

		setHasSubtypes(true);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
	}

	@Override
	public int getMetadata(int damage)
	{
		EnumSlabMaterial material = owner.getVariant(this, damage);
		SlabType variant = getSlabVariant(material);
		SlabObjectType objectType = GenesisBlocks.slabs.getObjectType(variant);
		IBlockState state = GenesisBlocks.slabs.getBlockState(objectType, variant);
		return state.getBlock().getMetaFromState(state);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		EnumSlabMaterial material = owner.getVariant(stack);
		SlabType variant = getSlabVariant(material);
		return getSlabBlock(variant).getUnlocalizedName() + variant.getUnlocalizedName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		owner.fillSubItems(type, variants, list);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (stack.stackSize == 0 || !player.canPlayerEdit(pos.offset(facing), facing, stack))
		{
			return EnumActionResult.FAIL;
		}
		
		EnumSlabMaterial itemMaterial = owner.getVariant(stack);
		IBlockState worldState = world.getBlockState(pos);
		SlabType worldVariant = GenesisBlocks.slabs.getVariant(worldState);
		
		if (worldVariant != null && worldVariant.material == itemMaterial && canFillEmptyHalf(worldVariant.half, facing))
		{
			IBlockState doubleSlabState = getDoubleSlabState(worldVariant);
			AxisAlignedBB collisionBB = doubleSlabState.getCollisionBoundingBox(world, pos);
			
			if (collisionBB != Block.NULL_AABB && world.checkNoEntityCollision(collisionBB.offset(pos)) && world.setBlockState(pos, doubleSlabState, 11))
			{
				SoundType sound = doubleSlabState.getBlock().getSoundType();
				world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
				--stack.stackSize;
			}
			
			return EnumActionResult.SUCCESS;
		}
		
		if (tryPlace(player, stack, world, pos.offset(facing), itemMaterial))
		{
			return EnumActionResult.SUCCESS;
		}
		
		return onItemUse(stack, itemMaterial, player, worldState, world, pos, facing, hitX, hitY, hitZ);
	}

	private EnumActionResult onItemUse(ItemStack stack, EnumSlabMaterial material, EntityPlayer player, IBlockState state, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!state.getBlock().isReplaceable(world, pos))
		{
			pos = pos.offset(facing);
		}

		SlabType slabVariant = getSlabVariant(material);
		SlabObjectType slabObjectType = GenesisBlocks.slabs.getObjectType(slabVariant);
		IBlockState slabState = GenesisBlocks.slabs.getBlockState(slabObjectType, slabVariant);
		Block slabBlock = slabState.getBlock();

		if (!world.canBlockBePlaced(slabBlock, pos, false, facing, null, stack))
		{
			return EnumActionResult.FAIL;
		}

		int slabMeta = slabBlock.getMetaFromState(slabState);
		slabState = slabBlock.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, slabMeta, player);

		if (placeBlockAt(stack, player, world, pos, facing, hitX, hitY, hitZ, slabState))
		{
			SoundType sound = slabBlock.getSoundType();
			world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
			--stack.stackSize;
		}

		return EnumActionResult.SUCCESS;
	}

	private boolean tryPlace(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumSlabMaterial material)
	{
		IBlockState state = world.getBlockState(pos);
		SlabType variant = GenesisBlocks.slabs.getVariant(state);

		if (variant != null && variant.material == material && variant.half.isSingle())
		{
			IBlockState doubleSlab = getDoubleSlabState(variant);
			AxisAlignedBB collisionBB = doubleSlab.getCollisionBoundingBox(world, pos);

			if (collisionBB != Block.NULL_AABB && world.checkNoEntityCollision(collisionBB.offset(pos)) && world.setBlockState(pos, doubleSlab, 11))
			{
				SoundType sound = doubleSlab.getBlock().getSoundType();
				world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
				--stack.stackSize;
			}

			return true;
		}

		return false;
	}

	private boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		if (!world.setBlockState(pos, newState, 3)) return false;

		IBlockState state = world.getBlockState(pos);
		EnumSlabMaterial material = owner.getVariant(stack);
		SlabType variant = getSlabVariant(material);
		Block slabBlock = getSlabBlock(variant);

		if (state.getBlock() == slabBlock)
		{
			slabBlock.onBlockPlacedBy(world, pos, state, player, stack);
		}

		return true;
	}

	private SlabType getSlabVariant(EnumSlabMaterial material)
	{
		return SlabTypes.getSlabType(material, EnumHalf.BOTTOM);
	}

	private Block getSlabBlock(SlabType variant)
	{
		SlabObjectType objectType = GenesisBlocks.slabs.getObjectType(variant);
		return GenesisBlocks.slabs.getBlock(objectType, variant);
	}

	private boolean canFillEmptyHalf(EnumHalf blockHalf, EnumFacing facing)
	{
		return facing == EnumFacing.UP && blockHalf.isBottom() || facing == EnumFacing.DOWN && blockHalf.isTop();
	}

	private IBlockState getDoubleSlabState(SlabType variant)
	{
		return GenesisBlocks.slabs.getDoubleSlabState(GenesisBlocks.slabs.getObjectType(variant), variant.material);
	}
}
