package nl.enjarai.doabarrelroll;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector2d;

public class MomentumCrosshairWidget {

    public static void render(MatrixStack matrices, int scaledWidth, int scaledHeight, Vector2d mouseTurnVec) {
        int color = 0xffffffff;
        int centerX = scaledWidth / 2;
        int centerY = scaledHeight / 2 - 1;
        mouseTurnVec.mul(50);
        var lineVec = new Vector2d(mouseTurnVec).add(
                new Vector2d(mouseTurnVec).negate().normalize().mul(Math.min(mouseTurnVec.length(), 10f)));

        if (!lineVec.equals(new Vector2d()) && mouseTurnVec.lengthSquared() > 10f * 10f) {

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            ModMath.forBresenhamLine(centerX, centerY, centerX + (int) lineVec.x, centerY + (int) lineVec.y, (x, y) -> {

                var matrix = matrices.peek().getPositionMatrix();
                var bufferBuilder = Tessellator.getInstance().getBuffer();

                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
                bufferBuilder.vertex(matrix, (float) x, (float) y + 1, 0.0F).color(color).next();
                bufferBuilder.vertex(matrix, (float) x + 1, (float) y + 1, 0.0F).color(color).next();
                bufferBuilder.vertex(matrix, (float) x + 1, (float) y, 0.0F).color(color).next();
                bufferBuilder.vertex(matrix, (float) x, (float) y, 0.0F).color(color).next();
                BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

            });
        }

        // change the position of the crosshair, which is rendered up the stack
        matrices.translate((int) mouseTurnVec.x, (int) mouseTurnVec.y, 0);
    }
}
