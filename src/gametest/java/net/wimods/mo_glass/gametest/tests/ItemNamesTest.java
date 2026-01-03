/*
 * Copyright (c) 2019-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass.gametest.tests;

import static net.wimods.mo_glass.gametest.WiModsTestHelper.*;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.wimods.mo_glass.MoGlassBlocks;
import net.wimods.mo_glass.gametest.MoGlassTest;

public enum ItemNamesTest
{
	;
	
	public static void testItemNamesShowUpCorrectly()
	{
		MoGlassTest.LOGGER.info("Testing item names...");
		
		assertItemName("Glass Slab", new ItemStack(MoGlassBlocks.GLASS_SLAB));
		assertItemName("Glass Stairs",
			new ItemStack(MoGlassBlocks.GLASS_STAIRS));
		
		assertItemName("Tinted Glass Slab",
			new ItemStack(MoGlassBlocks.TINTED_GLASS_SLAB));
		assertItemName("Tinted Glass Stairs",
			new ItemStack(MoGlassBlocks.TINTED_GLASS_STAIRS));
		
		String[] colors = {"White", "Orange", "Magenta", "Light Blue", "Yellow",
			"Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Blue",
			"Brown", "Green", "Red", "Black"};
		for(int i = 0; i < colors.length; i++)
		{
			assertItemName(colors[i] + " Stained Glass Slab",
				new ItemStack(MoGlassBlocks.STAINED_GLASS_SLABS.get(i)));
			assertItemName(colors[i] + " Stained Glass Stairs",
				new ItemStack(MoGlassBlocks.STAINED_GLASS_STAIRS.get(i)));
		}
	}
	
	private static void assertItemName(String expected, ItemStack stack)
	{
		Component actual = stack.getHoverName();
		String actualString = actual.getString();
		if(expected.equals(actualString))
			return;
		
		String errorMessage = "Wrong item name: Expected `" + expected
			+ "` but got `" + actualString + "` (`" + actual + "`)";
		ghSummary("### One or more item names aren't showing up correctly");
		ghSummary(errorMessage);
		throw new RuntimeException(errorMessage);
	}
}
