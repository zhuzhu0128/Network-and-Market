/**
 * Created by yingzhu on 11/4/17.
 */
import java.util.*;
import java.io.*;
public class MatchingPrice {
    public static void main (String[] args) throws IOException {
        //partA();
        partB();
    }
    public static void partA()  throws IOException {
        Matchgraph match_graph = new Matchgraph();
        MatchingPrice match_val = new MatchingPrice();

        //Part I: give preferred
        //(a) 7.3 example
        try {
            HashMap<Integer, List<Integer>> graph = match_graph.graphreader_choice("/Users/yingzhu/Desktop/73_prefered.txt");
            match_val.helper5(graph);
            int[] buyer_visit = new int[graph.size()];
            HashMap<Integer,Integer> match = match_graph.maxflow(graph,buyer_visit);
            List<Integer> constrict = match_graph.findContrict(match,graph,buyer_visit);
            if(constrict.size()!=0){
                System.out.println("constricted set");
                match_val.helper3(constrict);
            }else{
                System.out.println("perfect match");
                match_val.helper2(match);
            }
        } catch(IOException io){
            System.out.println("io exception");
        }

        // (b) test case 1:  10 nodes graph
        try {
            HashMap<Integer, List<Integer>> graph1 = match_graph.graphreader_choice("/Users/yingzhu/Desktop/10node_preferred.txt");
            match_val.helper5(graph1);
            int[] buyer_visit1 = new int[graph1.size()];
            HashMap<Integer,Integer> match = match_graph.maxflow(graph1,buyer_visit1);
            List<Integer> constrict = match_graph.findContrict(match,graph1,buyer_visit1);
            if(constrict.size()!=0){
                System.out.println("constricted set");
                match_val.helper3(constrict);
            }else{
                System.out.println("perfect match");
                match_val.helper2(match);
            }
        } catch(IOException io){
            System.out.println("io exception");
        }
        // (c) test case 2: 15 nodes graph
        try {
            HashMap<Integer, List<Integer>> graph2 = match_graph.graphreader_choice("/Users/yingzhu/Desktop/15node_preferred.txt");
            match_val.helper5(graph2);
            int[] buyer_visit2 = new int[graph2.size()];
            HashMap<Integer,Integer> match = match_graph.maxflow(graph2,buyer_visit2);
            List<Integer> constrict = match_graph.findContrict(match,graph2,buyer_visit2);
            if(constrict.size()!=0){
                System.out.println("constricted set");
                match_val.helper3(constrict);
            }else{
                System.out.println("perfect match");
                match_val.helper2(match);
            }
        } catch(IOException io){
            System.out.println("io exception");
        }
        // (d) test case 3: 20 nodes graph
        try {
            HashMap<Integer, List<Integer>> graph3 = match_graph.graphreader_choice("/Users/yingzhu/Desktop/20node_preferred.txt");
            match_val.helper5(graph3);
            int[] buyer_visit3 = new int[graph3.size()];
            HashMap<Integer,Integer> match = match_graph.maxflow(graph3,buyer_visit3);
            List<Integer> constrict = match_graph.findContrict(match,graph3,buyer_visit3);
            if(constrict.size()!=0){
                System.out.println("constricted set");
                match_val.helper3(constrict);
            }else{
                System.out.println("perfect match");
                match_val.helper2(match);
            }
        } catch(IOException io){
            System.out.println("io exception");
        }
    }
    public static void partB()  throws IOException {
        Matchgraph match_graph = new Matchgraph();
        MatchingPrice match_val = new MatchingPrice();

        // Part II test case : Figure 7.3
        try {
            HashMap<Integer,List<Integer>> value = match_graph.graphreader("/Users/yingzhu/Desktop/73.txt");
            HashMap<Integer,List<Integer>> graph = match_graph.buildGraph(value);
            match_val.helper(graph);
            int[] buyer_visit = new int[value.size()];
            HashMap<Integer,Integer> match = match_graph.maxflow(graph,buyer_visit);
            match_val.helper2(match);
            List<Integer> constrict = match_graph.findContrict(match,graph,buyer_visit);
            //match_val.helper3(constrict);
            int[] price = new int[value.size()];
            while(constrict.size()!=0){
                match_graph.updatevalue(value,graph,constrict,price);
                graph = match_graph.buildGraph(value);
                buyer_visit = new int[value.size()];
                match = match_graph.maxflow(graph,buyer_visit);
                constrict = match_graph.findContrict(match,graph,buyer_visit);
                match_val.helper3(constrict);
                match_val.helper(value);
                match_val.helper2(match);
                match_val.helper4(price);

            }

        }catch( IOException io ) {
            System.out.println("IO exception");
        }


        // test case 1: random generate graph with 10 nodes and 10 items with value limit 10 and display result of perfect match
       match_val. testCase(10,10,20);

        // test case 2: random generate graph with 15 nodes and 15 items with value limit 10 and display result of perfect match
        match_val. testCase(15,15,10);

        // test case 3: random generate graph with 20 nodes and 20 items with value limit 15 and display result of perfect match
       match_val.testCase(20,20,15);
    }


    // helper function to run different test case with different number of buyers and items

    public void testCase(int num1, int num2, int limit){
        Matchgraph match_graph = new Matchgraph();
        MatchingPrice match_val = new MatchingPrice();
        System.out.println("Test case : " +num1 +" buyers and " +num2 +" items" +"with value limit" + limit);
        HashMap<Integer,List<Integer>> value1 = match_graph.generateGraph(num1,num2,limit);
        System.out.println("The following is the value graph we generate");
        match_val.helper(value1);
        HashMap<Integer,List<Integer>> graph1 = match_graph.buildGraph(value1);
        System.out.println("The following is the preferred graph");
        match_val.helper(graph1);
        int[] buyer_visit1 = new int[value1.size()];
        HashMap<Integer,Integer> match1 = match_graph.maxflow(graph1,buyer_visit1);
        List<Integer> constrict1 = match_graph.findContrict(match1,graph1,buyer_visit1);
        int[] price1 = new int[value1.size()];
        int checjcnt =0;
        while(constrict1.size()!=0){
            match_graph.updatevalue(value1,graph1,constrict1,price1);
            graph1 = match_graph.buildGraph(value1);
            buyer_visit1 = new int[value1.size()];
            match1 = match_graph.maxflow(graph1,buyer_visit1);
            constrict1 = match_graph.findContrict(match1,graph1,buyer_visit1);
        }
        System.out.println("Final value for 10 buyers are: ");
        match_val.helper(value1);
        System.out.println("Perfect Match");
        match_val.helper2(match1);
        System.out.println("Associated price");
        match_val.helper4(price1);
    }

    // helper function to display output to test
    public void helper(HashMap<Integer,List<Integer>> map){
        int[] value_pro;
        for(int i: map.keySet()){
            value_pro = new int[map.get(i).size()];
            for(int j=0;j<map.get(i).size();j++){
                value_pro[j]=map.get(i).get(j);
            }
            String value_profile = Arrays.toString(value_pro);
            System.out.println("The value for buyer" + i + value_profile);
        }
    }

    public void helper2(HashMap<Integer,Integer> map){
        for(int i:map.keySet()){
            System.out.println("item "+ i + "--->" + "buyer "+ map.get(i));
        }
    }

    public void helper3(List<Integer> constrict){
        for(int i=0;i<constrict.size();i++){
            System.out.print(constrict.get(i) +" ");
        }
        System.out.println("");
    }

    public void helper4(int[] price){
       for(int i=0;i<price.length;i++){
           System.out.print(price[i]+" ");
       }
        System.out.println();
    }
    public void helper5(HashMap<Integer,List<Integer>> map){
        int[] value_pro;
        for(int i: map.keySet()){
            value_pro = new int[map.get(i).size()];
            for(int j=0;j<map.get(i).size();j++){
                value_pro[j]=map.get(i).get(j);
            }
            String value_profile = Arrays.toString(value_pro);
            System.out.println("The preferred choice for buyer" + (i-100) + value_profile);
        }
    }

}
