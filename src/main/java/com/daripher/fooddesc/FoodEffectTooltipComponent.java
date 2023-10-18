package com.daripher.fooddesc;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class FoodEffectTooltipComponent implements TooltipComponent {
    public final List<Pair<MobEffectInstance, Float>> effects;

    public FoodEffectTooltipComponent(List<Pair<MobEffectInstance, Float>> effects) {
        this.effects = effects;
    }
}