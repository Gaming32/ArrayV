package resources;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import panes.JErrorPane;

public abstract class Fetcher {
    private BufferedInputStream bufferedStream;
    protected String defaultStream;
    
    public Fetcher(String defaultStream) {
        this.defaultStream = defaultStream;
        
        try {
            InputStream input = this.getClass().getResourceAsStream(this.defaultStream);
            this.bufferedStream = new BufferedInputStream(input);
        }
        catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
        }
    }
    
    public Fetcher(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            this.bufferedStream = new BufferedInputStream(stream);
        }
        catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
        }
    }
    
    public BufferedInputStream getStream() {
        return this.bufferedStream;
    }
}