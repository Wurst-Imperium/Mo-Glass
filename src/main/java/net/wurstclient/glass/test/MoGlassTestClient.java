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

import org.spongepowered.asm.mixin.MixinEnvironment;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screen.AccessibilityOnboardingScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
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
		
		// TODO: Add Mo Glass-specific test code here
		testItemNamesShowUpCorrectly();
		
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
	
	private void assertItemName(String expected, ItemStack stack)
	{
		if(!expected.equals(I18n.translate(stack.getName().getString())))
			throw new RuntimeException("Wrong item name: Expected <" + expected
				+ "> but got <" + stack.getName() + ">");
	}
}
