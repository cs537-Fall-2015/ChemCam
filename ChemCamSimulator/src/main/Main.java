/*
 * @author Loc Truong
 */
package main;
import javax.swing.*;
public class Main {
     public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                try{
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    Utils.log(ex.toString());
                }
                JFrame frame = MainFrame.getInstance();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);   
                try{
                    MainFrame.getAgentPanel().getAgentListenThread().start();
                    MainFrame.getControllerPanel().getControllerListenThread().start();
                }
                catch(Exception e){
                    Utils.log("Exception Starting Server Threads: " + e);
                }
            }
        });
    }
    
}
