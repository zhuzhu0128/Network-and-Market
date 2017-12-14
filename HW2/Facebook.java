import java.util.*;
import java.io.*;
public class Facebook {
    public static void main(String[] args){
        int[][] facebook = new int[4039][4039];
        try{
            Scanner input = new Scanner(new FileReader("/Users/yingzhu/Desktop/facebook_combined.txt"));
            while(input.hasNextLine()){
                String line = input.nextLine();
                String[] tempdata = line.trim().split("\\s+");
                facebook[Integer.parseInt(tempdata[0])][Integer.parseInt(tempdata[1])] =1;
                facebook[Integer.parseInt(tempdata[1])][Integer.parseInt(tempdata[0])] =1;
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Random generator1 = new Random();
        Random generator2 = new Random();
        int total = 0;
        for(int i=0;i<1000;i++){
            int nodeA = generator1.nextInt(4039);
            int nodeB = generator2.nextInt(4039);
            if(nodeA==nodeB) {
                nodeB = generator2.nextInt(4039);
            }
            int[][] facebook_copy = new int[4039][4039];
            for(int j=0;j<4039;j++){
                for(int k=0;k<4039;k++){
                    facebook_copy[j][k]=facebook[j][k];
                }
            }
            Graph G = new Graph();
            int temp_res = -1;
            while(temp_res!=0){
                int[] visited = new int[4039];
                temp_res = G.maxFlow(facebook_copy,nodeA, nodeB,visited);
                total+=temp_res;
            }
        }
        System.out.println(total/1000.0);

    }
}
