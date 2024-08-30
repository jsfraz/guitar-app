package cz.josefraz;

import com.formdev.flatlaf.FlatDarkLaf;

import cz.josefraz.frames.GuitarApp;

public class Main {
    public static void main(String[] args) {
        System.out.println("Staring G.U.I.T.A.R - Graphical Universal Interface for Tone and Audio Reproduction");

        // TODO auto theme
        FlatDarkLaf.setup();
        System.out.println("Dark theme set.");

        new GuitarApp();
    }
}