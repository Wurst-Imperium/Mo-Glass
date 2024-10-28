/*
 * Copyright (c) 2019-2024 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass;

import java.util.ArrayList;
import java.util.function.Function;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public enum MoGlassBlocks
{
	;
	
	public static final DeferredRegister.Blocks BLOCKS =
		DeferredRegister.createBlocks(MoGlass.MODID);
	
	public static final DeferredRegister.Items ITEMS =
		DeferredRegister.createItems(MoGlass.MODID);
	
	public static final DeferredBlock<Block> GLASS_SLAB =
		registerBlock("glass_slab", GlassSlabBlock::new,
			BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT)
				.strength(0.3F).sound(SoundType.GLASS).noOcclusion()
				.sound(SoundType.GLASS).noOcclusion()
				.isValidSpawn(MoGlassBlocks::never)
				.isRedstoneConductor(MoGlassBlocks::never)
				.isSuffocating(MoGlassBlocks::never)
				.isViewBlocking(MoGlassBlocks::never));
	
	public static final DeferredBlock<Block> GLASS_STAIRS =
		registerBlock("glass_stairs", GlassStairsBlock::new,
			BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT)
				.strength(0.3F).sound(SoundType.GLASS).noOcclusion()
				.sound(SoundType.GLASS).noOcclusion()
				.isValidSpawn(MoGlassBlocks::never)
				.isRedstoneConductor(MoGlassBlocks::never)
				.isSuffocating(MoGlassBlocks::never)
				.isViewBlocking(MoGlassBlocks::never));
	
	public static final DeferredBlock<Block> TINTED_GLASS_SLAB = registerBlock(
		"tinted_glass_slab", TintedGlassSlabBlock::new,
		BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT)
			.strength(0.3F).sound(SoundType.GLASS).mapColor(MapColor.COLOR_GRAY)
			.isValidSpawn(MoGlassBlocks::never)
			.isRedstoneConductor(MoGlassBlocks::never)
			.isSuffocating(MoGlassBlocks::never)
			.isViewBlocking(MoGlassBlocks::never));
	
	public static final DeferredBlock<Block> TINTED_GLASS_STAIRS =
		registerBlock("tinted_glass_stairs", TintedGlassStairsBlock::new,
			BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT)
				.strength(0.3F).sound(SoundType.GLASS)
				.mapColor(MapColor.COLOR_GRAY)
				.isValidSpawn(MoGlassBlocks::never)
				.isRedstoneConductor(MoGlassBlocks::never)
				.isSuffocating(MoGlassBlocks::never)
				.isViewBlocking(MoGlassBlocks::never));
	
	public static final ArrayList<DeferredBlock<StainedGlassSlabBlock>> STAINED_GLASS_SLABS =
		new ArrayList<>();
	
	public static final ArrayList<DeferredBlock<StainedGlassStairsBlock>> STAINED_GLASS_STAIRS =
		new ArrayList<>();
	
	public static final DeferredBlock<StainedGlassSlabBlock> WHITE_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.WHITE);
	public static final DeferredBlock<StainedGlassSlabBlock> ORANGE_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.ORANGE);
	public static final DeferredBlock<StainedGlassSlabBlock> MAGENTA_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.MAGENTA);
	public static final DeferredBlock<StainedGlassSlabBlock> LIGHT_BLUE_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.LIGHT_BLUE);
	public static final DeferredBlock<StainedGlassSlabBlock> YELLOW_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.YELLOW);
	public static final DeferredBlock<StainedGlassSlabBlock> LIME_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.LIME);
	public static final DeferredBlock<StainedGlassSlabBlock> PINK_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.PINK);
	public static final DeferredBlock<StainedGlassSlabBlock> GRAY_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.GRAY);
	public static final DeferredBlock<StainedGlassSlabBlock> LIGHT_GRAY_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.LIGHT_GRAY);
	public static final DeferredBlock<StainedGlassSlabBlock> CYAN_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.CYAN);
	public static final DeferredBlock<StainedGlassSlabBlock> PURPLE_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.PURPLE);
	public static final DeferredBlock<StainedGlassSlabBlock> BLUE_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.BLUE);
	public static final DeferredBlock<StainedGlassSlabBlock> BROWN_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.BROWN);
	public static final DeferredBlock<StainedGlassSlabBlock> GREEN_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.GREEN);
	public static final DeferredBlock<StainedGlassSlabBlock> RED_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.RED);
	public static final DeferredBlock<StainedGlassSlabBlock> BLACK_STAINED_GLASS_SLAB =
		createStainedGlassSlab(DyeColor.BLACK);
	
	public static final DeferredBlock<StainedGlassStairsBlock> WHITE_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.WHITE);
	public static final DeferredBlock<StainedGlassStairsBlock> ORANGE_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.ORANGE);
	public static final DeferredBlock<StainedGlassStairsBlock> MAGENTA_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.MAGENTA);
	public static final DeferredBlock<StainedGlassStairsBlock> LIGHT_BLUE_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.LIGHT_BLUE);
	public static final DeferredBlock<StainedGlassStairsBlock> YELLOW_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.YELLOW);
	public static final DeferredBlock<StainedGlassStairsBlock> LIME_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.LIME);
	public static final DeferredBlock<StainedGlassStairsBlock> PINK_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.PINK);
	public static final DeferredBlock<StainedGlassStairsBlock> GRAY_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.GRAY);
	public static final DeferredBlock<StainedGlassStairsBlock> LIGHT_GRAY_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.LIGHT_GRAY);
	public static final DeferredBlock<StainedGlassStairsBlock> CYAN_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.CYAN);
	public static final DeferredBlock<StainedGlassStairsBlock> PURPLE_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.PURPLE);
	public static final DeferredBlock<StainedGlassStairsBlock> BLUE_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.BLUE);
	public static final DeferredBlock<StainedGlassStairsBlock> BROWN_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.BROWN);
	public static final DeferredBlock<StainedGlassStairsBlock> GREEN_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.GREEN);
	public static final DeferredBlock<StainedGlassStairsBlock> RED_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.RED);
	public static final DeferredBlock<StainedGlassStairsBlock> BLACK_STAINED_GLASS_STAIRS =
		createStainedGlassStairs(DyeColor.BLACK);
	
	private static <T extends Block> DeferredBlock<T> registerBlock(
		String idPath, Function<BlockBehaviour.Properties, T> blockBuilder,
		BlockBehaviour.Properties props)
	{
		System.out.println("Registering block & item for mo_glass:" + idPath);
		DeferredBlock<T> result =
			BLOCKS.registerBlock(idPath, blockBuilder, props);
		ITEMS.registerSimpleBlockItem(result);
		
		return result;
	}
	
	private static DeferredBlock<StainedGlassSlabBlock> createStainedGlassSlab(
		DyeColor color)
	{
		DeferredBlock<StainedGlassSlabBlock> result =
			registerBlock(color + "_stained_glass_slab",
				props -> new StainedGlassSlabBlock(color, props),
				BlockBehaviour.Properties.of().mapColor(color)
					.instrument(NoteBlockInstrument.HAT).strength(0.3F)
					.sound(SoundType.GLASS).noOcclusion()
					.isValidSpawn(MoGlassBlocks::never)
					.isRedstoneConductor(MoGlassBlocks::never)
					.isSuffocating(MoGlassBlocks::never)
					.isViewBlocking(MoGlassBlocks::never));
		
		STAINED_GLASS_SLABS.add(result);
		return result;
	}
	
	private static DeferredBlock<StainedGlassStairsBlock> createStainedGlassStairs(
		DyeColor color)
	{
		DeferredBlock<StainedGlassStairsBlock> result =
			registerBlock(color + "_stained_glass_stairs",
				props -> new StainedGlassStairsBlock(color, props),
				BlockBehaviour.Properties.of().mapColor(color)
					.instrument(NoteBlockInstrument.HAT).strength(0.3F)
					.sound(SoundType.GLASS).noOcclusion()
					.isValidSpawn(MoGlassBlocks::never)
					.isRedstoneConductor(MoGlassBlocks::never)
					.isSuffocating(MoGlassBlocks::never)
					.isViewBlocking(MoGlassBlocks::never));
		
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
