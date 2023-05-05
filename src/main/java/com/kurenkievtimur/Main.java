package com.kurenkievtimur;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static com.kurenkievtimur.AgeGroup.getGroupByScore;

public class Main {
    private final static double AUTOMATED_READABILITY_0_5 = 0.5;
    private final static double AUTOMATED_READABILITY_4_71 = 4.71;
    private final static double AUTOMATED_READABILITY_21_43 = 21.43;
    private final static double FLESCH_KINCAID_READABILITY_0_39 = 0.39;
    private final static double FLESCH_KINCAID_READABILITY_11_8 = 11.8;
    private final static double FLESCH_KINCAID_READABILITY_15_59 = 15.59;
    private final static double SMOG_1_043 = 1.043;
    private final static double SMOG_30 = 30.0;
    private final static double SMOG_3_1291 = 3.1291;
    private final static double COLEMAN_LIAU_0_588 = 0.0588;
    private final static double COLEMAN_LIAU_0_296 = 0.296;
    private final static double COLEMAN_LIAU_15_8 = 15.8;
    private final static double COLEMAN_LIAU_100 = 100;


    public static void main(String[] args) {
        File file = new File(args[0]);
        printInfoText(file);
    }

    public static void printInfoText(File file) {
        try (Scanner scanner = new Scanner(file)) {
            System.out.println("The text is:");
            String text = scanner.nextLine();

            double words = countWord(text);
            double sentences = countSentence(text);
            double characters = countCharacters(text);
            double syllables = countSyllables(text.split("\\s"));
            double polysyllables = countPolysyllables(text.split("\\s"));

            System.out.printf("%s\n\n", text);

            System.out.printf("Words: %.0f\n", words);
            System.out.printf("Sentences: %.0f\n", sentences);
            System.out.printf("Characters: %.0f\n", characters);
            System.out.printf("Syllables: %.0f\n", syllables);
            System.out.printf("Polysyllables: %.0f\n", polysyllables);

            chooseReadabilityScore(words, sentences, characters, syllables, polysyllables);
        } catch (FileNotFoundException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private static double countWord(String text) {
        return text.split("\\s").length;
    }

    private static double countSentence(String text) {
        return text.split("[.?!]").length;
    }

    private static double countCharacters(String text) {
        return text.replaceAll("\\s", "").split("").length;
    }

    private static double countSyllables(String[] words) {
        int syllables = 0;

        for (String word : words) {
            syllables += word.toLowerCase()
                    .replaceAll("e$", "")
                    .replaceAll("[aeiouy]{2}", "a")
                    .replaceAll("[^aeiouy]", "")
                    .length();
        }

        return syllables;
    }

    private static double countPolysyllables(String[] words) {
        int polysyllables = 0;

        for (String word : words) {
            int syllables = word.toLowerCase()
                    .replaceAll("e$", "")
                    .replaceAll("[aeiouy]{2}", "a")
                    .replaceAll("[^aeiouy]", "")
                    .length();

            if (syllables > 2) {
                polysyllables++;
            }
        }

        return polysyllables - 1;
    }

    public static void chooseReadabilityScore(
            double words,
            double sentences,
            double characters,
            double syllables,
            double polysyllables
    ) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String input = scanner.next();

        System.out.println();
        switch (input) {
            case "ARI" -> {
                double score = getAutomatedReadabilityScore(characters, words, sentences);
                printAutomatedReadabilityScore(score);
            }
            case "FK" -> {
                double score = getFleschKincaidReadabilityScore(words, sentences, syllables);
                printFleschKincaidReadabilityScore(score);
            }
            case "SMOG" -> {
                double score = getSMOGReadabilityScore(polysyllables, sentences);
                printSMOGReadabilityScore(score);
            }
            case "CL" -> {
                double score = getColemanLiauReadabilityScore(characters, words, sentences);
                printColemanLiauReadabilityScore(score);
            }
            case "all" -> {
                double score1 = getAutomatedReadabilityScore(characters, words, sentences);
                printAutomatedReadabilityScore(score1);

                double score2 = getFleschKincaidReadabilityScore(words, sentences, syllables);
                printFleschKincaidReadabilityScore(score2);

                double score3 = getSMOGReadabilityScore(polysyllables, sentences);
                printSMOGReadabilityScore(score3);

                double score4 = getColemanLiauReadabilityScore(characters, words, sentences);
                printColemanLiauReadabilityScore(score4);

                printAvgAge(new double[]{score1, score2, score3, score4});
            }
            default -> throw new IllegalStateException("Unexpected value: " + input);
        }
    }

    private static double getAutomatedReadabilityScore(double characters, double words, double sentences) {
        return AUTOMATED_READABILITY_4_71 * (characters / words) + AUTOMATED_READABILITY_0_5 * (words / sentences) -
                AUTOMATED_READABILITY_21_43;
    }

    private static void printAutomatedReadabilityScore(double score) {
        String upperAge = getUpperAge(score);
        System.out.printf("Automated Readability Index: %.2f (about %s-year-olds).\n", score, upperAge);
    }

    private static double getFleschKincaidReadabilityScore(double words, double sentences, double syllables) {
        return FLESCH_KINCAID_READABILITY_0_39 * (words / sentences) + FLESCH_KINCAID_READABILITY_11_8 *
                (syllables / words) - FLESCH_KINCAID_READABILITY_15_59;
    }

    private static void printFleschKincaidReadabilityScore(double score) {
        String upperAge = getUpperAge(score);
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %s-years-olds).\n", score, upperAge);
    }

    private static double getSMOGReadabilityScore(double polysyllables, double sentences) {
        return SMOG_1_043 * Math.sqrt(polysyllables * (SMOG_30 / sentences)) + SMOG_3_1291;
    }

    private static void printSMOGReadabilityScore(double score) {
        String upperAge = getUpperAge(score);
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %s-years-olds).\n", score, upperAge);
    }

    private static double getColemanLiauReadabilityScore(double letters, double words, double sentences) {
        final double L = letters / words * COLEMAN_LIAU_100;
        final double S = sentences / words * COLEMAN_LIAU_100;

        return COLEMAN_LIAU_0_588 * L - COLEMAN_LIAU_0_296 * S - COLEMAN_LIAU_15_8;
    }

    private static void printColemanLiauReadabilityScore(double score) {
        String upperAge = getUpperAge(score);
        System.out.printf("Coleman–Liau index: %.2f (about %s-years-olds).\n", score, upperAge);
    }

    private static void printAvgAge(double[] scores) {
        double avgAge = 0;
        for (double score : scores) {
            avgAge += Double.parseDouble(getUpperAge(score));
        }

        System.out.printf("\nThis text should be understood in average by %.2f-year-olds.\n", avgAge / scores.length);
    }

    private static String getUpperAge(double score) {
        return getGroupByScore(score).getAge().split("-")[1];
    }
}