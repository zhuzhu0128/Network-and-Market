/**
 * Created by yingzhu on 11/4/17.
 */
import java.util.*;
import java.io.*;
public class Matchgraph {

    // function to reas preferred choice graph and turn into hash map
    public HashMap<Integer,List<Integer>> graphreader_choice(String inputFilePath) throws IOException{
        HashMap<Integer,List<Integer>> value = new HashMap<>();
        try(BufferedReader br = new BufferedReader(new FileReader (new File(inputFilePath)))){
            String line;
            while((line = br.readLine()) != null){
                // process the line to store the value for each player
                String[] str = line.split(" ");
                List<Integer> choice = new ArrayList<>();
                choice.add(Integer.valueOf(str[1]));
                value.put(Integer.valueOf(str[0])+100,choice);
            }
        }
        return value;
    }

    // function to read file and turn into hash map
    public HashMap<Integer,List<Integer>> graphreader(String inputFilePath) throws IOException{
        HashMap<Integer,List<Integer>> value = new HashMap<>();
        try(BufferedReader br = new BufferedReader(new FileReader (new File(inputFilePath)))){
            String line;
            while((line = br.readLine()) != null){
                // process the line to store the value for each player
                String[] str = line.split(" ");

                // store value in hashmap
                List<Integer> value_list = new ArrayList<Integer>();
                for(int i=1;i<str.length;i++){
                    value_list.add(Integer.valueOf(str[i]));
                }
                value.put(Integer.valueOf(str[0]),value_list);
            }
        }
        return value;
    }


    // function to build graph based on hashmap value
    public HashMap<Integer,List<Integer>> buildGraph (HashMap<Integer, List<Integer>> value){
        //convert the value graph we have to preferred choice graph, change the index of buyer such that we could run max flow function below
        HashMap<Integer,List<Integer>> graph = new HashMap<Integer,List<Integer>>();
        for(int key: value.keySet()){
            ArrayList<Integer> temp_value = new ArrayList<Integer>(value.get(key));
            Collections.sort(temp_value);
            int max = temp_value.get(temp_value.size()-1);
            for(int i=0;i<temp_value.size();i++){
                if(!graph.containsKey(key+100)){
                    graph.put(key+100,new ArrayList<Integer>());
                }
                if(value.get(key).get(i)==max){
                    graph.get(key+100).add(i);
                }
            }
        }
        return graph;
    }

    // function to update hashmap value after we found the contricted set
    public void updatevalue(HashMap<Integer, List<Integer>> value, HashMap<Integer,List<Integer>> graph, List<Integer> constrict, int[] price){
        List<Integer> adjust_item= new ArrayList<Integer>();
        for(int i=0;i <constrict.size();i++){
            int const_member = constrict.get(i);
            for(int j=0;j<graph.get(const_member+100).size();j++){
                int item_index=graph.get(const_member+100).get(j);
                if(!adjust_item.contains(item_index)){
                    adjust_item.add(item_index);
                    price[item_index]++;
                }
            }
        }
        for(int k=0;k<value.size();k++){
            for(int h=0;h<adjust_item.size();h++){
                int cur_value = value.get(k).get(adjust_item.get(h));
                value.get(k).set(adjust_item.get(h),cur_value-1);
            }
        }
    }


    // max flow function
    public HashMap<Integer,Integer> maxflow(HashMap<Integer, List<Integer>> graph, int[] buyer_visited){
        HashMap<Integer,Integer> match = new HashMap<Integer,Integer>();
        HashMap<Integer,List<Integer>> maxflowgraph  = new HashMap<>();
        HashMap<Integer,List<Integer>> graphCopy = copyMap(graph);
        int size = graphCopy.size();
        // add sink and source in the graphCopy
        graphCopy.put(1000, new ArrayList<>());
        graphCopy.put(-1000,new ArrayList<>());
        maxflowgraph.put(1000, new ArrayList<>());
        maxflowgraph.put(-1000, new ArrayList<>());

        for(int i=0;i<size;i++){
            maxflowgraph.put(i,new ArrayList<Integer>());
            maxflowgraph.put(100+i,new ArrayList<Integer>());
            graphCopy.get(1000).add(100+i);
            graphCopy.put(i,new ArrayList<Integer>());
            graphCopy.get(i).add(-1000);
        }
        // run dfs to find max flow on graphCopy
        while(dfs(graphCopy,maxflowgraph, 1000,-1000,new HashSet<>())){}
        for (int key:maxflowgraph.keySet()){
            if(maxflowgraph.get(key).size()!=0){
                if(maxflowgraph.get(key).get(0)>= 0 && maxflowgraph.get(key).get(0)<100){
                    match.put(maxflowgraph.get(key).get(0),key-100);
                    buyer_visited[key-100]=1;
                }
            }
        }
        return match;
    }

    // dfs function
    public boolean dfs(HashMap<Integer,List<Integer>> graphCopy, HashMap<Integer,List<Integer>> maxflowgraph, int source, int sink,Set<Integer> visited){
        if(source==sink) return true;
        if(visited.contains(source)) return false;

        visited.add(source);

        for(int neighbor: graphCopy.get(source)){
            if(dfs(graphCopy,maxflowgraph, neighbor,sink,visited)){
                Integer node1 = source;
                Integer node2 = neighbor;
                maxflowgraph.get(node2).remove(node1);
                maxflowgraph.get(node1).add(node2);

                graphCopy.get(node1).remove(node2);
                graphCopy.get(node2).add(node1);
                return true;
            }
        }
        return false;
    }
    // function to do deep cop of hashMap
    public static HashMap<Integer, List<Integer>> copyMap(HashMap<Integer, List<Integer>> original)
    {
        HashMap<Integer, List<Integer>> copy = new HashMap<Integer, List<Integer>>();
        for (Map.Entry<Integer, List<Integer>> entry : original.entrySet())
        {
            copy.put(entry.getKey(),

                    new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    // function to check whether is perfect matching or not, if not find the constricted set
    public List<Integer> findContrict(HashMap<Integer,Integer> match, HashMap<Integer, List<Integer>> graph,int[] buyer_visited){
        List<Integer> constrict = new ArrayList<Integer>();
        for(int i=0;i< buyer_visited.length;i++){
            if(buyer_visited[i]==0){
                constrict.add(i);
                for(int j=0;j< graph.get(i+100).size();j++){
                    int item = graph.get(i+100).get(j);
                    if(!constrict.contains(match.get(item))){
                        constrict.add(match.get(item));
                    }
                }
            }
        }
        return constrict;
    }

    // function to generate graph with given number of buyer and given number of items
    public HashMap<Integer, List<Integer>> generateGraph(int num1, int num2, int limit){
        HashMap<Integer,List<Integer>> value  = new HashMap<Integer,List<Integer>>();
        for(int i=0;i<num1;i++){
            value.put(i,new ArrayList<Integer>());
            for(int j=0;j<num2;j++){
                value.get(i).add((int)(Math.random()*limit+1));
            }
        }
        return value;
    }

    // function to generate graph for identical bundle
    public HashMap<Integer,List<Integer>> bundleGraph(int num1, int num2, int seed){
        HashMap<Integer,List<Integer>> map = new HashMap<>();
        Random generator = new Random();
        generator.setSeed(0);
        for(int i=0;i<num1;i++){
            int num = generator.nextInt(50)+1;
            map.put(i,new ArrayList<Integer>());
            for(int j=1;j<num2+1;j++){
                map.get(i).add(j*num);
            }
        }
        return map;
    }


}
