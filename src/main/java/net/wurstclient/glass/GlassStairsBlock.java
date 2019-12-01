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
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
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
		if(blockState_2.getBlock() == Blocks.GLASS)
			return true;
		
		if(blockState_2.getBlock() == MoGlass.GLASS_SLAB)
			if(isInvisibleToGlassSlab(blockState_1, blockState_2, direction_1))
				return true;
			
		if(blockState_2.getBlock() == this)
			if(isInvisibleToGlassStairs(blockState_1, blockState_2,
				direction_1))
				return true;
			
		return super.isSideInvisible(blockState_1, blockState_2, direction_1);
	}
	
	private boolean isInvisibleToGlassSlab(BlockState blockState_1,
		BlockState blockState_2, Direction direction_1)
	{
		BlockHalf half = blockState_1.get(StairsBlock.HALF);
		Direction facing = blockState_1.get(StairsBlock.FACING);
		
		switch(blockState_2.get(SlabBlock.TYPE))
		{
			case BOTTOM:
			return half == BlockHalf.BOTTOM
				&& facing == direction_1.getOpposite();
			
			case TOP:
			return half == BlockHalf.TOP && facing == direction_1.getOpposite();
			
			case DOUBLE:
			return true;
		}
		
		return false;
	}
	
	private boolean isInvisibleToGlassStairs(BlockState blockState_1,
		BlockState blockState_2, Direction direction_1)
	{
		BlockHalf half1 = blockState_1.get(StairsBlock.HALF);
		BlockHalf half2 = blockState_2.get(StairsBlock.HALF);
		Direction facing1 = blockState_1.get(StairsBlock.FACING);
		Direction facing2 = blockState_2.get(StairsBlock.FACING);
		
		if(direction_1 == Direction.UP)
			if(half2 == BlockHalf.BOTTOM)
				return true;
			else if(facing1 == facing2 && half1 != half2)
				return true;
			
		if(direction_1 == Direction.DOWN)
			if(half2 == BlockHalf.TOP)
				return true;
			else if(facing1 == facing2 && half1 != half2)
				return true;
			
		if(half1 == half2)
			if(facing1 == direction_1.getOpposite())
				return true;
			
		// TODO: other cases
		
		return false;
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
