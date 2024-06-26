package gui.user_frames.admin;

import com.jcraft.jsch.JSchException;
import gui.ChangePasswordFrame;
import gui.user_frames.admin.assignations.ex_monitor.CheckExMonitorAssignationsFrame;
import gui.user_frames.admin.assignations.ex_monitor.NewAssignationExMonitorFrame;
import gui.user_frames.admin.assignations.ex_olymp.CheckExOlympAssignationsFrame;
import gui.user_frames.admin.assignations.ex_olymp.NewAssignationExOlympFrame;
import gui.user_frames.admin.assignations.it_org.CheckItOrgAssignationsFrame;
import gui.user_frames.admin.assignations.it_org.NewAssignationItOrgFrame;
import gui.user_frames.admin.exercises.CheckExercisesFrame;
import gui.user_frames.admin.exercises.NewExerciseFrame;
import gui.user_frames.admin.itineraries.CheckItinerariesFrame;
import gui.user_frames.admin.itineraries.NewItineraryFrame;
import gui.user_frames.admin.olympiads.CheckOlympiadsFrame;
import gui.user_frames.admin.olympiads.NewOlympiadFrame;
import gui.user_frames.admin.rubrics.CheckRubricsFrame;
import gui.user_frames.admin.rubrics.NewRubricFrame;
import gui.user_frames.admin.teams.CheckTeamsFrame;
import gui.user_frames.admin.teams.NewTeamFrame;
import gui.user_frames.admin.users.CheckUsersFrame;
import gui.user_frames.admin.users.NewUserFrame;
import gui.custom_components.*;
import gui.custom_components.buttons.CustomButton;
import gui.custom_components.labels.CustomSubtitleLabel;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.template_pattern.UserFrameTemplate;
import users.Admin;

import java.awt.*;
import java.sql.SQLException;

public class AdminFrame extends UserFrameTemplate {
    // Botones
    CustomButton createOlympiad;
    CustomButton checkOlympiads;
    CustomButton createItinerary;
    CustomButton checkItinerary;
    CustomButton createExercise;
    CustomButton checkExercises;
    CustomButton createRubrica;
    CustomButton checkRubricas;
    CustomButton createTeam;
    CustomButton checkTeams;
    CustomButton assignExerciseToOlympiad;
    CustomButton checkAssignationExOlymp;
    CustomButton createUser;
    CustomButton checkUsers;
    CustomButton assignExerciseToUser;
    CustomButton checkAssignationExMonitor;
    CustomButton assignItToOrg;
    CustomButton checkAssignationItOrg;
    CustomButton changePassword;
    
    // Labels
    CustomSubtitleLabel olympiadManagementLabel;
    CustomSubtitleLabel userManagementLabel;

    // Paneles
    CustomPanel olympiadButtonsPanel;
    CustomPanel userButtonsPanel;
    CustomPanel olympiadManagementPanel;
    CustomPanel userManagementPanel;

    public AdminFrame(Admin administrador) {
        super(690, "Panel Administrador", "¡Bienvenido al panel de administrador de OlympULL!");

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);

        // Action listeners
        createOlympiad.addActionListener(e -> {
            new NewOlympiadFrame(administrador);
            dispose();
        });

        checkOlympiads.addActionListener(e -> {
            try {
                new CheckOlympiadsFrame(administrador);

            } catch (SQLException | JSchException ex) {
                new ErrorJOptionPane("No se ha podido recuperar la información de la base de datos");
            }
            dispose();
        });

        createItinerary.addActionListener(e -> {
            try {
                new NewItineraryFrame(administrador);

            } catch (JSchException | SQLException ex) {
                new ErrorJOptionPane("No se ha podido recuperar la información de la base de datos");

            }

            dispose();
        });

        checkItinerary.addActionListener(e -> {
            try {
                new CheckItinerariesFrame(administrador);

            } catch (SQLException | JSchException ex) {
                new ErrorJOptionPane("No se ha podido recuperar la información de la base de datos");

            }

            dispose();
        });

        createExercise.addActionListener(e -> {
            try {
                new NewExerciseFrame(administrador);

            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);

            }

            dispose();
        });

        checkExercises.addActionListener(e -> {
            try {
                new CheckExercisesFrame(administrador);

            } catch (SQLException | JSchException ex) {
                new ErrorJOptionPane("No se ha podido recuperar la información de la base de datos");

            }

            dispose();
        });

        createRubrica.addActionListener(e -> {
            new NewRubricFrame(administrador);
            dispose();
        });

        checkRubricas.addActionListener(e -> {
            try {
                new CheckRubricsFrame(administrador);

            } catch (SQLException | JSchException ex) {
                new ErrorJOptionPane("No se ha podido recuperar la información de la base de datos");

            }

            dispose();
        });

        createTeam.addActionListener(e -> {
            try {
                new NewTeamFrame(administrador);

            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);

            }

            dispose();
        });

        checkTeams.addActionListener(e -> {
            try {
                new CheckTeamsFrame(administrador);

            } catch (SQLException | JSchException ex) {
                new ErrorJOptionPane("No se ha podido recuperar la información de la base de datos");

            }

            dispose();
        });

        assignExerciseToOlympiad.addActionListener(e -> {
            try {
                new NewAssignationExOlympFrame(administrador);

            } catch (JSchException | SQLException ex) {
                new ErrorJOptionPane("No se ha podido recuperar la información de la base de datos");


            }

            dispose();
        });

        checkAssignationExOlymp.addActionListener(e -> {
            try {
                new CheckExOlympAssignationsFrame(administrador);

            } catch (SQLException | JSchException ex) {
                new ErrorJOptionPane("No se ha podido recuperar la información de la base de datos");

            }

            dispose();
        });

        createUser.addActionListener(e -> {
            new NewUserFrame(administrador);
            dispose();
        });

        checkUsers.addActionListener(e -> {
            try {
                new CheckUsersFrame(administrador);
            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        });

        assignExerciseToUser.addActionListener(e -> {
            try {
                new NewAssignationExMonitorFrame(administrador);
                dispose();

            } catch (JSchException | SQLException ex) {
                new ErrorJOptionPane("No se ha podido recuperar la información de la base de datos");

            }
        });

        checkAssignationExMonitor.addActionListener(e -> {
            try {
                new CheckExMonitorAssignationsFrame(administrador);
                dispose();
            } catch (JSchException | SQLException ex) {
                new ErrorJOptionPane("No se ha podido recuperar la información de la base de datos");
            }
        });

        assignItToOrg.addActionListener(e -> {
            try {
                new NewAssignationItOrgFrame(administrador);
                dispose();

            } catch (JSchException | SQLException ex) {
                new ErrorJOptionPane("No se ha podido recuperar la información de la base de datos");

            }
        });

        checkAssignationItOrg.addActionListener(e -> {
            try {
                new CheckItOrgAssignationsFrame(administrador);
                dispose();

            } catch (JSchException | SQLException ex) {
                new ErrorJOptionPane("No se ha podido recuperar la información de la base de datos");

            }
        });

        changePassword.addActionListener(e -> {
            new ChangePasswordFrame(administrador);
            dispose();
        });
    }

    @Override
    protected CustomPanel createCenterPanel() {
        // Panel de gestión de olimpiadas
        createOlympiad = new CustomButton("Crear nueva olimpiada", 200, 30);
        checkOlympiads = new CustomButton("Consultar olimpiadas", 200, 30);
        createItinerary = new CustomButton("Crear nuevo itinerario", 200, 30);
        checkItinerary = new CustomButton("Consultar itinerarios", 200, 30);
        createExercise = new CustomButton("Crear nuevo ejercicio", 200, 30);
        checkExercises = new CustomButton("Consultar ejercicios", 200, 30);
        createTeam = new CustomButton("Crear nuevo equipo", 200, 30);
        checkTeams = new CustomButton("Consultar equipos", 200, 30);
        createRubrica = new CustomButton("Crear nueva rúbrica", 200, 30);
        checkRubricas = new CustomButton("Consultar rúbricas", 200, 30);
        assignExerciseToOlympiad = new CustomButton("Asignar ejercicio a olimpiada", 200, 30);
        checkAssignationExOlymp = new CustomButton("Consultar asignaciones de ejercicios a olimpiadas", 200, 30);

        olympiadButtonsPanel = new CustomPanel();
        olympiadButtonsPanel.setLayout(new GridLayout(6, 2, 15, 15));
        olympiadButtonsPanel.add(createOlympiad);
        olympiadButtonsPanel.add(checkOlympiads);
        olympiadButtonsPanel.add(createItinerary);
        olympiadButtonsPanel.add(checkItinerary);
        olympiadButtonsPanel.add(createExercise);
        olympiadButtonsPanel.add(checkExercises);
        olympiadButtonsPanel.add(createRubrica);
        olympiadButtonsPanel.add(checkRubricas);
        olympiadButtonsPanel.add(createTeam);
        olympiadButtonsPanel.add(checkTeams);
        olympiadButtonsPanel.add(assignExerciseToOlympiad);
        olympiadButtonsPanel.add(checkAssignationExOlymp);

        olympiadManagementLabel = new CustomSubtitleLabel("Gestión de olimpiadas");

        olympiadManagementPanel = new CustomPanel();
        olympiadManagementPanel.setLayout(new BorderLayout(5, 5));
        olympiadManagementPanel.add(olympiadManagementLabel, BorderLayout.NORTH);
        olympiadManagementPanel.add(olympiadButtonsPanel, BorderLayout.CENTER);

        return olympiadManagementPanel;
    }

    @Override
    protected CustomPanel createSouthPanel() {
        // Panel de gestión de usuarios
        createUser = new CustomButton("Crear nuevo usuario", 200, 30);
        checkUsers = new CustomButton("Consultar usuarios", 200, 30);
        assignExerciseToUser = new CustomButton("Asignar ejercicio a monitor", 200, 30);
        checkAssignationExMonitor = new CustomButton("Consultar asignaciones de ejercicios a monitores", 200, 30);
        assignItToOrg = new CustomButton("Asignar itinerario a organizador", 200, 30);
        checkAssignationItOrg = new CustomButton("Consultar asignaciones de itinerarios a organizadores", 200, 30);
        changePassword = new CustomButton("Cambiar contraseña", 200, 30);

        userButtonsPanel = new CustomPanel();
        userButtonsPanel.setLayout(new GridLayout(4, 2, 15, 15));
        userButtonsPanel.add(createUser);
        userButtonsPanel.add(checkUsers);
        userButtonsPanel.add(assignExerciseToUser);
        userButtonsPanel.add(checkAssignationExMonitor);
        userButtonsPanel.add(assignItToOrg);
        userButtonsPanel.add(checkAssignationItOrg);
        userButtonsPanel.add(changePassword);

        userManagementLabel = new CustomSubtitleLabel("Gestión de usuarios");

        userManagementPanel = new CustomPanel();
        userManagementPanel.setLayout(new BorderLayout());
        userManagementPanel.add(userManagementLabel, BorderLayout.NORTH);
        userManagementPanel.add(userButtonsPanel, BorderLayout.CENTER);

        return userManagementPanel;
    }
}
