package genesis.block.tileentity;

import java.util.*;

import genesis.util.*;
import genesis.client.*;
import genesis.common.*;
import genesis.block.tileentity.*;
import genesis.block.tileentity.render.TileEntityCampfireRenderer;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockCampfire extends BlockContainer
{
	public static final PropertyEnum FACING = PropertyEnum.create("facing", EnumAxis.class, EnumAxis.HORIZ);
	public static final PropertyBool FIRE = PropertyBool.create("fire");
	
	public BlockCampfire()
	{
		super(Material.rock);
		
		setDefaultState(getBlockState().getBaseState());
		
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
	
	@Override
	public BlockCampfire setUnlocalizedName(String unlocName)
	{
		super.setUnlocalizedName(Constants.PREFIX + unlocName);
		
		return this;
	}
	
	public boolean canBlockStay(World worldIn, BlockPos pos)
	{
		return World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return canBlockStay(worldIn, pos);
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if (!canBlockStay(worldIn, pos))
		{
			dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
			worldIn.setBlockToAir(pos);
		}
		else
		{
			TileEntityCampfire campfire = getTileEntity(worldIn, pos);
			
			if (campfire != null)
			{
				boolean water = false;
				BlockPos[] aWaterPos = {pos.up(), pos.north(), pos.east(), pos.south(), pos.west()};
				
				for (BlockPos waterPos : aWaterPos)
				{
					if (worldIn.getBlockState(waterPos).getBlock().getMaterial() == Material.water)
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
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		TileEntityCampfire campfire = getTileEntity(worldIn, pos);

		if (campfire != null && campfire.isBurning())
		{
			RandomDoubleRange rangeXZ = new RandomDoubleRange(0.25, 0.75);
			RandomDoubleRange rangeY = new RandomDoubleRange(0.0, 0.5);
			
			for (int i = 0; i < 4; i++)
			{
				double x = pos.getX() + rangeXZ.getRandom(rand);
				double y = pos.getY() + rangeY.getRandom(rand);
				double z = pos.getZ() + rangeXZ.getRandom(rand);
				
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0, 0, 0);
			}
			
			ItemStack input = campfire.getInput();
			
			if (input != null && !TileEntityCampfireRenderer.hasCookingItemModel(input))
			{
				rangeXZ = new RandomDoubleRange(0.4, 0.6);
				rangeY = new RandomDoubleRange(0.9, 1);
				
				for (int i = 0; i < 2; i++)
				{
					double x = pos.getX() + rangeXZ.getRandom(rand);
					double y = pos.getY() + rangeY.getRandom(rand);
					double z = pos.getZ() + rangeXZ.getRandom(rand);
					
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0, 0, 0);
				}
			}
		}
	}
	
	protected void addIfIntersects(AxisAlignedBB aabb, AxisAlignedBB mask, List list)
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
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		return new AxisAlignedBB(x, y, z, x + 1, y + 0.1875, z + 1);
	}
	
	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
	{
		// Make collision boxes for the two sticks and the base of the campfire.
		EnumAxis facing = (EnumAxis) state.getValue(FACING);
		
		addIfIntersects(getCollisionBoundingBox(worldIn, pos, state), mask, list);
		
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
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity)
	{
		if (!worldIn.isRemote &&
				!entity.isImmuneToFire() &&
				entity instanceof EntityPlayer ? !((EntityPlayer) entity).capabilities.disableDamage : true)
		{
			TileEntityCampfire campfire = getTileEntity(worldIn, pos);
			
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
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		Random rand = worldIn.rand;
		TileEntityCampfire campfire = getTileEntity(worldIn, pos);
		
		if (campfire != null)
		{
			InventoryHelper.dropInventoryItems(worldIn, pos, campfire);
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
	
	protected final Set<ItemStackKey> lighterItems = new HashSet(){{
		add(new ItemStackKey(Items.flint_and_steel));
		add(new ItemStackKey(GenesisItems.flint_and_marcasite));
	}};
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntityCampfire campfire = getTileEntity(worldIn, pos);
		
		if (campfire != null)
		{
			ItemStack heldStack = player.getHeldItem();
			
			if (heldStack != null)
			{
				Item heldItem = heldStack.getItem();
				
				if (lighterItems.contains(new ItemStackKey(heldItem)))
				{
					if (campfire.burnFuelIfNotBurning())
					{
						heldStack.damageItem(1, player);
					}
					
					return true;
				}
				else if (FluidContainerRegistry.containsFluid(heldStack, new FluidStack(FluidRegistry.getFluid("water"), 250)))
				{
					boolean burning = campfire.isBurning();
					Random rand = worldIn.rand;
					
					RandomDoubleRange rangeXZ = new RandomDoubleRange(0.25, 0.75);
					RandomDoubleRange rangeY = new RandomDoubleRange(0.0, 0.25);
					RandomDoubleRange speedXZ = new RandomDoubleRange(-0.08, 0.08);
					RandomDoubleRange speedY = new RandomDoubleRange(0.1, 0.3);
					
					final int bigSmokeCount = 1;
					final int smokeCount = 25;
					final int waterCount = 50;
					final int maxCount = Math.max(bigSmokeCount, Math.max(smokeCount, waterCount));
					
					for (int i = 0; i < maxCount; i++)
					{
						double x = pos.getX() + rangeXZ.getRandom(rand);
						double y = pos.getY() + rangeY.getRandom(rand);
						double z = pos.getZ() + rangeXZ.getRandom(rand);
						
						if (burning)
						{
							if (i < bigSmokeCount)
							{
								worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x, y, z, 0, 0, 0);
							}
							if (i < smokeCount)
							{
								worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0, 0, 0);
							}
						}
						if (i < waterCount)
						{
							worldIn.spawnParticle(GenesisParticles.WATER_SPLASH, x, y, z,
									speedXZ.getRandom(rand), speedY.getRandom(rand), speedXZ.getRandom(rand));
						}
					}
					
					campfire.setWet();
					
					return true;
				}
			}
			
			player.openGui(Genesis.instance, GenesisGuiHandler.CAMPFIRE_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityCampfire();
	}
	
	public TileEntityCampfire getTileEntity(IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity tileEnt = worldIn.getTileEntity(pos);
		
		if (tileEnt instanceof TileEntityCampfire)
		{
			return (TileEntityCampfire)tileEnt;
		}
		
		return null;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack)
	{
		EnumAxis direction = EnumAxis.getForAngle(entity.rotationYaw);
		WorldUtils.setProperty(worldIn, pos, FACING, direction);
		
		if (stack.hasDisplayName())
		{
			TileEntityCampfire te = getTileEntity(worldIn, pos);
			
			if (te != null)
			{
				te.setCustomInventoryName(stack.getDisplayName());
			}
		}
	}

	@Override
	public int getLightValue(IBlockAccess worldIn, BlockPos pos)
	{
		int lightVal = super.getLightValue(worldIn, pos);
		
		IBlockState state = worldIn.getBlockState(pos);
		
		if (state.getBlock() == this && (Boolean) state.getValue(FIRE))
		{
			lightVal = Math.max(lightVal, 15);
		}
		
		return lightVal;
	}
}
