package com.au.pc.core.audio;

public interface AudioSource {

    void start() throws Exception;

    void stop();

    int readSamples(float[] dst);

    float getSampleRate();

}
