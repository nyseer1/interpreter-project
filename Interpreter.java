import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;
import java.util.regex.*;
import org.mariuszgromada.math.mxparser.*;

public class Interpreter {

    //      regex for every possible assignment
    static String equals = "=";
    static String letter = "_|[a-z]|[A-Z]";
    static String literal = "0|[1-9][0-9]*";
    static String identifier = "[_a-zA-Z][_a-zA-Z0-9]*"; // \\s* means any space in front of the identifier or none
    static ArrayList<String> constants = new ArrayList<>();
    static Constant[] list = new Constant[50];


    public static void main(String[] args) throws FileNotFoundException {

        //dependency for parser package
        License.iConfirmNonCommercialUse("Nyseer");

        //all data is in the form of x, y, z, c
        Scanner scnr = new Scanner(new File("input.txt"));

        while (scnr.hasNextLine()) {
            String line = scnr.nextLine();

            if (interpret(line)) parse(line);

        }
//          test
//        if ( interpret("x = 1;y = 2;z = ---(x+y)*(x+-y);c;") ) parse("x = 1;y = 2;z = ---(x+y)*(x+-y);c;");
    }

//    implements variable assignment to parser package and prints out each variables assignment
    public static int parse(String input){
//      initialize variable count, resets on every new set of assignment
        int var = 0;
        int storage = 0;

//        split up the assignments
        String[] tokens = input.split(";");

//        for each assignment do this:
        for(String assignments : tokens){

            //split into indentifer = exp
            String[] splits = assignments.split(" ");

            //if the variable is not defined, add it to the list of constants, and the arraylist to find constants, then increment var count
            if(!constants.contains(splits[0])){
                constants.add(splits[0]);

                if(splits.length > 2){
                    list[var] = new Constant(splits[0],new Expression(splits[2],list).calculate());
                }

            }
            Expression e = new Expression("",list);
            //splits[2] is the exp, evaluates the expression
            if(splits.length > 2){
                e = new Expression(splits[2],list);
            }
//            print the evaluated expression based on which var it is
            switch (var) {
                case 0:
                    System.out.println(splits[0] + " = " + e.calculate());
                    list[var] = new Constant("x", e.calculate());
                    constants.add("x");
                    var++;
                    break;
                case 1:
                    System.out.println(splits[0] + " = " + e.calculate());
                    list[var] = new Constant("y", e.calculate());
                    constants.add("y");
                    var++;
                    break;
                case 2:
                    System.out.println(splits[0] + " = " + e.calculate());
                    list[var] = new Constant("z", e.calculate());
                    constants.add("z");
                    var++;
                    break;
                case 3:
                    System.out.println(splits[0] + " = " + e.calculate());
                    list[var] = new Constant("c", e.calculate());
                    constants.add("c");
                    var++;
                    break;
            }
        }

//        line break
        System.out.println("");

        return storage;

    }
//    tests each assignment in a line if valid assignment using regex
    public static boolean interpret(String line){
        boolean passed = true;
        //using maps to map the identifier(key) to the expression(value)
        String[] results = new String[20];
//      return empty map if any syntax error


        String[] tokens = line.split(";");

        for(String token : tokens){
            if (!assignment(token)) passed = false;
        }
        System.out.println("passed: " + passed + " for the statement: " + line);
        return passed;
    }
//    individual assignment test, returns true if valid, false if invalid assignment
    public static boolean assignment(String token){

//      Assignment has to be Identifier = Exp;
        String[] tokens = token.split(" ");

//        for(String p : tokens) System.out.println("token is: " + p + ":");
        ;
        if(tokens.length < 1){
            System.out.println("empty semicologn, ignored");
            return true;
        }

//      first test if identifier
        if(!Pattern.matches(identifier,tokens[0])){
            System.out.println("syntax error, not an identifier");
            return false;
        }

//      no assignment for the variable
        if(tokens.length < 2){
            System.out.println("variable " + tokens[0] + " was never initialized");
            return true;
        }

//      then test if =
        if(!Pattern.matches(equals,tokens[1])){
            System.out.println("syntax error, not an =");
            return false;
        }

//      then test if exp, semicologn has no space so remove a char
//        System.out.println("exp is " + tokens[2]);
        if(!exp(tokens[2])){
            System.out.println("syntax error, not an exp");
            return false;
        }

//      if passed through, it is correct
        return true;
    }
//    individual term test
    public static boolean term(String token){

//      term can be Term * Fact  | Fact
//      every * must follow a fact, and be before a fact.

        //if * start, invalid, if * end, invalid

        if(token.charAt(0) == '*' || token.charAt(token.length()-1) == '*') return false;

//        split the token into two at that * and check if the splits are term and fact
        for(int i = 1;i < token.length() - 1;i++){
//            System.out.println("this empty?" + token.substring(0,i) + ":");
            if(token.charAt(i) == '*') return term(token.substring(0,i)) && fact(token.substring(i+1));
        }

//      if none of those, it has to be fact or invalid
//        System.out.println("passed this test");
//        System.out.println("fact is " + token);
        return fact(token);
    }
//    individual exp test
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
//                    System.out.println(token.substring(1,token.length()));
                    return fact(token.substring(1,token.length()));
                }
                //if not first, it has to be inbetween
//                System.out.println("this empty exp?" + token.substring(1,i) + ":");
                return exp(token.substring(1,i)) && term(token.substring(i+1,token.length()));
            }
        }

//      if not any of those it will get here, so now it has to be term or its false
//        System.out.println("term is " + token);
//        System.out.println("this empty term?" + token + ":");
        return term(token);
    }
//    individual fact test
    public static boolean fact(String token){
//        fact can be ( Exp ) | - Fact | + Fact | Literal | Identifier

//        //parenthesis matching using a int balance, if balanced(c=0) parenthesis were all closed, if not balanced(c != 0) error
        if(token.charAt(0) == '(') {
//            find the )
            int c = 1;
            for(int i = 1; i < token.length();i++ ){

                if(token.charAt(i) == '('){
                    c++;


                }
                if(token.charAt(i) == ')'){
                    c--;
                }


            }
            //if balanced
            if(c ==0){
//                System.out.println("found ( exp ) the exp is:  " + token.substring(i1+1,i2) + ":");
//                    return exp(token.substring(i1+1,i2));
                String[] splits = token.split("\\(|\\)");

                for(int j = 1; j < splits.length;j++){
//                    System.out.println("split"+ splits[j]);
                    if(splits[j].length() == 0) return true;
                    if(splits[j].charAt(0) == '*'){
                        return exp(splits[j-1]) && exp(splits[j+1]);
                    }

                }
                return true;
            }
            //not balanced (never closed)
            else{
                return false;
            }

        }

        //if start with + or -, must be another fact after that
        if((token.charAt(0) == '+')){
            return fact(token.substring(1));
        }
        else if((token.charAt(0) == '-')){
//            System.out.println("correct");
//            System.out.println(token.substring(1));
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