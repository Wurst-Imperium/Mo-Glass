/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass.test;

import static net.wurstclient.glass.test.WiModsTestHelper.*;

public enum TintedGlassLightBlockingTest
{
	;
	
	public static void testTintedGlassBlocksLightCorrectly()
	{
		System.out.println("Testing tinted glass light blocking...");
		String top = "~ ~1 ~4";
		String front = "~ ~ ~3";
		
		// Build test rig
		runChatCommand("fill ~-1 ~ ~3 ~1 ~1 ~5 tinted_glass");
		runChatCommand("setblock ~ ~ ~4 air");
		
		// Slab on top - should always block light
		testSlab(top, "bottom", 0);
		testSlab(top, "top", 0);
		testSlab(top, "double", 0);
		
		// Slab in front - depends on type
		testSlab(front, "bottom", 13);
		testSlab(front, "top", 13);
		testSlab(front, "double", 0);
		
		// Stairs on top - should always block light
		testStairs(top, "south", "bottom", "straight", 0);
		testStairs(top, "east", "bottom", "straight", 0);
		testStairs(top, "north", "bottom", "straight", 0);
		testStairs(top, "west", "bottom", "straight", 0);
		testStairs(top, "south", "top", "straight", 0);
		testStairs(top, "east", "top", "straight", 0);
		testStairs(top, "north", "top", "straight", 0);
		testStairs(top, "west", "top", "straight", 0);
		testStairs(top, "south", "bottom", "outer_left", 0);
		testStairs(top, "east", "bottom", "outer_left", 0);
		testStairs(top, "north", "bottom", "outer_left", 0);
		testStairs(top, "west", "bottom", "outer_left", 0);
		testStairs(top, "south", "top", "outer_left", 0);
		testStairs(top, "east", "top", "outer_left", 0);
		testStairs(top, "north", "top", "outer_left", 0);
		testStairs(top, "west", "top", "outer_left", 0);
		testStairs(top, "south", "bottom", "outer_right", 0);
		testStairs(top, "east", "bottom", "outer_right", 0);
		testStairs(top, "north", "bottom", "outer_right", 0);
		testStairs(top, "west", "bottom", "outer_right", 0);
		testStairs(top, "south", "top", "outer_right", 0);
		testStairs(top, "east", "top", "outer_right", 0);
		testStairs(top, "north", "top", "outer_right", 0);
		testStairs(top, "west", "top", "outer_right", 0);
		testStairs(top, "south", "bottom", "inner_left", 0);
		testStairs(top, "east", "bottom", "inner_left", 0);
		testStairs(top, "north", "bottom", "inner_left", 0);
		testStairs(top, "west", "bottom", "inner_left", 0);
		testStairs(top, "south", "top", "inner_left", 0);
		testStairs(top, "east", "top", "inner_left", 0);
		testStairs(top, "north", "top", "inner_left", 0);
		testStairs(top, "west", "top", "inner_left", 0);
		testStairs(top, "south", "bottom", "inner_right", 0);
		testStairs(top, "east", "bottom", "inner_right", 0);
		testStairs(top, "north", "bottom", "inner_right", 0);
		testStairs(top, "west", "bottom", "inner_right", 0);
		testStairs(top, "south", "top", "inner_right", 0);
		testStairs(top, "east", "top", "inner_right", 0);
		testStairs(top, "north", "top", "inner_right", 0);
		testStairs(top, "west", "top", "inner_right", 0);
		
		// Straight stairs in front - depends on facing
		testStairs(front, "south", "bottom", "straight", 0);
		testStairs(front, "east", "bottom", "straight", 13);
		testStairs(front, "north", "bottom", "straight", 0);
		testStairs(front, "west", "bottom", "straight", 13);
		testStairs(front, "south", "top", "straight", 0);
		testStairs(front, "east", "top", "straight", 13);
		testStairs(front, "north", "top", "straight", 0);
		testStairs(front, "west", "top", "straight", 13);
		
		// Outer stairs in front - should always let light through
		testStairs(front, "south", "bottom", "outer_left", 13);
		testStairs(front, "east", "bottom", "outer_left", 13);
		testStairs(front, "north", "bottom", "outer_left", 13);
		testStairs(front, "west", "bottom", "outer_left", 13);
		testStairs(front, "south", "top", "outer_left", 13);
		testStairs(front, "east", "top", "outer_left", 13);
		testStairs(front, "north", "top", "outer_left", 13);
		testStairs(front, "west", "top", "outer_left", 13);
		testStairs(front, "south", "bottom", "outer_right", 13);
		testStairs(front, "east", "bottom", "outer_right", 13);
		testStairs(front, "north", "bottom", "outer_right", 13);
		testStairs(front, "west", "bottom", "outer_right", 13);
		testStairs(front, "south", "top", "outer_right", 13);
		testStairs(front, "east", "top", "outer_right", 13);
		testStairs(front, "north", "top", "outer_right", 13);
		testStairs(front, "west", "top", "outer_right", 13);
		
		// Inner stairs in front - should always block light
		testStairs(front, "south", "bottom", "inner_left", 0);
		testStairs(front, "east", "bottom", "inner_left", 0);
		testStairs(front, "north", "bottom", "inner_left", 0);
		testStairs(front, "west", "bottom", "inner_left", 0);
		testStairs(front, "south", "top", "inner_left", 0);
		testStairs(front, "east", "top", "inner_left", 0);
		testStairs(front, "north", "top", "inner_left", 0);
		testStairs(front, "west", "top", "inner_left", 0);
		testStairs(front, "south", "bottom", "inner_right", 0);
		testStairs(front, "east", "bottom", "inner_right", 0);
		testStairs(front, "north", "bottom", "inner_right", 0);
		testStairs(front, "west", "bottom", "inner_right", 0);
		testStairs(front, "south", "top", "inner_right", 0);
		testStairs(front, "east", "top", "inner_right", 0);
		testStairs(front, "north", "top", "inner_right", 0);
		testStairs(front, "west", "top", "inner_right", 0);
		
		// Clean up
		runChatCommand("fill ~-1 ~ ~3 ~1 ~1 ~5 air");
		clearChat();
	}
	
	private static void testSlab(String position, String type, int light)
	{
		testConfiguration(position, "tinted_glass_slab[type=" + type + "]",
			light);
	}
	
	private static void testStairs(String position, String facing, String half,
		String shape, int light)
	{
		testConfiguration(position, "tinted_glass_stairs[facing=" + facing
			+ ",half=" + half + ",shape=" + shape + "]", light);
	}
	
	private static void testConfiguration(String position, String block,
		int expectedLightLevel)
	{
		runChatCommand("setblock " + position + " mo_glass:" + block);
		clearChat();
		assertLightLevel(0, 0, 4, expectedLightLevel);
		runChatCommand("setblock " + position + " tinted_glass");
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
