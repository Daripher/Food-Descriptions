package com.daripher.fooddesc;

import java.util.List;

import org.slf4j.Logger;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = FoodDescMod.MOD_ID)
@Mod(FoodDescMod.MOD_ID)
public class FoodDescMod {
    public static final String MOD_ID = "fooddesc";
    public static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void addFoodEffectTooltip(RenderTooltipEvent.GatherComponents event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null)
            return;
        FoodProperties foodProperties = event.getItemStack().getFoodProperties(minecraft.player);
        if (foodProperties == null)
            return;
        List<Pair<MobEffectInstance, Float>> effects = foodProperties.getEffects();
        if (!effects.isEmpty())
            event.getTooltipElements().add(Either.right(new FoodEffectTooltipComponent(effects)));
    }

    @EventBusSubscriber(modid = FoodDescMod.MOD_ID, bus = Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void registerFoodEffectTooltipComponent(RegisterClientTooltipComponentFactoriesEvent event) {
            event.register(FoodEffectTooltipComponent.class, FoodEffectTooltipRenderer::new);
        }
    }
}
