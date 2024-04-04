package usuarios;

public class Monitor extends Usuario {

    public Monitor(String nombreUsuario, String password) {
        this.userName = nombreUsuario;
        this.password = password;
        this.userType = "monitor";
    }
}
