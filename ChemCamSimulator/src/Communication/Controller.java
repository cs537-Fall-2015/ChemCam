package communication;

import java.util.ArrayList;

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

}
