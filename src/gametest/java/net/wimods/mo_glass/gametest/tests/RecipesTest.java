/*
 * Copyright (c) 2019-2026 Wurst-Imperium and contributors.
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
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SelectableRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.wimods.mo_glass.MoGlassBlocks;
import net.wimods.mo_glass.gametest.SingleplayerTest;

public final class RecipesTest extends SingleplayerTest
{
	public RecipesTest(ClientGameTestContext context,
		TestSingleplayerContext spContext)
	{
		super(context, spContext);
	}
	
	@Override
	protected void runImpl()
	{
		logger.info("Testing crafting/stonecutting recipes...");
		
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
	
	private void testRecipesForGlassType(ItemLike input, ItemLike slabOutput,
		ItemLike stairsOutput)
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
	
	private void assertCraftingRecipe(ItemStack[][] inputGrid,
		ItemStack expectedResult)
	{
		int width = inputGrid[0].length;
		int height = inputGrid.length;
		
		ArrayList<ItemStack> stacks = new ArrayList<>();
		for(ItemStack[] row : inputGrid)
			for(ItemStack item : row)
				stacks.add(item != null ? item : ItemStack.EMPTY);
			
		CraftingInput input =
			CraftingInput.ofPositioned(width, height, stacks).input();
		Optional<RecipeHolder<CraftingRecipe>> optional = context
			.computeOnClient(mc -> mc.getSingleplayerServer().getRecipeManager()
				.getRecipeFor(RecipeType.CRAFTING, input, mc.level));
		
		if(!optional.isPresent())
			fail("The following recipe for " + expectedResult + " is missing:\n"
				+ formatCraftingRecipe(inputGrid));
		
		RecipeHolder<CraftingRecipe> entry = optional.get();
		CraftingRecipe recipe = entry.value();
		ItemStack result = recipe.assemble(input);
		
		if(!ItemStack.matches(expectedResult, result))
			fail("Wrong crafting result: Expected " + expectedResult
				+ " but got " + result + " for recipe:\n"
				+ formatCraftingRecipe(inputGrid));
	}
	
	private String formatCraftingRecipe(ItemStack[][] inputGrid)
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
	
	private void assertStonecuttingRecipe(ItemStack input,
		ItemStack expectedResult)
	{
		SelectableRecipe.SingleInputSet<StonecutterRecipe> recipeGroups =
			context.computeOnClient(mc -> mc.getSingleplayerServer()
				.getRecipeManager().stonecutterRecipes().selectByInput(input));
		
		List<StonecutterRecipe> recipes = recipeGroups.entries().stream()
			.map(group -> group.recipe().recipe()).filter(Optional::isPresent)
			.map(Optional::get).map(RecipeHolder::value).toList();
		
		if(recipes.isEmpty())
			fail("No stonecutting recipes found for " + input);
		
		List<ItemStack> results = recipes.stream()
			.map(recipe -> recipe.assemble(new SingleRecipeInput(input)))
			.toList();
		
		if(!results.stream()
			.anyMatch(stack -> ItemStack.matches(stack, expectedResult)))
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
			case WHITE -> Blocks.STAINED_GLASS.white();
			case ORANGE -> Blocks.STAINED_GLASS.orange();
			case MAGENTA -> Blocks.STAINED_GLASS.magenta();
			case LIGHT_BLUE -> Blocks.STAINED_GLASS.lightBlue();
			case YELLOW -> Blocks.STAINED_GLASS.yellow();
			case LIME -> Blocks.STAINED_GLASS.lime();
			case PINK -> Blocks.STAINED_GLASS.pink();
			case GRAY -> Blocks.STAINED_GLASS.gray();
			case LIGHT_GRAY -> Blocks.STAINED_GLASS.lightGray();
			case CYAN -> Blocks.STAINED_GLASS.cyan();
			case PURPLE -> Blocks.STAINED_GLASS.purple();
			case BLUE -> Blocks.STAINED_GLASS.blue();
			case BROWN -> Blocks.STAINED_GLASS.brown();
			case GREEN -> Blocks.STAINED_GLASS.green();
			case RED -> Blocks.STAINED_GLASS.red();
			case BLACK -> Blocks.STAINED_GLASS.black();
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
