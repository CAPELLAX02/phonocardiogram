package com.au.pc.ui.controller;

import com.au.pc.core.audio.AudioSource;
import com.au.pc.core.audio.MicrophoneSource;
import com.au.pc.core.audio.WavFileSource;
import com.au.pc.service.AudioService;
import com.au.pc.ui.view.WaveformCanvas;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;

public class MainController {

    @FXML private Canvas waveformCanvas;
    @FXML private Label statusLabel;

    private AudioService audioService;

    @FXML
    public void initialize() {
        WaveformCanvas canvasWrapper = new WaveformCanvas(waveformCanvas);
        audioService = new AudioService(canvasWrapper);
    }

    @FXML
    public void onLoadWav() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV Files", "*.wav"));
        File selected = chooser.showOpenDialog(null);

        if (selected != null) {
            AudioSource wavSource = new WavFileSource(selected);
            audioService.startWithSource(wavSource);
            statusLabel.setText("Playing file: " + selected.getName());
        }
    }

    @FXML
    public void onPlayWav() {
        // optional
    }

    @FXML
    public void onStartMic() {
        AudioSource micSource = new MicrophoneSource();
        audioService.startWithSource(micSource);
        statusLabel.setText("Live microphone input started.");
    }

    @FXML
    public void onStop() {
        audioService.stop();
        statusLabel.setText("Stopped.");
    }

}
