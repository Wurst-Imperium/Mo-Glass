/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.EmptyBlockView;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public final class TintedGlassSlabBlock extends SlabBlock
{
	public TintedGlassSlabBlock(Settings settings)
	{
		super(settings);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState stateFrom,
		Direction direction)
	{
		if(stateFrom.getBlock() == Blocks.TINTED_GLASS)
			return true;
		
		if(stateFrom.getBlock() == this)
			if(isInvisible(state, stateFrom, direction))
				return true;
			
		if(stateFrom.getBlock() == MoGlassBlocks.TINTED_GLASS_STAIRS)
			if(isInvisible(state, stateFrom, direction))
				return true;
			
		return super.isSideInvisible(state, stateFrom, direction);
	}
	
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
		return false;
	}
	
	@Override
	public int getOpacity(BlockState state)
	{
		return state.get(TYPE) == SlabType.DOUBLE ? 15 : 0;
	}
}
