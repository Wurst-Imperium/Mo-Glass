/*
 * Copyright (c) 2019-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass.gametest;

import static net.wimods.mo_glass.gametest.WiModsTestHelper.*;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import net.fabricmc.fabric.api.client.gametest.v1.TestInput;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestClientLevelContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SingleplayerTest
{
	protected final ClientGameTestContext context;
	protected final TestSingleplayerContext spContext;
	protected final TestInput input;
	protected final TestClientLevelContext world;
	protected final TestServerContext server;
	protected final Logger logger = MoGlassTest.LOGGER;
	
	public SingleplayerTest(ClientGameTestContext context,
		TestSingleplayerContext spContext)
	{
		this.context = context;
		this.spContext = spContext;
		this.input = context.getInput();
		this.world = spContext.getClientLevel();
		this.server = spContext.getServer();
	}
	
	/**
	 * Runs the test and verifies cleanup afterward.
	 */
	public final void run()
	{
		runImpl();
		assertScreenshotEquals(
			getClass().getSimpleName().toLowerCase() + "_cleanup",
			"https://i.imgur.com/i2Nr9is.png");
	}
	
	/**
	 * Implement the actual test logic here. The test is responsible for
	 * cleaning up after itself (removing blocks, clearing
	 * chat/inventory/particles, etc.).
	 */
	protected abstract void runImpl();
	
	protected final void runCommand(String command)
	{
		WiModsTestHelper.runCommand(server, command);
	}
	
	protected final void clearChat()
	{
		context.runOnClient(mc -> mc.gui.getChat().clearMessages(true));
	}
	
	protected final void clearInventory()
	{
		input.pressKey(GLFW.GLFW_KEY_T);
		input.typeChars("/clear");
		input.pressKey(GLFW.GLFW_KEY_ENTER);
	}
	
	protected final void assertScreenshotEquals(String fileName,
		String templateUrl)
	{
		WiModsTestHelper.assertScreenshotEquals(context, fileName, templateUrl);
	}
	
	protected final void failWithScreenshot(String fileName, String title,
		String errorMessage)
	{
		Path screenshotPath = context.takeScreenshot(fileName);
		
		ghSummary("### " + title + "\n" + errorMessage + "\n");
		String url = tryUploadToImgur(screenshotPath);
		if(url != null)
			ghSummary("![" + fileName + "](" + url + ")");
		else
			ghSummary("Couldn't upload " + fileName
				+ ".png to Imgur. Check the Test Screenshots.zip artifact.");
		
		throw new RuntimeException(title + ": " + errorMessage);
	}
	
	/**
	 * Places the given block at the given position without any delays or block
	 * updates.
	 */
	protected final void setBlock(BlockPos pos, BlockState state)
	{
		server.runOnServer(mc -> mc.getLevel(Level.OVERWORLD).setBlock(pos,
			state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS));
	}
	
	/**
	 * Places all of the given blocks at the given positions at once without
	 * any delays or block updates.
	 */
	protected final void setBlocks(LinkedHashMap<BlockPos, BlockState> blocks)
	{
		server.runOnServer(mc -> {
			for(Entry<BlockPos, BlockState> entry : blocks.entrySet())
				mc.getLevel(Level.OVERWORLD).setBlock(entry.getKey(),
					entry.getValue(),
					Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
		});
	}
}
