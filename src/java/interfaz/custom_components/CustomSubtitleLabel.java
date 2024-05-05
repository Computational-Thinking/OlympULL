package interfaz.custom_components;

public class CustomSubtitleLabel extends CustomLabel implements Borders, Fonts {
    public CustomSubtitleLabel(String text) {
        super(text, fuenteSubtitulo);
        this.setBorder(borde);
    }
}
