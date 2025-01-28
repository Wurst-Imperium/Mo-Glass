/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass.test;

import static net.wurstclient.glass.test.WiModsTestHelper.*;

import java.time.Duration;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.block.enums.StairShape;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.wurstclient.glass.MoGlassBlocks;

public enum GlassPieceConnectionTest
{
	;
	
	public static void testGlassPiecesConnectCorrectly()
	{
		System.out.println("Testing if glass pieces connect correctly...");
		BlockPos pos = submitAndGet(mc -> mc.player.getBlockPos());
		
		BlockState[][] blocksWithFullyCoveredNorthSide = new BlockState[][]{
			blocks(), slabs(SlabType.DOUBLE),
			stairs(Direction.NORTH, BlockHalf.BOTTOM, StairShape.STRAIGHT),
			stairs(Direction.NORTH, BlockHalf.TOP, StairShape.STRAIGHT),
			stairs(Direction.NORTH, BlockHalf.BOTTOM, StairShape.INNER_LEFT),
			stairs(Direction.EAST, BlockHalf.BOTTOM, StairShape.INNER_LEFT),
			stairs(Direction.EAST, BlockHalf.TOP, StairShape.INNER_LEFT),
			stairs(Direction.NORTH, BlockHalf.TOP, StairShape.INNER_RIGHT),
			stairs(Direction.NORTH, BlockHalf.BOTTOM, StairShape.INNER_RIGHT),
			stairs(Direction.WEST, BlockHalf.BOTTOM, StairShape.INNER_RIGHT),
			stairs(Direction.NORTH, BlockHalf.TOP, StairShape.INNER_LEFT),
			stairs(Direction.WEST, BlockHalf.TOP, StairShape.INNER_RIGHT)};
		
		for(BlockState[] back : blocksWithFullyCoveredNorthSide)
		{
			test(pos, back, blocks(), true, true);
			test(pos, back, slabs(SlabType.BOTTOM), false, true);
			test(pos, back, slabs(SlabType.TOP), false, true);
			test(pos, back, slabs(SlabType.DOUBLE), true, true);
			for(Direction dir : Direction.Type.HORIZONTAL)
				for(BlockHalf half : BlockHalf.values())
				{
					test(pos, back, stairs(dir, half, StairShape.STRAIGHT),
						dir == Direction.SOUTH, true);
					test(pos, back, stairs(dir, half, StairShape.INNER_LEFT),
						dir == Direction.SOUTH || dir == Direction.WEST, true);
					test(pos, back, stairs(dir, half, StairShape.INNER_RIGHT),
						dir == Direction.SOUTH || dir == Direction.EAST, true);
					test(pos, back, stairs(dir, half, StairShape.OUTER_LEFT),
						false, true);
					test(pos, back, stairs(dir, half, StairShape.OUTER_RIGHT),
						false, true);
				}
		}
		
		// Clean up
		runChatCommand("fill ~-7 ~ ~-4 ~7 ~30 ~10 air");
		clearChat();
	}
	
	private static void test(BlockPos pos, BlockState[] backBlocks,
		BlockState[] frontBlocks, boolean frontSeamless, boolean backSeamless)
	{
		BlockPos back1 = pos.add(2, 0, 5);
		BlockPos back2 = pos.add(0, 0, 5);
		BlockPos back3 = pos.add(-2, 0, 5);
		BlockPos front1 = pos.add(2, 0, 4);
		BlockPos front2 = pos.add(0, 0, 4);
		BlockPos front3 = pos.add(-2, 0, 4);
		
		setBlock(back1, backBlocks[0]);
		setBlock(back2, backBlocks[1]);
		setBlock(back3, backBlocks[2]);
		setBlock(front1, frontBlocks[0]);
		setBlock(front2, frontBlocks[1]);
		setBlock(front3, frontBlocks[2]);
		
		waitUntil("blocks are placed", mc -> {
			return mc.world.getBlockState(back1) == backBlocks[0]
				&& mc.world.getBlockState(back2) == backBlocks[1]
				&& mc.world.getBlockState(back3) == backBlocks[2]
				&& mc.world.getBlockState(front1) == frontBlocks[0]
				&& mc.world.getBlockState(front2) == frontBlocks[1]
				&& mc.world.getBlockState(front3) == frontBlocks[2];
		});
		
		assertConnection(back1, Direction.NORTH, frontSeamless);
		assertConnection(back2, Direction.NORTH, frontSeamless);
		assertConnection(back3, Direction.NORTH, frontSeamless);
		assertConnection(front1, Direction.SOUTH, backSeamless);
		assertConnection(front2, Direction.SOUTH, backSeamless);
		assertConnection(front3, Direction.SOUTH, backSeamless);
	}
	
	private static void assertConnection(BlockPos pos, Direction dir,
		boolean connected)
	{
		RuntimeException e = submitAndGet(mc -> {
			BlockState state = mc.world.getBlockState(pos);
			BlockState state2 = mc.world.getBlockState(pos.offset(dir));
			if(Block.shouldDrawSide(state, state2, dir) != connected)
				return null;
			
			if(connected)
				return new RuntimeException(
					"Block " + state + " did not connect to " + state2);
			else
				return new RuntimeException(
					"Block " + state + " connected to " + state2);
		});
		
		if(e == null)
			return;
		
		setBlock(pos.up(2), Blocks.RED_CONCRETE.getDefaultState());
		if(dir == Direction.SOUTH)
			runChatCommand("tp @s ~ ~ ~9 180 0");
		clearChat();
		takeScreenshot("FAILED_TEST_glass_connected_incorrectly",
			Duration.ofMillis(250));
		throw e;
	}
	
	private static BlockState[] blocks()
	{
		return new BlockState[]{Blocks.GLASS.getDefaultState(),
			Blocks.WHITE_STAINED_GLASS.getDefaultState(),
			Blocks.TINTED_GLASS.getDefaultState()};
	}
	
	private static BlockState[] slabs(SlabType type)
	{
		return new BlockState[]{
			MoGlassBlocks.GLASS_SLAB.getDefaultState().with(SlabBlock.TYPE,
				type),
			MoGlassBlocks.STAINED_GLASS_SLABS.get(DyeColor.WHITE.ordinal())
				.getDefaultState().with(SlabBlock.TYPE, type),
			MoGlassBlocks.TINTED_GLASS_SLAB.getDefaultState()
				.with(SlabBlock.TYPE, type)};
	}
	
	private static BlockState[] stairs(Direction facing, BlockHalf half,
		StairShape shape)
	{
		return new BlockState[]{
			MoGlassBlocks.GLASS_STAIRS.getDefaultState()
				.with(StairsBlock.FACING, facing).with(StairsBlock.HALF, half)
				.with(StairsBlock.SHAPE, shape),
			MoGlassBlocks.STAINED_GLASS_STAIRS.get(DyeColor.WHITE.ordinal())
				.getDefaultState().with(StairsBlock.FACING, facing)
				.with(StairsBlock.HALF, half).with(StairsBlock.SHAPE, shape),
			MoGlassBlocks.TINTED_GLASS_STAIRS.getDefaultState()
				.with(StairsBlock.FACING, facing).with(StairsBlock.HALF, half)
				.with(StairsBlock.SHAPE, shape)};
	}
}
