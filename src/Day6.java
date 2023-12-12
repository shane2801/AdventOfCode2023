import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

import static java.lang.System.*;

public class Day6{
    private static final LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
    private static final LinkedHashMap<Long, Long> unKerned = new LinkedHashMap<>();

    public static void main(String[] args){
        getInput();
        out.println("The answer for part one is : " + solution1());
        out.println("The answer for part two is : " + solution2());
    }
    private static void getInput(){
        try{
            File file=new File("puzzles/puzzle6.txt");
            Scanner scanner=new Scanner(file);
            while(scanner.hasNextLine()){
                String firstLine =scanner.nextLine();
                String secondLine =scanner.nextLine();
                String[] t = firstLine.split(":")[1].trim().replaceAll("\\s+", ",").split(",");
                String ts = firstLine.split(":")[1].trim().replaceAll("\\s+", "");
                String[] d = secondLine.split(":")[1].trim().replaceAll("\\s+", ",").split(",");
                String ds = secondLine.split(":")[1].trim().replaceAll("\\s+", "");
                IntStream.range(0, t.length).forEach(i->map.put(Integer.valueOf(t[i]), Integer.valueOf(d[i])));
                unKerned.put(Long.valueOf(ts), Long.valueOf(ds));
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
    private static int solution1(){
        int totalWinningWays = 1;
        for ( int time: map.keySet()) {
            int record = map.get(time);
            // find min time
            int min = 0;
            for ( int i=1; i<time; i++ ) {
                int distance = (time-i) * i;
                if( distance > record ){
                    min = i;
                    break;
                }
            }
            // find max time
            int max = 0;
            for ( int i=time-1 ; i!=0; i-- ) {
                int distance = (time-i) * i;
                if( distance>record ){
                    max = i;
                    break;
                }
            }
            int numWinningWays = max - min + 1;
            totalWinningWays *= numWinningWays;
        }
        return totalWinningWays;
    }
    private static long solution2(){
        long totalWinningWays = 1;
        for ( long time: unKerned.keySet()) {
            long record = unKerned.get(time);
            // find min time
            long min = 0;
            for ( long i=1; i<time; i++ ) {
                long distance = (time-i) * i;
                if( distance > record ){
                    min = i;
                    break;
                }
            }
            // find max time
            long max = 0;
            for ( long i=time-1 ; i!=0; i-- ) {
                long distance = (time-i) * i;
                if( distance>record ){
                    max = i;
                    break;
                }
            }
            long numWinningWays = max - min + 1;
            totalWinningWays *= numWinningWays;
        }
        return totalWinningWays;
    }
}
