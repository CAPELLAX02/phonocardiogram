package com.au.pc.core.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;

public class WavFileSource implements AudioSource {

    private final File wavFile;
    private AudioInputStream inputStream;
    private AudioFormat format;
    private byte[] buffer;

    public WavFileSource(File wavFile) {
        this.wavFile = wavFile;
    }

    @Override
    public void start() {
        try {
            inputStream = AudioSystem.getAudioInputStream(wavFile);
            format = inputStream.getFormat();
            buffer = new byte[format.getFrameSize()];
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void stop() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException ignored) {
            System.out.println(ignored.getMessage());
        }
    }

    @Override
    public double readNextSample() {
        try {
            int bytesRead = inputStream.read(buffer);
            if (bytesRead == -1) return Double.NaN;

            int sample = (buffer[1] << 8) | (buffer[0] & 0xFF);
            return sample / 32768.0;
        } catch (IOException e) {
            return Double.NaN;
        }
    }

}
