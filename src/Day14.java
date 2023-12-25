import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.System.*;

public class Day14{

    static final long numCycles = 1000000000;
    public static void main(String[] args){
        out.println("The solution for part one is " + solution1());
        out.println("The solution for part two is " + solution2());
    }
    public static List<List<String>> getInput(){
        List<List<String>> grid = new LinkedList<>();
        try{
            File file=new File("puzzles/puzzle14.txt");
            Scanner scanner=new Scanner(file);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                grid.add(List.of(line.split("")));
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return grid;
    }
    public static int solution1(){
        LinkedHashMap<Integer, Integer> map = slideNorth(getInput());
        return map.keySet().stream().mapToInt(i->i).map(i->i * map.get(i)).sum();
    }
    public static int solution2(){
        List<List<String>> grid = getInput();
        Set<List<List<String>>> seen = new HashSet<>();
        List<List<List<String>>> history = new ArrayList<>();
        long iterationCount = 0;
        while(true){
            iterationCount +=1;
            grid = performCycle(grid);
            if( seen.contains(grid) ) break;
            seen.add(grid);
            history.add(grid);
        }
        long first = history.indexOf(grid)+1;
        long index = (numCycles-first)%(iterationCount-first)+first;
        grid = history.get((int) index-1);
        return calculateTotalLoad(grid);

    }
    public static List<Integer> createCoordinate(int x, int y){
        return new ArrayList<>(){{ add(x);add(y);}};
    }
    public static LinkedHashMap<Integer, Integer> slideNorth(List<List<String>> grid){
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        Set<List<Integer>> filled = new HashSet<>();
        // if we encounter a "0" store grid coordinates as filled ; technically "#" can also be marked as filled
        int gridSize =grid.size();
        for ( int i=0; i<grid.size(); i++ ) {
            List<String> line = grid.get(i);
            // if at first line then don't check above
            if( i == 0 ){
                for ( int j=0; j<line.size(); j++ ) {
                    switch (line.get(j)) {
                        case "O":
                            filled.add(createCoordinate(i, j));
                            if( !map.containsKey(gridSize-i) ) map.put(gridSize-i, 1);
                            else map.put(gridSize-i, map.get(gridSize-i)+1);
                        case "#":
                            filled.add(createCoordinate(i, j));
                    }
                }
            }
            // check if space is available above
            else{
                for ( int j=0; j<line.size(); j++ ) {
                    if( line.get(j).equals("O") ){
                        // find available coordinate
                        List<Integer> available=new ArrayList<>();
                        for ( int k=i-1; k>=0; k-- ) {
                            if( filled.contains(createCoordinate(k, j)) ){
                                if( !map.containsKey(gridSize-(k+1)) ) map.put(gridSize-(k+1), 1);
                                else map.put(gridSize-(k+1), map.get(gridSize-(k+1))+1);
                                filled.add(createCoordinate(k+1, j));
                                available = new ArrayList<>();
                                break;
                            }
                            // find min available position
                            else available=createCoordinate(k, j);
                        }
                        if( available.size()>0 ){
                            if( !map.containsKey(gridSize-available.get(0)) ) map.put(gridSize-available.get(0), 1);
                            else map.put(gridSize-available.get(0), map.get(gridSize-available.get(0))+1);
                            filled.add(available);
                        }
                    }
                    if( line.get(j).equals("#") ) filled.add(createCoordinate(i, j));
                }
            }
        }
        return map;
    }

    public static List<List<String>> slideNorthPart2(List<List<String>> grid){
        List<List<String>> newGrid = new ArrayList<>();
        Set<List<Integer>> filled = new HashSet<>();
        Set<List<Integer>> rocks = new HashSet<>();
        // if we encounter a "0" store grid coordinates as filled ; technically "#" can also be marked as filled
        int gridSize =grid.size();
        for ( int i=0; i<grid.size(); i++ ) {
            List<String> line = grid.get(i);
            // if at first line then don't check above
            if( i == 0 ){
                for ( int j=0; j<line.size(); j++ ) {
                    switch (line.get(j)) {
                        case "O":
                            filled.add(createCoordinate(i, j));
                        case "#":
                            rocks.add(createCoordinate(i, j));
                    }
                }
            }
            // check if space is available above
            else{
                for ( int j=0; j<line.size(); j++ ) {
                    if( line.get(j).equals("O") ){
                        // find available coordinate
                        List<Integer> available=new ArrayList<>();
                        for ( int k=i-1; k>=0; k-- ) {
                            if( filled.contains(createCoordinate(k, j)) || rocks.contains(createCoordinate(k, j)) ){
                                filled.add(createCoordinate(k+1, j));
                                available = new ArrayList<>();
                                break;
                            }
                            // find min available position
                            else available=createCoordinate(k, j);
                        }
                        if( available.size()>0 ) filled.add(available);
                    }
                    if( line.get(j).equals("#") ) rocks.add(createCoordinate(i, j));
                }
            }
        }
        // generate new grid
        for ( int i=0; i<gridSize; i++ ) {
            List<String> tempLine = new ArrayList<>();
            for ( int j=0; j<grid.get(i).size(); j++ ) {
                if( filled.contains(createCoordinate(i,j)) ) tempLine.add("O");
                else if( rocks.contains(createCoordinate(i,j)) ) tempLine.add("#");
                else tempLine.add(".");
            }
            newGrid.add(tempLine);
        }
        return newGrid;
    }

    public static List<List<String>> transpose (List<List<String>> grid) {
        List<List<String>> newGrid = new LinkedList<>();
        for ( int i=0; i<grid.get(0).size(); i++ ) {
            List<String> temp = new LinkedList<>();
            for ( List<String> g : grid ) temp.add(g.get(i));
            newGrid.add(temp);
        }
        return newGrid;
    }
    public static List<List<String>> reverseTranspose (List<List<String>> grid) {
        List<List<String>> newGrid = new LinkedList<>();
        for ( int i=grid.get(0).size()-1; i>=0; i-- ) {
            List<String> temp = new LinkedList<>();
            for ( List<String> strings : grid ) temp.add(strings.get(i));
            newGrid.add(temp);
        }
        return newGrid;
    }
    public static List<List<String>> flipVertically(List<List<String>> grid){
        List<List<String>> flipped = new ArrayList<>();
        for ( int i=grid.size()-1; i>=0; i-- ) {
            flipped.add(grid.get(i));
        }
        return flipped;
    }
    public static List<List<String>> revert(List<List<String>> grid){
        List<List<String>> newGrid = new ArrayList<>();
        for ( int i=0; i<grid.get(0).size(); i++ ) {
            List<String> temp = new ArrayList<>();
            for ( int j=grid.size()-1; j>=0; j-- ) {
                temp.add(grid.get(j).get(i));
            }
            newGrid.add(temp);
        }
        return newGrid;
    }
    public static void printGrid(List<List<String>> grid, String s){
        out.println("--------------------- "+ s +" --------------------");
        for ( List<String>g: grid) {
            out.println(g);
        }
        out.println("---------------- end --------------------");
    }
    public static List<List<String>> performCycle(List<List<String>> startGrid){
        // one cycle : north -> west -> south -> east
        // tilt north
        List<List<String>> tiltedNorthGrid = slideNorthPart2(startGrid);
//        printGrid(tiltedNorthGrid, "printing tilted north grid");
        // tilt west == transpose grid and tilt north and transpose it back
        List<List<String>> tiltedWestGrid  = transpose(slideNorthPart2(transpose(tiltedNorthGrid)));
//        printGrid(tiltedWestGrid, "printing tilted west grid");
        // tilt south == flip grid and tilt north and flip it back
        List<List<String>> tiltedSouthGrid = flipVertically(slideNorthPart2(flipVertically(tiltedWestGrid)));
//        printGrid(tiltedSouthGrid, "printing tilted south grid");
        // tilt east == transpose grid and tilt north and transpose it back
        //        printGrid(tiltedEastGrid, "printing tilted east grid");
        return revert(slideNorthPart2(reverseTranspose(tiltedSouthGrid)));
    }
    public static int calculateTotalLoad(List<List<String>> grid){
        int total = 0;
        for ( int i=0; i<grid.size(); i++ ) for ( int j=0; j<grid.get(0).size(); j++ ) if( grid.get(i).get(j).equals("O") ) total+=grid.size()-i;
        return total;
    }
}
