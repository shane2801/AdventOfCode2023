import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Day20{
    static Map<String, List<String>> modules = new LinkedHashMap<>();
    public static void main(String[] args){
        init();
        out.println("The solution for part one is " + solution1());
        out.println("The solution for part two is " + solution2());
    }

    // init instructions
    public static void init(){
        try{
            File file=new File("puzzles/puzzle20.txt");
            Scanner scanner=new Scanner(file);
            while(scanner.hasNextLine()){
                String[] line = scanner.nextLine().split(" -> ");
                modules.put(line[0], Arrays.asList(line[1].split(", ")));
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }

    }
    public static int solution1(){
        List<String> broadcastTargets= new ArrayList<>();
        Map<String, Module> modulesList = new LinkedHashMap<>();
        for ( String key: modules.keySet()) {
            List<String> outputs = modules.get(key);
            if( key.equals("broadcaster") ) broadcastTargets = outputs;
            else{
                String type =key.substring(0,1);
                String name = key.substring(1);
                Module temp = type.equals("%") ? new Module(name, type,outputs ) : new Module(name, type, outputs, new HashMap<>());
                modulesList.put(name, temp);
            }
        }
        modulesList.keySet().forEach(name->modulesList.get(name).getDestination().stream().filter(output->modulesList.containsKey(output)&&modulesList.get(output).getType().equals("&")).forEach(output->modulesList.get(output).setConjunctionMemory(name, "lo")));

        int lo=0, hi =0;
        for ( int i=0; i<1000; i++ ) {
            lo += 1;
            Deque<List<String>> q = new LinkedList<>();
            broadcastTargets.stream().map(broadcastTarget->createTriple("broadcaster", broadcastTarget, "lo")).forEach(q::addLast);
            while(q.size()>0){
                List<String> triple = q.removeFirst();
                String origin = triple.get(0);
                String target = triple.get(1);
                String pulse = triple.get(2);
                if( pulse.equals("lo") ) lo+=1; else hi+=1;
                if( !modulesList.containsKey(target) ) continue;
                Module module = modulesList.get(target);
                if( module.getType().equals("%") ){
                    if( pulse.equals("lo") ){
                        String memory=module.getFlipFlopMemory();
                        if( memory.equals("off") ) module.setFlipFlopMemory("on"); else module.setFlipFlopMemory("off");
                        memory = module.getFlipFlopMemory();
                        String outgoing=memory.equals("on") ? "hi" : "lo";
                        module.getDestination().stream().map(o->createTriple(module.getName(), o, outgoing)).forEach(q::addLast);
                    }
                }
                else{
                    module.getConjunctionMemory().put(origin,pulse);
                    Map<String, String> conjunctionMemory = module.getConjunctionMemory();
                    int count =(int) conjunctionMemory.keySet().stream().filter(k->conjunctionMemory.get(k).equals("hi")).count();
                    String outgoing = count == conjunctionMemory.size() ? "lo" : "hi";
                    module.getDestination().stream().map(o->createTriple(module.getName(), o, outgoing)).forEach(q::addLast);
                }
            }
        }
        out.println(lo);
        out.println(hi);
        return lo*hi;
    }
    public static long solution2(){
        List<String> broadcastTargets= new ArrayList<>();
        Map<String, Module> modulesList = new LinkedHashMap<>();
        for ( String key: modules.keySet()) {
            List<String> outputs = modules.get(key);
            if( key.equals("broadcaster") ) broadcastTargets = outputs;
            else{
                String type =key.substring(0,1);
                String name = key.substring(1);
                Module temp = type.equals("%") ? new Module(name, type,outputs ) : new Module(name, type, outputs, new HashMap<>());
                modulesList.put(name, temp);
            }
        }
        modulesList.keySet().forEach(name->modulesList.get(name).getDestination().stream().filter(output->modulesList.containsKey(output)&&modulesList.get(output).getType().equals("&")).forEach(output->modulesList.get(output).setConjunctionMemory(name, "lo")));

        // module that feeds into output module in this case rx
        String feed = getFeed(modulesList, "rx");
        Map<String,Integer> cycleLengths = new LinkedHashMap<>();
        Map<String,Integer> seen = getCycleLengths(modulesList, feed);

        int presses = 0;
        while(true){
            presses += 1;
            Deque<List<String>> q = new LinkedList<>();
            broadcastTargets.stream().map(broadcastTarget->createTriple("broadcaster", broadcastTarget, "lo")).forEach(q::addLast);
            while(q.size()>0){
                List<String> triple = q.removeFirst();
                String origin = triple.get(0);
                String target = triple.get(1);
                String pulse = triple.get(2);

                if( !modulesList.containsKey(target) ) continue;
                Module module = modulesList.get(target);

                if( module.getName().equals(feed) && pulse.equals("hi") ){
                    seen.put(origin, seen.get(origin)+1);
                    if( !cycleLengths.containsKey(origin) ) cycleLengths.put(origin, presses);
                    if( allEqual(seen) ){
                        out.println(seen);
                        out.println(cycleLengths);
                        out.println("exiting");
                        long x = 1;
                        for ( int c: cycleLengths.values() ) x=lcm(x, c);
                        return x;
                    }
                }

                if( module.getType().equals("%") ){
                    if( pulse.equals("lo") ){
                        String memory=module.getFlipFlopMemory();
                        if( memory.equals("off") ) module.setFlipFlopMemory("on"); else module.setFlipFlopMemory("off");
                        memory = module.getFlipFlopMemory();
                        String outgoing=memory.equals("on") ? "hi" : "lo";
                        module.getDestination().stream().map(o->createTriple(module.getName(), o, outgoing)).forEach(q::addLast);
                    }
                }
                else{
                    module.getConjunctionMemory().put(origin,pulse);
                    Map<String, String> conjunctionMemory = module.getConjunctionMemory();
                    int count =(int) conjunctionMemory.keySet().stream().filter(k->conjunctionMemory.get(k).equals("hi")).count();
                    String outgoing = count == conjunctionMemory.size() ? "lo" : "hi";
                    module.getDestination().stream().map(o->createTriple(module.getName(), o, outgoing)).forEach(q::addLast);
                }
            }
        }
    }
    public static List<String> createTriple(String x, String y, String z){
        return new ArrayList<>(){{ add(x);add(y);add(z);}};
    }

    public static String getFeed(Map<String,Module> modulesList,String outputModule ){
        return modulesList.keySet().stream().filter(m->modulesList.get(m).getDestination().stream().anyMatch(o->o.equals(outputModule))).findFirst().orElse("");
    }

    public static Map<String,Integer> getCycleLengths(Map<String,Module> modulesList, String feed){
        return modulesList.keySet().stream().filter(m->modulesList.get(m).getDestination().contains(feed)).collect(Collectors.toMap(m->m, m->0, (a, b)->b));
    }

    public static boolean allEqual(Map<String,Integer> map){
        Set<Integer> values = new HashSet<>(map.values());
        return values.size() == 1;
    }

    private static long lcm(long a, long b){
        return a * (b / gcd(a, b));
    }

    private static long gcd(long a, long b){
        while (b > 0){
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

}
 class Module{
    String name;
    String type;
    List<String> destination;
    String flipFlopMemory;
    Map<String, String> conjunctionMemory;

    public Module(String _name, String _type, List<String> _destination, Map<String, String> _conjunctionMemory){
        name = _name;
        type = _type;
        destination = _destination;
        conjunctionMemory = _conjunctionMemory;
    }

     public Module(String _name, String _type, List<String> _destination){
         name = _name;
         type = _type;
         flipFlopMemory = type.equals("%")? "off" : "";
         destination = _destination;
     }

    public String toString(){
        return type.equals("%")? "name: " + name + " type " + type + " memory " + flipFlopMemory + " destination " + destination : "name: " + name + " type " + type + " memory " + conjunctionMemory + " destination " + destination ;
    }

     public List<String> getDestination(){
         return destination;
     }

     public Map<String, String> getConjunctionMemory(){
         return conjunctionMemory;
     }

     public String getFlipFlopMemory(){
         return flipFlopMemory;
     }

     public String getName(){
         return name;
     }

     public String getType(){
         return type;
     }

     public void setConjunctionMemory(String x, String y){
        conjunctionMemory.put(x, y);
     }

     public void setFlipFlopMemory(String x){
        flipFlopMemory = x;
     }
 }

