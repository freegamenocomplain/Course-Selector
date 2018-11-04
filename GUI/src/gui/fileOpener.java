/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
import java.util.Scanner;
import java.io.File;
import javax.swing.JFileChooser;
/**
 *
 * @author Eric Wang
 */
public class fileOpener {
    JFileChooser fileChooser = new JFileChooser();
    public void openFile()throws Exception{
        if(fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
            java.io.File file = fileChooser.getSelectedFile();
            Scanner input = new Scanner(file);
            //put input code here
            input.close();
        } else {
            
        }
    }
    
}
