package gui.admin;

import com.jcraft.jsch.JSchException;
import gui.custom_components.*;
import gui.template_pattern.NewRegistrationFrameTemplate;
import users.Admin;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class NewItineraryFrame extends NewRegistrationFrameTemplate implements Borders, Fonts, Icons {
    // Botones
    CustomButton botonCrearItinerario;

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

    public NewItineraryFrame(Admin administrador) throws JSchException, SQLException {
        super(325, "Nuevo itinerario");
        
        this.admin = administrador;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);
        
        this.setVisible(true);

        getGoBackButton().addActionListener(e -> {
            new AdminFrame(administrador);
            dispose();
        });

        // Botón de crear itinerario
        botonCrearItinerario.addActionListener(e -> {
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
                    if (administrador.createItinerario(code, name, desc, olymp) == 0) {
                        campoCodigoItinerario.setText("");
                        campoNombreItinerario.setText("");
                        campoDescripcionItinerario.setText("");
                        campoOlimpiadaItinerario.setSelectedItem(campoOlimpiadaItinerario.getItemAt(0));
                    }

                } catch (JSchException | SQLException exc) {
                    new ErrorJOptionPane("Ha ocurrido un error inesperado");
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
            campoCodigoItinerario = new CustomTextField("");
            campoNombreItinerario = new CustomTextField("");
            campoDescripcionItinerario = new CustomTextField("");

            ArrayList<String> codigosOlimpiadas = new ArrayList<>();
            campoOlimpiadaItinerario = new CustomComboBox();

            // Se obtienen los códigos de las olimpiadas existentes
            ResultSet olympCodes = admin.selectCol("T_OLIMPIADAS", "CODIGO");

            // Se añaden los códigos al ArrayList
            while (olympCodes.next()) {
                String registro = olympCodes.getString("CODIGO");
                codigosOlimpiadas.add(registro);
            }

            // Utilizamos los códigos para meterlos en el combo box
            for (String codigosOlimpiada : codigosOlimpiadas) {
                campoOlimpiadaItinerario.addItem(codigosOlimpiada);
            }

            olympCodes.close();

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
        botonCrearItinerario = new CustomButton("Crear itinerario", 150, 30);

        createButtonPanel = new CustomPanel();
        createButtonPanel.add(botonCrearItinerario);

        return createButtonPanel;
    }
}
