package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemBlockMulti;
import genesis.metadata.EnumTree;
import genesis.metadata.PropertyIMetadata;
import genesis.metadata.TreeBlocksAndItems;
import genesis.metadata.VariantsOfTypesCombo.BlockProperties;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.BlockStateToMetadata;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;
import genesis.world.gen.feature.WorldGenTreeArchaeopteris;
import genesis.world.gen.feature.WorldGenTreeCordaites;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSapling;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BlockGenesisSaplings extends BlockSapling
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty[] getProperties()
	{
		return new IProperty[]{ STAGE };
	}
	
	public final TreeBlocksAndItems owner;
	public final ObjectType<BlockGenesisSaplings, ItemBlockMulti> type;
	
	public final List<EnumTree> variants;
	public final PropertyIMetadata<EnumTree> variantProp;
	
	public BlockGenesisSaplings(List<EnumTree> variants, TreeBlocksAndItems owner, ObjectType<BlockGenesisSaplings, ItemBlockMulti> type)
	{
		super();
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		variantProp = new PropertyIMetadata<EnumTree>("variant", variants);
		
		blockState = new BlockState(this, variantProp, STAGE);
		setDefaultState(getBlockState().getBaseState());
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setStepSound(soundTypeGrass);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return BlockStateToMetadata.getBlockStateFromMeta(getDefaultState(), meta);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return BlockStateToMetadata.getMetaForBlockState(state);
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		owner.fillSubItems(type, variants, list);
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = getDefaultState();
		state = state.withProperty(variantProp, (Comparable) owner.getVariant(this, meta));
		return state;
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getItemMetadata(type, (EnumTree) state.getValue(variantProp));
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos)
	{
		return owner.getStack(type, owner.getVariant(world.getBlockState(pos)));
	}
	
	@Override
	public void generateTree(World world, BlockPos pos, IBlockState state, Random rand)
	{
		WorldGenAbstractTree gen = null;
		BlockPos[] positions = {pos};
		
		switch (owner.getVariant(state))
		{
		case ARCHAEOPTERIS:
			gen = new WorldGenTreeArchaeopteris(15, 20, true);
			break;
		case SIGILLARIA:
			gen = new WorldGenTreeSigillaria(10, 15, true);
			break;
		case LEPIDODENDRON:
			gen = new WorldGenTreeLepidodendron(14, 18, true);
			break;
		case CORDAITES:
			gen = new WorldGenTreeCordaites(15, 20, true);
			break;
		case PSARONIUS:
			gen = new WorldGenTreePsaronius(5, 8, true);
			break;
		case ARAUCARIOXYLON:
			gen = new WorldGenTreeAraucarioxylon(25, 30, true).setGenerateRandomSaplings(false);
			break;
		}
		
		if (gen != null)
		{
			IBlockState[] states = new IBlockState[positions.length];
			int i = 0;
			
			for (BlockPos sapPos : positions)
			{
				states[i] = world.getBlockState(sapPos);
				world.setBlockToAir(sapPos);
				i++;
			}
			
			if (!gen.generate(world, rand, pos))
			{
				i = 0;
				
				for (BlockPos sapPos : positions)
				{
					world.setBlockState(sapPos, states[i]);
					i++;
				}
			}
		}
	}
}
