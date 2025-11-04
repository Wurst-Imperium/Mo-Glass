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
		disableInactivityFpsLimit();
		submitAndWait(mc -> mc.options.getViewDistance().setValue(5));
		
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
			"E2E Test " + SharedConstants.getGameVersion().name());
		// Select creative mode
		clickButton("selectWorld.gameMode");
		clickButton("selectWorld.gameMode");
		takeScreenshot("create_world_screen");
		
		System.out.println("Creating test world");
		clickButton("selectWorld.create");
		
		waitForWorldLoad();
		dismissTutorialToasts();
		
		// Disable anisotropic filtering
		submitAndWait(mc -> mc.options.getMaxAnisotropy().setValue(0));
		
		// Disable chunk fade
		submitAndWait(mc -> mc.options.getChunkFade().setValue(0.0));
		
		waitForWorldTicks(200);
		runChatCommand("seed");
		System.out.println("Reached singleplayer world");
		takeScreenshot("in_game", Duration.ZERO);
		runChatCommand("gamerule advance_time false");
		runChatCommand("gamerule advance_weather false");
		runChatCommand("gamerule spawn_wandering_traders false");
		runChatCommand("gamerule spawn_patrols false");
		runChatCommand("time set noon");
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
		runChatCommand("fill ~-7 ~-5 ~-4 ~7 ~-1 ~10 stone");
		runChatCommand("fill ~-7 ~ ~-4 ~7 ~30 ~10 air");
		runChatCommand("kill @e[type=!player,distance=..15]");
		runChatCommand("kill @e[type=item]");
		
		// Clear inventory and chat before running tests
		runChatCommand("clear");
		clearChat();
		
		// Test Mo Glass features
		ItemNamesTest.testItemNamesShowUpCorrectly();
		RecipesTest.testRecipesWork();
		LootTableTest.testGlassPiecesDropCorrectItems();
		GlassPieceConnectionTest.testGlassPiecesConnectCorrectly();
		TintedGlassLightBlockingTest.testTintedGlassBlocksLightCorrectly();
		
		System.out.println("Opening game menu");
		openGameMenu();
		takeScreenshot("game_menu");
		
		System.out.println("Returning to title screen");
		clickButton("menu.returnToMenu");
		waitForScreen(TitleScreen.class);
		
		System.out.println("Stopping the game");
		clickButton("menu.quit");
	}
}
