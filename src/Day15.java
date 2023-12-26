import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.System.*;

public class Day15{
    public static void main(String[] args){
        out.println("The solution for part one is " + solution1());
        out.println("The solution for part two is " + solution2());
    }
    public static List<String> getInput(){
        List<String> strings = new LinkedList<>();
        try{
            File file=new File("puzzles/puzzle15.txt");
            Scanner scanner=new Scanner(file);
            while(scanner.hasNextLine()){
                strings =Arrays.asList(scanner.nextLine().split(","));
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return strings;
    }
    public static int solution1(){
        //returns sum of the hash values for each string in getInput()
        return getInput().stream().mapToInt(Day15::getHash).sum();
    }
    public static int getHash(String s){
        char[] chars = s.toCharArray();
        int currentValue = 0;
        for ( char c: chars) currentValue=((currentValue+c) * 17) % 256;
        return currentValue;
    }

    public static int solution2(){
        List<String> strings = getInput();
        Map<Integer, LinkedList<String>> map = new HashMap<>();
        // initialize map
        for ( int i=0; i<256; i++ ) map.put(i, new LinkedList<>());

        // populate map
        for ( String s: strings) {
            String label;
            int box;
            if( s.contains("=") ){
                label = s.split("=")[0];
                box = getHash(label);
                int focalLength = Integer.parseInt(s.substring(s.length()-1));
                // if the box is empty add item to box
                if( map.get(box).size() == 0 ) map.get(box).add(label+" "+focalLength);
                else{
                    // check if we have an item with same label and if so just replace the focal length with new focal length value
                    LinkedList<String> lenses = map.get(box);
                    boolean replaced=false;
                    int index=0;
                    for ( String l : lenses ) {
                        String currentLabel=l.split(" ")[0];
                        if( currentLabel.equals(label) ){
                            map.get(box).set(index, label + " " + focalLength);
                            replaced=true;
                            break;
                        }
                        index++;
                    }
                    // box does not already contain label if we reach here, add item to box
                    if( !replaced ) map.get(box).add(label+" "+focalLength);
                }
            }
            else if( s.contains("-") ){
                label = s.split("-")[0];
                box = getHash(label);
                // check if the box is not empty, if empty do nothing
                if( map.get(box).size() > 0 ){
                    List<String> lenses = map.get(box);
                    // check the lenses to see if we find one with the same label and if so remove it
                    for ( String l: lenses) {
                        String currentLabel = l.split(" ")[0];
                        if( currentLabel.equals(label) ) {
                            map.get(box).remove(l);
                            break;
                        }
                    }
                }
            }
        }

        // calculate focusing power
        int totalFocusingPower = 0;
        for ( int box: map.keySet()) {
            List<String> lenses = map.get(box);
            int boxNumber = box+1;
            for ( int i=0; i<lenses.size(); i++ ) {
                int position = i+1;
                int focalLength = Integer.parseInt(lenses.get(i).split(" ")[1]);
                // 1+ box number * slot position * focal length
                totalFocusingPower  += (boxNumber * position * focalLength);
            }
        }
        return totalFocusingPower;
    }

}
