package org.example.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private static final String FILE_NAME = "log.txt";

    public static synchronized void log(String message) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))){
            writer.write(message);
            writer.newLine();
        }catch (IOException e) {
            System.out.println("Eroare la screre in fisier: " + e.getMessage());
        }
    }
    public static void clearLog() {
        try(BufferedWriter writer =new BufferedWriter(new FileWriter(FILE_NAME))){
            writer.write("");
        }catch (IOException e) {
            System.out.println("Eroare la curatare: " + e.getMessage());
        }
    }
}
