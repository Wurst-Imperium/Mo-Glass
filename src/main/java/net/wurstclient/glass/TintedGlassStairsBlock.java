/*
 * Copyright (c) 2019-2023 Wurst-Imperium and contributors.
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
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.block.enums.StairShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public final class TintedGlassStairsBlock extends StairsBlock
{
	protected TintedGlassStairsBlock(Settings settings)
	{
		super(Blocks.TINTED_GLASS.getDefaultState(), settings);
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
	public boolean isSideInvisible(BlockState state, BlockState stateFrom,
		Direction direction)
	{
		if(stateFrom.getBlock() == Blocks.TINTED_GLASS)
			return true;
		
		if(stateFrom.getBlock() == MoGlassBlocks.TINTED_GLASS_SLAB)
			if(isInvisibleToGlassSlab(state, stateFrom, direction))
				return true;
			
		if(stateFrom.getBlock() == this)
			if(isInvisibleToGlassStairs(state, stateFrom, direction))
				return true;
			
		return super.isSideInvisible(state, stateFrom, direction);
	}
	
	private boolean isInvisibleToGlassSlab(BlockState state,
		BlockState stateFrom, Direction direction)
	{
		BlockHalf half = state.get(StairsBlock.HALF);
		Direction facing = state.get(StairsBlock.FACING);
		StairShape shape = state.get(StairsBlock.SHAPE);
		SlabType typeFrom = stateFrom.get(SlabBlock.TYPE);
		
		if(direction == Direction.UP)
			if(typeFrom != SlabType.TOP)
				return true;
			
		if(direction == Direction.DOWN)
			if(typeFrom != SlabType.BOTTOM)
				return true;
			
		if(typeFrom == SlabType.DOUBLE)
			return true;
		
		// front
		if(direction == facing.getOpposite())
		{
			if(typeFrom == SlabType.BOTTOM && half == BlockHalf.BOTTOM)
				return true;
			
			if(typeFrom == SlabType.TOP && half == BlockHalf.TOP)
				return true;
		}
		
		// right
		if(direction == facing.rotateYClockwise()
			&& shape == StairShape.OUTER_LEFT)
		{
			if(typeFrom == SlabType.BOTTOM && half == BlockHalf.BOTTOM)
				return true;
			
			if(typeFrom == SlabType.TOP && half == BlockHalf.TOP)
				return true;
		}
		
		// left
		if(direction == facing.rotateYCounterclockwise()
			&& shape == StairShape.OUTER_RIGHT)
		{
			if(typeFrom == SlabType.BOTTOM && half == BlockHalf.BOTTOM)
				return true;
			
			if(typeFrom == SlabType.TOP && half == BlockHalf.TOP)
				return true;
		}
		
		return false;
	}
	
	private boolean isInvisibleToGlassStairs(BlockState state,
		BlockState stateFrom, Direction direction)
	{
		BlockHalf half = state.get(StairsBlock.HALF);
		BlockHalf halfFrom = stateFrom.get(StairsBlock.HALF);
		Direction facing = state.get(StairsBlock.FACING);
		Direction facingFrom = stateFrom.get(StairsBlock.FACING);
		StairShape shape = state.get(StairsBlock.SHAPE);
		StairShape shapeFrom = stateFrom.get(StairsBlock.SHAPE);
		
		// up
		if(direction == Direction.UP)
		{
			if(halfFrom == BlockHalf.BOTTOM)
				return true;
			
			if(half != halfFrom)
			{
				if(facing == facingFrom && shape == shapeFrom)
					return true;
				
				switch(shape)
				{
					case STRAIGHT:
					if(shapeFrom == StairShape.INNER_LEFT
						&& (facingFrom == facing
							|| facingFrom == facing.rotateYClockwise()))
						return true;
					if(shapeFrom == StairShape.INNER_RIGHT
						&& (facingFrom == facing
							|| facingFrom == facing.rotateYCounterclockwise()))
						return true;
					break;
					
					case INNER_LEFT:
					if(shapeFrom == StairShape.INNER_RIGHT
						&& facingFrom == facing.rotateYCounterclockwise())
						return true;
					break;
					
					case INNER_RIGHT:
					if(shapeFrom == StairShape.INNER_LEFT
						&& facingFrom == facing.rotateYClockwise())
						return true;
					break;
					
					case OUTER_LEFT:
					if(shapeFrom == StairShape.OUTER_RIGHT
						&& facingFrom == facing.rotateYCounterclockwise())
						return true;
					if(shapeFrom == StairShape.STRAIGHT && (facingFrom == facing
						|| facingFrom == facing.rotateYCounterclockwise()))
						return true;
					break;
					
					case OUTER_RIGHT:
					if(shapeFrom == StairShape.OUTER_LEFT
						&& facingFrom == facing.rotateYClockwise())
						return true;
					if(shapeFrom == StairShape.STRAIGHT && (facingFrom == facing
						|| facingFrom == facing.rotateYClockwise()))
						return true;
					break;
				}
			}
		}
		
		// down
		if(direction == Direction.DOWN)
		{
			if(halfFrom == BlockHalf.TOP)
				return true;
			
			switch(shape)
			{
				case STRAIGHT:
				if(shapeFrom == StairShape.INNER_LEFT && (facingFrom == facing
					|| facingFrom == facing.rotateYClockwise()))
					return true;
				if(shapeFrom == StairShape.INNER_RIGHT && (facingFrom == facing
					|| facingFrom == facing.rotateYCounterclockwise()))
					return true;
				break;
				
				case INNER_LEFT:
				if(shapeFrom == StairShape.INNER_RIGHT
					&& facingFrom == facing.rotateYCounterclockwise())
					return true;
				break;
				
				case INNER_RIGHT:
				if(shapeFrom == StairShape.INNER_LEFT
					&& facingFrom == facing.rotateYClockwise())
					return true;
				break;
				
				case OUTER_LEFT:
				if(shapeFrom == StairShape.OUTER_RIGHT
					&& facingFrom == facing.rotateYCounterclockwise())
					return true;
				if(shapeFrom == StairShape.STRAIGHT && (facingFrom == facing
					|| facingFrom == facing.rotateYCounterclockwise()))
					return true;
				break;
				
				case OUTER_RIGHT:
				if(shapeFrom == StairShape.OUTER_LEFT
					&& facingFrom == facing.rotateYClockwise())
					return true;
				if(shapeFrom == StairShape.STRAIGHT && (facingFrom == facing
					|| facingFrom == facing.rotateYClockwise()))
					return true;
				break;
			}
		}
		
		// other stairs rear
		if(facingFrom == direction.getOpposite()
			&& shapeFrom != StairShape.OUTER_LEFT
			&& shapeFrom != StairShape.OUTER_RIGHT)
			return true;
		
		// other curved stairs fully covered side
		if(facingFrom == direction.rotateYClockwise()
			&& shapeFrom == StairShape.INNER_RIGHT)
			return true;
		if(facingFrom == direction.rotateYCounterclockwise()
			&& shapeFrom == StairShape.INNER_LEFT)
			return true;
		
		// rear
		if(direction == facing && half == halfFrom)
		{
			if(facingFrom == facing.rotateYCounterclockwise()
				&& shape == StairShape.OUTER_LEFT
				&& shapeFrom != StairShape.OUTER_RIGHT)
				return true;
			
			if(facingFrom == facing.rotateYClockwise()
				&& shape == StairShape.OUTER_RIGHT
				&& shapeFrom != StairShape.OUTER_LEFT)
				return true;
		}
		
		// front
		if(direction == facing.getOpposite() && half == halfFrom)
		{
			if(facingFrom == facing.rotateYCounterclockwise()
				&& shapeFrom != StairShape.OUTER_LEFT)
				return true;
			
			if(facingFrom == facing.rotateYClockwise()
				&& shapeFrom != StairShape.OUTER_RIGHT)
				return true;
			
			if(facingFrom == facing.getOpposite())
				return true;
		}
		
		// left
		if(direction == facing.rotateYCounterclockwise() && half == halfFrom)
		{
			if(facingFrom == direction && shape != StairShape.INNER_LEFT
				&& shapeFrom == StairShape.INNER_RIGHT)
				return true;
			
			if(facingFrom == facing && shapeFrom != StairShape.OUTER_LEFT)
				return true;
			
			if(facingFrom == facing.rotateYClockwise()
				&& shapeFrom == StairShape.OUTER_LEFT
				&& shape != StairShape.INNER_LEFT)
				return true;
			
			if(facingFrom == facing.getOpposite()
				&& shape == StairShape.OUTER_RIGHT)
				return true;
		}
		
		// right
		if(direction == facing.rotateYClockwise() && half == halfFrom)
		{
			if(facingFrom == direction && shape != StairShape.INNER_RIGHT
				&& shapeFrom == StairShape.INNER_LEFT)
				return true;
			
			if(facingFrom == facing && shapeFrom != StairShape.OUTER_RIGHT)
				return true;
			
			if(facingFrom == facing.rotateYCounterclockwise()
				&& shapeFrom == StairShape.OUTER_RIGHT
				&& shape != StairShape.INNER_RIGHT)
				return true;
			
			if(facingFrom == facing.getOpposite()
				&& shape == StairShape.OUTER_LEFT)
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
	public float getAmbientOcclusionLightLevel(BlockState state,
		BlockView world, BlockPos pos)
	{
		return 1.0F;
	}
	
	@Override
	public boolean isTransparent(BlockState state, BlockView world,
		BlockPos pos)
	{
		return false;
	}
}
