package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int gridSize;
    private int numOpenSites;
    private WeightedQuickUnionUF uf;
    private int isFullSetNum;
    private static int VIRTUAL_TOP_SITE_NUMBER_INDEX;
    private static int VIRTUAL_BOTTOM_SITE_NUMBER;
    private int[] status;

    private int xyTo1D(int r, int c) {
        return gridSize*r + c;
    }

    private boolean isValidLocation(int r, int c) {
        return (0 <= r && r < gridSize && 0 <= c && c < gridSize);
    }

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be positive");
        }
        grid = new boolean[N][N];
        gridSize = N;
        numOpenSites = 0;
        uf = new WeightedQuickUnionUF(N*N + 1); //N^2 + 1 items (virtual top node)
        VIRTUAL_TOP_SITE_NUMBER_INDEX = N*N ;

        //Index-Based Tagging method
        /*
        0 := blocked/closed
        1 := open
        2 := site connected to top row
        4 := site connected to bottom row
        7 := open + connected to top + connected to bottom
         */
        status = new int[N*N+1];
        status[VIRTUAL_TOP_SITE_NUMBER_INDEX] = 2; //virtual node at last index
    }

    //open the site (row, col) if it is not open
    /*
    if the adjacent sites of (row, col) are open, then we need to union the adjacent site
    with the (row, col) site.
     */
    public void open(int row, int col) {
        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize) {
            throw new IndexOutOfBoundsException("row and col must be within: (" + 0 + "," + (gridSize -1) + ")");
        }

        if (isOpen(row, col)){
            return;
        }

        grid[row][col] = true;
        numOpenSites += 1;
        int siteIndex = xyTo1D(row, col);
        status[siteIndex] = 1; //the site has been opened

        //need to use accumulated bitwise OR assignment |=

        //open a top row site, then you need to
        //update site status to 2 and then connect to virtual top.
        if (row == 0) {
            status[siteIndex] |= 2; //the site is opened and connected to top row
            uf.union(siteIndex, VIRTUAL_TOP_SITE_NUMBER_INDEX);
        }

        //if in bottom row and we open it, we need to flag that it is connected to bottom row
        if (row == gridSize - 1) {
            status[siteIndex] |= 4; //the site is opened and connected to bottom row (and in the case of N = 1x1 grid, it is open and top and 4)
        }


        //for loop to iterate through the four possible neighbors
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};
        for (int i = 0; i < 4; i++) {
            int neighborRow = row + dRow[i];
            int neighborColumn = col + dCol[i];
            if(isValidLocation(neighborRow, neighborColumn) && isOpen(neighborRow, neighborColumn)) {
                //for each valid neighbor that is open, we need to:

                //calculate the site root's status
                int siteRootStatus = status[uf.find(siteIndex)];

                //calculate the neighbor's roots status
                int neighborIndex = xyTo1D(neighborRow, neighborColumn);
                int neighborRootStatus = status[uf.find(neighborIndex)];

                //merge the bitwise information about the siteRootStatus and neighborRootStatus
                int mergedStatus = siteRootStatus | neighborRootStatus;

                //uf.union(site, neighbor)
                uf.union(siteIndex, neighborIndex);

                //update the status array at the site's root's index with the merged information
                status[uf.find(siteIndex)] = mergedStatus;
            }
        }
    }

    //is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize) {
            throw new IndexOutOfBoundsException("row and col must be within: (" + 0 + "," + (gridSize -1) + ")");
        }
        return grid[row][col];
    }

    //is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize) {
            throw new IndexOutOfBoundsException("row and col must be within: (" + 0 + "," + (gridSize -1) + ")");
        }
        /*
        //Linear search. not optimal
        for (int j = 0; j < gridSize; j++) {
            if(uf.connected(xyTo1D(0,j), xyTo1D(row, col)) && isOpen(row, col)) {
                return true;
            }
        }
        return false;
         */
        return uf.connected(VIRTUAL_TOP_SITE_NUMBER_INDEX, xyTo1D(row, col));
    }

    //return number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    //does the system percolate?
    public boolean percolates() {
        int rootIndex = uf.find(VIRTUAL_TOP_SITE_NUMBER_INDEX);
        return status[rootIndex] >= 4;
    }

    public static void main(String[] args) {

    }

}
