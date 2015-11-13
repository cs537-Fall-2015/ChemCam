/*
 * @author truol014
 */
package chemcam;
import java.io.*;
import java.net.*;
public abstract class ControllerRunnable implements Runnable{
    private ControllerSocket runnableServerSocket;
    private ControllerSocket runnableSocket;
    public ControllerRunnable(int port) throws IOException{
        setRunnableServerSocket(port);
    }
    public ControllerRunnable(int port, InetAddress host) throws UnknownHostException{
        setRunnableSocket(port, host);
    }
    public ControllerSocket getRunnableServerSocket(){
        return runnableServerSocket;
    }
    private void setRunnableServerSocket(int port) throws IOException{
        this.runnableServerSocket = new ControllerSocket(port);
    }
    public ControllerSocket getRunnableSocket(){
        return runnableSocket;
    }
    private void setRunnableSocket(int port, InetAddress host) throws UnknownHostException{		
        this.runnableSocket = new ControllerSocket(port, host);
    }
    public void closeAllRunnable() throws IOException{
        if(runnableServerSocket != null)
            runnableServerSocket.closeAll();
        if(runnableSocket != null)
            runnableSocket.closeAll();
    }
}
