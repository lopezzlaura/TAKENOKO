package com.polytech.bdsm.takenoko.gui;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class LoggerOutput extends OutputStream {

    private Optional<TextArea> textArea = Optional.empty();

    public void setTextArea(TextArea textArea) {
        this.textArea = Optional.of(textArea);
    }

    public Optional<TextArea> getTextArea() {
        return textArea;
    }

    @Override
    public void write(int b) throws IOException {
        Platform.runLater(() -> textArea.ifPresent(textArea -> textArea.appendText(String.valueOf((char) b))));
    }
}
