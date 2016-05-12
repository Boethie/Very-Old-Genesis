package genesis.block;

import java.util.List;
import java.util.Random;

import com.google.common.base.Objects;

import genesis.combo.ObjectType;
import genesis.combo.TreeBlocksAndItems;
import genesis.combo.VariantsOfTypesCombo.BlockProperties;
import genesis.combo.variant.EnumTree;
import genesis.combo.variant.PropertyIMetadata;
import genesis.common.GenesisBlocks;
import genesis.common.GenesisCreativeTabs;
import genesis.item.ItemBlockMulti;
import genesis.util.BlockStateToMetadata;
import genesis.util.WorldUtils;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;
import genesis.world.gen.feature.WorldGenTreeArchaeanthus;
import genesis.world.gen.feature.WorldGenTreeArchaeopteris;
import genesis.world.gen.feature.WorldGenTreeBase;
import genesis.world.gen.feature.WorldGenTreeBase.TreeTypes;
import genesis.world.gen.feature.WorldGenTreeBjuvia;
import genesis.world.gen.feature.WorldGenTreeCordaites;
import genesis.world.gen.feature.WorldGenTreeDryophyllum;
import genesis.world.gen.feature.WorldGenTreeFicus;
import genesis.world.gen.feature.WorldGenTreeGinkgo;
import genesis.world.gen.feature.WorldGenTreeLaurophyllum;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreeMetasequoia;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;
import genesis.world.gen.feature.WorldGenTreeVoltzia;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
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
	public final ObjectType<EnumTree, BlockGenesisSaplings, ItemBlockMulti<EnumTree>> type;
	
	public final List<EnumTree> variants;
	public final PropertyIMetadata<EnumTree> variantProp;
	
	public BlockGenesisSaplings(TreeBlocksAndItems owner,
			ObjectType<EnumTree, BlockGenesisSaplings, ItemBlockMulti<EnumTree>> type,
			List<EnumTree> variants, Class<EnumTree> variantClass)
	{
		super();
		
		this.owner = owner;
		this.type = type;
		
		this.variants = variants;
		variantProp = new PropertyIMetadata<EnumTree>("variant", variants, variantClass);
		
		blockState = new BlockStateContainer(this, variantProp, STAGE);
		setDefaultState(getBlockState().getBaseState());
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setSoundType(SoundType.PLANT);
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
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return owner.getStack(type, world.getBlockState(pos).getValue(variantProp));
	}
	
	@Override
	public void generateTree(World world, BlockPos pos, IBlockState state, Random rand)
	{
		WorldGenTreeBase gen = null;
		BlockPos[] positions = {pos};
		EnumTree variant = state.getValue(variantProp);
		
		TreeTypes treeType;
		int minHeight;
		int maxHeight;
		
		switch (variant)
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
			gen = new WorldGenTreeCordaites(12, 17, true);
			break;
		case PSARONIUS:
			gen = new WorldGenTreePsaronius(5, 8, true);
			break;
		case ARAUCARIOXYLON:
			gen = new WorldGenTreeAraucarioxylon(25, 30, true);
			break;
		case BJUVIA:
			gen = new WorldGenTreeBjuvia(4, 6, true);
			break;
		case VOLTZIA:
			gen = new WorldGenTreeVoltzia(5, 10, true);
			break;
		case METASEQUOIA:
			positions = Objects.firstNonNull(findSaplings(world, pos, variant, 2), positions);
			pos = positions[0];
			treeType = (positions.length > 1)? TreeTypes.TYPE_2 : TreeTypes.TYPE_1;
			minHeight = (treeType == TreeTypes.TYPE_2)? 23 : 20;
			maxHeight = (treeType == TreeTypes.TYPE_2)? 27 : 24;
			gen = new WorldGenTreeMetasequoia(minHeight, maxHeight, true).setType(treeType);
			break;
		case GINKGO:
			treeType = (rand.nextInt(6) == 0)? TreeTypes.TYPE_2 : TreeTypes.TYPE_1;
			minHeight = (treeType == TreeTypes.TYPE_2)? 12 : 8;
			maxHeight = (treeType == TreeTypes.TYPE_2)? 17 : 13;
			gen = new WorldGenTreeGinkgo(minHeight, maxHeight, true).setType(treeType);
			break;
		case FICUS:
			gen = new WorldGenTreeFicus(4, 8, true);
			break;
		case DRYOPHYLLUM:
			treeType = (rand.nextInt(6) == 0)? TreeTypes.TYPE_2 : TreeTypes.TYPE_1;
			minHeight = (treeType == TreeTypes.TYPE_2)? 15 : 11;
			maxHeight = (treeType == TreeTypes.TYPE_2)? 22 : 18;
			gen = new WorldGenTreeDryophyllum(minHeight, maxHeight, true).setType(treeType);
			break;
		case ARCHAEANTHUS:
			gen = new WorldGenTreeArchaeanthus(15, 20, true);
			break;
		case LAUROPHYLLUM:
			gen = new WorldGenTreeLaurophyllum(3, 4, true);
			break;
		default:
			break;
		}
		
		if (gen != null)
		{
			IBlockState[] states = new IBlockState[positions.length];
			
			for (int i = 0; i < positions.length; i++)
			{
				BlockPos sapPos = positions[i];
				states[i] = world.getBlockState(sapPos);
				world.setBlockState(sapPos, Blocks.air.getDefaultState(), 0);
			}
			
			boolean success = gen.generate(world, rand, pos);

			for (int i = 0; i < positions.length; i++)
			{
				BlockPos sapPos = positions[i];
				
				if (success)
					world.markAndNotifyBlock(sapPos, world.getChunkFromBlockCoords(sapPos), states[i], world.getBlockState(sapPos), 3);
				else
					world.setBlockState(sapPos, states[i], 0);
			}
		}
	}
	
	protected BlockPos[] findSaplings(World world, BlockPos pos, EnumTree variant, int area)
	{
		for (BlockPos checkStart : BlockPos.getAllInBox(pos.add(-area + 1, 0, -area + 1), pos))
		{
			BlockPos[] positions = new BlockPos[area * area];
			
			areaCheck:
			for (int x = 0; x < area; x++)
			{
				for (int z = 0; z < area; z++)
				{
					BlockPos check = checkStart.add(x, 0, z);
					
					if (GenesisBlocks.trees.isStateOf(world.getBlockState(check), variant, type))
						positions[z * area + x] = check;
					else
						break areaCheck;
				}
			}
			
			if (positions[positions.length - 1] != null)
				return positions;
		}
		
		return null;
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
