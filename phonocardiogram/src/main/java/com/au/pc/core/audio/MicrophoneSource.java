package com.au.pc.core.audio;

import javax.sound.sampled.*;

public class MicrophoneSource implements AudioSource {

    private TargetDataLine line;
    private AudioFormat format;
    private byte[] buffer;

    @Override
    public void start() {
        try {
            format = new AudioFormat(44100.0f, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            buffer = new byte[2];
        } catch (LineUnavailableException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void stop() {
        if (line != null) {
            line.stop();
            line.close();
        }
    }

    @Override
    public double readNextSample() {
        int bytesRead = line.read(buffer, 0, buffer.length);
        if (bytesRead != 2) return Double.NaN;
        int sample = (buffer[1] << 8) | (buffer[0] & 0xFF);
        return sample / 32768.0;
    }
}
