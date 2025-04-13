package com.byteme.bytemeapplication.Utils;

public class QuizDataHolder {
    private static String extractedText;
    private static String fileName;  // 🆕 Add this

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

    public static void clear() {
        extractedText = null;
        fileName = null; // 🧹 clear filename too
    }
}
