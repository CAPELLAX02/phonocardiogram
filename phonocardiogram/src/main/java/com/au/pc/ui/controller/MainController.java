package com.au.pc.ui.controller;

import com.au.pc.core.audio.*;
import com.au.pc.service.*;
import com.au.pc.ui.view.WaveformCanvas;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;

public final class MainController {

    /* UI refs */
    @FXML private Canvas waveformCanvas;
    @FXML private Label  statusOut;
    @FXML private Label  bpmOut;
    @FXML private Button toggleBtn;

    /* Services */
    private AudioService  audio;
    private SignalService signal;

    /* State */
    private File    wavFile;
    private boolean filePlaying;
    private boolean micPlaying;

    @FXML
    public void initialize() {
        WaveformCanvas view = new WaveformCanvas(waveformCanvas);
        signal             = new SignalService(bpmOut);
        audio              = new AudioService(view, signal);

        statusOut.setText("Ready");
        bpmOut.setText("BPM: –");
    }

    /* ──────────────── UI callbacks ───────────────────────────────── */

    @FXML
    public void onLoadWav() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("WAV", "*.wav"));
        File f = fc.showOpenDialog(waveformCanvas.getScene().getWindow());
        if (f != null) {
            wavFile = f;
            filePlaying = false;
            toggleBtn.setText("Play");
            statusOut.setText("Loaded: " + f.getName());
        }
    }

    @FXML
    public void onPlayWav() {
        if (wavFile == null) return;
        if (!filePlaying) {
            audio.play(new WavFileSource(wavFile));
            filePlaying = true; micPlaying = false;
            toggleBtn.setText("Pause");
            statusOut.setText("Playing: " + wavFile.getName());
        } else stopAll();
    }

    @FXML
    public void onStartMic() {
        if (!micPlaying) {
            audio.play(new MicrophoneSource());
            micPlaying = true; filePlaying = false;
            toggleBtn.setText("Pause");
            statusOut.setText("Microphone live");
        } else stopAll();
    }

    @FXML
    public void onStop() {
        stopAll();
    }

    /* ─────────────── helpers ─────────────────────────────────────── */

    private void stopAll() {
        audio.stop();
        filePlaying = micPlaying = false;
        toggleBtn.setText("Play");
        statusOut.setText("Stopped");
    }
}
