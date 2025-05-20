package com.au.pc.ui.util;

import javafx.application.Platform;

public class UiUtils {
    public static void runLater(Runnable task) {
        if (Platform.isFxApplicationThread()) {
            task.run();
        } else {
            Platform.runLater(task);
        }
    }
}
