/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.mo_glass.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.fabric.api.block.v1.FabricBlock;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.wimods.mo_glass.MoGlass;
import net.wimods.mo_glass.MoGlassTags;

@Mixin(Block.class)
public abstract class BlockMixin extends BlockBehaviour
	implements ItemLike, FabricBlock
{
	private BlockMixin(MoGlass moGlass, Properties settings)
	{
		super(settings);
	}
	
	/**
	 * See {@link MoGlassTags#NON_OPAQUE_FOR_CULLING} for why this exists.
	 */
	@Inject(at = @At("HEAD"),
		method = "shouldRenderFace(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z",
		cancellable = true)
	private static void onShouldDrawSide(BlockState state,
		BlockState otherState, Direction direction,
		CallbackInfoReturnable<Boolean> cir)
	{
		if(!state.is(ConventionalBlockTags.GLASS_BLOCKS_TINTED)
			&& !state.is(MoGlassTags.NON_OPAQUE_FOR_CULLING)
			&& otherState.is(MoGlassTags.NON_OPAQUE_FOR_CULLING))
			cir.setReturnValue(true);
	}
}
