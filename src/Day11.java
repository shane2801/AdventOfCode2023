import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import static java.lang.System.*;

public class Day11{
    public static void main(String[] args){
        out.println("The answer for part one is : " + solution1());
        out.println("The answer for part two is : " + solution2(1000000));
    }

    // read and parse our input
    public static List<List<String>> getInput(){
        List<List<String>> image = new LinkedList<>();
        try{
            File file=new File("puzzles/puzzle11.txt");
            Scanner scanner=new Scanner(file);
            while(scanner.hasNextLine()){
                String[] line= scanner.nextLine().split("");
                image.add(Arrays.asList(line));
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
//        for ( List<String> i: image ){
//            out.println(i);
//        }
        return image;
    }

    /* This function takes in our original universe from the getInput function
    *  and applies the cosmic expansion to our universe which results in:
    *  If an entire row contains only "." characters, add an additional row below it
    *  If an entire column contains only "." characters, add an additional column after it
    *  The new expanded universe is then returned
    * */
    public static List<List<String>> cosmicExpansion(List<List<String>> universe){
        // holds universe with both col and row expansion
        List<List<String>> newUniverse = new LinkedList<>();

        // holds universe with row expansion only
        List<List<String>> tempUniverse = new LinkedList<>();

        // expand the rows first and store resulting universe in temp universe
        for ( List<String> line : universe ) {
            int rowCount = 0;
            for(String s : line) if( s.equals(".") ) rowCount++;
            if( rowCount==line.size() ) tempUniverse.add(line);
            tempUniverse.add(line);
        }

        // find the indexes at which to expand the columns for the temp universe(which has already been "row expanded")
        List<Integer> colIndexesToInsert = new LinkedList<>();
        for ( int i=0; i< universe.size(); i++ ) {
            int colCount = 0;
            for ( List<String> strings : universe ) if( strings.get(i).equals(".") ) colCount++;
            if( colCount == universe.size() ) colIndexesToInsert.add(i);
        }

        // expand the columns and store the expanded universe in newUniverse
        for ( List<String> strings : tempUniverse ) {
            List<String> temp=new ArrayList<>();
            for ( int k=0; k<strings.size(); k++ ) {
                temp.add(strings.get(k));
                if( colIndexesToInsert.contains(k) ) temp.add(".");
            }
            newUniverse.add(temp);
        }
        // printing resulting universe after cosmic expansion
//        out.println("-------cosmic expansion--------------");
//        for ( List<String> newU: newUniverse) {
//            out.println(newU);
//        }
        return  newUniverse;
    }

    /*
    * This function will instead of creating a new expanded universe will return a map of galaxies assigned to
    * unique ids and their corresponding coordinates after the expansion.
    * The underlying idea is translating the x and y coordinates of the each galaxy by the expansion value
    * if they match those rules :
    *  1 : there is a column that needs to be expanded before y then the y coordinate of that value will shift by
    * the expansion value else if after do nothing
    *  2 : there is a row  that needs to be expanded before x then the x coordinate of that value will shift by
    * the expansion value else if after do nothing
    *
    * */
    public static LinkedHashMap<Integer, List<Long>> optimisedCosmicExpansion(List<List<String>> universe, long expansionValue){
        if( expansionValue == 0 ) expansionValue = 1;
        List<Integer> rowIndexesToInsert = new ArrayList<>();
        for ( int i=0; i< universe.size(); i++ ) {
            int rowCount = 0;
            for ( int j=0; j<universe.get(i).size(); j++ ) {
                if( universe.get(i).get(j).equals(".") ) rowCount++;
            }
            if( rowCount == universe.get(i).size() ) rowIndexesToInsert.add(i);
        }
//        out.println("The row index count is " + rowIndexesToInsert);
        List<Integer> colIndexesToInsert = new ArrayList<>();
        for ( int i=0; i< universe.size(); i++ ) {
            int colCount = 0;
            for ( List<String> strings : universe ) {
                if( strings.get(i).equals(".") ) colCount++;
            }
            if( colCount == universe.size() ) colIndexesToInsert.add(i);
        }
//        out.println("The col index count is " + colIndexesToInsert);

        LinkedHashMap<Integer, List<Long>> map = new LinkedHashMap<>();
        int id = 1;
        for ( int i=0; i<universe.size(); i++ ) {
            for ( int j=0; j<universe.get(i).size(); j++ ) {
                if( universe.get(i).get(j).equals("#") ){
                    // check if i and j > val in rowIndexes and colIndexes to start then i,j = i,j + expansionValue else i,j stays same
                    long newJ = j;
                    for ( int val: colIndexesToInsert) if( j>val ) newJ+=expansionValue;
                    long newI = i;
                    for ( int val: rowIndexesToInsert) if( i>val ) newI+=expansionValue;
                    map.put(id, createCoordinate(newI, newJ));
                    id++;
                }
            }
        }
//        for ( int i: map.keySet()) {
//            out.println("id " + i + " coordinate " + map.get(i) );
//        }
        return map;
    }

    public static int solution1(){
        List<List<String>> universe = cosmicExpansion(getInput());
        LinkedHashMap<Integer, List<Integer>> galaxiesCoordinates = new LinkedHashMap<>();
        Set<List<Integer>> seen = new HashSet<>();
        LinkedHashMap<List<Integer>, Integer> shortestPaths = new LinkedHashMap<>();
        // find galaxies and assign them with an id. Add their id and coordinates as a pair in galaxiesCoordinates
        int galaxyID = 1;
        for ( int i=0; i<universe.size(); i++ ) {
            for ( int j=0; j<universe.get(i).size(); j++ ) {
                if( universe.get(i).get(j).equals("#") ) {
                    galaxiesCoordinates.put(galaxyID, createCoordinate1(i, j));
                    galaxyID++;
                }
            }
        }

//        for ( int i: galaxiesCoordinates.keySet()) {
//            out.println(i+" -> "+galaxiesCoordinates.get(i));
//        }

        int sum = 0;
        for ( int id: galaxiesCoordinates.keySet() ) {
            List<Integer> coordinatesX = galaxiesCoordinates.get(id);
            for ( int i: galaxiesCoordinates.keySet()) {
                if( id == i ) continue;
                List<Integer> coordinatesY = galaxiesCoordinates.get(i);
                if( !seen.contains(createCoordinate1(id, i)) && !seen.contains(createCoordinate1(i, id)) ){
                    seen.add(createCoordinate1(id, i));
                    seen.add(createCoordinate1(i, id));
                    shortestPaths.put(createCoordinate1(id, i), calculateShortestPath1(coordinatesX, coordinatesY));
                    sum += calculateShortestPath1(coordinatesX, coordinatesY);
                }
            }
        }
//        out.println(shortestPaths);
//        for ( List<Integer> i: shortestPaths.keySet()) {
//            out.println(i + " -> "+shortestPaths.get(i));
//        }
//        out.println(shortestPaths.size());
        return sum;
    }

    public static List<Integer> createCoordinate1 (int x, int y){
        List<Integer> c = new ArrayList<>();
        c.add(x);
        c.add(y);
        return c;
    }

    public static List<Long> createCoordinate (long x, long y){
        List<Long> c = new ArrayList<>();
        c.add(x);
        c.add(y);
        return c;
    }

//     this function takes in two sets of coordinates and returns the shortest path between them
    public static int calculateShortestPath1(List<Integer> x, List<Integer> y){
        return Math.abs(x.get(0) - y.get(0)) + Math.abs(x.get(1) -y.get(1));
    }

    // this function takes in two sets of coordinates and returns the shortest path between them
    public static long calculateShortestPath(List<Long> x, List<Long> y){
        return Math.abs(x.get(0) - y.get(0)) + Math.abs(x.get(1) -y.get(1));
    }

    public static long solution2(int expansionValue){
        LinkedHashMap<Integer, List<Long>> expandedGalaxiesCoordinatesMap = optimisedCosmicExpansion(getInput(), expansionValue-1);
        Set<List<Long>> seen = new HashSet<>();
        LinkedHashMap<List<Long>, Long> shortestPaths = new LinkedHashMap<>();

        long sum = 0;
        for ( int id: expandedGalaxiesCoordinatesMap.keySet() ) {
            List<Long> coordinatesX = expandedGalaxiesCoordinatesMap.get(id);
            for ( int i: expandedGalaxiesCoordinatesMap.keySet()) {
                if( id == i ) continue;
                List<Long> coordinatesY = expandedGalaxiesCoordinatesMap.get(i);
                if( !seen.contains(createCoordinate(id, i)) || !seen.contains(createCoordinate(i, id)) ){
                    seen.add(createCoordinate(id, i));
                    seen.add(createCoordinate(i, id));
                    shortestPaths.put(createCoordinate(id, i), calculateShortestPath(coordinatesX, coordinatesY));
                    sum += calculateShortestPath(coordinatesX, coordinatesY);
                }
            }
        }
        return sum;
    }
}
