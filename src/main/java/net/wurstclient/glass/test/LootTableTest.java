/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass.test;

import static net.wurstclient.glass.test.WiModsTestHelper.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import net.wurstclient.glass.MoGlassBlocks;

public enum LootTableTest
{
	;
	
	public static void testGlassPiecesDropCorrectItems()
	{
		System.out.println("Testing if glass pieces drop the correct items...");
		BlockPos playerPos = submitAndGet(mc -> mc.player.getBlockPos());
		BlockPos startPos = playerPos.add(-4, 0, 7);
		
		// Build the test rig
		LinkedHashMap<BlockPos, BlockState> blocks = createTestRig(startPos);
		setBlocks(blocks);
		
		try
		{
			// Test with and without silk touch pickaxe
			testWithTool(false, blocks, startPos);
			testWithTool(true, blocks, startPos);
			takeScreenshot("loot_table_test");
			
		}catch(TestFailureException e)
		{
			clearChat();
			e.showFailure(playerPos);
			throw e;
		}
		
		// Clean up
		runChatCommand("fill ~-7 ~ ~-4 ~7 ~30 ~10 air");
		runChatCommand("clear");
		clearChat();
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
	
	private static void testWithTool(boolean silkTouch,
		Map<BlockPos, BlockState> blocks, BlockPos startPos)
	{
		// Equip the appropriate tool
		runChatCommand("clear");
		runChatCommand("give @s diamond_pickaxe"
			+ (silkTouch ? "[enchantments={levels:{silk_touch:1}}]" : ""));
		waitForWorldTicks(2);
		
		// Test all blocks
		for(int row = 0; row < 6; row++)
			for(int col = 0; col < 9; col++)
			{
				BlockPos pos = startPos.add(col, row, 0);
				testBlockDrops(pos, blocks.get(pos), silkTouch);
			}
		
		clearChat();
	}
	
	private static void testBlockDrops(BlockPos pos, BlockState state,
		boolean silkTouch)
	{
		// Check what the block would drop if mined by the player
		// (requires server-side player and world)
		List<ItemStack> drops = submitAndGet(mc -> {
			
			ServerPlayerEntity player = mc.getServer().getPlayerManager()
				.getPlayer(mc.player.getUuid());
			ServerWorld world = mc.getServer().getWorld(World.OVERWORLD);
			
			return Block.getDroppedStacks(state, world, pos, null, player,
				player.getMainHandStack());
		});
		
		// Log the results
		String dropsString = drops.stream().map(ItemStack::toString)
			.reduce((a, b) -> a + ", " + b).orElse("no items");
		System.out.println(state + " " + (silkTouch ? "with" : "without")
			+ " silk touch drops " + dropsString);
		
		// Check if the drops are as expected
		ItemStack expectedStack = getExpectedDrops(state, silkTouch);
		ItemStack firstDrop =
			drops.stream().findFirst().orElse(ItemStack.EMPTY);
		if(drops.size() <= 1 && ItemStack.areEqual(expectedStack, firstDrop))
			return;
		
		throw new TestFailureException(state, silkTouch, expectedStack,
			dropsString);
	}
	
	private static ItemStack getExpectedDrops(BlockState state,
		boolean silkTouch)
	{
		boolean doubleSlab =
			state.getNullable(SlabBlock.TYPE) == SlabType.DOUBLE;
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
			boolean doubleSlab =
				state.getNullable(SlabBlock.TYPE) == SlabType.DOUBLE;
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
		
		public void showFailure(BlockPos playerPos)
		{
			// Place the failed block in front of the player
			BlockPos pos = playerPos.south(4);
			setBlock(pos, state);
			
			// Spawn the items on top of the block
			runChatCommand("loot spawn " + pos.getX() + " " + (pos.getY() + 1)
				+ " " + pos.getZ() + " mine " + pos.getX() + " " + pos.getY()
				+ " " + pos.getZ() + " diamond_pickaxe"
				+ (silkTouch ? "[enchantments={levels:{silk_touch:1}}]" : ""));
			
			// Take a screenshot
			clearChat();
			takeScreenshot(
				"FAILED_TEST_" + getBlockName(state) + "_drops_wrong_items_"
					+ (silkTouch ? "with" : "without") + "_silk_touch");
		}
	}
}
