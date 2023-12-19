import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.System.*;

public class Day12{
    public static Map<String, Long> cache = new HashMap<>();
    public static void main(String[] args){
        out.println("The answer for part one is : " + solution1());
        out.println("The answer for part two is : " + solution2());
    }
    public static List<List<List<String>>> getInput(){
        List<List<List<String>>> input = new LinkedList<>();
        try{
            File file=new File("puzzles/puzzle12.txt");
            Scanner scanner=new Scanner(file);
            while(scanner.hasNextLine()){
                List<List<String>> temp = new LinkedList<>();
                String[] line= scanner.nextLine().split(" ");
                List<String> record =Arrays.asList(line[0].split(""));
                List<String> recordNumber =Arrays.asList(line[1].split(","));
                temp.add(record);
                temp.add(recordNumber);
                input.add(temp);
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return input;
    }
    public static int solution1(){
        List<List<List<String>>> input = getInput();
        int total = 0;
        for ( List<List<String>> line: input) total+=getArrangements(line.get(0), line.get(1));
        return total;
    }
    public static int getArrangements(List<String> line, List<String> numbers){
        // returns 1 if numbers = [] else returns 0
        if( line.isEmpty() ) return numbers.isEmpty() ? 1 : 0;

        // returns 0 is "#" is not in line else returns 1
        if( numbers.isEmpty() ) return line.contains("#") ? 0 : 1;

        int result = 0;
        // if first char is either a "." or "?" we can treat the "?" as a "."
        if( line.get(0).equals(".") || line.get(0).equals("?") ) result+=getArrangements(line.subList(1, line.size()), numbers);
        // if first char is either a "#" or "?" we can treat the "?" as a "#"
        if( line.get(0).equals("#") || line.get(0).equals("?") ){
            int x = Integer.parseInt(numbers.get(0));
            if( x <= line.size() && !line.subList(0,x).contains(".")  && (x == line.size() || !line.get(x).equals("#")) ){
                if( x+1 > line.size() ) result+= getArrangements(new LinkedList<>(), numbers.subList(1, numbers.size()));
                else result += getArrangements(line.subList(x+1, line.size()), numbers.subList(1, numbers.size()));
            }
        }
        return result;
    }
    public static long getArrangementsOptimised(List<String> line, List<String> numbers){
        // returns 1 if numbers = [] else returns 0
        if( line.isEmpty() ) return numbers.isEmpty() ? 1 : 0;

        // returns 0 is "#" is not in line else returns 1
        if( numbers.isEmpty() ) return line.contains("#") ? 0 : 1;

        String key = createString(line, numbers);
        if( cache.containsKey(key) ) return cache.get(key);

        long result = 0;
        // if first char is either a "." or "?" we can treat the "?" as a "."
        if( line.get(0).equals(".") || line.get(0).equals("?") ) result+=getArrangements(line.subList(1, line.size()), numbers);
        // if first char is either a "#" or "?" we can treat the "?" as a "#"
        if( line.get(0).equals("#") || line.get(0).equals("?") ){
            int x = Integer.parseInt(numbers.get(0));
            if( x <= line.size() && !line.subList(0,x).contains(".")  && (x == line.size() || !line.get(x).equals("#")) ){
                if( x+1 > line.size() ) result+=getArrangements(new LinkedList<>(), numbers.subList(1, numbers.size()));
                else result+=getArrangements(line.subList(x+1, line.size()), numbers.subList(1, numbers.size()));
            }
        }
        cache.put(key, result);
        return result;
    }

    public static List<String> unfoldList(List<String> line){
        List<String> unfolded = new LinkedList<>();
        // repeat 5 times
        for ( int i=0; i<5; i++ ) {
            unfolded.addAll(line);
            if( i != 4 )unfolded.add("?");
        }
        return unfolded;
    }

    public static List<String> unfoldNumbers(List<String> numbers){
        List<String> unfolded = new LinkedList<>();
        // repeat 5 times
        for ( int i=0; i<5; i++ ) unfolded.addAll(numbers);
        return unfolded;
    }
    public static long solution2(){
        List<List<List<String>>> input = getInput();
        long total = 0;
        for ( List<List<String>> line: input)
            total+=getArrangementsOptimised(unfoldList(line.get(0)), unfoldNumbers(line.get(1)));
        return total;
    }
    public static String createString(List<String> aa, List<String> bb){
        StringBuilder s =new StringBuilder();
        aa.forEach(s::append);
        bb.forEach(s::append);
        return s.toString();
    }
}
