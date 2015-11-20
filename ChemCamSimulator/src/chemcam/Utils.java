/*
 * @author Loc Truong
 */
package chemcam;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
public class Utils{
    private static Utils instance = null;
    private JTextArea textArea = null;
    private Utils(){
    }
    public static Utils getInstance(){
        if (instance == null){
            instance = new Utils();
        }
        return instance;
    }
    public void setTextArea(JTextArea textArea){
        this.textArea = textArea;
    }
    public void logMsg(String msg){
        System.out.println(msg);
        Logger.getLogger("ChemCam Simulator").log(Level.INFO, msg);
        if (textArea != null) {
            final String output = msg;
            // TODO: I hope this doesn't lock up the SwingUtilities... Bah.
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Date d = new Date();
                    textArea.append(d.toString() + " " + output + "\n");
                }
            });
        }
    }
    public static void log(String msg){
        Utils.getInstance().logMsg(msg);
    }
}
