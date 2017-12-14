/**
 * Created by yingzhu on 11/5/17.
 */
import java.util.*;
import java.io.*;
public class GSP {
    public static void main(String[] args){
        // generate graph: 20 buyers and 20 items with limit value of 50
        Matchgraph matchgraph = new Matchgraph();
        MatchingPrice matchprice = new MatchingPrice();
        GSP gsp = new GSP();
        // generate 20 buyers and 20 items graph
        HashMap<Integer,List<Integer>> GSP_value = matchgraph.bundleGraph(20,20,3);
        matchprice.helper(GSP_value);
        int[] price = new int[20];
        HashMap<Integer,Integer> match = gsp.socialOpt(GSP_value,price);
        List<Integer> gsp_price = gsp.gspPricing(match,GSP_value);
        gsp.displayPrice(gsp_price);


    }

    // GSP pricing
    public List<Integer> gspPricing(HashMap<Integer,Integer> match, HashMap<Integer,List<Integer>> GSP_value){
        List<Integer> gspprice = new ArrayList<Integer>();
        List<Integer> sortval = sortValue(GSP_value);
        for(int i=match.size();i>0;i--){
            if(i==1) gspprice.add(i*sortval.get(i-1));
            else gspprice.add(i*sortval.get(i-2));
        }
        return gspprice;
    }

    // function return ordered price from buyer for each identical item
    private List<Integer> sortValue(HashMap<Integer,List<Integer>> value){
        List<Integer> sortvalue = new ArrayList<>();
        for(int i=0;i<value.size();i++){
            sortvalue.add(value.get(i).get(0));
        }
        Collections.sort(sortvalue);
        return sortvalue;
    }

    // function to achieve social optimal allocation
    public HashMap<Integer,Integer> socialOpt(HashMap<Integer,List<Integer>> value, int[] price){
        Matchgraph match_graph = new Matchgraph();
        MatchingPrice match_val = new MatchingPrice();
        HashMap<Integer,Integer> match = new HashMap<>();
        HashMap<Integer,List<Integer>> graph = match_graph.buildGraph(value);
        int[] buyer_visit = new int[value.size()];
        match = match_graph.maxflow(graph,buyer_visit);
        List<Integer> constrict1 = match_graph.findContrict(match,graph,buyer_visit);
        while(constrict1.size()!=0){
            match_graph.updatevalue(value,graph,constrict1,price);
            graph = match_graph.buildGraph(value);
            buyer_visit = new int[value.size()];
            match = match_graph.maxflow(graph,buyer_visit);
            constrict1 = match_graph.findContrict(match,graph,buyer_visit);
        }
        return match;
    }

    // function to display VCG price
    public void displayPrice(List<Integer> gspprice){
        System.out.println("GSP price are : ");
        for(int i=gspprice.size()-1;i>=0;i--){
            System.out.print(gspprice.get(i) + " ");
        }
        System.out.println();
    }

}
