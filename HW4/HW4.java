import java.util.HashMap;
import java.util.List;
import java.io.*;
import java.util.*;


public class HW4{

	public static class DirectedGraph{
		HashMap<Integer,List<Integer>> map;

		DirectedGraph(int numberOfNodes){
			map = new HashMap<>();
			for(int i=0;i<numberOfNodes;i++){
				map.put(i, new ArrayList<>());
			}
		}
		
		void addEdge(int origin, int destination){
			if(!checkEdge(origin,destination)){
				map.get(origin).add(destination);
			}
		}
		
        // This method should return true is there is an edge between origin and destination
		boolean checkEdge(int origin, int destination){
			List<Integer> temp_list = map.get(origin);
			for(int i=0;i<temp_list.size();i++){
				if(temp_list.get(i)==destination){
					return true;
				}
			}
			return false;
		}
		
		// This method should return a list of all the nodes u such that the edge (origin_node,u) is
        // part of the graph.
		List<Integer> edgesFrom(int origin){
			return map.get(origin);
		}
		
        // This method should return the number of nodes in the graph
		int numberOfNodes(){
			return map.size();
		}

		// This method will build graph according to txt file
		void addEdge_list(String path){
			try{
				Scanner input = new Scanner(new FileReader(path));
				while(input.hasNextLine()){
					String line = input.nextLine();
					String[] tempdata = line.trim().split("\\s+");
					addEdge(Integer.valueOf(tempdata[0]),Integer.valueOf(tempdata[1]));
				}
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
		}
	}
	

	//	This method, given a directed graph, should run the epsilon-scaled page-rank
	//	algorithm for num-iter iterations and return an array where position i contains the weight of the corresponsing node. 
	//	In the case of 0 iterations, all nodes should have weight 1/number_of_nodes 
	public static double[] scaledPageRank(DirectedGraph g, int num_iter, double eps){
		int numberOfNodes = g.numberOfNodes();
		double[] cur_res = new double[numberOfNodes];
		double[] res = new double[numberOfNodes];
		Arrays.fill(cur_res, 1.0/numberOfNodes);
		for(int i=0;i<num_iter;i++){
			res = new double[numberOfNodes];
			for(int j=0;j<numberOfNodes;j++){
				//double temp_score = 0;
				for(int cur: g.edgesFrom(j)){
					res[cur] += cur_res[j]/g.edgesFrom(j).size();
				}
			}
			for (int k =0;k<numberOfNodes;k++){
				res[k] = eps/numberOfNodes +(1-eps)*res[k];
			}
			cur_res = res;
		}
		return res;
	}
	
	// This method, should construct and return a DirectedGraph encoding the left example in fig 15.1
    // Use the following indexes: A:0, B:1, C:2, Z:3 
	public static DirectedGraph graph15_1Left(){
		DirectedGraph left_151 = new DirectedGraph(4);
		left_151.addEdge(0,1);
		left_151.addEdge(1,2);
		left_151.addEdge(2,0);
		left_151.addEdge(0,3);
		left_151.addEdge(3,3);
		return left_151;
	}

	// This method, should construct and return a DirectedGraph encoding the right example in fig 15.1
    // Use the following indexes: A:0, B:1, C:2, Z1:3, Z2:4
	public static DirectedGraph graph15_1Right(){
		DirectedGraph right_151 = new DirectedGraph(5);
		right_151.addEdge(0,1);
		right_151.addEdge(1,2);
		right_151.addEdge(2,0);
		right_151.addEdge(0,3);
		right_151.addEdge(0,4);
		right_151.addEdge(3,4);
		right_151.addEdge(4,3);
		return right_151;
	}

	
	// This method, should construct and return a DirectedGraph encoding example 15.2
    // Use the following indexes: A:0, B:1, C:2, A':3, B':4, C':5
	public static DirectedGraph graph15_2(){
		DirectedGraph right_152 = new DirectedGraph(6);
		right_152.addEdge(0,1);
		right_152.addEdge(1,2);
		right_152.addEdge(2,0);
		right_152.addEdge(5,3);
		right_152.addEdge(3,4);
		right_152.addEdge(4,5);
		return right_152;
	}

	// This method, should construct and return a DirectedGraph of your choice with at least 10 nodes
	public static DirectedGraph extraGraph1(){
		DirectedGraph extra1 = new DirectedGraph(78);
		extra1.addEdge_list("/Users/yingzhu/Desktop/extra_graph_1.txt");
		return extra1;
	}
	
	// This array should contains the expected weights of each node when running the scaled page rank on the extraGraph1 output
	// with epsilon = 0.07 and num_iter = 20. 
	public static double[] extraGraph1Weights = scaledPageRank(extraGraph1(),20, 0.07);


	// This method, should construct and return a DirectedGraph of your choice with at least 10 nodes
	public static DirectedGraph extraGraph2(){
		DirectedGraph extra2 = new DirectedGraph(53);
		extra2.addEdge_list("/Users/yingzhu/Desktop/extra_graph_2.txt");
		return extra2;
	}
	
	// This array should contains the expected weights of each node when running the scaled page rank on the extraGraph2 output
	// with epsilon = 0.07 and num_iter = 20. 
	public static double[] extraGraph2Weights = scaledPageRank(extraGraph2(),20, 0.07);

	
	// This method should return a DIRECTED version of the facebook graph as an instance of the DirectedGraph class.
    // In particular, if u and v are friends, there should be an edge between u and v and an edge between v and u.
	public static DirectedGraph facebookGraph(){
		DirectedGraph facebook = new DirectedGraph(4039);
		try {
			Scanner input = new Scanner(new FileReader("/Users/yingzhu/Desktop/facebook_combined.txt"));
			while (input.hasNextLine()) {
				String line = input.nextLine();
				String[] tempdata = line.trim().split("\\s+");
				facebook.map.get(Integer.valueOf(tempdata[0])).add(Integer.valueOf(tempdata[1]));
				facebook.map.get(Integer.valueOf(tempdata[1])).add(Integer.valueOf(tempdata[0]));
			}
		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return facebook;
	}
	

	// All the code necessary for measurements for question 8b should go in the main function.
	public static void main(String[] args) throws Throwable{

		/**************** Result Code for Question 7 ***************/
		// test for graph151_left
		DirectedGraph graph151_left = graph15_1Left();
		double[] res = scaledPageRank(graph151_left,20, 1/7.0);
		System.out.println("Output for graph 151 left");
		for(int i=0;i<res.length;i++){
			System.out.println(res[i]);
		}
		System.out.println("");

		// test for graph151_right
		DirectedGraph graph151_right = graph15_1Right();
		double[] res1 = scaledPageRank(graph151_right,20, 1/7.0);
		System.out.println("Output for graph 151 right");
		for(int i=0;i<res1.length;i++){
			System.out.println(res1[i]);
		}
		System.out.println("");

		// test for graph 152
		DirectedGraph graph152 = graph15_2();
		double[] res2 = scaledPageRank(graph152,20, 1/7.0);
		System.out.println("Output for graph 152");
		for(int i=0;i<res2.length;i++){
			System.out.println(res2[i]);
		}
		System.out.println("");

		// test for extra graph1
		System.out.println("Output for extra graph 1");
		for(int i=0;i< extraGraph1Weights.length;i++){
			System.out.println(i + ":  " + extraGraph1Weights[i]);
		}
		System.out.println("");

		// test for extra graph2
		System.out.println("Output for extra graph 2");
		for(int i=0;i< extraGraph2Weights.length;i++){
			System.out.println(i + ":  " + extraGraph2Weights[i]);
		}
		System.out.println("");


		/**************** Result Code for Question 8 FaceBook Data *****************************/
		// test for facebook data
		DirectedGraph facebook = facebookGraph();
		double[] facebook_res = scaledPageRank(facebook,15, 1/7.0);
		Integer[] facebook_rank = new Integer[facebook_res.length];
		for (int i = 0; i < facebook_rank.length; i++) {
			facebook_rank[i] = i;
		}
		Arrays.sort(facebook_rank, new Comparator<Integer>() {
			@Override
			public int compare(Integer i1, Integer i2) {
				if (facebook_res[i1] > facebook_res[i2]) {
					return 1;
				} else {
					return -1;
				}
			}
		});

		// for loop to display the  most common score in facebook data
		HashMap<Double,Integer> score_counting = new HashMap<>();
		for(int k = 0;k < facebook_res.length; k++){
			score_counting.put(facebook_res[k],score_counting.getOrDefault(facebook_res[k],0)+1);
		}
		int max_count = 0;
		double common_score = 0.0;
		for(int h = 0;h < facebook_res.length; h++){
			if(score_counting.get(facebook_res[h])> max_count){
				max_count = score_counting.get(facebook_res[h]);
				common_score = facebook_res[h];
			}
		}
		System.out.println("The most common score is : " + common_score);
		System.out.println("");

		// for loop display 10 highest score nodes and 10 lowest score nodes in facebook graph
		for (int i = 0; i < 10; i++) {
			System.out.println("Lowest 10s: " + facebook_rank[i] + ", value: " + facebook_res[facebook_rank[i]] +
					", Highest 10s: " + facebook_rank[facebook_rank.length - 10 + i] +
					", value: " + facebook_res[facebook_rank[facebook_rank.length - 10 + i]]);
		}
		System.out.println("");
		// for loop to display the number of friends that top 10 nodes have and that of bottom 10 nodes
		for (int j = 0;j < 10; j++){
			System.out.println("Lowest 10s: " + facebook_rank[j] + ", number of friends : " + facebook.map.get(facebook_rank[j]).size()+
			                   ",  Highest 10s: " + facebook_rank[facebook_rank.length-10+j] +
					           ", number of firends : " + facebook.map.get(facebook_rank[facebook_rank.length-10+j]).size());
		}


	}
}
