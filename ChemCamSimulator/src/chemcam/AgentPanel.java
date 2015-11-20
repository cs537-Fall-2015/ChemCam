/*
 * @author Loc Truong
 */
package chemcam;
import chemcam.json.*;
import chemcam.src.*;
import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import java.util.concurrent.*;
public class AgentPanel extends javax.swing.JPanel{
    private final int AgentPort = 9111;
    private final int ControllerPort = 9011;
    private final AgentStatus status = new AgentStatus();
    private final BlockingQueue queue = new ArrayBlockingQueue(1024);    
    public AgentPanel(){     
        initComponents();
        serverStart();
        workerStart();
    }
    private void serverStart(){
        AgentRunnable agent = null;  
        try{
            agent = new AgentRunnable(AgentPort, queue){
                @Override
                public void run(){
                    try{
                        jTextArea1.append("Agent - Server: Waiting for command.\n");
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        while(true){
                            getRunnableServerSocket().openSocket();
                            try(ObjectInputStream ois = new ObjectInputStream(getRunnableServerSocket().getSocket().getInputStream())){
                                String jsonString = (String)ois.readObject();
                                ArrayList<CommandObject> command = gson.fromJson(jsonString, new TypeToken<ArrayList<CommandObject>>(){}.getType());
                                if(!command.isEmpty()){                                    
                                    jTextArea1.append("Agent - Server: Commands Received from Controller.\n");
                                    //jTextArea1.append(gson.toJson(command) + "\n");
                                    int queueNumber = queue.size();
                                    jTextArea1.append("Agent - Server: Assigned Queue Item #" + queueNumber + " to Client Thread.\n");
                                    queue.put(new QueueItem(jsonString, queueNumber));                                    
                                }
                                ois.close();
                                Thread.sleep(3000);
                            }
                        }                        
                    } 
                    catch(IOException | ClassNotFoundException | InterruptedException exception) {
                        Utils.log("Exception: " + exception + "\n");
                    } 
                }
            };
        }
        catch(IOException exception){
            Utils.log("Exception: " + exception + "\n");
        }
        RoverThread agentListenThread = new RoverThread(agent, "Agent Server");
        agentListenThread.start();
    }
    private void workerStart(){
        AgentRunnable agent = null;        
        try{
            agent = new AgentRunnable(ControllerPort, null, queue){                
                @Override
                public void run(){
                    try(ObjectOutputStream oos = new ObjectOutputStream(getRunnableSocket().getSocket().getOutputStream())){ 
                        while(true){                            
                            if(!queue.isEmpty()){
                                try{
                                    QueueItem qi = (QueueItem)queue.take();
                                    String commandsJSON = qi.toString();
                                    Gson gson = new Gson();
                                    ArrayList<CommandObject> recievedList = gson.fromJson(commandsJSON, new TypeToken<ArrayList<CommandObject>>(){}.getType());
                                    jTextArea1.append("Agent - Client: Starting New Tasks.\n");
                                    for(CommandObject i: recievedList){
                                        jTextArea1.append("Agent - Client: Executing - " + i.toString() + ".\n");
                                        switch(i.toString()){
                                            case "CCAM_POWER_ON":
                                                status.setStatus(Status.ON);
                                                break;
                                            case "CCAM_COOLER_ON":
                                                status.setStatus(Status.CoolerON);
                                                break;
                                            case "CCAM_LASER_ON":
                                                status.setStatus(Status.LaserON);
                                                break;
                                            case "CCAM_CWL_WARM":
                                                status.setStatus(Status.WarmingUp);
                                                break;
                                            case "CCAM_LIBS_WARM":
                                                status.setStatus(Status.WarmingUp);
                                                break;
                                            case "CCAM_SET_FOCUS":
                                                status.setStatus(Status.FocusingLens);
                                                break;
                                            case "CCAM_FIRE_LASER":
                                                status.setStatus(Status.FiringLaser);
                                                Thread.sleep(5000);
                                                jTextArea1.append("Agent - Client: Analysing Result.\n");
                                                status.setStatus(Status.AnalysingResults);
                                                Thread.sleep(5000);
                                                sendReport(oos, analyzeReport());
                                                break;
                                            case "CCAM_LASER_OFF":
                                                status.setStatus(Status.CoolerOFF);
                                                break;
                                            case "CCAM_COOLER_OFF":
                                                status.setStatus(Status.LaserOFF);
                                                break;
                                            case "CCAM_POWER_OFF":
                                                status.setStatus(Status.OFF);
                                                break;
                                        }
                                        //sleeping inside a thread! WOOT! =)                            
                                        Thread.sleep(5000);
                                    }
                                    jTextArea1.append("Agent - Client: Tasks are finished for queue number " + qi.getIndex() + ".\n");
                                }
                                catch(InterruptedException exception){
                                    Utils.log("Exception: " + exception + "\n");
                                }
                            }                                
                        }
                    }
                    catch(IOException exception){
                        Utils.log("Exception: " + exception + "\n");
                    }                    
                }
            };
        }
        catch(IOException exception){
            Utils.log("Exception: " + exception + "\n");
        }
        RoverThread agentConnectThread = new RoverThread(agent, "Agent Client");
        agentConnectThread.start();        
    }
    private String analyzeReport(){
        BufferedReader br = null;                                        
        String resultJSON = "";
        try{
            br = new BufferedReader(new FileReader(new File("src/chemcam/data/data.txt").getAbsoluteFile()));
        }
        catch(FileNotFoundException exception){
            Utils.log("Exception: " + exception + "\n");
        }
        if(br != null){
            String line;
            try{
                while((line = br.readLine()) != null){
                    resultJSON += line;
                }
                br.close();
            }
            catch(IOException exception){
                Utils.log("Exception: " + exception + "\n");
            }
        }
        else{
            Utils.log("Failed to read from file. Should not be here...\n");
        }
        return resultJSON;
    }
    private void sendReport(ObjectOutputStream oos, String resultJSON){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();        
        Random randomGenerator = new Random();
        ArrayList<ReportObject> reportList = gson.fromJson(resultJSON, new TypeToken<ArrayList<ReportObject>>(){}.getType());
        try{           
            oos.writeObject("[" + reportList.get(randomGenerator.nextInt(reportList.size())) + "]");
            oos.flush();
        }
        catch(IOException exception){
            Utils.log("Agent Exception: " + exception + "\n");
        }
        jTextArea1.append("Agent - Client: Report sent to Controller\n");        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        jTextArea1.setBackground(new java.awt.Color(0, 0, 0));
        jTextArea1.setColumns(20);
        jTextArea1.setForeground(new java.awt.Color(51, 153, 0));
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
