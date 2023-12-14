import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.*;

public class Day9{
    public static void main(String[] args){
        out.println("The answer for part one is :" + solution1());
        out.println("The answer for part two is :" + solution2());
    }

    public static List<List<Integer>> getInput(){
        List<List<Integer>> input = new LinkedList<>();
        try{
            File file=new File("puzzles/puzzle9.txt");
            Scanner scanner=new Scanner(file);
            while(scanner.hasNextLine()){
                String[] numbers = scanner.nextLine().split(" ");
                List<Integer> temp =Arrays.stream(numbers).map(Integer::parseInt).collect(Collectors.toCollection(LinkedList::new));
                input.add(temp);
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return input;
    }

    public static int solution1(){
        int sum = 0;
        List<List<Integer>> sequences = getInput();
        for ( List<Integer> sequence: sequences) {
            List<List <Integer>> generatedSequences = new LinkedList<>();
            // current sequence
            List<Integer> current = sequence;
            boolean isFinished = false;
            while(!isFinished){
                int zeroCount = 0;

                if( !generatedSequences.contains(current) ) generatedSequences.add(current);

                // compute new sequence
                List<Integer> newSequence = new LinkedList<>();
                for ( int i=0; i<current.size()-1; i++ ) {
                    int diff = current.get(i+1) - current.get(i);
                    if( diff == 0 ) zeroCount ++;
                    newSequence.add(diff);
                }
                if( !generatedSequences.contains(newSequence) ) generatedSequences.add(newSequence);

                // end of sequence =  all zeros
                if( zeroCount == newSequence.size() ) {
                    sum += generatedSequences.stream().mapToInt(generatedSequence->generatedSequence.get(generatedSequence.size()-1)).sum();
                    generatedSequences = new LinkedList<>();
                    isFinished = true;
                }
                else current=new LinkedList<>(newSequence);
            }
        }
        return sum;
    }

    public static int solution2(){
        int sum = 0;
        List<List<Integer>> sequences = getInput();
        for ( List<Integer> sequence: sequences) {
            List<List <Integer>> generatedSequences = new LinkedList<>();
            // current sequence
            List<Integer> current = sequence;
            boolean isFinished = false;
            while(!isFinished){
                int zeroCount = 0;

                if( !generatedSequences.contains(current) ) generatedSequences.add(current);

                // compute new sequence
                List<Integer> newSequence = new LinkedList<>();
                for ( int i=0; i<current.size()-1; i++ ) {
                    int diff = current.get(i+1) - current.get(i);
                    if( diff == 0 ) zeroCount ++;
                    newSequence.add(diff);
                }
                if( !generatedSequences.contains(newSequence) ) generatedSequences.add(newSequence);

                // end of sequence =  all zeros
                if( zeroCount == newSequence.size() ) {
                    int temp = 0;
                    for ( int i=generatedSequences.size()-2; i>=0; i-- ) {
                        temp = generatedSequences.get(i).get(0) - temp;
                    }
                    sum+=temp;
                    generatedSequences = new LinkedList<>();
                    isFinished = true;
                }
                else current=new LinkedList<>(newSequence);
            }
        }
        return sum;
    }
}
