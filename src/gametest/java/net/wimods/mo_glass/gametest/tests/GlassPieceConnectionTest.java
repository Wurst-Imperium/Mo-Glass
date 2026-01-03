/*
 * Copyright (c) 2019-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass.gametest.tests;

import static net.wimods.mo_glass.gametest.WiModsTestHelper.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestClientWorldContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.wimods.mo_glass.MoGlassBlocks;
import net.wimods.mo_glass.gametest.MoGlassTest;

public enum GlassPieceConnectionTest
{
	;
	
	public static void testGlassPiecesConnectCorrectly(
		ClientGameTestContext context, TestSingleplayerContext spContext)
	{
		TestClientWorldContext world = spContext.getClientWorld();
		TestServerContext server = spContext.getServer();
		
		MoGlassTest.LOGGER.info("Testing if glass pieces connect correctly...");
		BlockPos pos = context.computeOnClient(mc -> mc.player.blockPosition());
		
		BlockState[][] blocksWithFullyCoveredNorthSide =
			{blocks(), slabs(SlabType.DOUBLE),
				stairs(Direction.NORTH, Half.BOTTOM, StairsShape.STRAIGHT),
				stairs(Direction.NORTH, Half.TOP, StairsShape.STRAIGHT),
				stairs(Direction.NORTH, Half.BOTTOM, StairsShape.INNER_LEFT),
				stairs(Direction.EAST, Half.BOTTOM, StairsShape.INNER_LEFT),
				stairs(Direction.EAST, Half.TOP, StairsShape.INNER_LEFT),
				stairs(Direction.NORTH, Half.TOP, StairsShape.INNER_RIGHT),
				stairs(Direction.NORTH, Half.BOTTOM, StairsShape.INNER_RIGHT),
				stairs(Direction.WEST, Half.BOTTOM, StairsShape.INNER_RIGHT),
				stairs(Direction.NORTH, Half.TOP, StairsShape.INNER_LEFT),
				stairs(Direction.WEST, Half.TOP, StairsShape.INNER_RIGHT)};
		
		for(BlockState[] back : blocksWithFullyCoveredNorthSide)
		{
			test(context, spContext, pos, back, blocks(), false, false);
			test(context, spContext, pos, back, slabs(SlabType.BOTTOM), true,
				false);
			test(context, spContext, pos, back, slabs(SlabType.TOP), true,
				false);
			test(context, spContext, pos, back, slabs(SlabType.DOUBLE), false,
				false);
			for(Direction dir : Direction.Plane.HORIZONTAL)
				for(Half half : Half.values())
				{
					test(context, spContext, pos, back,
						stairs(dir, half, StairsShape.STRAIGHT),
						dir != Direction.SOUTH, false);
					test(context, spContext, pos, back,
						stairs(dir, half, StairsShape.INNER_LEFT),
						dir != Direction.SOUTH && dir != Direction.WEST, false);
					test(context, spContext, pos, back,
						stairs(dir, half, StairsShape.INNER_RIGHT),
						dir != Direction.SOUTH && dir != Direction.EAST, false);
					test(context, spContext, pos, back,
						stairs(dir, half, StairsShape.OUTER_LEFT), true, false);
					test(context, spContext, pos, back,
						stairs(dir, half, StairsShape.OUTER_RIGHT), true,
						false);
				}
		}
		
		BlockState[][] blocksWithBottomHalfCoveredNorthSide =
			{slabs(SlabType.BOTTOM),
				stairs(Direction.SOUTH, Half.BOTTOM, StairsShape.STRAIGHT),
				stairs(Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_LEFT),
				stairs(Direction.WEST, Half.BOTTOM, StairsShape.OUTER_LEFT),
				stairs(Direction.EAST, Half.BOTTOM, StairsShape.OUTER_RIGHT),
				stairs(Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_RIGHT)};
		
		for(BlockState[] back : blocksWithBottomHalfCoveredNorthSide)
		{
			test(context, spContext, pos, back, slabs(SlabType.BOTTOM), false,
				false);
			test(context, spContext, pos, back, slabs(SlabType.TOP), true,
				true);
			for(Half half : Half.values())
			{
				test(context, spContext, pos, back,
					stairs(Direction.NORTH, half, StairsShape.STRAIGHT),
					half == Half.TOP, half == Half.TOP);
				test(context, spContext, pos, back,
					stairs(Direction.EAST, half, StairsShape.STRAIGHT),
					half == Half.TOP, true);
				test(context, spContext, pos, back,
					stairs(Direction.WEST, half, StairsShape.STRAIGHT),
					half == Half.TOP, true);
				
				test(context, spContext, pos, back,
					stairs(Direction.NORTH, half, StairsShape.INNER_LEFT),
					half == Half.TOP, true);
				test(context, spContext, pos, back,
					stairs(Direction.EAST, half, StairsShape.INNER_LEFT),
					half == Half.TOP, true);
				test(context, spContext, pos, back,
					stairs(Direction.NORTH, half, StairsShape.INNER_RIGHT),
					half == Half.TOP, true);
				test(context, spContext, pos, back,
					stairs(Direction.WEST, half, StairsShape.INNER_RIGHT),
					half == Half.TOP, true);
				
				test(context, spContext, pos, back,
					stairs(Direction.NORTH, half, StairsShape.OUTER_LEFT),
					half == Half.TOP, half == Half.TOP);
				test(context, spContext, pos, back,
					stairs(Direction.EAST, half, StairsShape.OUTER_LEFT),
					half == Half.TOP, half == Half.TOP);
				test(context, spContext, pos, back,
					stairs(Direction.SOUTH, half, StairsShape.OUTER_LEFT),
					half == Half.TOP, true);
				test(context, spContext, pos, back,
					stairs(Direction.WEST, half, StairsShape.OUTER_LEFT),
					half == Half.TOP, true);
				
				test(context, spContext, pos, back,
					stairs(Direction.NORTH, half, StairsShape.OUTER_RIGHT),
					half == Half.TOP, half == Half.TOP);
				test(context, spContext, pos, back,
					stairs(Direction.EAST, half, StairsShape.OUTER_RIGHT),
					half == Half.TOP, true);
				test(context, spContext, pos, back,
					stairs(Direction.SOUTH, half, StairsShape.OUTER_RIGHT),
					half == Half.TOP, true);
				test(context, spContext, pos, back,
					stairs(Direction.WEST, half, StairsShape.OUTER_RIGHT),
					half == Half.TOP, half == Half.TOP);
			}
		}
		
		// Clean up
		runCommand(server, "fill ~-7 ~ ~-4 ~7 ~30 ~9 air");
		context.waitTicks(2);
		world.waitForChunksRender();
	}
	
	private static void test(ClientGameTestContext context,
		TestSingleplayerContext spContext, BlockPos startPos,
		BlockState[] backBlocks, BlockState[] frontBlocks, boolean drawFront,
		boolean drawBack)
	{
		TestClientWorldContext world = spContext.getClientWorld();
		TestServerContext server = spContext.getServer();
		
		record TestCase(BlockPos pos, Direction dir, boolean shouldDraw)
		{
			boolean test(LinkedHashMap<BlockPos, BlockState> blocks)
			{
				return Block.shouldRenderFace(blocks.get(pos),
					blocks.get(pos.relative(dir)), dir) == shouldDraw;
			}
		}
		
		// Create test cases and map of blocks
		ArrayList<TestCase> testCases = new ArrayList<>();
		LinkedHashMap<BlockPos, BlockState> blocks = new LinkedHashMap<>();
		int[] xOffsets = {2, 0, -2};
		for(int i = 0; i < 3; i++)
		{
			BlockPos backPos = startPos.offset(xOffsets[i], 0, 5);
			blocks.put(backPos, backBlocks[i]);
			// testCases.add(new TestCase(backPos, Direction.NORTH, drawFront));
			
			BlockPos frontPos = startPos.offset(xOffsets[i], 0, 4);
			blocks.put(frontPos, frontBlocks[i]);
			testCases.add(new TestCase(frontPos, Direction.SOUTH, drawBack));
		}
		
		// Run tests and return if successful
		TestCase failed =
			testCases.stream().filter(testCase -> !testCase.test(blocks))
				.findFirst().orElse(null);
		if(failed == null)
			return;
		
		// Build the test case and mark where it failed
		blocks.put(failed.pos().above(2),
			Blocks.RED_CONCRETE.defaultBlockState());
		setBlocks(server, blocks);
		if(failed.dir() == Direction.SOUTH)
			runCommand(server, "tp @s ~ ~ ~9 180 0");
		context
			.waitFor(mc -> blocks.entrySet().stream().allMatch(entry -> mc.level
				.getBlockState(entry.getKey()) == entry.getValue()));
		world.waitForChunksRender();
		
		// Take a screenshot
		String fileName = "glass_connected_incorrectly";
		Path screenshotPath = context.takeScreenshot(fileName);
		
		BlockState state1 = blocks.get(failed.pos());
		BlockState state2 = blocks.get(failed.pos().relative(failed.dir()));
		String errorMessage = "Block `" + state1 + "`"
			+ (failed.shouldDraw() ? " connected to " : " did not connect to ")
			+ "`" + state2 + "`";
		ghSummary("### Some glass pieces are not connecting correctly");
		ghSummary(errorMessage + ", see below:\n");
		
		String url = tryUploadToImgur(screenshotPath);
		if(url != null)
			ghSummary("![" + fileName + "](" + url + ")");
		else
			ghSummary("Couldn't upload " + fileName
				+ ".png to Imgur. Check the Test Screenshots.zip artifact.");
		
		throw new RuntimeException(errorMessage);
	}
	
	private static BlockState[] blocks()
	{
		return new BlockState[]{Blocks.GLASS.defaultBlockState(),
			Blocks.WHITE_STAINED_GLASS.defaultBlockState(),
			Blocks.TINTED_GLASS.defaultBlockState()};
	}
	
	private static BlockState[] slabs(SlabType type)
	{
		return new BlockState[]{
			MoGlassBlocks.GLASS_SLAB.defaultBlockState()
				.setValue(SlabBlock.TYPE, type),
			MoGlassBlocks.STAINED_GLASS_SLABS.get(DyeColor.WHITE.ordinal())
				.defaultBlockState().setValue(SlabBlock.TYPE, type),
			MoGlassBlocks.TINTED_GLASS_SLAB.defaultBlockState()
				.setValue(SlabBlock.TYPE, type)};
	}
	
	private static BlockState[] stairs(Direction facing, Half half,
		StairsShape shape)
	{
		return new BlockState[]{MoGlassBlocks.GLASS_STAIRS.defaultBlockState()
			.setValue(StairBlock.FACING, facing).setValue(StairBlock.HALF, half)
			.setValue(StairBlock.SHAPE, shape),
			MoGlassBlocks.STAINED_GLASS_STAIRS.get(DyeColor.WHITE.ordinal())
				.defaultBlockState().setValue(StairBlock.FACING, facing)
				.setValue(StairBlock.HALF, half)
				.setValue(StairBlock.SHAPE, shape),
			MoGlassBlocks.TINTED_GLASS_STAIRS.defaultBlockState()
				.setValue(StairBlock.FACING, facing)
				.setValue(StairBlock.HALF, half)
				.setValue(StairBlock.SHAPE, shape)};
	}
}
