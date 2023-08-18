package nl.enjarai.doabarrelroll.math;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SyntaxHighlighter {
	public static MutableText highlightText(String str) {
		MutableText formattedText = Text.literal("");
		SyntaxHighlightContext context = new SyntaxHighlightContext(str);
		
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
	
	public static boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}
	
	public static boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
	}
	
	public static MutableText formatChar(char ch, SyntaxType type) {
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

class SyntaxHighlightContext {
	public int position = 0;
	public String rawText;
	
	public SyntaxHighlightContext(String raw) {
		this.rawText = raw;
	}
	
	public char getCurrent() {
		if (position >= rawText.length()) return (char)0;
		return rawText.charAt(position);
	}
}

enum SyntaxType {
	Variable,
	Operator,
	Error,
	Number
}