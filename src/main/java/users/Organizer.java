package users;

import gui.custom_components.option_panes.MessageJOptionPane;
import operations.DBOperations;

import java.util.ArrayList;

public class Organizer extends User {
    ArrayList<String> itineraries;

    public Organizer(String name, String password, ArrayList<String> itineraries) {
        this.userName = name;
        this.password = password;
        this.userType = "organizador";
        this.itineraries = itineraries;
    }

    public ArrayList<String> getItineraries() {
        return itineraries;
    }
}
