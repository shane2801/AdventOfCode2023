import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

import static java.lang.System.*;

public class Day13{
    public static void main(String[] args){
        out.println("The solution for part one is " + solution1());
        out.println("The solution for part two is " + solution2());
    }
    public static List<List<List<String>>> getInput(){
        List<List<List<String>>> blocks = new LinkedList<>();
        try{
            File file=new File("puzzles/puzzle13.txt");
            Scanner scanner=new Scanner(file);
            List<List<String>> block = new LinkedList<>();
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                if( line.isEmpty() ) {
                    blocks.add(block);
                    block = new LinkedList<>();
                    continue;
                }
                block.add(Arrays.asList(line.split("")));
            }
            blocks.add(block);
            scanner.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
//        for ( List<List<String>> bb: blocks ) {
//            for ( List<String> b: bb) {
//                out.println(b);
//            }
//            out.println("---------------");
//        }
        return blocks;
    }
    public static int solution1(){
        List<List<List<String>>> patterns = getInput();
        int total = 0;
        for ( List<List<String>> pattern: patterns) {
            // find index of line of reflection - horizontally
            int reflectionIndex = getReflectionIndex(pattern);
            total += reflectionIndex *100;
            // find index of line of reflection - vertically(just call getReflectionIndex on transposed pattern)
            reflectionIndex = getReflectionIndex(transpose(pattern));
            total += reflectionIndex;
        }

        return total;
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

    public static int solution2(){
        List<List<List<String>>> patterns = getInput();
        int total = 0;
        for ( List<List<String>> pattern: patterns) {
            // find index of line of reflection - horizontally
            int reflectionIndex = getReflectionIndexPart2(pattern);
            total += reflectionIndex *100;
            // find index of line of reflection - vertically(just call getReflectionIndex on transposed pattern)
            reflectionIndex = getReflectionIndexPart2(transpose(pattern));
            total += reflectionIndex;
        }
        return total;
    }
    public static int getReflectionIndex(List<List<String>> pattern){
        for ( int i=0, patternSize=pattern.size(); i<patternSize-1; i++ ) {
            // make sure we are not out of bounds
            boolean match = true;
            if( pattern.get(i).equals(pattern.get(i+1))){
                // we might have found the reflection and need to check that i-1 until i ==0 and i+1+1 until i == pattern.size-1 match
                int b = i-1;
                int a = i+2;
                while( b >= 0 && a < patternSize ){
                    if( !pattern.get(b).equals(pattern.get(a)) ){
                        match=false;
                        break;
                    }
                    b--;
                    a++;
                }
                if( match ) return i+1;
            }
        }
        return 0;
    }
    public static int getReflectionIndexPart2(List<List<String>> pattern){
        for ( int i=0, patternSize=pattern.size(); i<patternSize; i++ ) {
            List<List<String>> above = new LinkedList<>(pattern.subList(0, i+1));
            Collections.reverse(above);
            List<List<String>> below = new LinkedList<>(pattern.subList(i+1,patternSize));
            var reflectionDiffSum = 0;
            var minSize=Math.min(above.size(), below.size());
            for ( int j=0; j<minSize; j++ ) {
                var x = above.get(j);
                var y = below.get(j);
                var diffSum =(int) IntStream.range(0, x.size()).filter(k->!x.get(k).equals(y.get(k))).count();
                reflectionDiffSum += diffSum;
            }
            if( reflectionDiffSum == 1 ) return i+1;
        }
        return 0;
    }
}
