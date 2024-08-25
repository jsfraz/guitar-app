package cz.josefraz;

import com.formdev.flatlaf.FlatDarkLaf;

import cz.josefraz.frames.GuitarApp;

public class Main {
    public static void main(String[] args) {
        // TODO auto theme
        FlatDarkLaf.setup();
        new GuitarApp();
    }
}