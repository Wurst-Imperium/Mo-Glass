/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass.mixin;

import net.wurstclient.glass.MoGlass;
import net.wurstclient.glass.MoGlassBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.TintedGlassBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.EmptyBlockView;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TintedGlassBlock.class)
public abstract class TintedGlassBlockMixin extends TransparentBlock
{
	private TintedGlassBlockMixin(MoGlass moGlass, Settings settings)
	{
		super(settings);
	}
	
	@Override
	public boolean isSideInvisible(BlockState state, BlockState stateFrom,
		Direction direction)
	{
		if(stateFrom.getBlock() == MoGlassBlocks.TINTED_GLASS_SLAB)
			if(isInvisible(state, stateFrom, direction))
				return true;
			
		if(stateFrom.getBlock() == MoGlassBlocks.TINTED_GLASS_STAIRS)
			if(isInvisible(state, stateFrom, direction))
				return true;
			
		return super.isSideInvisible(state, stateFrom, direction);
	}
	
	@Unique
	protected boolean isInvisible(BlockState state, BlockState stateFrom,
		Direction direction)
	{
		VoxelShape stateCullingShape =
			state.getOutlineShape(EmptyBlockView.INSTANCE, BlockPos.ORIGIN);
		VoxelShape stateFromCullingShape =
			stateFrom.getOutlineShape(EmptyBlockView.INSTANCE, BlockPos.ORIGIN);
		return VoxelShapes.isSideCovered(stateCullingShape,
			stateFromCullingShape, direction);
	}
}
