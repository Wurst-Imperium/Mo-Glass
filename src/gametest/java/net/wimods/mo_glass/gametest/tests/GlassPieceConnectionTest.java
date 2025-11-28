/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
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
import net.wimods.mo_glass.gametest.MoGlassTest;
import net.wurstclient.glass.MoGlassBlocks;

public enum GlassPieceConnectionTest
{
	;
	
	public static void testGlassPiecesConnectCorrectly(
		ClientGameTestContext context, TestSingleplayerContext spContext)
	{
		TestClientWorldContext world = spContext.getClientWorld();
		TestServerContext server = spContext.getServer();
		
		MoGlassTest.LOGGER.info("Testing if glass pieces connect correctly...");
		BlockPos pos = context.computeOnClient(mc -> mc.player.getBlockPos());
		
		BlockState[][] blocksWithFullyCoveredNorthSide = {blocks(),
			slabs(SlabType.DOUBLE),
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
			test(context, spContext, pos, back, blocks(), false, false);
			test(context, spContext, pos, back, slabs(SlabType.BOTTOM), true,
				false);
			test(context, spContext, pos, back, slabs(SlabType.TOP), true,
				false);
			test(context, spContext, pos, back, slabs(SlabType.DOUBLE), false,
				false);
			for(Direction dir : Direction.Type.HORIZONTAL)
				for(BlockHalf half : BlockHalf.values())
				{
					test(context, spContext, pos, back,
						stairs(dir, half, StairShape.STRAIGHT),
						dir != Direction.SOUTH, false);
					test(context, spContext, pos, back,
						stairs(dir, half, StairShape.INNER_LEFT),
						dir != Direction.SOUTH && dir != Direction.WEST, false);
					test(context, spContext, pos, back,
						stairs(dir, half, StairShape.INNER_RIGHT),
						dir != Direction.SOUTH && dir != Direction.EAST, false);
					test(context, spContext, pos, back,
						stairs(dir, half, StairShape.OUTER_LEFT), true, false);
					test(context, spContext, pos, back,
						stairs(dir, half, StairShape.OUTER_RIGHT), true, false);
				}
		}
		
		BlockState[][] blocksWithBottomHalfCoveredNorthSide = {
			slabs(SlabType.BOTTOM),
			stairs(Direction.SOUTH, BlockHalf.BOTTOM, StairShape.STRAIGHT),
			stairs(Direction.SOUTH, BlockHalf.BOTTOM, StairShape.OUTER_LEFT),
			stairs(Direction.WEST, BlockHalf.BOTTOM, StairShape.OUTER_LEFT),
			stairs(Direction.EAST, BlockHalf.BOTTOM, StairShape.OUTER_RIGHT),
			stairs(Direction.SOUTH, BlockHalf.BOTTOM, StairShape.OUTER_RIGHT)};
		
		for(BlockState[] back : blocksWithBottomHalfCoveredNorthSide)
		{
			test(context, spContext, pos, back, slabs(SlabType.BOTTOM), false,
				false);
			test(context, spContext, pos, back, slabs(SlabType.TOP), true,
				true);
			for(BlockHalf half : BlockHalf.values())
			{
				test(context, spContext, pos, back,
					stairs(Direction.NORTH, half, StairShape.STRAIGHT),
					half == BlockHalf.TOP, half == BlockHalf.TOP);
				test(context, spContext, pos, back,
					stairs(Direction.EAST, half, StairShape.STRAIGHT),
					half == BlockHalf.TOP, true);
				test(context, spContext, pos, back,
					stairs(Direction.WEST, half, StairShape.STRAIGHT),
					half == BlockHalf.TOP, true);
				
				test(context, spContext, pos, back,
					stairs(Direction.NORTH, half, StairShape.INNER_LEFT),
					half == BlockHalf.TOP, true);
				test(context, spContext, pos, back,
					stairs(Direction.EAST, half, StairShape.INNER_LEFT),
					half == BlockHalf.TOP, true);
				test(context, spContext, pos, back,
					stairs(Direction.NORTH, half, StairShape.INNER_RIGHT),
					half == BlockHalf.TOP, true);
				test(context, spContext, pos, back,
					stairs(Direction.WEST, half, StairShape.INNER_RIGHT),
					half == BlockHalf.TOP, true);
				
				test(context, spContext, pos, back,
					stairs(Direction.NORTH, half, StairShape.OUTER_LEFT),
					half == BlockHalf.TOP, half == BlockHalf.TOP);
				test(context, spContext, pos, back,
					stairs(Direction.EAST, half, StairShape.OUTER_LEFT),
					half == BlockHalf.TOP, half == BlockHalf.TOP);
				test(context, spContext, pos, back,
					stairs(Direction.SOUTH, half, StairShape.OUTER_LEFT),
					half == BlockHalf.TOP, true);
				test(context, spContext, pos, back,
					stairs(Direction.WEST, half, StairShape.OUTER_LEFT),
					half == BlockHalf.TOP, true);
				
				test(context, spContext, pos, back,
					stairs(Direction.NORTH, half, StairShape.OUTER_RIGHT),
					half == BlockHalf.TOP, half == BlockHalf.TOP);
				test(context, spContext, pos, back,
					stairs(Direction.EAST, half, StairShape.OUTER_RIGHT),
					half == BlockHalf.TOP, true);
				test(context, spContext, pos, back,
					stairs(Direction.SOUTH, half, StairShape.OUTER_RIGHT),
					half == BlockHalf.TOP, true);
				test(context, spContext, pos, back,
					stairs(Direction.WEST, half, StairShape.OUTER_RIGHT),
					half == BlockHalf.TOP, half == BlockHalf.TOP);
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
				return Block.shouldDrawSide(blocks.get(pos),
					blocks.get(pos.offset(dir)), dir) == shouldDraw;
			}
		}
		
		// Create test cases and map of blocks
		ArrayList<TestCase> testCases = new ArrayList<>();
		LinkedHashMap<BlockPos, BlockState> blocks = new LinkedHashMap<>();
		int[] xOffsets = {2, 0, -2};
		for(int i = 0; i < 3; i++)
		{
			BlockPos backPos = startPos.add(xOffsets[i], 0, 5);
			blocks.put(backPos, backBlocks[i]);
			// testCases.add(new TestCase(backPos, Direction.NORTH, drawFront));
			
			BlockPos frontPos = startPos.add(xOffsets[i], 0, 4);
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
		blocks.put(failed.pos().up(2), Blocks.RED_CONCRETE.getDefaultState());
		setBlocks(server, blocks);
		if(failed.dir() == Direction.SOUTH)
			runCommand(server, "tp @s ~ ~ ~9 180 0");
		context
			.waitFor(mc -> blocks.entrySet().stream().allMatch(entry -> mc.world
				.getBlockState(entry.getKey()) == entry.getValue()));
		world.waitForChunksRender();
		
		// Take a screenshot
		String fileName = "glass_connected_incorrectly";
		Path screenshotPath = context.takeScreenshot(fileName);
		
		BlockState state1 = blocks.get(failed.pos());
		BlockState state2 = blocks.get(failed.pos().offset(failed.dir()));
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
