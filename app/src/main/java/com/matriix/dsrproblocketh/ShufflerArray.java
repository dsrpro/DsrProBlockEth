package com.matriix.dsrproblocketh;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class ShufflerArray {
    public static String textVal = "d4f6047d9f3d161e3cb1e302fc66ca13cf24f0d3be907359577e2cb7905cecae";
    public static String shuffleArrayHold(String text) {
        List<Character> characters = new ArrayList<>();
        for (char c : text.toCharArray()) {
            characters.add(c);
        }

        Collections.shuffle(characters);

        StringBuilder shuffledText = new StringBuilder();
        for (char c : characters) {
            shuffledText.append(c);
        }

        return shuffledText.toString();
    }

/*    public static void main(String[] args) {
        String textVal = "d4f6047d9f3d161e3cb1e302fc66ca13cf24f0d3be907359577e2cb7905cecae";

        String textResult = shuffleArray(textVal);

        System.out.println(textResult);

        Random random = new Random();
        int k = 1;
        do {
            System.out.println(shuffleArray(textVal)+";");
            k++;
        } while (k <= 10000000);
    }*/
public static void main(String[] args) {
    try{

        String textVal = "d4f6047d9f3d161e3cb1e302fc66ca13cf24f0d3be907359577e2cb7905cecae";

        String textResult = shuffleArray(textVal);

        System.out.println(textResult);

        String[] shuffledArray = generateShuffledArray(textVal, 10);

        // Écrire les éléments du tableau dans le fichier shufflerBuild
        writeToFile("shufflerBuild", shuffledArray);

        System.out.println("Fichier shufflerBuild généré.");
    }catch (Exception e){
        e.printStackTrace();
    }

}

    private static String shuffleArray(String textVal) {
        char[] array = textVal.toCharArray();
        Random random = new Random();
try{

}catch (Exception e){
    e.printStackTrace();
}
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        return new String(array);
    }

    static String[] generateShuffledArray(String textVal, int size) {
        try{

        }catch (Exception e){
            e.printStackTrace();
        }

        String[] shuffledArray = new String[size];
        shuffledArray[0] = shuffleArray(textVal);

        for (int i = 1; i < size; i++) {
            shuffledArray[i] = shuffleArray(textVal) + ";";
            System.out.println(shuffledArray[i]);
        }

        return shuffledArray;
    }

    private static void writeToFile(String fileName, String[] lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

