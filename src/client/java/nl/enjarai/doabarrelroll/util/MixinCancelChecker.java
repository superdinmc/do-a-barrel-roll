package nl.enjarai.doabarrelroll.util;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;

import java.io.IOException;

public class MixinCancelChecker {
    /**
     * Check if a mixin does not have the expected priority.
     * If priority is not set on the target mixin, expectedPriority should be 0.
     */
    public static boolean hasChangedPriority(String mixinClassName, int expectedPriority) {
        try {
            var classNode = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName);
            var annotationNode = Annotations.getInvisible(classNode, Mixin.class);
            var visitor = new Visitor();
            annotationNode.accept(visitor);

            return visitor.getPriority() != expectedPriority;
        } catch (ClassNotFoundException | IOException | NullPointerException e) {
            return false;
        }
    }

    static class Visitor extends AnnotationVisitor {
        private int priority;

        protected Visitor() {
            super(Opcodes.ASM9);
        }

        @Override
        public void visit(String name, Object value) {
            if (name.equals("priority")) {
                this.priority = (int) value;
            }
            super.visit(name, value);
        }

        public int getPriority() {
            return priority;
        }
    }
}
