package org.arg.engine.audio;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public enum SoundEffect {
    ENEMYSHOOT("/enemyshoot.mp3"),
    PLAYERSHOOT("/playershoot.mp3"),
    ENEMYCOLLISION("/enemyexplosion.mp3"),
    PLAYERCOLLISION("/playercollision.mp3");


    private final String rutaArchivo;
    private byte[] audioData;

    private SoundEffect(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.loadInMemorory();
    }
    private void loadInMemorory() {
        try (InputStream is = getClass().getResourceAsStream(rutaArchivo)) {
            if (is != null) {

                this.audioData = is.readAllBytes();
            } else {
                System.err.println("No se pudo precargar el audio: " + rutaArchivo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Devuelve un nuevo stream rápido que lee directamente de la RAM
    public InputStream getInputStream() {
        return new ByteArrayInputStream(audioData);
    }
    public String getRutaArchivo() {
        return rutaArchivo;
    }
}
