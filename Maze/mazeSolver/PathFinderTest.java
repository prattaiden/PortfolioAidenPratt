package mazeSolver;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {


    @Test
    public void testNoGoalMaze() {
        PathFinder.solveMaze("/Users/aidenpratt/Documents/Documents - Aiden’s MacBook Pro/AidenPrattPortfolio/Projects/MazeSolver/testmazes/mazes/classic.txt"
                , "test.txt");

    }

//    @Test
//    public void testNoStartMaze(){
//        PathFinder.solveMaze("/Users/aidenpratt/Documents/AP-6012/Week4/Day5/Assingment08/testmazes/emptyMazes/two.txt"
//                , "twoTestOutput.txt");
//
//    }
//
//    @Test
//    public void testCloseSG(){
//        PathFinder.solveMaze("/Users/aidenpratt/Documents/AP-6012/Week4/Day5/Assingment08/testmazes/emptyMazes/three.txt"
//                , "threeTestOutput.txt");
//    }
//
//    @Test
//    public void testProfessorMazes()
//    {
//        PathFinder.solveMaze("/Users/aidenpratt/Documents/AP-6012/Week4/Day5/Assingment08/testmazes/mazes/unsolvable.txt",
//                "OUTPUT1.txt");
//
//        PathFinder.solveMaze("/Users/aidenpratt/Documents/AP-6012/Week4/Day5/Assingment08/testmazes/mazes/tinyMaze.txt",
//                "OUTPUT2.txt");
//
//        PathFinder.solveMaze("/Users/aidenpratt/Documents/AP-6012/Week4/Day5/Assingment08/testmazes/mazes/bigMaze.txt",
//                "OUTPUT3.txt");
//    }
//
//    @Test
//    public void notSolveable(){
//        PathFinder.solveMaze("/Users/aidenpratt/Documents/AP-6012/Week4/Day5/Assingment08/testmazes/emptyMazes/four.txt",
//                "fourTestOutput.txt");
//    }


}