/*
 * Copyright (c) 2019-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.TranslucentBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.block.enums.StairShape;
import net.minecraft.util.math.Direction;
import net.wurstclient.glass.MoGlass;
import net.wurstclient.glass.MoGlassBlocks;

@Mixin(TransparentBlock.class)
public abstract class GlassBlockMixin extends TranslucentBlock
{
	private GlassBlockMixin(MoGlass moGlass, Settings settings)
	{
		super(settings);
	}
	
	@Override
	public boolean isSideInvisible(BlockState state, BlockState stateFrom,
		Direction direction)
	{
		if(state.getBlock() == Blocks.GLASS)
		{
			if(stateFrom.getBlock() == MoGlassBlocks.GLASS_SLAB)
				if(isInvisibleToGlassSlab(state, stateFrom, direction))
					return true;
				
			if(stateFrom.getBlock() == MoGlassBlocks.GLASS_STAIRS)
				if(isInvisibleToGlassStairs(state, stateFrom, direction))
					return true;
		}
		
		return super.isSideInvisible(state, stateFrom, direction);
	}
	
	private boolean isInvisibleToGlassSlab(BlockState state,
		BlockState stateFrom, Direction direction)
	{
		SlabType typeFrom = stateFrom.get(SlabBlock.TYPE);
		
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
		BlockHalf halfFrom = stateFrom.get(StairsBlock.HALF);
		Direction facingFrom = stateFrom.get(StairsBlock.FACING);
		StairShape shapeFrom = stateFrom.get(StairsBlock.SHAPE);
		
		// up
		if(direction == Direction.UP)
			if(halfFrom == BlockHalf.BOTTOM)
				return true;
			
		// down
		if(direction == Direction.DOWN)
			if(halfFrom == BlockHalf.TOP)
				return true;
			
		// other stairs rear
		if(facingFrom == direction.getOpposite()
			&& shapeFrom != StairShape.OUTER_LEFT
			&& shapeFrom != StairShape.OUTER_RIGHT)
			return true;
		
		// other curved stairs fully covered side
		if(facingFrom.rotateYCounterclockwise() == direction
			&& shapeFrom == StairShape.INNER_RIGHT)
			return true;
		if(facingFrom.rotateYClockwise() == direction
			&& shapeFrom == StairShape.INNER_LEFT)
			return true;
		
		return false;
	}
}
