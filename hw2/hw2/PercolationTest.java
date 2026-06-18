package hw2;


import org.junit.Test;

import static org.junit.Assert.*;

public class PercolationTest {

    @Test
    public void testPercolationStats() {
        PercolationStats percStats = new PercolationStats(20, 1000, new PercolationFactory());
        System.out.println("mean: " + percStats.mean());
        System.out.println("stddev: " + percStats.stddev());
        System.out.println("Confidence Interval: (" + percStats.confidenceLow() + ", " + percStats.confidenceHigh() + ")");
    }
}
