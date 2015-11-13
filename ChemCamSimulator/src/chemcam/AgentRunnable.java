/*
 * @author truol014
 */
package chemcam;
import java.io.*;
import java.net.*;
public abstract class AgentRunnable implements Runnable{
    private AgentSocket runnableServerSocket;
    private AgentSocket runnableSocket;
    public AgentRunnable(int port) throws IOException{
        setRunnableServerSocket(port);
    }
    public AgentRunnable(int port, InetAddress host) throws UnknownHostException{
        setRunnableSocket(port, host);
    }
    public AgentSocket getRunnableServerSocket(){
        return runnableServerSocket;
    }
    private void setRunnableServerSocket(int port) throws IOException{
        this.runnableServerSocket = new AgentSocket(port);
    }
    public AgentSocket getRunnableSocket(){
        return runnableSocket;
    }
    private void setRunnableSocket(int port, InetAddress host) throws UnknownHostException{		
        this.runnableSocket = new AgentSocket(port, host);
    }
    public void closeAllRunnable() throws IOException{
        if(runnableServerSocket != null)
            runnableServerSocket.closeAll();
        if(runnableSocket != null)
            runnableSocket.closeAll();
    }
}
