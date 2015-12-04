/*
 * @author Loc Truong
 */
package chemcam.json;
public class CommandObject{
    private final int ind;
    private final String cmd;    
    public CommandObject(int i, String command){
        this.ind = i;
        this.cmd = command;        
    }
    public int getIndex(){
        return ind;
    }
    @Override
    public String toString(){
        return String.format("%s", cmd);
    }
    public boolean isAbort(){
        return cmd.equals("ABORT");
    }
}
