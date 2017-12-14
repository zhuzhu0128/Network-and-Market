/**
 * NetWork and Market HW Part 3 Ying Zhu
 */
import java.util.*;
import java.io.*;
public class undiGraph{
    public HashMap<String, List<String>> buildGraph(int number, double prob){
        HashMap<String, List<String>> map = new HashMap<>();
        Random generator = new Random();
        generator.setSeed(0);
        for(int i=1;i<=number;i++){
            if(!map.containsKey(String.valueOf(i))) map.put(String.valueOf(i),new ArrayList<String>());
            for(int j=i+1;j<=number;j++){
                double random_val = generator.nextDouble();
                if(random_val<=prob){
                    map.get(String.valueOf(i)).add(String.valueOf(j));
                    if(!map.containsKey(String.valueOf(j))) map.put(String.valueOf(j),new ArrayList<String>());
                    map.get(String.valueOf(j)).add(String.valueOf(i));
                }
            }
        }
        return map;
    }

    public int shortestPath (HashMap<String,List<String>> map,String startNode, String endNode){
       HashMap<String, Integer> pathLen = new HashMap<String,Integer>();
        Queue<String> queue = new LinkedList<String>();
        queue.add(startNode);
        pathLen.put(startNode, 0);
        while(!queue.isEmpty()){
            String curNode = queue.poll();
            int curlen = pathLen.get(curNode)+1;
            for(String s:map.get(curNode)){
                if(!pathLen.containsKey(s)){
                    queue.add(s);
                    pathLen.put(s,curlen);
                    if(s.equals(endNode)) return pathLen.get(s);
                }
            }
        }
        return map.size()+1;
    }

    public double averageShortestPath(int nodeNum, int n, double p){
        HashMap<String, List<String>> testmap = buildGraph(nodeNum,p);
        Random generator1 = new Random();
        generator1.setSeed(1);
        Random generator2 = new Random();
        generator2.setSeed(2);
        double sum=0, cnt=0;
        for(int i=0;i<n;i++){
            int nodeA = generator1.nextInt(nodeNum)+1;
            int nodeB = generator2.nextInt(nodeNum)+1;
            if(nodeA==nodeB) {
                nodeA = generator1.nextInt(nodeNum)+1;
                nodeB = generator2.nextInt(nodeNum)+1;
                i--;
            }
            int templen = shortestPath(testmap, Integer.toString(nodeA), Integer.toString(nodeB));
           if(i<=100) {
                System.out.println("nodeA is" + nodeA+"  "+"nodeB is" + nodeB+"   "+"Path Length is" +templen);
            }
            if ( templen!= nodeNum+1 && templen != 0) {
                sum+=templen;
                cnt++;
            }
            else if(templen==nodeNum+1){
                System.out.print("infinite exist");
            }
        }
        System.out.println("Final Average is "+ sum/cnt);
        return sum/cnt;
    }

    // overloading average shortest path function for facebook data
    public double averageShortestPath(HashMap<String,List<String>> testmap){
        Random generator1 = new Random();
        generator1.setSeed(5);
        Random generator2 = new Random();
        generator2.setSeed(6);
        double sum=0, cnt=0;
        for(int i=0;i<10000;i++){
            int nodeA = generator1.nextInt(4039);
            int nodeB = generator2.nextInt(4039);
            int templen = shortestPath(testmap, Integer.toString(nodeA), Integer.toString(nodeB));
            if(i<10) {
                System.out.println("nodeA " + nodeA+"  "+"nodeB " + nodeB+"   "+"Path Length is" +templen);
            }
            if ( templen!= 4039+1 && templen != 0) {
                sum+=templen;
                cnt++;
            }
        }
        System.out.println("Final Average is "+ sum/cnt);
        return sum/cnt;
    }

    public static void main(String arg[]){
        undiGraph G = new undiGraph();
        G.averageShortestPath(1000,10000,0.1);
        // Draw the average shortest path graph
        double[] record = new double[14];
        double[] pvalue = new double[14];
        double start1=0.01, start2=0.05;
        for(int i=0;i<14;i++){
            if(i<4){
                pvalue[i]=start1;
                start1+=0.01;
            }else{
                pvalue[i]=start2;
                start2+=0.05;
            }
            record[i]=G.averageShortestPath(1000,10000,pvalue[i]);
        }
        try {
            PrintStream fileStream = new PrintStream("output.txt");
            System.setOut(fileStream);
            for (int i = 0; i < record.length; i++) {
                System.out.println(record[i]);
            }
        }catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}