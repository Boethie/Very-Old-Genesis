/**
 * Created by Fatalitiii on 05/06/2016.
 */

package genesis.world.biome.decorate;

import genesis.util.functional.WorldBlockMatcher;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Predicate;

public class WorldGenSplash extends WorldGenDecorationBase
{

    protected final IBlockState parentBlock;
    protected final IBlockState subBlock;

    protected int radius;
    protected double probability;

    /**
     *
     * @param replacePredicate
     * @param parentBlock
     * @param subBlock
     */
    public WorldGenSplash(Predicate<IBlockState> replacePredicate, IBlockState parentBlock, IBlockState subBlock)
    {
        super(WorldBlockMatcher.STANDARD_AIR_WATER, WorldBlockMatcher.state(replacePredicate));

        this.parentBlock = parentBlock;
        this.subBlock = subBlock;

        setRadius(3);
        setProbability(1);
        setPatchRadius(3);
        setPatchCount(64);
    }

    @Override
    public boolean place(World world, Random rand, BlockPos pos)
    {

        Random rand2 = new Random();
        double prob = rand2.nextDouble();

        //WATER CHECK
        if(findBlockInRange(world,pos.down(), Blocks.water,this.radius,this.radius,this.radius))
            return false;

        //PARENT BELOW CHECK
        if(findBlockInRange(world,pos.down(), parentBlock,0,1,0) && prob <= probability)
        {
            prob = rand2.nextDouble();
            //PLACE
            setBlock(world, pos.down(), subBlock);
            return true;
        }else{
            prob = rand2.nextDouble();
            double prob2 = rand2.nextDouble();
            //PLACE
            if(prob <= prob2)
            {
                setBlock(world, pos.down(), subBlock);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param radius set radius from water to stop spawning.
     * @return
     */
    public WorldGenSplash setRadius(int radius)
    {
        this.radius = radius;
        return this;
    }

    /**
     *
     * @param probability set probability of how probable the block will spawn on parent block. Remaining will be on random blocks in the biome.
     * @return
     */
    public WorldGenSplash setProbability(double probability)
    {
        if(probability > 1D)
        {
            Random rand = new Random();
            this.probability = rand.nextDouble();
        }else {
            this.probability = probability;
        }
        return this;
    }
}
