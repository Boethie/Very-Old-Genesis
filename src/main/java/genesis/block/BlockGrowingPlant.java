package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.util.BlockStateToMetadata;
import genesis.util.RandomItemDrop;

import java.util.ArrayList;
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
		public ArrayList<ItemStack> getDrops(BlockGrowingPlant plant, World worldIn, BlockPos pos, IBlockState state, int fortune);
		
		/**
		 * Called after updateTick in a BlockGrowingPlant.
		 * 
		 * @param worldIn
		 * @param pos
		 * @param state
		 * @param random
		 * @param grew Whether the plant grew in this random block update.
		 */
		public void updateTick(BlockGrowingPlant plant, World worldIn, BlockPos pos, IBlockState state, Random rand, boolean grew);
	}
	
	protected BlockState ourBlockState;
	
	public PropertyInteger ageProp;
	public PropertyBool topProp;

	protected final boolean topProperty;

	public int growthAge;
	public int maxAge;
	public boolean growTogether = false;
	public boolean breakTogether = false;
	public boolean resetAge = false;

	protected ArrayList<RandomItemDrop> drops = new ArrayList<RandomItemDrop>();
	protected ArrayList<RandomItemDrop> cropDrops = new ArrayList<RandomItemDrop>();
	protected Item pickedItem = null;
	
	IGrowingPlantCustoms customs = null;
	
	EnumPlantType plantType = EnumPlantType.Plains;
	protected ArrayList<Block> growsOn = null;

	protected int maxHeight;
	protected int topBlockPos = -1;

	protected float baseHeight = 0.25F;
	protected float heightPerStage = 0F;
	protected float width = 1F;

	public BlockGrowingPlant(boolean topPropertyIn, int maxAgeIn, int growthAgeIn, int height)
	{
		super();

		maxAge = maxAgeIn;
		growthAge = growthAgeIn;

		topProperty = topPropertyIn;
		maxHeight = height;

		ourBlockState = createOurBlockState();

		setTickRandomly(true);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}

	public BlockGrowingPlant(int maxAgeIn, int height)
	{
		this(false, maxAgeIn, maxAgeIn, height);
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
		
		if (topProperty && topPos > 1)
		{
			setDefaultState(getDefaultState().withProperty(topProp, false));
		}
		
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
	
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return plantType;
	}
	
	/**
	 * Set the Blocks that the plant can grow on. This is an alternative to EnumPlantType.
	 * Overrides EnumPlantType plantType, use sparingly/never.
	 * TODO: May not function correctly.
	 * 
	 * @return Returns this block.
	 */
	public BlockGrowingPlant setGrowsOn(Block... onBlocks)
	{
		growsOn = new ArrayList<Block>();
		
		for (Block onBlock : onBlocks)
		{
			growsOn.add(onBlock);
		}
		
		return this;
	}
	
	/**
	 * Cause the plant block to reset their age when they grow taller.
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
	 * Sets the drops when the plant is fully grown.
	 * 
	 * @return Returns this block.
	 */
	public BlockGrowingPlant setCustomsInterface(IGrowingPlantCustoms customsIn)
	{
		customs = customsIn;
		
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

		if (topProperty)
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

		return state;
	}

	@Override
	public BlockState getBlockState()
	{
		return ourBlockState;
	}

	@Override
	protected BlockState createBlockState()
	{
		return super.createBlockState();
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state);
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
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(topProp, isTop(worldIn, pos));
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(topProp, isTop(worldIn, pos)));
		
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta);
	}

	protected float baseAgeChance = 12F;
	protected float fertileChanceMult = 0.5F;
	protected float neighborFertileChanceMult = 0.975F;

	public BlockGrowingPlant setNoGrowthChance()
	{
		baseAgeChance = 0;
		fertileChanceMult = 0;
		neighborFertileChanceMult = 0;

		return this;
	}

	public BlockGrowingPlant setGrowthChanceMult(float base, float fertileMult, float neighborFertileMult)
	{
		baseAgeChance = base;
		fertileChanceMult = fertileMult;
		neighborFertileChanceMult = neighborFertileMult;

		return this;
	}

	/*
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
		IBlockState onBlockState;
		int i = 0;
		boolean up = true;
		
		while (true)
		{
			BlockPos onPos = fromBlock.up(i);
			onBlockState = worldIn.getBlockState(onPos);
			
			// If we're on this plant type still, set the property.
			if (onBlockState.getBlock() == this)
			{
				call.call(worldIn, onPos, onBlockState, fromBlock, args);
			}
			else if (up)	// If we've run out of this plant in the up direction, switch to going down.
			{
				up = false;
				i = 0;
			}
			else	// If we've run out of this plant going down, break the loop.
			{
				break;
			}
			
			if (up)
			{
				i++;
			}
			else
			{
				i--;
			}
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
					// Add to the height of the plant if this is the top block and has aged fully
					if (props.getHeight() < maxHeight && age >= growthAge)
					{
						BlockPos top = props.getTop().up();
						IBlockState upState = worldIn.getBlockState(top);
						Block upBlock = upState.getBlock();
						
						if (upBlock != this && upBlock.isReplaceable(worldIn, top))
						{
							age++;
							changed = true;
							
							if (!noChange)
							{
								worldIn.setBlockState(top, getDefaultState());
							}
						}
					}
					else
					{
						age++;
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
		BlockPos bottom = new GrowingPlantProperties(worldIn, pos).getBottom();
		
		return grow(worldIn, worldIn.rand, bottom, worldIn.getBlockState(bottom), true, true);
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
		BlockPos bottom = new GrowingPlantProperties(worldIn, pos).getBottom();
		
		for (int i = 0; i < MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5); i++)
		{
			grow(worldIn, rand, bottom, worldIn.getBlockState(bottom), true, false);
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		checkAndDropBlock(worldIn, pos, state);
		
		boolean grew = grow(worldIn, rand, pos, state, false, false);
		
		if (customs != null)
		{
			customs.updateTick(this, worldIn, pos, state, rand, grew);
		}
	}
	
	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		boolean heightOK = new GrowingPlantProperties(worldIn, pos).getHeight() <= maxHeight;
		heightOK = true;
		
		boolean correctPlant = false;
		BlockPos under = pos.down();
		IBlockState stateUnder = worldIn.getBlockState(under);
		Block blockUnder = stateUnder.getBlock();
		
		if (blockUnder == this)
		{
			if (!resetAge && (Integer) stateUnder.getValue(ageProp) > growthAge)
			{
				correctPlant = true;
			}
		}
		
		boolean correctLand = false;
		
		if (canPlaceBlockOn(blockUnder))
		{
			correctLand = true;
		}
		else
		{
			correctLand = blockUnder.canSustainPlant(worldIn, pos, EnumFacing.UP, this);
		}
		
		return heightOK && (correctLand || correctPlant);
	}

	@Override
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!canBlockStay(worldIn, pos, state))
		{
			destroyPlant(worldIn, pos, true);
		}
	}
	
	@Override
	protected boolean canPlaceBlockOn(Block ground)
	{
		boolean correctLand = false;
		
		if (growsOn != null)
		{
			correctLand = growsOn.contains(ground);
		}
		
		// Return false unless we override the EnumPlantType, so that canSustainPlant checks the plant type instead.
		return correctLand;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return super.canPlaceBlockAt(worldIn, pos);
	}

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
	{
		// Prevent the block dropping without out code telling it, to prevent blocks other than the bottom one dropping.
		if (!worldIn.isRemote)
		{
			if (chance == -1)
			{
				ArrayList<ItemStack> dropStacks = null;
				
				if (customs != null)
				{
					dropStacks = customs.getDrops(this, worldIn, pos, state, fortune);
				}
				
				if (dropStacks == null || dropStacks.isEmpty())
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
				else
				{
					for (ItemStack stack : dropStacks)
					{
						spawnAsEntity(worldIn, pos, stack);
					}
				}
			}
			else if (harvesters.get() == null)	// If the game tries to drop this block, handle it with destroyPlant()
			{
				destroyPlant(worldIn, pos, true);
			}
		}
	}
	
	@Override
	public void onBlockExploded(World worldIn, BlockPos pos, Explosion explosion)
	{
		destroyPlant(worldIn, pos, false);
	}
	
	/**
	 * Handles removal of a plant block.
	 * If breakTogether is true, it removes all plant blocks of this type in a column that they should grow in, optionally dropping items.
	 * Otherwise, it will handle setting the BlockState to acceptable values for this type of plant.
	 */
	protected void destroyPlant(World worldIn, BlockPos pos, boolean drop)
	{
		if (!worldIn.isRemote)
		{
			final HashMap<BlockPos, IBlockState> oldStates = new HashMap<BlockPos, IBlockState>();
			
			if (breakTogether)
			{
				BlockPos firstPos = new GrowingPlantProperties(worldIn, pos).getBottom();
				IBlockState firstState = worldIn.getBlockState(firstPos);
				
				if (drop)
				{
					dropBlockAsItemWithChance(worldIn, firstPos, firstState, -1, 0);
				}
				
				callForEachInColumn(worldIn, pos, new IPerBlockCall() {
					public void call(World worldIn, BlockPos curPos, IBlockState curState, BlockPos startPos, Object... args) {
						oldStates.put(curPos, worldIn.getBlockState(curPos));
						worldIn.setBlockState(curPos, Blocks.air.getDefaultState(), 2);
					}
				});
			}
			else
			{
				int up = 0;
				Block remBlock = null;
				
				do
				{
					BlockPos remPos = pos.up(up);
					IBlockState remState = worldIn.getBlockState(remPos);
					remBlock = remState.getBlock();

					if (remBlock == this)
					{
						worldIn.setBlockState(remPos, Blocks.air.getDefaultState(), 2);
						oldStates.put(remPos, remState);
						
						if (drop)
						{
							dropBlockAsItemWithChance(worldIn, remPos, remState, -1, 0);
						}
					}
					
					up++;
				} while (remBlock == this);
				
				int down = 1;
				
				do
				{
					BlockPos remPos = pos.down(down);
					IBlockState remState = worldIn.getBlockState(remPos);
					remBlock = remState.getBlock();
					
					if (remBlock == this)
					{
						worldIn.setBlockState(remPos, remState.withProperty(ageProp, growthAge));
					}
					
					down++;
				} while (remBlock == this);
			}
			
			for (Entry<BlockPos, IBlockState> entry : oldStates.entrySet())
			{
				BlockPos notifyPos = entry.getKey();
				IBlockState oldState = entry.getValue();
				worldIn.markAndNotifyBlock(notifyPos, worldIn.getChunkFromBlockCoords(notifyPos), oldState, worldIn.getBlockState(notifyPos), 1);
				
				// Spawn breaking particles for blocks that weren't directly broken by the player
				if (!notifyPos.equals(pos))
				{
					worldIn.playAuxSFX(2001, notifyPos, Block.getIdFromBlock(this));
				}
			}
		}
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
	{
		
	}
	
	@Override
	public boolean removedByPlayer(World worldIn, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		destroyPlant(worldIn, pos, !player.capabilities.isCreativeMode);
		
		return true;
	}
}
