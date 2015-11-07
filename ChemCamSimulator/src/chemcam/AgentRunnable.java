package chemcam;
import java.io.*;
public abstract class AgentRunnable implements Runnable{
    private AgentSocket agentSocket;
    public AgentRunnable(int port) throws IOException{
        setAgentSocket(port);
    }
    public void closeAll() throws IOException{
        if(agentSocket != null)
            agentSocket.closeAll();
    }
    public AgentSocket getAgentSocket() {
        return agentSocket;
    }
    private void setAgentSocket(int port) throws IOException {
        this.agentSocket = new AgentSocket(port);
    }
}
