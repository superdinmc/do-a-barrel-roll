package nl.enjarai.doabarrelroll;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec2;

public class MomentumCrosshairWidget {

    public static void render(PoseStack matrices, int scaledWidth, int scaledHeight, Vec2 mouseTurnVec) {
        int color = 0xffffffff;
        int centerX = scaledWidth / 2;
        int centerY = scaledHeight / 2 - 1;
        var turnVec = mouseTurnVec.scale(50);
        var lineVec = turnVec.add(turnVec.negated().normalized().scale(Math.min(turnVec.length(), 10f)));

        if (!lineVec.equals(Vec2.ZERO)) {

            RenderSystem.enableBlend();
            RenderSystem.disableTexture();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            ModMath.forBresenhamLine(centerX, centerY, centerX + (int) lineVec.x, centerY + (int) lineVec.y, (x, y) -> {

                var matrix = matrices.last().pose();
                var bufferBuilder = Tesselator.getInstance().getBuilder();

                bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                bufferBuilder.vertex(matrix, (float) x, (float) y + 1, 0.0F).color(color).endVertex();
                bufferBuilder.vertex(matrix, (float) x + 1, (float) y + 1, 0.0F).color(color).endVertex();
                bufferBuilder.vertex(matrix, (float) x + 1, (float) y, 0.0F).color(color).endVertex();
                bufferBuilder.vertex(matrix, (float) x, (float) y, 0.0F).color(color).endVertex();
                BufferUploader.drawWithShader(bufferBuilder.end());

            });
            RenderSystem.enableTexture();
        }

        // change the position of the crosshair, which is rendered up the stack
        matrices.translate((int) turnVec.x, (int) turnVec.y, 0);
    }
}
