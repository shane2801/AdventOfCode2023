import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import static java.lang.System.*;

public class Day5{
    static List<Long> initialSeeds = new ArrayList<>();
    static LinkedHashMap<String, List<List<Long>>> almanac = new LinkedHashMap<>();
    public static void main(String[] args){
        getInput();
        out.println("The answer for part one is : " + solution1());
        out.println("The answer for part two is : " + solution2());
    }

    public static void getInput(){
        String category = "";
        try{
            File file=new File("puzzles/puzzle5.txt");
            Scanner scanner=new Scanner(file);

            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                if( line.equals("") ){
                    category = "";
                    continue;
                }
                if( line.startsWith("seeds") ){
                    String[] temp = line.split(" ");
                    for ( int i=1; i<temp.length; i++ ) {
                        initialSeeds.add(Long.parseLong(temp[i]));
                    }
                    continue;
                }
                if( line.contains("map") ){
                    category = line;
                    if( !almanac.containsKey(line) ) {
                        List<List<Long>> temp = new ArrayList<>();
                        almanac.put(category, temp);
                    }
                }
                else{
                    long source = Long.parseLong(line.split(" ")[1]);
                    long destination = Long.parseLong(line.split(" ")[0]);
                    long rangeLength = Long.parseLong(line.split(" ")[2]);
                    List<Long> ranges = new ArrayList<>();
                    ranges.add(destination);
                    ranges.add(source);
                    ranges.add(rangeLength);
                    almanac.get(category).add(ranges);
                }
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
//        out.println(almanac);
    }

    public static long solution1(){
        List<Long> matchesFound = new ArrayList<>();
        for ( Long initialSeed: initialSeeds) {
            long match = initialSeed;
            for ( String entry: almanac.keySet()) {
                for ( List<Long> range: almanac.get(entry)) {
                    // check if seed is in range
                    long start = range.get(1);
                    long end = range.get(1)+range.get(2)-1;
                    if( match >= start && match <= end ){
                        long diff = match - range.get(1);
                        match = range.get(0) + diff;
                        break;
                    }
                }
            }
            matchesFound.add(match);
        }
        return Collections.min(matchesFound);
    }
    public static LinkedList<Long> computeRange (long start, long end){
        LinkedList<Long> range = new LinkedList<>();
        range.add(start);
        range.add(end);
        return range;
    }
    public static long solution2(){
        LinkedList<LinkedList<Long>> seedsRange =IntStream.iterate(0, i->i<initialSeeds.size()-1, i->i+2).mapToObj(i->computeRange(initialSeeds.get(i), initialSeeds.get(i)+initialSeeds.get(i+1))).collect(Collectors.toCollection(LinkedList::new));

        for ( String block: almanac.keySet()) {
            LinkedList<LinkedList<Long>> mappedRanges = new LinkedList<>();
            while(seedsRange.size()>0){
                LinkedList<Long> current = seedsRange.removeFirst();
                long s = current.get(0);
                long e = current.get(1);
                boolean finishedForLoop = false;
                for ( List<Long> range: almanac.get(block)) {
                    long destination = range.get(0);
                    long source = range.get(1);
                    long increment = range.get(2);
                    long intersectingStart = Math.max(s,source);
                    long intersectingEnd = Math.min(e, source+increment);

                    if( intersectingStart < intersectingEnd ){
                        mappedRanges.add(computeRange(intersectingStart-source+destination, intersectingEnd-source+destination));
                        if( intersectingStart > s ) seedsRange.add(computeRange(s, intersectingStart));
                        if( e > intersectingEnd ) seedsRange.add(computeRange(intersectingEnd, e));
                        finishedForLoop = true;
                        break;
                    }
                }
                if( !finishedForLoop )  mappedRanges.add(computeRange(s, e));
            }
            seedsRange.addAll(mappedRanges);
        }
        // return min of left sided values in seed ranges
        return seedsRange.stream().map(range->range.get(0)).mapToLong(range->range).min().orElse(Long.MAX_VALUE);
    }

}

