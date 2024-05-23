package gui.custom_components.text_fields;

import gui.custom_components.labels.CustomLabel;
import gui.custom_components.predefined_elements.Fonts;

public class CustomPresetTextField extends CustomLabel implements Fonts {
    public CustomPresetTextField() {
        super(textFieldFont);
    }
    public CustomPresetTextField(String text) {
        super(text, textFieldFont);
    }
}
