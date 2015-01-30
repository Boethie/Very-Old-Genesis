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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockGrowingPlant extends BlockBush
{
	protected interface IPerBlockCall
	{
		void call(World worldIn, BlockPos curPos, IBlockState curState, BlockPos startPos, Object... args);
	}
	
	protected BlockState ourBlockState;
	protected PropertyInteger AGE;
	protected PropertyBool TOP;

	protected final boolean topProperty;

	protected int growthRate;
	protected int growthAge;
	protected int maxAge;
	protected boolean growTogether = false;
	protected boolean breakTogether = false;
	protected boolean resetAge = false;

	protected ArrayList<RandomItemDrop> drops = new ArrayList<RandomItemDrop>();
	protected ArrayList<RandomItemDrop> cropDrops = new ArrayList<RandomItemDrop>();
	
	EnumPlantType plantType = EnumPlantType.Plains;

	protected int maxHeight;
	protected int topBlockPos = -1;
	
	protected ArrayList<Block> growsOn = null;

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
	
	/*
	 * Sets the position in a plant column that the "top" property should be true at.
	 * -1 = Default, the top property will be the top plant block in the column.
	 */
	public BlockGrowingPlant setTopPosition(int topPos)
	{
		topBlockPos = topPos;
		
		if (topProperty && topPos > 1)
		{
			setDefaultState(getDefaultState().withProperty(TOP, false));
		}
		
		return this;
	}
	
	/*
	 * Set a property to make all the plant's blocks age stay the same, and age as the first block ages.
	 */
	public BlockGrowingPlant setGrowAllTogether(boolean growTogetherIn)
	{
		growTogether = growTogetherIn;
		
		return this;
	}
	
	/*
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
	
	
	/*
	 * Sets the EnumPlantType that the plant block should grow on.
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
	
	/*
	 * Set the Blocks that the plant can grow on. This is an alternative to EnumPlantType.
	 * Overrides EnumPlantType plantType, use sparingly/never.
	 * TODO: May not function correctly.
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
	
	/*
	 * Cause the plant block to reset their age when they grow taller.
	 */
	public BlockGrowingPlant setResetAgeOnGrowth(boolean resetAgeIn)
	{
		resetAge = resetAgeIn;
		
		return this;
	}
	
	/*
	 * Sets the drops 
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
	
    public BlockGrowingPlant setCropDrops(RandomItemDrop... dropsIn)
    {
    	cropDrops.clear();
    	
    	for (RandomItemDrop drop : dropsIn)
    	{
    		cropDrops.add(drop);
    	}
    	
    	return this;
    }

	protected BlockState createOurBlockState()
	{
		BlockState state;

		if (topProperty)
		{
			AGE = PropertyInteger.create("age", 0, maxAge);
			TOP = PropertyBool.create("top");
			state = new BlockState(this, AGE, TOP);
			setDefaultState(state.getBaseState().withProperty(AGE, 0).withProperty(TOP, true));
		}
		else
		{
			AGE = PropertyInteger.create("age", 0, maxAge);
			state = new BlockState(this, AGE);
			setDefaultState(state.getBaseState().withProperty(AGE, 0));
		}

		return state;
	}

	public BlockState getBlockState()
	{
		return ourBlockState;
	}

	protected BlockState createBlockState()
	{
		return super.createBlockState();
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state);
	}
	
	public int getPosInPlant(IBlockAccess worldIn, BlockPos pos)
	{
		int inPlantPos;

		for (inPlantPos = 1; worldIn.getBlockState(pos.down(inPlantPos)).getBlock() == this; inPlantPos++)
		{
		}
		
		return inPlantPos;
	}
	
	public int getHeight(IBlockAccess worldIn, BlockPos pos)
	{
		int posInPlant = getPosInPlant(worldIn, pos);
		int toTop;

		for (toTop = 1; worldIn.getBlockState(pos.up(toTop)).getBlock() == this; toTop++)
		{
		}
		
		return posInPlant + toTop - 1;
	}
	
	protected boolean isTop(IBlockAccess worldIn, BlockPos pos)
	{
		boolean top;
		
		// If topBlock position is defined, set it according to that
		if (topBlockPos >= 0)
		{
			int height = getPosInPlant(worldIn, pos);
			top = height == topBlockPos;
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
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(TOP, isTop(worldIn, pos));
    }
	
	@Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
		worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(TOP, isTop(worldIn, pos)));
		
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta);
	}

	protected float baseAgeChance = 6.5F;
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
			// Base chance is <= 1 in 7.5
			float rate = baseAgeChance;

			BlockPos under = pos.down();
			IBlockState blockUnder = worldIn.getBlockState(under);
			boolean fertile = blockUnder.getBlock().isFertile(worldIn, under);

			// If the land under the crop is fertile, chance will be <= 1 in 4.25
			if (fertile)
			{
				rate *= fertileChanceMult;
			}

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

			return rate;
		}

		return baseAgeChance;
	}
	
	/* Sets all properties in a column of this plant's blocks. */
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

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
		
		int inPlantPos = getPosInPlant(worldIn, pos);
		int height = getHeight(worldIn, pos);

		if (!growTogether || (growTogether && inPlantPos == 1))
		{
			int age = (Integer) state.getValue(AGE);
	
			// Age the plant (using the chance generated from getGrowthChance)
			if (worldIn.getLightFromNeighbors(pos) >= minGrowthLight)
			{
				float chance = getGrowthChance(worldIn, pos, state);
				float randVal = rand.nextFloat() * (chance + 1);
	
				if (chance > 0 && (randVal <= 1))
				{
					// Add to the height of the plant if this is the top block and has aged fully
					if (height < maxHeight && age >= growthAge)
					{
						BlockPos top = pos.up(height - inPlantPos + 1);
						IBlockState upState = worldIn.getBlockState(top);
						Block upBlock = upState.getBlock();
	
						if (upBlock != this && upBlock.isReplaceable(worldIn, top))
						{
							age++;
							worldIn.setBlockState(top, getDefaultState());
						}
					}
					else if (age < maxAge)
					{
						age++;
					}
				}
			}

			if (growTogether)
			{
				callForEachInColumn(worldIn, pos, new IPerBlockCall() {
						public void call(World worldIn, BlockPos curPos, IBlockState curState, BlockPos startPos, Object... args) {
							worldIn.setBlockState(curPos, curState.withProperty(AGE, (Integer) args[0]));
						}
					}, age);
			}
			else
			{
				worldIn.setBlockState(pos, state.withProperty(AGE, age), 2);
			}
		}
	}
	
	@Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
		int height = getPosInPlant(worldIn, pos);
		boolean heightOK = height <= maxHeight;
		heightOK = true;
		
		boolean correctPlant = false;
		BlockPos under = pos.down();
		IBlockState stateUnder = worldIn.getBlockState(under);
		Block blockUnder = stateUnder.getBlock();
		
		if (blockUnder == this)
		{
			if (!resetAge && (Integer) stateUnder.getValue(AGE) > growthAge)
			{
				correctPlant = true;
			}
		}
		
		boolean correctLand = false;
		
		if (growsOn != null)
		{
			correctLand = growsOn.contains(blockUnder);
		}
		else
		{
			correctLand = blockUnder.canSustainPlant(worldIn, under, EnumFacing.UP, this);
		}
		
		return heightOK && (correctLand || correctPlant);
    }

	@Override
    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!canBlockStay(worldIn, pos, state))
        {
    		destroyAll(worldIn, pos, true);
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
		if (chance == -1)
		{
			if ((Integer) state.getValue(AGE) >= maxAge)
			{
				for (RandomItemDrop drop : cropDrops)
				{
					ItemStack stack = drop.getRandom(worldIn.rand);
	                spawnAsEntity(worldIn, pos, stack);
				}
			}

			for (RandomItemDrop drop : drops)
			{
				ItemStack stack = drop.getRandom(worldIn.rand);
                spawnAsEntity(worldIn, pos, stack);
			}
		}
    }
	
	@Override
    public void onBlockExploded(World worldIn, BlockPos pos, Explosion explosion)
    {
		destroyAll(worldIn, pos, false);
    }
	
	/* Removes all plant blocks of this type in a column that they should grow in, optionally dropping items. */
	protected void destroyAll(World worldIn, BlockPos pos, boolean drop)
	{
		if (!worldIn.isRemote && breakTogether)
		{
			int posInPlant = getPosInPlant(worldIn, pos);
			BlockPos firstPos = pos.down(posInPlant - 1);
			IBlockState firstState = worldIn.getBlockState(firstPos);
			
			if (drop)
			{
				firstState.getBlock().dropBlockAsItemWithChance(worldIn, firstPos, firstState, -1, 0);
			}
			
			final HashMap<BlockPos, IBlockState> oldStates = new HashMap<BlockPos, IBlockState>();
			
			callForEachInColumn(worldIn, pos, new IPerBlockCall() {
				public void call(World worldIn, BlockPos curPos, IBlockState curState, BlockPos startPos, Object... args) {
					oldStates.put(curPos, worldIn.getBlockState(curPos));
					worldIn.setBlockState(curPos, Blocks.air.getDefaultState(), 2);
				}
			}, this);
			
			int i = 0;
			
			for (Entry<BlockPos, IBlockState> entry : oldStates.entrySet())
			{
				BlockPos notifyPos = entry.getKey();
				IBlockState oldState = entry.getValue();
				worldIn.markAndNotifyBlock(notifyPos, worldIn.getChunkFromBlockCoords(notifyPos), oldState, worldIn.getBlockState(notifyPos), 1);
			}
		}
	}
	
	@Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
		super.onBlockHarvested(worldIn, pos, state, player);
    }
    
	@Override
    public boolean removedByPlayer(World worldIn, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
		destroyAll(worldIn, pos, !player.capabilities.isCreativeMode);
		
		return true;
    }
}
