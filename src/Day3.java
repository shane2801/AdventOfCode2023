import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.System.*;

public class Day3{

//    final static int lenRow = 12;
//    final static int lenCol = 12;
    final static int lenRow = 142;
    final static int lenCol = 142;
    public static void main(String[] args){
        out.println("The answer for part one is : " + solution1());
        out.println("The answer for part two is : " + solution2());

    }
    public static String[][] getInput(){
        String[][] map = new String[lenRow][lenCol];
        try{
            File file=new File("puzzles/puzzle3.txt");
            Scanner scanner=new Scanner(file);
            int i = 0;
            while(scanner.hasNextLine()){
                String line="." +scanner.nextLine() +".";
                if( i == 0 ){
                    map[i] = ".".repeat(lenCol).split("");
                    i+=1;
                }
                String[] splitLine = line.split("");
                map[i] = splitLine;
                i++;
            }
            map[i] = ".".repeat(lenCol).split("");
            scanner.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
//        out.println("-----------------");
//        for ( String[] strings : map ) {
//            out.println(Arrays.toString(strings));
//        }
        return map;
    }
    public static int solution1(){
        List<String> validNumbers = new ArrayList<>();
        String[][] engineSchematic = getInput();
        for ( int i=1; i< lenRow-1; i++ ) {
            StringBuilder number =new StringBuilder();
            List<Integer> indexesToCheck = new ArrayList<>();
            for ( int j=1; j< lenCol-1; j++ ) {
                // if is digit add to number and continue until a symbol or "." is found to mark end of number
                if( !containsSpecialCharacter(engineSchematic[i][j]) ){
                    number.append(engineSchematic[i][j]);
                    indexesToCheck.add(j);

                    //edge case: number at the very end of a line, e.g .....456
                    if( j == lenCol-2 && !number.toString().equals("") ) {
                        boolean isValid = false;
                        // check neighbours
                        for ( int index: indexesToCheck) {
                            if( checkNeighbours(engineSchematic, i, index) ) isValid = true;
                        }
                        if( isValid ) {
                            validNumbers.add(number.toString());
                        }
                    }
                }
                // either a symbol and we skip or reached end of number and we check its neighbours to check its validity
                if( containsSpecialCharacter(engineSchematic[i][j]) ){
                    if( !number.toString().equals("") ){
                        boolean isValid = false;
                        // check neighbours
                        for ( int index: indexesToCheck) {
                           if( checkNeighbours(engineSchematic, i, index) ) isValid = true;
                        }

                        if( isValid ) validNumbers.add(number.toString());
                        number=new StringBuilder();
                        indexesToCheck = new ArrayList<>();
                    }

                }
            }
        }
        int sum = 0;
        for ( String number: validNumbers) {
            sum += Integer.parseInt(number);
        }
//        out.println(validNumbers);
//        out.println(sum);
        return sum;
    }

    // Returns true if s contains any character other than letters, numbers.  Returns false otherwise.
    public static boolean containsSpecialCharacter(String s) {
        return s!=null && s.matches("[^A-Za-z0-9]");
    }

    //Returns true if s contains any character other than letters, numbers and periods("."), Returns false otherwise.
    public static boolean containsSpecialCharacterExceptDot(String s) {
        return s!=null && s.matches("[^A-Za-z0-9.]");
    }

    // returns true if neighbour is a special character excluding "." else returns false
    public static boolean checkNeighbours(String[][] engineSchematic, int i, int index){
        return containsSpecialCharacterExceptDot(engineSchematic[i-1][index-1])||   // top left
               containsSpecialCharacterExceptDot(engineSchematic[i-1][index])||     // top
               containsSpecialCharacterExceptDot(engineSchematic[i-1][index+1])||   // top right
               containsSpecialCharacterExceptDot(engineSchematic[i][index-1])||     // left
               containsSpecialCharacterExceptDot(engineSchematic[i][index+1])||     // right
               containsSpecialCharacterExceptDot(engineSchematic[i+1][index-1])||   // bottom left
               containsSpecialCharacterExceptDot(engineSchematic[i+1][index+1])||   // bottom right
               containsSpecialCharacterExceptDot(engineSchematic[i+1][index]);      // bottom

    }


    // check if string is a number
    public static boolean isDigit(String s) {
        return s!=null && s.matches("[0-9]");
    }


    public static int checkNeighbours2(String[][] engineSchematic, int i, int j,  HashMap<Integer, LinkedList<List<Integer>>> map){
        Set<Integer> numbers = new HashSet<>();
        // top left
        if( isDigit(engineSchematic[i-1][j-1]) ){
            map.keySet().forEach(id->{
                List<List<Integer>> coordinates=map.get(id);
                if( coordinates.stream().anyMatch(coordinate->coordinate.size()>1&&coordinate.get(0)==i-1&&coordinate.get(1)==j-1) ){
                    numbers.add(map.get(id).getLast().get(0));
                }
            });
        }

        // top
        if( isDigit(engineSchematic[i-1][j]) ){
            map.keySet().forEach(id->{
                List<List<Integer>> coordinates=map.get(id);
                if( coordinates.stream().anyMatch(coordinate->coordinate.size()>1&&coordinate.get(0)==i-1&&coordinate.get(1)==j) ){
                    numbers.add(map.get(id).getLast().get(0));
                }
            });
        }

        // top right
        if( isDigit(engineSchematic[i-1][j+1]) ){
            map.keySet().forEach(id->{
                List<List<Integer>> coordinates=map.get(id);
                if( coordinates.stream().anyMatch(coordinate->coordinate.size()>1&&coordinate.get(0)==i-1&&coordinate.get(1)==j+1) ){
                    numbers.add(map.get(id).getLast().get(0));
                }
            });
        }

        // left
        if( isDigit(engineSchematic[i][j-1]) ){
            map.keySet().forEach(id->{
                List<List<Integer>> coordinates=map.get(id);
                if( coordinates.stream().anyMatch(coordinate->coordinate.size()>1&&coordinate.get(0)==i&&coordinate.get(1)==j-1) ){
                    numbers.add(map.get(id).getLast().get(0));
                }
            });
        }

        // right
        if( isDigit(engineSchematic[i][j+1]) ){
            map.keySet().forEach(id->{
                List<List<Integer>> coordinates=map.get(id);
                if( coordinates.stream().anyMatch(coordinate->coordinate.size()>1&&coordinate.get(0)==i&&coordinate.get(1)==j+1) ){
                    numbers.add(map.get(id).getLast().get(0));
                }
            });
        }

        // bottom left
        if( isDigit(engineSchematic[i+1][j-1]) ){
            map.keySet().forEach(id->{
                List<List<Integer>> coordinates=map.get(id);
                if( coordinates.stream().anyMatch(coordinate->coordinate.size()>1&&coordinate.get(0)==i+1&&coordinate.get(1)==j-1) ){
                    numbers.add(map.get(id).getLast().get(0));
                }
            });
        }

        // bottom
        if( isDigit(engineSchematic[i+1][j]) ){
            map.keySet().forEach(id->{
                List<List<Integer>> coordinates=map.get(id);
                if( coordinates.stream().anyMatch(coordinate->coordinate.size()>1&&coordinate.get(0)==i+1&&coordinate.get(1)==j) ){
                    numbers.add(map.get(id).getLast().get(0));
                }
            });
        }

        // bottom right
        if( isDigit(engineSchematic[i+1][j+1]) ){
            map.keySet().forEach(id->{
                List<List<Integer>> coordinates=map.get(id);
                if( coordinates.stream().anyMatch(coordinate->coordinate.size()>1&&coordinate.get(0)==i+1&&coordinate.get(1)==j+1) ){
                    numbers.add(map.get(id).getLast().get(0));
                }
            });
        }

        int gearRatio = 1;
        if( numbers.size() == 2 ){
            for ( int num: numbers) {
                gearRatio *= num;
            }
        }
//        out.println(numbers);
//        out.println(gearRatio);
        return gearRatio;
    }

    public static int solution2(){
        // go through the map
        // find all numbers
        // for each number
        // store the coordinates of each individual digit
        // id -> coordinates of individual numbers -> number

        String[][] engineSchematic = getInput();
        int id = 0;

        HashMap<Integer, LinkedList<List<Integer>>> map = new HashMap<>();

        // find all the numbers, store
        for ( int i=1; i< lenRow-1; i++ ) {
            LinkedList<List<Integer>> coordinates = new LinkedList<>();
            StringBuilder number =new StringBuilder();
            List<Integer> coordinate = new ArrayList<>();
            List<Integer> num = new ArrayList<>();
            for ( int j=1; j< lenCol-1; j++ ) {
                // if we find a digit, store its coordinates and add its digit to our current number
                if( !containsSpecialCharacter(engineSchematic[i][j]) ){
                    number.append(engineSchematic[i][j]);
                    coordinate.add(i);
                    coordinate.add(j);
                    coordinates.add(coordinate);
                    coordinate = new ArrayList<>();
                    //edge case: number at the very end of a line, e.g .....456
                    if( j == lenCol-2 && !number.toString().equals("") ) {
                        num.add(Integer.parseInt(number.toString()));
                        coordinates.add(num);
                        map.put(id, coordinates);
                        id +=1;
                    }
                }
                // we have reached the end of our number
                // store it in our map with a unique id(could have used the number itself as the ID but
                // the problem didn't mention that all the numbers were unique) and its value of
                // lists of coordinates with the actual number as a single list containing itself
                // e.g. Id : 1209 -> [[140, 138], [140, 139], [140, 140], [446]]
                // meaning 446 has id 1209, the first digit has coordinates[140, 138], the second digit is at[140,139], etc
                if( containsSpecialCharacter(engineSchematic[i][j]) ){
                    if( !number.toString().equals("") ){
                        num.add(Integer.parseInt(number.toString()));
                        coordinates.add(num);
                        map.put(id, coordinates);
                        //
                        number=new StringBuilder();
                        num = new ArrayList<>();
                        coordinates = new LinkedList<>();
                        id +=1;
                    }

                }
            }
        }

        // map visualizer
//        for (Integer i : map.keySet()) {
//            List<List<Integer>> value = map.get(i);
//            System.out.println("Id : "+i);
//            System.out.println("Coordinates + Number: " + value);
//        }

        // now that we can identify each number from each other, we can find our gearRatios
        int gearRatioSum = 0;
        for ( int i=1; i< lenRow-1; i++ ) {
            for ( int j=1; j< lenCol-1; j++ ) {
                // if we encounter a star check its neighbours
                if( engineSchematic[i][j].equals("*") ){
                    // check neighbours and if exactly two adjacent numbers are found checkNeighbours2 will return its gear ratio
                    // otherwise it will return 1
                    int gearRatio = checkNeighbours2(engineSchematic, i, j, map);
                    if( gearRatio != 1 ) gearRatioSum += gearRatio;
                }
            }
        }

        return gearRatioSum;
    }

}
