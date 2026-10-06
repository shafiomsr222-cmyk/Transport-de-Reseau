package sae.poo;

import java.io.File;

public class EmptyCSV extends RuntimeException {
    private File file;

    public EmptyCSV() {
        super();
    }

    public EmptyCSV(String message) {
        super(message);
    }

    public EmptyCSV(String message, File file) {
        super(message);
        this.file = file;
    }

    public File getMauvaiseVille() {
        return this.file;
    }
}
