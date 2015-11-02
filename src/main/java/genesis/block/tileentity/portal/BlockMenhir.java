package genesis.block.tileentity.portal;

import java.util.*;

import genesis.common.GenesisItems;
import genesis.common.IRegistrationCallback;
import genesis.block.BlockGenesis;
import genesis.block.tileentity.portal.GenesisPortal.MenhirData;
import genesis.client.model.ListedItemMeshDefinition;
import genesis.common.Genesis;
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
import genesis.util.WorldUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMenhir extends BlockGenesis implements IRegistrationCallback
{
	public static enum EnumGlyph implements IMetadata
	{
		NONE("none"),
		VEGETAL("vegetal",
				GenesisItems.menhir_activators.getStack(EnumMenhirActivator.PETRIFIED_WOOD_PIECE),
				GenesisItems.menhir_activators.getStack(EnumMenhirActivator.ANCIENT_AMBER)),
		ANIMAL("animal",
				GenesisItems.menhir_activators.getStack(EnumMenhirActivator.FOSSILIZED_EGG)),
		HOMINID("hominid",
				GenesisItems.menhir_activators.getStack(EnumMenhirActivator.BROKEN_CEREMONIAL_AXE),
				GenesisItems.menhir_activators.getStack(EnumMenhirActivator.BROKEN_SPIRIT_MASK)),
		COSMOS("cosmos",
				GenesisItems.menhir_activators.getStack(EnumMenhirActivator.RUSTED_OCTAEDRITE_FLAKE));
		
		public static final EnumGlyph[] VALID = {VEGETAL, ANIMAL, HOMINID, COSMOS};
		
		final String name;
		final String unlocalizedName;
		final ItemStack defaultActivator;
		final Set<ItemStackKey> activators = new HashSet<ItemStackKey>();
		
		EnumGlyph(String name, String unlocalizedName, Object... activators)
		{
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			
			ItemStack defaultActivator = null;
			
			for (Object activator : activators)
			{
				ItemStackKey key = null;
				
				if (activator instanceof ItemStack)
				{
					key = addActivator((ItemStack) activator);
				}
				else if (activator instanceof ItemStackKey)
				{
					key = addActivator((ItemStackKey) activator);
				}
				else
				{
					throw new IllegalArgumentException("Invalid activator item " + activator + " for glyph with name \"" + name + "\".");
				}
				
				if (defaultActivator == null)
				{
					defaultActivator = key.createNewStack();
				}
			}
			
			this.defaultActivator = defaultActivator;
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
		
		public ItemStackKey addActivator(ItemStackKey stack)
		{
			activators.add(stack);
			return stack;
		}
		
		public ItemStackKey addActivator(ItemStack stack)
		{
			return addActivator(new ItemStackKey(stack));
		}
		
		public ItemStack getDefaultActivator()
		{
			return defaultActivator;
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
	
	@Override
	public void onRegistered()
	{
		Genesis.proxy.registerModel(Item.getItemFromBlock(this),
				new ListedItemMeshDefinition()
				{
					public String getName(EnumMenhirPart part, EnumGlyph glyph)
					{
						return "portal/" + part.getName() + (glyph != null ? "_" + glyph.getName() : "");
					}
					
					@Override
					public ModelResourceLocation getModelLocation(ItemStack stack)
					{
						EnumMenhirPart part = owner.getVariant(stack);
						EnumGlyph glyph = null;
						
						if (part == EnumMenhirPart.GLYPH)
						{
							glyph = EnumGlyph.NONE;
							TileEntityMenhirGlyph glyphTE = new TileEntityMenhirGlyph();
							
							NBTTagCompound compound = stack.getTagCompound();
							
							if (compound != null && compound.hasKey("BlockEntityTag", 10))
							{
								glyphTE.readFromNBT(compound.getCompoundTag("BlockEntityTag"));
								glyph = glyphTE.getGlyph();
							}
						}
						
						return (ModelResourceLocation) Genesis.proxy.getItemModelLocation(getName(part, glyph));
					}
					
					@Override
					public Collection<String> getVariants()
					{
						ArrayList<String> variants = new ArrayList<String>();
						
						for (EnumMenhirPart part : EnumMenhirPart.values())
						{
							if (part == EnumMenhirPart.GLYPH)
							{
								for (EnumGlyph glyph : EnumGlyph.values())
								{
									variants.add(getName(part, glyph));
								}
							}
							else
							{
								variants.add(getName(part, null));
							}
						}
						
						return variants;
					}
				});
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
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
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
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
	{
		if (owner.getVariant(world.getBlockState(pos)) == EnumMenhirPart.GLYPH)
		{
			TileEntityMenhirGlyph glyphTE = getGlyphTileEntity(world, pos);
			
			if (glyphTE != null)
			{
				return getGlyphStack(glyphTE.getGlyph());
			}
		}
		
		return super.getPickBlock(target, world, pos, player);
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
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		EnumMenhirPart part = (EnumMenhirPart) world.getBlockState(pos).getValue(variantProp);
		
		switch (part)
		{
		case RECEPTACLE:
			MenhirData menhir = new MenhirData(world, pos);
			TileEntityMenhirReceptacle recepTE = getReceptacleTileEntity(world, pos);
			
			if (recepTE != null && !recepTE.isReceptacleActive() &&
				menhir.getGlyph() != EnumGlyph.NONE)
			{
				ItemStack heldStack = player.getHeldItem();
				
				if (heldStack != null && menhir.getGlyph().isActivator(heldStack))
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
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		super.breakBlock(world, pos, state);
		
		switch (((EnumMenhirPart) state.getValue(variantProp)))
		{
		case GLYPH:
			TileEntityMenhirReceptacle recepTE = new MenhirData(world, pos, state).getReceptacleTE();
			
			if (recepTE != null)
			{
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
	
	public static EnumFacing getFacing(IBlockState state)
	{
		if (state.getProperties().containsKey(FACING))
		{
			return (EnumFacing) state.getValue(FACING);
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
