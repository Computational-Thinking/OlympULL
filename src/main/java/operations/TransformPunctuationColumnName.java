package operations;

public interface TransformPunctuationColumnName {
    public default String transformColumnName(String concept) {
        String punctuationColumn = null;

        switch(concept) {
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

        return punctuationColumn;
    }
}
