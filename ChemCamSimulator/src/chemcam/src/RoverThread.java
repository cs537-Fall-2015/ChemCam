package chemcam.src;
public class RoverThread extends Thread{
    public RoverThread(Runnable runnable, String name){
        super(runnable, name);
    }
}
