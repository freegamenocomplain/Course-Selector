/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
import java.util.*;
/**
 *
 * @author Eric Wang
 */
public class TestCaseGenerator {
    public static void main(String[]args){
        String  [] courses = {"English","Physics","French","Computer Science"};
        for(int i = 0; i<100;i++){
            System.out.print("Student "+i+" ");
            int english = 0;
            int [] used = {-1,-1,-1,-1};
            for(int j = 0; j<4;j++){
                
                boolean valid = false;
                int random = 0;
                while(valid == false){
                    valid = true;
                    random = (int)(Math.random()*4);
                    //System.out.println(random);
                    for(int k = 0;k<4;k++){
                        if(used[k]==random){
                            valid = false;
                        }
                    }
                }
                System.out.print(courses[random]+" ");
                if (courses[random].equals("English")){
                    english = j;
                }
                used[j] = random;
            }
            int smth = 3;
            for(int j = 0;j<4;j++){
                if (j == english){
                    System.out.print("100000 ");
                    
                } else{
                System.out.print(smth+" ");
                smth--;
            }
            }
            System.out.println();
        }
    }
}
