/*
 * Copyright (C) 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public final class GlassStairsBlock extends StairsBlock
{
	protected GlassStairsBlock(Settings block$Settings_1)
	{
		super(Blocks.GLASS.getDefaultState(), block$Settings_1);
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState blockState_1,
		BlockState blockState_2, Direction direction_1)
	{
		return blockState_2.getBlock() == this && blockState_1
			.get(StairsBlock.HALF) == blockState_2.get(StairsBlock.HALF) ? true
				: super.isSideInvisible(blockState_1, blockState_2,
					direction_1);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public float getAmbientOcclusionLightLevel(BlockState blockState_1,
		BlockView blockView_1, BlockPos blockPos_1)
	{
		return 1.0F;
	}
	
	@Override
	public boolean isTranslucent(BlockState blockState_1, BlockView blockView_1,
		BlockPos blockPos_1)
	{
		return true;
	}
	
	@Override
	public boolean canSuffocate(BlockState blockState_1, BlockView blockView_1,
		BlockPos blockPos_1)
	{
		return false;
	}
	
	@Override
	public boolean isSimpleFullBlock(BlockState blockState_1,
		BlockView blockView_1, BlockPos blockPos_1)
	{
		return false;
	}
	
	@Override
	public boolean allowsSpawning(BlockState blockState_1,
		BlockView blockView_1, BlockPos blockPos_1, EntityType<?> entityType_1)
	{
		return false;
	}
}
