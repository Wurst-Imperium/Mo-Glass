/*
 * Copyright (c) 2019-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass.gametest.tests;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.wimods.mo_glass.MoGlassBlocks;
import net.wimods.mo_glass.gametest.SingleplayerTest;

public final class LootTableTest extends SingleplayerTest
{
	public LootTableTest(ClientGameTestContext context,
		TestSingleplayerContext spContext)
	{
		super(context, spContext);
	}
	
	@Override
	protected void runImpl()
	{
		logger.info("Testing if glass pieces drop the correct items...");
		BlockPos playerPos =
			context.computeOnClient(mc -> mc.player.blockPosition());
		BlockPos startPos = playerPos.offset(-4, 0, 7);
		
		// Build the test rig
		LinkedHashMap<BlockPos, BlockState> blocks = createTestRig(startPos);
		setBlocks(blocks);
		context.waitTicks(2);
		world.waitForChunksRender();
		
		try
		{
			// Test with and without silk touch pickaxe
			testWithTool(false, blocks, startPos);
			testWithTool(true, blocks, startPos);
			assertScreenshotEquals("loot_table_test",
				"https://i.imgur.com/GLUrKlw.png");
			
		}catch(TestFailureException e)
		{
			showFailureAndThrow(e, playerPos);
		}
		
		// Clean up
		runCommand("fill ~-7 ~ ~-4 ~7 ~30 ~9 air");
		context.waitTicks(2);
		world.waitForChunksRender();
	}
	
	private void showFailureAndThrow(TestFailureException e, BlockPos playerPos)
	{
		runCommand("fill ~-7 ~ ~-4 ~7 ~30 ~9 air");
		BlockPos pos = playerPos.south(4);
		setBlock(pos, e.state);
		
		context.waitTicks(3);
		world.waitForChunksRender();
		
		runCommand("loot spawn " + pos.getX() + " " + (pos.getY() + 1) + " "
			+ pos.getZ() + " mine " + pos.getX() + " " + pos.getY() + " "
			+ pos.getZ() + " diamond_pickaxe"
			+ (e.silkTouch ? "[enchantments={silk_touch:1}]" : ""));
		context.waitTick();
		
		failWithScreenshot("loot_table_test_failure",
			"One or more loot tables are broken", e.getMessage());
	}
	
	private static LinkedHashMap<BlockPos, BlockState> createTestRig(
		BlockPos startPos)
	{
		LinkedHashMap<BlockPos, BlockState> blocks = new LinkedHashMap<>();
		
		for(DyeColor color : DyeColor.values())
		{
			int i = color.ordinal();
			BlockPos pos = startPos.offset(i % 3 * 3, i / 3, 0);
			addGroup(blocks, pos, MoGlassBlocks.STAINED_GLASS_SLABS.get(i),
				MoGlassBlocks.STAINED_GLASS_STAIRS.get(i));
		}
		
		addGroup(blocks, startPos.offset(3, 5, 0), MoGlassBlocks.GLASS_SLAB,
			MoGlassBlocks.GLASS_STAIRS);
		addGroup(blocks, startPos.offset(6, 5, 0),
			MoGlassBlocks.TINTED_GLASS_SLAB, MoGlassBlocks.TINTED_GLASS_STAIRS);
		
		return blocks;
	}
	
	private static void addGroup(LinkedHashMap<BlockPos, BlockState> blocks,
		BlockPos pos, Block slab, Block stairs)
	{
		blocks.put(pos, slab.defaultBlockState());
		blocks.put(pos.east(1), stairs.defaultBlockState()
			.setValue(StairBlock.FACING, Direction.EAST));
		blocks.put(pos.east(2),
			slab.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.DOUBLE));
	}
	
	private void testWithTool(boolean silkTouch,
		Map<BlockPos, BlockState> blocks, BlockPos startPos)
	{
		// Equip the appropriate tool
		runCommand("give @s diamond_pickaxe"
			+ (silkTouch ? "[enchantments={silk_touch:1}]" : ""));
		context.waitTick();
		
		// Test all blocks
		for(int row = 0; row < 6; row++)
			for(int col = 0; col < 9; col++)
			{
				BlockPos pos = startPos.offset(col, row, 0);
				testBlockDrops(pos, blocks.get(pos), silkTouch);
			}
		
		clearInventory();
		context.waitTicks(8);
	}
	
	private void testBlockDrops(BlockPos pos, BlockState state,
		boolean silkTouch)
	{
		// Check what the block would drop if mined by the player
		UUID playerUuid = context.computeOnClient(mc -> mc.player.getUUID());
		List<ItemStack> drops = server.computeOnServer(mc -> {
			
			ServerPlayer player = mc.getPlayerList().getPlayer(playerUuid);
			ServerLevel world = mc.getLevel(Level.OVERWORLD);
			
			return Block.getDrops(state, world, pos, null, player,
				player.getMainHandItem());
		});
		
		// Check if the drops are as expected
		ItemStack expectedStack = getExpectedDrops(state, silkTouch);
		ItemStack firstDrop =
			drops.stream().findFirst().orElse(ItemStack.EMPTY);
		if(drops.size() <= 1 && ItemStack.matches(expectedStack, firstDrop))
			return;
		
		String dropsString = drops.stream().map(ItemStack::toString)
			.reduce((a, b) -> a + ", " + b).orElse("no items");
		throw new TestFailureException(state, silkTouch, expectedStack,
			dropsString);
	}
	
	private static ItemStack getExpectedDrops(BlockState state,
		boolean silkTouch)
	{
		boolean doubleSlab = state.getOptionalValue(SlabBlock.TYPE)
			.orElse(null) == SlabType.DOUBLE;
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
			boolean doubleSlab = state.getOptionalValue(SlabBlock.TYPE)
				.orElse(null) == SlabType.DOUBLE;
			Identifier id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
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
	}
}
