package daripher.fooddesc.tooltip;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record FoodEffectTooltipComponent(List<Pair<MobEffectInstance, Float>> effects)
    implements TooltipComponent {}
