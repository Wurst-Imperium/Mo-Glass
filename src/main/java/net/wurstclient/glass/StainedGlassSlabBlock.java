/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
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
	
	public StainedGlassSlabBlock(DyeColor color, Settings settings)
	{
		super(settings);
		this.color = color;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState stateFrom,
		Direction direction)
	{
		Block blockFrom = stateFrom.getBlock();
		
		if(blockFrom instanceof StainedGlassBlock
			&& ((StainedGlassBlock)blockFrom).getColor() == color)
			return true;
		
		if(blockFrom == this)
			if(isInvisibleToGlassSlab(state, stateFrom, direction))
				return true;
			
		if(blockFrom instanceof StainedGlassStairsBlock
			&& ((StainedGlassStairsBlock)blockFrom).getColor() == color)
			if(isInvisibleToGlassStairs(state, stateFrom, direction))
				return true;
			
		return super.isSideInvisible(state, stateFrom, direction);
	}
	
	private boolean isInvisibleToGlassSlab(BlockState state,
		BlockState stateFrom, Direction direction)
	{
		SlabType type = state.get(SlabBlock.TYPE);
		SlabType typeFrom = stateFrom.get(SlabBlock.TYPE);
		
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
		SlabType type = state.get(SlabBlock.TYPE);
		BlockHalf halfFrom = stateFrom.get(StairsBlock.HALF);
		Direction facingFrom = stateFrom.get(StairsBlock.FACING);
		
		// up
		if(direction == Direction.UP)
			if(halfFrom == BlockHalf.BOTTOM)
				return true;
			
		// down
		if(direction == Direction.DOWN)
			if(halfFrom == BlockHalf.TOP)
				return true;
			
		// other stairs rear
		if(facingFrom == direction.getOpposite())
			return true;
		
		// sides
		if(direction.getHorizontalQuarterTurns() != -1)
		{
			if(type == SlabType.BOTTOM && halfFrom == BlockHalf.BOTTOM)
				return true;
			
			if(type == SlabType.TOP && halfFrom == BlockHalf.TOP)
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
	public boolean isTransparent(BlockState state)
	{
		return true;
	}
	
	@Override
	public DyeColor getColor()
	{
		return color;
	}
}
