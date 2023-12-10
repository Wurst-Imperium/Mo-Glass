/*
 * Copyright (c) 2019-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.glass;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

@Mod(MoGlass.MODID)
@Mod.EventBusSubscriber(modid = MoGlass.MODID,
	bus = Mod.EventBusSubscriber.Bus.MOD)
public final class MoGlass
{
	public static final String MODID = "mo_glass";
	private static boolean initialized;
	
	public MoGlass()
	{
		if(initialized)
			throw new RuntimeException("MoGlass constructor ran twice!");
		
		initialized = true;
		System.out.println("Starting Mo Glass...");
		MoGlassBlocks.BLOCKS
			.register(FMLJavaModLoadingContext.get().getModEventBus());
		MoGlassBlocks.ITEMS
			.register(FMLJavaModLoadingContext.get().getModEventBus());
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
		for(RegistryObject<StainedGlassStairsBlock> stairs : MoGlassBlocks.STAINED_GLASS_STAIRS)
			event.accept(stairs);
		
		// slabs
		event.accept(MoGlassBlocks.GLASS_SLAB);
		event.accept(MoGlassBlocks.TINTED_GLASS_SLAB);
		for(RegistryObject<StainedGlassSlabBlock> slab : MoGlassBlocks.STAINED_GLASS_SLABS)
			event.accept(slab);
	}
}
