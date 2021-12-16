/*
 * Copyright (c) 2019-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import net.wurstclient.glass.MoGlassTags;

@Mixin(ChunkLightProvider.class)
public class ChunkLightProviderMixin
{
	/**
	 * See {@link MoGlassTags#OPAQUE_FOR_LIGHTING} for why this exists.
	 */
	@Redirect(
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/block/BlockState;isOpaque()Z",
			ordinal = 0),
		method = "getStateForLighting(JLorg/apache/commons/lang3/mutable/MutableInt;)Lnet/minecraft/block/BlockState;")
	private boolean isOpaqueForLighting(BlockState blockState)
	{
		return blockState.isOpaque()
			|| blockState.isIn(MoGlassTags.OPAQUE_FOR_LIGHTING);
	}
	
	/**
	 * See {@link MoGlassTags#OPAQUE_FOR_LIGHTING} for why this exists.
	 */
	@Redirect(
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/block/BlockState;isOpaque()Z",
			ordinal = 0),
		method = "getOpaqueShape(Lnet/minecraft/block/BlockState;JLnet/minecraft/util/math/Direction;)Lnet/minecraft/util/shape/VoxelShape;")
	private boolean isOpaqueForLightingShape(BlockState blockState)
	{
		return blockState.isOpaque()
			|| blockState.isIn(MoGlassTags.OPAQUE_FOR_LIGHTING);
	}
}
