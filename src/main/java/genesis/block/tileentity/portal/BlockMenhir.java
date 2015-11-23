package genesis.block.tileentity.portal;

import java.util.*;

import com.google.common.collect.ImmutableList;

import genesis.common.IRegistrationCallback;
import genesis.block.BlockGenesis;
import genesis.client.GenesisClient;
import genesis.client.model.ListedItemMeshDefinition;
import genesis.common.*;
import genesis.item.ItemBlockMulti;
import genesis.metadata.*;
import genesis.metadata.VariantsOfTypesCombo.*;
import genesis.portal.GenesisPortal;
import genesis.portal.MenhirData;
import genesis.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockMenhir extends BlockGenesis implements IRegistrationCallback
{
	/**
	 * Used in {@link VariantsOfTypesCombo}.
	 */
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{FACING};
	}

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyEnum GLYPH = PropertyEnum.create("glyph", EnumGlyph.class);
	public static final PropertyBool ACTIVE = PropertyBool.create("active");
	
	public VariantsCombo<EnumMenhirPart, BlockMenhir, ItemBlockMulti<EnumMenhirPart>> owner;
	public ObjectType<BlockMenhir, ItemBlockMulti<EnumMenhirPart>> type;
	
	public List<EnumMenhirPart> variants;
	public PropertyIMetadata<EnumMenhirPart> variantProp;
	
	public BlockMenhir(List<EnumMenhirPart> variants, VariantsCombo<EnumMenhirPart, BlockMenhir, ItemBlockMulti<EnumMenhirPart>> owner, ObjectType<BlockMenhir, ItemBlockMulti<EnumMenhirPart>> type)
	{
		super(Material.rock);
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		variantProp = new PropertyIMetadata<EnumMenhirPart>("variant", variants);
		
		blockState = new BlockState(this, variantProp, FACING, GLYPH, ACTIVE);
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
	public void onRegistered()
	{
		Genesis.proxy.callSided(new SidedFunction()
		{
			@SideOnly(Side.CLIENT)
			@Override
			public void client(GenesisClient client)
			{
				client.registerModel(Item.getItemFromBlock(BlockMenhir.this),
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
									
									NBTTagCompound compound = stack.getTagCompound();
									
									if (compound != null && compound.hasKey("BlockEntityTag", 10))
									{
										TileEntityMenhirGlyph glyphTE = new TileEntityMenhirGlyph();
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
								
								for (EnumMenhirPart part : EnumMenhirPart.ORDERED)
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
	
	public ItemStack getStack(IBlockState state, TileEntity te)
	{
		if (owner.getVariant(state) == EnumMenhirPart.GLYPH && te instanceof TileEntityMenhirGlyph)
		{
			return getGlyphStack(((TileEntityMenhirGlyph) te).getGlyph());
		}
		
		return owner.getStack((EnumMenhirPart) state.getValue(variantProp));
	}
	
	public ItemStack getStack(IBlockAccess world, BlockPos pos)
	{
		return getStack(world.getBlockState(pos), world.getTileEntity(pos));
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
	{
		return getStack(world, pos);
	}
	
	protected TileEntity brokenTE = null;
	
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		brokenTE = world.getTileEntity(pos);
		boolean removed = super.removedByPlayer(world, pos, player, willHarvest);
		
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
		
		switch (((EnumMenhirPart) state.getValue(variantProp)))
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
	public int getLightValue(IBlockAccess world, BlockPos pos)
	{
		MenhirData menhir = new MenhirData(world, pos);
		
		if (pos.equals(menhir.getGlyphPos()) && menhir.isReceptacleActive())
		{
			return 8;
		}
		
		return super.getLightValue(world, pos);
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
