import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Day1{
    public static void main(String[]args){
        solution1();
        solution2();
    }
    public static List<String> getInput(){
        List<String> input = new ArrayList<>();
        try{
            File file = new File("puzzles/puzzle1.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                input.add(line);
            }
            scanner.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
//        for ( String i: input) {
//            System.out.println(i);
//        }
        return input;
    }
    public static void solution1(){
        List<String> input = getInput();
        List<LinkedList<String>> values = new ArrayList<>();
        for ( String line: input) {
            LinkedList<String> temp = new LinkedList<>();
            for ( int i=0; i<line.length(); i++ ) {
                char currentChar = line.charAt(i);
                if (Character.isDigit(currentChar)) temp.add(String.valueOf(currentChar));
            }
            values.add(temp);
        }
//        List<Integer> calibrationValues = new ArrayList<>();
        int sum = 0;
        for ( LinkedList<String> innerList: values) {
            String temp=innerList.getFirst()+innerList.getLast();
            sum += Integer.parseInt(temp);
//            calibrationValues.add(Integer.parseInt(temp));
//            System.out.println(innerList);
        }
//        System.out.println(calibrationValues);
        System.out.println("Total for solution 1 is = " + sum);
    }
    public static void solution2(){
        List<String> input = getInput();
        int total =0;
        List<LinkedList<String>> values = new ArrayList<>();
        for ( String line: input) {
            LinkedList<String> temp = new LinkedList<>();
//            System.out.println("The current line is: "+ line);
            for ( int i=0; i<line.length(); i++ ) {
                char currentChar=line.charAt(i);

                // if is digit
                if( Character.isDigit(currentChar) ) temp.add(String.valueOf(currentChar));
                    // one
                else if( currentChar=='o'&&i+2<line.length()&&line.charAt(i+1)=='n'&&line.charAt(i+2)=='e' )
                    temp.add("1");
                    // two
                else if( (currentChar=='t')&&((i+2)<line.length())&&line.charAt(i+1)=='w'&&line.charAt(i+2)=='o' )
                    temp.add("2");
                    // three
                else if( currentChar=='t'&&i+4<line.length()&&line.charAt(i+1)=='h'&&line.charAt(i+2)=='r'&&line.charAt(i+3)=='e'&&line.charAt(i+4)=='e' )
                    temp.add("3");
                    // four
                else if( currentChar=='f'&&i+3<line.length() ){
                    if( line.charAt(i+1)=='o'&&line.charAt(i+2)=='u'&&line.charAt(i+3)=='r' ) temp.add("4");
                        // five
                    else if( line.charAt(i+1)=='i'&&line.charAt(i+2)=='v'&&line.charAt(i+3)=='e' ) temp.add("5");
                }
                // six
                else if( currentChar=='s'&&i+2<line.length()&&line.charAt(i+1)=='i'&&line.charAt(i+2)=='x' )
                    temp.add("6");
                    // seven
                else if( currentChar=='s'&&i+4<line.length()&&line.charAt(i+1)=='e'&&line.charAt(i+2)=='v'&&line.charAt(i+3)=='e'&&line.charAt(i+4)=='n' )
                    temp.add("7");
                    // eight
                else if( currentChar=='e'&&i+4<line.length()&&line.charAt(i+1)=='i'&&line.charAt(i+2)=='g'&&line.charAt(i+3)=='h'&&line.charAt(i+4)=='t' )
                    temp.add("8");
                    // nine
                else if( currentChar=='n'&&i+3<line.length()&&line.charAt(i+1)=='i'&&line.charAt(i+2)=='n'&&line.charAt(i+3)=='e' )
                    temp.add("9");

            }
//            System.out.println(temp);
//            System.out.println(Integer.parseInt(temp.getFirst() + temp.getLast()));
            total += Integer.parseInt(temp.getFirst() + temp.getLast());
//            System.out.println("Total for solution 2 is = " + total);
            values.add(temp);
        }
        System.out.println("Total for solution 2 is = " + total);
    }
}
