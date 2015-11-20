/*
 * @author Loc Truong
 */
package main;
import json.*;
import chemcam.*;
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
                        while(true){
                            jTextArea1.append("Agent - Server: Waiting for command.\n");
                            getRunnableServerSocket().openSocket();
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            try (ObjectInputStream ois = new ObjectInputStream(getRunnableServerSocket().getSocket().getInputStream())) {
                                String commandsJSON = (String)ois.readObject();
                                ArrayList<CommandObject> command = gson.fromJson(commandsJSON, new TypeToken<ArrayList<CommandObject>>(){}.getType());
                                if(!command.isEmpty()){                                    
                                    jTextArea1.append("Agent - Server: Commands Received from Controller.\n");
                                    jTextArea1.append(gson.toJson(command) + "\n");
                                    int queueNumber = queue.size();
                                    jTextArea1.append("Agent - Server: Assigned Queue Item #" + queueNumber + " to Client Thread.\n");
                                    queue.put(new QueueItem(commandsJSON, queueNumber));
                                }
                                ois.close();
                            }
                        }                        
                    } 
                    catch(IOException | ClassNotFoundException | InterruptedException exception) {
                        Utils.log("Exception: " + exception + "\n");
                    } 
                }
            };
        }
        catch(IOException socketException){
            Utils.log("IOException on creating new socket: " + socketException + "\n");
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
                    while(true){
                        try{
                            QueueItem qi = (QueueItem)queue.take();
                            String commandsJSON = qi.toString();
                            Gson gson = new Gson();
                            ArrayList<CommandObject> recievedList = gson.fromJson(commandsJSON, new TypeToken<ArrayList<CommandObject>>(){}.getType());
                            jTextArea1.append("Agent - Client: Starting New Tasks.\n");
                            for(CommandObject i: recievedList){
                                jTextArea1.append("Agent - Client: Executing Command - " + i.toString() + ".\n");
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
                                        status.setStatus(Status.AnalysingResults);
                                        Thread.sleep(5000);
                                        sendReport(this);
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
                                try{
                                    Thread.sleep(5000);
                                }
                                catch(InterruptedException exception){
                                    Utils.log("Exception: " + exception + "\n");
                                }
                            }
                            jTextArea1.append("Agent - Client: Tasks are finished for queue number " + qi.getIndex() + ".\n");
                        }
                        catch(InterruptedException exception){
                            Utils.log("Exception: " + exception + "\n");
                        }
                    }
                }
            };
        }
        catch(IOException socketException){
            Utils.log("IOException on creating new socket: " + socketException + "\n");
        }
        RoverThread agentConnectThread = new RoverThread(agent, "Agent Client");
        agentConnectThread.start();
    }
    private void sendReport(AgentRunnable agent){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
        BufferedReader br = null;                                        
        String reportJSON = "";
        try{
            br = new BufferedReader(new FileReader(new File("src/data/data.txt").getAbsoluteFile()));
        }
        catch(FileNotFoundException exception){
            Utils.log("Exception: " + exception + "\n");
        }
        if(br != null){
            String line;
            try{
                while((line = br.readLine()) != null){
                    reportJSON += line;
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
        Random randomGenerator = new Random();
        //jTextArea1.append("what i got from report file" + reportJSON + "\n");
        ArrayList<ReportObject> reportList = gson.fromJson(reportJSON, new TypeToken<ArrayList<ReportObject>>(){}.getType());
        try{
            try(ObjectOutputStream oos = new ObjectOutputStream(agent.getRunnableSocket().getSocket().getOutputStream())){
                RoverThread.sleep(2000);
                jTextArea1.append("Agent - Client: Sending report to Controller\n");
                //jTextArea1.append("what i got after parse" + reportList.toString() + "\n");
                //jTextArea1.append("what i got for 1st item in array" + reportList.get(0).toString() + "\n");
                oos.writeObject("[" + reportList.get(randomGenerator.nextInt(reportList.size())) + "]");
                RoverThread.sleep(1000);
            } 
            agent.closeAllRunnable();
        }
        catch(InterruptedException | IOException exception){
            Utils.log("Exception: " + exception + "\n");
        }
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
