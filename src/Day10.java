import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

import static java.lang.System.*;

public class Day10{
    public static void main(String[] args){
        out.println("The answer for part one is : " + solution1());
        out.println("The answer for part two is : " + solution2());
    }
    public static String[][] getInput(){
//        String[][] grid = new String[5][5];
//        String[][] grid = new String[9][11];
        String[][] grid = new String[140][140];
        try{
            File file=new File("puzzles/puzzle10.txt");
            Scanner scanner=new Scanner(file);
            var i = 0;
            while(scanner.hasNextLine()){
                grid[i] = scanner.nextLine().split("");
                i++;
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
//        for ( String[] a: grid) {
//            out.println(Arrays.toString(a));
//        }
        return grid;
    }

    public static int solution1(){
        String[][] grid = getInput();
        Set<List<Integer>> visited = new HashSet<>();
        Deque<List<Integer>> queue = new ArrayDeque<>();
        // find s and add its value to grid and visited
        for ( int i=0; i<grid.length; i++ ) {
            boolean isFinished = false;
            for ( int j=0; j<grid.length; j++ ) {
                if( grid[i][j].equals("S") ){
                    List<Integer> sCoordinates= createCoordinate(i, j);
                    visited.add(sCoordinates);
                    queue.add(sCoordinates);
                    isFinished  = true;
                    break;
                }
            }
            if( isFinished ) break;
        }
        while(!queue.isEmpty()){
            List<Integer> current = queue.removeFirst();
            int row = current.get(0);
            int col = current.get(1);
            String c = grid[row][col];

            // going north
            if( row > 0 && "S|JL".contains(c) && "|7F".contains(grid[row-1][col]) && !visited.contains(createCoordinate(row-1, col))  ){
                visited.add(createCoordinate(row-1, col));
                queue.addLast(createCoordinate(row-1, col));
            }
            // down
            // if not in last row and our current pos is able to move down and the position below us is able to accept downward and not encountered before
            if( row < grid[0].length-1 && "S|7F".contains(c) && "|JL".contains(grid[row+1][col]) && !visited.contains(createCoordinate(row+1, col))  ){
                visited.add(createCoordinate(row+1, col));
                queue.addLast(createCoordinate(row+1, col));
            }
            // left
            // if col > 0 and we are able to move left and the position to our left is able to be moved into from the right and we have not visited
            if( col > 0 && "S-J7".contains(c) && "-LF".contains(grid[row][col-1]) && !visited.contains(createCoordinate(row, col-1)  )){
                visited.add(createCoordinate(row, col-1));
                queue.addLast(createCoordinate(row, col-1));
            }
            // right
            if( col <grid[0].length-1 && "S-LF".contains(c) && "-J7".contains(grid[row][col+1]) && !visited.contains(createCoordinate(row, col+1) )){
                visited.add(createCoordinate(row, col+1));
                queue.addLast(createCoordinate(row, col+1));
            }

        }
        return visited.size()/2;
    }

    // returns a list of the coordinates of x and y -> [x, y]
    public static List<Integer> createCoordinate(int x, int y){
        List<Integer> a = new ArrayList<>();
        a.add(x);
        a.add(y);
        return a;
    }

    /*
    * something we can do so see is a tile is inside or outside of the loop, we can see how many times it crosses the path of the loop
    * even number of crosses = outside
    * odd number of crosses = inside
    * traversing horizontally if two pipes are pointing in the same direction we can ride along the pipe else if two pipes are pointing in opposite directions then we are crossing it
    * if traversing horizontally does not give the answer we have to traverse vertically as well (trying to reduce the time complexity of the algo by splitting it)
    * (the proposed idea is similar to the ray tracing algorithm on polygons)
    * */


    public static int countInversions(int i, int j,Set<List<Integer>> visited, String[][] grid){
        int count = 0;
        String[] line = grid[i];
        for ( int k=0; k<j; k++ ) {
            if( !visited.contains(createCoordinate(i, k)) ) continue;
//            if( line[k].equals("J") || line[k].equals("L") || line[k].equals("|") ) count ++;
            if( line[k].equals("F") || line[k].equals("7") || line[k].equals("|") ) count ++;
        }
        return count;
    }

    

    public static int solution2(){
        String[][] grid = getInput();
        Set<List<Integer>> visited = new HashSet<>();
        Deque<List<Integer>> queue = new ArrayDeque<>();
        // in part two we have to find the Starting position S
        char[] possibleSValues = {'|', '-', 'J', 'L', '7', 'F'};
        char S= 0;

        // find s and add its coordinates to grid and visited
        for ( int i=0; i<grid.length; i++ ) {
            boolean isFinished = false;
            for ( int j=0; j<grid[0].length; j++ ) {
                if( grid[i][j].equals("S") ){
                    List<Integer> sCoordinates= createCoordinate(i, j);
                    visited.add(sCoordinates);
                    queue.add(sCoordinates);
                    isFinished  = true;
                    break;
                }
            }
            if( isFinished ) break;
        }

        while(!queue.isEmpty()){
            List<Integer> current = queue.removeFirst();
            int row = current.get(0);
            int col = current.get(1);
            String c = grid[row][col];

            // going north
            if( row > 0 && "S|JL".contains(c) && "|7F".contains(grid[row-1][col]) && !visited.contains(createCoordinate(row-1, col))  ){
                visited.add(createCoordinate(row-1, col));
                queue.addLast(createCoordinate(row-1, col));
                if( c.equals("S") ){
                    for ( char ch: possibleSValues) {
                        String chs = "|JL";
                        if( IntStream.range(0, chs.length()).anyMatch(i->ch==chs.charAt(i)) ){
                            S=ch;
                        }
                    }
                }
            }
            // down
            // if not in last row and our current pos is able to move down and the position below us is able to accept downward and not encountered before
            if( row < grid[0].length-1 && "S|7F".contains(c) && "|JL".contains(grid[row+1][col]) && !visited.contains(createCoordinate(row+1, col))  ){
                visited.add(createCoordinate(row+1, col));
                queue.addLast(createCoordinate(row+1, col));
                if( c.equals("S") ){
                    for ( char ch: possibleSValues) {
                        String chs = "|7F";
                        if( IntStream.range(0, chs.length()).anyMatch(i->ch==chs.charAt(i)) ) S=ch;
                    }
                }
            }
            // left
            // if col > 0 and we are able to move left and the position to our left is able to be moved into from the right and we have not visited
            if( col > 0 && "S-J7".contains(c) && "-LF".contains(grid[row][col-1]) && !visited.contains(createCoordinate(row, col-1)  )){
                visited.add(createCoordinate(row, col-1));
                queue.addLast(createCoordinate(row, col-1));
                if( c.equals("S") ){
                    for ( char ch: possibleSValues) {
                        String chs = "-J7";
                        if( IntStream.range(0, chs.length()).anyMatch(i->ch==chs.charAt(i)) ) S=ch;
                    }
                }
            }
            // right
            if( col <grid[0].length-1 && "S-LF".contains(c) && "-J7".contains(grid[row][col+1]) && !visited.contains(createCoordinate(row, col+1) )){
                visited.add(createCoordinate(row, col+1));
                queue.addLast(createCoordinate(row, col+1));
                if( c.equals("S") ){
                    for ( char ch: possibleSValues) {
                        String chs = "-LF";
                        if( IntStream.range(0, chs.length()).anyMatch(i->ch==chs.charAt(i)) ) S=ch;
                    }
                }
            }

        }
        out.println("S is " + S);

        // replace S value in out grid with its real value
        // find s and add its coordinates to grid and visited
        for ( int i=0; i<grid.length; i++ ) {
            boolean isFinished = false;
            for ( int j=0; j<grid.length; j++ ) {
                if( grid[i][j].equals("S") ){
                    grid[i][j] =String.valueOf(S);
                    isFinished  = true;
                    break;
                }
            }
            if( isFinished ) break;
        }
//      replace every tile that is not in the loop with "." .
        for ( int i=0; i<grid.length; i++ ) {
            for ( int j=0; j<grid[0].length; j++ ) {
                if( !visited.contains(createCoordinate(i,j)) ) grid[i][j] = ".";
            }
        }

        for ( String[] strings : grid ) {
            out.println(Arrays.toString(strings));
        }
        out.println("-------");

        // horizontal scan
        int ans = 0;
        for ( int i=0; i<grid.length; i++ ) {
            for ( int j=0; j<grid[0].length; j++ ) {
                if( !visited.contains(createCoordinate(i, j)) ){
                    int inv = countInversions(i, j, visited, grid);
                    if( inv % 2 == 1 ) ans +=1;
                }
            }
        }

        // replace every tile that is not in the loop with "." .
        for ( int i=0; i<grid.length; i++ ) {
            for ( int j=0; j<grid[0].length; j++ ) {
                if( visited.contains(createCoordinate(i, j)) ) grid[i][j] = "X";
                else grid[i][j] = ".";
            }
        }
        for ( String[] strings : grid ) out.println(Arrays.toString(strings));

        return ans;
    }
}
