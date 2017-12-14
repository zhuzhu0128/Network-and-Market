/**
 * Created by yingzhu on 11/14/17.
 */
import java.util.*;
import java.io.*;
import java.lang.*;
public class GSP_BRD {
    public static void main(String args[]) throws IOException{
        GSP_BRD gsp = new GSP_BRD();
        HashMap<String,Integer> value = gsp.graphreader("/Users/yingzhu/Desktop/GSP_test.txt"); // hashmap only store buyer's value for each identical good
        HashMap<String,Integer> random_start = gsp.random(value.size());
        gsp.display_price(random_start);
        HashMap<String,Integer> utility = new HashMap<>();
        List<String> list = new ArrayList<>(); // list of buyers
        List<String> list_random = new ArrayList<>(); // list of buyers
        for(int i=0;i<value.size();i++){
            list.add(Integer.toString(i));
            list_random.add(Integer.toString(i));
        }
        Collections.sort(list,new Comparator<String>(){
            @Override
            public int compare(String s1, String s2){
                return value.get(s1) - value.get(s2);
            }
        });
        gsp.display_assign(list);

        Collections.sort(list_random,new Comparator<String>(){
            @Override
            public int compare(String s1, String s2){
                return random_start.get(s1) - random_start.get(s2);
            }
        });
        gsp.display_assign(list_random);
        gsp.full_utility(utility,list_random,value,random_start);
        int iteration =0;
        Random generator = new Random();
        //generator.setSeed(7);
        // HashSet to record bid history
        HashSet<HashMap<String,Integer>> record = new HashSet<>();
        while(iteration<10000){       //!list_random.equals(list)&&
                int num = generator.nextInt(random_start.size());
                String buyer = list_random.get(num);
                int initial_price = random_start.get(list_random.get(num));
                // System.out.println("initial price" + initial_price);
                int cur_max = utility.get(list_random.get(num));
                int move_price = initial_price;
                for (int k = 0; k <= 100; k++) {
                    random_start.remove(buyer);
                    random_start.put(buyer, k);
                    Collections.sort(list_random, new Comparator<String>() {
                        @Override
                        public int compare(String s1, String s2) {
                            return random_start.get(s1) - random_start.get(s2);
                        }
                    });
                    gsp.full_utility(utility, list_random, value, random_start);
                    int utility_k = utility.get(buyer);
                    if (utility_k > cur_max) {
                        move_price = k;
                        cur_max = utility_k;
                    }
                }
                if (move_price!= initial_price) {
                    System.out.println("iteration " +iteration);
                    System.out.println("buyer " + buyer + "move");
                    random_start.remove(buyer);
                    random_start.put(buyer, move_price);
                    Collections.sort(list_random, new Comparator<String>() {
                        @Override
                        public int compare(String s1, String s2) {
                            return random_start.get(s1) - random_start.get(s2);
                        }
                    });
                    gsp.full_utility(utility, list_random, value, random_start);
                    gsp.display_price(random_start);
                    if(record.contains(random_start)){
                        System.out.println("REPEATED");
                    }else{
                        record.add(copyMap(random_start));
                    }
                    gsp.display_assign(list_random);
                    System.out.println();
                    System.out.println("buyers' utility");
                    gsp.display_utility(utility);
                    iteration++;
                }else{
                    random_start.remove(buyer);
                    random_start.put(buyer, initial_price);
                    Collections.sort(list_random, new Comparator<String>() {
                        @Override
                        public int compare(String s1, String s2) {
                            return random_start.get(s1) - random_start.get(s2);
                        }
                    });
                    gsp.full_utility(utility, list_random, value, random_start);
                }
        }
    }
    // function to read in txt file
    public HashMap<String,Integer> graphreader(String inputFilePath) throws IOException{
        HashMap<String,Integer> value = new HashMap<>();
        try(BufferedReader br = new BufferedReader(new FileReader (new File(inputFilePath)))){
            String line;
            while((line = br.readLine()) != null){
                // process the line to store the value for each player
                String[] str = line.split(" ");
                value.put(str[0],Integer.valueOf(str[1]));
            }
        }
        return value;
    }
    // function to generate random start state for buyer
    public HashMap<String, Integer> random (int size){
        HashMap<String, Integer> random_start = new HashMap<>();
        Random generator = new Random();
        generator.setSeed(1);
        for(int i=0;i<size;i++){
            int num = generator.nextInt(50)+1;
            random_start.put(Integer.toString(i),num);
        }
        return random_start;
    }
    // function to calculate individual utility upon adjustment
    // function to calculate utility for all buyers
    public void full_utility(HashMap<String,Integer> utility, List<String>list,HashMap<String,Integer> value, HashMap<String,Integer> random_start){
        for(int i=0;i<list.size();i++){
            int cur_utility;
            if(i==0) cur_utility = (i+1)*(value.get(list.get(i))-random_start.get(list.get(i)));
            else cur_utility = (i+1)*(value.get(list.get(i))-random_start.get(list.get(i-1)));
            utility.remove(list.get(i));
            utility.put(list.get(i),cur_utility);
        }
    }
    // function to calculate utility for specific buyer
    public int utility_indiv(HashMap<String,Integer> utility, List<String> list_random ,HashMap<String,Integer> value, HashMap<String,Integer> random_start, String buyer){
        int num=0;
        for(int i=0;i<list_random.size();i++){
            if(list_random.get(i).equals(buyer)) num=i;
        }
        int cur_utility;
        if(num==0) cur_utility = (num+1)*(value.get(list_random.get(num))-random_start.get(list_random.get(num)));
        else cur_utility = (num+1)*(value.get(list_random.get(num))-random_start.get(list_random.get(num-1)));
        return cur_utility;
    }
    // helper function to display assignment of bundle
    public void display_assign(List list){
        for(int i=0;i<list.size();i++){
            System.out.print("bundle " +i+ ": " +list.get(i) + "    ");
        }
    }
    // helper function to display buyer's posted price
    public void display_price(HashMap<String,Integer> random_start){
        for(String buyer: random_start.keySet()){
            System.out.println(buyer + "bid "+ random_start.get(buyer)+ "    ");
        }
    }
    // helper function to display utility
    public void display_utility(HashMap<String,Integer> utility){
        for(String buyer: utility.keySet()){
            System.out.println(buyer + " "+ utility.get(buyer)+ "    ");
        }
    }
    // helper function to do deep copy
    public static HashMap<String, Integer> copyMap(HashMap<String, Integer> original)
    {
        HashMap<String, Integer> copy = new HashMap<>();
        for (Map.Entry<String, Integer> entry : original.entrySet())
        {
            copy.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }

}
