package usecase;

import generic.RoverServerRunnable;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UseCaseServer extends RoverServerRunnable{

	public UseCaseServer(int port) throws IOException {
		super(port);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			List<String> inputCommands = new ArrayList<String>();
			inputCommands.add("CCAM_COOLER_ON");
			inputCommands.add("CCAM_COOLER_OFF");
			inputCommands.add("CCAM_POWER_ON");
			inputCommands.add("CCAM_POWER_OFF");
			inputCommands.add("CCAM_SET_FOCUS");
			inputCommands.add("CCAM_LASER_ON");
			inputCommands.add("CCAM_LASER_OFF");
			inputCommands.add("CCAM_CWL_WARM");
			inputCommands.add("CCAM_LIBS_WARM");
			inputCommands.add("CCAM_FIRE_LASER");
			
			while(true){				
	            System.out.println("Server: Waiting for client request");	            
				//creating socket and waiting for client connection
	            getRoverServerSocket().openSocket();
	            //read from socket to ObjectInputStream object
	            ObjectInputStream ois = new ObjectInputStream(getRoverServerSocket().getSocket().getInputStream());
//	            ObjectInputStream tempOis = new ObjectInputStream((InputStream) inputCommands);
//	            System.out.println("temp ois"+tempOis);
//	            List<String> allCmds =  (List<String>) tempOis.readObject();
//	            System.out.println("all cmds size"+allCmds.size());
	            String message = null;
	            for(String command : inputCommands){
	            	
	    		    switch (command) {
	    			
	    		    case "CCAM_COOLER_ON":
//	    				System.out.println("Client: It is used to turn on the cooler - "+command);
	    				message = "It is used to turn on the cooler";
	    				break;
	    			
	    		    case "CCAM_COOLER_OFF":
//	    				System.out.println("Client: It is used to turn off the cooler - "+command);
	    				message = "It is used to turn off the cooler";
	    				break;
	    			
	    			case "CCAM_POWER_ON":
//	    				System.out.println("Client: It is used to turn on the system - "+command);
	    				message = "It is used to turn on the system";
	    				break;
	    			
	    			case "CCAM_POWER_OFF":
//	    				System.out.println("Client: It is used to turn off the system - "+command);
	    				message = "It is used to turn off the system";
	    				break;
	    			
	    			case "CCAM_SET_FOCUS":
//	    				System.out.println("Client: It is used to focus the identified object - "+command);
	    				message = "It is used to focus the identified object";
	    				break;

	    			case "CCAM_LASER_ON":
//	    				System.out.println("Client: It is used to turn on the laser beam - "+command);
	    				message = "It is used to turn on the laser beam";
	    				break;

	    			case "CCAM_LASER_OFF":
//	    				System.out.println("Client: It is used to turn off the laser beam - "+command);
	    				message = "It is used to turn off the laser beam";
	    				break;
	    				
	    			case "CCAM_CWL_WARM":
//	    				System.out.println("Client: It is used to warm CWL(continuous-wave laser) - "+command);
	    				message = "It is used to warm CWL(continuous-wave laser)";
	    				break;
	    				
	    			case "CCAM_LIBS_WARM":
//	    				System.out.println("Client: It is used to warm LIBS - "+command);
	    				message = "It is used to warm LIBS";
	    				break;
	    			case "CCAM_FIRE_LASER":
//	    				System.out.println("Client: It is used to fire the laser beam to focused object - "+command);
	    				message = "It is used to fire the laser beam to focused object";
	    				break;

	    			default:
	    				message = "exit";
	    				break;
	    			}

	            	
	            }
	            
	            
	            //convert ObjectInputStream object to String
//	            String message = (String) ois.readObject();
//	            System.out.println("Server: Message Received from Client - " + message.toUpperCase());
	            //create ObjectOutputStream object
	            ObjectOutputStream oos = new ObjectOutputStream(getRoverServerSocket().getSocket().getOutputStream());
	            //write object to Socket
	            oos.writeObject("Server says Hi Client - "+message);
	            //close resources
	            ois.close();
	            oos.close();
	            //getRoverServerSocket().closeSocket();
	            //terminate the server if client sends exit request
	            if(message.equalsIgnoreCase("exit")) break;
	        }
			System.out.println("Server: Shutting down Socket server!!");
	        //close the ServerSocket object
	        closeAll();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	    catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//	    }
        catch(Exception error){
        	System.out.println("Server: Error:" + error.getMessage());
        }
		
	}
}
