/*
 * @author Loc Truong
 */
package main;
import json.Command;
import chemcam.*;
import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import java.util.concurrent.*;

public class AgentPanel extends javax.swing.JPanel{
    public RoverThread agentListenThread;
    private int AgentPort = 9111;
    private int ControllerPort = 9011;
    private AgentStatus status = new AgentStatus();
    private BlockingQueue queue = new ArrayBlockingQueue(1024);    
    public AgentPanel(){
        AgentRunnable agent = null;        
        initComponents();
        try{
            agent = new AgentRunnable(AgentPort, queue){
                public int i = 1;
                @Override
                public void run(){
                    try{                        
                        while(true){
                            jTextArea1.append("Agent - Server Thread: Waiting for command.\n");
                            getRunnableServerSocket().openSocket();
                            ObjectInputStream ois = new ObjectInputStream(getRunnableServerSocket().getSocket().getInputStream());
                            ObjectOutputStream oos = new ObjectOutputStream(getRunnableServerSocket().getSocket().getOutputStream());
                            String jsonString = (String)ois.readObject();
                            if(!jsonString.isEmpty()){
                                jTextArea1.append("Agent - Server Thread: Commands Received from Controller.\n");
                                jTextArea1.append(jsonString + "\n");
                                jTextArea1.append("Agent - Server Thread: Assigned to Client Thread #" + i + ".\n");                                
                                queue.put(new QueueItem(jsonString, i));
                            }                            
                            ois.close();
                            oos.close();
                            i++;
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
        agentListenThread = new RoverThread(agent, "Agent Server Thread");
        executeCommands();
    }
    private void executeCommands(){
        RoverThread agentConnectThread;
        AgentRunnable agent = null;
        try{
            agent = new AgentRunnable(ControllerPort, null, queue){
                @Override
                public void run(){
                    do{
                        try{
                            QueueItem qi = (QueueItem)queue.take();
                            String jsonString = qi.toString();
                            int threadNumber = qi.getIndex();
                            Gson recievedJSON = new Gson();
                            ArrayList<Command> recievedList = recievedJSON.fromJson(jsonString, new TypeToken<ArrayList<Command>>(){}.getType());
                            for(Command i: recievedList){
                                jTextArea1.append("Agent - Client Thread #" + threadNumber + ": Executing Command - " + i.toString() + ".\n");
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
                        }
                        catch(InterruptedException exception){
                            Utils.log("Exception: " + exception + "\n");
                        }
                    }while(true);
                }
            };
        }
        catch(IOException socketException){
            Utils.log("IOException on creating new socket: " + socketException + "\n");
        }
        agentConnectThread = new RoverThread(agent, "Agent Client Thread #" + queue.size());
        agentConnectThread.start();
    } 
    public RoverThread getAgentListenThread(){
        return agentListenThread;
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
