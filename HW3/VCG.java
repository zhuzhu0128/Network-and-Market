/**
 * Created by yingzhu on 11/5/17.
 */
import java.util.*;
import java.io.*;
public class VCG {

    public static void main(String[] args){
        // generate graph: 20 buyers and 20 items with limit value of 50
       Matchgraph matchgraph = new Matchgraph();
        MatchingPrice matchprice = new MatchingPrice();
        VCG vcg = new VCG();

        // generate 20 buyers and 20 items graph
        HashMap<Integer,List<Integer>> VCG_value = matchgraph.bundleGraph(20,20,3);
        matchprice.helper(VCG_value);


        // VCG price for bundle in (a)
       List<Integer> vcg_price = vcg.vcgSingle(VCG_value);
        vcg.displayPrice(vcg_price);
        //repeat above with shuffling the values for each buyers
        System.out.println("PRICES FOR EACH BUYER ARE SHUFFLING");
       vcg.shuffleValues(VCG_value);
        matchprice.helper(VCG_value);

       List<Integer>vcg_price1 = vcg.vcgSingle(VCG_value);
        vcg.displayPrice(vcg_price1);

        }

    // function to vcg price for single item
    public List<Integer> vcgSingle(HashMap<Integer,List<Integer>> value){
        List<Integer> vcgprice = new ArrayList<>();
        VCG vcg = new VCG();
        HashMap<Integer, List<Integer>> VCG_valuecopy1 = vcg.copyMap(value);
        int[] price1 = new int[20];
        HashMap<Integer,Integer> match1  = vcg.socialOpt(VCG_valuecopy1,price1);
        List<Integer> VCGprice = new ArrayList<>();

        for(int i=0;i<20;i++){
            // welfare with buyer i
            int welfarewith = welfare(match1,value,i);

            // welfare without buyer i;
            HashMap<Integer, List<Integer>> VCG_valuecopy2 = vcg.copyMap(value);
            List<Integer> replace = new ArrayList<Integer>(Collections.nCopies(20, 0));
            VCG_valuecopy2.put(i,replace);
            HashMap<Integer, List<Integer>> value2 = vcg.copyMap(VCG_valuecopy2);
            int [] price2 = new int[20];
            HashMap<Integer,Integer>  match2  = vcg.socialOpt(VCG_valuecopy2,price2);
            int welfarewithout = welfare(match2,value2,i);
            vcgprice.add(welfarewithout-welfarewith);
        }
        for(int i=0;i<vcgprice.size();i++){
            VCGprice.add(vcgprice.get(match1.get(i)));
        }

        return VCGprice;

    }

    // function to generate social optimal allocation using function from previous question
    public HashMap<Integer,Integer> socialOpt(HashMap<Integer,List<Integer>> value,int[] price){
        Matchgraph match_graph = new Matchgraph();
        MatchingPrice match_val = new MatchingPrice();
        HashMap<Integer,Integer> match = new HashMap<>();
        //System.out.println("The following is the value graph we generate");
        //match_val.helper(value);
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

    // function to shuffle the value of items for each buyers
    public void shuffleValues(HashMap<Integer,List<Integer>> value){
        for(int i=0;i<value.size();i++){
            Collections.shuffle(value.get(i));
        }
    }

    // function to generate VCG price
    public List<Integer> vcgPricing (HashMap<Integer,Integer> match, HashMap<Integer,List<Integer>> VCG_value){
        List<Integer> vcgprice = new ArrayList<Integer>();
        for(int i=match.size()-1;i >0;i--){
            int cur_price =0;
            for(int j=i-1;j>=0;j--){
                int cur_buyer = match.get(j);
                int cur_value = VCG_value.get(cur_buyer).get(0);
                cur_price+=cur_value*(i-j);
            }
            vcgprice.add(cur_price);
        }
        vcgprice.add(0);
        return vcgprice;
    }

    // function to display VCG price
    public void displayPrice(List<Integer> vcgprice){
        System.out.println("VCG price ");
        for(int i=0;i<vcgprice.size();i++){
            System.out.print(vcgprice.get(i) + " ");
        }
        System.out.println();
    }

    // function to do deep cop of hashMap
    public static HashMap<Integer, List<Integer>> copyMap(HashMap<Integer, List<Integer>> original)
    {
        HashMap<Integer, List<Integer>> copy = new HashMap<Integer, List<Integer>>();
        for (Map.Entry<Integer, List<Integer>> entry : original.entrySet())
        {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    // function calculate welfare with one buyer absent
    public int welfare(HashMap<Integer,Integer> match, HashMap<Integer,List<Integer>> value, int buyer){
        int welfare =0;
        for(int i=0;i<match.size();i++){
            int cur_buyer = match.get(i);
            if(cur_buyer!=buyer){
                welfare+=value.get(cur_buyer).get(i);
            }
        }
        return welfare;
    }
}
