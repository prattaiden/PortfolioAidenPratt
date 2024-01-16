package mazeSolver;

import java.util.Scanner;

public class Maze {
    private int height_;
    private int width_;

    private char[][] mazeMap_;

    /**
     * Maze constructor
     * loops through the read in file and creates the 2d array object of a Maze
     * @param height the height of the maze
     * @param width the width of the maze
     * @param scanner the scanner which reads in the maze from the file
     */
    public Maze(int height, int width, Scanner scanner) {
        height_ = height;
        width_ = width;
        mazeMap_ = new char[height][width];
        //reading in the maze file nad populating it into a mazeMap_ 2d array
        fileReader(height, width, scanner);
    }

    /**
     * reads in the file with the provided scanner to construct the 2Darray Maze object
     * @param height height
     * @param width width
     * @param scanner scanner
     */
    public void fileReader(int height, int width, Scanner scanner){
        for (int i = 0; i < height; i++) {
            String line = scanner.nextLine();
            for (int j = 0; j < width; j++) {
                mazeMap_[i][j] = line.charAt(j);
            }
        }
    }
    /**
     *
     * @return the height of the maze
     */
    public int getHeight_() {
        return height_;
    }

    /**
     *
     * @return the width of the maze
     */
    public int getWidth() {
        return width_;
    }

    /**
     * gets the character at the provided height and width coordinates
     * @param height height
     * @param width width
     * @return the character at the coordinates
     */
    public char getCharAtCell(int height, int width) {
        return mazeMap_[height][width];
    }
}
