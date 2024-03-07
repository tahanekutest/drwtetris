import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * The Shape class models the falling blocks (or Tetrimino) in the Tetris game.
 * This class uses the "Singleton" design pattern. To get a new (random) shape,
 * call static method Shape.newShape().
 *
 * A Shape is defined and encapsulated inside the Matrix class.
 */
public class Shape {


    public enum ShapeType {
        Z, S, Q, I,L,
        J, T
    }
    // == Define named constants ==
    /** The width and height of a cell of the Shape (in pixels) */
    public final static int CELL_SIZE = 32;

    // == Singleton Pattern: Get an instance via Shape.newShape() ==
    // Singleton instance (class variable)
    private static Shape shape;

    // Private constructor, cannot be called outside this class
    private Shape() { }

    // == Define Shape's properties ==
    // A shape is defined by a 2D boolean array map, with its
    //   top-left corner at the (x, y) of the Matrix.
    // All variables are "package" visible

    ShapeType shapeType;
    int leftMostIndex;
    // Property 1: Top-left corner (x, y) of this Shape on the Matrix
    int x, y;
    // Property 2: Occupancy map
    boolean[][] map;
    // Property 3: The rows and columns for this Shape. Although they can be obtained
    //  from map[][], they are defined here for efficiency.
    int maxRows, maxCols;
    // Property 4: Array index for colors and maps
    int shapeIdx;
    // For ease of undo rotation, the original map is saved here.
    private boolean[][] mapSaved = new boolean[5][5];

    // All the possible shape maps
    // Use square array 3x3, 4x4, 5x5 to facilitate rotation computation.

    private static final boolean [][] Z =
            {{ true, true,  false },
            { false,  true,  true },
            { false,  false, false }};
    private static final boolean [][] L =
            {{ false, true,  false},
                    { false, true,  false},
                 { false, true,  true }};
    private static final boolean [][] Q =
                  {{ true, true },
                   { true, true }};
    private static final boolean [][] S =
                    {{ false, true,  true },
                    { true, true,  false },
                    { false, false, false }};
    private static final boolean [][] I =  {
                    { true, true, true, true }};

    private static final boolean [][] J =
                {{ false, true,  false},
                    { false, true,  false},
                    { true,  true,  false}};

    private static final boolean [][] T =
                        {{ false, true,  false },
                    { true,  true,  true  },
                    { false, false, false }};

    private static Map<String,boolean[][]> SHAPES_MAP = new HashMap<String, boolean[][]>(){
        {
            put("Q", Q);
            put("Z", Z);
            put("L", L);
            put("I", I);
            put("J", J);
            put("S", S);
            put("T", T);
        }};



    // Each shape has its own color
    private static final Color[] SHAPES_COLOR = {
            new Color(245, 45, 65),  // Z (Red #F52D41)
            Color.ORANGE, // L
            Color.YELLOW, // O
            Color.GREEN,  // S
            Color.CYAN,   // I
            new Color(76, 181, 245), // J (Blue #4CB5F5)
            Color.PINK    // T (Purple)
    };

    // For generating new random shape
    private static final Random rand = new Random();

    /**
     * Static factory method to get a newly initialized random
     *   singleton Shape.
     *
     * @return the singleton instance
     */
    public static Shape newShape(String letter, int number) {
        // Create object if it's not already created
        if(shape == null) {
            shape = new Shape();
        }

        // Set this shape's pattern. No need to copy the contents
        shape.map = SHAPES_MAP.get(letter);
        shape.maxRows = shape.map.length;
        shape.maxCols = shape.map[0].length;
        shape.leftMostIndex = number;

        switch (letter) {
            case "Z":
                shape.shapeType = ShapeType.Z;
                break;

            case "S":
                shape.shapeType = ShapeType.S;
                break;

            case "Q":  shape.shapeType = ShapeType.Q;
                break;
            case "I":  shape.shapeType = ShapeType.I;
                break;
            case "L":  shape.shapeType = ShapeType.L;
                break;
            case "J":  shape.shapeType = ShapeType.J;
                break;
            case "T": shape.shapeType = ShapeType.T;
                break;
        }

        return shape;  // return the singleton object
    }

    /**
     * Rotate the shape clockwise by 90 degrees.
     * Applicable to square matrix.
     *
     * <pre>
     *  old[row][col]             new[row][col]
     *  (0,0) (0,1) (0,2)         (2,0) (1,0) (0,0)
     *  (1,0) (1,1) (1,2)         (2,1) (1,1) (0,1)
     *  (2,0) (2,1) (2,2)         (2,2) (1,2) (0,2)
     *
     *  new[row][col] = old[numCols-1-col][row]
     * </pre>
     */
    public void rotateRight() {
        // Save the current map before rotate for quick undo if collision detected
        // (instead of performing an inverse rotate).
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                mapSaved[row][col] = map[row][col];
//            }
//        }
//
//        // Do the rotation on this map
//        // Rows must be the same as columns (i.e., square)
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                map[row][col] = mapSaved[cols - 1 - col][row];
//            }
//        }
    }

    /**
     * Rotate the shape anti-clockwise by 90 degrees.
     * Applicable to square matrix.
     *
     * <pre>
     *  old[row][col]             new[row][col]
     *  (0,0) (0,1) (0,2)         (0,2) (1,2) (2,2)
     *  (1,0) (1,1) (1,2)         (0,1) (1,1) (2,1)
     *  (2,0) (2,1) (2,2)         (0,0) (1,0) (2,0)
     *
     *  new[row][col] = old[col][numRows-1-row]
     * </pre>
     */
    public void rotateLeft() {
//        // Save the current map before rotate for quick undo if collision detected
//        // (instead of performing an inverse rotate).
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                mapSaved[row][col] = map[row][col];
//            }
//        }
//
//        // Do the rotation on this map
//        // Rows must be the same as columns (i.e., square)
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                map[row][col] = mapSaved[col][rows-1-row];
//            }
//        }
    }

    /**
     * Undo the rotate, due to move not allowed.
     */
    public void undoRotate() {
//        // Restore the array saved before the move
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                map[row][col] = mapSaved[row][col];
//            }
//        }
    }

    /**
     * Paint itself via the Graphics object.
     * Since Shape is encapsulated in Matrix, shape.paint(Graphics)
     * shall be called in matrix.paint(Graphics).
     *
     * @param g - the drawing Graphics object
     */
    public void paint(Graphics g) {
//        int yOffset = 1;  // Apply a small Y_OFFSET for nicer display?!
//
//        g.setColor(SHAPES_COLOR[this.shapeIdx]);
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                if (map[row][col]) {
//                    g.fill3DRect((x+col)*CELL_SIZE, (y+row)*CELL_SIZE+yOffset,
//                            CELL_SIZE, CELL_SIZE, true);
//                }
//            }
//        }
    }
}