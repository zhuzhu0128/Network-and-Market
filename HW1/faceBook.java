/**
 * Nerwork and Market Ying Zhu
 */
import java.io.*;
import java.util.*;
public class faceBook {
    public static void main(String[] args){
        // Load the facebook data
       HashMap<String, List<String>> map = new HashMap<>();
        int cnt =0;
        try {
            Scanner input = new Scanner(new FileReader("/Users/yingzhu/Desktop/facebook_combined.txt"));
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] tempdata = line.trim().split("\\s+");
                cnt++;
                if(!map.containsKey(tempdata[0])){
                    map.put(tempdata[0],new ArrayList<>());
                }
                map.get(tempdata[0]).add(tempdata[1]);
                if(!map.containsKey(tempdata[1])){
                    map.put(tempdata[1],new ArrayList<>());
                }
                map.get(tempdata[1]).add(tempdata[0]);
            }
        }catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        undiGraph G = new undiGraph();
        G.averageShortestPath(map);
        // estimate prob
        double prob = cnt/(4039*4038/2.0);
        System.out.println(prob);
        // run the facebook probability in random edge graph
        System.out.println(G.averageShortestPath(4039, 10000, prob));
    }
}
