package genesis.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import genesis.client.GenesisClient;
import genesis.common.Genesis;
import genesis.common.GenesisProxy;
import genesis.item.ItemBlockMulti;
import genesis.item.ItemPebble;
import genesis.metadata.*;
import genesis.metadata.ToolTypes.ToolType;
import genesis.metadata.ToolItems.*;
import genesis.metadata.VariantsOfTypesCombo.BlockProperties;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.BlockStateToMetadata;
import genesis.util.RandomVariantDrop;
import genesis.util.SidedFunction;
import genesis.util.render.ModelHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGenesisPebble extends Block
{
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{NW, NE, SE, SW};
	}

	public static final PropertyBool NW = PropertyBool.create("nw");
	public static final PropertyBool NE = PropertyBool.create("ne");
	public static final PropertyBool SE = PropertyBool.create("se");
	public static final PropertyBool SW = PropertyBool.create("sw");
	
	public final ToolItems owner;
	public final ToolObjectType<BlockGenesisPebble, ItemPebble> type;
	
	public final ToolType variant;
	public final PropertyIMetadata<ToolType> variantProp;
	public PropertyInteger randomProp;
	
	public BlockGenesisPebble(ToolType variant, ToolItems owner, ToolObjectType<BlockGenesisPebble, ItemPebble> type)
	{
		super(Material.rock);
		
		this.owner = owner;
		this.type = type;
		
		this.variant = variant;
		variantProp = new PropertyIMetadata<ToolType>("variant", Collections.singletonList(variant));
		
		final String randomName = "zrandom";
		Genesis.proxy.callSided(new SidedFunction()
		{
			@Override
			public void client(GenesisClient client)
			{
				Set<String> variants = ModelHelpers.getBlockstatesVariants(new ResourceLocation("genesis:pebble"));
				
				randomProp = PropertyInteger.create(randomName, 0, 1);
			}
			
			@Override
			public void server(GenesisProxy server)
			{
				randomProp = PropertyInteger.create(randomName, 0, 1);
			}
		});
		
		blockState = new BlockState(this, variantProp, randomProp, NW, NE, SE, SW);
		setDefaultState(getBlockState().getBaseState().withProperty(randomProp, 0).withProperty(NW, false).withProperty(NE, false).withProperty(SE, false).withProperty(SW, false));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, NW, NE, SE, SW);
	}
	
	@Override
	public IBlockState getStateFromMeta(int metadata)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), metadata, NW, NE, SE, SW);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		if (world instanceof World && ((World) world).isRemote)
		{
			long rand = MathHelper.getPositionRandom(pos);
			Collection<Integer> values = randomProp.getAllowedValues();
			int size = values.size();
			Iterator<Integer> iter = values.iterator();
			int value = 0;
			
			for (int i = 0; i < (rand / 5.0F) % size; i++)
			{
				value = iter.next();
			}
			
			state = state.withProperty(randomProp, 0);
		}
		
		return state;
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
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = world.getBlockState(pos);
		
		if (state.getBlock() != this)
		{
			state = owner.getBlockState(type, variant);
		}
		
		boolean north = hitZ < 0.5F;
		boolean east = hitX > 0.5F;
		
		if (north)
		{
			if (east)
			{
				state = state.withProperty(NE, true);
			}
			else
			{
				state = state.withProperty(NW, true);
			}
		}
		else
		{
			if (east)
			{
				state = state.withProperty(SE, true);
			}
			else
			{
				state = state.withProperty(SW, true);
			}
		}
		
		return state;
	}
	
	protected void setBlockBounds(AxisAlignedBB bb)
	{
		setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
	}
	
	protected static enum Part
	{
		NW(BlockGenesisPebble.NW, new AxisAlignedBB(0.0, 0.0, 0.0, 0.5, 0.25, 0.5)),
		NE(BlockGenesisPebble.NE, new AxisAlignedBB(0.5, 0.0, 0.0, 1.0, 0.25, 0.5)),
		SE(BlockGenesisPebble.SE, new AxisAlignedBB(0.5, 0.0, 0.5, 1.0, 0.25, 1.0)),
		SW(BlockGenesisPebble.SW, new AxisAlignedBB(0.0, 0.0, 0.5, 0.5, 0.25, 1.0));
		
		public final PropertyBool prop;
		public final AxisAlignedBB bounds;
		
		private Part(PropertyBool prop, AxisAlignedBB bounds)
		{
			this.prop = prop;
			this.bounds = bounds;
		}
	}
	
	@Override
	public MovingObjectPosition collisionRayTrace(World world, BlockPos pos, Vec3 start, Vec3 end)
	{
		ArrayList<Pair<AxisAlignedBB, MovingObjectPosition>> hits = Lists.newArrayList();
		
		IBlockState state = world.getBlockState(pos);
		boolean hasPebble = false;
		
		for (Part part : Part.values())
		{
			if ((Boolean) state.getValue(part.prop))
			{
				hasPebble = true;
				setBlockBounds(part.bounds);
				MovingObjectPosition hit = super.collisionRayTrace(world, pos, start, end);
				
				if (hit != null)
				{
					hit.subHit = part.ordinal();
					hits.add(Pair.of(part.bounds, hit));
				}
			}
		}
		
		double lastDistSqr = -1;
		Pair<AxisAlignedBB, MovingObjectPosition> hit = null;
		
		for (Pair<AxisAlignedBB, MovingObjectPosition> checkHit : hits)
		{
			double distSqr = checkHit.getRight().hitVec.squareDistanceTo(start);
			
			if (lastDistSqr == -1 || distSqr < lastDistSqr)
			{
				lastDistSqr = distSqr;
				hit = checkHit;
			}
		}
		
		if (hit != null)
		{
			setBlockBounds(hit.getLeft());
			return hit.getRight();
		}
		
		if (!hasPebble)
		{
			setBlockBounds(0, 0, 0, 1, 1, 1);
			return super.collisionRayTrace(world, pos, start, end);
		}
		
		return null;
	}
	
	@Override
	public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
	{
		for (Part part : Part.values())
		{
			AxisAlignedBB bb = part.bounds.offset(pos.getX(), pos.getY(), pos.getZ());
			
			if ((Boolean) state.getValue(part.prop) && mask.intersectsWith(bb))
			{
				list.add(bb);
			}
		}
	}
	
	boolean dropAll = true;
	
	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		MovingObjectPosition hit = player.rayTrace(15, 1);
		IBlockState state = world.getBlockState(hit.getBlockPos());
		
		if (state.getBlock() == this)
		{
			boolean hasPebble = false;
			
			if (hit.subHit >= 0)
			{
				state = state.withProperty(Part.values()[hit.subHit].prop, false);
				
				world.setBlockState(pos, state);
				
				if (!player.capabilities.isCreativeMode)
				{
					dropAll = false;
					dropBlockAsItem(world, pos, state, 0);
					dropAll = true;
				}
			}
			
			for (Part part : Part.values())
			{
				if ((Boolean) state.getValue(part.prop))
				{
					hasPebble = true;
					break;
				}
			}
			
			if (!hasPebble)
			{
				super.removedByPlayer(world, pos, player, willHarvest);
			}
		}
		
		return true;
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		if (dropAll)
		{
			int count = 0;
			
			for (Part part : Part.values())
			{
				if ((Boolean) state.getValue(part.prop))
				{
					count++;
				}
			}
			
			return count;
		}
		
		return 1;
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getStack(type, (ToolType) state.getValue(variantProp)).getItemDamage();
	}
	
	protected boolean canBlockStay(World world, BlockPos pos, IBlockState state)
	{
		pos = pos.down();
		return world.getBlockState(pos).getBlock().isSideSolid(world, pos, EnumFacing.UP);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return canBlockStay(world, pos, world.getBlockState(pos));
	}
	
	protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state)
	{
		if (!canBlockStay(world, pos, state))
		{
			world.destroyBlock(pos, true);
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		checkAndDropBlock(world, pos, state);
	}
}
