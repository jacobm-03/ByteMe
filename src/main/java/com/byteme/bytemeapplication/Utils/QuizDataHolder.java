package com.byteme.bytemeapplication.Utils;

public class QuizDataHolder {
    private static String extractedText;
    private static String fileName;
    private static String quizText; // ✅ NEW

    public static void setExtractedText(String text) {
        extractedText = text;
    }

    public static String getExtractedText() {
        return extractedText;
    }

    public static void setFileName(String name) {
        fileName = name;
    }

    public static String getFileName() {
        return fileName;
    }

    public static void setQuizText(String text) {  // ✅ NEW
        quizText = text;
    }

    public static String getQuizText() {  // ✅ NEW
        return quizText;
    }

    public static void clear() {
        extractedText = null;
        fileName = null;
        quizText = null; // ✅ NEW
    }
}
