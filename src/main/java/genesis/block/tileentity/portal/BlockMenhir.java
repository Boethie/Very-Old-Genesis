package genesis.block.tileentity.portal;

import java.util.*;

import genesis.common.GenesisItems;
import genesis.block.BlockGenesis;
import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemBlockMulti;
import genesis.metadata.EnumMenhirActivator;
import genesis.metadata.EnumMenhirPart;
import genesis.metadata.IMetadata;
import genesis.metadata.PropertyIMetadata;
import genesis.metadata.VariantsCombo;
import genesis.metadata.VariantsOfTypesCombo.BlockProperties;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.BlockStateToMetadata;
import genesis.util.ItemStackKey;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMenhir extends BlockGenesis
{
	public static enum EnumGlyph implements IMetadata
	{
		NONE("none"),
		VEGETAL("vegetal",
				GenesisItems.menhir_activators.getStack(EnumMenhirActivator.ANCIENT_AMBER)),
		ANIMAL("animal",
				GenesisItems.menhir_activators.getStack(EnumMenhirActivator.FOSSILIZED_EGG)),
		HOMINID("hominid",
				GenesisItems.menhir_activators.getStack(EnumMenhirActivator.BROKEN_CEREMONIAL_AXE)),
		COSMOS("cosmos",
				GenesisItems.menhir_activators.getStack(EnumMenhirActivator.RUSTED_OCTAEDRITE_FLAKE));
		
		public static final EnumGlyph[] VALID = {VEGETAL, ANIMAL, HOMINID, COSMOS};
		
		final String name;
		final String unlocalizedName;
		final Set<ItemStackKey> activators = new HashSet<ItemStackKey>();
		
		EnumGlyph(String name, String unlocalizedName, Object... activators)
		{
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			
			for (Object activator : activators)
			{
				if (activator instanceof ItemStack)
				{
					addActivator((ItemStack) activator);
				}
				else if (activator instanceof ItemStackKey)
				{
					addActivator((ItemStackKey) activator);
				}
				else
				{
					throw new IllegalArgumentException("Invalid activator item " + activator + " for glyph with name \"" + name + "\".");
				}
			}
		}
		
		EnumGlyph(String name, Object... activators)
		{
			this(name, name, activators);
		}
		
		@Override
		public String getName()
		{
			return name;
		}
		
		@Override
		public String getUnlocalizedName()
		{
			return unlocalizedName;
		}
		
		public Set<ItemStackKey> getActivators()
		{
			return Collections.unmodifiableSet(activators);
		}
		
		public boolean isActivator(ItemStack stack)
		{
			return activators.contains(new ItemStackKey(stack));
		}
		
		public void addActivator(ItemStackKey stack)
		{
			activators.add(stack);
		}
		
		public void addActivator(ItemStack stack)
		{
			addActivator(new ItemStackKey(stack));
		}
	}
	
	/**
	 * Used in {@link #VariantsOfTypesCombo}.
	 */
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{FACING};
	}

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyEnum GLYPH = PropertyEnum.create("glyph", EnumGlyph.class);
	public static final PropertyBool ACTIVE = PropertyBool.create("active");
	
	public VariantsCombo<EnumMenhirPart, BlockMenhir, ItemBlockMulti> owner;
	public ObjectType<BlockMenhir, ItemBlockMulti> type;
	
	public List<EnumMenhirPart> variants;
	public PropertyIMetadata<EnumMenhirPart> variantProp;
	
	public BlockMenhir(List<EnumMenhirPart> variants, VariantsCombo<EnumMenhirPart, BlockMenhir, ItemBlockMulti> owner, ObjectType<BlockMenhir, ItemBlockMulti> type)
	{
		super(Material.rock);
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		variantProp = new PropertyIMetadata<EnumMenhirPart>("variant", variants);
		
		blockState = new BlockState(this, variantProp, FACING, GLYPH, ACTIVE);
		setDefaultState(getBlockState().getBaseState().withProperty(GLYPH, EnumGlyph.NONE).withProperty(ACTIVE, false));
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
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
	
	public BlockPos getTopOfMenhir(IBlockAccess world, BlockPos pos)
	{
		BlockPos curPos = pos;
		EnumMenhirPart top = EnumMenhirPart.values()[0];
		boolean foundTop = false;
		
		while (true)
		{
			EnumMenhirPart part = owner.getVariant(world.getBlockState(curPos));
			
			if (part == top)
			{
				foundTop = true;
			}
			else if (foundTop ? true : part == null)
			{
				curPos = curPos.down();
				break;
			}
			
			curPos = curPos.up();
		}
		
		return curPos;
	}
	
	public TileEntityMenhirGlyph getGlyphInMenhir(IBlockAccess world, BlockPos pos)
	{
		pos = getTopOfMenhir(world, pos);
		
		while (true)
		{
			IBlockState state = world.getBlockState(pos);
			EnumMenhirPart part = owner.getVariant(state);
			
			if (part == null)
			{
				break;
			}
			else if (part == EnumMenhirPart.GLYPH)
			{
				TileEntityMenhirGlyph glyph = getGlyphTileEntity(world, pos);
				
				if (glyph != null)
				{
					return glyph;
				}
			}
			
			pos = pos.down();
		}
		
		return null;
	}
	
	public TileEntityMenhirReceptacle getReceptacleInMenhir(IBlockAccess world, BlockPos pos)
	{
		pos = getTopOfMenhir(world, pos);
		
		while (true)
		{
			IBlockState state = world.getBlockState(pos);
			EnumMenhirPart part = owner.getVariant(state);
			
			if (part == null)
			{
				break;
			}
			else if (part == EnumMenhirPart.RECEPTACLE)
			{
				TileEntityMenhirReceptacle recep = getReceptacleTileEntity(world, pos);
				
				if (recep != null)
				{
					return recep;
				}
			}
			
			pos = pos.down();
		}
		
		return null;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = getStateFromMeta(getMetaFromState(state));	// Fix stupid bug where default doesn't stay for GLYPH

		TileEntityMenhirGlyph glyphTE = getGlyphInMenhir(world, pos);
		
		if (glyphTE != null)
		{
			state = state.withProperty(GLYPH, glyphTE.getGlyph());
		}

		TileEntityMenhirReceptacle recepTE = getReceptacleInMenhir(world, pos);
		
		if (recepTE != null)
		{
			state = state.withProperty(ACTIVE, recepTE.isReceptacleActive());
		}
		
		return state;
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		owner.fillSubItems(type, variants, list);
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
		switch (((EnumMenhirPart) state.getValue(variantProp)))
		{
		case GLYPH:
			TileEntityMenhirGlyph glyphTE = getGlyphTileEntity(world, pos);
			
			if (glyphTE != null)
			{
				glyphTE.setGlyph(EnumGlyph.VEGETAL);
			}
			break;
		case RECEPTACLE:
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		EnumMenhirPart part = (EnumMenhirPart) world.getBlockState(pos).getValue(variantProp);
		
		switch (part)
		{
		case RECEPTACLE:
			TileEntityMenhirReceptacle recepTE = getReceptacleTileEntity(world, pos);
			TileEntityMenhirGlyph glyphTE = getGlyphInMenhir(world, pos);
			
			if (recepTE != null && !recepTE.isReceptacleActive() &&
				glyphTE != null && glyphTE.getGlyph() != EnumGlyph.NONE)
			{
				ItemStack heldStack = player.getHeldItem();
				
				if (heldStack != null && glyphTE.getGlyph().isActivator(heldStack))
				{
					ItemStack addStack;
					
					if (player.capabilities.isCreativeMode)
					{
						addStack = heldStack.copy();
						addStack.stackSize = Math.min(addStack.stackSize, 1);
					}
					else
					{
						addStack = heldStack.splitStack(1);
					}
					
					if (!world.isRemote)
					{
						recepTE.setContainedItem(addStack);
					}
					
					return true;
				}
			}
		default:
			return false;
		}
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getItemMetadata(type, (EnumMenhirPart) state.getValue(variantProp));
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		switch (((EnumMenhirPart) state.getValue(variantProp)))
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
		switch (((EnumMenhirPart) state.getValue(variantProp)))
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
	public boolean isFullCube()
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
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
