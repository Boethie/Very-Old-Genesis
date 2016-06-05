package genesis.block;

import genesis.combo.variant.EnumRubble;
import genesis.common.GenesisBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockRubbleWall extends BlockGenesisWall
{
	public final IBlockState modelState;
	public final Block modelBlock;

	public BlockRubbleWall(IBlockState modelState)
	{
		super(modelState.getMaterial(), 0.25F, 1.0F, -1);

		this.modelState = modelState;
		modelBlock = modelState.getBlock();

		setSoundType(modelBlock.getSoundType());
	}

	public BlockRubbleWall(EnumRubble variant)
	{
		this(GenesisBlocks.rubble.getBlockState(variant));
	}

	@Override
	public int getHarvestLevel(IBlockState state)
	{
		return modelBlock.getHarvestLevel(modelState);
	}

	@Override
	public String getHarvestTool(IBlockState state)
	{
		return modelBlock.getHarvestTool(modelState);
	}

	@Override
	public float getBlockHardness(IBlockState state, World world, BlockPos pos)
	{
		return modelState.getBlockHardness(world, pos);
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return modelBlock.getExplosionResistance(world, pos, exploder, explosion);
	}
}
