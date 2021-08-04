package io.github.arrayv.resources;

import java.io.File;

final public class SFXFetcher extends Fetcher {
    public SFXFetcher() {
        super("sfx.sf2");
    }
    
    public SFXFetcher(File file) {
        super(file);
    }
}