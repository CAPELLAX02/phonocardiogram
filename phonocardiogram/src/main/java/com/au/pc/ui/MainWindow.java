package com.au.pc.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class MainWindow extends JFrame {
    private final WaveformPanel waveformPanel = new WaveformPanel();

    public MainWindow() {
        setTitle("Phonocardiogram Monitoring");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton loadButton = new JButton("Load WAV File");
        loadButton.addActionListener(this::onLoad);

        JPanel topPanel = new JPanel();
        topPanel.add(loadButton);

        add(topPanel, BorderLayout.NORTH);
        add(waveformPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void onLoad(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            waveformPanel.loadAudioFile(file);
        }
    }
}
