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
import java.awt.image.*;
import java.text.*;
import java.util.concurrent.*;
import javax.imageio.*;
import javax.swing.*;
import javax.xml.bind.*;
public class AgentPanel extends javax.swing.JPanel{
    private final int AgentPort = 9111;
    private final int ControllerPort = 9011;
    private final AgentStatus status = new AgentStatus();
    private final BlockingQueue queue = new ArrayBlockingQueue(1024);    
    public AgentPanel(){     
        initComponents();
        serverStart();
        workerStart();
        reporterStart();
    }
    @SuppressWarnings("unchecked")
    private void serverStart(){
        AgentRunnable agent = null;  
        try{
            agent = new AgentRunnable(AgentPort, queue){
                @Override
                public void run(){
                    try{
                        jTextArea1.append("Server: Waiting for command.\n");
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        while(true){
                            getRunnableServerSocket().openSocket();
                            try(ObjectInputStream ois = new ObjectInputStream(getRunnableServerSocket().getSocket().getInputStream())){
                                String jsonString = (String)ois.readObject();
                                ArrayList<CommandObject> command = gson.fromJson(jsonString, new TypeToken<ArrayList<CommandObject>>(){}.getType());
                                if(!command.isEmpty()){                                    
                                    jTextArea1.append("Server: Commands Received from Controller.\n");
                                    //jTextArea1.append(gson.toJson(command) + "\n");
                                    int queueNumber = queue.size();
                                    jTextArea1.append("Server: Assigned Queue Item #" + queueNumber + " to Worker Thread.\n");
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
                    while(true){                            
                        if(!queue.isEmpty()){
                            try{
                                QueueItem qi = (QueueItem)queue.take();
                                jTextArea1.append("Worker: Starting New Tasks for Queue Item #" + qi.getIndex() + ".\n");
                                String commandsJSON = qi.toString();
                                Gson gson = new Gson();
                                ArrayList<CommandObject> recievedList = gson.fromJson(commandsJSON, new TypeToken<ArrayList<CommandObject>>(){}.getType());                                
                                for(CommandObject i: recievedList){
                                    jTextArea1.append("Worker: Executing - " + i.toString() + ".\n");
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
                                            jTextArea1.append("Worker: Analysing Result.\n");
                                            status.setStatus(Status.AnalysingResults);                     
                                            try{
                                                gatherData();
                                            }
                                            catch(ParseException exception){
                                                Utils.log("Exception: " + exception + "\n");
                                            }
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
                                jTextArea1.append("Worker: All Tasks Completed for Queue Item #" + qi.getIndex() + ".\n");
                            }
                            catch(InterruptedException exception){
                                Utils.log("Exception: " + exception + "\n");
                            }
                        }                                
                    }                   
                }
            };
        }
        catch(IOException exception){
            Utils.log("Exception: " + exception + "\n");
        }
        RoverThread agentConnectThread = new RoverThread(agent, "Agent Worker");
        agentConnectThread.start();        
    }
    private void reporterStart(){
        AgentRunnable agent = null;        
        try{
            agent = new AgentRunnable(ControllerPort, null, queue){                
                @Override
                public void run(){
                    try(ObjectOutputStream oos = new ObjectOutputStream(getRunnableSocket().getSocket().getOutputStream())){
                        while(true){ 
                            try{
                                //sleeping inside a thread! WOOT! =) 
                                Thread.sleep(50000);
                                jTextArea1.append("Reporter: Starting Scheduled Tasks.\n");
                                Thread.sleep(5000);
                                jTextArea1.append("Reporter: Compiling Reports.\n");
                                Thread.sleep(5000);
                                sendReport(oos);                                
                            }
                            catch(InterruptedException exception){
                                Utils.log("Exception: " + exception + "\n");
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
        RoverThread agentConnectThread = new RoverThread(agent, "Agent Reporter");
        agentConnectThread.start(); 
    }
    private void gatherData() throws ParseException{
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").setPrettyPrinting().create();
        String resultListJSON = "";
        try{
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("src/chemcam/data/AgentData/gatherData.txt").getAbsoluteFile()))) {
                String line;                
                while((line = bufferedReader.readLine()) != null){
                    resultListJSON += line;
                }
            }  
        }
        catch(IOException exception){
            Utils.log("Exception: " + exception + "\n");
        }
        String reportListJSON = "";
        try{
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("src/chemcam/data/AgentData/reportData.txt").getAbsoluteFile()))) {
                String line;                
                while((line = bufferedReader.readLine()) != null){
                    reportListJSON += line;
                }
            }  
        }
        catch(IOException exception){
            Utils.log("Exception: " + exception + "\n");
        }
        ArrayList<ResultObject> resultList = gson.fromJson(resultListJSON, new TypeToken<ArrayList<ResultObject>>(){}.getType());
        ArrayList<ReportObject> reportList = gson.fromJson(reportListJSON, new TypeToken<ArrayList<ReportObject>>(){}.getType());
        Random random = new Random();
        ResultObject result = resultList.get(random.nextInt(resultList.size()));
        String base64Encoded = "";
        try{
            // 5 pictures...
            BufferedImage bufferedImage = ImageIO.read(new File("src/chemcam/data/AgentData/ccPic" + (random.nextInt(5) + 1) + ".jpg"));
            try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
                ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
                byteArrayOutputStream.flush();
                byte[] imageInByte = byteArrayOutputStream.toByteArray();
                base64Encoded += DatatypeConverter.printBase64Binary(imageInByte);                
            }
        }
        catch(IOException exception){
            Utils.log("Exception: " + exception + "\n");
        }
        ReportObject report = new ReportObject(result.getRECORD_TYPE(), result.getDATA_SET_NAME(), result.getPRODUCT_CREATION_TIME(), base64Encoded);
        if(reportList != null){
            reportList.add(report);
            String reportJSON = gson.toJson(reportList);
            try(FileWriter writer = new FileWriter("src/chemcam/data/AgentData/reportData.txt")){
                writer.write(reportJSON);
            }
            catch(IOException exception){
                Utils.log("Exception: " + exception + "\n");
            }
        }
        else{
            String reportJSON = gson.toJson(report);
            try(FileWriter writer = new FileWriter("src/chemcam/data/AgentData/reportData.txt")){
                writer.write("[" + reportJSON + "]");
            }
            catch(IOException exception){
                Utils.log("Exception: " + exception + "\n");
            }
        }
        jTextArea1.append("Worker: Report Saved to Memory.\n");
    }
    private void sendReport(ObjectOutputStream oos){
        String reportListJSON = "";
        try{
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("src/chemcam/data/AgentData/reportData.txt").getAbsoluteFile()))) {
                String line;                
                while((line = bufferedReader.readLine()) != null){
                    reportListJSON += line;
                }
            }  
        }
        catch(IOException exception){
            Utils.log("Exception: " + exception + "\n");
        }
        if(!reportListJSON.isEmpty()){
            try{           
                oos.writeObject(reportListJSON);
                oos.flush();
            }
            catch(IOException exception){
                Utils.log("Agent Exception: " + exception + "\n");
            }
            jTextArea1.append("Reporter: Report sent to Controller\n");  
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new JLabel("Agent Console", SwingConstants.CENTER);

        jTextArea1.setBackground(new java.awt.Color(0, 0, 0));
        jTextArea1.setColumns(20);
        jTextArea1.setForeground(new java.awt.Color(51, 153, 0));
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Agent Console");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
