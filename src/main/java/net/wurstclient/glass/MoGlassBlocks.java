/*
 * Copyright (c) 2019-2024 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass;

import java.util.Arrays;
import java.util.List;
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
				.isValidSpawn(MoGlassBlocks::never)
				.isRedstoneConductor(MoGlassBlocks::never)
				.isSuffocating(MoGlassBlocks::never)
				.isViewBlocking(MoGlassBlocks::never));
	
	public static final DeferredBlock<Block> GLASS_STAIRS =
		registerBlock("glass_stairs", GlassStairsBlock::new,
			BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT)
				.strength(0.3F).sound(SoundType.GLASS).noOcclusion()
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
	
	public static final List<DeferredBlock<StainedGlassSlabBlock>> STAINED_GLASS_SLABS =
		Arrays.stream(DyeColor.values())
			.map(color -> createStainedGlassSlab(color)).toList();
	
	public static final List<DeferredBlock<StainedGlassStairsBlock>> STAINED_GLASS_STAIRS =
		Arrays.stream(DyeColor.values())
			.map(color -> createStainedGlassStairs(color)).toList();
	
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
		return registerBlock(color + "_stained_glass_slab",
			props -> new StainedGlassSlabBlock(color, props),
			BlockBehaviour.Properties.of().mapColor(color)
				.instrument(NoteBlockInstrument.HAT).strength(0.3F)
				.sound(SoundType.GLASS).noOcclusion()
				.isValidSpawn(MoGlassBlocks::never)
				.isRedstoneConductor(MoGlassBlocks::never)
				.isSuffocating(MoGlassBlocks::never)
				.isViewBlocking(MoGlassBlocks::never));
	}
	
	private static DeferredBlock<StainedGlassStairsBlock> createStainedGlassStairs(
		DyeColor color)
	{
		return registerBlock(color + "_stained_glass_stairs",
			props -> new StainedGlassStairsBlock(color, props),
			BlockBehaviour.Properties.of().mapColor(color)
				.instrument(NoteBlockInstrument.HAT).strength(0.3F)
				.sound(SoundType.GLASS).noOcclusion()
				.isValidSpawn(MoGlassBlocks::never)
				.isRedstoneConductor(MoGlassBlocks::never)
				.isSuffocating(MoGlassBlocks::never)
				.isViewBlocking(MoGlassBlocks::never));
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
