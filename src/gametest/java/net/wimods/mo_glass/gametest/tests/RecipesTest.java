/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass.gametest.tests;

import static net.wimods.mo_glass.gametest.WiModsTestHelper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.StonecuttingRecipe;
import net.minecraft.recipe.display.CuttingRecipeDisplay;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.util.DyeColor;
import net.wimods.mo_glass.gametest.MoGlassTest;
import net.wurstclient.glass.MoGlassBlocks;

public enum RecipesTest
{
	;
	
	public static void testRecipesWork(ClientGameTestContext context)
	{
		MoGlassTest.LOGGER.info("Testing crafting/stonecutting recipes...");
		
		// normal glass
		testRecipesForGlassType(context, Blocks.GLASS, MoGlassBlocks.GLASS_SLAB,
			MoGlassBlocks.GLASS_STAIRS);
		
		// tinted glass
		testRecipesForGlassType(context, Blocks.TINTED_GLASS,
			MoGlassBlocks.TINTED_GLASS_SLAB, MoGlassBlocks.TINTED_GLASS_STAIRS);
		
		// stained glass
		for(DyeColor color : DyeColor.values())
		{
			Block block = getStainedGlassBlock(color);
			Block slab = MoGlassBlocks.STAINED_GLASS_SLABS.get(color.ordinal());
			Block stairs =
				MoGlassBlocks.STAINED_GLASS_STAIRS.get(color.ordinal());
			testRecipesForGlassType(context, block, slab, stairs);
		}
	}
	
	private static void testRecipesForGlassType(ClientGameTestContext context,
		ItemConvertible input, ItemConvertible slabOutput,
		ItemConvertible stairsOutput)
	{
		// slab crafting
		assertCraftingRecipe(
			context, new ItemStack[][]{{new ItemStack(input),
				new ItemStack(input), new ItemStack(input)}},
			new ItemStack(slabOutput, 6));
		
		// stairs crafting
		assertCraftingRecipe(context, new ItemStack[][]{
			{new ItemStack(input), null, null},
			{new ItemStack(input), new ItemStack(input), null},
			{new ItemStack(input), new ItemStack(input), new ItemStack(input)}},
			new ItemStack(stairsOutput, 4));
		
		// slab stonecutting
		assertStonecuttingRecipe(context, new ItemStack(input),
			new ItemStack(slabOutput, 2));
		
		// stairs stonecutting
		assertStonecuttingRecipe(context, new ItemStack(input),
			new ItemStack(stairsOutput, 1));
	}
	
	private static void assertCraftingRecipe(ClientGameTestContext context,
		ItemStack[][] inputGrid, ItemStack expectedResult)
	{
		int width = inputGrid[0].length;
		int height = inputGrid.length;
		
		ArrayList<ItemStack> stacks = new ArrayList<>();
		for(ItemStack[] row : inputGrid)
			for(ItemStack item : row)
				stacks.add(item != null ? item : ItemStack.EMPTY);
			
		CraftingRecipeInput input =
			CraftingRecipeInput.createPositioned(width, height, stacks).input();
		Optional<RecipeEntry<CraftingRecipe>> optional =
			context.computeOnClient(mc -> mc.getServer().getRecipeManager()
				.getFirstMatch(RecipeType.CRAFTING, input, mc.world));
		
		if(!optional.isPresent())
			fail("The following recipe for " + expectedResult + " is missing:\n"
				+ formatCraftingRecipe(inputGrid));
		
		RecipeEntry<CraftingRecipe> entry = optional.get();
		CraftingRecipe recipe = entry.value();
		ItemStack result = context.computeOnClient(
			mc -> recipe.craft(input, mc.world.getRegistryManager()));
		
		if(!ItemStack.areEqual(expectedResult, result))
			fail("Wrong crafting result: Expected " + expectedResult
				+ " but got " + result + " for recipe:\n"
				+ formatCraftingRecipe(inputGrid));
	}
	
	private static String formatCraftingRecipe(ItemStack[][] inputGrid)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("```\n");
		for(ItemStack[] row : inputGrid)
		{
			for(int i = 0; i < row.length; i++)
				builder.append(i > 0 ? ", " : "[ ").append(row[i]);
			
			builder.append(" ]\n");
		}
		builder.append("```");
		return builder.toString();
	}
	
	private static void assertStonecuttingRecipe(ClientGameTestContext context,
		ItemStack input, ItemStack expectedResult)
	{
		CuttingRecipeDisplay.Grouping<StonecuttingRecipe> recipeGroups =
			context.computeOnClient(mc -> mc.getServer().getRecipeManager()
				.getStonecutterRecipes().filter(input));
		
		List<StonecuttingRecipe> recipes = recipeGroups.entries().stream()
			.map(group -> group.recipe().recipe()).filter(Optional::isPresent)
			.map(Optional::get).map(RecipeEntry::value).toList();
		
		if(recipes.isEmpty())
			fail("No stonecutting recipes found for " + input);
		
		List<ItemStack> results = context.computeOnClient(mc -> recipes.stream()
			.map(recipe -> recipe.craft(new SingleStackRecipeInput(input),
				mc.world.getRegistryManager()))
			.toList());
		
		if(!results.stream()
			.anyMatch(stack -> ItemStack.areEqual(stack, expectedResult)))
			fail("No stonecutting recipe found for " + input + " -> "
				+ expectedResult + ", only found recipes that result in "
				+ String.join(", ",
					results.stream().map(ItemStack::toString).toList()));
	}
	
	// As of 1.21.10, vanilla Minecraft doesn't seem to have a method like this.
	private static Block getStainedGlassBlock(DyeColor color)
	{
		return switch(color)
		{
			case WHITE -> Blocks.WHITE_STAINED_GLASS;
			case ORANGE -> Blocks.ORANGE_STAINED_GLASS;
			case MAGENTA -> Blocks.MAGENTA_STAINED_GLASS;
			case LIGHT_BLUE -> Blocks.LIGHT_BLUE_STAINED_GLASS;
			case YELLOW -> Blocks.YELLOW_STAINED_GLASS;
			case LIME -> Blocks.LIME_STAINED_GLASS;
			case PINK -> Blocks.PINK_STAINED_GLASS;
			case GRAY -> Blocks.GRAY_STAINED_GLASS;
			case LIGHT_GRAY -> Blocks.LIGHT_GRAY_STAINED_GLASS;
			case CYAN -> Blocks.CYAN_STAINED_GLASS;
			case PURPLE -> Blocks.PURPLE_STAINED_GLASS;
			case BLUE -> Blocks.BLUE_STAINED_GLASS;
			case BROWN -> Blocks.BROWN_STAINED_GLASS;
			case GREEN -> Blocks.GREEN_STAINED_GLASS;
			case RED -> Blocks.RED_STAINED_GLASS;
			case BLACK -> Blocks.BLACK_STAINED_GLASS;
		};
	}
	
	private static void fail(String message)
	{
		ghSummary(
			"### One or more crafting/stonecutting recipes aren't working");
		ghSummary(message);
		throw new RuntimeException(message);
	}
}
