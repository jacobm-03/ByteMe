import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class progressTest {

    //Average test scores from all quizzes
    @Test
    public void testCalculateAverageScore(){
        List<Double> scores = Arrays.asList(80.0, 90.0, 70.0, 85.0);
        double total = 0;

        for (double score : scores) {
            total += score;
        }
        double expectedAverage = total / scores.size();
        assertEquals(81.25, expectedAverage, 0.01, "Average score should be 81.25");
    }

    //Quizzes completed
    @Test
    public void testQuizzesCompletedCount() {
        List<Double> scores = Arrays.asList(80.0, 90.0, 70.0, 85.0);
        assertEquals(4, scores.size(), "There should be 4 quizzes completed");
    }

    //Quiz performance staistics
    @Test
    public void testQuizPerformanceChartSimulation() {
        List<Double> scores = Arrays.asList(80.0, 90.0, 70.0, 85.0);

        assertAll("Quiz Scores",
                () -> assertEquals(80.0, scores.get(0)),
                () -> assertEquals(90.0, scores.get(1)),
                () -> assertEquals(70.0, scores.get(2)),
                () -> assertEquals(85.0, scores.get(3))
        );
    }

    @Test
    public void testEmptyScoresHandleGracefully() {
        List<Double> scores = List.of();
        double total = 0;
        for (double score : scores) {
            total += score;
        }
        double average = scores.isEmpty() ? 0 : total / scores.size();
        assertEquals(0, average, "Average should be 0 when there are no scores");
        assertEquals(0, scores.size(), "Quiz count should be 0 for empty list");
    }
}
