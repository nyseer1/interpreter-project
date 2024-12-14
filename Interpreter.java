import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

public class Interpreter {

    public static void main(String[] args) throws FileNotFoundException {

        //all data is in the form of x, y, z

//        System.out.println("bruh");
        Scanner scnr = new Scanner(new File("input.txt"));

        while (scnr.hasNextLine()) {
            String line = scnr.nextLine();

            Map<String, Integer> results = interpret(line);

//          if no error occured (returns empty list if error)
            if(!results.isEmpty()){
                System.out.println("list not empty");
            }
        }


//        System.out.println(Character.isLetter('_'));





    }

    public static Map<String, Integer> interpret(String line){

        //using maps to map the identifier(key) to the expression(value)
        Map<String, Integer> results = new HashMap<>();
//      return empty map if any syntax error

        String[] tokens = line.split(" ");

//        System.out.println("tokens: "); for (String token : tokens) System.out.print(token + ", "); //test that token works

        boolean hasNextAssignment = true;

        while(hasNextAssignment) {
            hasNextAssignment = false;


//            test each assignment, assignment has to have Identifier = Exp;

//            test if identifier
           if(!identifier(tokens[0])) return new HashMap<>();

//          identifier found, now has to be =

            if(!equalSign(tokens[1])) return new HashMap<>();

//          equals found, now has to be exp
//          exp can be Exp + Term | Exp - Term | Term








        }

        System.out.println();




        return results;
    }

    public static boolean identifier(String token){
        //    identifier has to be Letter [Letter | Digit]*  ,atleast one letter, then any # of letters or digits after
//            test for first char = letter
        if(!Character.isLetter(token.charAt(0)) || token.charAt(0) == '_'){
            System.out.println("Syntax error: " + token + " does not start with a letter");
            return false;
        }
//             test if rest of chars are letter or digit
        for(int i = 1; i < token.length(); i++){
            if (!(Character.isLetter(token.charAt(i)) || Character.isDigit(token.charAt(i)) || token.charAt(i) == '_')){
                System.out.println("Syntax error: " + token + " has a non letter/digit");
                return false;
            }
        }
        return true;
    }

    public static boolean equalSign(String token){
        //    identifier has to be Letter [Letter | Digit]*  ,atleast one letter, then any # of letters or digits after
//            test for first char = letter
        if(token.charAt(0) != '=' || token.length() != 1){
            System.out.println("Syntax error: " + token + " expected: =");
            return false;
        }

        return true;
    }

}