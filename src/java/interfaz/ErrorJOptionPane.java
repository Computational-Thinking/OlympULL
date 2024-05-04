package interfaz;

public class ErrorJOptionPane extends CustomJOptionPane {
    public ErrorJOptionPane(String mensaje) {
        super("ERROR - " + mensaje);
    }
}
