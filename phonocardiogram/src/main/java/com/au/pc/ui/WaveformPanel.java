package com.au.pc.ui;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WaveformPanel extends JPanel {
    private byte[] audioBytes;

    public void loadAudioFile(File file) {
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(file)) {
            audioBytes = audioStream.readAllBytes();
            repaint();
        } catch (UnsupportedAudioFileException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (audioBytes == null || audioBytes.length == 0) return;

        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, w, h);
        g2.setColor(Color.GREEN);

        int centerY = h / 2;
        for (int i = 0; i < w; i++) {
            int idx = i * audioBytes.length / w;
            int sample = audioBytes[idx];
            g2.drawLine(i, centerY, i, centerY - sample / 2);
        }
    }
}