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

import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestClientLevelContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.wimods.mo_glass.MoGlassBlocks;
import net.wimods.mo_glass.gametest.MoGlassTest;

public enum TintedGlassLightBlockingTest
{
	;
	
	public static void testTintedGlassBlocksLightCorrectly(
		ClientGameTestContext context, TestSingleplayerContext spContext)
	{
		TestClientLevelContext world = spContext.getClientLevel();
		TestServerContext server = spContext.getServer();
		
		MoGlassTest.LOGGER.info("Testing tinted glass light blocking...");
		BlockPos playerPos =
			context.computeOnClient(mc -> mc.player.blockPosition());
		BlockPos top = playerPos.offset(0, 1, 4);
		BlockPos front = playerPos.offset(0, 0, 3);
		
		// Build test rig
		runCommand(server, "fill ~-1 ~-1 ~3 ~1 ~-1 ~5 smooth_stone");
		runCommand(server, "fill ~-1 ~ ~3 ~1 ~1 ~5 tinted_glass");
		runCommand(server, "setblock ~ ~ ~4 air");
		
		// Slab on top - should always block light
		for(SlabType type : SlabType.values())
			testSlab(context, spContext, top, type, 0);
		
		// Slab in front - depends on type
		testSlab(context, spContext, front, SlabType.BOTTOM, 13);
		testSlab(context, spContext, front, SlabType.TOP, 13);
		testSlab(context, spContext, front, SlabType.DOUBLE, 0);
		
		// Stairs on top - should always block light
		for(Direction dir : Direction.Plane.HORIZONTAL)
			for(Half half : Half.values())
				for(StairsShape shape : StairsShape.values())
					testStairs(context, spContext, top, dir, half, shape, 0);
				
		// Straight stairs in front - depends on direction
		for(Direction dir : Direction.Plane.HORIZONTAL)
		{
			int light = dir.getAxis() == Direction.Axis.X ? 13 : 0;
			for(Half half : Half.values())
				testStairs(context, spContext, front, dir, half,
					StairsShape.STRAIGHT, light);
		}
		
		// Curved stairs in front - depends on curve type
		for(Direction dir : Direction.Plane.HORIZONTAL)
			for(Half half : Half.values())
			{
				testStairs(context, spContext, front, dir, half,
					StairsShape.INNER_LEFT, 0);
				testStairs(context, spContext, front, dir, half,
					StairsShape.INNER_RIGHT, 0);
				testStairs(context, spContext, front, dir, half,
					StairsShape.OUTER_LEFT, 13);
				testStairs(context, spContext, front, dir, half,
					StairsShape.OUTER_RIGHT, 13);
			}
		
		// Clean up
		runCommand(server, "fill ~-7 ~-1 ~3 ~7 ~10 ~9 air");
		context.waitTicks(2);
		world.waitForChunksRender();
	}
	
	private static void testSlab(ClientGameTestContext context,
		TestSingleplayerContext spContext, BlockPos pos, SlabType type,
		int light)
	{
		testConfiguration(context, spContext, pos,
			MoGlassBlocks.TINTED_GLASS_SLAB.defaultBlockState()
				.setValue(SlabBlock.TYPE, type),
			light);
	}
	
	private static void testStairs(ClientGameTestContext context,
		TestSingleplayerContext spContext, BlockPos pos, Direction facing,
		Half half, StairsShape shape, int light)
	{
		testConfiguration(context, spContext, pos,
			MoGlassBlocks.TINTED_GLASS_STAIRS.defaultBlockState()
				.setValue(StairBlock.FACING, facing)
				.setValue(StairBlock.HALF, half)
				.setValue(StairBlock.SHAPE, shape),
			light);
	}
	
	private static void testConfiguration(ClientGameTestContext context,
		TestSingleplayerContext spContext, BlockPos pos, BlockState state,
		int expectedLightLevel)
	{
		TestServerContext server = spContext.getServer();
		
		setBlock(server, pos, state);
		context.waitFor(mc -> (mc.level.getBlockState(pos) == state
			&& !mc.level.getLightEngine().hasLightWork()));
		assertLightLevel(context, spContext, 0, 0, 4, expectedLightLevel,
			state);
		setBlock(server, pos, Blocks.TINTED_GLASS.defaultBlockState());
	}
	
	private static void assertLightLevel(ClientGameTestContext context,
		TestSingleplayerContext spContext, int x, int y, int z, int expected,
		BlockState state)
	{
		int lightLevel =
			context.computeOnClient(mc -> mc.level.getMaxLocalRawBrightness(
				mc.player.blockPosition().offset(x, y, z)));
		
		if(lightLevel == expected)
			return;
		
		TestClientLevelContext world = spContext.getClientLevel();
		world.waitForChunksRender();
		String fileName = "wrong_light_level";
		Path screenshotPath = context.takeScreenshot(fileName);
		
		String errorMessage = "Wrong light level at ~" + x + " ~" + y + " ~" + z
			+ ": Expected " + expected + ", got " + lightLevel;
		ghSummary(
			"### Some tinted glass pieces are not blocking light correctly");
		ghSummary(errorMessage + " (`" + state + "`)\n");
		String url = tryUploadToImgur(screenshotPath);
		if(url != null)
			ghSummary("![" + fileName + "](" + url + ")");
		else
			ghSummary("Couldn't upload " + fileName
				+ ".png to Imgur. Check the Test Screenshots.zip artifact.");
		throw new RuntimeException(errorMessage);
	}
}
