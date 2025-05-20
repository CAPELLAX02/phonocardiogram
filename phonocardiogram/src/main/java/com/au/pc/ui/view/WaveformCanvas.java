package com.au.pc.ui.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class WaveformCanvas {

    private final Canvas canvas;
    private final GraphicsContext gc;

    public WaveformCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void draw(double amplitude) {
        double height = canvas.getHeight();
        double centerY = height / 2;
        double mapped = amplitude * (height / 2);

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), height);

        gc.setStroke(Color.LIMEGREEN);
        gc.strokeLine(0, centerY, canvas.getWidth(), centerY);

        gc.setStroke(Color.RED);
        gc.strokeLine(0, centerY, canvas.getWidth(), centerY - mapped);
    }

    public void clear() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

}
