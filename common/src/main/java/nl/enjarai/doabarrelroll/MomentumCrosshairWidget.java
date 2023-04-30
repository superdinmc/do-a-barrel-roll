package nl.enjarai.doabarrelroll;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import nl.enjarai.doabarrelroll.util.Vec2d;

public class MomentumCrosshairWidget {

    public static void render(MatrixStack matrices, int scaledWidth, int scaledHeight, Vec2d mouseTurnVec) {
        int alpha = 0xff;
        int red = 0xff;
        int green = 0xff;
        int blue = 0xff;

        int centerX = scaledWidth / 2;
        int centerY = scaledHeight / 2 - 1;
        var turnVec = mouseTurnVec.multiply(50);
        var lineVec = turnVec.add(turnVec.negate().normalize().multiply(Math.min(turnVec.length(), 10f)));

        if (!lineVec.equals(Vec2d.ZERO)) {

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
            ModMath.forBresenhamLine(centerX, centerY, centerX + (int) lineVec.x, centerY + (int) lineVec.y, (x, y) -> {

                var matrix = matrices.peek().getModel();
                var bufferBuilder = Tessellator.getInstance().getBuffer();

                bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
                bufferBuilder.vertex(matrix, (float) x, (float) y + 1, 0.0F).color(alpha, red, green, blue).next();
                bufferBuilder.vertex(matrix, (float) x + 1, (float) y + 1, 0.0F).color(alpha, red, green, blue).next();
                bufferBuilder.vertex(matrix, (float) x + 1, (float) y, 0.0F).color(alpha, red, green, blue).next();
                bufferBuilder.vertex(matrix, (float) x, (float) y, 0.0F).color(alpha, red, green, blue).next();
                bufferBuilder.end();
                BufferRenderer.draw(bufferBuilder);
            });
        }

        // change the position of the crosshair, which is rendered up the stack
        matrices.translate((int) turnVec.x, (int) turnVec.y, 0);
    }
}