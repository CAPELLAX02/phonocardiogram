package com.au.pc.service;

import com.au.pc.core.audio.AudioSource;
import com.au.pc.ui.view.WaveformCanvas;
import javafx.application.Platform;

public class AudioService {

    private final WaveformCanvas waveformCanvas;
    private AudioSource currentSource;
    private Thread readThread;
    private volatile boolean running = false;

    public AudioService(WaveformCanvas waveformCanvas) {
        this.waveformCanvas = waveformCanvas;
    }

    public void startWithSource(AudioSource source) {
        stop();

        this.currentSource = source;
        currentSource.start();
        running = true;

        readThread = new Thread(() -> {
            while (running) {
                double sample = currentSource.readNextSample();
                if (Double.isNaN(sample)) break;

                double amplitude = Math.max(-1.0, Math.min(1.0, sample));

                Platform.runLater(() -> waveformCanvas.draw(amplitude));

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        readThread.setDaemon(true);
        readThread.start();
    }

    public void stop() {
        running = false;

        if (readThread != null) {
            readThread.interrupt();
            readThread = null;
        }

        if (currentSource != null) {
            currentSource.stop();
            currentSource = null;
        }

        Platform.runLater(waveformCanvas::clear);
    }

}
