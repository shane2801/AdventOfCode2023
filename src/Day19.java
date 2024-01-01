import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import static java.lang.System.out;

public class Day19{
    static final Map<String, List<String>> workflows = new LinkedHashMap<>();
    static final List<Map<String, Integer>> partsRatings = new LinkedList<>();

    public static void main(String[] args){
        init();
        out.println("The solution for part one is " + solution1());
        out.println("The solution for part two is " + solution2());
    }

    // init instructions
    public static void init(){
        List<String> block1 = new ArrayList<>();
        List<String> block2 = new ArrayList<>();
        try{
            File file=new File("puzzles/puzzle19.txt");
            Scanner scanner=new Scanner(file);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                if( line.equals("") ){
                    while(scanner.hasNextLine()) block2.add(scanner.nextLine());
                    break;
                }
                else block1.add(line);
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }

        for ( String b1: block1 ) {
            // remove last "}" from string
            b1 = b1.substring(0, b1.length()-1);
            // split at "{" and add everything after "{" to temp removing whitespaces
            workflows.put(b1.split("\\{")[0], Arrays.asList(b1.split("\\{")[1].split(",")));
        }
        for ( String b2: block2 ) {
            // remove first "{" and last "}" from string
            b2 = b2.substring(1, b2.length()-1);
            String[] s = b2.split(",");
            Map<String, Integer> temp = new LinkedHashMap<>();
            for ( String ss: s) {
                String[] t = ss.split("=");
                temp.put(t[0], Integer.valueOf(t[1]));
            }
            partsRatings.add(temp);
        }
    }
    public static int solution1(){
        Map<Map<String, Integer>, String> result = new HashMap<>();
        for ( Map<String,Integer> partRating: partsRatings) {
            String key = "in";
            do {
                List<String> workflow=workflows.get(key);
                for ( String w : workflow ) {
                    boolean matched=false;
                    List<String> brokenDown=breakDownWorkflow(w);
                    // process workflow
                    if( brokenDown.size()==1 ){
                        key=brokenDown.get(0);
                        break;
                    }
                    else{
                        String variableName=brokenDown.get(0);
                        String operator=brokenDown.get(1);
                        int variableValue=Integer.parseInt(brokenDown.get(2));
                        String destination=brokenDown.get(3);
                        for ( String part : partRating.keySet() ) {
                            int partValue=partRating.get(part);
                            // < operator
                            if( operator.equals("<")&&variableName.equals(part)&&partValue<variableValue ){
                                key=destination;
                                matched=true;
                                break;
                            }
                            // > operator
                            else if( operator.equals(">")&&variableName.equals(part)&&partValue>variableValue ){
                                key=destination;
                                matched=true;
                                break;
                            }
                        }
                    }
                    if( matched ) break;
                }
                // break clause for while loop
            } while(!key.equals("R")&&!key.equals("A"));
            result.put(partRating, key);
        }
        return result.keySet().stream().filter(a->result.get(a).equals("A")).mapToInt(a->a.keySet().stream().mapToInt(a::get).sum()).sum();
    }

    public static long solution2(){
        Map<String, List<Integer>> xmas  = new LinkedHashMap<>();
        xmas.put("x", createRange(1,4000));
        xmas.put("m", createRange(1,4000));
        xmas.put("a", createRange(1,4000));
        xmas.put("s", createRange(1,4000));
        return getCombinations(xmas, "in");
    }

    // breakdown workflow instruction x<2169:lv into broken down [x, <, 2169, lv] and single instruction, A, R or ... into a singleton list
    public static List<String> breakDownWorkflow(String toProcess){
        List<String> brokenDown = new ArrayList<>();
        if( toProcess.contains(":") ){
            String[] temp = toProcess.split(":");
            String destination = temp[1];
            // <  or > occurrence
            if( temp[0].contains("<") || temp[0].contains(">")  ){
                String operator = temp[0].contains("<") ? "<" : ">";
                String[] temp2 = temp[0].split(operator);
                String variableName = temp2[0];
                String variableValue = temp2[1];
                brokenDown.add(variableName);
                brokenDown.add(operator);
                brokenDown.add(variableValue);
                brokenDown.add(destination);
            }
        }
        else brokenDown.add(toProcess);
        return brokenDown;
    }

    public static long getCombinations(Map<String, List<Integer>> ranges, String key){
        if( key.equals("R") ) return 0;
        if( key.equals("A") ) return ranges.keySet().stream().map(ranges::get).mapToLong(xmasRange->xmasRange.get(1)-xmasRange.get(0)+1).reduce(1, (a, b)->a * b);
        long total = 0;
        List<String> workflow = workflows.get(key);
        for ( String w: workflow) {
            List<String> brokenDown = breakDownWorkflow(w);
            if( brokenDown.size() > 1 ){
                String variableName=brokenDown.get(0);
                String operator=brokenDown.get(1);
                int variableValue=Integer.parseInt(brokenDown.get(2));
                String destination=brokenDown.get(3);
                int lo = ranges.get(variableName).get(0);
                int hi = ranges.get(variableName).get(1);
                List<Integer> rangeT, rangeF;
                if( operator.equals("<") ){
                    rangeT = createRange(lo, variableValue-1);
                    rangeF = createRange(variableValue, hi);
                }
                else{
                    rangeT = createRange(variableValue+1, hi);
                    rangeF = createRange(lo, variableValue);
                }
                if( rangeT.get(0) <= rangeT.get(1) ){
                    Map<String, List<Integer>> copy = new LinkedHashMap<>(ranges);
                    copy.put(variableName, rangeT);
                    total += getCombinations(copy, destination);
                }
                if( rangeF.get(0) <= rangeF.get(1) ) ranges.put(variableName, rangeF);
            }
            // fallback
            else total+=getCombinations(ranges, brokenDown.get(0));
        }
        return total;
    }

    public static List<Integer> createRange(int lo, int hi){
        return new ArrayList<>(){{ add(lo);add(hi);}};
    }

}
