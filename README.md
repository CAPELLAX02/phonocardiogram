# Phonocardiogram Monitor

Phonocardiogram Monitor is a lightweight desktop tool that visualises heart‑sound signals and estimates the heart‑rate (BPM) in real‑time.  
It accepts **live microphone input** or **PCM WAV** files and displays a continuously scrolling phonocardiogram similar to a clinical bedside monitor.

---

## Key Features

| Feature | Description |
|---------|-------------|
| **Live capture** | Opens the first available audio input line (44.1 kHz or 48 kHz, mono or stereo) and starts processing immediately. |
| **WAV playback** | 16‑bit / 24‑bit PCM WAV files are decoded and analysed while they play. |
| **Adaptive waveform gain** | Automatic gain control keeps quiet recordings visible and prevents clipping on loud ones. |
| **Real‑time BPM** | Peak detector with adaptive threshold, 350 ms refractory window and 4 s silence reset; stable within ±3 BPM on clean recordings. |
| **Clear UI** | Scrollable waveform canvas, separate status and BPM labels, buttons for Load WAV, Mic/Pause, Play/Pause and Stop. |
| **Pure Java** | Java 17 + JavaFX 24, Java‑Sound only – no native DSP libraries or platform‑specific code. |

---

## Requirements

* **JDK 17 or newer**  
  (JavaFX 24 requires JDK 22; JavaFX 21 works on JDK 17. Adjust the versions in `pom.xml` accordingly.)
* A microphone or an electronic stethoscope plugged into the computer’s input jack.

---

## Building & Running

```bash
# compile and package
mvn clean package

# run the application
java   --module-path $PATH_TO_FX/lib   --add-modules javafx.controls,javafx.fxml,javafx.media   -jar target/phonocardiogram-1.0-SNAPSHOT.jar
```

For development convenience you can also start the GUI with:

```bash
mvn javafx:run
```

---

## Project Structure

```
src
 └─ main
     ├─ java
     │   └─ com.au.pc
     │        ├─ PhonocardiogramApplication  (JavaFX entry point)
     │        ├─ ui
     │        │    ├─ controller.MainController
     │        │    ├─ view.WaveformCanvas
     │        │    └─ util.UiUtils
     │        ├─ service
     │        │    ├─ AudioService
     │        │    └─ SignalService
     │        └─ core
     │             ├─ audio  (AudioSource interface + Mic / WAV implementations)
     │             └─ signal (SignalProcessor peak/BPM logic)
     └─ resources
          └─ ui/main.fxml
```

---

## How the BPM Algorithm Works

1. **RMS envelope** – a 90 % IIR filter tracks signal RMS, providing a local energy estimate.  
2. **Adaptive threshold** – peaks are detected when |sample| > RMS × 2.0.  
3. **Refractory window** – subsequent peaks within 350 ms are ignored to prevent counting both S1 and S2 sounds.  
4. **Sliding window** – only the last 10 peaks (≈ 8 s) are kept; after­ 4 s of silence the BPM resets to 0.  
5. **BPM = 60·fs / mean‑interval** of those remaining peaks.

---

## Typical Workflow

1. Connect the stethoscope or select a WAV recording.  
2. Press **Mic / Pause** for live monitoring **or** **Load WAV → Play** for file analysis.  
3. Observe the waveform and BPM read‑out.  
4. Press **Stop** to end the current session.


