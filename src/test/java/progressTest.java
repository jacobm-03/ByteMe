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
}

