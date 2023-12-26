import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.System.*;
public class Day16{
    public static String[][] contraption;

    public static void main(String[] args){
        initialiseContraption();
        out.println("The solution for part one is " + solution1());
        out.println("The solution for part two is " + solution2());
    }

    public static void initialiseContraption(){
        List<List<String>> strings = new LinkedList<>();
        try{
            File file=new File("puzzles/puzzle16.txt");
            Scanner scanner=new Scanner(file);
            while(scanner.hasNextLine()){
                String[] line = scanner.nextLine().split("");
                strings.add(Arrays.asList(line));
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        int row = strings.size();
        int col = strings.get(0).size();
        contraption = new String[row][col];
        for ( int i=0; i<strings.size(); i++ ) for ( int j=0; j<strings.get(i).size(); j++ ) contraption[i][j]=strings.get(i).get(j);
//        out.println("----------------------");
//        for ( String[] s: contraption) {
//            out.println(Arrays.asList(s));
//        }
    }

    public static int solution1(){
        return getEnergizedCount(createTriple("0", "0", "East"));
    }

    public static int solution2(){
        int min =Integer.MIN_VALUE;
        for ( List<String> startPosition: getStartPositions()) {
            int energizedCount = getEnergizedCount(startPosition);
            if( energizedCount > min ) min = energizedCount;
        }
        return min;
    }

    public static int getEnergizedCount(List<String> startPosition){
        Stack<List<String>> toVisit = new Stack<>();
        Set<List<String>> visited = new HashSet<>();
        Set<List<String>> energized = new HashSet<>();
        toVisit.add(startPosition);
        while(toVisit.size()>0){
            List<String> currentCoordinate = toVisit.pop();
            if( visited.contains(currentCoordinate) ) continue;
            else visited.add(currentCoordinate);
            String facing = currentCoordinate.get(2);
            // get current coordinate
            int x =Integer.parseInt(currentCoordinate.get(0));
            int y =Integer.parseInt(currentCoordinate.get(1));
            if( isCoordinateInBound(currentCoordinate) ){
                energized.add(createCoordinate(String.valueOf(x), String.valueOf(y)));
                // if char is a "." continue in same direction
                if( contraption[x][y].equals(".") ) {
                    // get next coordinate
                    int nextX = x;
                    int nextY = y;
                    switch (facing) {
                        case "North":
                            nextX-=1;
                            break;
                        case "South":
                            nextX+=1;
                            break;
                        case "West":
                            nextY-=1;
                            break;
                        case "East":
                            nextY+=1;
                            break;
                    }
                    // check if next coordinate is in bound and if so add it the the list to visit
                    if( isCoordinateInBound(createCoordinate(String.valueOf(nextX), String.valueOf(nextY))) )
                        toVisit.add(createTriple(String.valueOf(nextX), String.valueOf(nextY), facing));
                }
                // if char is a mirror handle reflection and change direction
                else if( contraption[x][y].equals("/") ){
                    switch (facing) {
                        case "North":
                            toVisit.add(createTriple(String.valueOf(x), String.valueOf(y+1), "East"));
                            break;
                        case "South":
                            toVisit.add(createTriple(String.valueOf(x), String.valueOf(y-1), "West"));
                            break;
                        case "West":
                            toVisit.add(createTriple(String.valueOf(x+1), String.valueOf(y), "South"));
                            break;
                        case "East":
                            toVisit.add(createTriple(String.valueOf(x-1), String.valueOf(y), "North"));
                            break;
                    }
                }
                else if( contraption[x][y].equals("\\") ){
                    switch (facing) {
                        case "North":
                            toVisit.add(createTriple(String.valueOf(x), String.valueOf(y-1), "West"));
                            break;
                        case "South":
                            toVisit.add(createTriple(String.valueOf(x), String.valueOf(y+1), "East"));
                            break;
                        case "West":
                            toVisit.add(createTriple(String.valueOf(x-1), String.valueOf(y), "North"));
                            break;
                        case "East":
                            toVisit.add(createTriple(String.valueOf(x+1), String.valueOf(y), "South"));
                            break;
                    }
                }
                // if char is a splitter handle splitter logic
                else if( contraption[x][y].contains("|") ){
                    switch (facing) {
                        case "North":
                            toVisit.add(createTriple(String.valueOf(x-1), String.valueOf(y), "North"));
                            break;
                        case "South":
                            toVisit.add(createTriple(String.valueOf(x+1), String.valueOf(y), "South"));
                            break;
                        case "West":
                        case "East":
                            toVisit.add(createTriple(String.valueOf(x-1), String.valueOf(y), "North"));
                            toVisit.add(createTriple(String.valueOf(x+1), String.valueOf(y), "South"));
                            break;
                    }
                }
                else if( contraption[x][y].contains("-") ){
                    switch (facing) {
                        case "West":
                            toVisit.add(createTriple(String.valueOf(x), String.valueOf(y-1), "West"));
                            break;
                        case "East":
                            toVisit.add(createTriple(String.valueOf(x), String.valueOf(y+1), "East"));
                            break;
                        case "North":
                        case "South":
                            toVisit.add(createTriple(String.valueOf(x), String.valueOf(y-1), "West"));
                            toVisit.add(createTriple(String.valueOf(x), String.valueOf(y+1), "East"));
                            break;
                    }
                }

            }
        }
        return energized.size();
    }

    public static List<String> createTriple(String x, String y, String f){
        return new ArrayList<>(){{ add(x);add(y);add(f);}};
    }

    public static List<String> createCoordinate(String x, String y){
        return new ArrayList<>(){{ add(x);add(y);}};
    }

    public static boolean isCoordinateInBound(List<String> coordinate){
        int x = Integer.parseInt(coordinate.get(0));
        int y = Integer.parseInt(coordinate.get(1));
        return x>=0&&x<contraption.length&&y<contraption[0].length&&y>=0;
    }

    public static List<List<String>> getStartPositions(){
        List<List<String>> startPositions = new ArrayList<>();
        for ( int i=0; i<contraption.length; i++ ) {
            if( i > 0 && i <contraption.length-1 ) {
                startPositions.add(createTriple(String.valueOf(i), "0", "East"));
                startPositions.add(createTriple(String.valueOf(i), String.valueOf(contraption.length-1), "West"));
                continue;
            }
            if( i==0 ){
                for ( int j=0; j<contraption[i].length; j++ )
                    startPositions.add(createTriple(String.valueOf(i), String.valueOf(j), "South"));
            }
            else if( i==contraption.length-1 ){
                for ( int j=0; j<contraption[i].length; j++ )
                    startPositions.add(createTriple(String.valueOf(i), String.valueOf(j), "North"));
            }

        }
        return startPositions;
    }

}
