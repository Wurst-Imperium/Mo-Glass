/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass.test;

import static net.wimods.mo_glass.test.WiModsTestHelper.*;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.block.enums.StairShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.wimods.mo_glass.MoGlassBlocks;

public enum TintedGlassLightBlockingTest
{
	;
	
	public static void testTintedGlassBlocksLightCorrectly()
	{
		System.out.println("Testing tinted glass light blocking...");
		BlockPos playerPos = submitAndGet(mc -> mc.player.getBlockPos());
		BlockPos top = playerPos.add(0, 1, 4);
		BlockPos front = playerPos.add(0, 0, 3);
		
		// Build test rig
		runChatCommand("fill ~-1 ~ ~3 ~1 ~1 ~5 tinted_glass");
		runChatCommand("setblock ~ ~ ~4 air");
		clearChat();
		
		// Slab on top - should always block light
		for(SlabType type : SlabType.values())
			testSlab(top, type, 0);
		
		// Slab in front - depends on type
		testSlab(front, SlabType.BOTTOM, 13);
		testSlab(front, SlabType.TOP, 13);
		testSlab(front, SlabType.DOUBLE, 0);
		
		// Stairs on top - should always block light
		for(Direction dir : Direction.Type.HORIZONTAL)
			for(BlockHalf half : BlockHalf.values())
				for(StairShape shape : StairShape.values())
					testStairs(top, dir, half, shape, 0);
				
		// Straight stairs in front - depends on direction
		for(Direction dir : Direction.Type.HORIZONTAL)
		{
			int light = dir.getAxis() == Direction.Axis.X ? 13 : 0;
			for(BlockHalf half : BlockHalf.values())
				testStairs(front, dir, half, StairShape.STRAIGHT, light);
		}
		
		// Curved stairs in front - depends on curve type
		for(Direction dir : Direction.Type.HORIZONTAL)
			for(BlockHalf half : BlockHalf.values())
			{
				testStairs(front, dir, half, StairShape.INNER_LEFT, 0);
				testStairs(front, dir, half, StairShape.INNER_RIGHT, 0);
				testStairs(front, dir, half, StairShape.OUTER_LEFT, 13);
				testStairs(front, dir, half, StairShape.OUTER_RIGHT, 13);
			}
		
		// Clean up
		runChatCommand("fill ~-7 ~ ~-4 ~7 ~30 ~10 air");
		clearChat();
	}
	
	private static void testSlab(BlockPos pos, SlabType type, int light)
	{
		testConfiguration(pos, MoGlassBlocks.TINTED_GLASS_SLAB.getDefaultState()
			.with(SlabBlock.TYPE, type), light);
	}
	
	private static void testStairs(BlockPos pos, Direction facing,
		BlockHalf half, StairShape shape, int light)
	{
		testConfiguration(pos,
			MoGlassBlocks.TINTED_GLASS_STAIRS.getDefaultState()
				.with(StairsBlock.FACING, facing).with(StairsBlock.HALF, half)
				.with(StairsBlock.SHAPE, shape),
			light);
	}
	
	private static void testConfiguration(BlockPos pos, BlockState state,
		int expectedLightLevel)
	{
		setBlock(pos, state);
		waitUntil("block " + state + " is placed at " + pos,
			mc -> (mc.world.getBlockState(pos) == state
				&& !mc.world.getLightingProvider().hasUpdates()));
		assertLightLevel(0, 0, 4, expectedLightLevel);
		setBlock(pos, Blocks.TINTED_GLASS.getDefaultState());
	}
	
	private static void assertLightLevel(int x, int y, int z, int expected)
	{
		int lightLevel = submitAndGet(
			mc -> mc.world.getLightLevel(mc.player.getBlockPos().add(x, y, z)));
		
		if(lightLevel == expected)
			return;
		
		takeScreenshot("FAILED_TEST_expected_light_level_" + expected + "_got_"
			+ lightLevel);
		throw new RuntimeException("Wrong light level at ~" + x + " ~" + y
			+ " ~" + z + ": Expected " + expected + ", got " + lightLevel);
	}
}
