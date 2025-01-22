/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import net.wurstclient.glass.MoGlass;
import net.wurstclient.glass.MoGlassTags;

@Mixin(Block.class)
public abstract class BlockMixin extends BlockBehaviour
	implements ItemLike, IBlockExtension
{
	private BlockMixin(MoGlass moGlass, BlockBehaviour.Properties settings)
	{
		super(settings);
	}
	
	/**
	 * See {@link MoGlassTags#NON_OPAQUE_FOR_CULLING} for why this exists.
	 */
	@Inject(at = @At("HEAD"),
		method = "shouldRenderFace(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z",
		cancellable = true)
	private static void onShouldRenderFace(BlockGetter level, BlockPos pos,
		BlockState state, BlockState otherState, Direction direction,
		CallbackInfoReturnable<Boolean> cir)
	{
		if(!state.is(Tags.Blocks.GLASS_BLOCKS_TINTED)
			&& !state.is(MoGlassTags.NON_OPAQUE_FOR_CULLING)
			&& otherState.is(MoGlassTags.NON_OPAQUE_FOR_CULLING))
			cir.setReturnValue(true);
	}
}
