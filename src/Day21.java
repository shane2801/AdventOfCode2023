import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import static java.lang.System.out;

public class Day21{

    final static int[][] directions = new int[][] {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    final static List<List<String>> grid = new ArrayList<>();

    public static void main(String[] args){
        init();
        out.println("The solution for part one is " + solution1());
        out.println("The solution for part two is " + solution2());
    }

    // init grid
    public static void init(){
        try{
            File file = new File("puzzles/puzzle21.txt");
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) grid.add(Arrays.asList(scanner.nextLine().split("")));
            scanner.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public static long solution1(){
        List<Integer> start = getStartCoordinate();
        assert start!=null;
        return fill(start.get(0), start.get(1), 64);
    }

    public static long solution2(){
        int size =grid.size();
        // check if we are working with a square grid
        assert grid.size() == grid.get(0).size();

        long steps = 26501365;
        long grid_width = steps / size - 1;
        List<Integer> startPosition = getStartCoordinate();
        assert startPosition!=null;
        int sr = startPosition.get(0); // start row
        int sc = startPosition.get(1); // start col

        long odd = (long) Math.pow(((grid_width >> 1) * 2+1), 2);
        long even = (long) Math.pow((((grid_width+1) >> 1) * 2), 2);

        long oddPoints = fill(sr, sc, size*2+1);
        long evenPoints = fill(sr, sc, size*2);

        long corner_t = fill(size - 1, sc, size - 1);
        long corner_r = fill(sr, 0, size - 1);
        long corner_b = fill(0, sc, size - 1);
        long corner_l = fill(sr, size - 1, size - 1);

        long small_tr = fill(size - 1, 0, size / 2 - 1);
        long small_tl = fill(size - 1, size - 1, size / 2 - 1);
        long small_br = fill(0, 0, size / 2 - 1);
        long small_bl = fill(0, size - 1, size / 2 - 1);

        long large_tr = fill(size - 1, 0, size * 3 / 2 - 1);
        long large_tl = fill(size - 1, size - 1, size * 3 / 2 - 1);
        long large_br = fill(0, 0, size * 3 / 2 - 1);
        long large_bl = fill(0, size - 1, size * 3 / 2 - 1);

        return (odd * oddPoints) +
               (even * evenPoints)  +
               (corner_t + corner_r + corner_b + corner_l) +
               ((grid_width + 1) * (small_tr + small_tl + small_br + small_bl)) +
               (grid_width * (large_tr + large_tl + large_br + large_bl));
    }

    public static List<Integer> createCoordinate(int x, int y){
        return new ArrayList<>(){{ add(x);add(y);}};
    }

    public static List<Integer> getStartCoordinate(){
        for ( int i=0; i<grid.size(); i++ ) for ( int j=0; j<grid.get(0).size(); j++ ) if( grid.get(i).get(j).equals("S") ) return createCoordinate(i, j);
        return null;
    }

    public static long fill(int sr, int sc, long steps){
        Stack<List<Integer>> possible = new Stack<>();
        possible.add(createCoordinate(sr, sc));
        for ( long i=0; i<steps; i++ ) {
            Set<List<Integer>> seen = new HashSet<>();
            while(possible.size()>0){
                List<Integer> current = possible.pop();
                // check the neighbours of current -> R, U, L, D
                Arrays.stream(directions).forEach(direction->{
                    int newX=direction[0]+current.get(0);
                    int newY=direction[1]+current.get(1);
                    if( newX<0||newX>=grid.size()||newY<0||newY>=grid.get(0).size() ) return;
                    if( !grid.get(newX).get(newY).equals("#") )
                        seen.add(createCoordinate(newX, newY));
                });

            }
            possible.addAll(seen);
        }
        return possible.size();
    }
}
