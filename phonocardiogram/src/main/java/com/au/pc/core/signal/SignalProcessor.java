package com.au.pc.core.signal;

import java.util.ArrayDeque;
import java.util.Deque;

public final class SignalProcessor {

    private static final double RMS_ALPHA  = 0.9;
    private static final double THRESH_K   = 3.0;
    private static final float  REFRACT_MS = 450f;
    private static final float  STALE_SEC  = 4f;
    private static final int    MAX_PEAKS  = 10;

    private final float sampleRate;
    private final long  refractorySamples;
    private final long  staleSamples;

    private final Deque<Long> peaks = new ArrayDeque<>(MAX_PEAKS);
    private long   samples = 0;
    private double rms     = 0;

    public SignalProcessor(float fs) {
        this.sampleRate        = fs;
        this.refractorySamples = (long) (REFRACT_MS * fs / 1_000f);
        this.staleSamples      = (long) (STALE_SEC  * fs);
    }

    public void analyze(float[] buf, int len) {

        /* adaptif RMS → eşik */
        double sum = 0;
        for (float v : buf) sum += v * v;
        rms = RMS_ALPHA * rms + (1 - RMS_ALPHA) * Math.sqrt(sum / len);
        double thr = rms * THRESH_K;

        for (int i = 0; i < len; i++, samples++) {
            if (Math.abs(buf[i]) > thr
                    && (peaks.isEmpty() || samples - peaks.getLast() > refractorySamples)) {

                peaks.addLast(samples);
                if (peaks.size() > MAX_PEAKS) peaks.removeFirst();
            }
        }
    }

    public double bpm() {

        if (peaks.size() < 2) return 0;

        long lastPeak = peaks.getLast();
        if (samples - lastPeak > staleSamples) {
            peaks.clear();
            return 0;
        }

        long prev = peaks.peekFirst();
        double sum = 0; int n = 0;
        for (long p : peaks) {
            if (p == prev) continue;
            sum += (p - prev);
            prev = p;
            n++;
        }
        if (n == 0) return 0;

        double periodSamples = sum / n;
        return 60.0 * sampleRate / periodSamples;
    }

    public void reset() {
        peaks.clear();
        samples = 0;
        rms     = 0;
    }
}
