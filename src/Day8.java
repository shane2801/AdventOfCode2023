import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.*;

public class Day8{
    private static final LinkedList<Character> instructions = new LinkedList<>();
    private static final LinkedHashMap<String,List<String>> map = new LinkedHashMap<>();


    public static void main(String[] args){
        // parse input into map & instructions
        getInput();
        out.println("The solution for part one is : " + solution1());
        out.println("The solution for part two is : " + solution2());

    }
    // parse our input
    public static void getInput(){
        try{
            File file=new File("puzzles/puzzle8.txt");
            Scanner scanner=new Scanner(file);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                if( line.equals("") ) continue;
                if( !line.contains("=") ) {
                    for ( char c: line.toCharArray()) {
                        instructions.add(c);
                    }
                }
                else {
//                    out.println(line);
                    String[] temp = line.split("=");
                    String start = temp[0].trim();
                    String destination = temp[1].trim();
//                    out.println(start);
//                    out.println(destination);
                    String left = destination.substring(1, 4);
                    String right = destination.substring(destination.length()-4, destination.length()-1);
//                    out.println(left);
//                    out.println(right);
                    List<String> tempMap = new ArrayList<>();
                    tempMap.add(left);
                    tempMap.add(right);
                    map.put(start, tempMap);
                }
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
//        out.println(instructions);
//        for ( String s: map.keySet()) {
//            out.println(s +  " " + map.get(s));
//        }
    }

    public static int solution1 (){
        LinkedList<Character> instructionsCopy=new LinkedList<>(instructions);
        String start = "AAA";
        String end = "ZZZ";
        String destination="";
        int steps = 0;
        while(true){
            char direction;
            if( instructionsCopy.size() == 0 ) instructionsCopy=new LinkedList<>(instructions);
            direction = instructionsCopy.removeFirst();
            for ( String s : map.keySet()) {
                if( s.equals(start) ){
                    // go left or right
                    if( direction == 'L' ) destination=map.get(s).get(0);
                    else if( direction == 'R' ) destination=map.get(s).get(1);
                    steps ++;
                    if( destination.equals(end) ) return steps;
                    else start = destination;
                    break;
                }
            }
        }
    }

    public static long solution2Unoptimised(){
        LinkedList<Character> instructionsCopy=new LinkedList<>(instructions);
        List<String> start = new LinkedList<>();
        // find start positions
        for ( String s: map.keySet()) if( s.endsWith("A") ) start.add(s);
        long steps = 0;
        while(true){
            List<String> destination = new LinkedList<>();
            // get direction and if we run out of directions, start over with initial set of directions
            char direction;
            if( instructionsCopy.size() == 0 ) instructionsCopy=new LinkedList<>(instructions);
            direction = instructionsCopy.removeFirst();

            // for every start node
            for ( String s: start) {
                // get left and right of start node
                // go left or right
                if( direction == 'L' ) destination.add(map.get(s).get(0));
                else if( direction == 'R' ) destination.add(map.get(s).get(1));
                steps ++;
            }
            long count = destination.stream().filter(e->e.endsWith("Z")).count();
            if( count == destination.size() ) return steps/start.size();
            else start = new LinkedList<>(destination);
        }
    }

    public static long solution2(){
        LinkedList<Character> instructionsCopy=new LinkedList<>(instructions);
        // find start positions
        List<String> starts =map.keySet().stream().filter(s->s.endsWith("A")).collect(Collectors.toCollection(LinkedList::new));
        List<Long> totalSteps = new LinkedList<>();

        for ( String start : starts) {
            String destination="";
            long steps = 0;
            boolean reachedEnd = false;
            while(!reachedEnd){
                char direction;
                if( instructionsCopy.size() == 0 ) instructionsCopy=new LinkedList<>(instructions);
                direction = instructionsCopy.removeFirst();
                for ( String s : map.keySet()) {
                    if( s.equals(start) ){
                        // go left or right
                        if( direction == 'L' ) destination=map.get(s).get(0);
                        else if( direction == 'R' ) destination=map.get(s).get(1);
                        steps ++;
                        if( destination.endsWith("Z") ) {
                            reachedEnd = true;
                            totalSteps.add(steps);
                            break;
                        }
                        else start = destination;
                        break;
                    }
                }
            }
        }
        return lcm(totalSteps);

    }

    private static long gcd(long a, long b){
        while (b > 0){
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    private static long lcm(long a, long b){
        return a * (b / gcd(a, b));
    }

    private static long lcm(List<Long> input){
        long result = input.get(0);
        for(int i = 1; i < input.size(); i++) result = lcm(result, input.get(i));
        return result;
    }
}
