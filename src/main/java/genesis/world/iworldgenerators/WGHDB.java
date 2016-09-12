package genesis.world.iworldgenerators;

import genesis.block.tileentity.BlockRack;
import genesis.block.tileentity.BlockStorageBox;
import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.BlockStairs.EnumShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class WGHDB {

	static IBlockState nullRow[] = {null , null , null, null, null, null, null, null};
	
	//Materials
	static IBlockState base = GenesisBlocks.TREES.getBlockState(TreeBlocksAndItems.LOG, EnumTree.LEPIDODENDRON);
			
	static IBlockState baseZ = base.withProperty(BlockLog.LOG_AXIS, EnumAxis.Z);
			
	static IBlockState baseY = base.withProperty(BlockLog.LOG_AXIS, EnumAxis.Y);
			
	static IBlockState wattle = GenesisBlocks.TREES.getBlockState(TreeBlocksAndItems.WATTLE_FENCE, EnumTree.LEPIDODENDRON);
			
	static IBlockState roof = GenesisBlocks.CALAMITES_ROOF.getDefaultState();
			
	//0-West Bottom, 1 - East Top, 2 - EastBottom, 3 - WestTop
	static IBlockState[] rf = new IBlockState[]{roof.withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT), roof.withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, EnumHalf.TOP).withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT), roof.withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT), roof.withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, EnumHalf.TOP).withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT)};
			
	static IBlockState bundle = GenesisBlocks.CALAMITES_BUNDLE.getDefaultState();
	
	static IBlockState rack = GenesisBlocks.TREES.getBlockState(TreeBlocksAndItems.RACK, EnumTree.SIGILLARIA).withProperty(BlockRack.RACKS.south, true);
	
	//The main building matrix
	
					 					//y x z
	public static final IBlockState MATRIX[][][] = new IBlockState[][][]{
	{
		nullRow,
		{null, wattle, null, null, null, null, wattle, null},
		nullRow,
		nullRow,
		{null, wattle, null, baseY, null, null, wattle, null},
		nullRow
	},
	{
		nullRow,
		{null, baseZ, baseZ, baseZ, baseZ, baseZ, baseZ, null},
		{null, base, baseZ, baseZ, baseZ, baseZ, base, null},
		{null, base, baseZ, baseZ, baseZ, baseZ, base, null},
		{null, baseZ, baseZ, baseZ, baseZ, baseZ, baseZ, null},
		nullRow
	},
	{
		nullRow,
		{null, baseY, bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), baseY, null},
		{null, bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), GenesisBlocks.STORAGE_BOX.getDefaultState().withProperty(BlockStorageBox.FACING, EnumFacing.EAST),null, null, null, bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), null},
		{null, bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), null,null,null, GenesisBlocks.WORKBENCH.getDefaultState(), bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), null},
		{null, baseY, baseY, null, baseY, bundle.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), baseY, null},
		nullRow
	},
	{
		nullRow,
		{null, baseY, wattle, wattle, wattle, wattle, baseY, null},
		{null, wattle, null, null, null, null, wattle, null},
		{null, wattle, null, null, null, null, wattle, null},
		{null, baseY, baseY, null, baseY, wattle, baseY, null},
		nullRow
	},
	{
		{rf[2],rf[2],rf[2],rf[2],rf[2],rf[2],rf[2],rf[2]},
		{rf[3], baseZ, baseZ, baseZ, baseZ, baseZ, baseZ, rf[3]},
		{null, base, null,null,null, rack, base, null},
		{null, base, null,null,null, rack, base, null},
		{rf[1], baseZ, baseZ, baseZ, baseZ, baseZ, baseZ, rf[1]},
		{rf[0],rf[0],rf[0],rf[0],rf[0],rf[0],rf[0],rf[0]}
	},
	{
		nullRow,
		{rf[2],rf[2],rf[2],rf[2],rf[2],rf[2],rf[2],rf[2]},
		{rf[3],wattle, null,null,null,null, wattle, rf[3]},
		{rf[1],wattle, null,null,null,null, wattle, rf[1]},
		{rf[0],rf[0],rf[0],rf[0],rf[0],rf[0],rf[0],rf[0]},
		nullRow
	},
	{
		nullRow,
		nullRow,
		{rf[2],rf[2],rf[2],rf[2],rf[2],rf[2],rf[2],rf[2]},
		{rf[0],rf[0],rf[0],rf[0],rf[0],rf[0],rf[0],rf[0]},
		nullRow,
		nullRow
	}

	};
}
