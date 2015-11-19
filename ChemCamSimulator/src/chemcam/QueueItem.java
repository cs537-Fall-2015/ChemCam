package chemcam;
public class QueueItem{
    String jsonString;
    int index;
    public QueueItem(String jsonString, int index){
        this.jsonString = jsonString;
        this.index = index;
    }
    @Override
    public String toString(){
        return jsonString;
    }
    public int getIndex(){
        return index;
    }
}
