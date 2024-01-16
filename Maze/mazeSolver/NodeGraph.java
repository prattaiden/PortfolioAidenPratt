package mazeSolver;

import java.util.LinkedList;
import java.util.Queue;

public class NodeGraph {

    //---------------------------------NODE CLASS----------------------------------------\\
    public class Node {
        char data_; //the character at each node in the maze
        public LinkedList<Node> neighbors_;
        Node cameFrom_;
        boolean visited_;
        //Node constructor
        public Node(char data) {
            neighbors_ = new LinkedList<>();
            data_ = data;
            visited_ = false;
        }
    }
    //------------------------------------------------------------------------------------\\
    Node start_;
    Node goal_;
    public Queue<Node> frontier_;
    Node[][] nodes_;
    int width_;
    int height_;

    //constructor
    /**
     * Graph Constructor
     * loops over each element in the Maze and turns it into a node
     * assigns the neighbors to each node that is not an X
     * @param maze the Maze object passed in as a file
     * @param start the starting point with char value of 'S' in the mazes
     * @param goal the end point with char value of 'G' in the mazes
     */
    public NodeGraph(Maze maze, char start, char goal){
        //getting the dimensions from my maze object
        int width = maze.getWidth();
        int height = maze.getHeight_();
        width_ = width;
        height_ = height;
        //building the 2dArray of nodes based on the maze dimensions
        nodes_ = new Node[height][width];
        //converting the characters to nodes
        convertToNodes(maze, start, goal);

    }

    /**
     * loops through the Maze to convert the chars to Nodes
     * @param maze the 2d array passed in
     * @param start 'S' in the 2d array
     * @param goal 'G' in the 2d array
     */
    public void convertToNodes(Maze maze, char start, char goal){
        for(int i = 0; i < height_; i++) {
            for (int j = 0; j < width_; j++) {
                //char variable to get the character at each cell in the maze
                char cellChar = maze.getCharAtCell(i, j);
                nodes_[i][j] = new Node(cellChar);
                //finding the 'S' and 'G' locations
                if (cellChar == start) {
                    start_ = nodes_[i][j];
                }
                if (cellChar == goal) {
                    goal_ = nodes_[i][j];
                }
            }
        }
    }

    /**
     * takes the 2d node array and loops through looking at their neighbors
     * if the neighbors are not an 'X', add them to that node's neighbors array
     * @param nodes the 2d array of nodes just created in the method above
     */
    public void connectNodeNeighbors(Node[][] nodes){
        for(int h = 0; h < height_; h++) {
            for (int w = 0; w < width_; w++) {

                if (nodes[h][w] != null) {
                    //node above
                    if (h > 0 && nodes[h - 1][w].data_ != 'X') {
                        nodes[h][w].neighbors_.add(nodes_[h - 1][w]);
                    }
                    //node below
                    if (h < height_ - 1 && nodes[h + 1][w].data_ != 'X') {
                        nodes[h][w].neighbors_.add(nodes[h + 1][w]);
                    }
                    //node left
                    if (w > 0 && nodes[h][w - 1].data_ != 'X') {
                        nodes[h][w].neighbors_.add(nodes[h][w - 1]);
                    }
                    //node right
                    if (w < width_ - 1 && nodes[h][w + 1].data_ != 'X') {
                        nodes[h][w].neighbors_.add(nodes[h][w + 1]);
                    }
                }
            }
        }
    }

    /**
     * Traces the path from the goal to the start
     * leaves a '.' to indicate the path taken
     */
    public void setPath(){
        Node current = goal_;
        while (current != null){
            if(current.cameFrom_ != null && current.cameFrom_ != start_){
                current = current.cameFrom_;
                 current.data_ = '.';
            }
            else {
                break;
            }
        }
    }
}
