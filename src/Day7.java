import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import static java.lang.System.*;

public class Day7{
    private static final LinkedHashMap<String, Integer> camelHands = new LinkedHashMap<>();
    private static final LinkedHashMap<Character, Integer> rankValues = new LinkedHashMap<>();
    private static final LinkedHashMap<Character, Integer> rankValuesPart2 = new LinkedHashMap<>();

    public static void main(String[] args){
        // initialise our input and rank values
        getInput();
        createRankValues();
        createRankValuesPart2();

        out.println("The answer for part one is : " + solution1());
        out.println("The answer for part two is : " + solution2());
    }

    // parse our input
    public static void getInput(){
        try{
            File file=new File("puzzles/puzzle7.txt");
            Scanner scanner=new Scanner(file);
            while(scanner.hasNextLine()){
                String[] line =scanner.nextLine().split(" ");
                int bidAmount  = Integer.parseInt(line[1]);
                camelHands.put(line[0], bidAmount);
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }

    }

    // initialises rankValues which represent what rank value is associated to each character
    // e.g A has value 14 and therefore is higher than a 2 which has value 2
    public static void createRankValues(){
        for ( int i=2; i<15; i++ ) {
            switch (i) {
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    rankValues.put(Character.forDigit(i,10),i);
                    break;
                case 10:
                    rankValues.put('T', i);
                    break;
                case 11:
                    rankValues.put('J', i);
                    break;
                case 12:
                    rankValues.put('Q', i);
                    break;
                case 13:
                    rankValues.put('K', i);
                    break;
                case 14:
                    rankValues.put('A', i);
                    break;
            }
        }
    }
    public static void createRankValuesPart2(){
        for ( int i=2; i<15; i++ ) {
            switch (i) {
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    rankValuesPart2.put(Character.forDigit(i,10),i);
                    break;
                case 10:
                    rankValuesPart2.put('T', i);
                    break;
                case 11:
                    rankValuesPart2.put('J', 1);
                    break;
                case 12:
                    rankValuesPart2.put('Q', i);
                    break;
                case 13:
                    rankValuesPart2.put('K', i);
                    break;
                case 14:
                    rankValuesPart2.put('A', i);
                    break;
            }
        }
    }

    /* Returns a Map of hand and its associated type in the form of a number between 1 and 7 inclusive
     * 1 - high card
     * 2 - pair
     * 3 - two pairs
     * 4 - three of a kind
     * 5 - full house
     * 6 - four of a kind
     * 7 - five of a kind
     * */
    public static LinkedHashMap<String,Integer> getHandsType(){
        LinkedHashMap<String,Integer> handsType = new LinkedHashMap<>();
        for ( String hand: camelHands.keySet()) {
//            out.println(hand);
            LinkedHashMap<Character, Integer> characterCount = new LinkedHashMap<>();
            // GET CHARACTER COUNT FOR EACH CHAR IN STRING HAND
            for ( int i=0; i< hand.length(); i++ ) {
                int count =0;
                char current = hand.charAt(i);
                for ( int j=0; j<hand.length() ; j++ ) {
                    if( i == j ) continue;
                    if( current == hand.charAt(j) ) count++;
                }
                if( !characterCount.containsKey(current) ) characterCount.put(current, count+1);
            }
//            out.println(characterCount);
            switch (characterCount.size()) {
                case 5: // high card strength value = 1
                    handsType.put(hand, 1);
                    break;
                case 4: // one pair strength value = 2
                    handsType.put(hand, 2);
                    break;
                case 3: // 3 of kind or 2 pairs
                    for ( char c : characterCount.keySet() ) {
                        if( characterCount.get(c)==3 ){
                            // 3 of a kind strength value = 4
                            handsType.put(hand, 4);
                            break;
                        }
                        else if( characterCount.get(c)==2 ){
                            // 2 pairs strength value = 3
                            handsType.put(hand, 3);
                            break;
                        }
                    }
                    break;
                case 2: // 4 of a kind or full house
                    for ( char c : characterCount.keySet() ) {
                        if( characterCount.get(c)==4 ){
                            // 4 of a kind strength value = 6
                            handsType.put(hand, 6);
                            break;
                        }
                        else if( characterCount.get(c)==2||characterCount.get(c)==3 ){
                            // full house strength value = 5
                            handsType.put(hand, 5);
                            break;
                        }
                    }
                    break;
                case 1 :  // 5 of a kind strength value = 7
                    handsType.put(hand, 7);
                    break;
            }
        }
        return handsType;
    }
    // get hands type for part 2 dealing with the jokers
    public static LinkedHashMap<String,Integer> getHandsTypePart2(){
        LinkedHashMap<String,Integer> handsType = new LinkedHashMap<>();
        for ( String hand: camelHands.keySet()) {
            LinkedHashMap<Character, Integer> characterCount = new LinkedHashMap<>();
            for ( int i=0; i< hand.length(); i++ ) {
                int count =0;
                char current = hand.charAt(i);
                for ( int j=0; j<hand.length() ; j++ ) {
                    if( i == j ) continue;
                    if( current == hand.charAt(j) ) count++;
                }
                if( !characterCount.containsKey(current) ) characterCount.put(current, count+1);
            }
            // jokers logic
            for ( Character c: characterCount.keySet()) {
                if( c == 'J' ){
                    if( characterCount.get(c) == 5 ) break;
                    int jokerCount = characterCount.get(c);
                    char maxKey=0;
                    int max = 0;
                    for ( char cs: characterCount.keySet()) {
                        if( cs == c ) continue;
                        if( characterCount.get(cs) > max ) {
                            maxKey = cs;
                            max = characterCount.get(cs);
                        }
                    }
                    characterCount.replace(maxKey, max, max+jokerCount);
                    characterCount.remove(c);
                    break;
                }
            }

            switch (characterCount.size()) {
                case 5: // high card strength value = 1
                    handsType.put(hand, 1);
                    break;
                case 4: // one pair strength value = 2
                    handsType.put(hand, 2);
                    break;
                case 3: // 3 of kind or 2 pairs
                    for ( char c : characterCount.keySet() ) {
                        if( characterCount.get(c)==3 ){
                            // 3 of a kind strength value = 4
                            handsType.put(hand, 4);
                            break;
                        }
                        else if( characterCount.get(c)==2 ){
                            // 2 pairs strength value = 3
                            handsType.put(hand, 3);
                            break;
                        }
                    }
                    break;
                case 2: // 4 of a kind or full house
                    for ( char c : characterCount.keySet() ) {
                        if( characterCount.get(c)==4 ){
                            // 4 of a kind strength value = 6
                            handsType.put(hand, 6);
                            break;
                        }
                        else if( characterCount.get(c)==2||characterCount.get(c)==3 ){
                            // full house strength value = 5
                            handsType.put(hand, 5);
                            break;
                        }
                    }
                    break;
                case 1 :  // 5 of a kind strength value = 7
                    handsType.put(hand, 7);
                    break;
            }
        }
        return handsType;
    }

    // returns true is firstHand is stronger than secondHand
    public static boolean isStronger(String firstHand, String secondHand){
        for ( int i=0; i<firstHand.length(); i++) {
            char firstHandCharacter = firstHand.charAt(i);
            char secondHandCharacter = secondHand.charAt(i);
            int firstHandCharacterRankVal = 0;
            int secondHandCharacterRankVal = 0;
            for ( char c: rankValues.keySet()) {
                if( c == firstHandCharacter ) firstHandCharacterRankVal = rankValues.get(c);
                if( c == secondHandCharacter ) secondHandCharacterRankVal = rankValues.get(c);
            }
            if( firstHandCharacterRankVal == secondHandCharacterRankVal ) continue;
            return firstHandCharacterRankVal>secondHandCharacterRankVal;
        }
        return false;
    }
    // returns true is firstHand is stronger than secondHand with the jokers
    public static boolean isStrongerPart2(String firstHand, String secondHand){
        for ( int i=0; i<firstHand.length(); i++) {
            char firstHandCharacter = firstHand.charAt(i);
            char secondHandCharacter = secondHand.charAt(i);
            int firstHandCharacterRankVal = 0;
            int secondHandCharacterRankVal = 0;
            for ( char c: rankValuesPart2.keySet()) {
                if( c == firstHandCharacter ) firstHandCharacterRankVal = rankValuesPart2.get(c);
                if( c == secondHandCharacter ) secondHandCharacterRankVal = rankValuesPart2.get(c);
            }
            if( firstHandCharacterRankVal == secondHandCharacterRankVal ) continue;
            return firstHandCharacterRankVal>secondHandCharacterRankVal;
        }
        return false;
    }

    public static long solution1(){
        LinkedHashMap<String,Integer> handsType = getHandsType();

        // loop through the ranks and sort them in ascending order for each rank group
        // 1-7
        LinkedList<String> sorted = new LinkedList<>();
        for ( int i=1; i<8; i++ ) {
            int index=i;
            Deque<String> rank =handsType.keySet().stream().filter(hand->handsType.get(hand)==index).collect(Collectors.toCollection(LinkedList::new));
            while(rank.size()>0){
                String min = "";
                for ( String hand: rank) {
                    min = hand;
                    for ( String h: rank) {
                        if( min.equals(h) ) continue;
                        // check is min is stronger hand if so min = hand
                        if( isStronger(min, h) ) min = h;
                    }
                }
                sorted.add(min);
                rank.remove(min);
            }
        }

        int rank = 1;
        long sum = 0;
        for ( String hand: sorted) {
            sum += camelHands.get(hand) * rank;
            rank ++;
        }
        return sum;
    }

    public static long solution2(){
        LinkedHashMap<String,Integer> handsType = getHandsTypePart2();
        LinkedList<String> sorted = new LinkedList<>();
        for ( int i=1; i<8; i++ ) {
            int index=i;
            Deque<String> rank =handsType.keySet().stream().filter(hand->handsType.get(hand)==index).collect(Collectors.toCollection(LinkedList::new));
            while(rank.size()>0){
                String min = "";
                for ( String hand: rank) {
                    min = hand;
                    for ( String h: rank) {
                        if( min.equals(h) ) continue;
                        // check is min is stronger hand if so min = hand
                        if( isStrongerPart2(min, h) ) min = h;
                    }
                }
                sorted.add(min);
                rank.remove(min);
            }
        }
        int rank = 1;
        long sum = 0;
        for ( String hand: sorted) {
            sum += camelHands.get(hand) * rank;
            rank ++;
        }
        return sum;
    }


    
    
}
