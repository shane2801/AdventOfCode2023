import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.*;

public class Day4{
    public static void main(String[] args){
        out.println("The answer for part 1 is :" + solution1());
        out.println("The answer for part 2 is :" + solution2());
    }

    public static HashMap<Integer, List<Set<Integer>>> getInput(){
        HashMap<Integer, List<Set<Integer>>> input = new HashMap<>();
        try{
            File file=new File("puzzles/puzzle4.txt");
            Scanner scanner=new Scanner(file);

            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                List<Set<Integer>> setHolder = new ArrayList<>();
                String[] temp = line.split(":");
                int cardNumber = Integer.parseInt(temp[0].replaceAll("\\s+", ",").split(",")[1]);
                String[] numbers = temp[1].trim().split("\\|");

                Set<Integer> winningNumbers =Arrays.stream(numbers[0].trim().strip().replaceAll("\\s+", ",").split(","))
                        .map(Integer::valueOf).collect(Collectors.toSet());
                Set<Integer> playedNumbers =Arrays.stream(numbers[1].trim().strip().replaceAll("\\s+", ",").split(","))
                        .map(Integer::valueOf).collect(Collectors.toSet());

                setHolder.add(winningNumbers);
                setHolder.add(playedNumbers);

                input.put(cardNumber, setHolder);
            }

            scanner.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
//        for (Map.Entry<Integer, List<Set<Integer>>> entry : input.entrySet()) {
//            System.out.println(entry.getKey() + ":" + entry.getValue());
//        }
        return input;
    }

    public static int solution1(){
        HashMap<Integer, List<Set<Integer>>> scratchCards = getInput();
        int totalPoints = 0;

        for (Map.Entry<Integer, List<Set<Integer>>> entry : scratchCards.entrySet()) {
            Set<Integer> winningNumbers=entry.getValue().get(0);
            Set<Integer> playedNumbers=entry.getValue().get(1);
            Set<Integer> intersection=new HashSet<>(winningNumbers);
            // find common numbers
            intersection.retainAll(playedNumbers);
//            out.println(intersection);
//            out.println("Number of common numbers is :"+intersection.size());
            // calculate points won
            int cardPoints=0;
            for ( int i=0; i<intersection.size(); i++ ) {
                if( i!=0 ) cardPoints=cardPoints * 2;
                else cardPoints+=1;
            }
//            out.println(cardPoints);
            totalPoints+=cardPoints;
        }
        return totalPoints;
    }

    public static int solution2(){
        HashMap<Integer, List<Set<Integer>>> scratchCards = getInput();
        HashMap<Integer, Integer> cardsWon = new HashMap<>();
        int totalCards;

        for (Map.Entry<Integer, List<Set<Integer>>> entry : scratchCards.entrySet()) {
            int cardNumber = entry.getKey();
            Set<Integer> winningNumbers=entry.getValue().get(0);
            Set<Integer> playedNumbers=entry.getValue().get(1);
            Set<Integer> intersection=new HashSet<>(winningNumbers);
            // find common numbers
            intersection.retainAll(playedNumbers);

            if( !cardsWon.containsKey(cardNumber) ) cardsWon.put(cardNumber, 1);
            else cardsWon.put(cardNumber, cardsWon.get(cardNumber)+1);

            List<Integer> listCardsWon =IntStream.range(0, intersection.size()).map(i->i+1).mapToObj(index->cardNumber+index).collect(Collectors.toList());

            //            out.println("List of cards won for " + cardNumber +" is: " +listCardsWon );

            if( listCardsWon.size()>0 ){
                IntStream.range(0, cardsWon.get(cardNumber)).<Consumer<? super Integer>>mapToObj(i->cardClone->{
                    if( cardsWon.containsKey(cardClone) ) cardsWon.put(cardClone, cardsWon.get(cardClone)+1);
                    else cardsWon.put(cardClone, 1);
                }).forEach(listCardsWon::forEach);
            }

        }
        totalCards=cardsWon.values().stream().mapToInt(i->i).sum();
        return totalCards;
    }
}
