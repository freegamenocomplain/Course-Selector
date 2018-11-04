/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
import java.io.*;
import java.util.*;

public class bruteForceSearch {
   static int [][] duplicates;
   static Stack<String>[]days = new Stack[10];
   static ArrayList<String>clubs;
   static int best;
   static int sum;
   static int [] clubDays;
   static int n;
   public static void calculateClub () {

       //math, physics, art
       //10,7,15
       //math + physics = 3, art + math = 4, art + physics = 1;
       //clubs = new ArrayList<>();
       sum = 0;
       duplicates = arrayGenerator(clubList());

       /*
       n = 6;
       clubs.add("compisci");
       clubs.add("math");
       clubs.add("physics");
       clubs.add("art");
       clubs.add("engineering");
       clubs.add("literature");
       sum = 446;
       duplicates = new int[6][6];
       duplicates[0][1] = 63;
       duplicates[0][2] = 38;
       duplicates[0][3] = 23;
       duplicates[0][4] = 23;
       duplicates[0][5] = 27;
       duplicates[1][2] = 38;
       duplicates[1][3] = 23;
       duplicates[1][4] = 23;
       duplicates[1][5] = 27;
       duplicates[2][3] = 25;
       duplicates[2][4] = 27;
       duplicates[2][5] = 32;
       duplicates[3][4] = 25;
       duplicates[3][5] = 25;
       duplicates[4][5] = 27;
       */
       for(int i=0; i<n; i++) {
           for(int j=0; j<n; j++) {
               System.out.print(duplicates[i][j]+" ");
           }
           System.out.println();
       }
       //clubs.add("Math Club");
       //clubs.add("Physics Club");
       //clubs.add("Art Club");
       //duplicates[0][1] = 3;
       //duplicates[1][0] = 3;
       //duplicates[1][2] = 1;
       //duplicates[2][1] = 1;
       //duplicates[0][2] = 4;
       //duplicates[2][0] = 4;
       days = new Stack[10];
       clubDays = new int[n];
       best = 0;
       for(int i=0; i<10; i++) {
           days[i] = new Stack<>();
       }
       days[0].push(clubs.get(0));
       clubDays[0] = 0;
       search(0,clubs.get(0),0);
           //days[i].pop();
       String [] dates = {"Monday","Tuesday","Wednesday","Thursday","Friday"};
       for(int i=5; i<10; i++) {
           String [] arr = new String[days[i].size()];
           int index = days[i].size()-1;
           //System.out.println(index);
           while(!days[i].isEmpty()) {
               arr[index] = days[i].pop();
               index --;
           }
           System.out.println(dates[i-5]+": ");
           for(int j=0; j<arr.length; j++) {
               System.out.println(arr[j]);
           }
       }
       System.out.println("Best amoun satisfied: "+best);
       System.out.println("Total amount of duplicates: "+sum);
   }
   public static void search (int index, String club, int dayNum) {
       //get next element in hash map
       if(best == sum) {
           return;
       }
       if(index >= clubs.size()-1) {
           int satisfied = satisfiedCount();
           String [] dates = {"Monday","Tuesday","Wednesday","Thursday","Friday"};
           if(satisfied>best) {
               best = satisfied;
               //System.out.println(best);
               for(int i=0; i<5; i++) {
                   days[i + 5] = (Stack<String>) days[i].clone();
                   //System.out.println(days[i + 5].size());
               }
           }
           days[dayNum].pop();
           return;
       }
       String next = clubs.get(index+1);
       //System.out.println(next);
       for(int i=0; i<5; i++) {
           days[i].push(next);
           clubDays[index+1] = i;
           search(index+1,next,i);
       }
       days[dayNum].pop();

   }
   public static int satisfiedCount () {
       int counter = 0;
       for(int i=0; i<n; i++) {
           for(int j=0; j<n; j++) {
               String club1 = clubs.get(i);
               String club2 = clubs.get(j);

               if(clubDays[i]!= clubDays[j]) counter += duplicates[i][j];
           }
       }
       return counter;
   }
   public static ArrayList<String> clubList() {
       String[]club = {"Compsci","Math","Physics", "Art", "Engineering", "DECA", "Debate", "RASA", "Greenhouse", "Chess", "Robotics", "Badminton", "Anime", "HOSA", "E-Sports"};
       clubs = new ArrayList<>();
       n = (int)(Math.random()*5)+6;
       for(int i=0; i<n; i++) {
           clubs.add(club[i]);
       }
       return clubs;
   }
   public static int[][] arrayGenerator(ArrayList<String>arr) {
       int [][] dupe = new int[n][n];
       for(int i=0; i<n; i++) {
           for(int j=i+1; j<n; j++) {
               int x = (int)(Math.random()*10);
               dupe[i][j] = x;
               sum+= x;
           }
       }
       return dupe;
   }
}
