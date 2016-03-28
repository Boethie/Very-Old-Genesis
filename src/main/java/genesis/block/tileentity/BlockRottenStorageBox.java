package genesis.block.tileentity;

import java.util.Random;

import genesis.common.Genesis;
import genesis.common.GenesisCreativeTabs;
import genesis.util.WorldUtils;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import static genesis.block.tileentity.BlockStorageBox.*;

public class BlockRottenStorageBox extends Block
{
	public BlockRottenStorageBox()
	{
		super(Material.wood);
		
		setStepSound(SoundType.WOOD);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		
		setHardness(0.5F);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		/*placeWithItems(world, pos, genesis.common.GenesisItems.menhir_activators.getStack(genesis.combo.variant.EnumMenhirActivator.FOSSILIZED_EGG));
		if (getTileEntity(world, pos) != null)
			System.out.println(genesis.util.Stringify.stringify(getTileEntity(world, pos).inventory));*/
	}
	
	public void placeWithItems(World world, BlockPos pos, ItemStack... stacks)
	{
		world.setBlockState(pos, getDefaultState());
		
		if (world.isRemote)
			return;
		
		TileEntityRottenStorageBox box = getTileEntity(world, pos);
		
		if (box == null)
		{
			Genesis.logger.warn("Failed to create a rotten storage box tile entity.");
			return;
		}
		
		if (stacks.length > box.inventory.length)
			Genesis.logger.warn(new IllegalArgumentException("Too many stacks to fit in a rotten storage box."));
		
		TIntList slots = new TIntArrayList(box.inventory.length);
		
		for (int i = 0; i < box.inventory.length; i++)
			if (box.inventory[i] == null)
				slots.add(i);
		
		for (ItemStack stack : stacks)
		{
			int slot = slots.get(world.rand.nextInt(slots.size()));
			box.inventory[slot] = stack;
			slots.remove(slot);
		}
	}
	
	@Override
	public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune)
	{
		if (!world.isRemote && world.provider.getDimension() == 0 && world.getGameRules().getBoolean("doTileDrops"))
		{
			EntitySilverfish silverfish = new EntitySilverfish(world);
			silverfish.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
			world.spawnEntityInWorld(silverfish);
			silverfish.spawnExplosionParticle();
		}
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntityRottenStorageBox box = getTileEntity(world, pos);
		
		if (box != null)
			for (ItemStack stack : box.inventory)
				WorldUtils.spawnItemsAt(world, pos, WorldUtils.DropType.CONTAINER, stack);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
			EntityPlayer player, EnumHand hand, ItemStack held,
			EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!player.capabilities.isCreativeMode)
			dropBlockAsItem(world, pos, state, 0);
		
		world.playAuxSFXAtEntity(player, 2001, pos, getStateId(state));
		world.setBlockToAir(pos);
		
		return true;
	}
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		Random rand = new Random(MathHelper.getPositionRandom(pos));
		return state.withProperty(FACING, EnumFacing.HORIZONTALS[rand.nextInt(EnumFacing.HORIZONTALS.length)]);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public TileEntityRottenStorageBox createTileEntity(World world, IBlockState state)
	{
		return new TileEntityRottenStorageBox();
	}
	
	public TileEntityRottenStorageBox getTileEntity(IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		
		if (te instanceof TileEntityRottenStorageBox)
			return (TileEntityRottenStorageBox) te;
		
		return null;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
}
