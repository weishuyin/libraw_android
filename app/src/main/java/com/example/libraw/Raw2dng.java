package com.example.libraw;

public class Raw2dng {
    static {
        System.loadLibrary("raw2dngjni");
    }

    public static native void raw2dng(String rawFilename, String outFilename);
}
