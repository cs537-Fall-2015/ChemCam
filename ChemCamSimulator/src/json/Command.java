/*
 * @author Loc Truong
 */
package json;
public class Command{
    int ind;
    String cmd;    
    public Command(int i, String command){
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
}
