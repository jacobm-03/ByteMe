// Add these imports if missing
package com.byteme.bytemeapplication.Utils;


import com.byteme.bytemeapplication.Models.QuizQuestion;
import java.util.List;


public class QuizDataHolder {
    private static String extractedText;
    private static String fileName;
    private static String quizText;
    private static int subjectId;

    // 🟢 NEW: Result data
    private static List<QuizQuestion> questionList;
    private static List<String> userAnswers;
    private static int finalScore;

    // Existing getters/setters...

    public static void setExtractedText(String text) { extractedText = text; }
    public static String getExtractedText() { return extractedText; }

    public static void setFileName(String name) { fileName = name; }
    public static String getFileName() { return fileName; }

    public static void setQuizText(String text) { quizText = text; }
    public static String getQuizText() { return quizText; }

    public static void setSubjectId(int id) { subjectId = id; }
    public static int getSubjectId() { return subjectId; }

    public static void clear() {
        extractedText = null;
        fileName = null;
        quizText = null;
        subjectId = -1;
        questionList = null;
        userAnswers = null;
        finalScore = 0;
    }

    // 🟢 NEW: Store result data
    public static void setQuizResults(List<QuizQuestion> questions, List<String> answers, int score) {
        questionList = questions;
        userAnswers = answers;
        finalScore = score;
    }

    public static List<QuizQuestion> getQuestionList() {
        return questionList;
    }

    public static List<String> getUserAnswers() {
        return userAnswers;
    }

    public static int getFinalScore() {
        return finalScore;
    }
}
