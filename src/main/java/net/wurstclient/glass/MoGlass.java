/*
 * Copyright (c) 2019-2024 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;

@Mod(MoGlass.MODID)
@EventBusSubscriber(modid = MoGlass.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class MoGlass
{
	public static final String MODID = "mo_glass";
	private static boolean initialized;
	
	public MoGlass(IEventBus modBus, ModContainer container)
	{
		if(initialized)
			throw new RuntimeException("MoGlass constructor ran twice!");
		
		initialized = true;
		System.out.println("Starting Mo Glass...");
		MoGlassBlocks.BLOCKS.register(modBus);
		MoGlassBlocks.ITEMS.register(modBus);
	}
	
	@SubscribeEvent
	public static void onBuildCreativeTabContents(
		BuildCreativeModeTabContentsEvent event)
	{
		if(event.getTabKey() != CreativeModeTabs.COLORED_BLOCKS)
			return;
		
		// stairs
		event.accept(MoGlassBlocks.GLASS_STAIRS);
		event.accept(MoGlassBlocks.TINTED_GLASS_STAIRS);
		for(DeferredBlock<StainedGlassStairsBlock> stairs : MoGlassBlocks.STAINED_GLASS_STAIRS)
			event.accept(stairs);
		
		// slabs
		event.accept(MoGlassBlocks.GLASS_SLAB);
		event.accept(MoGlassBlocks.TINTED_GLASS_SLAB);
		for(DeferredBlock<StainedGlassSlabBlock> slab : MoGlassBlocks.STAINED_GLASS_SLABS)
			event.accept(slab);
	}
}
