import java.io.*;
import java.util.*;

/**
 * Created by ying zhu on 10/15/2017.
 */
import java.io.*;
public class Problem_9 {

    public static void main (String[] args) throws IOException {
        partA();
        //partB();
       //partC();
          //partBonus(0.2);
          //partBonus(0.3);
          //partBonus(0.4);
          //partBonus(0.5);
          //partBonus(0.6);
          //partBonus(0.7);
    }

    public static void partA () throws IOException {

        String input_file_name = "/Users/yingzhu/Desktop/fig31a.txt";
        Integer seed_number= 2;
        Double threshold = 0.4;
        Integer interation = 1;


        Map<Integer, List<Integer>> graph = graphFileReader(input_file_name);

        Set<Integer> affected = new HashSet<Integer>();
        affected.add(0);
        affected.add(1);
        for (int i = 0; i < interation; i++) {
            System.out.println(affected);
            brd(graph, affected, threshold);
            System.out.println("Affection Percentage is: "
                    + (double) affected.size() / (double) graph.size() * 100 + "%" +" with q =" + threshold);
        }

        String input_file_name1 = "/Users/yingzhu/Desktop/fig31b.txt";
        Integer seed_number1= 3;
        Double threshold1 = 0.4;
        Integer interation1 = 1;


        Map<Integer, List<Integer>> graph1 = graphFileReader(input_file_name1);
        Set<Integer> affected1 = new HashSet<Integer>();
        for(int k=0;k<3;k++){
            affected1.add(k);
        }

        for (int j = 0; j < interation1; j++) {
            System.out.println(affected1);
            brd(graph1, affected1, threshold1);
            System.out.println("Affection Percentage is: "
                    + (double) affected1.size() / (double) graph1.size() * 100 + "%" +" with q =" + threshold1);
        }

    }

    public static void partB () throws IOException {

        String input_file_name = "/Users/yingzhu/Desktop/facebook_combined.txt";
        Integer seed_number = 10;
        Double threshold = 0.1;
        Integer iteration = 500;

        Map<Integer, List<Integer>> graph = graphFileReader(input_file_name);

        double averagePercentage = 0;
        int numAffected =0;
        for (int i = 0; i < iteration; i++) {
            Set<Integer> affected = randomSeedSelector(seed_number, graph.size());
            //System.out.println(affected);
            brd(graph, affected, threshold);
            double percentage = (double) affected.size() / (double) graph.size() * 100;
            System.out.println("Iteration # " + i + "Affection Percentage is: " + percentage + "%");
            averagePercentage += percentage/iteration;
            numAffected +=affected.size();
        }

        System.out.println("Average Percentage is: " + averagePercentage);
        System.out.println("Average number of infected is:" + numAffected/iteration);
    }

    public static void partC () throws IOException {

        String input_file_name = "/Users/yingzhu/Desktop/facebook_combined.txt";
        double[][] display = new double[26][11];
        int seed_number = 0;
        double threshold =0;
        int iteration =10;
        Map<Integer, List<Integer>> graph = graphFileReader(input_file_name);


        for (int i = 0;i<=25;i++){
            seed_number = i*10;
            for (int j=0;j<=10;j++){
                threshold = j*0.05;
                double averagePercentage = 0;
                for (int k = 0; k < iteration; k++) {
                    Set<Integer> affected = randomSeedSelector(seed_number, graph.size());
                    //System.out.println(affected);
                    brd(graph, affected, threshold);
                    double percentage = (double) affected.size() / (double) graph.size() * 100;
                    // System.out.println("Iteration # " + k + "Affection Percentage is: " + percentage + "%");
                    averagePercentage += percentage/iteration;
                    display[i][j] = averagePercentage;
                }
            }
        }
        for(int g=0;g<=25;g++){
            for(int h=0;h<=10;h++){
                System.out.print(String.format("%.2f",display[g][h]) +" ");
            }
            System.out.println();
        }


       // System.out.println("Average Percentage is: " + averagePercentage);
    }

    public static void partBonus (double thresHold) throws IOException {

        String input_file_name = "/Users/yingzhu/Desktop/facebook_combined.txt";
        Map<Integer, List<Integer>> graph = graphFileReader(input_file_name);

        int l = 1;
        int r = graph.size();
        while (l < r) {
            int m = (l + r)/2;
            if (bonusHelper(graph, m, thresHold)) {
                r = m;
            } else {
                l = m + 1;
            }
        }

        System.out.println("Smallest Possible set size for "+ thresHold+" is : " + l);
    }

    public static boolean bonusHelper (Map<Integer, List<Integer>> graph, int seedNumber, double threshold) {
        Integer iteration = 1000;
        for (int i = 0; i < iteration; i++) {
            Set<Integer> affected = randomSeedSelector(seedNumber, graph.size());
            brd(graph, affected, threshold);
            if(affected.size() == graph.size()) {
                return true;
            }
        }

        return false;
    }


    /**
     * BRD Algorithm
     */
    public static void brd (Map<Integer, List<Integer>> graph, Set<Integer> affected, double threshold) {
        int[] affectedNeighbors = new int[graph.size()];
        Queue<Integer> newAffected = new LinkedList<>(affected);
        while (!newAffected.isEmpty()) {
            Integer spreadingNode = newAffected.poll();
            //System.out.println(spreadingNode);
            for (Integer neighbor : graph.get(spreadingNode)) {
                affectedNeighbors[neighbor]++;
                //System.out.println(neighbor);
                if (!affected.contains(neighbor) &&
                        (double) affectedNeighbors[neighbor] / (double) graph.get(neighbor).size() > threshold) {
                    newAffected.offer(neighbor);
                    affected.add(neighbor);
                }
            }
        }
    }



    /**
     * Random Seed Selector
     */
    public static Set<Integer> randomSeedSelector (int seedNumber, int graphSize) {
        List<Integer> list = new ArrayList<>();
        Set<Integer> affected = new HashSet<>();
        for (int i = 0; i < graphSize; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        for (int i = 0; i < seedNumber; i++) {
            affected.add(list.get(i));
        }
        return affected;
    }

    /**
     * File Reader
     */
    public static Map<Integer, List<Integer>> graphFileReader (String inputFilePath) throws IOException {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFilePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                // process the line to get an edge.
                String[] edge = line.split(" ");

                // add edge to graph
                if (graph.containsKey(Integer.valueOf(edge[0]))) {
                    graph.get(Integer.valueOf(edge[0])).add(Integer.valueOf(edge[1]));
                } else {
                    List<Integer> neiggbors = new ArrayList<>();
                    neiggbors.add(Integer.valueOf(edge[1]));
                    graph.put(Integer.valueOf(edge[0]), neiggbors);
                }

                if (graph.containsKey(Integer.valueOf(edge[1]))) {
                    graph.get(Integer.valueOf(edge[1])).add(Integer.valueOf(edge[0]));
                } else {
                    List<Integer> neiggbors = new ArrayList<>();
                    neiggbors.add(Integer.valueOf(edge[0]));
                    graph.put(Integer.valueOf(edge[1]), neiggbors);
                }
            }
        }
        return graph;
    }


}
