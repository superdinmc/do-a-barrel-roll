package nl.enjarai.doabarrelroll.math;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SyntaxHighlighter {
	public static Text highlightText(String text) {
		MutableText formattedText = Text.literal("");
		SyntaxHighlightContext context = new SyntaxHighlightContext(text);
		
		while (context.getCurrent() != (char)0) {
			if (context.getCurrent() == '$') { //variables
				formattedText.append(String.valueOf(context.getCurrent()));
				context.position++;
				
				while (isLetter(context.getCurrent()) || context.getCurrent() == '_') {
					formattedText.append(formatText(context.getCurrent(), SyntaxType.Variable));
					context.position++;
				}
			} else if (context.getCurrent() == '-' || context.getCurrent() == '+') { //unary operators
				if (Character.isDigit(context.peek())) {
					formattedText.append(formatText(context.getCurrent(), SyntaxType.Number));
					context.position++;
				} else if (isLetter(context.peek())) {
					formattedText.append(formatText(context.getCurrent(), SyntaxType.Function));
					context.position++;
				}
			} else if (Character.isDigit(context.getCurrent()) || context.getCurrent() == '.') { //numbers
				formattedText.append(formatText(context.getCurrent(), SyntaxType.Number));
				context.position++;
			} else if (isLetter(context.getCurrent())) { //functions
				StringBuilder builder = new StringBuilder();

				while (isLetter(context.getCurrent())) {
					builder.append(context.getCurrent());
					context.position++;
				}

				String builtResult = builder.toString();

				if (isKeyword(builtResult) && context.getCurrent() == '(') {
					formattedText.append(formatText(builtResult, SyntaxType.Function));
				} else {
					formattedText.append(formatText(builtResult, SyntaxType.Error));
				}
			} else if (isOperator(context.getCurrent())) { //typical operators
				formattedText.append(formatText(context.getCurrent(), SyntaxType.Operator));
				context.position++;
			} else if (isScope(context.getCurrent())) { //parentheses
				formattedText.append(formatText(context.getCurrent(), SyntaxType.Scope));
				context.position++;
			} else if (Character.isWhitespace(context.getCurrent())) { //whitespace
				formattedText.append(String.valueOf(context.getCurrent()));
				context.position++;
			} else { //errors
				formattedText.append(formatText(context.getCurrent(), SyntaxType.Error));
				context.position++;
			}
		}
		
		return formattedText;
	}
	
	public static boolean isKeyword(String str) {
		switch (str) {
			case "sqrt", "sin", "cos",
					"tan", "asin", "acos",
					"atan", "abs", "exp",
					"ceil", "floor", "log",
					"round", "randint", "min",
					"max" -> {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}
	
	public static boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
	}
	
	public static boolean isScope(char c) {
		 return c == ',' || c == '(' || c == ')';
	}
	
	public static MutableText formatText(char ch, SyntaxType type) {
		String str = String.valueOf(ch);
		return formatText(str, type);
	}
	
	public static MutableText formatText(String str, SyntaxType type) {
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
			
			case Function -> {
				return Text.literal(str).formatted(Formatting.BLUE);
			}
			
			case Scope -> {
				return Text.literal(str);
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
	
	public String peek(int amount) {
		if (position + amount >= rawText.length()) return null;
		return rawText.substring(position, position + amount);
	}
	
	public char peek() {
		return getByIndex(position + 1);
	}
	
	public char getCurrent() {
		return getByIndex(position);
	}
	
	public char getByIndex(int i) {
		if (i >= rawText.length()) return (char)0;
		return rawText.charAt(1);
	}
}

enum SyntaxType {
	Variable,
	Operator,
	Error,
	Scope,
	Function,
	Number
}