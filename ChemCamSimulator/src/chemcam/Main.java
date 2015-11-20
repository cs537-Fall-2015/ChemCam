/*
 * @author Loc Truong
 */
package chemcam;
import javax.swing.*;
public class Main {
     public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                try{
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
                catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
                    Utils.log(exception.toString());
                }
                JFrame frame = MainFrame.getInstance();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
    
}
