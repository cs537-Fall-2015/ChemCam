/*
 * @author Loc Truong
 */
package chemcam;
public class AgentStatus {
    Status status;
    public AgentStatus(){
        status = Status.OFF;
    }
    public Status getStatus(){
        return status;
    }
    public void setStatus(Status s){
        status = s;
    }
}
