package gui.custom_components;

public class CustomFieldLabel extends CustomLabel implements Fonts {
    public CustomFieldLabel() {
        super(fuenteBotonesEtiquetas);
    }
    public CustomFieldLabel(String text) {
        super(text, fuenteBotonesEtiquetas);
    }
}
