import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

import static java.lang.System.*;

public class Day18{
    final static List<List<String>> instructions = new LinkedList<>();
    final static LinkedHashMap<String, List<Integer>> directions = new LinkedHashMap<>();

    public static void main(String[] args){
        init();
        out.println("The solution for part one is " + solution1());
        out.println("The solution for part two is " + solution2());
    }

    public static void init(){
        // init instructions
        try{
            File file=new File("puzzles/puzzle18.txt");
            Scanner scanner=new Scanner(file);
            while(scanner.hasNextLine()){
                instructions.add(Arrays.asList(scanner.nextLine().split(" ")));
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }

        // init directions
        directions.put("U", createCoordinate(0,1));
        directions.put("D", createCoordinate(0,-1));
        directions.put("L", createCoordinate(-1,0));
        directions.put("R", createCoordinate(1, 0));
    }
    public static int solution1(){
        List<List<Integer>> digPlan = new LinkedList<>();
        List<Integer> currentCoordinate = createCoordinate(0, 0);
        int boundaryPointsCount = 0;
        // follow the instructions and map out the dig plan
        for ( List<String> instruction: instructions) {
            String direction = instruction.get(0);
            int steps = Integer.parseInt(instruction.get(1));
            boundaryPointsCount += steps;
            for ( String d: directions.keySet()) {
                if( direction.equals(d) ) {
                    currentCoordinate = createCoordinate(directions.get(d).get(0)*steps+currentCoordinate.get(0), directions.get(d).get(1)*steps+currentCoordinate.get(1) );
                    digPlan.add(currentCoordinate);
                    break;
                }
            }
        }
        // calculating area of polygon = abs(sum(i->n) xi(yi+1 -  yi-1))/2 (shoelace formula)
        int polygonArea =Math.abs(IntStream.range(0, digPlan.size()).map(i->digPlan.get(i).get(0) * ((i-1!=-1 ? digPlan.get(i-1).get(1) : 0)-(i+1<digPlan.size()-1 ? digPlan.get(i+1).get(1) : 0))).sum())/2;
        // number of inner points == Areas of polygon - number of points on the boundary / 2 + 1 (pick's algorithm)
        int innerPointsCount = polygonArea - boundaryPointsCount/2 + 1;
        // total number of points in polygon = inner points count + boundary points count
        return innerPointsCount +  boundaryPointsCount;
    }
    public static long solution2(){
        List<List<Long>> digPlan = new LinkedList<>();
        List<Long> currentCoordinate = createCoordinateL(0, 0);
        long boundaryPointsCount = 0;
        // follow the instructions and map out the dig plan
        for ( List<String> instruction: instructions) {
            // (#70c710) -> steps = hex:70c71 to long
            long steps = Long.parseLong(instruction.get(2).substring(2, instruction.get(2).length()-2), 16);
            // (#70c710) -> direction = string enclosed by ||  -> removed (#70c71|0|)
            String direction = getDirection(instruction.get(2).substring(instruction.get(2).length()-2, instruction.get(2).length()-1));
            boundaryPointsCount += steps;
            for ( String d: directions.keySet()) {
                if( direction.equals(d) ) {
                    currentCoordinate = createCoordinateL(directions.get(d).get(0)*steps+currentCoordinate.get(0), directions.get(d).get(1)*steps+currentCoordinate.get(1) );
                    digPlan.add(currentCoordinate);
                    break;
                }
            }
        }
        // calculating area of polygon = abs(sum(i->n) xi(yi+1 -  yi-1))/2 (shoelace formula)
        long polygonArea =Math.abs(IntStream.range(0, digPlan.size()).mapToLong(i->digPlan.get(i).get(0) * ((i-1!=-1 ? digPlan.get(i-1).get(1) : 0)-(i+1<digPlan.size()-1 ? digPlan.get(i+1).get(1) : 0))).sum()) / 2;
        // number of inner points == Areas of polygon - number of points on the boundary / 2 + 1 (pick's algorithm)
        long innerPointsCount = polygonArea - boundaryPointsCount/2 + 1;
        // total number of points in polygon = inner points count + boundary points count
        return innerPointsCount +  boundaryPointsCount;
    }
    public static List<Integer> createCoordinate(int x, int y){
        return new ArrayList<>(){{ add(x);add(y);}};
    }
    public static List<Long> createCoordinateL(long x, long y){
        return new ArrayList<>(){{ add(x);add(y);}};
    }

    // 0 means R, 1 means D, 2 means L, and 3 means U.
    public static String getDirection(String d){
        switch (d) {
            case "0":
                return "R";
            case "1":
                return "D";
            case "2":
                return "L";
            case "3":
                return "U";
            default:
                throw new IllegalStateException("Unexpected value: "+d);
        }
    }
}
