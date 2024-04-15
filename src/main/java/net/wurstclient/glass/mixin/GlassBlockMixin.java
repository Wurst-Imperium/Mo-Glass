/*
 * Copyright (c) 2019-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.wurstclient.glass.GlassSlabBlock;
import net.wurstclient.glass.GlassStairsBlock;
import net.wurstclient.glass.MoGlass;

@Mixin(GlassBlock.class)
public class GlassBlockMixin extends AbstractGlassBlock
{
	private GlassBlockMixin(MoGlass moGlass, BlockBehaviour.Properties settings)
	{
		super(settings);
	}
	
	@Override
	public boolean skipRendering(BlockState state, BlockState stateFrom,
		Direction direction)
	{
		if(stateFrom.getBlock() instanceof GlassSlabBlock)
			if(isInvisibleToGlassSlab(state, stateFrom, direction))
				return true;
			
		if(stateFrom.getBlock() instanceof GlassStairsBlock)
			if(isInvisibleToGlassStairs(state, stateFrom, direction))
				return true;
			
		return super.skipRendering(state, stateFrom, direction);
	}
	
	private boolean isInvisibleToGlassSlab(BlockState state,
		BlockState stateFrom, Direction direction)
	{
		SlabType typeFrom = stateFrom.getValue(SlabBlock.TYPE);
		
		if(typeFrom == SlabType.DOUBLE)
			return true;
		
		if(direction == Direction.UP)
			if(typeFrom != SlabType.TOP)
				return true;
			
		if(direction == Direction.DOWN)
			if(typeFrom != SlabType.BOTTOM)
				return true;
			
		return false;
	}
	
	private boolean isInvisibleToGlassStairs(BlockState state,
		BlockState stateFrom, Direction direction)
	{
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
		
		return false;
	}
}
