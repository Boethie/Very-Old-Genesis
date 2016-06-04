package genesis.block.tileentity.portal;

import java.util.*;

import com.google.common.collect.ImmutableList;

import genesis.block.BlockGenesis;
import genesis.combo.*;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.*;
import genesis.common.*;
import genesis.item.ItemBlockMulti;
import genesis.portal.*;
import genesis.util.*;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class BlockMenhir extends BlockGenesis
{
	/**
	 * Used in {@link VariantsOfTypesCombo}.
	 */
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return new IProperty[]{FACING};
	}

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyEnum<EnumGlyph> GLYPH = PropertyEnum.create("glyph", EnumGlyph.class);
	public static final PropertyBool ACTIVE = PropertyBool.create("active");
	
	public VariantsCombo<EnumMenhirPart, BlockMenhir, ItemBlockMulti<EnumMenhirPart>> owner;
	public ObjectType<EnumMenhirPart, BlockMenhir, ItemBlockMulti<EnumMenhirPart>> type;
	
	public List<EnumMenhirPart> variants;
	public PropertyIMetadata<EnumMenhirPart> variantProp;
	
	public BlockMenhir(VariantsCombo<EnumMenhirPart, BlockMenhir, ItemBlockMulti<EnumMenhirPart>> owner,
			ObjectType<EnumMenhirPart, BlockMenhir, ItemBlockMulti<EnumMenhirPart>> type,
			List<EnumMenhirPart> variants, Class<EnumMenhirPart> variantClass)
	{
		super(Material.rock, SoundType.STONE);
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		variantProp = new PropertyIMetadata<EnumMenhirPart>("variant", variants, variantClass);
		
		blockState = new BlockStateContainer(this, variantProp, FACING, GLYPH, ACTIVE);
		setDefaultState(getBlockState().getBaseState().withProperty(GLYPH, EnumGlyph.NONE).withProperty(ACTIVE, false));
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		
		setHardness(2.1F);
		setResistance(2000);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, variantProp, FACING);
	}
	
	@Override
	public IBlockState getStateFromMeta(int metadata)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), metadata, variantProp, FACING);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = getStateFromMeta(getMetaFromState(state));	// Fix stupid bug where default doesn't stay for GLYPH
		
		MenhirData menhir = new MenhirData(world, pos);
		state = state.withProperty(GLYPH, menhir.getGlyph());
		state = state.withProperty(ACTIVE, menhir.isReceptacleActive());
		
		return state;
	}
	
	public ItemStack getGlyphStack(EnumGlyph glyph)
	{
		ItemStack glyphStack = owner.getStack(EnumMenhirPart.GLYPH);
		
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagCompound blockCompound = new NBTTagCompound();
		
		// Create a TE to store in the block tag.
		TileEntityMenhirGlyph glyphTE = new TileEntityMenhirGlyph();
		glyphTE.setGlyph(glyph);
		glyphTE.writeToNBT(blockCompound);
		WorldUtils.removeBoilerplateTileEntityNBT(blockCompound);
		
		compound.setTag("BlockEntityTag", blockCompound);
		glyphStack.setTagCompound(compound);
		
		return glyphStack;
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		for (EnumMenhirPart part : variants)
		{
			ItemStack stack = owner.getStack(part);
			
			if (part == EnumMenhirPart.GLYPH)
			{
				for (EnumGlyph glyph : EnumGlyph.values())
				{	// Create a stack containing tile entity data to set the glyph.
					list.add(getGlyphStack(glyph));
				}
			}
			else
			{
				list.add(stack);
			}
		}
	}
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = getDefaultState();
		state = state.withProperty(variantProp, owner.getVariant(this, meta));
		state = state.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
		return state;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		
		TileEntity te = world.getTileEntity(pos);
		
		if (te instanceof TileEntityMenhirGlyph)
			((TileEntityMenhirGlyph) te).markDirty();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
			EntityPlayer player, EnumHand hand, ItemStack held,
			EnumFacing side, float hitX, float hitY, float hitZ)
	{
		EnumMenhirPart part = world.getBlockState(pos).getValue(variantProp);
		
		switch (part)
		{
		case RECEPTACLE:
			MenhirData menhir = new MenhirData(world, pos);
			TileEntityMenhirReceptacle recepTE = getReceptacleTileEntity(world, pos);
			
			if (recepTE != null && !recepTE.isReceptacleActive()
				&& menhir.getGlyph() != EnumGlyph.NONE
				&& held != null && menhir.getGlyph().isActivator(held))
			{
				ItemStack addStack;
				
				if (player.capabilities.isCreativeMode)
				{
					addStack = held.copy();
					addStack.stackSize = Math.min(addStack.stackSize, 1);
				}
				else
				{
					addStack = held.splitStack(1);
				}
				
				if (!world.isRemote)
				{
					recepTE.setContainedItem(addStack);
				}
				
				return true;
			}
		default:
			return false;
		}
	}
	
	public ItemStack getStack(IBlockState state, TileEntity te)
	{
		if (owner.getVariant(state) == EnumMenhirPart.GLYPH && te instanceof TileEntityMenhirGlyph)
		{
			return getGlyphStack(((TileEntityMenhirGlyph) te).getGlyph());
		}
		
		return owner.getStack(state.getValue(variantProp));
	}
	
	public ItemStack getStack(IBlockAccess world, BlockPos pos)
	{
		return getStack(world.getBlockState(pos), world.getTileEntity(pos));
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return getStack(world, pos);
	}
	
	protected TileEntity brokenTE = null;
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		brokenTE = world.getTileEntity(pos);
		boolean removed = super.removedByPlayer(state, world, pos, player, willHarvest);
		
		if (!willHarvest || !removed)
		{
			brokenTE = null;
		}
		
		return removed;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		TileEntity te = brokenTE == null ? world.getTileEntity(pos) : brokenTE;
		ItemStack blockStack = getStack(state, te);
		
		if (owner.getVariant(state) == EnumMenhirPart.RECEPTACLE && te instanceof TileEntityMenhirReceptacle)
		{
			return ImmutableList.of(blockStack, ((TileEntityMenhirReceptacle) te).getReceptacleItem());
		}
		
		return Collections.singletonList(blockStack);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		super.breakBlock(world, pos, state);
		
		switch (state.getValue(variantProp))
		{
		case GLYPH:
			TileEntityMenhirReceptacle recepTE = new MenhirData(world, pos, state).getReceptacleTE();
			
			if (recepTE != null)
			{
				if (recepTE.getReceptacleItem() != null)
				{
					spawnAsEntity(world, recepTE.getPos(), recepTE.getReceptacleItem());
				}
				
				recepTE.setContainedItem(null);
			}
			break;
		case RECEPTACLE:
			break;
		default:
			break;
		}
		
		GenesisPortal.fromMenhirBlock(world, pos, state).updatePortalStatus(world);
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getItemMetadata(type, state.getValue(variantProp));
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		switch (state.getValue(variantProp))
		{
		case GLYPH:
		case RECEPTACLE:
			return true;
		default:
			return false;
		}
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		switch (state.getValue(variantProp))
		{
		case GLYPH:
			return new TileEntityMenhirGlyph();
		case RECEPTACLE:
			return new TileEntityMenhirReceptacle();
		default:
			return null;
		}
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		MenhirData menhir = new MenhirData(world, pos);
		
		if (pos.equals(menhir.getGlyphPos()) && menhir.isReceptacleActive())
		{
			return 8;
		}
		
		return super.getLightValue(state, world, pos);
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	public static EnumFacing getFacing(IBlockState state)
	{
		if (state.getPropertyNames().contains(FACING))
		{
			return state.getValue(FACING);
		}
		
		return null;
	}
	
	public static TileEntityMenhirGlyph getGlyphTileEntity(IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		
		if (te instanceof TileEntityMenhirGlyph)
		{
			return (TileEntityMenhirGlyph) te;
		}
		
		return null;
	}
	
	public static TileEntityMenhirReceptacle getReceptacleTileEntity(IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		
		if (te instanceof TileEntityMenhirReceptacle)
		{
			return (TileEntityMenhirReceptacle) te;
		}
		
		return null;
	}
}
