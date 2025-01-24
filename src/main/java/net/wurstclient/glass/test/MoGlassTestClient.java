/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass.test;

import static net.wurstclient.glass.test.WiModsTestHelper.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.MixinEnvironment;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.AccessibilityOnboardingScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.resource.language.I18n;
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
import net.wurstclient.glass.MoGlassBlocks;

public final class MoGlassTestClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		if(System.getProperty("mo_glass.e2eTest") == null)
			return;
		
		Thread.ofVirtual().name("Mo Glass End-to-End Test")
			.uncaughtExceptionHandler((t, e) -> {
				e.printStackTrace();
				System.exit(1);
			}).start(this::runTests);
	}
	
	private void runTests()
	{
		System.out.println("Starting Mo Glass End-to-End Test");
		waitForResourceLoading();
		
		if(submitAndGet(mc -> mc.options.onboardAccessibility))
		{
			System.out.println("Onboarding is enabled. Waiting for it");
			waitForScreen(AccessibilityOnboardingScreen.class);
			System.out.println("Reached onboarding screen");
			clickButton("gui.continue");
		}
		
		waitForScreen(TitleScreen.class);
		waitForTitleScreenFade();
		System.out.println("Reached title screen");
		takeScreenshot("title_screen", Duration.ZERO);
		
		System.out.println("Clicking singleplayer button");
		clickButton("menu.singleplayer");
		
		if(submitAndGet(mc -> !mc.getLevelStorage().getLevelList().isEmpty()))
		{
			System.out.println("World list is not empty. Waiting for it");
			waitForScreen(SelectWorldScreen.class);
			System.out.println("Reached select world screen");
			takeScreenshot("select_world_screen");
			clickButton("selectWorld.create");
		}
		
		waitForScreen(CreateWorldScreen.class);
		System.out.println("Reached create world screen");
		
		// Set MC version as world name
		setTextFieldText(0,
			"E2E Test " + SharedConstants.getGameVersion().getName());
		// Select creative mode
		clickButton("selectWorld.gameMode");
		clickButton("selectWorld.gameMode");
		takeScreenshot("create_world_screen");
		
		System.out.println("Creating test world");
		clickButton("selectWorld.create");
		
		waitForWorldLoad();
		dismissTutorialToasts();
		waitForWorldTicks(200);
		runChatCommand("seed");
		System.out.println("Reached singleplayer world");
		takeScreenshot("in_game", Duration.ZERO);
		clearChat();
		
		System.out.println("Opening debug menu");
		toggleDebugHud();
		takeScreenshot("debug_menu");
		
		System.out.println("Closing debug menu");
		toggleDebugHud();
		
		System.out.println("Checking for broken mixins");
		MixinEnvironment.getCurrentEnvironment().audit();
		
		System.out.println("Opening inventory");
		openInventory();
		takeScreenshot("inventory");
		
		System.out.println("Closing inventory");
		closeScreen();
		
		// Build a test platform and clear out the space above it
		runChatCommand("fill ~-5 ~-1 ~-5 ~5 ~-1 ~5 stone");
		runChatCommand("fill ~-5 ~ ~-5 ~5 ~30 ~5 air");
		
		// Clear inventory and chat before running tests
		runChatCommand("clear");
		clearChat();
		
		// Test Mo Glass features
		testItemNamesShowUpCorrectly();
		testRecipesWork();
		
		System.out.println("Opening game menu");
		openGameMenu();
		takeScreenshot("game_menu");
		
		System.out.println("Returning to title screen");
		clickButton("menu.returnToMenu");
		waitForScreen(TitleScreen.class);
		
		System.out.println("Stopping the game");
		clickButton("menu.quit");
	}
	
	private void testItemNamesShowUpCorrectly()
	{
		assertItemName("Glass Slab", new ItemStack(MoGlassBlocks.GLASS_SLAB));
		assertItemName("Glass Stairs",
			new ItemStack(MoGlassBlocks.GLASS_STAIRS));
		assertItemName("Tinted Glass Slab",
			new ItemStack(MoGlassBlocks.TINTED_GLASS_SLAB));
		
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
	
	private void testRecipesWork()
	{
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
	
	private void testRecipesForGlassType(ItemConvertible input,
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
	
	private void assertCraftingRecipe(ItemStack[][] inputGrid,
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
	
	private void assertStonecuttingRecipe(ItemStack input,
		ItemStack expectedResult)
	{
		CuttingRecipeDisplay.Grouping<StonecuttingRecipe> recipeGroups =
			submitAndGet(mc -> mc.getServer().getRecipeManager()
				.getStonecutterRecipes().filter(input));
		
		List<StonecuttingRecipe> recipes = recipeGroups.entries().stream()
			.map(group -> group.recipe().recipe()).filter(Optional::isPresent)
			.map(Optional::get).map(entry -> entry.value()).toList();
		
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
	
	private void assertItemName(String expected, ItemStack stack)
	{
		if(!expected.equals(I18n.translate(stack.getName().getString())))
			throw new RuntimeException("Wrong item name: Expected <" + expected
				+ "> but got <" + stack.getName() + ">");
	}
	
	// As of 1.21.4, vanilla Minecraft doesn't seem to have a method like this.
	private Block getStainedGlassBlock(DyeColor color)
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
