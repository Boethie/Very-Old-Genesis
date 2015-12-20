package genesis.block;

import java.util.List;
import java.util.Random;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemBlockMulti;
import genesis.metadata.EnumTree;
import genesis.metadata.PropertyIMetadata;
import genesis.metadata.TreeBlocksAndItems;
import genesis.metadata.VariantsOfTypesCombo.BlockProperties;
import genesis.metadata.VariantsOfTypesCombo.ObjectType;
import genesis.util.BlockStateToMetadata;
import genesis.util.WorldUtils;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;
import genesis.world.gen.feature.WorldGenTreeArchaeopteris;
import genesis.world.gen.feature.WorldGenTreeBjuvia;
import genesis.world.gen.feature.WorldGenTreeCordaites;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreeMetasequoia;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;
import genesis.world.gen.feature.WorldGenTreeVoltzia;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.EnumPlantType;

public class BlockGenesisSaplings extends BlockSapling
{
	/**
	 * Used in BlocksAndItemsWithVariantsOfTypes.
	 */
	@BlockProperties
	public static IProperty<?>[] getProperties()
	{
		return new IProperty[]{ STAGE };
	}
	
	public final TreeBlocksAndItems owner;
	public final ObjectType<BlockGenesisSaplings, ItemBlockMulti<EnumTree>> type;
	
	public final List<EnumTree> variants;
	public final PropertyIMetadata<EnumTree> variantProp;
	
	public BlockGenesisSaplings(TreeBlocksAndItems owner, ObjectType<BlockGenesisSaplings, ItemBlockMulti<EnumTree>> type, List<EnumTree> variants, Class<EnumTree> variantClass)
	{
		super();
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		variantProp = new PropertyIMetadata<EnumTree>("variant", variants, variantClass);
		
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
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		owner.fillSubItems(type, variants, list);
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState state = getDefaultState();
		state = state.withProperty(variantProp, owner.getVariant(this, meta));
		return state;
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return owner.getItemMetadata(type, state.getValue(variantProp));
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
	{
		return owner.getStack(type, world.getBlockState(pos).getValue(variantProp));
	}
	
	@Override
	public void generateTree(World world, BlockPos pos, IBlockState state, Random rand)
	{
		WorldGenAbstractTree gen = null;
		BlockPos[] positions = {pos};
		
		switch (state.getValue(variantProp))
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
		case BJUVIA:
			gen = new WorldGenTreeBjuvia(4, 6, true);
			break;
		case VOLTZIA:
			gen = new WorldGenTreeVoltzia(5, 8, true);
			break;
		case METASEQUOIA:
			int treeType = 0;
			int i = 0;
			int j = 0;
			
			saplingCheck:
			for (i = 0; i >= -1; --i)
			{
				for (j = 0; j >= -1; --j)
				{
					if (checkForSapling(world, pos.add(i, 0, j), EnumTree.METASEQUOIA))
					{
						treeType = 1;
						break saplingCheck;
					}
				}
			}
			
			gen = new WorldGenTreeMetasequoia(25, 30, true).setType(treeType, i, j);
			break;
		default:
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
	
	private boolean checkForSapling(World world, BlockPos pos, EnumTree saplingType)
	{
		return 
				GenesisBlocks.trees.isStateOf(world.getBlockState(pos), TreeBlocksAndItems.SAPLING, EnumTree.METASEQUOIA)
				&& GenesisBlocks.trees.isStateOf(world.getBlockState(pos.add(1, 0, 0)), TreeBlocksAndItems.SAPLING, EnumTree.METASEQUOIA)
				&& GenesisBlocks.trees.isStateOf(world.getBlockState(pos.add(0, 0, 1)), TreeBlocksAndItems.SAPLING, EnumTree.METASEQUOIA)
				&& GenesisBlocks.trees.isStateOf(world.getBlockState(pos.add(1, 0, 1)), TreeBlocksAndItems.SAPLING, EnumTree.METASEQUOIA);
	}
	
	@Override
	public boolean canReplace(World world, BlockPos pos, EnumFacing side, ItemStack stack)
	{
		EnumTree variant = owner.getVariant(stack);
		
		switch (variant)
		{
		case BJUVIA:
			return WorldUtils.canSoilSustainTypes(world, pos, EnumPlantType.Plains, EnumPlantType.Desert);
		default:
			break;
		}
		
		return super.canReplace(world, pos, side, stack);
	}
	
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
	{
		EnumTree variant = owner.getVariant(state);
		
		switch (variant)
		{
		case BJUVIA:
			return WorldUtils.canSoilSustainTypes(world, pos, EnumPlantType.Plains, EnumPlantType.Desert);
		default:
			return super.canBlockStay(world, pos, state);
		}
	}
}
