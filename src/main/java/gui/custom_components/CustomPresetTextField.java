package gui.custom_components;

public class CustomPresetTextField extends CustomLabel implements Fonts {
    public CustomPresetTextField() {
        super(fuenteCampoTexto);
    }
    public CustomPresetTextField(String text) {
        super(text, fuenteCampoTexto);
    }
}
