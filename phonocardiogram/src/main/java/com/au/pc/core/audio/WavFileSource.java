package com.au.pc.core.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public final class WavFileSource implements AudioSource {

    private final File file;
    private AudioInputStream in;
    private SourceDataLine out;
    private AudioFormat format;

    public WavFileSource(File file) {
        this.file = file;
    }

    @Override
    public void start() throws Exception {
        AudioInputStream raw = AudioSystem.getAudioInputStream(file);
        AudioFormat src = raw.getFormat();

        format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                src.getSampleRate(),
                16,
                1,
                2,
                src.getSampleRate(),
                false);

        in = AudioSystem.getAudioInputStream(format, raw);

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        out = (SourceDataLine) AudioSystem.getLine(info);
        out.open(format);
        out.start();
    }

    @Override
    public void stop() {
        try {
            if (out != null) {
                out.drain();
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException ignored) {
        }
    }

    @Override
    public float getSampleRate() {
        return format.getSampleRate();
    }

    @Override
    public int readSamples(float[] buffer) {
        byte[] bytes = new byte[buffer.length * 2];
        int bytesRead;
        try {
            bytesRead = in.read(bytes);
        } catch (IOException e) {
            return -1;
        }
        if (bytesRead == -1) {
            return -1;
        }
        out.write(bytes, 0, bytesRead);
        for (int i = 0; i < bytesRead / 2; i++) {
            int lo = bytes[2 * i] & 0xFF;
            int hi = bytes[2 * i + 1] << 8;
            buffer[i] = (short) (hi | lo) / 32_768f;
        }
        return bytesRead / 2;
    }
}
