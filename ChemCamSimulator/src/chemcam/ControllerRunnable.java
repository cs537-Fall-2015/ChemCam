package chemcam;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
public abstract class ControllerRunnable implements Runnable{
    private ControllerSocket controllerSocket;
    public ControllerRunnable(int port, InetAddress host) throws UnknownHostException{
        setControllerSocket(port, host);
    }
    private void setControllerSocket(int port, InetAddress host) throws UnknownHostException{		
        this.controllerSocket = new ControllerSocket(port, host);
    }
    public ControllerSocket getControllerSocket(){
        return controllerSocket;
    }
    public void closeAll() throws IOException{
        if(controllerSocket != null)
            controllerSocket.closeAll();
    }
}
