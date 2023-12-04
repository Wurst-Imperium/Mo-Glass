/*
 * Copyright (c) 2019-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.Stainable;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public final class StainedGlassSlabBlock extends SlabBlock implements Stainable
{
	private final DyeColor color;
	
	public StainedGlassSlabBlock(DyeColor color, Settings block$Settings_1)
	{
		super(block$Settings_1);
		this.color = color;
	}
	
	/**
	 * isSideInvisible() seems to be deprecated for no good reason. It is still
	 * used in
	 * {@link Block#shouldDrawSide(BlockState, BlockView, BlockPos, Direction, BlockPos)}
	 * and there is no replacement.
	 */
	@SuppressWarnings("deprecation")
	@Override
	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState blockState_1,
		BlockState blockState_2, Direction direction_1)
	{
		Block block_2 = blockState_2.getBlock();
		
		if(block_2 instanceof StainedGlassBlock
			&& ((StainedGlassBlock)block_2).getColor() == color)
			return true;
		
		if(block_2 == this)
			if(isInvisibleToGlassSlab(blockState_1, blockState_2, direction_1))
				return true;
			
		if(block_2 instanceof StainedGlassStairsBlock
			&& ((StainedGlassStairsBlock)block_2).getColor() == color)
			if(isInvisibleToGlassStairs(blockState_1, blockState_2,
				direction_1))
				return true;
			
		return super.isSideInvisible(blockState_1, blockState_2, direction_1);
	}
	
	private boolean isInvisibleToGlassSlab(BlockState blockState_1,
		BlockState blockState_2, Direction direction_1)
	{
		SlabType type1 = blockState_1.get(SlabBlock.TYPE);
		SlabType type2 = blockState_2.get(SlabBlock.TYPE);
		
		if(type2 == SlabType.DOUBLE)
			return true;
		
		switch(direction_1)
		{
			case UP:
			case DOWN:
			if(type1 != type2)
				return true;
			break;
			
			case NORTH:
			case EAST:
			case SOUTH:
			case WEST:
			if(type1 == type2)
				return true;
			break;
		}
		
		return false;
	}
	
	private boolean isInvisibleToGlassStairs(BlockState blockState_1,
		BlockState blockState_2, Direction direction_1)
	{
		SlabType type1 = blockState_1.get(SlabBlock.TYPE);
		BlockHalf half2 = blockState_2.get(StairsBlock.HALF);
		Direction facing2 = blockState_2.get(StairsBlock.FACING);
		
		// up
		if(direction_1 == Direction.UP)
			if(half2 == BlockHalf.BOTTOM)
				return true;
			
		// down
		if(direction_1 == Direction.DOWN)
			if(half2 == BlockHalf.TOP)
				return true;
			
		// other stairs rear
		if(facing2 == direction_1.getOpposite())
			return true;
		
		// sides
		if(direction_1.getHorizontal() != -1)
		{
			if(type1 == SlabType.BOTTOM && half2 == BlockHalf.BOTTOM)
				return true;
			
			if(type1 == SlabType.TOP && half2 == BlockHalf.TOP)
				return true;
		}
		
		return false;
	}
	
	@Override
	public VoxelShape getCameraCollisionShape(BlockState state, BlockView world,
		BlockPos pos, ShapeContext context)
	{
		return VoxelShapes.empty();
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public float getAmbientOcclusionLightLevel(BlockState blockState_1,
		BlockView blockView_1, BlockPos blockPos_1)
	{
		return 1.0F;
	}
	
	@Override
	public boolean isTransparent(BlockState blockState_1, BlockView blockView_1,
		BlockPos blockPos_1)
	{
		return true;
	}
	
	public boolean isSimpleFullBlock(BlockState blockState_1,
		BlockView blockView_1, BlockPos blockPos_1)
	{
		return false;
	}
	
	@Override
	public DyeColor getColor()
	{
		return color;
	}
}
