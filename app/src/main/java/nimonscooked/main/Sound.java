package nimonscooked.main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import java.net.URL;
import java.util.ArrayList;

public class Sound {
    public static final int CHOP = 0;
    public static final int FRY = 1;
    public static final int GAME_OVER = 2;
    public static final int LEVEL = 3;
    public static final int LOADING = 4;
    public static final int PLAYING = 5;
    public static final int TITLE = 6;
    public static final int VICTORY = 7;
    public static final int WASHING = 8;

    Clip clip;
    ArrayList<URL> soundURL = new ArrayList<>();
    FloatControl volumeControl;
    private float currentVolume = 0.8f;

    public Sound(){
        soundURL.add(getClass().getResource("/sound/chopping.wav")); // 0
        soundURL.add(getClass().getResource("/sound/frying.wav")); // 1
        soundURL.add(getClass().getResource("/sound/gameover.wav")); // 2
        soundURL.add(getClass().getResource("/sound/level.wav")); // 3
        soundURL.add(getClass().getResource("/sound/loading.wav")); // 4
        soundURL.add(getClass().getResource("/sound/playing.wav")); // 5
        soundURL.add(getClass().getResource("/sound/title.wav")); // 6
        soundURL.add(getClass().getResource("/sound/victory.wav")); // 7
        soundURL.add(getClass().getResource("/sound/washing.wav")); // 8
    }

    // --- METODE KUNCI YANG DIREVISI: Mencegah sound doubling dan NullPointerException ---
    public void setFile(int i) {
        try {
            // 1. Pastikan clip yang lama dihentikan dan ditutup
            if (clip != null && clip.isOpen()) {
                clip.stop();
                clip.close();
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL.get(i));
            clip = AudioSystem.getClip();
            clip.open(ais);

            // 2. Inisialisasi volume control
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                setVolume(currentVolume);
            } else {
                volumeControl = null;
                System.out.println("[SOUND] Master Gain control is not supported for this audio line.");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void play() {
        if (clip != null) {
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        // --- DIREVISI: Memperbaiki NullPointerException ---
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void setVolume(float volume) {

        volume = Math.max(0.0f, Math.min(1.0f, volume));
        this.currentVolume = volume;

        if (volumeControl != null) {

            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();

            if (volume == 0.0f) {
                volumeControl.setValue(min);
            } else {
                // Menggunakan mapping linear (yang Anda gunakan)
                float gain = min + (max - min) * volume;
                volumeControl.setValue(gain);
            }

            System.out.println(
                    "[SOUND] Volume set to: " + (volume * 100) + "% (Gain: " + volumeControl.getValue() + " dB)");
        }
    }

    public float getVolume() {
        return currentVolume;
    }

    public void increaseVolume(float amount) {
        setVolume(currentVolume + amount);
    }

    public void decreaseVolume(float amount) {
        setVolume(currentVolume - amount);
    }

    public void mute() {
        setVolume(0.0f);
    }

    public void unmute() {
        if (currentVolume == 0.0f) {
            setVolume(0.8f);
        }
    }

    public void playSE(int index) {
        setFile(index);
        if (clip != null) {
            clip.setFramePosition(0); // Memastikan SE mulai dari awal
            play();
        }
    }

    public void playMusic(int index) {
        setFile(index);
        if (clip != null) {
            loop();
        }
    }
}