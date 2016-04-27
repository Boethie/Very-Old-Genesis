package genesis.block;

import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms.CanStayOptions;
import genesis.common.GenesisCreativeTabs;
import genesis.util.*;
import genesis.util.random.drops.blocks.BlockDrops;
import genesis.util.random.i.IntRange;

import java.util.*;
import java.util.Map.Entry;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.*;
import net.minecraftforge.common.*;

public class BlockGrowingPlant extends BlockBush implements IGrowable
{
	protected interface IPerBlockCall
	{
		void call(World world, BlockPos curPos, IBlockState curState, BlockPos startPos);
	}
	
	public static class GrowingPlantProperties
	{
		protected IBlockAccess world;
		protected BlockPos startPos;
		
		protected BlockGrowingPlant plant = null;
		protected int toBottom = -1;
		protected int toTop = -1;
		protected int height = -1;
		protected BlockPos top = null;
		protected BlockPos bottom = null;
		
		public GrowingPlantProperties(IBlockAccess world, BlockPos startPos, BlockGrowingPlant plant)
		{
			this.world = world;
			this.startPos = startPos;
			this.plant = plant;
		}
		
		public GrowingPlantProperties(IBlockAccess world, BlockPos startPos, IBlockState startState)
		{
			this(world, startPos, (BlockGrowingPlant) startState.getBlock());
		}
		
		public GrowingPlantProperties(IBlockAccess world, BlockPos startPos)
		{
			this(world, startPos, world.getBlockState(startPos));
		}
		
		public BlockGrowingPlant getPlant()
		{
			if (plant == null)
			{
				plant = (BlockGrowingPlant) world.getBlockState(startPos).getBlock();
			}
			
			return plant;
		}
		
		/**
		 * Initializes all of the properties of this GrowingPlantProperties. For debugging only.
		 */
		@Deprecated
		public void initAll()
		{
			getTop();
			getBottom();
			getHeight();
		}
		
		/**
		 * @return Distance to the top of the plant (inclusive of the starting BlockPos).
		 */
		public int getToTop()
		{
			if (toTop == -1)
			{
				getPlant();
				int theToTop;
				
				for (theToTop = 1; world.getBlockState(startPos.up(theToTop)).getBlock() == plant; theToTop++)
				{}
				
				toTop = theToTop;
			}
			
			return toTop;
		}
		
		/**
		 * @return Distance to the bottom of the plant (inclusive of the starting BlockPos).
		 */
		public int getToBottom()
		{
			if (toBottom == -1)
			{
				getPlant();
				int theToBottom = 1;
				
				while (world.getBlockState(startPos.down(theToBottom)).getBlock() == plant)
				{
					theToBottom++;
				}
				
				/*for (theToBottom = 1; world.getBlockState(startPos.down(theToBottom)).getBlock() == plant; theToBottom++)
				{}*/
				
				toBottom = theToBottom;
			}
			
			return toBottom;
		}
		
		/**
		 * @return Height of the plant.
		 */
		public int getHeight()
		{
			if (height == -1)
			{
				height = getToTop() + getToBottom() - 1;
			}
			
			return height;
		}
		
		/**
		 * @return The BlockPos at the bottom of the plant.
		 */
		public BlockPos getBottom()
		{
			if (bottom == null)
			{
				bottom = startPos.down(getToBottom() - 1);
			}
			
			return bottom;
		}
		
		/**
		 * @return The BlockPos at the top of the plant.
		 */
		public BlockPos getTop()
		{
			if (top == null)
			{
				top = startPos.up(getToTop() - 1);
			}
			
			return top;
		}
		
		/**
		 * @param pos Position to check.
		 * @return Whether the position is at the top of the plant.
		 */
		public boolean isTop(BlockPos pos)
		{
			return pos.equals(getTop());
		}
		
		/**
		 * @param pos Position to check.
		 * @return Whether the position is at the bottom of the plant.
		 */
		public boolean isBottom(BlockPos pos)
		{
			return pos.equals(getBottom());
		}
	}

	public interface IGrowingPlantCustoms
	{
		/**
		 * Gets the items that should be dropped for this BlockGrowingPlant when it is broken at pos.
		 * 
		 * @param plant The BlockGrowingPlant that is calling the method.
		 * @return An ArrayList of ItemStacks to drop from this block's position.
		 */
		List<ItemStack> getPlantDrops(BlockGrowingPlant plant, World world, BlockPos pos, IBlockState state, int fortune);
		
		/**
		 * Called after updateTick in a BlockGrowingPlant.
		 * 
		 * @param grew Whether the plant grew in this random block update.
		 */
		void plantUpdateTick(BlockGrowingPlant plant, World world, BlockPos pos, IBlockState state, Random rand, boolean grew);
		
		enum CanStayOptions {
			YES,
			YIELD,
			NO
		}
		
		/**
		 * @param placed Whether the block has been placed yet. To allow customs to prevent placing stacked plant blocks.
		 * @return Whether the BlockGrowingPlant can grow at the specified BlockPos.
		 */
		CanStayOptions canPlantStayAt(BlockGrowingPlant plant, World world, BlockPos pos, boolean placed);
		
		/**
		 * For use in adding/removing IPropertys in a list of IPropertys that should be stored in metadata.
		 * @param plant This BlockGrowingPlant.
		 * @param metaProps The list whose contents must be stored in metadata.
		 */
		void managePlantMetaProperties(BlockGrowingPlant plant, ArrayList<IProperty<?>> metaProps);
		
		/**
		 * @return Whether to consume and use bonemeal to grow the plant.
		 */
		boolean shouldUseBonemeal(World world, BlockPos pos, IBlockState state);
	}
	
	public PropertyInteger ageProp;
	public PropertyBool topProp;
	public IProperty<?>[] metaProperties;
	
	protected final boolean topProperty;
	
	protected int growthAge;
	protected int maxAge;
	protected boolean growTogether = false;
	protected boolean breakTogether = false;
	protected boolean resetAge = false;
	protected boolean allowBonemeal = true;
	protected boolean useBiomeColor = false;
	protected boolean allowPlacingStacked = true;
	
	protected BlockDrops drops;
	protected BlockDrops cropDrops;
	protected ItemStack pickedStack = null;
	
	IGrowingPlantCustoms customs = null;
	
	EnumPlantType[] soilTypes = {EnumPlantType.Plains};
	
	protected int maxHeight;
	protected int topBlockPos = -1;
	
	protected float baseHeight = 0.25F;
	protected float heightPerStage = 0F;
	protected float width = 1F;
	protected AxisAlignedBB collisionBox = null;
	
	public BlockGrowingPlant(Material material, boolean topProperty, int maxAge, int growthAge, int maxHeight)
	{
		super(material);
		
		this.maxAge = maxAge;
		this.growthAge = growthAge;
		
		this.topProperty = topProperty;
		this.maxHeight = maxHeight;
		
		blockState = createOurBlockState();
		
		setTickRandomly(true);
		setSoundType(SoundType.PLANT);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		
		disableStats();
		
		if (this instanceof IGrowingPlantCustoms)
		{
			setCustoms((IGrowingPlantCustoms) this);
		}
	}
	
	public BlockGrowingPlant(Material material, boolean topProperty, int maxAge, int height)
	{
		this(material, topProperty, maxAge, maxAge + 1, height);
	}
	
	public BlockGrowingPlant(boolean topProperty, int maxAge, int growthAge, int height)
	{
		this(Material.plants, topProperty, maxAge, growthAge, height);
	}
	
	public BlockGrowingPlant(boolean topProperty, int maxAge, int height)
	{
		this(Material.plants, topProperty, maxAge, height);
	}
	
	/**
	 * Creates the BlockStateContainer for this BlockGrowingPlant. This was used to make the BlockStateContainer's properties depend on
	 * values given to the BlockGrowingPlant in the constructor.
	 * 
	 * @return Returns the BlockStateContainer containing all properties used by this block.
	 */
	protected BlockStateContainer createOurBlockState()
	{
		BlockStateContainer state;
		
		ageProp = PropertyInteger.create("age", 0, maxAge);
		
		if (topProperty)
		{
			topProp = PropertyBool.create("top");
			state = new BlockStateContainer(this, ageProp, topProp);
			setDefaultState(state.getBaseState().withProperty(ageProp, 0).withProperty(topProp, false));
		}
		else
		{
			state = new BlockStateContainer(this, ageProp);
			setDefaultState(state.getBaseState().withProperty(ageProp, 0));
		}
		
		ArrayList<IProperty<?>> metaProps = new ArrayList<>(state.getProperties());
		
		if (topProperty)
		{
			metaProps.remove(topProp);
		}
		
		if (customs != null)
		{
			customs.managePlantMetaProperties(this, metaProps);
		}
		
		metaProperties = metaProps.toArray(new IProperty[0]);

		return state;
	}
	
	public int getGrowthAge()
	{
		return growthAge;
	}
	
	/**
	 * Sets the position in a plant column that the "top" property should be true at.
	 * -1 = Default, the top property will be the top plant block in the column.
	 */
	public BlockGrowingPlant setTopPosition(int topPos)
	{
		topBlockPos = topPos;
		return this;
	}
	
	/**
	 * Set a property to make all the plant's blocks age stay the same, and age as the first block ages.
	 */
	public BlockGrowingPlant setGrowAllTogether(boolean growTogetherIn)
	{
		growTogether = growTogetherIn;
		return this;
	}

	/**
	 * Sets the block to use the biome color multiplier like vanilla grass and ferns.
	 */
	public BlockGrowingPlant setUseBiomeColor(boolean use)
	{
		useBiomeColor = use;
		return this;
	}
	
	/*@Override
	public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int renderPass)
	{
		return useBiomeColor ? BiomeColorHelper.getGrassColorAtPos(world, pos) : 0xFFFFFF;
	}
	
	@Override
	public int getRenderColor(IBlockState state)
	{
		return useBiomeColor ? ColorizerGrass.getGrassColor(0.5, 1) : 0xFFFFFF;
	}*/
	
	/**
	 * Sets a property to cause the entire plant column to break at once and drop only the first block's
	 * drops.
	 * false = Default, only the plant blocks above the broken one will break as well.
	 * true = All the plant blocks in the column will break at once and drop only the first block's drops.
	 */
	public BlockGrowingPlant setBreakAllTogether(boolean breakTogetherIn)
	{
		breakTogether = breakTogetherIn;
		return this;
	}
	
	/**
	 * Sets the EnumPlantType that the plant block should grow on.
	 */
	public BlockGrowingPlant setPlantSoilTypes(EnumPlantType... soilTypes)
	{
		this.soilTypes = soilTypes;
		return this;
	}
	
	/**
	 * Gets the soil types that this plant can survive on.
	 */
	public EnumPlantType[] getPlantSoilTypes()
	{
		return soilTypes;
	}
	
	/**
	 * Cause the plant block to reset its age when they grow taller.
	 */
	public BlockGrowingPlant setResetAgeOnGrowth(boolean resetAgeIn)
	{
		resetAge = resetAgeIn;
		return this;
	}
	
	/**
	 * Sets whether to allow bonemeal to be used on this plant.
	 */
	public BlockGrowingPlant setAllowBonemealUse(boolean allow)
	{
		allowBonemeal = allow;
		return this;
	}
	
	/**
	 * Sets the drops when the plant is NOT fully grown.
	 */
	public BlockGrowingPlant setDrops(BlockDrops dropsIn)
	{
		drops = dropsIn;
		return this;
	}
	
	/**
	 * Sets the drops when the plant is fully grown.
	 */
	public BlockGrowingPlant setCropDrops(BlockDrops dropsIn)
	{
		cropDrops = dropsIn;
		return this;
	}
	
	/**
	 * Sets the Item picked when the user middle clicks on the block in creative mode.
	 */
	public BlockGrowingPlant setPickedStack(ItemStack stack)
	{
		pickedStack = stack.copy();
		return this;
	}
	
	/**
	 * Sets whether this plant can be placed on top of itself.
	 */
	public BlockGrowingPlant setAllowPlacingStacked(boolean allow)
	{
		allowPlacingStacked = allow;
		return this;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		if (pickedStack != null)
		{
			return pickedStack;
		}
		
		return new ItemStack(this);
	}
	
	/**
	 * Sets the block bounds size of the plant.
	 * 
	 * @param hBase The height of the plant initially.
	 * @param hPerStage The height added for each stage (stage 0 counts too).
	 * @param w The width of the plant.
	 * @return
	 */
	public BlockGrowingPlant setPlantSize(float hBase, float hPerStage, float w)
	{
		baseHeight = hBase;
		heightPerStage = hPerStage;
		width = w;
		return this;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		GrowingPlantProperties props = new GrowingPlantProperties(world, pos, state);
		float w2 = width / 2;
		
		AxisAlignedBB newBB = new AxisAlignedBB(0.5 - w2, 0, 0.5 - w2, 0.5 + w2, 0, 0.5 + w2);
		
		if (props.isTop(pos))
		{
			newBB = newBB.addCoord(0, baseHeight, 0);
			
			if (heightPerStage > 0)
			{
				int stage = state.getValue(ageProp);
				
				if (props.getToBottom() > 1)
					stage -= growthAge;
				
				newBB.addCoord(0, (stage + 1) * heightPerStage, 0);
			}
		}
		else
		{
			newBB = newBB.addCoord(0, 1, 0);
		}
		
		return newBB;
	}
	
	/**
	 * Sets the collision bounds of the plant.
	 * 
	 * @param bb The AxisAlignedBB to use for entity collision.
	 */
	public BlockGrowingPlant setCollisionBox(AxisAlignedBB bb)
	{
		collisionBox = bb;
		return this;
	}
	
	/**
	 * Sets the collision bounds of the plant to a cuboid reaching from the bottom of the block to the top.
	 * 
	 * @param radius The radius of the bounds.
	 */
	public BlockGrowingPlant setCollisionBox(double radius)
	{
		return setCollisionBox(new AxisAlignedBB(0.5 - radius, 0, 0.5 - radius, 0.5 + radius, 1, 0.5 + radius));
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity entity)
	{
		if (collisionBox != null)
			addCollisionBoxToList(pos, mask, list, collisionBox);
	}
	
	/**
	 * Sets the drops when the plant is fully grown.
	 */
	public BlockGrowingPlant setCustoms(IGrowingPlantCustoms customsIn)
	{
		customs = customsIn;
		return this;
	}
	
	/**
	 * Returns whether the block at pos should have the top property set to true.
	 * This takes this.topBlockPos into account as well, so it will not always be the highest block in the plant.
	 * 
	 * @param world
	 * @param pos The position to check.
	 * @return Whether the block should have "top" property set true.
	 */
	protected boolean isTop(IBlockAccess world, BlockPos pos)
	{
		boolean top;
		
		// If topBlock position is defined, set it according to that
		if (topBlockPos >= 0)
		{
			int toBottom = new GrowingPlantProperties(world, pos, this).getToBottom();
			top = toBottom == topBlockPos;
		}
		else	// Otherwise, set according to if it really is top
		{
			top = world.getBlockState(pos.up()).getBlock() != this;
		}
		
		return top;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (resetAge)
		{
			BlockPos below = pos.down();
			IBlockState stateBelow = world.getBlockState(below);
			
			if (stateBelow.getBlock() == this)
			{
				world.setBlockState(below, stateBelow.withProperty(ageProp, 0));
			}
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		super.onNeighborBlockChange(world, pos, state, neighborBlock);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state, metaProperties);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta, metaProperties);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		if (topProperty)
		{
			state = state.withProperty(topProp, isTop(world, pos));
		}
		
		return state;
	}

	protected float baseAgeChance = 0.08F;
	protected float farmlandChanceMult = 1.0F;
	protected float fertileChanceMult = 2.0F;
	protected float neighborFertileChanceMult = 1.025F;
	
	/**
	 * Causes the plant to grow each time updateTick() is called.
	 * @return
	 */
	public BlockGrowingPlant setFullGrowthChance()
	{
		baseAgeChance = 1;
		farmlandChanceMult = 1;
		fertileChanceMult = 1;
		neighborFertileChanceMult = 1;
		return this;
	}
	
	/**
	 * Sets the chance (the code uses 1 in [chance]) that the plant will grow in each random tick.
	 * 
	 * @param base The base chance value. 
	 * @param fertileMult The chance is multiplied by this value if the block under the plant is fertile.
	 * @param neighborFertileMult The chance is multiplied by this value for each fertile land block around it (including at corners).
	 * @return
	 */
	public BlockGrowingPlant setGrowth(float base, float farmlandMult, float fertileMult, float neighborFertileMult)
	{
		if (base >= 0)
			baseAgeChance = base;
		if (farmlandMult >= 0)
			farmlandChanceMult = farmlandMult;
		if (fertileMult >= 0)
			fertileChanceMult = fertileMult;
		if (neighborFertileMult >= 0)
			neighborFertileChanceMult = neighborFertileMult;
		
		return this;
	}
	
	/**
	 * Sets the base growth chance.
	 */
	public BlockGrowingPlant setGrowthBase(float mult)
	{
		baseAgeChance = mult;
		return this;
	}
	
	/**
	 * Sets the growth chance multiplier for when the plant is not growing on farmland.
	 */
	public BlockGrowingPlant setGrowthOnFarmland(float mult)
	{
		farmlandChanceMult = mult;
		return this;
	}
	
	/**
	 * Sets the growth chance multiplier for when the plant is on fertile farmland.
	 */
	public BlockGrowingPlant setGrowthOnFertileFarmland(float mult)
	{
		fertileChanceMult = mult;
		return this;
	}
	
	/**
	 * Sets the growth chance multiplier for each neighbor farmland block that is fertile.
	 */
	public BlockGrowingPlant setGrowthForFertileNeighbors(float mult)
	{
		neighborFertileChanceMult = mult;
		return this;
	}
	
	/**
	 * Gets the chance that a random tick of a crop block will result in the crop aging. 1 is added to the output to determine the actual chance.
	 */
	public float getGrowthChance(World world, BlockPos pos, IBlockState state)
	{
		// If the base age is greater than 0, then we must calculate the chance based on surrounding blocks
		if (baseAgeChance > 0)
		{
			// Default base chance is <= 1 in 7.5
			float rate = baseAgeChance;
			
			BlockPos under = pos.down();
			IBlockState blockUnder = world.getBlockState(under);
			
			if (fertileChanceMult != 0 && blockUnder.getBlock().isFertile(world, under))
			{	// If the land under the crop is fertile, chance will be <= 1 in 4.25 by default
				rate *= fertileChanceMult;
			}
			
			if (neighborFertileChanceMult != 0)
			{
				// Make fertile neighboring land slightly affect the growth chance (4.25 -> ~3.65 max)
				for (int x = -1; x <= 1; x++)
				{
					for (int z = -1; z <= 1; z++)
					{
						if (x != 0 || z != 0)
						{
							BlockPos landPos = under.add(x, 0, z);
							IBlockState landState = world.getBlockState(landPos);
							
							if ((canPlaceBlockOnSide(world, landPos, EnumFacing.UP) && landState.getBlock().isFertile(world, landPos)) ||
								landState.getMaterial() == Material.water)
							{
								rate *= neighborFertileChanceMult;
							}
						}
					}
				}
			}
			
			if (farmlandChanceMult != 0 && blockUnder.getBlock() == Blocks.farmland)
			{
				rate *= farmlandChanceMult;
			}
			
			return rate;
		}
		
		return baseAgeChance;
	}
	
	/**
	 * Calls the call() method in the IPerBlockCall on each block in the plant column, starting with fromBlock.
	 * 
	 * @param world
	 * @param fromBlock The BlockPos to start from.
	 * @param call The IPerBlockCall whose call() method is called.
	 */
	protected void callForEachInColumn(World world, BlockPos fromBlock, IPerBlockCall call)
	{
		GrowingPlantProperties props = new GrowingPlantProperties(world, fromBlock);
		
		BlockPos top = props.getTop();
		int height = props.getHeight();
		
		for (int y = 0; y < height; y++)
		{
			BlockPos curPos = top.down(y);
			
			call.call(world, curPos, world.getBlockState(curPos), fromBlock);
		}
	}
	
	protected float minGrowthLight = 9;
	
	/**
	 * Attempts to grow the plant according to its rules of growth. Will use getGrowthChance unless an override chance is given.
	 * 
	 * @param world The world.
	 * @param rand The random number generator used to determine if the plant actually grows.
	 * @param pos
	 * @param state
	 * @param forceGrow Causes the plant to ignore its growth chance, and grows 100% of the time.
	 * @param noChange Cancel all changes to the world. This is used to determine if a plant can grow.
	 * @return Whether the plant was changed.
	 */
	public boolean grow(World world, Random rand, BlockPos pos, IBlockState state, boolean forceGrow, boolean noChange)
	{
		boolean changed = false;
		
		GrowingPlantProperties props = new GrowingPlantProperties(world, pos, (BlockGrowingPlant) state.getBlock());
		
		if ((!growTogether && props.isTop(pos)) || (growTogether && props.isBottom(pos)))
		{
			int oldAge = state.getValue(ageProp);
			int age = oldAge;
			
			// Age the plant (using the chance generated from getGrowthChance)
			if (forceGrow || world.getLightFromNeighbors(pos) >= minGrowthLight)
			{
				float chance = 1;
				
				if (!forceGrow)
				{
					chance = getGrowthChance(world, pos, state);
				}
				
				if (chance >= 1 || rand.nextFloat() <= chance)
				{
					age++;
					
					// Add to the height of the plant if this is the top block and has aged fully
					if (props.getHeight() < maxHeight && age >= growthAge)
					{
						BlockPos above = pos.up();
						
						if (world.isBlockLoaded(above))
						{
							Block aboveBlock = world.getBlockState(above).getBlock();
							
							if (aboveBlock != this && aboveBlock.isReplaceable(world, above))
							{
								changed = true;
								
								if (!noChange)
								{
									world.setBlockState(above, getDefaultState());
									
									if (resetAge)
									{
										age = 0;
									}
								}
							}
						}
						
						if (!changed)
						{
							age = growthAge - 1; // Reset the age to the growth age if it can't grow taller.
						}
					}
				}
			}
			
			age = MathHelper.clamp_int(age, 0, maxAge);
			changed = changed || age != oldAge;
			
			if (!noChange)
			{
				if (growTogether)
				{
					final int setAge = age;
					callForEachInColumn(world, pos, new IPerBlockCall() {
							@Override
							public void call(World world, BlockPos curPos, IBlockState curState, BlockPos startPos) {
								world.setBlockState(curPos, curState.withProperty(ageProp, setAge));
							}
						});
				}
				else
				{
					world.setBlockState(pos, state.withProperty(ageProp, age), 2);
				}
			}
		}
		
		return changed;
	}
	
	/**
	 * Determines whether the bonemeal is consumed when activating this plant.
	 */
	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
	{
		if (allowBonemeal && (customs == null || customs.shouldUseBonemeal(world, pos, state)))
		{
			BlockPos growOn = pos;
			
			if (growTogether)
			{
				growOn = new GrowingPlantProperties(world, pos).getBottom();
			}
			
			return grow(world, world.rand, growOn, world.getBlockState(growOn), true, true);
		}
		return false;
	}
	
	/**
	 * Used by vanilla to tell whether the plant should actually grow when bonemeal is used and consumed by a plant.
	 */
	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state)
	{
		return true;
	}
	
	/**
	 * Called by bonemeal to grow the plant.
	 */
	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state)
	{
		if (!world.isRemote)
		{
			BlockPos growOn = pos;
			
			if (growTogether)
			{
				growOn = new GrowingPlantProperties(world, pos).getBottom();
			}
			
			int growth = MathHelper.getRandomIntegerInRange(world.rand, 2, 5);
			
			for (int i = 0; i < growth; i++)
			{
				grow(world, rand, growOn, world.getBlockState(growOn), true, false);
			}
		}
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		checkAndDropBlock(world, pos, state);
		
		state = world.getBlockState(pos);
		
		// If the plant was not broken by checkAndDropBlock(), attempt to grow it.
		if (state.getBlock() == this)
		{
			boolean grew = grow(world, rand, pos, state, false, false);
			
			if (customs != null)
			{
				customs.plantUpdateTick(this, world, pos, state, rand, grew);
			}
		}
	}
	
	/**
	 * @return Whether the plant can survive at this location in the world.
	 */
	public boolean canPlantSurvive(World world, BlockPos pos, boolean placed)
	{
		CanStayOptions stay = CanStayOptions.YIELD;
		
		if (customs != null)
		{
			stay = customs.canPlantStayAt(this, world, pos, placed);
		}
		
		switch (stay)
		{
		case YES:
			return true;
		case NO:
			break;
		case YIELD:
			BlockPos below = pos.down();
			IBlockState stateBelow = world.getBlockState(below);
			
			if (WorldUtils.canSoilSustainTypes(world, pos, getPlantSoilTypes()))
			{
				return true;
			}
			
			if (stateBelow.getBlock() == this)
			{
				GrowingPlantProperties props = new GrowingPlantProperties(world, pos, this);
				int height = props.getToBottom();
				
				if (height <= maxHeight && height > 0 &&
						(resetAge || stateBelow.getValue(ageProp) >= growthAge))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
	{
		return canPlantSurvive(world, pos, true);
	}
	
	@Override
	protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state)
	{
		if (!canBlockStay(world, pos, state))
		{
			destroyPlant(world, pos, null, true, true);
		}
	}
	
	@Override
	protected boolean canSustainBush(IBlockState ground)
	{
		// Return false so that canSustainPlant checks the plant type instead of BlockBush.canPlaceBlockOn().
		return false;
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		if (!allowPlacingStacked && world.getBlockState(pos.down()).getBlock() == this)
			return false;
		
		return world.getBlockState(pos).getBlock().isReplaceable(world, pos) && canPlantSurvive(world, pos, false);
	}
	
	/**
	 * Handles removal of a plant block.
	 * If breakTogether is true, it removes all plant blocks of this type in a column that they should grow in, optionally dropping items.
	 * Otherwise, it will handle setting the BlockStateContainer to acceptable values for this type of plant.
	 */
	protected void destroyPlant(World world, BlockPos pos, EntityPlayer breaker, boolean drop, boolean noBreakTogether)
	{
		final HashMap<BlockPos, IBlockState> oldStates = new HashMap<BlockPos, IBlockState>();
		
		if (breakTogether && !noBreakTogether)
		{
			callForEachInColumn(world, pos, new IPerBlockCall() {
				@Override
				public void call(World world, BlockPos curPos, IBlockState curState, BlockPos startPos) {
					oldStates.put(curPos, getActualState(world.getBlockState(curPos), world, curPos));
				}
			});
		}
		else
		{
			int up = 0;
			
			while (true)
			{
				BlockPos remPos = pos.up(up);
				
				IBlockState remState = world.getBlockState(remPos);

				if (remState.getBlock() == this)
				{
					remState = getActualState(remState, world, remPos);
					oldStates.put(remPos, remState);
				}
				else
				{
					break;
				}
				
				up++;
			}
		}

		// Spawn breaking particles for blocks that weren't directly broken by the player
		for (Entry<BlockPos, IBlockState> entry : oldStates.entrySet())
		{
			BlockPos particlePos = entry.getKey();
			
			if (!particlePos.equals(pos))
			{
				world.playAuxSFXAtEntity(breaker, 2001, particlePos, Block.getStateId(entry.getValue()));
			}
		}
		
		GrowingPlantProperties props = new GrowingPlantProperties(world, pos);
		
		// Set blocks to air without notifying neighbors of the change (so items aren't dropped when they shouldn't).
		// Also drop items.
		for (Entry<BlockPos, IBlockState> entry : oldStates.entrySet())
		{
			BlockPos breakPos = entry.getKey();
			IBlockState oldState = entry.getValue();
			
			if (drop)
			{
				boolean isBase = props.isBottom(breakPos);
				dropBlockAsItemWithChance(world, breakPos, oldState, (isBase ? -1 : -2), 0);
			}
			
			world.setBlockState(breakPos, Blocks.air.getDefaultState(), 2);
		}
		
		// Notify of block changes.
		for (Entry<BlockPos, IBlockState> entry : oldStates.entrySet())
		{
			BlockPos notifyPos = entry.getKey();
			IBlockState oldState = entry.getValue();
			
			world.markAndNotifyBlock(notifyPos, world.getChunkFromBlockCoords(notifyPos), oldState, world.getBlockState(notifyPos), 1);
		}
		
		// If the plant grows together (meaning all plant blocks should be the same age in a column),
		// set blocks underneath the broken ones to the age before the plant grew taller than it is after being destroyed.
		if (growTogether && !breakTogether)
		{
			BlockPos under = pos.down();
			Block blockUnder = world.getBlockState(under).getBlock();
			
			if (blockUnder == this)
			{
				callForEachInColumn(world, under, new IPerBlockCall() {
					@Override
					public void call(World world, BlockPos curPos, IBlockState curState, BlockPos startPos) {
						world.setBlockState(curPos, curState.withProperty(ageProp, growthAge - 1));
					}
				});
			}
		}
	}
	
	/**
	 * Drops ItemStacks in EntityItems according to the plant's properties for dropping items.
	 * 
	 * @param world
	 * @param pos
	 * @param state
	 * @param chance With a value less than 0, the block will be dropped.
	 * Otherwise, it will destroy the plant as it should normally, but only if a player is not breaking it.
	 * @param fortune
	 */
	@Override
	public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune)
	{
		if (!world.isRemote)
		{
			if (chance <= -1)	// Prevent the block dropping without our code telling it to explicitly
			{
				state = getActualState(state, world, pos);
				List<ItemStack> dropStacks = null;
				
				if (customs != null)
				{
					dropStacks = customs.getPlantDrops(this, world, pos, state, fortune);
				}
				
				if (dropStacks == null)
				{
					if (!breakTogether || (breakTogether && chance == -1))
					{
						if (state.getValue(ageProp) < maxAge)
						{
							if (drops != null)
							{
								dropStacks = drops.getDrops(state, world.rand);
							}
						}
						else
						{
							if (cropDrops != null)
							{
								dropStacks = cropDrops.getDrops(state, world.rand);
							}
						}
					}
				}
				
				if (dropStacks != null)
				{
					for (ItemStack stack : dropStacks)
					{
						spawnAsEntity(world, pos, stack);
					}
				}
			}
			else if (harvesters.get() == null)
			{
				// If the game tries to drop this block, handle it with destroyPlant()
				// This handles explosions and drops due to the block being unable to stay.
				destroyPlant(world, pos, null, world.rand.nextFloat() <= chance, false);
			}
		}
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
	{
		// Do not allow the explosions to directly destroy the block, instead let dropBlockAsItemWithChance() handle destruction.
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		super.onBlockHarvested(world, pos, state, player);
	}
	
	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
	{
		super.harvestBlock(world, player, pos, state, te, stack);
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		destroyPlant(world, pos, player, !player.capabilities.isCreativeMode, false);
		
		return true;
	}
	
	/**
	 * Places a plant with a random height and age value.
	 */
	public boolean placeRandomAgePlant(World world, BlockPos pos, Random rand)
	{
		if (!canPlantSurvive(world, pos, true))
			return false;
		
		int targetHeight = IntRange.create(1, maxHeight).get(rand);
		
		for (int i = 0; i < targetHeight; i++)
		{
			BlockPos checkPos = pos.up(i);
			IBlockState checkState = world.getBlockState(checkPos);
			
			if (!checkState.getBlock().isAir(checkState, world, checkPos))
			{
				targetHeight = i;
				break;
			}
		}
		
		if (targetHeight > 0)
		{
			IntRange ageRange;
			
			if (growTogether)
			{
				int min = Math.min((targetHeight - 1) * growthAge, maxAge);
				int max = Math.min(targetHeight * growthAge - 1, maxAge);
				ageRange = IntRange.create(min, max);
				ageRange = IntRange.create(ageRange.get(rand));
			}
			else
			{
				ageRange = IntRange.create(0, maxAge);
			}
			
			for (int i = 0; i < targetHeight; i++)
			{
				int age = maxAge;
				
				if (growTogether || i == targetHeight - 1)
				{
					age = ageRange.get(rand);
				}
				
				world.setBlockState(pos.up(i), getDefaultState().withProperty(ageProp, age), 2);
			}
			
			return true;
		}
		
		return false;
	}
}
