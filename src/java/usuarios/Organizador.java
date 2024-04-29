package usuarios;

import java.util.ArrayList;

public class Organizador extends Usuario {
    ArrayList<String> itinerarios;

    public Organizador(String name, String password, ArrayList<String> itinerarios) {
        this.userName = name;
        this.password = password;
        this.itinerarios = itinerarios;
    }
}
