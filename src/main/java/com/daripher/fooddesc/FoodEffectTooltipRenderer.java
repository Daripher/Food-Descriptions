package com.daripher.fooddesc;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;

public class FoodEffectTooltipRenderer implements ClientTooltipComponent {
    private final FoodEffectTooltipComponent component;
    private final int spacing = Minecraft.getInstance().font.lineHeight + 2;

    public FoodEffectTooltipRenderer(FoodEffectTooltipComponent component) {
        this.component = component;
    }

    @Override
    public int getHeight() {
        return spacing * component.effects.size();
    }

    @Override
    public int getWidth(Font font) {
        int width = 0;
        for (int i = 0; i < component.effects.size(); i++) {
            MobEffectInstance effect = component.effects.get(i).getFirst();
            float chance = component.effects.get(i).getSecond();
            width = Math.max(width, font.width(getEffectDescription(effect, chance)) + 12);
        }
        return width;
    }

    @Override
    public void renderImage(Font pFont, int x, int y, PoseStack stack, ItemRenderer itemRenderer, int blitOffset) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        for (int i = 0; i < component.effects.size(); i++) {
            MobEffectInstance effect = component.effects.get(i).getFirst();
            TextureAtlasSprite texture = minecraft.getMobEffectTextures().get(effect.getEffect());
            RenderSystem.setShaderTexture(0, texture.atlas().location());
            GuiComponent.blit(stack, x, y + spacing * i, blitOffset, 9, 9, texture);
        }
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix, BufferSource buffer) {
        for (int i = 0; i < component.effects.size(); i++) {
            MobEffectInstance effect = component.effects.get(i).getFirst();
            float chance = component.effects.get(i).getSecond();
            Component description = getEffectDescription(effect, chance);
            font.drawInBatch(description, x + 12, y + 1 + spacing * i, 0xAABBCC, true, matrix, buffer, false, 0,
                    15728880);
        }
    }

    private Component getEffectDescription(MobEffectInstance effect, float chance) {
        MutableComponent description = Component.translatable(effect.getDescriptionId());
        String time = " (" + MobEffectUtil.formatDuration(effect, 1f) + ")";
        description.append(time);
        if (chance < 1f) {
            description.append(" (" + (int) (chance * 100) + "%)");
        }
        return description;
    }
}