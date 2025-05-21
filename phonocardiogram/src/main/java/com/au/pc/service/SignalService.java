package com.au.pc.service;

import com.au.pc.core.signal.SignalProcessor;
import com.au.pc.ui.util.UiUtils;
import javafx.scene.control.Label;

public final class SignalService {

    private SignalProcessor proc;
    private final Label bpmLabel;
    private float fs = 44_100f;

    public SignalService(Label bpmLabel) {
        this.bpmLabel = bpmLabel;
        proc = new SignalProcessor(fs);
    }

    public void setSampleRate(float sampleRate) {
        this.fs  = sampleRate;
        proc = new SignalProcessor(fs);
        bpmLabel.setText("BPM: –");
    }

    public void accept(float[] data, int len) {
        proc.analyze(data, len);
        double bpm = proc.bpm();
        UiUtils.runLater(() -> bpmLabel.setText(String.format("BPM: %.1f", bpm)));
    }

    public void reset() {
        proc.reset();
        bpmLabel.setText("BPM: –");
    }
}
