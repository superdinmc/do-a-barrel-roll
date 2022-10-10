package nl.enjarai.doabarrelroll.moonlightconfigs.fabric;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Main config screen
 */
public class FabricConfigListScreen extends Screen {

    protected final Screen parent;
    protected final ConfigSpec[] configs;
    protected final Identifier background;
    private final ItemStack mainIcon;
    private final String modId;
    private final String modURL;

    protected ConfigList list;

    public FabricConfigListScreen(String modId, ItemStack mainIcon, Text displayName, @Nullable Identifier background,
                                  Screen parent,
                                  ConfigSpec... specs) {
        super(displayName);
        this.parent = parent;
        this.configs = specs;
        this.background = background;
        this.mainIcon = mainIcon;
        this.modId = modId;
        this.modURL = FabricLoader.getInstance().getModContainer(modId).get().getMetadata().getContact().get("homepage").orElse(null);
    }

    @Override
    protected void init() {
        this.list = new ConfigList(this.client, this.width, this.height, 32, this.height - 32, 40,
                this.configs);
        this.addDrawableChild(this.list);

        this.addExtraButtons();
    }

    protected void addExtraButtons() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155 + 160, this.height - 29, 150, 20,
                ScreenTexts.DONE, button -> this.client.setScreen(this.parent)));
    }

    @Override
    public void removed() {
    }

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        drawCenteredText(poseStack, this.textRenderer, this.title, this.width / 2, 15, 16777215);

        if (modURL != null && isMouseWithin((this.width / 2) - 90, 2 + 6, 180, 16 + 2, mouseX, mouseY)) {
            this.renderOrderedTooltip(poseStack, this.textRenderer.wrapLines(Text.translatable("gui.moonlight.open_mod_page", this.modId), 200), mouseX, mouseY);
        }
        int titleWidth = this.textRenderer.getWidth(this.title) + 35;
        this.itemRenderer.renderInGui(this.mainIcon, (this.width / 2) + titleWidth / 2 - 17, 2 + 8);
        this.itemRenderer.renderInGui(this.mainIcon, (this.width / 2) - titleWidth / 2, 2 + 8);
    }

    private boolean isMouseWithin(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (modURL != null && isMouseWithin((this.width / 2) - 90, 2 + 6, 180, 16 + 2, (int) mouseX, (int) mouseY)) {
            Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,  modURL));
            this.handleTextClick(style);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    protected class ConfigList extends ElementListWidget<ConfigButton> {

        public ConfigList(MinecraftClient minecraft, int width, int height, int y0, int y1, int itemHeight, ConfigSpec... specs) {
            super(minecraft, width, height, y0, y1, itemHeight);
            this.centerListVertically = true;
            this.setRenderSelection(false);
            for (var s : specs) {
                this.addEntry(new ConfigButton(s, this.width, this.getRowWidth()));
            }
        }

        @Override
        public int getRowWidth() {
            return 260;
        }

        @Override
        protected int getScrollbarPositionX() {
            return super.getScrollbarPositionX() + 32;
        }

        /*
        @Override
        protected int getRowTop(int index) {
            if (!this.centerListVertically) return super.getRowTop(index);
            return (y1 - y0) / 2 - (this.children().size() * itemHeight) / 2 +
                    this.y0 + 4 - (int) this.getScrollAmount() + index * this.itemHeight + this.headerHeight;
        }*/

        @Override
        public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTick) {
            this.renderBackground(poseStack);

            var background = FabricConfigListScreen.this.background;

            int i = this.getScrollbarPositionX();
            int j = i + 6;
            Tessellator tesselator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tesselator.getBuffer();
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            //this.hovered = this.isMouseOver((double)mouseX, (double)mouseY) ? this.getEntryAtPosition((double)mouseX, (double)mouseY) : null;

            RenderSystem.setShaderTexture(0, background);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            float f = 32.0F;
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex(this.left, this.bottom, 0.0).texture((float) this.left / 32.0F, (float) (this.bottom + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, 255).next();
            bufferBuilder.vertex(this.right, this.bottom, 0.0).texture((float) this.right / 32.0F, (float) (this.bottom + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, 255).next();
            bufferBuilder.vertex(this.right, this.top, 0.0).texture((float) this.right / 32.0F, (float) (this.top + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, 255).next();
            bufferBuilder.vertex(this.left, this.top, 0.0).texture((float) this.left / 32.0F, (float) (this.top + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, 255).next();
            tesselator.draw();


            int k = this.getRowLeft();
            int l = this.top + 4 - (int) this.getScrollAmount();


            this.renderList(poseStack, k, l, partialTick);

            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            RenderSystem.setShaderTexture(0, background);
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(519);


            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex(this.left, this.top, -100.0).texture(0.0F, (float) this.top / 32.0F).color(64, 64, 64, 255).next();
            bufferBuilder.vertex(this.left + this.width, this.top, -100.0).texture((float) this.width / 32.0F, (float) this.top / 32.0F).color(64, 64, 64, 255).next();
            bufferBuilder.vertex(this.left + this.width, 0.0, -100.0).texture((float) this.width / 32.0F, 0.0F).color(64, 64, 64, 255).next();
            bufferBuilder.vertex(this.left, 0.0, -100.0).texture(0.0F, 0.0F).color(64, 64, 64, 255).next();
            bufferBuilder.vertex(this.left, this.height, -100.0).texture(0.0F, (float) this.height / 32.0F).color(64, 64, 64, 255).next();
            bufferBuilder.vertex(this.left + this.width, this.height, -100.0).texture((float) this.width / 32.0F, (float) this.height / 32.0F).color(64, 64, 64, 255).next();
            bufferBuilder.vertex(this.left + this.width, this.bottom, -100.0).texture((float) this.width / 32.0F, (float) this.bottom / 32.0F).color(64, 64, 64, 255).next();
            bufferBuilder.vertex(this.left, this.bottom, -100.0).texture(0.0F, (float) this.bottom / 32.0F).color(64, 64, 64, 255).next();
            tesselator.draw();
            RenderSystem.depthFunc(515);
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
            RenderSystem.disableTexture();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);

            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            bufferBuilder.vertex(this.left, this.top + 4, 0.0).color(0, 0, 0, 0).next();
            bufferBuilder.vertex(this.right, this.top + 4, 0.0).color(0, 0, 0, 0).next();
            bufferBuilder.vertex(this.right, this.top, 0.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(this.left, this.top, 0.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(this.left, this.bottom, 0.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(this.right, this.bottom, 0.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(this.right, this.bottom - 4, 0.0).color(0, 0, 0, 0).next();
            bufferBuilder.vertex(this.left, this.bottom - 4, 0.0).color(0, 0, 0, 0).next();
            tesselator.draw();


            int o = this.getMaxScroll();
            if (o > 0) {
                RenderSystem.disableTexture();
                RenderSystem.setShader(GameRenderer::getPositionColorShader);
                int m = (int) ((float) ((this.bottom - this.top) * (this.bottom - this.top)) / (float) this.getMaxPosition());
                m = MathHelper.clamp(m, 32, this.bottom - this.top - 8);
                int n = (int) this.getScrollAmount() * (this.bottom - this.top - m) / o + this.top;
                if (n < this.top) {
                    n = this.top;
                }

                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
                bufferBuilder.vertex(i, this.bottom, 0.0).color(0, 0, 0, 255).next();
                bufferBuilder.vertex(j, this.bottom, 0.0).color(0, 0, 0, 255).next();
                bufferBuilder.vertex(j, this.top, 0.0).color(0, 0, 0, 255).next();
                bufferBuilder.vertex(i, this.top, 0.0).color(0, 0, 0, 255).next();
                bufferBuilder.vertex(i, n + m, 0.0).color(128, 128, 128, 255).next();
                bufferBuilder.vertex(j, n + m, 0.0).color(128, 128, 128, 255).next();
                bufferBuilder.vertex(j, n, 0.0).color(128, 128, 128, 255).next();
                bufferBuilder.vertex(i, n, 0.0).color(128, 128, 128, 255).next();
                bufferBuilder.vertex(i, n + m - 1, 0.0).color(192, 192, 192, 255).next();
                bufferBuilder.vertex(j - 1, n + m - 1, 0.0).color(192, 192, 192, 255).next();
                bufferBuilder.vertex(j - 1, n, 0.0).color(192, 192, 192, 255).next();
                bufferBuilder.vertex(i, n, 0.0).color(192, 192, 192, 255).next();
                tesselator.draw();
            }

            this.renderDecorations(poseStack, mouseX, mouseY);
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
        }
    }

    protected class ConfigButton extends ElementListWidget.Entry<ConfigButton> {

        private final List<ClickableWidget> children;

        private ConfigButton(ClickableWidget widget) {
            this.children = List.of(widget);
        }

        protected ConfigButton(ConfigSpec spec, int width, int buttonWidth) {
            this(new ButtonWidget(width / 2 - buttonWidth / 2, 0, buttonWidth, 20, Text.literal(spec.getFileName()), (b) ->
                    MinecraftClient.getInstance().setScreen(spec.makeScreen(FabricConfigListScreen.this, FabricConfigListScreen.this.background))));
        }

        @Override
        public void render(MatrixStack poseStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            this.children.forEach((button) -> {
                button.y = top;
                button.render(poseStack, mouseX, mouseY, partialTick);
            });
        }

        @Override
        public List<? extends Element> children() {
            return this.children;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return this.children;
        }
    }

}

