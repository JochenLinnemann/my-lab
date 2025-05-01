package de.jochenlinnemann.javalib;

import java.lang.annotation.Native;

public class API {
    public API() {
        super();
    }

    public int getInt() {
        return 42;
    }
    public String getString() {
        return "Hello world!";
    }

    public native int getNativeInt();
}
