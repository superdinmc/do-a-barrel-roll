package nl.enjarai.doabarrelroll.compat.yacl;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.string.IStringController;
import dev.isxander.yacl3.gui.controllers.string.StringControllerElement;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nl.enjarai.doabarrelroll.math.ExpressionParser;

public record ExpressionParserController(Option<ExpressionParser> option) implements IStringController<ExpressionParser> {
    @Override
    public String getString() {
        return option.pendingValue().getString();
    }

    @Override
    public void setFromString(String value) {
        option.requestSet(new ExpressionParser(value));
    }
    
    @Override
    public Text formatValue() {
        var context = new SyntaxHighlightingContext(getString());
        MutableText formattedText = Text.literal("");
        
        while (context.getCurrent() != (char)0) {
            if (context.getCurrent() == '$') {
                formattedText.append(formatChar(context.getCurrent(), SyntaxType.Variable));
                context.position++;
                
                while (isLetter(context.getCurrent()) || context.getCurrent() == '_') {
                    formattedText.append(formatChar(context.getCurrent(), SyntaxType.Variable));
                    context.position++;
                }
            } else if (Character.isDigit(context.getCurrent()) || context.getCurrent() == '.') {
                formattedText.append(formatChar(context.getCurrent(), SyntaxType.Number));
                context.position++;
            } else if (isOperator(context.getCurrent())) {
                formattedText.append(formatChar(context.getCurrent(), SyntaxType.Operator));
                context.position++;
            } else if (Character.isWhitespace(context.getCurrent())) {
                formattedText.append(String.valueOf(context.getCurrent()));
                context.position++;
            } else {
                formattedText.append(formatChar(context.getCurrent(), SyntaxType.Error));
                context.position++;
            }
        }
        
        return formattedText;
    }
    
    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new StringControllerElement(this, screen, widgetDimension, true) {
            @Override
            protected int getValueColor() {
                return option.pendingValue().hasError() ? 0xFF5555 : super.getValueColor();
            }
        };
    }
    
    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }
    
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
    }
    
    private MutableText formatChar(char ch, SyntaxType type) {
        String str = String.valueOf(ch);
        
        switch (type) {
            case Variable -> {
                return Text.literal(str).formatted(Formatting.GREEN);
            }
            
            case Operator -> {
                return Text.literal(str).formatted(Formatting.LIGHT_PURPLE);
            }
            
            case Error -> {
                return Text.literal(str).formatted(Formatting.RED);
            }
            
            case Number -> {
                return Text.literal(str).formatted(Formatting.AQUA);
            }
        }
        
        return null;
    }
}

class SyntaxHighlightingContext {
    public int position = 0;
    public String rawText;
    
    public SyntaxHighlightingContext(String raw) {
        this.rawText = raw;
    }
    
    char getCurrent() {
        if (position >= rawText.length()) return (char)0;
        return rawText.charAt(position);
    }
}

enum SyntaxType {
    Variable,
    Operator,
    Error, Number
}