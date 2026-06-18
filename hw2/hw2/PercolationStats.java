package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

import java.util.ArrayList;
import java.util.List;

public class PercolationStats {

    private double[] trialsArray;
    private Percolation perc;


    private int getYfrom1D(int siteVal, int N) {
        return Math.floorMod(siteVal, N);
    }

    private int getXfrom1D(int siteVal, int N) {
        return siteVal/N;
    }
    public PercolationStats(int N, int T, PercolationFactory pf) {
        // perform T independent experiments on an N-by-N grid
        if(N <= 0 || T<=0) {
            throw new IllegalArgumentException("N and T must be positive");
        }
        trialsArray = new double[T];
        //do T independent experiments. store results in trialsArray[] instance variable
        for (int t = 0; t < T; t++) {

            //make a new percolation object
            perc = pf.make(N);

            //repeat the following until the system percolates
            int openSiteCounter = 0;
            do {
                //choose a site uniformly at random among all blocked sites
                int random = StdRandom.uniform(N*N);
                while(perc.isOpen(getXfrom1D(random, N), getYfrom1D(random, N))) { //keep sampling until you find a blocked site
                    random = StdRandom.uniform(N*N);
                }
                //open the site
                perc.open(getXfrom1D(random, N), getYfrom1D(random, N));
                openSiteCounter += 1;
            } while (!perc.percolates());

            //the system has now percolated. need to record openSiteCounter/N as a double into the trialsList
            double trialResult = ((double) openSiteCounter) / (N*N);
            trialsArray[t] = trialResult;
        }
    }
    // sample mean of percolation threshold
    public double mean()  {
        return StdStats.mean(trialsArray);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(trialsArray);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow()  {
        double T = (double) trialsArray.length;
        return mean() - 1.96*stddev()/Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh()   {
        double T = (double) trialsArray.length;
        return mean() + 1.96*stddev()/Math.sqrt(T);
    }
}
