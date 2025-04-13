package com.byteme.bytemeapplication.Utils;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TerminalQuizRunner {

    public static void run(String quizText) {
        Scanner scanner = new Scanner(System.in);
        int score = 0;
        int questionNumber = 1;

        // ✅ Updated regex to be more lenient with spacing and formatting
        Pattern pattern = Pattern.compile(
                "Q?\\d+\\.\\s+(.*?)\\s*^\\s*A\\.\\s+(.*?)\\s*^\\s*B\\.\\s+(.*?)\\s*^\\s*C\\.\\s+(.*?)\\s*^\\s*D\\.\\s+(.*?)\\s*^\\s*Answer:\\s*([A-D])",
                Pattern.MULTILINE | Pattern.DOTALL
        );

        Matcher matcher = pattern.matcher(quizText);

        while (matcher.find()) {
            String question = matcher.group(1).trim();
            String a = matcher.group(2).trim();
            String b = matcher.group(3).trim();
            String c = matcher.group(4).trim();
            String d = matcher.group(5).trim();
            String correct = matcher.group(6).trim().toUpperCase();

            System.out.println("\n🔸 Question " + questionNumber + ": " + question);
            System.out.println("A. " + a);
            System.out.println("B. " + b);
            System.out.println("C. " + c);
            System.out.println("D. " + d);
            System.out.print("Your answer (A/B/C/D): ");

            String input = scanner.nextLine().trim().toUpperCase();
            while (!input.matches("[A-D]")) {
                System.out.print("❗ Invalid input. Please enter A, B, C or D: ");
                input = scanner.nextLine().trim().toUpperCase();
            }

            if (input.equals(correct)) {
                System.out.println("✅ Correct!");
                score++;
            } else {
                System.out.println("❌ Incorrect. Correct answer: " + correct);
            }

            questionNumber++;
        }

        if (questionNumber == 1) {
            System.out.println("⚠️ No valid questions found. Check quiz formatting.");
        } else {
            System.out.println("\n🎉 Quiz complete! Final score: " + score + " out of " + (questionNumber - 1));
        }
    }
}
