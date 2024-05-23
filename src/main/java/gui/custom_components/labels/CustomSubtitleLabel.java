package gui.custom_components.labels;

import gui.custom_components.predefined_elements.Borders;
import gui.custom_components.predefined_elements.Fonts;

public class CustomSubtitleLabel extends CustomLabel implements Borders, Fonts {
    public CustomSubtitleLabel(String text) {
        super(text, subtitleFont);
        this.setBorder(border);
    }
}
