/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class TintedGlassStairsBlock extends StairBlock
{
	protected TintedGlassStairsBlock(Properties settings)
	{
		super(Blocks.TINTED_GLASS.defaultBlockState(), settings);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public boolean skipRendering(BlockState state, BlockState stateFrom,
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
			
		return super.skipRendering(state, stateFrom, direction);
	}
	
	private boolean isInvisibleToGlassSlab(BlockState state,
		BlockState stateFrom, Direction direction)
	{
		Half half = state.getValue(StairBlock.HALF);
		Direction facing = state.getValue(StairBlock.FACING);
		StairsShape shape = state.getValue(StairBlock.SHAPE);
		SlabType typeFrom = stateFrom.getValue(SlabBlock.TYPE);
		
		if(direction == Direction.UP)
			if(typeFrom != SlabType.TOP)
				return true;
			
		if(direction == Direction.DOWN)
			if(typeFrom != SlabType.BOTTOM)
				return true;
			
		if(typeFrom == SlabType.DOUBLE)
			return true;
		
		// front
		if(direction == facing.getOpposite() && shape != StairsShape.INNER_LEFT
			&& shape != StairsShape.INNER_RIGHT)
		{
			if(typeFrom == SlabType.BOTTOM && half == Half.BOTTOM)
				return true;
			
			if(typeFrom == SlabType.TOP && half == Half.TOP)
				return true;
		}
		
		// right
		if(direction == facing.getClockWise()
			&& shape == StairsShape.OUTER_LEFT)
		{
			if(typeFrom == SlabType.BOTTOM && half == Half.BOTTOM)
				return true;
			
			if(typeFrom == SlabType.TOP && half == Half.TOP)
				return true;
		}
		
		// left
		if(direction == facing.getCounterClockWise()
			&& shape == StairsShape.OUTER_RIGHT)
		{
			if(typeFrom == SlabType.BOTTOM && half == Half.BOTTOM)
				return true;
			
			if(typeFrom == SlabType.TOP && half == Half.TOP)
				return true;
		}
		
		return false;
	}
	
	private boolean isInvisibleToGlassStairs(BlockState state,
		BlockState stateFrom, Direction direction)
	{
		Half half = state.getValue(StairBlock.HALF);
		Half halfFrom = stateFrom.getValue(StairBlock.HALF);
		Direction facing = state.getValue(StairBlock.FACING);
		Direction facingFrom = stateFrom.getValue(StairBlock.FACING);
		StairsShape shape = state.getValue(StairBlock.SHAPE);
		StairsShape shapeFrom = stateFrom.getValue(StairBlock.SHAPE);
		
		// up
		if(direction == Direction.UP)
		{
			if(halfFrom == Half.BOTTOM)
				return true;
			
			if(half != halfFrom)
			{
				if(facing == facingFrom && shape == shapeFrom)
					return true;
				
				switch(shape)
				{
					case STRAIGHT:
					if(shapeFrom == StairsShape.INNER_LEFT
						&& (facingFrom == facing
							|| facingFrom == facing.getClockWise()))
						return true;
					if(shapeFrom == StairsShape.INNER_RIGHT
						&& (facingFrom == facing
							|| facingFrom == facing.getCounterClockWise()))
						return true;
					break;
					
					case INNER_LEFT:
					if(shapeFrom == StairsShape.INNER_RIGHT
						&& facingFrom == facing.getCounterClockWise())
						return true;
					break;
					
					case INNER_RIGHT:
					if(shapeFrom == StairsShape.INNER_LEFT
						&& facingFrom == facing.getClockWise())
						return true;
					break;
					
					case OUTER_LEFT:
					if(shapeFrom == StairsShape.OUTER_RIGHT
						&& facingFrom == facing.getCounterClockWise())
						return true;
					if(shapeFrom == StairsShape.STRAIGHT
						&& (facingFrom == facing
							|| facingFrom == facing.getCounterClockWise()))
						return true;
					break;
					
					case OUTER_RIGHT:
					if(shapeFrom == StairsShape.OUTER_LEFT
						&& facingFrom == facing.getClockWise())
						return true;
					if(shapeFrom == StairsShape.STRAIGHT
						&& (facingFrom == facing
							|| facingFrom == facing.getClockWise()))
						return true;
					break;
				}
			}
		}
		
		// down
		if(direction == Direction.DOWN)
		{
			if(halfFrom == Half.TOP)
				return true;
			
			switch(shape)
			{
				case STRAIGHT:
				if(shapeFrom == StairsShape.INNER_LEFT && (facingFrom == facing
					|| facingFrom == facing.getClockWise()))
					return true;
				if(shapeFrom == StairsShape.INNER_RIGHT && (facingFrom == facing
					|| facingFrom == facing.getCounterClockWise()))
					return true;
				break;
				
				case INNER_LEFT:
				if(shapeFrom == StairsShape.INNER_RIGHT
					&& facingFrom == facing.getCounterClockWise())
					return true;
				break;
				
				case INNER_RIGHT:
				if(shapeFrom == StairsShape.INNER_LEFT
					&& facingFrom == facing.getClockWise())
					return true;
				break;
				
				case OUTER_LEFT:
				if(shapeFrom == StairsShape.OUTER_RIGHT
					&& facingFrom == facing.getCounterClockWise())
					return true;
				if(shapeFrom == StairsShape.STRAIGHT && (facingFrom == facing
					|| facingFrom == facing.getCounterClockWise()))
					return true;
				break;
				
				case OUTER_RIGHT:
				if(shapeFrom == StairsShape.OUTER_LEFT
					&& facingFrom == facing.getClockWise())
					return true;
				if(shapeFrom == StairsShape.STRAIGHT && (facingFrom == facing
					|| facingFrom == facing.getClockWise()))
					return true;
				break;
			}
		}
		
		// other stairs rear
		if(facingFrom == direction.getOpposite()
			&& shapeFrom != StairsShape.OUTER_LEFT
			&& shapeFrom != StairsShape.OUTER_RIGHT)
			return true;
		
		// other curved stairs fully covered side
		if(facingFrom.getCounterClockWise() == direction
			&& shapeFrom == StairsShape.INNER_RIGHT)
			return true;
		if(facingFrom.getClockWise() == direction
			&& shapeFrom == StairsShape.INNER_LEFT)
			return true;
		
		// rear
		if(direction == facing && half == halfFrom)
		{
			if(facingFrom == facing.getCounterClockWise()
				&& shape == StairsShape.OUTER_LEFT
				&& shapeFrom != StairsShape.OUTER_RIGHT)
				return true;
			
			if(facingFrom == facing.getClockWise()
				&& shape == StairsShape.OUTER_RIGHT
				&& shapeFrom != StairsShape.OUTER_LEFT)
				return true;
		}
		
		// front
		if(direction == facing.getOpposite() && half == halfFrom)
		{
			if(shape != StairsShape.INNER_LEFT
				&& shape != StairsShape.INNER_RIGHT)
				return true;
			
			if(facingFrom == facing.getCounterClockWise()
				&& shapeFrom != StairsShape.OUTER_LEFT)
				return true;
			
			if(facingFrom == facing.getClockWise()
				&& shapeFrom != StairsShape.OUTER_RIGHT)
				return true;
			
			if(facingFrom == facing.getOpposite())
				if(shape == StairsShape.INNER_LEFT
					&& shapeFrom == StairsShape.INNER_RIGHT)
					return true;
				else if(shape == StairsShape.INNER_RIGHT
					&& shapeFrom == StairsShape.INNER_LEFT)
					return true;
		}
		
		// left
		if(direction == facing.getCounterClockWise() && half == halfFrom)
		{
			if(facingFrom == direction)
				if(shape != StairsShape.INNER_LEFT
					&& shapeFrom == StairsShape.INNER_RIGHT)
					return true;
				else if(shape == StairsShape.OUTER_RIGHT)
					return true;
				
			if(facingFrom == facing && shape != StairsShape.INNER_LEFT)
				if(shapeFrom != StairsShape.OUTER_LEFT)
					return true;
				else if(shape == StairsShape.OUTER_RIGHT
					&& shapeFrom == StairsShape.OUTER_LEFT)
					return true;
				
			if(facingFrom == facing.getClockWise())
				if(shapeFrom == StairsShape.OUTER_RIGHT
					&& shape == StairsShape.OUTER_RIGHT)
					return true;
				else if(shapeFrom == StairsShape.OUTER_LEFT
					&& shape != StairsShape.INNER_LEFT)
					return true;
				
			if(facingFrom == facing.getOpposite()
				&& shape == StairsShape.OUTER_RIGHT)
				return true;
		}
		
		// right
		if(direction == facing.getClockWise() && half == halfFrom)
		{
			if(facingFrom == direction)
				if(shape != StairsShape.INNER_RIGHT
					&& shapeFrom == StairsShape.INNER_LEFT)
					return true;
				else if(shape == StairsShape.OUTER_LEFT)
					return true;
				
			if(facingFrom == facing && shape != StairsShape.INNER_RIGHT)
				if(shapeFrom != StairsShape.OUTER_RIGHT)
					return true;
				else if(shape == StairsShape.OUTER_LEFT
					&& shapeFrom == StairsShape.OUTER_RIGHT)
					return true;
				
			if(facingFrom == facing.getCounterClockWise())
				if(shapeFrom == StairsShape.OUTER_LEFT
					&& shape == StairsShape.OUTER_LEFT)
					return true;
				else if(shapeFrom == StairsShape.OUTER_RIGHT
					&& shape != StairsShape.INNER_RIGHT)
					return true;
				
			if(facingFrom == facing.getOpposite()
				&& shape == StairsShape.OUTER_LEFT)
				return true;
		}
		
		return false;
	}
	
	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world,
		BlockPos pos, CollisionContext context)
	{
		return Shapes.empty();
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public float getShadeBrightness(BlockState state, BlockGetter world,
		BlockPos pos)
	{
		return 1.0F;
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state)
	{
		return false;
	}
}
