package chemcam;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public final class AgentSocket {
    private ServerSocket agentSocket;
    private Socket socket;
    private int port;
    public AgentSocket(int port) throws IOException{
        setPort(port);
        agentSocket = getAgentSocket();
    }
    public void closeAll() throws IOException{
        if (agentSocket != null)
            agentSocket.close();
        if(socket != null)
            socket.close();
    }
    public void closeSocket() throws IOException{
        if(socket != null)
            socket.close();
    }
    public ServerSocket getAgentSocket() throws IOException{
        if(agentSocket == null)
            agentSocket = new ServerSocket(getPort());
        return agentSocket;
    }
    public void openSocket() throws IOException{
        setSocket(agentSocket.accept());
    }
    public int getPort(){
        return port;
    }
    public void setPort(int port){
        this.port = port;
    }
    public Socket getSocket(){
        return socket;
    }
    public void setSocket(Socket socket){
        this.socket = socket;
    }
}
