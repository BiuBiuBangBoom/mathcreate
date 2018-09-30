package po;

import java.util.Stack;

public class Fraction {
	private int numerator = 0;
	private int denominator = 1;
	private int symbol;
	
	
	public int getNumerator() {
		return numerator;
	}



	public void setNumerator(int numerator) {
		this.numerator = numerator;
	}



	public int getDenominator() {
		return denominator;
	}



	public void setDenominator(int denominator) {
		this.denominator = denominator;
	}



	public int getSymbol() {
		return symbol;
	}



	public void setSymbol(int symbol) {
		this.symbol = symbol;
	}

	public Fraction(int number1, int number2) {
		numerator = Math.abs(number1);
		denominator = Math.abs(number2);
		symbol = number1 * number2 < 0 ? -1 : 1;
		if(numerator != 0) 
			this.simplify();
	}
	
	

	private int gcd(int a, int b) {
        int mod = a % b;
        return mod == 0 ? b : gcd(b, mod);
    }

	private void simplify() {
		
		int a = gcd(numerator, denominator);	
		if(a == 0) System.out.println("ERROR");
		numerator /= a;
		denominator /= a;
	}

	public Fraction add(Fraction addend) {
		int newnum = this.symbol * this.numerator * addend.denominator
				+ addend.symbol * addend.numerator * this.denominator;
		int newden = this.denominator * addend.denominator;
		return new Fraction(newnum, newden);
	}

	public Fraction sub(Fraction subtrahend) {
		int newnum = this.symbol * this.numerator * subtrahend.denominator
				- subtrahend.symbol * subtrahend.numerator * subtrahend.denominator;
		int newden = this.denominator * subtrahend.denominator;
		return new Fraction(newnum, newden);
	}

	public Fraction mul(Fraction multiplicator) {
		int newnum = this.symbol * this.numerator * multiplicator.symbol * multiplicator.numerator;
		int newden = this.denominator * multiplicator.denominator;
		return new Fraction(newnum, newden);
	}

	public Fraction div(Fraction divisor) {
		int newnum = this.symbol * this.numerator * divisor.symbol * divisor.denominator;
		int newden = this.denominator * divisor.numerator;
		return new Fraction(newnum, newden);
	}

	public static Fraction calculateStringExp(String sIn) {
        Stack<Fraction> fractionStack = new Stack<>();
        Stack<Character> symbolStack = new Stack<>();
        String s = sIn.replaceAll(" ","");
        s += "=";
        StringBuffer temp = new StringBuffer();
        for(int i = 0; i < s.length();i++) {
            char ch = s.charAt(i);
            if((ch >= '0' && ch <= '9') || ch == '\'' || ch == '/')
                temp.append(ch);
            else {
                String tempStr = temp.toString();
                if(!tempStr.isEmpty()) {
                    Fraction f = Fraction.string2Fraction(tempStr);
                    fractionStack.push(f);
                    temp = new StringBuffer();
                }
                while(!comparePriority(ch, symbolStack) && !symbolStack.empty()) {
                    Fraction numberB = fractionStack.pop();
                    Fraction numberA = fractionStack.pop();
                    switch(symbolStack.pop()) {
                        case '+':
                            fractionStack.push(numberA.add(numberB));
                            break;
                        case '-':
                            fractionStack.push(numberA.sub(numberB));
                            break;
                        case 'x':
                            fractionStack.push(numberA.mul(numberB));
                            break;
                        case '÷':
                            fractionStack.push(numberA.div(numberB));
                            break;
                        default:
                            break;
                    }
                }
                if(ch != '=') {
                    symbolStack.push(ch);
                    if (ch == ')') {
                        symbolStack.pop();
                        symbolStack.pop();
                    }
                }
            }
        }
        return fractionStack.pop();
    }

	private static boolean comparePriority(char symbol, Stack<Character> symbolStack) {
		if (symbolStack.empty())
			return true;
		char top = symbolStack.peek();
		if (top == '(')
			return true;
		switch (symbol) {
		case '(':
			return true;
		case '×':
			if (top == '+' || top == '-')
				return true;
			else
				return false;
		case '÷':
			if (top == '+' || top == '-')
				return true;
			else
				return false;
		case '+':
			return false;
		case '-':
			return false;
			
		case ')':
			return false;
		case '=':
			return false;
		default:
			break;
		}
		return true;
	}

	public static Fraction string2Fraction(String s) {
		int curnum = 0;
		int curden = 1;
		String[] strings = s.substring(s.indexOf("'") + 1).split("/");
		if (strings.length <= 0)
			throw new NumberFormatException();
		curnum = Integer.parseInt(strings[0]);
		if (strings.length >= 2)
			curden = Integer.parseInt(strings[1]);
		if (s.contains("'"))
			curnum = Integer.parseInt(s.substring(0, s.indexOf("'"))) * curden + curnum;
		return new Fraction(curnum, curden);
	}
	
	public String toString() {
		StringBuilder fs = new StringBuilder();
		if(numerator == 0)
			fs.append(0);
		else if(numerator > denominator) {
			fs.append(numerator / denominator);
			if(numerator % denominator != 0) {
				fs.append("'");
				fs.append(numerator % denominator);
				fs.append("/");
				fs.append(denominator);
			}
		} else if(numerator < denominator) {
			fs.append(numerator);
			fs.append("/");
			fs.append(denominator);
		} else
			fs.append(1);
		return fs.toString();
	}
}
