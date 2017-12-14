/**
 * Created by yingzhu on 10/14/17.
 */

import java.util.*;
import java.io.*;
public class Graph {
    public static void main(String[] args) {
        /* Calculate max flow for Figure 5.1*/
        int[][] fig51 = new int[4][4];
        try {
            Scanner input = new Scanner(new FileReader("/Users/yingzhu/Desktop/fig51.txt"));
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] tempdata = line.trim().split("\\s+");
                fig51[Integer.parseInt(tempdata[0])][Integer.parseInt(tempdata[1])] += 1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Graph G = new Graph();
        int[] visited = new int[4];
        int temp_res = -1;
        int res = 0;
        while (temp_res != 0) {
            temp_res = G.maxFlow(fig51, 0, 2, visited);
            res += temp_res;
        }
        System.out.println("The max flow for figure 5.1 is " + res);

        /*Calculate max flow for Figure 5.3 (# of node: 12)*/
        int[][] fig53 = new int[12][12];
        try {
            Scanner input = new Scanner(new FileReader("/Users/yingzhu/Desktop/fig53.txt"));
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] tempdata = line.trim().split("\\s+");
                fig53[Integer.parseInt(tempdata[0])][Integer.parseInt(tempdata[1])] += 1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Graph G1 = new Graph();
        int temp_res1 = -1;
        int res1 = 0;
        while (temp_res1 != 0) {
            int[] visited1 = new int[12];
            temp_res1 = G1.maxFlow(fig53, 0, 11, visited1);
            res1 += temp_res1;
        }
        System.out.println("The max flow for figure 5.3 is " + res1);
    }

    public int maxFlow(int[][] cap, int s, int t,int[] visited){
        if(s==t){
            return 1;
        }
        visited[s]=1;
        for(int i=0;i<cap.length;i++){
            if(cap[s][i]>0 && visited[i]!=1){
              int temp = maxFlow(cap,i,t,visited);
              if(temp!=0){
                  cap[s][i] -=temp;
                  cap[i][s] +=temp;
                  return temp;
              }
            }
        }
        return 0;
    }
}
