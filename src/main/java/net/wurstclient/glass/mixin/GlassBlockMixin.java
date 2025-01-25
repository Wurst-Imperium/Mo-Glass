/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TranslucentBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.EmptyBlockView;

import net.wurstclient.glass.MoGlassBlocks;

@Mixin(TransparentBlock.class)
public abstract class GlassBlockMixin extends TranslucentBlock
{
	private GlassBlockMixin(Settings settings)
	{
		super(settings);
	}
	
	@Override
	public boolean isSideInvisible(BlockState state, BlockState stateFrom,
		Direction direction)
	{
		if(state.getBlock() == Blocks.GLASS)
		{
			if(stateFrom.getBlock() == MoGlassBlocks.GLASS_SLAB && isInvisible(state, stateFrom, direction))
				return true;
				
			if(stateFrom.getBlock() == MoGlassBlocks.GLASS_STAIRS && isInvisible(state, stateFrom, direction))
				return true;
		}
		
		return super.isSideInvisible(state, stateFrom, direction);
	}
	
	@Unique
	private boolean isInvisible(BlockState state, BlockState stateFrom, Direction direction)
	{
		VoxelShape stateCullingShape = state.getOutlineShape(EmptyBlockView.INSTANCE, BlockPos.ORIGIN);
		VoxelShape stateFromCullingShape = stateFrom.getOutlineShape(EmptyBlockView.INSTANCE, BlockPos.ORIGIN);
		return VoxelShapes.isSideCovered(stateCullingShape, stateFromCullingShape, direction);
	}
}
