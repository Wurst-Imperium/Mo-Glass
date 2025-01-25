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
import net.minecraft.block.*;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public final class StainedGlassStairsBlock extends GlassStairsBlock
	implements Stainable
{
	private final DyeColor color;
	
	public StainedGlassStairsBlock(DyeColor color, Settings settings)
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
		
		if(blockFrom instanceof StainedGlassSlabBlock
			&& ((StainedGlassSlabBlock)blockFrom).getColor() == color)
			if(isInvisible(state, stateFrom, direction))
				return true;
			
		if(blockFrom == this && isInvisible(state, stateFrom, direction))
			return true;
		
		return super.isSideInvisible(state, stateFrom, direction);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public float getAmbientOcclusionLightLevel(BlockState state,
		BlockView world, BlockPos pos)
	{
		return 1.0F;
	}
	
	@Override
	public DyeColor getColor()
	{
		return color;
	}
}
