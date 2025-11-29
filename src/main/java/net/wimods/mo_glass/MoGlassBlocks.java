/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass;

import java.util.Arrays;
import java.util.List;

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
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public enum MoGlassBlocks
{
	;
	
	public static final RegistryKey<Block> GLASS_SLAB_KEY =
		blockKey("glass_slab");
	
	public static final Block GLASS_SLAB = new GlassSlabBlock(
		AbstractBlock.Settings.create().instrument(NoteBlockInstrument.HAT)
			.strength(0.3F).sounds(BlockSoundGroup.GLASS).nonOpaque()
			.allowsSpawning(Blocks::never).solidBlock(Blocks::never)
			.suffocates(Blocks::never).blockVision(Blocks::never)
			.registryKey(GLASS_SLAB_KEY));
	
	public static final RegistryKey<Block> GLASS_STAIRS_KEY =
		blockKey("glass_stairs");
	
	public static final Block GLASS_STAIRS = new GlassStairsBlock(
		AbstractBlock.Settings.create().instrument(NoteBlockInstrument.HAT)
			.strength(0.3F).sounds(BlockSoundGroup.GLASS).nonOpaque()
			.allowsSpawning(Blocks::never).solidBlock(Blocks::never)
			.suffocates(Blocks::never).blockVision(Blocks::never)
			.registryKey(GLASS_STAIRS_KEY));
	
	public static final RegistryKey<Block> TINTED_GLASS_SLAB_KEY =
		blockKey("tinted_glass_slab");
	
	public static final Block TINTED_GLASS_SLAB = new TintedGlassSlabBlock(
		AbstractBlock.Settings.create().instrument(NoteBlockInstrument.HAT)
			.strength(0.3F).sounds(BlockSoundGroup.GLASS)
			.mapColor(MapColor.GRAY).allowsSpawning(Blocks::never)
			.solidBlock(Blocks::never).suffocates(Blocks::never)
			.blockVision(Blocks::never).registryKey(TINTED_GLASS_SLAB_KEY));
	
	public static final RegistryKey<Block> TINTED_GLASS_STAIRS_KEY =
		blockKey("tinted_glass_stairs");
	
	public static final Block TINTED_GLASS_STAIRS = new TintedGlassStairsBlock(
		AbstractBlock.Settings.create().instrument(NoteBlockInstrument.HAT)
			.strength(0.3F).sounds(BlockSoundGroup.GLASS)
			.mapColor(MapColor.GRAY).allowsSpawning(Blocks::never)
			.solidBlock(Blocks::never).suffocates(Blocks::never)
			.blockVision(Blocks::never).registryKey(TINTED_GLASS_STAIRS_KEY));
	
	public static final List<RegistryKey<Block>> STAINED_GLASS_SLAB_KEYS =
		Arrays.stream(DyeColor.values())
			.map(color -> color.getName() + "_stained_glass_slab")
			.map(MoGlassBlocks::blockKey).toList();
	
	public static final List<StainedGlassSlabBlock> STAINED_GLASS_SLABS = Arrays
		.stream(DyeColor.values()).map(color -> createStainedGlassSlab(color,
			STAINED_GLASS_SLAB_KEYS.get(color.ordinal())))
		.toList();
	
	public static final List<RegistryKey<Block>> STAINED_GLASS_STAIRS_KEYS =
		Arrays.stream(DyeColor.values())
			.map(color -> color.getName() + "_stained_glass_stairs")
			.map(MoGlassBlocks::blockKey).toList();
	
	public static final List<StainedGlassStairsBlock> STAINED_GLASS_STAIRS =
		Arrays.stream(DyeColor.values())
			.map(color -> createStainedGlassStairs(color,
				STAINED_GLASS_STAIRS_KEYS.get(color.ordinal())))
			.toList();
	
	public static void initialize()
	{
		if(FabricLoader.getInstance().isModLoaded("translucent-glass"))
		{
			registerBlockTranslucent(GLASS_SLAB, GLASS_SLAB_KEY);
			registerBlockTranslucent(GLASS_STAIRS, GLASS_STAIRS_KEY);
			
		}else
		{
			registerBlockCutoutMipped(GLASS_SLAB, GLASS_SLAB_KEY);
			registerBlockCutoutMipped(GLASS_STAIRS, GLASS_STAIRS_KEY);
		}
		
		registerBlockTranslucent(TINTED_GLASS_SLAB, TINTED_GLASS_SLAB_KEY);
		registerBlockTranslucent(TINTED_GLASS_STAIRS, TINTED_GLASS_STAIRS_KEY);
		
		for(int i = 0; i < 16; i++)
		{
			registerBlockTranslucent(STAINED_GLASS_SLABS.get(i),
				STAINED_GLASS_SLAB_KEYS.get(i));
			registerBlockTranslucent(STAINED_GLASS_STAIRS.get(i),
				STAINED_GLASS_STAIRS_KEYS.get(i));
		}
		
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS)
			.register(content -> {
				
				// stairs
				content.addBefore(Blocks.GLASS_PANE, GLASS_STAIRS,
					TINTED_GLASS_STAIRS);
				content.addBefore(Blocks.GLASS_PANE,
					STAINED_GLASS_STAIRS.toArray(new ItemConvertible[0]));
				
				// slabs
				content.addBefore(Blocks.GLASS_PANE, GLASS_SLAB,
					TINTED_GLASS_SLAB);
				content.addBefore(Blocks.GLASS_PANE,
					STAINED_GLASS_SLABS.toArray(new ItemConvertible[0]));
			});
	}
	
	private static RegistryKey<Block> blockKey(String idPath)
	{
		return RegistryKey.of(RegistryKeys.BLOCK,
			Identifier.of("mo_glass", idPath));
	}
	
	private static void registerBlockTranslucent(Block block,
		RegistryKey<Block> key)
	{
		registerBlock(block, key);
		
		if(MoGlass.INSTANCE.isClient())
			BlockRenderLayerMap.INSTANCE.putBlock(block,
				RenderLayer.getTranslucent());
	}
	
	private static void registerBlockCutoutMipped(Block block,
		RegistryKey<Block> key)
	{
		registerBlock(block, key);
		
		if(MoGlass.INSTANCE.isClient())
			BlockRenderLayerMap.INSTANCE.putBlock(block,
				RenderLayer.getCutoutMipped());
	}
	
	private static void registerBlock(Block block, RegistryKey<Block> blockKey)
	{
		Identifier id = blockKey.getValue();
		Registry.register(Registries.BLOCK, blockKey, block);
		
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
		Settings settings = new Item.Settings().registryKey(itemKey)
			.useBlockPrefixedTranslationKey();
		BlockItem item = new BlockItem(block, settings);
		Registry.register(Registries.ITEM, id, item);
	}
	
	private static StainedGlassSlabBlock createStainedGlassSlab(DyeColor color,
		RegistryKey<Block> key)
	{
		return new StainedGlassSlabBlock(color,
			AbstractBlock.Settings.create().mapColor(color)
				.instrument(NoteBlockInstrument.HAT).strength(0.3F)
				.sounds(BlockSoundGroup.GLASS).nonOpaque()
				.allowsSpawning(Blocks::never).solidBlock(Blocks::never)
				.suffocates(Blocks::never).blockVision(Blocks::never)
				.registryKey(key));
	}
	
	private static StainedGlassStairsBlock createStainedGlassStairs(
		DyeColor color, RegistryKey<Block> key)
	{
		return new StainedGlassStairsBlock(color,
			AbstractBlock.Settings.create().mapColor(color)
				.instrument(NoteBlockInstrument.HAT).strength(0.3F)
				.sounds(BlockSoundGroup.GLASS).nonOpaque()
				.allowsSpawning(Blocks::never).solidBlock(Blocks::never)
				.suffocates(Blocks::never).blockVision(Blocks::never)
				.registryKey(key));
	}
}
