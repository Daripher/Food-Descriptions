package daripher.fooddesc.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import org.jetbrains.annotations.NotNull;

public class FoodEffectTooltipRenderer implements ClientTooltipComponent {
  private final FoodEffectTooltipComponent component;

  public FoodEffectTooltipRenderer(FoodEffectTooltipComponent component) {
    this.component = component;
  }

  @Override
  public int getHeight() {
    return getSpacing() * component.effects().size();
  }

  @Override
  public int getWidth(@NotNull Font font) {
    int width = 0;
    for (int i = 0; i < component.effects().size(); i++) {
      width = Math.max(width, font.width(getEffectDescription(component.effects().get(i))) + 12);
    }
    return width;
  }

  @Override
  public void renderImage(
      @NotNull Font font,
      int x,
      int y,
      @NotNull PoseStack poseStack,
      @NotNull ItemRenderer itemRenderer,
      int blitOffset) {
    Minecraft minecraft = Minecraft.getInstance();
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    for (int i = 0; i < component.effects().size(); i++) {
      MobEffectInstance effect = component.effects().get(i).getFirst();
      renderEffectIcon(x, y + getSpacing() * i, poseStack, blitOffset, minecraft, effect);
    }
  }

  private void renderEffectIcon(
      int x,
      int y,
      @NotNull PoseStack poseStack,
      int blitOffset,
      Minecraft minecraft,
      MobEffectInstance effect) {
    TextureAtlasSprite sprite = minecraft.getMobEffectTextures().get(effect.getEffect());
    ResourceLocation textureLocation = sprite.atlas().location();
    RenderSystem.setShaderTexture(0, textureLocation);
    GuiComponent.blit(poseStack, x, y, blitOffset, 9, 9, sprite);
  }

  @Override
  public void renderText(
      @NotNull Font font, int x, int y, @NotNull Matrix4f matrix, @NotNull BufferSource buffer) {
    for (int i = 0; i < component.effects().size(); i++) {
      Component description = getEffectDescription(component.effects().get(i));
      font.drawInBatch(
          description,
          x + 12,
          y + 1 + getSpacing() * i,
          0xAABBCC,
          true,
          matrix,
          buffer,
          false,
          0,
          15728880);
    }
  }

  public int getSpacing() {
    return Minecraft.getInstance().font.lineHeight + 2;
  }

  private Component getEffectDescription(Pair<MobEffectInstance, Float> pair) {
    MobEffectInstance effect = pair.getFirst();
    float chance = pair.getSecond();
    MutableComponent description = Component.translatable(effect.getDescriptionId());
    if (effect.getAmplifier() > 0) {
      MutableComponent amplifier =
          Component.translatable("potion.potency." + effect.getAmplifier());
      description = Component.translatable("potion.withAmplifier", description, amplifier);
    }
    String time = " (" + MobEffectUtil.formatDuration(effect, 1f) + ")";
    description.append(time);
    if (chance < 1f) {
      description.append(" (" + (int) (chance * 100) + "%)");
    }
    return description;
  }
}
