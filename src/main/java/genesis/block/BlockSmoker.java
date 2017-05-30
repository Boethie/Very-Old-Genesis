package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.util.Constants;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("UnnecessaryUnboxing")
public class BlockSmoker extends BlockGenesis
{

    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);//So the game doesn't crash

    public BlockSmoker()
    {
        super(Material.WATER, SoundType.STONE);
        setCreativeTab(GenesisCreativeTabs.DECORATIONS);
        setUnlocalizedName(Constants.Unlocalized.PREFIX + "smoker");
        setHardness(1.5F);
        setResistance(10.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, Integer.valueOf(0)));
        setTickRandomly(true);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public int tickRate(World worldIn)
    {
        return 300;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return new AxisAlignedBB(0.25F, 0F, 0.25F, 0.75F, 1.0F, 0.75F);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(LEVEL, Integer.valueOf(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(LEVEL)).intValue();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {LEVEL});
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (worldIn.getBlockState(pos.up()).getMaterial().isLiquid() && worldIn.getBlockState(pos.up()).getBlock() != GenesisBlocks.SMOKER)
        {
            Random random = worldIn.rand;

            for (int i = 0; i < 8; ++i)
            {
                double x = pos.getX() + 0.5;
                double y = pos.getY() + i * 0.1;
                double z = pos.getZ() + 0.5;

                worldIn.spawnParticle(random.nextInt(10) == 0 ? EnumParticleTypes.WATER_BUBBLE : EnumParticleTypes.SMOKE_LARGE, x, y, z, 0.0D, 0.1D, 0.0D);
            }
        }
    }

    @Override
    protected boolean canSilkHarvest()
    {
        return true;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }
}
