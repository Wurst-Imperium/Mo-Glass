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

import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public enum MoGlassBlocks
{
	;
	
	public static final ResourceKey<Block> GLASS_SLAB_KEY =
		blockKey("glass_slab");
	
	public static final Block GLASS_SLAB =
		new GlassSlabBlock(BlockBehaviour.Properties.of()
			.instrument(NoteBlockInstrument.HAT).strength(0.3F)
			.sound(SoundType.GLASS).noOcclusion().isValidSpawn(Blocks::never)
			.isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never)
			.isViewBlocking(Blocks::never).setId(GLASS_SLAB_KEY));
	
	public static final ResourceKey<Block> GLASS_STAIRS_KEY =
		blockKey("glass_stairs");
	
	public static final Block GLASS_STAIRS =
		new GlassStairsBlock(BlockBehaviour.Properties.of()
			.instrument(NoteBlockInstrument.HAT).strength(0.3F)
			.sound(SoundType.GLASS).noOcclusion().isValidSpawn(Blocks::never)
			.isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never)
			.isViewBlocking(Blocks::never).setId(GLASS_STAIRS_KEY));
	
	public static final ResourceKey<Block> TINTED_GLASS_SLAB_KEY =
		blockKey("tinted_glass_slab");
	
	public static final Block TINTED_GLASS_SLAB = new TintedGlassSlabBlock(
		BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT)
			.strength(0.3F).sound(SoundType.GLASS).mapColor(MapColor.COLOR_GRAY)
			.isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never)
			.isSuffocating(Blocks::never).isViewBlocking(Blocks::never)
			.setId(TINTED_GLASS_SLAB_KEY));
	
	public static final ResourceKey<Block> TINTED_GLASS_STAIRS_KEY =
		blockKey("tinted_glass_stairs");
	
	public static final Block TINTED_GLASS_STAIRS = new TintedGlassStairsBlock(
		BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT)
			.strength(0.3F).sound(SoundType.GLASS).mapColor(MapColor.COLOR_GRAY)
			.isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never)
			.isSuffocating(Blocks::never).isViewBlocking(Blocks::never)
			.setId(TINTED_GLASS_STAIRS_KEY));
	
	public static final List<ResourceKey<Block>> STAINED_GLASS_SLAB_KEYS =
		Arrays.stream(DyeColor.values())
			.map(color -> color.getName() + "_stained_glass_slab")
			.map(MoGlassBlocks::blockKey).toList();
	
	public static final List<StainedGlassSlabBlock> STAINED_GLASS_SLABS = Arrays
		.stream(DyeColor.values()).map(color -> createStainedGlassSlab(color,
			STAINED_GLASS_SLAB_KEYS.get(color.ordinal())))
		.toList();
	
	public static final List<ResourceKey<Block>> STAINED_GLASS_STAIRS_KEYS =
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
		
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COLORED_BLOCKS)
			.register(content -> {
				
				// stairs
				content.addBefore(Blocks.GLASS_PANE, GLASS_STAIRS,
					TINTED_GLASS_STAIRS);
				content.addBefore(Blocks.GLASS_PANE,
					STAINED_GLASS_STAIRS.toArray(new ItemLike[0]));
				
				// slabs
				content.addBefore(Blocks.GLASS_PANE, GLASS_SLAB,
					TINTED_GLASS_SLAB);
				content.addBefore(Blocks.GLASS_PANE,
					STAINED_GLASS_SLABS.toArray(new ItemLike[0]));
			});
	}
	
	private static ResourceKey<Block> blockKey(String idPath)
	{
		return ResourceKey.create(Registries.BLOCK,
			ResourceLocation.fromNamespaceAndPath("mo_glass", idPath));
	}
	
	private static void registerBlockTranslucent(Block block,
		ResourceKey<Block> key)
	{
		registerBlock(block, key);
		
		if(MoGlass.INSTANCE.isClient())
			BlockRenderLayerMap.putBlock(block, ChunkSectionLayer.TRANSLUCENT);
	}
	
	private static void registerBlockCutoutMipped(Block block,
		ResourceKey<Block> key)
	{
		registerBlock(block, key);
		
		if(MoGlass.INSTANCE.isClient())
			BlockRenderLayerMap.putBlock(block,
				ChunkSectionLayer.CUTOUT_MIPPED);
	}
	
	private static void registerBlock(Block block, ResourceKey<Block> blockKey)
	{
		ResourceLocation id = blockKey.location();
		Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
		
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
		Properties settings =
			new Item.Properties().setId(itemKey).useBlockDescriptionPrefix();
		BlockItem item = new BlockItem(block, settings);
		Registry.register(BuiltInRegistries.ITEM, id, item);
	}
	
	private static StainedGlassSlabBlock createStainedGlassSlab(DyeColor color,
		ResourceKey<Block> key)
	{
		return new StainedGlassSlabBlock(color, BlockBehaviour.Properties.of()
			.mapColor(color).instrument(NoteBlockInstrument.HAT).strength(0.3F)
			.sound(SoundType.GLASS).noOcclusion().isValidSpawn(Blocks::never)
			.isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never)
			.isViewBlocking(Blocks::never).setId(key));
	}
	
	private static StainedGlassStairsBlock createStainedGlassStairs(
		DyeColor color, ResourceKey<Block> key)
	{
		return new StainedGlassStairsBlock(color, BlockBehaviour.Properties.of()
			.mapColor(color).instrument(NoteBlockInstrument.HAT).strength(0.3F)
			.sound(SoundType.GLASS).noOcclusion().isValidSpawn(Blocks::never)
			.isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never)
			.isViewBlocking(Blocks::never).setId(key));
	}
}
