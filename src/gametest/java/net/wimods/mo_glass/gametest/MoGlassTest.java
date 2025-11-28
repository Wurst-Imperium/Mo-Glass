/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass.gametest;

import static net.wimods.mo_glass.gametest.WiModsTestHelper.*;

import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.MixinEnvironment;

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.TestInput;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestClientWorldContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.fabricmc.fabric.api.client.gametest.v1.world.TestWorldBuilder;
import net.minecraft.SharedConstants;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;
import net.minecraft.world.rule.GameRules;
import net.wimods.mo_glass.gametest.tests.GlassPieceConnectionTest;
import net.wimods.mo_glass.gametest.tests.ItemNamesTest;
import net.wimods.mo_glass.gametest.tests.LootTableTest;
import net.wimods.mo_glass.gametest.tests.RecipesTest;
import net.wimods.mo_glass.gametest.tests.TintedGlassLightBlockingTest;

public final class MoGlassTest implements FabricClientGameTest
{
	public static final Logger LOGGER =
		LoggerFactory.getLogger("Mo Glass Test");
	
	@Override
	public void runTest(ClientGameTestContext context)
	{
		LOGGER.info("Starting Mo Glass Client GameTest");
		hideSplashTexts(context);
		waitForTitleScreenFade(context);
		disableInactivityFpsLimit(context);
		
		LOGGER.info("Reached title screen");
		context.takeScreenshot("title_screen");
		
		LOGGER.info("Creating test world");
		TestWorldBuilder worldBuilder = context.worldBuilder();
		worldBuilder.adjustSettings(creator -> {
			String mcVersion = SharedConstants.getGameVersion().name();
			creator.setWorldName("E2E Test " + mcVersion);
			creator.setGameMode(WorldCreator.Mode.CREATIVE);
			creator.getGameRules().setValue(GameRules.SEND_COMMAND_FEEDBACK,
				false, null);
			applyFlatPresetWithSmoothStone(creator);
		});
		
		try(TestSingleplayerContext spContext = worldBuilder.create())
		{
			testInWorld(context, spContext);
			LOGGER.info("Exiting test world");
		}
		
		LOGGER.info("Test complete");
	}
	
	private void testInWorld(ClientGameTestContext context,
		TestSingleplayerContext spContext)
	{
		TestInput input = context.getInput();
		TestClientWorldContext world = spContext.getClientWorld();
		TestServerContext server = spContext.getServer();
		
		// Disable anisotropic filtering
		context.runOnClient(mc -> mc.options.getMaxAnisotropy().setValue(0));
		
		// Disable chunk fade
		context.runOnClient(mc -> mc.options.getChunkFade().setValue(0.0));
		
		runCommand(server, "time set noon");
		runCommand(server, "tp 0 -57 0");
		runCommand(server, "fill ~ ~-3 ~ ~ ~-1 ~ smooth_stone");
		runCommand(server, "fill ~-12 ~-3 ~10 ~12 ~9 ~10 smooth_stone");
		
		LOGGER.info("Loading chunks");
		context.waitTicks(2);
		world.waitForChunksRender();
		
		assertScreenshotEquals(context, "in_game",
			"https://i.imgur.com/i2Nr9is.png");
		
		LOGGER.info("Recording debug menu");
		input.pressKey(GLFW.GLFW_KEY_F3);
		context.takeScreenshot("debug_menu");
		input.pressKey(GLFW.GLFW_KEY_F3);
		
		LOGGER.info("Checking for broken mixins");
		MixinEnvironment.getCurrentEnvironment().audit();
		
		LOGGER.info("Opening inventory");
		input.pressKey(GLFW.GLFW_KEY_E);
		assertScreenshotEquals(context, "inventory",
			"https://i.imgur.com/GP74ZNS.png");
		input.pressKey(GLFW.GLFW_KEY_ESCAPE);
		
		// Test Mo Glass features
		ItemNamesTest.testItemNamesShowUpCorrectly();
		RecipesTest.testRecipesWork(context);
		LootTableTest.testGlassPiecesDropCorrectItems(context, spContext);
		GlassPieceConnectionTest.testGlassPiecesConnectCorrectly(context,
			spContext);
		TintedGlassLightBlockingTest
			.testTintedGlassBlocksLightCorrectly(context, spContext);
		
		LOGGER.info("Opening game menu");
		input.pressKey(GLFW.GLFW_KEY_ESCAPE);
		context.takeScreenshot("game_menu");
		input.pressKey(GLFW.GLFW_KEY_ESCAPE);
	}
	
	// because the grass texture is randomized and smooth stone isn't
	private void applyFlatPresetWithSmoothStone(WorldCreator creator)
	{
		FlatChunkGeneratorConfig config =
			((FlatChunkGenerator)creator.getGeneratorOptionsHolder()
				.selectedDimensions().getChunkGenerator()).getConfig();
		
		List<FlatChunkGeneratorLayer> layers =
			List.of(new FlatChunkGeneratorLayer(1, Blocks.BEDROCK),
				new FlatChunkGeneratorLayer(2, Blocks.DIRT),
				new FlatChunkGeneratorLayer(1, Blocks.SMOOTH_STONE));
		
		creator.applyModifier((drm, dorHolder) -> dorHolder.with(drm,
			new FlatChunkGenerator(config.with(layers,
				config.getStructureOverrides(), config.getBiome()))));
	}
}
