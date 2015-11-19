/*
 * @author Loc Truong
 */
package chemcam;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
public abstract class AgentRunnable implements Runnable{
    protected BlockingQueue queue = null;
    private AgentSocket runnableServerSocket;
    private AgentSocket runnableSocket;
    public AgentRunnable(int port, BlockingQueue queue) throws IOException{
        setRunnableServerSocket(port);
        this.queue = queue;
    }
    public AgentRunnable(int port, InetAddress host, BlockingQueue queue) throws UnknownHostException{
        setRunnableSocket(port, host);
        this.queue = queue;
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
