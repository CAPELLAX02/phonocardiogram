package com.au.pc.ui.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;

public final class WaveformCanvas {

    private static final int ADVANCE_PER_FRAME = 4;
    private static final float GAIN = 8f;
    private static final float SMOOTH = 0.35f;

    private final Canvas canvas;
    private final GraphicsContext g;

    private final float[] trace;
    private int writeX;

    public WaveformCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.g = canvas.getGraphicsContext2D();
        this.trace = new float[(int) canvas.getWidth()];
        initGraphics();
    }

    public void push(float[] batch, int len) {
        float peak = 0f;
        for (int i = 0; i < len; i++) {
            float v = Math.abs(batch[i]);
            if (v > peak) peak = v;
        }
        peak = Math.min(peak * GAIN, 1f);        // GAIN ↑
        for (int i = 0; i < ADVANCE_PER_FRAME; i++) {
            trace[writeX] = trace[writeX] * SMOOTH + peak * (1f - SMOOTH);
            writeX = (writeX + 1) % trace.length;
        }
        draw();
    }

    public void pushSilence() {
        for (int i = 0; i < ADVANCE_PER_FRAME; i++) {
            trace[writeX] = trace[writeX] * SMOOTH;
            writeX = (writeX + 1) % trace.length;
        }
        draw();
    }

    public void clear() {
        Arrays.fill(trace, 0f);
        writeX = 0;
        draw();
    }

    private void initGraphics() {
        canvas.setFocusTraversable(false);
        g.setLineWidth(1.2);
        g.setStroke(Color.LIME);
        g.setFill(Color.BLACK);
        g.setGlobalAlpha(1.0);
    }

    private void draw() {
        int    w   = trace.length;
        int    h   = (int) canvas.getHeight();
        double mid = h / 2.0;

        g.setFill(Color.BLACK);
        g.fillRect(0, 0, w, h);

        g.setStroke(Color.LIME);
        g.setLineWidth(1.2);

        double[] xs = new double[w];
        double[] ys = new double[w];
        for (int i = 0; i < w; i++) {
            int idx   = (writeX + i) % w;
            float v   = trace[idx];
            xs[i] = i;
            ys[i] = mid - v * mid;
        }
        g.strokePolyline(xs, ys, w);
    }

    @SuppressWarnings("unused")
    private void drawGrid(int w, int h, double mid) {
        g.setStroke(Color.web("#202020"));
        for (int y = 0; y < h; y += 10) {
            g.setLineWidth((y % 50 == 0) ? 1.0 : 0.3);
            g.strokeLine(0, y, w, y);
        }
        g.setLineWidth(1.0);
        g.setStroke(Color.web("#009900"));
        g.strokeLine(0, mid, w, mid);
    }
}
