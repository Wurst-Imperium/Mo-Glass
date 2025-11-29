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

public final class TintedGlassSlabBlock extends SlabBlock
{
	public TintedGlassSlabBlock(Properties settings)
	{
		super(settings);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public boolean skipRendering(BlockState state, BlockState stateFrom,
		Direction direction)
	{
		if(stateFrom.getBlock() == Blocks.TINTED_GLASS)
			return true;
		
		if(stateFrom.getBlock() == this)
			if(isInvisibleToGlassSlab(state, stateFrom, direction))
				return true;
			
		if(stateFrom.getBlock() == MoGlassBlocks.TINTED_GLASS_STAIRS)
			if(isInvisibleToGlassStairs(state, stateFrom, direction))
				return true;
			
		return super.skipRendering(state, stateFrom, direction);
	}
	
	private boolean isInvisibleToGlassSlab(BlockState state,
		BlockState stateFrom, Direction direction)
	{
		SlabType type = state.getValue(SlabBlock.TYPE);
		SlabType typeFrom = stateFrom.getValue(SlabBlock.TYPE);
		
		switch(direction)
		{
			case UP:
			if(typeFrom != SlabType.TOP && type != SlabType.BOTTOM)
				return true;
			break;
			
			case DOWN:
			if(typeFrom != SlabType.BOTTOM && type != SlabType.TOP)
				return true;
			break;
			
			case NORTH:
			case EAST:
			case SOUTH:
			case WEST:
			if(type == typeFrom || typeFrom == SlabType.DOUBLE)
				return true;
			break;
		}
		
		return false;
	}
	
	private boolean isInvisibleToGlassStairs(BlockState state,
		BlockState stateFrom, Direction direction)
	{
		SlabType type = state.getValue(SlabBlock.TYPE);
		Half halfFrom = stateFrom.getValue(StairBlock.HALF);
		Direction facingFrom = stateFrom.getValue(StairBlock.FACING);
		StairsShape shapeFrom = stateFrom.getValue(StairBlock.SHAPE);
		
		// up
		if(direction == Direction.UP)
			if(halfFrom == Half.BOTTOM)
				return true;
			
		// down
		if(direction == Direction.DOWN)
			if(halfFrom == Half.TOP)
				return true;
			
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
		
		// sides
		if(direction.get2DDataValue() != -1)
		{
			if(type == SlabType.BOTTOM && halfFrom == Half.BOTTOM)
				return true;
			
			if(type == SlabType.TOP && halfFrom == Half.TOP)
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
	public boolean propagatesSkylightDown(BlockState state, BlockGetter world,
		BlockPos pos)
	{
		return false;
	}
	
	@Override
	public int getLightBlock(BlockState state, BlockGetter world, BlockPos pos)
	{
		return state.getValue(TYPE) == SlabType.DOUBLE
			? world.getMaxLightLevel() : 0;
	}
}
