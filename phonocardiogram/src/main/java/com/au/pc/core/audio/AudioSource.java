package com.au.pc.core.audio;

public interface AudioSource {

    /**
     * Starts reading audio data.
     */
    void start();

    /**
     * Stops reading audio data.
     */
    void stop();

    /**
     * Returns the next amplitude sample in the range [-1.0, 1.0].
     * Returns Double.NaN if the stream has ended or failed.
     */
    double readNextSample();

}
