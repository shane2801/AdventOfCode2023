import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.System.*;

public class Day2{
    public static void main(String[] args){
        out.println("The answer for part 1 is : " + solution1());
        out.println("The answer for part 2 is : " + solution2());
    }

    public static HashMap<String,List<String>> getInput(){
        HashMap<String,List<String>> map = new HashMap<>();
        try{
            File file=new File("puzzles/puzzle2.txt");
            Scanner scanner=new Scanner(file);
            while(scanner.hasNextLine()){
                String line=scanner.nextLine();
                String[] temp=line.split(": ");
                String gameNumber = temp[0].split(" ")[1];
                String[] cubeSubsets = temp[1].split("; ");
                map.put(gameNumber, Arrays.asList(cubeSubsets));
            }
            scanner.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
//        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
//            String key = entry.getKey();
//            List<String> value = entry.getValue();
//            System.out.println("Key=" + key + ", Value=" + value);
//        }
        return map;
    }
    public static int solution1(){
        final int red = 12;
        final int green = 13;
        final int blue = 14;
        HashMap<String, List<String>> input = getInput();
        List<String> validGames = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : input.entrySet()) {
            String key = entry.getKey();
            List<String> games = entry.getValue();
//            System.out.println("Key=" + key + ", Value=" + games);
            boolean isValid = true;
            for ( String game: games) {
                String[] sets = game.split(", ");
                for ( String set : sets ) {
                    int ballNumber=Integer.parseInt(set.split(" ")[0]);
                    String ballColor=set.split(" ")[1];
                    if( ballColor.equals("red")&&ballNumber>red||ballColor.equals("green")&&ballNumber>green||ballColor.equals("blue")&&ballNumber>blue ){
                        isValid=false;
                        break;
                    }
                }
            }
            if( isValid) validGames.add(key);
            
        }

//        out.println(validGames);
        int sum = 0;
        for ( String validGame: validGames) {
            sum += Integer.parseInt(validGame);
        }
        return sum;
    }
    public static int solution2(){
        HashMap<String, List<String>> input = getInput();
//        List<Integer> gamePowers = new ArrayList<>();
        int total = 0;
        for (Map.Entry<String, List<String>> entry : input.entrySet()) {
            String key = entry.getKey();
            List<String> games = entry.getValue();
//            System.out.println("Key=" + key + ", Value=" + games);
            int maxRed = 0;
            int maxBlue = 0;
            int maxGreen = 0;

            for ( String game: games) {
                String[] sets = game.split(", ");
                for ( String set : sets ) {
                    int ballNumber=Integer.parseInt(set.split(" ")[0]);
                    String ballColor=set.split(" ")[1];

                    if( ballColor.equals("red")&&ballNumber>maxRed ) maxRed=ballNumber;
                    if( ballColor.equals("green")&&ballNumber>maxGreen ) maxGreen=ballNumber;
                    if( ballColor.equals("blue")&&ballNumber>maxBlue ) maxBlue=ballNumber;
                }
            }
            int gamePower = maxRed * maxBlue * maxGreen;
            total += gamePower;
//            gamePowers.add(gamePower);
        }
//        out.println(gamePowers);
        return total;
    }
}