/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass.gametest.tests;

import static net.wimods.mo_glass.gametest.WiModsTestHelper.*;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestClientWorldContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.wimods.mo_glass.MoGlassBlocks;
import net.wimods.mo_glass.gametest.MoGlassTest;

public enum LootTableTest
{
	;
	
	public static void testGlassPiecesDropCorrectItems(
		ClientGameTestContext context, TestSingleplayerContext spContext)
	{
		TestClientWorldContext world = spContext.getClientWorld();
		TestServerContext server = spContext.getServer();
		
		MoGlassTest.LOGGER
			.info("Testing if glass pieces drop the correct items...");
		BlockPos playerPos =
			context.computeOnClient(mc -> mc.player.getBlockPos());
		BlockPos startPos = playerPos.add(-4, 0, 7);
		
		// Build the test rig
		LinkedHashMap<BlockPos, BlockState> blocks = createTestRig(startPos);
		setBlocks(server, blocks);
		context.waitTicks(2);
		world.waitForChunksRender();
		
		try
		{
			// Test with and without silk touch pickaxe
			testWithTool(context, spContext, false, blocks, startPos);
			testWithTool(context, spContext, true, blocks, startPos);
			assertScreenshotEquals(context, "loot_table_test",
				"https://i.imgur.com/GLUrKlw.png");
			
		}catch(TestFailureException e)
		{
			e.showFailure(context, spContext, playerPos);
			throw e;
		}
		
		// Clean up
		runCommand(server, "fill ~-7 ~ ~-4 ~7 ~30 ~9 air");
		context.waitTicks(2);
		world.waitForChunksRender();
	}
	
	private static LinkedHashMap<BlockPos, BlockState> createTestRig(
		BlockPos startPos)
	{
		LinkedHashMap<BlockPos, BlockState> blocks = new LinkedHashMap<>();
		
		for(DyeColor color : DyeColor.values())
		{
			int i = color.ordinal();
			BlockPos pos = startPos.add(i % 3 * 3, i / 3, 0);
			addGroup(blocks, pos, MoGlassBlocks.STAINED_GLASS_SLABS.get(i),
				MoGlassBlocks.STAINED_GLASS_STAIRS.get(i));
		}
		
		addGroup(blocks, startPos.add(3, 5, 0), MoGlassBlocks.GLASS_SLAB,
			MoGlassBlocks.GLASS_STAIRS);
		addGroup(blocks, startPos.add(6, 5, 0), MoGlassBlocks.TINTED_GLASS_SLAB,
			MoGlassBlocks.TINTED_GLASS_STAIRS);
		
		return blocks;
	}
	
	private static void addGroup(LinkedHashMap<BlockPos, BlockState> blocks,
		BlockPos pos, Block slab, Block stairs)
	{
		blocks.put(pos, slab.getDefaultState());
		blocks.put(pos.east(1),
			stairs.getDefaultState().with(StairsBlock.FACING, Direction.EAST));
		blocks.put(pos.east(2),
			slab.getDefaultState().with(SlabBlock.TYPE, SlabType.DOUBLE));
	}
	
	private static void testWithTool(ClientGameTestContext context,
		TestSingleplayerContext spContext, boolean silkTouch,
		Map<BlockPos, BlockState> blocks, BlockPos startPos)
	{
		TestServerContext server = spContext.getServer();
		
		// Equip the appropriate tool
		runCommand(server, "give @s diamond_pickaxe"
			+ (silkTouch ? "[enchantments={silk_touch:1}]" : ""));
		context.waitTick();
		
		// Test all blocks
		for(int row = 0; row < 6; row++)
			for(int col = 0; col < 9; col++)
			{
				BlockPos pos = startPos.add(col, row, 0);
				testBlockDrops(context, spContext, pos, blocks.get(pos),
					silkTouch);
			}
		
		clearInventory(context);
		context.waitTicks(8);
	}
	
	private static void testBlockDrops(ClientGameTestContext context,
		TestSingleplayerContext spContext, BlockPos pos, BlockState state,
		boolean silkTouch)
	{
		TestServerContext server = spContext.getServer();
		
		// Check what the block would drop if mined by the player
		// (requires server-side player and world)
		UUID playerUuid = context.computeOnClient(mc -> mc.player.getUuid());
		List<ItemStack> drops = server.computeOnServer(mc -> {
			
			ServerPlayerEntity player =
				mc.getPlayerManager().getPlayer(playerUuid);
			ServerWorld world = mc.getWorld(World.OVERWORLD);
			
			return Block.getDroppedStacks(state, world, pos, null, player,
				player.getMainHandStack());
		});
		
		// Check if the drops are as expected
		ItemStack expectedStack = getExpectedDrops(state, silkTouch);
		ItemStack firstDrop =
			drops.stream().findFirst().orElse(ItemStack.EMPTY);
		if(drops.size() <= 1 && ItemStack.areEqual(expectedStack, firstDrop))
			return;
		
		String dropsString = drops.stream().map(ItemStack::toString)
			.reduce((a, b) -> a + ", " + b).orElse("no items");
		throw new TestFailureException(state, silkTouch, expectedStack,
			dropsString);
	}
	
	private static ItemStack getExpectedDrops(BlockState state,
		boolean silkTouch)
	{
		boolean doubleSlab =
			state.getOrEmpty(SlabBlock.TYPE).orElse(null) == SlabType.DOUBLE;
		boolean tinted = state.getBlock() == MoGlassBlocks.TINTED_GLASS_SLAB
			|| state.getBlock() == MoGlassBlocks.TINTED_GLASS_STAIRS;
		
		if(silkTouch || tinted)
			return new ItemStack(state.getBlock(), doubleSlab ? 2 : 1);
		
		return ItemStack.EMPTY;
	}
	
	private static class TestFailureException extends RuntimeException
	{
		private final BlockState state;
		private final boolean silkTouch;
		
		public TestFailureException(BlockState state, boolean silkTouch,
			ItemStack expectedStack, String actualDrops)
		{
			super(formatErrorMessage(state, silkTouch, expectedStack,
				actualDrops));
			this.state = state;
			this.silkTouch = silkTouch;
		}
		
		private static String getBlockName(BlockState state)
		{
			boolean doubleSlab = state.getOrEmpty(SlabBlock.TYPE)
				.orElse(null) == SlabType.DOUBLE;
			Identifier id = Registries.BLOCK.getId(state.getBlock());
			return (doubleSlab ? "double_" : "")
				+ id.toString().replace("mo_glass:", "");
		}
		
		private static String formatErrorMessage(BlockState state,
			boolean silkTouch, ItemStack expectedStack, String actualDrops)
		{
			return getBlockName(state) + (silkTouch ? " with" : " without")
				+ " silk touch dropped " + actualDrops + " instead of "
				+ expectedStack;
		}
		
		public void showFailure(ClientGameTestContext context,
			TestSingleplayerContext spContext, BlockPos playerPos)
		{
			TestClientWorldContext world = spContext.getClientWorld();
			TestServerContext server = spContext.getServer();
			
			runCommand(server, "fill ~-7 ~ ~-4 ~7 ~30 ~9 air");
			BlockPos pos = playerPos.south(4);
			setBlock(server, pos, state);
			
			context.waitTicks(3);
			world.waitForChunksRender();
			
			runCommand(server,
				"loot spawn " + pos.getX() + " " + (pos.getY() + 1) + " "
					+ pos.getZ() + " mine " + pos.getX() + " " + pos.getY()
					+ " " + pos.getZ() + " diamond_pickaxe"
					+ (silkTouch ? "[enchantments={silk_touch:1}]" : ""));
			context.waitTick();
			
			ghSummary("### One or more loot tables are broken");
			ghSummary(getMessage() + "\n");
			String fileName = "loot_table_test_failure";
			Path screenshotPath = context.takeScreenshot(fileName);
			String url = tryUploadToImgur(screenshotPath);
			if(url != null)
				ghSummary("![" + fileName + "](" + url + ")");
			else
				ghSummary("Couldn't upload " + fileName
					+ ".png to Imgur. Check the Test Screenshots.zip artifact.");
		}
	}
}
