package interfaz.custom_components;

public class CustomFieldLabel extends CustomLabel implements Fonts {
    public CustomFieldLabel() {
        super(fuenteCampoTexto);
    }
    public CustomFieldLabel(String text) {
        super(text, fuenteBotonesEtiquetas);
    }
}
