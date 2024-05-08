package interfaz.admin;

import com.jcraft.jsch.JSchException;
import interfaz.custom_components.*;
import interfaz.template.ModifyRegistrationFrameTemplate;
import users.Admin;

import java.awt.*;
import java.sql.*;
import java.util.Objects;

public class ModifyItineraryFrame extends ModifyRegistrationFrameTemplate implements Borders, Fonts, Icons {
    // Botones
    CustomButton botonModificarItinerario;

    // Etiquetas
    CustomFieldLabel codigoItinerario;
    CustomFieldLabel nombreItinerario;
    CustomFieldLabel descripcionItinerario;
    CustomFieldLabel olimpiadaItinerario;

    // Campos de texto
    CustomTextField campoCodigoItinerario;
    CustomTextField campoNombreItinerario;
    CustomTextField campoDescripcionItinerario;
    CustomComboBox campoOlimpiadaItinerario;

    // Paneles
    CustomPanel inputPanel;
    CustomPanel createButtonPanel;

    // Otros
    Admin admin;
    String oldCode, oldTitle, oldDesc, oldOlymp;

    public ModifyItineraryFrame(Admin administrador, String codigo, String titulo, String descripcion, String olimpiada) throws JSchException, SQLException {
        super(335, "Modificar itinerario");
        
        admin = administrador;
        oldCode = codigo;
        oldTitle = titulo;
        oldDesc = descripcion;
        oldOlymp = olimpiada;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);

        // Botón de volver
        getGoBackButton().addActionListener(e -> {
            try {
                new CheckItinerariesFrame(administrador);

            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        });

        // Botón modificar
        botonModificarItinerario.addActionListener(e -> {
            if (campoCodigoItinerario.getText().matches("^\\s*$")
                    || campoNombreItinerario.getText().matches("^\\s*$")
                    || Objects.requireNonNull(campoOlimpiadaItinerario.getSelectedItem()).toString().matches("^\\s*$")) {
                new ErrorJOptionPane("Los campos Código, Nombre y Olimpiada son obligatorios");

            } else {
                String code = campoCodigoItinerario.getText();
                String name = campoNombreItinerario.getText();
                String desc = campoDescripcionItinerario.getText();
                String olymp = (String) campoOlimpiadaItinerario.getSelectedItem();

                try {
                    if (administrador.modifyItinerario(oldCode, code, name, desc, olymp) == 0) {
                        new CheckItinerariesFrame(administrador);
                        dispose();

                    }

                } catch (JSchException | SQLException ex) {
                    new ErrorJOptionPane(ex.getMessage());

                }

            }
        });
    }

    @Override
    protected CustomPanel createCenterPanel() {
        try {
            codigoItinerario = new CustomFieldLabel("Código (*)");
            nombreItinerario = new CustomFieldLabel("Nombre (*)");
            descripcionItinerario = new CustomFieldLabel("Descripción");
            olimpiadaItinerario = new CustomFieldLabel("Olimpiada (*)");
            campoCodigoItinerario = new CustomTextField(oldCode);
            campoNombreItinerario = new CustomTextField(oldTitle);
            campoDescripcionItinerario = new CustomTextField(oldDesc);
            campoOlimpiadaItinerario = new CustomComboBox();

            ResultSet olympCodes = admin.selectCol("T_OLIMPIADAS", "CODIGO");

            // Se añaden los códigos de olimpiada al combo box
            while (olympCodes.next()) {
                String registro = olympCodes.getString("CODIGO");
                campoOlimpiadaItinerario.addItem(registro);
            }

            olympCodes.close();

            campoOlimpiadaItinerario.setSelectedItem(oldOlymp);

            inputPanel = new CustomPanel();
            inputPanel.setLayout(new GridLayout(4, 2, 10, 10));
            inputPanel.add(codigoItinerario);
            inputPanel.add(campoCodigoItinerario);
            inputPanel.add(nombreItinerario);
            inputPanel.add(campoNombreItinerario);
            inputPanel.add(descripcionItinerario);
            inputPanel.add(campoDescripcionItinerario);
            inputPanel.add(olimpiadaItinerario);
            inputPanel.add(campoOlimpiadaItinerario);

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }
        
        return inputPanel;
    }

    @Override
    protected CustomPanel createSouthPanel() {
        botonModificarItinerario = new CustomButton("Modificar itinerario", 175, 30);

        createButtonPanel = new CustomPanel();
        createButtonPanel.add(botonModificarItinerario);
        
        return createButtonPanel;
    }
}


