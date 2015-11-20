/*
 * @author Loc Truong
 */
package chemcam.src;
import java.io.*;
import java.net.*;
public final class ControllerSocket{
    private ServerSocket serverSocket;
    private Socket socket;
    private int port;
    private InetAddress host;
    public ControllerSocket(int port) throws IOException{
        setPort(port);
        serverSocket = getServerSocket();
    }
    public ControllerSocket(int port, InetAddress host) throws UnknownHostException{
        setPort(port);
        setHost(host);
    }
    public ServerSocket getServerSocket() throws IOException{
        if(serverSocket == null)
            serverSocket = new ServerSocket(getPort());
        return serverSocket;
    }
    public Socket getSocket() throws IOException{
        if(socket == null)
            setSocket();
        return socket;
    }
    public Socket getNewSocket() throws IOException{
        setSocket();
        return socket;
    }
    public void setSocket(Socket socket){
        this.socket = socket;
    }
    private void setSocket() throws IOException{
        if(this.socket != null)
            socket.close();
        if(host != null)
            this.socket = new Socket(host, port);
    }
    public int getPort(){
        return port;
    }
    public void setPort(int port){
        this.port = port;
    }
    public InetAddress getHost() throws UnknownHostException{
        if(this.host == null)
            setHost(null);
        return host;
    }
    public void setHost(InetAddress host) throws UnknownHostException{
        if(host == null)
            this.host = InetAddress.getLocalHost();
        else
            this.host = host;
    }
    public void openSocket() throws IOException{
        setSocket(serverSocket.accept());
    }
    public void closeAll() throws IOException{
        if (serverSocket != null)
            serverSocket.close();
        if(socket != null)
            socket.close();
    }
    public void closeSocket() throws IOException{
        if(socket != null)
            socket.close();
    }
}

