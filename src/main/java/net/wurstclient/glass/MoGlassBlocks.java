/*
 * Copyright (c) 2019-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass;

import java.util.ArrayList;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public enum MoGlassBlocks
{
	;
	
	public static final DeferredRegister<Block> BLOCKS =
		DeferredRegister.create(ForgeRegistries.BLOCKS, MoGlass.MODID);
	
	public static final DeferredRegister<Item> ITEMS =
		DeferredRegister.create(ForgeRegistries.ITEMS, MoGlass.MODID);
	
	public static final RegistryObject<Block> GLASS_SLAB =
		registerBlock("glass_slab",
			() -> new GlassSlabBlock(BlockBehaviour.Properties.of()
				.instrument(NoteBlockInstrument.HAT).strength(0.3F)
				.sound(SoundType.GLASS).noOcclusion()
				.isValidSpawn(MoGlassBlocks::never)
				.isRedstoneConductor(MoGlassBlocks::never)
				.isSuffocating(MoGlassBlocks::never)
				.isViewBlocking(MoGlassBlocks::never)));
	
	public static final RegistryObject<Block> GLASS_STAIRS =
		registerBlock("glass_stairs",
			() -> new GlassStairsBlock(BlockBehaviour.Properties.of()
				.instrument(NoteBlockInstrument.HAT).strength(0.3F)
				.sound(SoundType.GLASS).noOcclusion()
				.isValidSpawn(MoGlassBlocks::never)
				.isRedstoneConductor(MoGlassBlocks::never)
				.isSuffocating(MoGlassBlocks::never)
				.isViewBlocking(MoGlassBlocks::never)));
	
	public static final RegistryObject<Block> TINTED_GLASS_SLAB =
		registerBlock("tinted_glass_slab",
			() -> new TintedGlassSlabBlock(BlockBehaviour.Properties
				.copy(Blocks.GLASS).mapColor(MapColor.COLOR_GRAY).noOcclusion()
				.isValidSpawn(MoGlassBlocks::never)
				.isRedstoneConductor(MoGlassBlocks::never)
				.isSuffocating(MoGlassBlocks::never)
				.isViewBlocking(MoGlassBlocks::never)));
	
	public static final RegistryObject<Block> TINTED_GLASS_STAIRS =
		registerBlock("tinted_glass_stairs",
			() -> new TintedGlassStairsBlock(BlockBehaviour.Properties
				.copy(Blocks.GLASS).mapColor(MapColor.COLOR_GRAY).noOcclusion()
				.isValidSpawn(MoGlassBlocks::never)
				.isRedstoneConductor(MoGlassBlocks::never)
				.isSuffocating(MoGlassBlocks::never)
				.isViewBlocking(MoGlassBlocks::never)));
	
	public static final ArrayList<RegistryObject<StainedGlassSlabBlock>> STAINED_GLASS_SLABS =
		new ArrayList<>();
	
	public static final ArrayList<RegistryObject<StainedGlassStairsBlock>> STAINED_GLASS_STAIRS =
		new ArrayList<>();
	
	public static final RegistryObject<StainedGlassSlabBlock> WHITE_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.WHITE);
	public static final RegistryObject<StainedGlassSlabBlock> ORANGE_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.ORANGE);
	public static final RegistryObject<StainedGlassSlabBlock> MAGENTA_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.MAGENTA);
	public static final RegistryObject<StainedGlassSlabBlock> LIGHT_BLUE_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.LIGHT_BLUE);
	public static final RegistryObject<StainedGlassSlabBlock> YELLOW_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.YELLOW);
	public static final RegistryObject<StainedGlassSlabBlock> LIME_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.LIME);
	public static final RegistryObject<StainedGlassSlabBlock> PINK_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.PINK);
	public static final RegistryObject<StainedGlassSlabBlock> GRAY_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.GRAY);
	public static final RegistryObject<StainedGlassSlabBlock> LIGHT_GRAY_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.LIGHT_GRAY);
	public static final RegistryObject<StainedGlassSlabBlock> CYAN_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.CYAN);
	public static final RegistryObject<StainedGlassSlabBlock> PURPLE_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.PURPLE);
	public static final RegistryObject<StainedGlassSlabBlock> BLUE_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.BLUE);
	public static final RegistryObject<StainedGlassSlabBlock> BROWN_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.BROWN);
	public static final RegistryObject<StainedGlassSlabBlock> GREEN_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.GREEN);
	public static final RegistryObject<StainedGlassSlabBlock> RED_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.RED);
	public static final RegistryObject<StainedGlassSlabBlock> BLACK_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.BLACK);
	
	public static final RegistryObject<StainedGlassStairsBlock> WHITE_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.WHITE);
	public static final RegistryObject<StainedGlassStairsBlock> ORANGE_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.ORANGE);
	public static final RegistryObject<StainedGlassStairsBlock> MAGENTA_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.MAGENTA);
	public static final RegistryObject<StainedGlassStairsBlock> LIGHT_BLUE_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.LIGHT_BLUE);
	public static final RegistryObject<StainedGlassStairsBlock> YELLOW_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.YELLOW);
	public static final RegistryObject<StainedGlassStairsBlock> LIME_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.LIME);
	public static final RegistryObject<StainedGlassStairsBlock> PINK_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.PINK);
	public static final RegistryObject<StainedGlassStairsBlock> GRAY_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.GRAY);
	public static final RegistryObject<StainedGlassStairsBlock> LIGHT_GRAY_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.LIGHT_GRAY);
	public static final RegistryObject<StainedGlassStairsBlock> CYAN_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.CYAN);
	public static final RegistryObject<StainedGlassStairsBlock> PURPLE_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.PURPLE);
	public static final RegistryObject<StainedGlassStairsBlock> BLUE_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.BLUE);
	public static final RegistryObject<StainedGlassStairsBlock> BROWN_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.BROWN);
	public static final RegistryObject<StainedGlassStairsBlock> GREEN_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.GREEN);
	public static final RegistryObject<StainedGlassStairsBlock> RED_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.RED);
	public static final RegistryObject<StainedGlassStairsBlock> BLACK_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.BLACK);
	
	private static <T extends Block> RegistryObject<T> registerBlock(
		String idPath, Supplier<T> block)
	{
		System.out.println("Registering block & item for mo_glass:" + idPath);
		RegistryObject<T> result = BLOCKS.register(idPath, block);
		ITEMS.register(idPath,
			() -> new BlockItem(result.get(), new Item.Properties()));
		
		return result;
	}
	
	private static RegistryObject<StainedGlassSlabBlock> createStainedGlassSlab(
		DyeColor color)
	{
		RegistryObject<StainedGlassSlabBlock> result =
			registerBlock(color + "_stained_glass_slab",
				() -> new StainedGlassSlabBlock(color,
					BlockBehaviour.Properties.of().mapColor(color)
						.instrument(NoteBlockInstrument.HAT).strength(0.3F)
						.sound(SoundType.GLASS).noOcclusion()
						.isValidSpawn(MoGlassBlocks::never)
						.isRedstoneConductor(MoGlassBlocks::never)
						.isSuffocating(MoGlassBlocks::never)
						.isViewBlocking(MoGlassBlocks::never)));
		
		STAINED_GLASS_SLABS.add(result);
		return result;
	}
	
	private static RegistryObject<StainedGlassStairsBlock> createStainedGlassStairs(
		DyeColor color)
	{
		RegistryObject<StainedGlassStairsBlock> result =
			registerBlock(color + "_stained_glass_stairs",
				() -> new StainedGlassStairsBlock(color,
					BlockBehaviour.Properties.of().mapColor(color)
						.instrument(NoteBlockInstrument.HAT).strength(0.3F)
						.sound(SoundType.GLASS).noOcclusion()
						.isValidSpawn(MoGlassBlocks::never)
						.isRedstoneConductor(MoGlassBlocks::never)
						.isSuffocating(MoGlassBlocks::never)
						.isViewBlocking(MoGlassBlocks::never)));
		
		STAINED_GLASS_STAIRS.add(result);
		return result;
	}
	
	// Copies of the Blocks.never() methods because the originals are not
	// accessible from here.
	
	private static Boolean never(BlockState blockState, BlockGetter blockView,
		BlockPos blockPos, EntityType<?> entityType)
	{
		return false;
	}
	
	private static boolean never(BlockState blockState, BlockGetter blockView,
		BlockPos blockPos)
	{
		return false;
	}
}
