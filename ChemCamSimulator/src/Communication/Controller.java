package Communication;

import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.runtime.Coordinator;

public class Controller {

	public static void main(String[] args) {

		ArrayList<Model> list = new ArrayList<Model>();
		list.add(new Model("CCAM_POWER_ON"));
		list.add(new Model("CCAM_COOLER_ON"));
		list.add(new Model("CCAM_LASER_ON"));
		list.add(new Model("CCAM_CWL_WARM"));
		list.add(new Model("CCAM_SET_FOCUS"));
		list.add(new Model("CCAM_LIBS_WARM"));
		list.add(new Model("CCAM_FIRE_LASER"));
		list.add(new Model("CCAM_LASER_OFF"));
		list.add(new Model("CCAM_COOLER_OFF"));
		list.add(new Model("CCAM_POWER_OFF"));
	}

	private Coordinator coordinates;
    public void setCoordinates(Coordinator value) {
        coordinates = value;
    }
    public Coordinator getCoordinates() {
        return coordinates;
    }

    public void Rover(Coordinator coordinatesValue) {
        setCoordinates(coordinatesValue);
    }

    public void receiveCommands(String commands) throws Exception {
        for (char command : commands.toCharArray()) {
            if (!receiveSingleCommand(command)) {
                break;
            }
        }
    }

    public boolean receiveSingleCommand(char command) throws Exception {
        switch(Character.toUpperCase(command)) {
            case 'L':
                getCoordinates();
                return true;
            case 'R':
                getCoordinates();
                return true;
            default:
                throw new Exception("Command " + command + " is unknown.");
        }
    }

    public String getPosition() {
        return getCoordinates().toString();
    }

}
