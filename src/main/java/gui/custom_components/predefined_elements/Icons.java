package gui.custom_components.predefined_elements;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public interface Icons {
    Image frameIcon = new ImageIcon(Objects.requireNonNull(Icons.class.getClassLoader().
             getResource("src/main/resources/images/icono-ull-original.png"))).getImage();

    Image editIcon = new ImageIcon(Objects.requireNonNull(Icons.class.getClassLoader().
            getResource("src/main/resources/images/edit icon.png"))).getImage();

    Image duplicateIcon = new ImageIcon(Objects.requireNonNull(Icons.class.getClassLoader().
            getResource("src/main/resources/images/duplicate icon.png"))).getImage();

    Image deleteIcon = new ImageIcon(Objects.requireNonNull(Icons.class.getClassLoader().
            getResource("src/main/resources/images/delete icon.png"))).getImage();

    Image exportIcon = new ImageIcon(Objects.requireNonNull(Icons.class.getClassLoader().
            getResource("src/main/resources/images/exportIcon.png"))).getImage();

    Image importIcon = new ImageIcon(Objects.requireNonNull(Icons.class.getClassLoader().
            getResource("src/main/resources/images/importIcon.png"))).getImage();
}
