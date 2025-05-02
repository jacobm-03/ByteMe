package com.byteme.bytemeapplication.Services;

import java.util.ArrayList;
import java.util.List;

public class ProgressTest {
    public static void main(String[] args) {
        // Simulated quiz scores for a subject
        List<Double> scores = new ArrayList<>();
        scores.add(80.0);
        scores.add(90.0);
        scores.add(70.0);
        scores.add(85.0);

        System.out.println("📘 Simulated Subject: Mathematics");
        System.out.println("📊 Raw Scores: " + scores);

        // Calculate average
        double total = 0;
        for (double score : scores) {
            total += score;
        }

        double average = scores.isEmpty() ? 0 : total / scores.size();
        System.out.printf("✅ Average Score: %.2f\n", average);
        System.out.println("✅ Quizzes Completed: " + scores.size());
        System.out.println("⏱ Time per Question: N/A (not tracked here)");
        System.out.println("🔥 Current Streak: 3 days (simulated)");

        // Simulated chart output
        System.out.println("\n📈 Quiz Performance Chart:");
        for (int i = 0; i < scores.size(); i++) {
            System.out.printf("Quiz %d: %.1f\n", i + 1, scores.get(i));
        }
    }
}
