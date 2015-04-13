package genesis.block;

import genesis.block.BlockGrowingPlant.IGrowingPlantCustoms.CanStayOptions;
import genesis.common.GenesisCreativeTabs;
import genesis.util.BlockStateToMetadata;
import genesis.util.Constants;
import genesis.util.RandomItemDrop;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGrowingPlant extends BlockCrops implements IGrowable
{
	protected static interface IPerBlockCall
	{
		void call(World worldIn, BlockPos curPos, IBlockState curState, BlockPos startPos, Object... args);
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
		
		public GrowingPlantProperties(IBlockAccess worldIn, BlockPos pos)
		{
			world = worldIn;
			startPos = pos;
		}
		
		public GrowingPlantProperties(IBlockAccess worldIn, BlockPos pos, BlockGrowingPlant plantIn)
		{
			world = worldIn;
			startPos = pos;
			plant = plantIn;
		}
		
		protected void getPlant()
		{
			if (plant == null)
			{
				plant = (BlockGrowingPlant) world.getBlockState(startPos).getBlock();
			}
		}
		
		/**
		 * Initializes all of the properties of this GrowingPlantProperties. For debugging only.
		 */
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
				{
				}
				
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
				
				int theToBottom;
	
				for (theToBottom = 1; world.getBlockState(startPos.down(theToBottom)).getBlock() == plant; theToBottom++)
				{
				}
				
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
				getToTop();
				getToBottom();
				
				height = toTop + toBottom - 1;
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
				getToBottom();
				
				bottom = startPos.down(toBottom - 1);
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
				getToTop();
				
				top = startPos.up(toTop - 1);
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

	public static interface IGrowingPlantCustoms {
		/**
		 * Gets the items that should be dropped for this BlockGrowingPlant when it is broken at pos.
		 * 
		 * @param plant The BlockGrowingPlant that is calling the method.
		 * @param worldIn
		 * @param pos
		 * @param state
		 * @param fortune
		 * @param firstBlock Whether the BlockPos pos is at the first position in the plant's height.
		 * @return An ArrayList of ItemStacks to drop from this block's position.
		 */
		public ArrayList<ItemStack> getPlantDrops(BlockGrowingPlant plant, World worldIn, BlockPos pos, IBlockState state, int fortune, boolean firstBlock);
		
		/**
		 * Called after updateTick in a BlockGrowingPlant.
		 * 
		 * @param plant The BlockGrowingPlant that is calling the method.
		 * @param worldIn
		 * @param pos
		 * @param state
		 * @param random
		 * @param grew Whether the plant grew in this random block update.
		 */
		public void plantUpdateTick(BlockGrowingPlant plant, World worldIn, BlockPos pos, IBlockState state, Random rand, boolean grew);

		public static enum CanStayOptions {
			YES,
			YIELD,
			NO
		}
		
		/**
		 * @param plant The BlockGrowingPlant that is calling the method.
		 * @param worldIn
		 * @param pos The position that the plant would at.
		 * @param placed Whether the block has been placed yet. To allow customs to prevent placing stacked plant blocks.
		 * @return Whether the BlockGrowingPlant can grow at the specified BlockPos.
		 */
		public CanStayOptions canPlantStayAt(BlockGrowingPlant plant, World worldIn, BlockPos pos, boolean placed);

		/**
		 * For use in adding/removing IPropertys in a list of IPropertys that should be stored in metadata.
		 * @param plant This BlockGrowingPlant.
		 * @param metaProps The list whose contents must be stored in metadata.
		 */
		public void managePlantMetaProperties(BlockGrowingPlant plant, ArrayList<IProperty> metaProps);
	}
	
	public PropertyInteger ageProp;
	public PropertyBool topProp;
	public IProperty[] metaProperties;

	protected final boolean hasTopProperty;

	public int growthAge;
	public int maxAge;
	public boolean growTogether = false;
	public boolean breakTogether = false;
	public boolean resetAge = false;
	public boolean useBiomeColor = false;

	protected ArrayList<RandomItemDrop> drops = new ArrayList<RandomItemDrop>();
	protected ArrayList<RandomItemDrop> cropDrops = new ArrayList<RandomItemDrop>();
	protected Item pickedItem = null;
	
	IGrowingPlantCustoms customs = null;
	
	EnumPlantType plantType = EnumPlantType.Plains;

	protected int maxHeight;
	protected int topBlockPos = -1;

	protected float baseHeight = 0.25F;
	protected float heightPerStage = 0F;
	protected float width = 1F;
	protected AxisAlignedBB collisionBox = null;

	public BlockGrowingPlant(boolean topPropertyIn, int maxAgeIn, int growthAgeIn, int height)
	{
		super();

		maxAge = maxAgeIn;
		growthAge = growthAgeIn;

		hasTopProperty = topPropertyIn;
		maxHeight = height;
		
		this.blockState = createOurBlockState();

		setTickRandomly(true);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		
		if (this instanceof IGrowingPlantCustoms)
		{
			setCustomsInterface((IGrowingPlantCustoms) this);
		}
	}

	public BlockGrowingPlant(boolean topPropertyIn, int maxAgeIn, int height)
	{
		this(topPropertyIn, maxAgeIn, maxAgeIn, height);
	}

	@Override
	public BlockGrowingPlant setUnlocalizedName(String unlocalizedName)
	{
		super.setUnlocalizedName(Constants.PREFIX + unlocalizedName);
		
		return this;
	}

	/**
	 * Creates the BlockState for this BlockGrowingPlant. This was used to make the BlockState's properties depend on
	 * values given to the BlockGrowingPlant in the constructor.
	 * 
	 * @return Returns the BlockState containing all properties used by this block.
	 */
	protected BlockState createOurBlockState()
	{
		BlockState state;

		if (hasTopProperty)
		{
			ageProp = PropertyInteger.create("age", 0, maxAge);
			topProp = PropertyBool.create("top");
			state = new BlockState(this, ageProp, topProp);
			setDefaultState(state.getBaseState().withProperty(ageProp, 0).withProperty(topProp, true));
		}
		else
		{
			ageProp = PropertyInteger.create("age", 0, maxAge);
			state = new BlockState(this, ageProp);
			setDefaultState(state.getBaseState().withProperty(ageProp, 0));
		}
		
		ArrayList<IProperty> metaProps = new ArrayList<IProperty>();
		metaProps.addAll(state.getProperties());
		
		if (hasTopProperty)
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
	
	/**
	 * Sets the position in a plant column that the "top" property should be true at.
	 * -1 = Default, the top property will be the top plant block in the column.
	 * 
	 * @return Returns this block.
	 */
	public BlockGrowingPlant setTopPosition(int topPos)
	{
		topBlockPos = topPos;
		
		return this;
	}
	
	/**
	 * Set a property to make all the plant's blocks age stay the same, and age as the first block ages.
	 * 
	 * @return Returns this block.
	 */
	public BlockGrowingPlant setGrowAllTogether(boolean growTogetherIn)
	{
		growTogether = growTogetherIn;
		
		return this;
	}

	/**
	 * Sets the block to use the biome color multiplier like vanilla grass and ferns.
	 * 
	 * @return Returns this block.
	 */
	public BlockGrowingPlant setUseBiomeColor(boolean use)
	{
		useBiomeColor = use;
		
		return this;
	}
	
	@Override
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
	{
		if (useBiomeColor)
		{
			return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
		}
		
		return 16777215;
	}
	
	/**
	 * Sets a property to cause the entire plant column to break at once and drop only the first block's
	 * drops.
	 * false = Default, only the plant blocks above the broken one will break as well.
	 * true = All the plant blocks in the column will break at once and drop only the first block's drops.
	 * 
	 * @return Returns this block.
	 */
	public BlockGrowingPlant setBreakAllTogether(boolean breakTogetherIn)
	{
		breakTogether = breakTogetherIn;
		
		return this;
	}
	
	/**
	 * Sets the EnumPlantType that the plant block should grow on.
	 * 
	 * @return Returns this block.
	 */
	public BlockGrowingPlant setPlantType(EnumPlantType plantTypeIn)
	{
		plantType = plantTypeIn;
		
		return this;
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return plantType;
	}
	
	/**
	 * Cause the plant block to reset its age when they grow taller.
	 * 
	 * @return Returns this block.
	 */
	public BlockGrowingPlant setResetAgeOnGrowth(boolean resetAgeIn)
	{
		resetAge = resetAgeIn;
		
		return this;
	}
	
	/**
	 * Sets the drops when the plant is NOT fully grown.
	 * 
	 * @return Returns this block.
	 */
	public BlockGrowingPlant setDrops(RandomItemDrop... dropsIn)
	{
		drops.clear();
		
		for (RandomItemDrop drop : dropsIn)
		{
			drops.add(drop);
		}
		
		return this;
	}

	/**
	 * Sets the drops when the plant is fully grown.
	 * 
	 * @return Returns this block.
	 */
	public BlockGrowingPlant setCropDrops(RandomItemDrop... dropsIn)
	{
		cropDrops.clear();
		
		for (RandomItemDrop drop : dropsIn)
		{
			cropDrops.add(drop);
		}
		
		return this;
	}

	/**
	 * Sets the Item picked when the user middle clicks on the block in creative mode.
	 * 
	 * @return Returns this block.
	 */
	public BlockGrowingPlant setPickedItem(Item item)
	{
		pickedItem = item;
		
		return this;
	}
	
	@Override
	public Item getItem(World worldIn, BlockPos pos)
	{
		if (pickedItem != null)
		{
			return pickedItem;
		}

		return Item.getItemFromBlock(this);
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
	
	protected void setBlockBounds(AxisAlignedBB bb)
	{
		this.minX = bb.minX;
		this.minY = bb.minY;
		this.minZ = bb.minZ;
		this.maxX = bb.maxX;
		this.maxY = bb.maxY;
		this.maxZ = bb.maxZ;
	}

	@SideOnly(Side.CLIENT)
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	{
		GrowingPlantProperties props = new GrowingPlantProperties(worldIn, pos);
		float w2 = width / 2;
		
		AxisAlignedBB newBB = new AxisAlignedBB(0.5 - w2, 0, 0.5 - w2, 0.5 + w2, 0, 0.5 + w2);
		
		if (props.isTop(pos))
		{
			int stage = (Integer) worldIn.getBlockState(pos).getValue(ageProp);
			
			if (props.getToBottom() > 1)
			{
				stage -= growthAge;
			}
			
			newBB = newBB.addCoord(0, baseHeight + ((stage + 1) * heightPerStage), 0);
		}
		else
		{
			newBB = newBB.addCoord(0, 1, 0);
		}
		
		setBlockBounds(newBB);
	}

	/**
	 * Sets the collision axis aligned bounds of the plant.
	 * 
	 * @param bb The AxisAlignedBB to use for entity collision.
	 * @return
	 */
	public BlockGrowingPlant setCollisionBox(AxisAlignedBB bb)
	{
		collisionBox = bb;
		
		return this;
	}
	
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
    	if (collisionBox != null)
    	{
    		return collisionBox.offset(pos.getX(), pos.getY(), pos.getZ());
    	}
    	
    	return null;
    }

	/**
	 * Sets the drops when the plant is fully grown.
	 * 
	 * @return Returns this block.
	 */
	public BlockGrowingPlant setCustomsInterface(IGrowingPlantCustoms customsIn)
	{
		customs = customsIn;
		
		return this;
	}

	@Override
	public BlockState getBlockState()
	{
		return super.getBlockState();
	}

	@Override
	protected BlockState createBlockState()
	{
		return super.createBlockState();
	}
	
	/**
	 * Returns whether the block at pos should have the top property set to true.
	 * This takes this.topBlockPos into account as well, so it will not always be the highest block in the plant.
	 * 
	 * @param worldIn
	 * @param pos The position to check.
	 * @return Whether the block should have "top" property set true.
	 */
	protected boolean isTop(IBlockAccess worldIn, BlockPos pos)
	{
		boolean top;
		
		// If topBlock position is defined, set it according to that
		if (topBlockPos >= 0)
		{
			int toBottom = new GrowingPlantProperties(worldIn, pos, this).getToBottom();
			top = toBottom == topBlockPos;
		}
		else	// Otherwise, set according to if it really is top
		{
			top = worldIn.getBlockState(pos.up()).getBlock() != this;
		}
		
		return top;
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
		
		return state;
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
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

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
    	if (hasTopProperty)
    	{
    		state = state.withProperty(topProp, isTop(worldIn, pos));
    	}
    	
        return state;
    }

	protected float baseAgeChance = 12F;
	protected float fertileChanceMult = 0.5F;
	protected float neighborFertileChanceMult = 0.975F;

	/**
	 * Causes the plant to grow each time updateTick() is called.
	 * @return
	 */
	public BlockGrowingPlant setNoGrowthChance()
	{
		baseAgeChance = 0;
		fertileChanceMult = 0;
		neighborFertileChanceMult = 0;

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
	public BlockGrowingPlant setGrowthChanceMult(float base, float fertileMult, float neighborFertileMult)
	{
		baseAgeChance = base;
		fertileChanceMult = fertileMult;
		neighborFertileChanceMult = neighborFertileMult;

		return this;
	}

	/**
	 * Gets the chance that a random tick of a crop block will result in the crop aging. 1 is added to the output to determine the actual chance.
	 */
	public float getGrowthChance(World worldIn, BlockPos pos, IBlockState state)
	{
		// If the base age is greater than 0, then we must calculate the chance based on surrounding blocks
		if (baseAgeChance > 0)
		{
			// Default base chance is <= 1 in 7.5
			float rate = baseAgeChance;

			BlockPos under = pos.down();
			IBlockState blockUnder = worldIn.getBlockState(under);
			
			if (fertileChanceMult != 0)
			{
				boolean fertile = blockUnder.getBlock().isFertile(worldIn, under);
	
				// If the land under the crop is fertile, chance will be <= 1 in 4.25 by default
				if (fertile)
				{
					rate *= fertileChanceMult;
				}
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
							IBlockState landState = worldIn.getBlockState(landPos);
							Block landBlock = landState.getBlock();
	
							if (landBlock.canSustainPlant(worldIn, landPos, EnumFacing.UP, (IPlantable) state.getBlock()) && landBlock.isFertile(worldIn, landPos))
							{
								rate *= neighborFertileChanceMult;
							}
						}
					}
				}
			}

			return rate;
		}

		return baseAgeChance;
	}
	
	/**
	 * Calls the call() method in the IPerBlockCall on each block in the plant column, starting with fromBlock.
	 * 
	 * @param worldIn
	 * @param fromBlock The BlockPos to start from.
	 * @param call The IPerBlockCall whose call() method is called.
	 * @param args An array of arguments to send to call().
	 */
	protected void callForEachInColumn(World worldIn, BlockPos fromBlock, IPerBlockCall call, Object... args)
	{
		GrowingPlantProperties props = new GrowingPlantProperties(worldIn, fromBlock);
		
		BlockPos top = props.getTop();
		int height = props.getHeight();
		
		for (int y = 0; y < height; y++)
		{
			BlockPos curPos = top.down(y);
			
			call.call(worldIn, curPos, worldIn.getBlockState(curPos), fromBlock, args);
		}
	}

	protected float minGrowthLight = 9;
	
	/**
	 * Attempts to grow the plant according to its rules of growth. Will use getGrowthChance unless an override chance is given.
	 * 
	 * @param worldIn The world.
	 * @param rand The random number generator used to determine if the plant actually grows.
	 * @param pos
	 * @param state
	 * @param forceGrow Causes the plant to ignore its growth chance, and grows 100% of the time.
	 * @param noChange Cancel all changes to the world. This is used to determine if a plant can grow.
	 * @return Whether the plant was changed.
	 */
	public boolean grow(World worldIn, Random rand, BlockPos pos, IBlockState state, boolean forceGrow, boolean noChange)
	{
		boolean changed = false;
		
		GrowingPlantProperties props = new GrowingPlantProperties(worldIn, pos, (BlockGrowingPlant) state.getBlock());
		
		if (!growTogether || (growTogether && props.isBottom(pos)))
		{
			int oldAge = (Integer) state.getValue(ageProp);
			int age = oldAge;
	
			// Age the plant (using the chance generated from getGrowthChance)
			if (forceGrow || worldIn.getLightFromNeighbors(pos) >= minGrowthLight)
			{
				float chance = 0;
				float randVal = 0;
				
				if (!forceGrow)
				{
					chance = getGrowthChance(worldIn, pos, state);
					randVal = rand.nextFloat() * (chance + 1);
				}
				
				if (chance == 0 || (chance > 0 && (randVal <= 1)))
				{
					age++;
					
					// Add to the height of the plant if this is the top block and has aged fully
					if (props.getHeight() < maxHeight && age >= growthAge)
					{
						BlockPos above = pos.up();
						IBlockState aboveState = worldIn.getBlockState(above);
						Block aboveBlock = aboveState.getBlock();
						
						if (aboveBlock != this && aboveBlock.isReplaceable(worldIn, above))
						{
							changed = true;
							
							if (!noChange)
							{
								worldIn.setBlockState(above, getDefaultState());
							}
						}
					}
				}
			}
			
			age = MathHelper.clamp_int(age, 0, maxAge);
			changed |= age != oldAge;

			if (!noChange)
			{
				if (growTogether)
				{
					callForEachInColumn(worldIn, pos, new IPerBlockCall() {
							public void call(World worldIn, BlockPos curPos, IBlockState curState, BlockPos startPos, Object... args) {
								worldIn.setBlockState(curPos, curState.withProperty(ageProp, (Integer) args[0]));
							}
						}, age);
				}
				else
				{
					worldIn.setBlockState(pos, state.withProperty(ageProp, age), 2);
				}
			}
		}
		
		return changed;
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		BlockPos growOn = pos;
		
		if (growTogether)
		{
			growOn = new GrowingPlantProperties(worldIn, pos).getBottom();
		}
		else
		{
			growOn = pos;
		}
		
		return grow(worldIn, worldIn.rand, growOn, worldIn.getBlockState(growOn), true, true);
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		return true;
	}

	/**
	 * Called by bonemeal to grow the plant.
	 */
	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			BlockPos growOn = pos;
			
			if (growTogether)
			{
				growOn = new GrowingPlantProperties(worldIn, pos).getBottom();
			}
			else
			{
				growOn = pos;
			}
			
			int growth = MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
			
			for (int i = 0; i < growth; i++)
			{
				grow(worldIn, rand, growOn, worldIn.getBlockState(growOn), true, false);
			}
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		checkAndDropBlock(worldIn, pos, state);
		
		state = worldIn.getBlockState(pos);
		
		// If the plant was not broken by checkAndDropBlock(), attempt to grow it.
		if (state.getBlock() == this)
		{
			boolean grew = grow(worldIn, rand, pos, state, false, false);
			
			if (customs != null)
			{
				customs.plantUpdateTick(this, worldIn, pos, state, rand, grew);
			}
		}
	}
	
	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		boolean heightOK = new GrowingPlantProperties(worldIn, pos).getHeight() <= maxHeight;

		BlockPos below = pos.down();
		IBlockState stateBelow = worldIn.getBlockState(below);
		Block blockBelow = stateBelow.getBlock();
		
		/*boolean correctPlant = false;
		
		if (blockUnder == this)
		{
			if (!resetAge && (Integer) stateUnder.getValue(ageProp) >= growthAge)
			{
				correctPlant = true;
			}
		}*/
		
		boolean correctLand = false;
		CanStayOptions stay = CanStayOptions.YIELD;
		
		if (customs != null)
		{
			stay = customs.canPlantStayAt(this, worldIn, pos, true);
		}
		
		switch(stay)
		{
		case YES:
			correctLand = true;
			break;
		case NO:
			correctLand = false;
			break;
		case YIELD:
			correctLand = blockBelow.canSustainPlant(worldIn, below, EnumFacing.UP, this);
			
			if (blockBelow == this)
			{
				GrowingPlantProperties props = new GrowingPlantProperties(worldIn, pos);
				int height = props.getToBottom();
				
				if (height <= maxHeight && (resetAge || growthAge == maxAge))
				{
					correctLand = true;
				}
			}
			
			break;
		}
		
		return heightOK && correctLand;
	}

	@Override
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!canBlockStay(worldIn, pos, state))
		{
			destroyPlant(worldIn, pos, null, true, true);
		}
	}
	
	@Override
	protected boolean canPlaceBlockOn(Block ground)
	{
		// Return false so that canSustainPlant checks the plant type instead of BlockBush.canPlaceBlockOn().
		return false;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		CanStayOptions stay = CanStayOptions.YIELD;
		
		if (customs != null)
		{
			stay = customs.canPlantStayAt(this, worldIn, pos, false);
		}
		
		switch(stay)
		{
		case YES:
			return true;
		case NO:
			break;
		case YIELD:
			boolean canPlace = super.canPlaceBlockAt(worldIn, pos);
			
			BlockPos below = pos.down();
			IBlockState stateBelow = worldIn.getBlockState(below);
			
			if (stateBelow.getBlock() == this)
			{
				GrowingPlantProperties props = new GrowingPlantProperties(worldIn, below);
				int height = props.getToBottom();
				
				if (height < maxHeight && (resetAge || growthAge == maxAge))
				{
					canPlace = true;
				}
			}
			
			return canPlace;
		}
		
		return false;
	}
	
	/**
	 * Handles removal of a plant block.
	 * If breakTogether is true, it removes all plant blocks of this type in a column that they should grow in, optionally dropping items.
	 * Otherwise, it will handle setting the BlockState to acceptable values for this type of plant.
	 */
	protected void destroyPlant(World worldIn, BlockPos pos, final EntityPlayer breaker, final boolean drop, boolean noBreakTogether)
	{
		final HashMap<BlockPos, IBlockState> oldStates = new HashMap<BlockPos, IBlockState>();
		
		if (breakTogether && !noBreakTogether)
		{
			callForEachInColumn(worldIn, pos, new IPerBlockCall() {
				public void call(World worldIn, BlockPos curPos, IBlockState curState, BlockPos startPos, Object... args) {
					oldStates.put(curPos, getActualState(worldIn.getBlockState(curPos), worldIn, curPos));
				}
			});
		}
		else
		{
			int up = 0;
			Block remBlock = null;
			
			while (true)
			{
				BlockPos remPos = pos.up(up);
				
				IBlockState remState = worldIn.getBlockState(remPos);
				remBlock = remState.getBlock();

				if (remBlock == this)
				{
					remState = getActualState(remState, worldIn, remPos);
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
			IBlockState oldState = entry.getValue();
			
			if (!particlePos.equals(pos) && worldIn.isRemote)
			{
				worldIn.playAuxSFXAtEntity(breaker, 2001, particlePos, Block.getStateId(oldState));
			}
		}
		
		// Set blocks to air without notifying neighbors of the change (so items aren't dropped when they shouldn't).
		// Also drop items.
		if (!worldIn.isRemote)
		{
			GrowingPlantProperties props = new GrowingPlantProperties(worldIn, pos);
			
			for (Entry<BlockPos, IBlockState> entry : oldStates.entrySet())
			{
				BlockPos breakPos = entry.getKey();
				IBlockState oldState = entry.getValue();
				
				if (drop)
				{
					boolean isBase = props.isBottom(breakPos);
					dropBlockAsItemWithChance(worldIn, breakPos, oldState, (isBase ? -1 : -2), 0);
				}
	
				worldIn.setBlockState(breakPos, Blocks.air.getDefaultState(), 2);
			}
			
			// Notify of block changes.
			for (Entry<BlockPos, IBlockState> entry : oldStates.entrySet())
			{
				BlockPos notifyPos = entry.getKey();
				IBlockState oldState = entry.getValue();
				
				worldIn.markAndNotifyBlock(notifyPos, worldIn.getChunkFromBlockCoords(notifyPos), oldState, worldIn.getBlockState(notifyPos), 1);
			}
			
			if (growTogether && !breakTogether)
			{
				BlockPos under = pos.down();
				Block blockUnder = worldIn.getBlockState(under).getBlock();
				
				if (blockUnder == this)
				{
					callForEachInColumn(worldIn, under, new IPerBlockCall() {
						public void call(World worldIn, BlockPos curPos, IBlockState curState, BlockPos startPos, Object... args) {
							worldIn.setBlockState(curPos, curState.withProperty(ageProp, growthAge - 1));
						}
					});
				}
			}
		}
	}

	/**
	 * Drops ItemStacks in EntityItems according to the plant's properties for dropping items.
	 * 
	 * @param worldIn
	 * @param pos
	 * @param state
	 * @param chance With a value less than 0, the block will be dropped.
	 * Otherwise, it will destroy the plant as it should normally, but only if a player is not breaking it.
	 * @param fortune
	 */
	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
	{
		if (!worldIn.isRemote)
		{
			if (chance <= -1)	// Prevent the block dropping without our code telling it to explicitly
			{
				ArrayList<ItemStack> dropStacks = null;
				
				if (customs != null)
				{
					dropStacks = customs.getPlantDrops(this, worldIn, pos, state, fortune, chance == -1);
				}
				
				if (dropStacks == null || dropStacks.isEmpty())
				{
					if (!breakTogether || (breakTogether && chance == -1))
					{
						if ((Integer) state.getValue(ageProp) >= maxAge)
						{
							for (RandomItemDrop drop : cropDrops)
							{
								ItemStack stack = drop.getRandom(worldIn.rand);
								spawnAsEntity(worldIn, pos, stack);
							}
						}
						else
						{
							for (RandomItemDrop drop : drops)
							{
								ItemStack stack = drop.getRandom(worldIn.rand);
								spawnAsEntity(worldIn, pos, stack);
							}
						}
					}
				}
				else
				{
					for (ItemStack stack : dropStacks)
					{
						spawnAsEntity(worldIn, pos, stack);
					}
				}
			}
			else if (harvesters.get() == null)
			{
				// If the game tries to drop this block, handle it with destroyPlant()
				// This handles explosions and drops due to the block being unable to stay.
				destroyPlant(worldIn, pos, null, worldIn.rand.nextFloat() <= chance, false);
			}
		}
	}
	
	@Override
	public void onBlockExploded(World worldIn, BlockPos pos, Explosion explosion)
	{
		// Do not allow the explosions to directly destroy the block, instead let dropBlockAsItemWithChance() handle destruction.
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
	{
		super.harvestBlock(worldIn, player, pos, state, te);
	}
	
	@Override
	public boolean removedByPlayer(World worldIn, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		destroyPlant(worldIn, pos, player, !player.capabilities.isCreativeMode, false);
		
		return true;
	}
}
