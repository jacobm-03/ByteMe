package com.byteme.bytemeapplication.Utils;

import com.byteme.bytemeapplication.Models.QuizQuestion;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuizParser {

    public static List<QuizQuestion> parse(String quizText) {
        List<QuizQuestion> questions = new ArrayList<>();

        // Pattern to extract: Q. ... A. ... B. ... C. ... D. ... Answer: X
        Pattern pattern = Pattern.compile(
                "Q\\d+\\. (.*?)\\s+" +
                        "A\\. (.*?)\\s+" +
                        "B\\. (.*?)\\s+" +
                        "C\\. (.*?)\\s+" +
                        "D\\. (.*?)\\s+" +
                        "Answer: ([A-D])",
                Pattern.DOTALL);

        Matcher matcher = pattern.matcher(quizText);

        while (matcher.find()) {
            String question = matcher.group(1).trim();
            String a = matcher.group(2).trim();
            String b = matcher.group(3).trim();
            String c = matcher.group(4).trim();
            String d = matcher.group(5).trim();
            String correct = matcher.group(6).trim();

            QuizQuestion q = new QuizQuestion(question, a, b, c, d, correct);
            questions.add(q);
        }

        return questions;
    }
}
