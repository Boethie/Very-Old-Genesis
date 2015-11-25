package genesis.block.tileentity;

import java.util.*;

import genesis.util.*;
import genesis.util.Constants.Sounds;
import genesis.util.random.DoubleRange;
import genesis.client.*;
import genesis.common.*;
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
import net.minecraft.world.*;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.*;

public class BlockCampfire extends Block
{
	public static final PropertyEnum<EnumAxis> FACING = PropertyEnum.create("facing", EnumAxis.class, EnumAxis.HORIZ);
	public static final PropertyBool FIRE = PropertyBool.create("fire");
	
	public BlockCampfire()
	{
		super(Material.wood);
		
		setTickRandomly(true);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, FACING, FIRE);
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
		return World.doesBlockHaveSolidTopSurface(world, pos.down());
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
					if (world.getBlockState(waterPos).getBlock().getMaterial() == Material.water)
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
	public int getRenderType()
	{
		return 3;
	}
	
	@Override
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand)
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
			
			ItemStack input = campfire.getInput();
			
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
	
	protected void addIfIntersects(AxisAlignedBB aabb, AxisAlignedBB mask, List<AxisAlignedBB> list)
	{
		if (aabb != null && aabb.intersectsWith(mask))
		{
			list.add(aabb);
		}
	}
	
	protected AxisAlignedBB reverseBB(BlockPos pos, AxisAlignedBB bb)
	{
		return new AxisAlignedBB(bb.minZ - pos.getZ() + pos.getX(), bb.minY, -(bb.minX - pos.getX() - 0.5) + pos.getZ() + 0.5,
								bb.maxZ - pos.getZ() + pos.getX(), bb.maxY, -(bb.maxX - pos.getX() - 0.5) + pos.getZ() + 0.5);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state)
	{
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		return new AxisAlignedBB(x, y, z, x + 1, y + 0.1875, z + 1);
	}
	
	@Override
	public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
		// Make collision boxes for the two sticks and the base of the campfire.
		EnumAxis facing = state.getValue(FACING);
		
		addIfIntersects(getCollisionBoundingBox(world, pos, state), mask, list);
		
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		double stickSize = 0.125;
		double halfStickSize = stickSize / 2;
		
		AxisAlignedBB stick0 = new AxisAlignedBB(x + 0.5 - halfStickSize, y, z,
					x + 0.5 + halfStickSize, y + 1, z + stickSize);
		AxisAlignedBB stick1 = new AxisAlignedBB(x + 0.5 - halfStickSize, y, z + 1 - stickSize,
					x + 0.5 + halfStickSize, y + 1, z + 1);
		
		if (facing == EnumAxis.Z)
		{
			stick0 = reverseBB(pos, stick0);
			stick1 = reverseBB(pos, stick1);
		}
		
		addIfIntersects(stick0, mask, list);
		addIfIntersects(stick1, mask, list);
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
				
				Vec3 vec = new Vec3(posX, posY, posZ);
				
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
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean isFullCube()
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}
	
	public void setBurning(World world, BlockPos pos, boolean burning)
	{
		WorldUtils.setProperty(world, pos, FIRE, burning);
	}
	
	@SuppressWarnings("serial")
	protected final Map<ItemStackKey, String> lighterItems = new HashMap<ItemStackKey, String>(){{
		put(new ItemStackKey(Items.flint_and_steel), "fire.ignite");
		put(new ItemStackKey(GenesisItems.flint_and_marcasite), Sounds.IGNITE_FIRE);
	}};
	
	/**
	 * Registers a item to light this campfire.
	 * @param key The {@link ItemStackKey} to match the ItemStack to use to light the campfire.
	 * @param sound The sound to play when the fire has been lit.
	 */
	public void registerLighterItem(ItemStackKey key, String sound)
	{
		lighterItems.put(key, sound);
	}
	
	/**
	 * Registers a item to light this campfire.
	 * @param stack The {@link ItemStack} to match when right clicking to light the campfire.
	 * @param sound The sound to play when the fire has been lit.
	 */
	public void registerLighterItem(ItemStack stack, String sound)
	{
		registerLighterItem(new ItemStackKey(stack), sound);
	}
	
	/**
	 * Registers a item to light this campfire.
	 * @param item The {@link Item} to match when right clicking to light the campfire.
	 * @param sound The sound to play when the fire has been lit.
	 */
	public void registerLighterItem(Item item, String sound)
	{
		registerLighterItem(new ItemStackKey(item), sound);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntityCampfire campfire = getTileEntity(world, pos);
		
		if (campfire != null)
		{
			ItemStack heldStack = player.getHeldItem();
			
			if (heldStack != null)
			{
				ItemStackKey key = new ItemStackKey(heldStack);
				
				if (lighterItems.containsKey(key))
				{
					if (campfire.burnFuelIfNotBurning())
					{
						world.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, lighterItems.get(key), 1, world.rand.nextFloat() * 0.4F + 0.8F);
						
						heldStack.damageItem(1, player);
					}
					
					return true;
				}
				else if (FluidContainerRegistry.containsFluid(heldStack, new FluidStack(FluidRegistry.getFluid("water"), 250)))
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
	public int getLightValue(IBlockAccess world, BlockPos pos)
	{
		int lightVal = super.getLightValue(world, pos);
		
		IBlockState state = world.getBlockState(pos);
		
		if (state.getBlock() == this && state.getValue(FIRE))
		{
			lightVal = Math.max(lightVal, 15);
		}
		
		return lightVal;
	}
}
