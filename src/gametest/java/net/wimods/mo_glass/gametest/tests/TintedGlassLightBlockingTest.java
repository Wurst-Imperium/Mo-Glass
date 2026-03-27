/*
 * Copyright (c) 2019-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass.gametest.tests;

import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
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
import net.wimods.mo_glass.gametest.SingleplayerTest;

public final class TintedGlassLightBlockingTest extends SingleplayerTest
{
	public TintedGlassLightBlockingTest(ClientGameTestContext context,
		TestSingleplayerContext spContext)
	{
		super(context, spContext);
	}
	
	@Override
	protected void runImpl()
	{
		logger.info("Testing tinted glass light blocking...");
		BlockPos playerPos =
			context.computeOnClient(mc -> mc.player.blockPosition());
		BlockPos top = playerPos.offset(0, 1, 4);
		BlockPos front = playerPos.offset(0, 0, 3);
		
		// Build test rig
		runCommand("fill ~-1 ~-1 ~3 ~1 ~-1 ~5 smooth_stone");
		runCommand("fill ~-1 ~ ~3 ~1 ~1 ~5 tinted_glass");
		runCommand("setblock ~ ~ ~4 air");
		
		// Slab on top - should always block light
		for(SlabType type : SlabType.values())
			testSlab(top, type, 0);
		
		// Slab in front - depends on type
		testSlab(front, SlabType.BOTTOM, 13);
		testSlab(front, SlabType.TOP, 13);
		testSlab(front, SlabType.DOUBLE, 0);
		
		// Stairs on top - should always block light
		for(Direction dir : Direction.Plane.HORIZONTAL)
			for(Half half : Half.values())
				for(StairsShape shape : StairsShape.values())
					testStairs(top, dir, half, shape, 0);
				
		// Straight stairs in front - depends on direction
		for(Direction dir : Direction.Plane.HORIZONTAL)
		{
			int light = dir.getAxis() == Direction.Axis.X ? 13 : 0;
			for(Half half : Half.values())
				testStairs(front, dir, half, StairsShape.STRAIGHT, light);
		}
		
		// Curved stairs in front - depends on curve type
		for(Direction dir : Direction.Plane.HORIZONTAL)
			for(Half half : Half.values())
			{
				testStairs(front, dir, half, StairsShape.INNER_LEFT, 0);
				testStairs(front, dir, half, StairsShape.INNER_RIGHT, 0);
				testStairs(front, dir, half, StairsShape.OUTER_LEFT, 13);
				testStairs(front, dir, half, StairsShape.OUTER_RIGHT, 13);
			}
		
		// Clean up
		runCommand("fill ~-7 ~-1 ~3 ~7 ~10 ~9 air");
		context.waitTicks(2);
		world.waitForChunksRender();
	}
	
	private void testSlab(BlockPos pos, SlabType type, int light)
	{
		testConfiguration(pos, MoGlassBlocks.TINTED_GLASS_SLAB
			.defaultBlockState().setValue(SlabBlock.TYPE, type), light);
	}
	
	private void testStairs(BlockPos pos, Direction facing, Half half,
		StairsShape shape, int light)
	{
		testConfiguration(pos, MoGlassBlocks.TINTED_GLASS_STAIRS
			.defaultBlockState().setValue(StairBlock.FACING, facing)
			.setValue(StairBlock.HALF, half).setValue(StairBlock.SHAPE, shape),
			light);
	}
	
	private void testConfiguration(BlockPos pos, BlockState state,
		int expectedLightLevel)
	{
		setBlock(pos, state);
		context.waitFor(mc -> (mc.level.getBlockState(pos) == state
			&& !mc.level.getLightEngine().hasLightWork()));
		assertLightLevel(0, 0, 4, expectedLightLevel, state);
		setBlock(pos, Blocks.TINTED_GLASS.defaultBlockState());
	}
	
	private void assertLightLevel(int x, int y, int z, int expected,
		BlockState state)
	{
		int lightLevel =
			context.computeOnClient(mc -> mc.level.getMaxLocalRawBrightness(
				mc.player.blockPosition().offset(x, y, z)));
		
		if(lightLevel == expected)
			return;
		
		world.waitForChunksRender();
		failWithScreenshot("wrong_light_level",
			"Some tinted glass pieces are not blocking light correctly",
			"Wrong light level at ~" + x + " ~" + y + " ~" + z + ": Expected "
				+ expected + ", got " + lightLevel + " (`" + state + "`)");
	}
}
