package gui.custom_components.labels;

import gui.custom_components.predefined_elements.Fonts;

public class CustomFieldLabel extends CustomLabel implements Fonts {
    public CustomFieldLabel() {
        super(fuenteBotonesEtiquetas);
    }
    public CustomFieldLabel(String text) {
        super(text, fuenteBotonesEtiquetas);
    }
}
