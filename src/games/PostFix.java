package games;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PostFix {

	private List<String> tokens; 
	private String infix;
	
	public PostFix(String infix) {
		this.infix = infix;
		try {
			infixToPostfix();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int Prec(char ch)
    {
        switch (ch)
        {
        case '+':
        case '-':
            return 1;
      
        case '*':
        case '/':
            return 2;
        }
        return -1;
    }
	
    private void infixToPostfix() throws Exception
    {
		this.tokens = new ArrayList<String>();

        // initializing empty String for result
        String result = new String("");
        // initializing empty stack
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i<infix.length(); ++i)
        {
            char c = infix.charAt(i);
             
             // If the scanned character is an operand, add it to output.
            if (Character.isLetterOrDigit(c))
            {
                result += (c + " ");
                tokens.add(""+c);
            }
            // If the scanned character is an '(', push it to the stack.
            else if (c == '(')
                stack.push(c);
             
            //  If the scanned character is an ')', pop and output from the stack 
            // until an '(' is encountered.
            else if (c == ')')
            {
                while (!stack.isEmpty() && stack.peek() != '(')
                {
                	char temp = stack.pop();
                    result += (temp + " ");
                    tokens.add(""+temp);
                }
                 
                if (!stack.isEmpty() && stack.peek() != '(')
                    throw new Exception("WTF"); // invalid expression                
                else
                    stack.pop();
            }
            else // an operator is encountered
            {
                while (!stack.isEmpty() && Prec(c) <= Prec(stack.peek()))
                {
                	char temp = stack.pop();
                    result += (temp + " ");
                    tokens.add(""+temp);
                }                
                stack.push(c);
            }
      
        }
        // pop all the operators from the stack
        while (!stack.isEmpty())
        {
        	char temp = stack.pop();
            result += (temp + " ");
            tokens.add(""+temp);

        }
        System.out.println("PostFix Res " + result);
    }
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(String s: tokens) {
			sb.append(" "+s);
		}
		sb.deleteCharAt(0);
		return sb.toString();
	}
	
	public double evaluate() {
		// Assume that the postfix is always valid
		Stack<Double> stack = new Stack<Double>();
		for(String token: tokens) {
			if(token.equals("+")) {
				double a = stack.pop();
				double b = stack.pop();
				stack.push(b+a);
			} else if(token.equals("-")) {
				double a = stack.pop();
				double b = stack.pop();
				stack.push(b-a);
			} else if(token.equals("*")) {
				double a = stack.pop();
				double b = stack.pop();
				stack.push(b*a);	
			} else if(token.equals("/")) {
				double a = stack.pop();
				double b = stack.pop();
				stack.push(b/a);	
			} else {
				double val;
				switch (token)
				{
				case "A":
					val = 1;
					break;
				case "J":
					val = 11;
					break;
				case "Q": 
					val = 12;
					break;
				case "K":
					val = 13;
					break;
				default:
					val = Double.valueOf(token);
				}
				stack.push(val);
			}
		}
		return stack.pop();
	}
}
