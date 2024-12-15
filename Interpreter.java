import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;
import java.util.regex.*;

public class Interpreter {

    //      regex for every possible assignment
    static String equals = "=";
    static String letter = "_|[a-z]|[A-Z]";
    static String literal = "0|[1-9][0-9]*";
    static String identifier = "[_a-zA-Z][_a-zA-Z0-9]*"; // \\s* means any space in front of the identifier or none

    public static void main(String[] args) throws FileNotFoundException {

        //all data is in the form of x, y, z

//        System.out.println("bruh");
        Scanner scnr = new Scanner(new File("input.txt"));

//        while (scnr.hasNextLine()) {
//            String line = scnr.nextLine();
//
//            String[] results = interpret(line);
//
//        }
        interpret("x = 1;y = 2;z = ---(x+y)*(x+-y);");
    }

    public static String[] interpret(String line){
        boolean passed = true;
        //using maps to map the identifier(key) to the expression(value)
        String[] results = new String[20];
//      return empty map if any syntax error


        String[] tokens = line.split(";");

        for(String token : tokens){
            if (!assignment(token)) passed = false;
        }

        System.out.println("passed: " + passed);
        return results;
    }

    public static boolean assignment(String token){

//      Assignment has to be Identifier = Exp;
        String[] tokens = token.split(" ");

        for(String p : tokens) System.out.println("token is: " + p + ":");

//      first test if identifier
        if(!Pattern.matches(identifier,tokens[0])){
            System.out.println("syntax error, not an identifier");
            return false;
        }

//      then test if =
        if(!Pattern.matches(equals,tokens[1])){
            System.out.println("syntax error, not an =");
            return false;
        }

//      then test if exp, semicologn has no space so remove a char
        System.out.println("exp is " + tokens[2]);
        if(!exp(tokens[2])){
            System.out.println("syntax error, not an exp");
            return false;
        }

//      if passed through, it is correct
        return true;
    }

    public static boolean term(String token){

//      term can be Term * Fact  | Fact
//      every * must follow a fact, and be before a fact.

        //if * start, invalid, if * end, invalid

        if(token.charAt(0) == '*' || token.charAt(token.length()-1) == '*') return false;

//        split the token into two at that * and check if the splits are term and fact
        for(int i = 1;i < token.length() - 1;i++){
            System.out.println("this empty?" + token.substring(0,i) + ":");
            if(token.charAt(i) == '*') return term(token.substring(0,i)) && fact(token.substring(i+1));
        }

//      if none of those, it has to be fact or invalid
//        System.out.println("passed this test");
        System.out.println("fact is " + token);
        return fact(token);
    }


    public static boolean exp(String token){
//        exp can be Exp + Term | Exp - Term | Term

//        first test if it finds + or - in between, because then it has to be an exp + term or exp - term
//        if(token.charAt(token.length()-1) == '-' || token.charAt(token.length()-1) == '+')


        for(int i = 1;i < token.length() - 1;i++){
            if(token.charAt(i) == '-' || token.charAt(i) == '+'){
//                System.out.println("found -");
//                System.out.println(token.substring(1,token.length()));
//                System.out.println(token.substring(1,i));
//                System.out.println(token.substring(i+1,token.length()-1));
                //has to be a fact if - or + is first
                if(i == 1){
                    System.out.println(token.substring(1,token.length()));
                    return fact(token.substring(1,token.length()));
                }
                //if not first, it has to be inbetween
                System.out.println("this empty exp?" + token.substring(1,i) + ":");
                return exp(token.substring(1,i)) && term(token.substring(i+1,token.length()));
            }
        }

//      if not any of those it will get here, so now it has to be term or its false
        System.out.println("term is " + token);
        System.out.println("this empty term?" + token + ":");
        return term(token);
    }
//
//
//
    public static boolean fact(String token){
//        fact can be ( Exp ) | - Fact | + Fact | Literal | Identifier

//        //exp if inside () which we are
        if(token.charAt(0) == '(') {
//            find the )
            for(int i = 1; i < token.length();i++){
                if(token.charAt(token.length()-1) == ')'){
                    System.out.println("this empty exp??" + token.substring(1,i) + ":");
                    return exp(token.substring(1,i));
                }
            }

        }

        //if start with + or -, must be another fact after that
        if((token.charAt(0) == '+')){
            return fact(token.substring(1));
        }
        else if((token.charAt(0) == '-')){
            return fact(token.substring(1));
        }
//      if starts with letter it has to be identifier or else it is syntax error
        else if((token.charAt(0) == '_' || Character.isLetter(token.charAt(0)))){
            return Pattern.matches(letter,token);
        }
        //if starts with digit it has to be literal or else error
        else if(Character.isDigit(token.charAt(0))){
            return Pattern.matches(literal,token);
        }

//      if none of those cases were started, then it cannot be a fact either

        return false;
    }



}