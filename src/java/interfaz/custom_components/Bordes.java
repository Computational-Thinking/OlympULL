package interfaz.custom_components;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

public interface Bordes {
    Border borde = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    Border bordeDoble = BorderFactory.createEmptyBorder(20, 20, 20, 20);
    Border bordeRubricMaxField = BorderFactory.createEmptyBorder(0, 10, 0, 10);
    Border bordeRubricBasicInfo = BorderFactory.createEmptyBorder(10, 10, 0, 10);

    //Border bordeCampoTexto = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK);
    Border bordeCampoTexto = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK), // Izquierda: 10 píxeles
            BorderFactory.createEmptyBorder(0, 5, 0, 0)); // Margen interno general
}
