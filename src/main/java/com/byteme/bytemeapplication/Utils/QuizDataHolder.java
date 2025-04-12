package com.byteme.bytemeapplication.Utils;

public class QuizDataHolder {
    private static String extractedText;

    public static void setExtractedText(String text) {
        extractedText = text;
    }

    public static String getExtractedText() {
        return extractedText;
    }

    public static void clear() {
        extractedText = null;
    }
}
