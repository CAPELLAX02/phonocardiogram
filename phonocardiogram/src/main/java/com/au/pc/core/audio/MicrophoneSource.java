package com.au.pc.core.audio;

import javax.sound.sampled.*;

public final class MicrophoneSource implements AudioSource {

    private TargetDataLine line;
    private AudioFormat    format;

    @Override
    public void start() throws LineUnavailableException {
        format = new AudioFormat(44_100f, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();
    }

    @Override
    public void stop() {
        if (line != null) {
            line.stop();
            line.close();
        }
    }

    @Override
    public float getSampleRate() {
        return format.getSampleRate();
    }

    @Override
    public int readSamples(float[] buffer) {
        if (line == null) {
            return -1;
        }
        byte[] bytes = new byte[buffer.length * 2];
        int bytesRead = line.read(bytes, 0, bytes.length);
        if (bytesRead <= 0) {
            return -1;
        }
        for (int i = 0; i < bytesRead / 2; i++) {
            int lo = bytes[2 * i] & 0xFF;
            int hi = bytes[2 * i + 1] << 8;
            buffer[i] = (short) (hi | lo) / 32_768f;
        }
        return bytesRead / 2;
    }
}
