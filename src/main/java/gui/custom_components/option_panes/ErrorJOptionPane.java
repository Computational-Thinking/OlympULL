package gui.custom_components.option_panes;

import gui.custom_components.option_panes.CustomJOptionPane;

public class ErrorJOptionPane extends CustomJOptionPane {
    public ErrorJOptionPane(String mensaje) {
        super("ERROR - " + mensaje);
    }
}
