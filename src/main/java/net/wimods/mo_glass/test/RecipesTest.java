/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass.test;

import static net.wimods.mo_glass.test.WiModsTestHelper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import net.wimods.mo_glass.MoGlassBlocks;

public enum RecipesTest
{
	;
	
	public static void testRecipesWork()
	{
		System.out.println("Testing crafting/stonecutting recipes...");
		
		// normal glass
		testRecipesForGlassType(Blocks.GLASS, MoGlassBlocks.GLASS_SLAB,
			MoGlassBlocks.GLASS_STAIRS);
		
		// tinted glass
		testRecipesForGlassType(Blocks.TINTED_GLASS,
			MoGlassBlocks.TINTED_GLASS_SLAB, MoGlassBlocks.TINTED_GLASS_STAIRS);
		
		// stained glass
		for(DyeColor color : DyeColor.values())
		{
			Block block = getStainedGlassBlock(color);
			Block slab = MoGlassBlocks.STAINED_GLASS_SLABS.get(color.ordinal());
			Block stairs =
				MoGlassBlocks.STAINED_GLASS_STAIRS.get(color.ordinal());
			testRecipesForGlassType(block, slab, stairs);
		}
	}
	
	private static void testRecipesForGlassType(ItemConvertible input,
		ItemConvertible slabOutput, ItemConvertible stairsOutput)
	{
		// slab crafting
		assertCraftingRecipe(new ItemStack[][]{
			{new ItemStack(input), new ItemStack(input), new ItemStack(input)}},
			new ItemStack(slabOutput, 6));
		
		// stairs crafting
		assertCraftingRecipe(new ItemStack[][]{
			{new ItemStack(input), null, null},
			{new ItemStack(input), new ItemStack(input), null},
			{new ItemStack(input), new ItemStack(input), new ItemStack(input)}},
			new ItemStack(stairsOutput, 4));
		
		// slab stonecutting
		assertStonecuttingRecipe(new ItemStack(input),
			new ItemStack(slabOutput, 2));
		
		// stairs stonecutting
		assertStonecuttingRecipe(new ItemStack(input),
			new ItemStack(stairsOutput, 1));
	}
	
	private static void assertCraftingRecipe(ItemStack[][] inputGrid,
		ItemStack expectedResult)
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
			submitAndGet(mc -> mc.getServer().getRecipeManager()
				.getFirstMatch(RecipeType.CRAFTING, input, mc.world));
		
		if(!optional.isPresent())
			throw new RuntimeException(
				"No crafting recipe found for " + expectedResult);
		
		RecipeEntry<CraftingRecipe> entry = optional.get();
		CraftingRecipe recipe = entry.value();
		ItemStack result = submitAndGet(
			mc -> recipe.craft(input, mc.world.getRegistryManager()));
		
		if(!ItemStack.areEqual(expectedResult, result))
			throw new RuntimeException("Wrong crafting result: Expected "
				+ expectedResult + " but got " + result);
	}
	
	private static void assertStonecuttingRecipe(ItemStack input,
		ItemStack expectedResult)
	{
		CuttingRecipeDisplay.Grouping<StonecuttingRecipe> recipeGroups =
			submitAndGet(mc -> mc.getServer().getRecipeManager()
				.getStonecutterRecipes().filter(input));
		
		List<StonecuttingRecipe> recipes = recipeGroups.entries().stream()
			.map(group -> group.recipe().recipe()).filter(Optional::isPresent)
			.map(Optional::get).map(RecipeEntry::value).toList();
		
		if(recipes.isEmpty())
			throw new RuntimeException(
				"No stonecutting recipes found for " + input);
		
		List<ItemStack> results = submitAndGet(mc -> recipes.stream()
			.map(recipe -> recipe.craft(new SingleStackRecipeInput(input),
				mc.world.getRegistryManager()))
			.toList());
		
		if(!results.stream()
			.anyMatch(stack -> ItemStack.areEqual(stack, expectedResult)))
			throw new RuntimeException("No stonecutting recipe found for "
				+ input + " -> " + expectedResult
				+ ", only found recipes that result in " + String.join(", ",
					results.stream().map(ItemStack::toString).toList()));
	}
	
	// As of 1.21.4, vanilla Minecraft doesn't seem to have a method like this.
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
}
