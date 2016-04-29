package genesis.block.tileentity;

import java.util.*;

import genesis.util.*;
import genesis.util.random.d.DoubleRange;
import genesis.client.*;
import genesis.common.*;
import genesis.sounds.GenesisSoundEvents;
import genesis.block.tileentity.render.TileEntityCampfireRenderer;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.*;

public class BlockCampfire extends Block
{
	public static final PropertyEnum<EnumAxis> FACING = PropertyEnum.create("facing", EnumAxis.class, EnumAxis.HORIZ);
	public static final PropertyBool FIRE = PropertyBool.create("fire");
	
	protected static final AxisAlignedBB BASE_BB = new AxisAlignedBB(0, 0, 0,
																	 1, 0.1875, 1);
	protected static final double STICK_SIZE = 0.125;
	protected static final double HALF_STICK_SIZE = STICK_SIZE / 2;
	protected static final AxisAlignedBB STICK_0 = new AxisAlignedBB(0.5 - HALF_STICK_SIZE, 0, 0,
																	 0.5 + HALF_STICK_SIZE, 1, STICK_SIZE);
	protected static final AxisAlignedBB STICK_0_R = AABBUtils.rotate(STICK_0, Rotation.CLOCKWISE_90);
	protected static final AxisAlignedBB STICK_1 = new AxisAlignedBB(0.5 - HALF_STICK_SIZE, 0, 1 - STICK_SIZE,
																	 0.5 + HALF_STICK_SIZE, 1, 1);
	protected static final AxisAlignedBB STICK_1_R = AABBUtils.rotate(STICK_1, Rotation.CLOCKWISE_90);
	
	public BlockCampfire()
	{
		super(Material.wood);
		
		setTickRandomly(true);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING, FIRE);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntityCampfire campfire = getTileEntity(world, pos);
		
		if (campfire != null)
		{
			//return state.withProperty(FIRE, campfire.isBurning());
			// TODO: Try to get this working.
		}
		
		return state;
	}
	
	public boolean canBlockStay(World world, BlockPos pos)
	{
		return world.isSideSolid(pos.down(), EnumFacing.UP);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return canBlockStay(world, pos);
	}
	
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if (!canBlockStay(world, pos))
		{
			dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
			world.setBlockToAir(pos);
		}
		else
		{
			TileEntityCampfire campfire = getTileEntity(world, pos);
			
			if (campfire != null)
			{
				boolean water = false;
				BlockPos[] aWaterPos = {pos.up(), pos.north(), pos.east(), pos.south(), pos.west()};
				
				for (BlockPos waterPos : aWaterPos)
				{
					if (world.getBlockState(waterPos).getMaterial() == Material.water)
					{
						water = true;
						break;
					}
				}
				
				campfire.setWaterAround(water);
			}
		}
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
	{
		TileEntityCampfire campfire = getTileEntity(world, pos);

		if (campfire != null && campfire.isBurning())
		{
			DoubleRange rangeXZ = DoubleRange.create(0.25, 0.75);
			DoubleRange rangeY = DoubleRange.create(0.0, 0.5);
			
			for (int i = 0; i < 4; i++)
			{
				double x = pos.getX() + rangeXZ.get(rand);
				double y = pos.getY() + rangeY.get(rand);
				double z = pos.getZ() + rangeXZ.get(rand);
				
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0, 0, 0);
			}
			
			ItemStack input = campfire.getInput().getStack();
			
			if (input != null && !TileEntityCampfireRenderer.hasCookingItemModel(input))
			{
				rangeXZ = DoubleRange.create(0.4, 0.6);
				rangeY = DoubleRange.create(0.9, 1);
				
				for (int i = 0; i < 2; i++)
				{
					double x = pos.getX() + rangeXZ.get(rand);
					double y = pos.getY() + rangeY.get(rand);
					double z = pos.getZ() + rangeXZ.get(rand);
					
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0, 0, 0);
				}
			}
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return BASE_BB;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos,
			AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
		// Make collision boxes for the two sticks and the base of the campfire.
		addCollisionBoxToList(pos, mask, list, getBoundingBox(state, world, pos));
		
		if (state.getValue(FACING) == EnumAxis.X)
		{
			addCollisionBoxToList(pos, mask, list, STICK_0);
			addCollisionBoxToList(pos, mask, list, STICK_1);
		}
		else
		{
			addCollisionBoxToList(pos, mask, list, STICK_0_R);
			addCollisionBoxToList(pos, mask, list, STICK_1_R);
		}
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
	{
		if (!world.isRemote &&
				!entity.isImmuneToFire() &&
				entity instanceof EntityPlayer ? !((EntityPlayer) entity).capabilities.disableDamage : true)
		{
			TileEntityCampfire campfire = getTileEntity(world, pos);
			
			// Set a player on fire if they step into the fire.
			if (campfire != null && campfire.isBurning())
			{
				double posX = entity.posX;
				double posY = entity.posY;
				double posZ = entity.posZ;
				
				double dist = 0.35;
				AxisAlignedBB fireBB = new AxisAlignedBB(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
														pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5);
				fireBB = fireBB.expand(dist, 0, dist);
				
				Vec3d vec = new Vec3d(posX, posY, posZ);
				
				if (fireBB.isVecInside(vec))
				{
					entity.setFire(8);
					entity.attackEntityFrom(DamageSource.inFire, 1);
				}
			}
		}
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntityCampfire campfire = getTileEntity(world, pos);
		
		if (campfire != null)
		{
			InventoryHelper.dropInventoryItems(world, pos, campfire);
		}
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
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	public void setBurning(World world, BlockPos pos, boolean burning)
	{
		WorldUtils.setProperty(world, pos, FIRE, burning);
	}
	
	@SuppressWarnings("serial")
	protected final Map<ItemStackKey, SoundEvent> lighterItems = new HashMap<ItemStackKey, SoundEvent>(){{
		put(new ItemStackKey(Items.flint_and_steel), SoundEvents.item_flintandsteel_use);
		put(new ItemStackKey(GenesisItems.flint_and_marcasite), GenesisSoundEvents.item_flintAndMarcasite_use);
	}};
	
	/**
	 * Registers a item to light this campfire.
	 * @param key The {@link ItemStackKey} to match the ItemStack to use to light the campfire.
	 * @param sound The sound to play when the fire has been lit.
	 */
	public void registerLighterItem(ItemStackKey key, SoundEvent sound)
	{
		lighterItems.put(key, sound);
	}
	
	/**
	 * Registers a item to light this campfire.
	 * @param stack The {@link ItemStack} to match when right clicking to light the campfire.
	 * @param sound The sound to play when the fire has been lit.
	 */
	public void registerLighterItem(ItemStack stack, SoundEvent sound)
	{
		registerLighterItem(new ItemStackKey(stack), sound);
	}
	
	/**
	 * Registers a item to light this campfire.
	 * @param item The {@link Item} to match when right clicking to light the campfire.
	 * @param sound The sound to play when the fire has been lit.
	 */
	public void registerLighterItem(Item item, SoundEvent sound)
	{
		registerLighterItem(new ItemStackKey(item), sound);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
			EntityPlayer player, EnumHand hand, ItemStack held,
			EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntityCampfire campfire = getTileEntity(world, pos);
		
		if (campfire != null && held != null)
		{
			ItemStackKey key = new ItemStackKey(held);
			
			if (lighterItems.containsKey(key))
			{
				if (campfire.burnFuelIfNotBurning())
				{
					world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
							lighterItems.get(key), SoundCategory.BLOCKS, 1, world.rand.nextFloat() * 0.4F + 0.8F);
					
					held.damageItem(1, player);
				}
				
				return true;
			}
			else if (FluidContainerRegistry.containsFluid(held, new FluidStack(FluidRegistry.getFluid("water"), 250)))
			{
				boolean burning = campfire.isBurning();
				Random rand = world.rand;
				
				DoubleRange rangeXZ = DoubleRange.create(0.25, 0.75);
				DoubleRange rangeY = DoubleRange.create(0.0, 0.25);
				DoubleRange speedXZ = DoubleRange.create(-0.08, 0.08);
				DoubleRange speedY = DoubleRange.create(0.1, 0.3);
				
				final int bigSmokeCount = 1;
				final int smokeCount = 25;
				final int waterCount = 50;
				final int maxCount = Math.max(bigSmokeCount, Math.max(smokeCount, waterCount));
				
				for (int i = 0; i < maxCount; i++)
				{
					double x = pos.getX() + rangeXZ.get(rand);
					double y = pos.getY() + rangeY.get(rand);
					double z = pos.getZ() + rangeXZ.get(rand);
					
					if (burning)
					{
						if (i < bigSmokeCount)
						{
							world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x, y, z, 0, 0, 0);
						}
						if (i < smokeCount)
						{
							world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0, 0, 0);
						}
					}
					if (i < waterCount)
					{
						world.spawnParticle(GenesisParticles.WATER_SPLASH, x, y, z,
								speedXZ.get(rand), speedY.get(rand), speedXZ.get(rand));
					}
				}
				
				campfire.setWet();
				
				return true;
			}
			
			player.openGui(Genesis.instance, GenesisGuiHandler.CAMPFIRE_ID, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public TileEntityCampfire createTileEntity(World world, IBlockState state)
	{
		return new TileEntityCampfire();
	}
	
	public TileEntityCampfire getTileEntity(IBlockAccess world, BlockPos pos)
	{
		TileEntity tileEnt = world.getTileEntity(pos);
		
		if (tileEnt instanceof TileEntityCampfire)
		{
			return (TileEntityCampfire) tileEnt;
		}
		
		return null;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack)
	{
		EnumAxis direction = EnumAxis.getForAngle(entity.rotationYaw);
		WorldUtils.setProperty(world, pos, FACING, direction);
		
		if (stack.hasDisplayName())
		{
			TileEntityCampfire te = getTileEntity(world, pos);
			
			if (te != null)
			{
				te.setCustomInventoryName(stack.getDisplayName());
			}
		}
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		int lightVal = super.getLightValue(state, world, pos);
		
		if (state.getValue(FIRE))
		{
			lightVal = Math.max(lightVal, 15);
		}
		
		return lightVal;
	}
}
