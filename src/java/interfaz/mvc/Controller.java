package interfaz.mvc;

import interfaz.custom_components.CustomQuickSort;
import interfaz.custom_components.Fonts;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import interfaz.custom_components.CustomComboBox;
import interfaz.custom_components.ErrorJOptionPane;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller implements CustomQuickSort, Fonts {
    // Filters
    CustomComboBox olympiadComboBox;
    CustomComboBox itineraryComboBox;
    CustomComboBox conceptComboBox;

    // Otros
    ArrayList<String> teams;
    ArrayList<Integer> punctuations;
    ArrayList<String> exerciseTitles;

    // Model
    Model model;

    public Controller() {
        model = new Model();

        olympiadComboBox = new CustomComboBox();
        olympiadComboBox.addItem("");
        itineraryComboBox = new CustomComboBox();
        itineraryComboBox.addItem("");
        conceptComboBox = new CustomComboBox();
        conceptComboBox.addItem("");

        ArrayList<String> itineraryCodes = new ArrayList<>();
        exerciseTitles = new ArrayList<>();

        try {
            ResultSet data = model.selectOlympiads();
            while (data.next()) {
                olympiadComboBox.addItem(data.getString("TITULO"));
            }
            data.close();

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }

        olympiadComboBox.addActionListener(e -> {
            itineraryComboBox.removeAllItems();
            conceptComboBox.removeAllItems();
            itineraryCodes.clear();

            itineraryComboBox.addItem("");

            try {
                ResultSet data = model.selectItineraries((String) olympiadComboBox.getSelectedItem());
                while (data.next()) {
                    itineraryComboBox.addItem(data.getString("TITULO"));
                    itineraryCodes.add(data.getString("CODIGO"));
                }
                data.close();

            } catch (SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }
        });

        itineraryComboBox.addActionListener(e -> {
            conceptComboBox.removeAllItems();
            exerciseTitles.clear();

            conceptComboBox.addItem("");

            try {
                String selectedOlymp = (String) olympiadComboBox.getSelectedItem();
                String selectedIt = (String) itineraryComboBox.getSelectedItem();
                ResultSet data = model.selectExercises(selectedOlymp, selectedIt);
                while (data.next()) {
                    conceptComboBox.addItem(data.getString("CONCEPTO"));
                    exerciseTitles.add(data.getString("TITULO"));
                }
                data.close();

            } catch (SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }
        });

        conceptComboBox.addActionListener(e -> {
            if (itineraryComboBox.getSelectedItem() != null && itineraryComboBox.getSelectedItem() != "" &&
                    conceptComboBox.getSelectedItem() != null && conceptComboBox.getSelectedItem() != "") {
                String concept = (String) conceptComboBox.getSelectedItem();
                String punctuationColumn = null;

                switch (concept) {
                    case "ABSTRACCION" -> punctuationColumn = "P_ABSTRACCION";
                    case "ALGORITMOS" -> punctuationColumn = "P_ALGORITMOS";
                    case "BUCLES" -> punctuationColumn = "P_BUCLES";
                    case "CONDICIONALES" -> punctuationColumn = "P_CONDICIONALES";
                    case "DESCOMPOSICION" -> punctuationColumn = "P_DESCOMPOSICION";
                    case "FUNCIONES" -> punctuationColumn = "P_FUNCIONES";
                    case "IA" -> punctuationColumn = "P_IA";
                    case "RECONOCIMIENTO DE PATRONES" -> punctuationColumn = "P_REC_PATRONES";
                    case "SECUENCIAS" -> punctuationColumn = "P_SECUENCIAS";
                    case "SECUENCIAS Y BUCLES" -> punctuationColumn = "P_SECUENCIAS_Y_BUCLES";
                    case "VARIABLES" -> punctuationColumn = "P_VARIABLES";
                    case "VARIABLES Y FUNCIONES" -> punctuationColumn = "P_VARIABLES_Y_FUNC";
                    case "OTROS" -> punctuationColumn = "P_OTROS";
                }

                String itineraryCode = itineraryCodes.get(itineraryComboBox.getSelectedIndex() - 1);
                ResultSet data = model.selectTeams(itineraryCode, punctuationColumn);

                teams = new ArrayList<>();
                punctuations = new ArrayList<>();

                try {
                    while (data.next()) {
                        if (data.getString(punctuationColumn) != null) {
                            teams.add(data.getString("NOMBRE"));
                            punctuations.add(Integer.valueOf(data.getString(punctuationColumn)));
                        }
                    }

                    data.close();

                } catch (SQLException ex) {
                    new ErrorJOptionPane(ex.getMessage());
                }
            }
        });
    }

    public CustomComboBox getOlympiadComboBox() {
        return olympiadComboBox;
    }

    public CustomComboBox getItineraryComboBox() {
        return itineraryComboBox;
    }

    public CustomComboBox getConceptComboBox() {
        return conceptComboBox;
    }

    public JFreeChart generateRanking() {
        // Se ordenan los equipos de mayor a menor puntuación
        CustomQuickSort.sort(punctuations, teams);

        // Se crea el conjunto de datos
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < teams.size(); i++) {
            dataset.addValue(punctuations.get(i), teams.get(i), "");
        }

        // Se crea la gráfica
        JFreeChart chart = ChartFactory.createBarChart(exerciseTitles.get(conceptComboBox.getSelectedIndex() - 1),
                "Equipo", "Puntos", dataset,
                PlotOrientation.VERTICAL, false, false, false);

        // Se establecen los números mínimo y máximo del eje Y y que no haya decimales
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.getCategoryPlot().getRangeAxis().setLowerBound(0);
        chart.getCategoryPlot().getRangeAxis().setUpperBound(10.5);

        // Fuentes de texto
        // Título
        chart.getTitle().setFont(fuenteSubtitulo);
        // Etiquetas de las categorías del eje X
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelFont(fuenteBotonesEtiquetas);
        domainAxis.setTickLabelsVisible(true);
        // Etiquetas de las categorías del eje Y
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelFont(fuenteCampoTexto);
        rangeAxis.setTickLabelsVisible(true);
        // Fuente de etiquetas de barras y leyenda
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDefaultItemLabelFont(fuenteCampoTexto);
        renderer.setLegendTextFont(0, fuenteCampoTexto);

        return chart;
    }
}
