/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public enum MoGlassBlocks
{
	;
	
	public static final Block GLASS_SLAB = new GlassSlabBlock(
		BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT)
			.strength(0.3F).sound(SoundType.GLASS).noOcclusion()
			.isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never)
			.isSuffocating(Blocks::never).isViewBlocking(Blocks::never));
	
	public static final Block GLASS_STAIRS = new GlassStairsBlock(
		BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT)
			.strength(0.3F).sound(SoundType.GLASS).noOcclusion()
			.isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never)
			.isSuffocating(Blocks::never).isViewBlocking(Blocks::never));
	
	public static final Block TINTED_GLASS_SLAB = new TintedGlassSlabBlock(
		BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)
			.mapColor(MapColor.COLOR_GRAY).noOcclusion()
			.isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never)
			.isSuffocating(Blocks::never).isViewBlocking(Blocks::never));
	
	public static final Block TINTED_GLASS_STAIRS = new TintedGlassStairsBlock(
		BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)
			.mapColor(MapColor.COLOR_GRAY).noOcclusion()
			.isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never)
			.isSuffocating(Blocks::never).isViewBlocking(Blocks::never));
	
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
		
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COLORED_BLOCKS)
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
				RenderType.translucent());
	}
	
	private static void registerBlockCutoutMipped(Block block, String idPath)
	{
		registerBlock(block, idPath);
		
		if(MoGlass.INSTANCE.isClient())
			BlockRenderLayerMap.INSTANCE.putBlock(block,
				RenderType.cutoutMipped());
	}
	
	private static void registerBlock(Block block, String idPath)
	{
		ResourceLocation identifier =
			ResourceLocation.fromNamespaceAndPath("mo_glass", idPath);
		Registry.register(BuiltInRegistries.BLOCK, identifier, block);
		
		Properties itemSettings = new Item.Properties();
		BlockItem blockItem = new BlockItem(block, itemSettings);
		Registry.register(BuiltInRegistries.ITEM, identifier, blockItem);
	}
	
	private static StainedGlassSlabBlock createStainedGlassSlab(DyeColor color)
	{
		return new StainedGlassSlabBlock(color,
			BlockBehaviour.Properties.of().mapColor(color)
				.instrument(NoteBlockInstrument.HAT).strength(0.3F)
				.sound(SoundType.GLASS).noOcclusion()
				.isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never)
				.isSuffocating(Blocks::never).isViewBlocking(Blocks::never));
	}
	
	private static StainedGlassStairsBlock createStainedGlassStairs(
		DyeColor color)
	{
		return new StainedGlassStairsBlock(color,
			BlockBehaviour.Properties.of().mapColor(color)
				.instrument(NoteBlockInstrument.HAT).strength(0.3F)
				.sound(SoundType.GLASS).noOcclusion()
				.isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never)
				.isSuffocating(Blocks::never).isViewBlocking(Blocks::never));
	}
}
