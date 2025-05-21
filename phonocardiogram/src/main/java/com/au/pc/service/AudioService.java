package com.au.pc.service;

import com.au.pc.core.audio.AudioSource;
import com.au.pc.ui.view.WaveformCanvas;
import javafx.application.Platform;

import java.util.concurrent.*;

public final class AudioService {

    private final WaveformCanvas canvas;
    private final SignalService signal;

    private ScheduledExecutorService exec;
    private AudioSource src;
    private float[] buf;

    public AudioService(WaveformCanvas canvas, SignalService signal) {
        this.canvas = canvas;
        this.signal = signal;
    }

    public void play(AudioSource source) {
        stop();
        this.src = source;
        try {
            src.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        signal.setSampleRate(src.getSampleRate());

        int fps   = 60;
        int batch = Math.max(1_024, (int) (src.getSampleRate() / fps));
        buf = new float[batch];

        long period = 1_000L / fps;
        exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            int n = src.readSamples(buf);
            if (n == -1) { stop(); return; }

            double sum = 0;
            for (int i = 0; i < n; i++) sum += buf[i] * buf[i];
            double rms = Math.sqrt(sum / n);

            if (rms > 2e-4) {
                signal.accept(buf, n);

                float[] copy = new float[n];
                System.arraycopy(buf, 0, copy, 0, n);
                Platform.runLater(() -> canvas.push(copy, n));
            } else {
                Platform.runLater(canvas::pushSilence);
            }
        }, 0, period, TimeUnit.MILLISECONDS);

    }

    public void stop() {
        if (exec != null) {
            exec.shutdownNow();
            exec = null;
        }
        if (src != null) {
            src.stop();
            src = null;
        }
        Platform.runLater(() -> {
            canvas.clear();
            signal.reset();
        });
    }
}
