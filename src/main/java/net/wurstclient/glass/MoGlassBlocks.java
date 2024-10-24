/*
 * Copyright (c) 2019-2024 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public enum MoGlassBlocks
{
	;
	
	public static final Block GLASS_SLAB = new GlassSlabBlock(
		AbstractBlock.Settings.create().instrument(NoteBlockInstrument.HAT)
			.strength(0.3F).sounds(BlockSoundGroup.GLASS).nonOpaque()
			.allowsSpawning(Blocks::never).solidBlock(Blocks::never)
			.suffocates(Blocks::never).blockVision(Blocks::never));
	
	public static final Block GLASS_STAIRS = new GlassStairsBlock(
		AbstractBlock.Settings.create().instrument(NoteBlockInstrument.HAT)
			.strength(0.3F).sounds(BlockSoundGroup.GLASS).nonOpaque()
			.allowsSpawning(Blocks::never).solidBlock(Blocks::never)
			.suffocates(Blocks::never).blockVision(Blocks::never));
	
	public static final Block TINTED_GLASS_SLAB =
		new TintedGlassSlabBlock(AbstractBlock.Settings.create()
			.instrument(NoteBlockInstrument.HAT).strength(0.3F)
			.sounds(BlockSoundGroup.GLASS).mapColor(MapColor.GRAY)
			.allowsSpawning(Blocks::never).solidBlock(Blocks::never)
			.suffocates(Blocks::never).blockVision(Blocks::never));
	
	public static final Block TINTED_GLASS_STAIRS =
		new TintedGlassStairsBlock(AbstractBlock.Settings.create()
			.instrument(NoteBlockInstrument.HAT).strength(0.3F)
			.sounds(BlockSoundGroup.GLASS).mapColor(MapColor.GRAY)
			.allowsSpawning(Blocks::never).solidBlock(Blocks::never)
			.suffocates(Blocks::never).blockVision(Blocks::never));
	
	public static final StainedGlassSlabBlock WHITE_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.WHITE);
	public static final StainedGlassSlabBlock ORANGE_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.ORANGE);
	public static final StainedGlassSlabBlock MAGENTA_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.MAGENTA);
	public static final StainedGlassSlabBlock LIGHT_BLUE_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.LIGHT_BLUE);
	public static final StainedGlassSlabBlock YELLOW_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.YELLOW);
	public static final StainedGlassSlabBlock LIME_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.LIME);
	public static final StainedGlassSlabBlock PINK_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.PINK);
	public static final StainedGlassSlabBlock GRAY_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.GRAY);
	public static final StainedGlassSlabBlock LIGHT_GRAY_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.LIGHT_GRAY);
	public static final StainedGlassSlabBlock CYAN_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.CYAN);
	public static final StainedGlassSlabBlock PURPLE_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.PURPLE);
	public static final StainedGlassSlabBlock BLUE_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.BLUE);
	public static final StainedGlassSlabBlock BROWN_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.BROWN);
	public static final StainedGlassSlabBlock GREEN_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.GREEN);
	public static final StainedGlassSlabBlock RED_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.RED);
	public static final StainedGlassSlabBlock BLACK_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.BLACK);
	
	public static final StainedGlassStairsBlock WHITE_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.WHITE);
	public static final StainedGlassStairsBlock ORANGE_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.ORANGE);
	public static final StainedGlassStairsBlock MAGENTA_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.MAGENTA);
	public static final StainedGlassStairsBlock LIGHT_BLUE_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.LIGHT_BLUE);
	public static final StainedGlassStairsBlock YELLOW_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.YELLOW);
	public static final StainedGlassStairsBlock LIME_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.LIME);
	public static final StainedGlassStairsBlock PINK_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.PINK);
	public static final StainedGlassStairsBlock GRAY_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.GRAY);
	public static final StainedGlassStairsBlock LIGHT_GRAY_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.LIGHT_GRAY);
	public static final StainedGlassStairsBlock CYAN_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.CYAN);
	public static final StainedGlassStairsBlock PURPLE_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.PURPLE);
	public static final StainedGlassStairsBlock BLUE_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.BLUE);
	public static final StainedGlassStairsBlock BROWN_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.BROWN);
	public static final StainedGlassStairsBlock GREEN_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.GREEN);
	public static final StainedGlassStairsBlock RED_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.RED);
	public static final StainedGlassStairsBlock BLACK_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.BLACK);
	
	public static final StainedGlassSlabBlock[] STAINED_GLASS_SLABS =
		{WHITE_STAINED_GLASS_SLAB, ORANGE_STAINED_GLASS_SLAB,
			MAGENTA_STAINED_GLASS_SLAB, LIGHT_BLUE_STAINED_GLASS_SLAB,
			YELLOW_STAINED_GLASS_SLAB, LIME_STAINED_GLASS_SLAB,
			PINK_STAINED_GLASS_SLAB, GRAY_STAINED_GLASS_SLAB,
			LIGHT_GRAY_STAINED_GLASS_SLAB, CYAN_STAINED_GLASS_SLAB,
			PURPLE_STAINED_GLASS_SLAB, BLUE_STAINED_GLASS_SLAB,
			BROWN_STAINED_GLASS_SLAB, GREEN_STAINED_GLASS_SLAB,
			RED_STAINED_GLASS_SLAB, BLACK_STAINED_GLASS_SLAB};
	
	public static final StainedGlassStairsBlock[] STAINED_GLASS_STAIRS =
		{WHITE_STAINED_GLASS_STAIRS, ORANGE_STAINED_GLASS_STAIRS,
			MAGENTA_STAINED_GLASS_STAIRS, LIGHT_BLUE_STAINED_GLASS_STAIRS,
			YELLOW_STAINED_GLASS_STAIRS, LIME_STAINED_GLASS_STAIRS,
			PINK_STAINED_GLASS_STAIRS, GRAY_STAINED_GLASS_STAIRS,
			LIGHT_GRAY_STAINED_GLASS_STAIRS, CYAN_STAINED_GLASS_STAIRS,
			PURPLE_STAINED_GLASS_STAIRS, BLUE_STAINED_GLASS_STAIRS,
			BROWN_STAINED_GLASS_STAIRS, GREEN_STAINED_GLASS_STAIRS,
			RED_STAINED_GLASS_STAIRS, BLACK_STAINED_GLASS_STAIRS};
	
	public static void initialize()
	{
		if(FabricLoader.getInstance().isModLoaded("translucent-glass"))
		{
			registerBlockTranslucent(GLASS_SLAB, "glass_slab");
			registerBlockTranslucent(GLASS_STAIRS, "glass_stairs");
			
		}else
		{
			registerBlockCutoutMipped(GLASS_SLAB, "glass_slab");
			registerBlockCutoutMipped(GLASS_STAIRS, "glass_stairs");
		}
		
		registerBlockTranslucent(TINTED_GLASS_SLAB, "tinted_glass_slab");
		registerBlockTranslucent(TINTED_GLASS_STAIRS, "tinted_glass_stairs");
		
		String[] colors = {"white", "orange", "magenta", "light_blue", "yellow",
			"lime", "pink", "gray", "light_gray", "cyan", "purple", "blue",
			"brown", "green", "red", "black"};
		
		for(int i = 0; i < 16; i++)
			registerBlockTranslucent(STAINED_GLASS_SLABS[i],
				colors[i] + "_stained_glass_slab");
		
		for(int i = 0; i < 16; i++)
			registerBlockTranslucent(STAINED_GLASS_STAIRS[i],
				colors[i] + "_stained_glass_stairs");
		
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS)
			.register(content -> {
				
				// stairs
				content.addBefore(Blocks.GLASS_PANE, GLASS_STAIRS,
					TINTED_GLASS_STAIRS);
				content.addBefore(Blocks.GLASS_PANE, STAINED_GLASS_STAIRS);
				
				// slabs
				content.addBefore(Blocks.GLASS_PANE, GLASS_SLAB,
					TINTED_GLASS_SLAB);
				content.addBefore(Blocks.GLASS_PANE, STAINED_GLASS_SLABS);
			});
	}
	
	private static void registerBlockTranslucent(Block block, String idPath)
	{
		registerBlock(block, idPath);
		
		if(MoGlass.INSTANCE.isClient())
			BlockRenderLayerMap.INSTANCE.putBlock(block,
				RenderLayer.getTranslucent());
	}
	
	private static void registerBlockCutoutMipped(Block block, String idPath)
	{
		registerBlock(block, idPath);
		
		if(MoGlass.INSTANCE.isClient())
			BlockRenderLayerMap.INSTANCE.putBlock(block,
				RenderLayer.getCutoutMipped());
	}
	
	private static void registerBlock(Block block, String idPath)
	{
		Identifier identifier = Identifier.of("mo_glass", idPath);
		Registry.register(Registries.BLOCK, identifier, block);
		
		Settings itemSettings = new Item.Settings();
		BlockItem blockItem = new BlockItem(block, itemSettings);
		Registry.register(Registries.ITEM, identifier, blockItem);
	}
	
	private static StainedGlassSlabBlock createStainedGlassSlab(DyeColor color)
	{
		return new StainedGlassSlabBlock(color,
			AbstractBlock.Settings.create().mapColor(color)
				.instrument(NoteBlockInstrument.HAT).strength(0.3F)
				.sounds(BlockSoundGroup.GLASS).nonOpaque()
				.allowsSpawning(Blocks::never).solidBlock(Blocks::never)
				.suffocates(Blocks::never).blockVision(Blocks::never));
	}
	
	private static StainedGlassStairsBlock createStainedGlassStairs(
		DyeColor color)
	{
		return new StainedGlassStairsBlock(color,
			AbstractBlock.Settings.create().mapColor(color)
				.instrument(NoteBlockInstrument.HAT).strength(0.3F)
				.sounds(BlockSoundGroup.GLASS).nonOpaque()
				.allowsSpawning(Blocks::never).solidBlock(Blocks::never)
				.suffocates(Blocks::never).blockVision(Blocks::never));
	}
}
