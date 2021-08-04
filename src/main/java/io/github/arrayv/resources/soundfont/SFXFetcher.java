package io.github.arrayv.resources.soundfont;

import java.io.File;

import io.github.arrayv.resources.Fetcher;

final public class SFXFetcher extends Fetcher {
    public SFXFetcher() {
        super("sfx.sf2");
    }
    
    public SFXFetcher(File file) {
        super(file);
    }
}