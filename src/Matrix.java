import java.awt.Color;
import java.awt.Graphics;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * The Matrix class models the Tetris Game Board (called matrix)
 * that holds one falling block (shape).
 */
public class Matrix  {
    // == Define named constants ==
    /** Number of rows of the matrix */
    public final static int ROWS = 10;
    /** Number of columns of the matrix */
    public final static int COLS = 10;
    /** The width and height of a cell of the Shape (in pixels) */
    public final static int CELL_SIZE = Shape.CELL_SIZE;

    private static final Color COLOR_OCCUPIED = Color.LIGHT_GRAY;
    private static final Color COLOR_EMPTY = Color.WHITE;

    // == Define Matrix's properties ==
    // Property 1: The game board (matrix) is defined by a 2D boolean array map.
    boolean map[][] = new boolean[ROWS][COLS];
    // Property 2: The board has ONE falling shape
    Shape shape;

    /**
     * Constructor
     */
    public Matrix() { }

    /**
     * Reset the matrix for a new game, by reseting all the properties.
     * Clear map[][] and get a new random Shape.
     */

    public void newGame() {
        String letter = null;
        int number = 0;
        try {
            InputStream is = new FileInputStream("src/input.txt");
            try (Scanner sc = new Scanner(
                    is, StandardCharsets.UTF_8.name())) {

                // It holds true till there is single element
                // left in the object with usage of hasNext()
                // method
                while (sc.hasNextLine()) {
                    processLine(sc.nextLine());
                    clearMatrix();

                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Get a new random shape

    }

    private void clearMatrix(){
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                map[row][col] = false;  // empty
            }
        }
    }
    private void processLine(String line){
        String[] tokens = line.split(",");
        for(String token : tokens){
            String letter = Character.toString(token.charAt(0));
                String numberString = Character.toString(token.charAt(1));
                int number = Integer.parseInt(numberString);
                System.out.println("line "+line);
                System.out.println("values "+letter+number);
            addShapeToMatrix(Shape.newShape(letter, number));
        }
    }


    /**
     * The shape moves on the given action (left, right, down, rotate).
     * @return true if it is at the bottom and cannot move down further.
     *         Need to lock down this block.
     */
    public boolean stepGame() {
//        switch (action) {
//            case LEFT:
//                shape.x--;  // try moving
//                if (!actionAllowed()) shape.x++;  // undo the move
//                break;
//            case RIGHT:
//                shape.x++;
//                if (!actionAllowed()) shape.x--;  // undo the move
//                break;
//            case ROTATE_LEFT:
//                shape.rotateLeft();
//                if (!actionAllowed()) shape.undoRotate();  // undo the move
//                break;
//            case ROTATE_RIGHT:
//                shape.rotateRight();
//                if (!actionAllowed()) shape.undoRotate();  // undo the move
//                break;
//            case HARD_DROP: // Handle as FAST "down" in GameMain class for better visual
//            case SOFT_DROP: // Handle as FAST "down" in GameMain class for better visual
////          do {
////             shape.y++;
////          } while (moveAllowed());
////          shape.y--;
////          break;
//            case DOWN:
//                shape.y++;
//                if (!actionAllowed()) {
//                    // At bottom, cannot move down further. To lock down this block
//                    shape.y--;    // undo the move
//                    return true;
//                }
//                break;
//        }
        return false;  // not reach the bottom
    }

    /**
     * Check if the shape moves outside the matrix,
     *   or collide with existing shapes in the matrix.
     * @return true if this move action is allowed
     */
    public boolean actionAllowed() {
//        for (int shapeRow = 0; shapeRow < shape.rows; shapeRow++) {
//            for (int shapeCol = 0; shapeCol < shape.cols; shapeCol++) {
//                int matrixRow = shapeRow + shape.y;
//                int matrixCol = shapeCol + shape.x;
//                if (shape.map[shapeRow][shapeCol]
//                        && (matrixRow < 0 || matrixRow >= Matrix.ROWS
//                        || matrixCol < 0 || matrixCol >= Matrix.COLS
//                        || this.map[matrixRow][matrixCol])) {
//                    return false;
//                }
//            }
//        }
        return true;
    }

    private void addShapeToMatrix(Shape shape){
        boolean shouldStartFromTheBottom = false;
        int leftMostIndex = shape.leftMostIndex;
        for (int shapeRow = shape.maxRows-1 ; shapeRow >=0; shapeRow--) {
            // Check for the first occupied row along the column corresponding
            // to the leftmost index of the shape
            // in order to find the shape's place in the matrix
            if (this.map[shapeRow][leftMostIndex]) {
                shouldStartFromTheBottom = true;
                addShapeToMatrixHelper(shape, shapeRow, leftMostIndex);
            }
        }
        if(!shouldStartFromTheBottom){
            for (int shapeRow = 0; shapeRow < shape.maxRows; shapeRow++) {
                for (int shapeCol = 0; shapeCol < shape.maxCols; shapeCol++) {
                    int matrixCol = shapeCol + leftMostIndex;
                    if (shape.map[shapeRow][shapeCol]) {
                       this.map[shapeRow][matrixCol] = shape.map[shapeRow][shapeCol];
                    }
                }
            }
        }
    }

    private void addShapeToMatrixHelper(Shape shape,int orignalRow, int originCol){
        switch (shape.shapeType) {
            case Shape.ShapeType.Z:
                this.map[orignalRow][originCol + 1] = true;
                this.map[orignalRow][originCol + 2] = true;
                this.map[orignalRow+1][originCol] = true;
                this.map[orignalRow+1][originCol + 1] = true;
                break;
            case Shape.ShapeType.S:
                this.map[orignalRow+1][originCol] = true;
                this.map[orignalRow+1][originCol + 1] = true;
                this.map[orignalRow+2][originCol+1] = true;
                this.map[orignalRow+2][originCol+2] = true;
                break;
            case Shape.ShapeType.Q:
                this.map[orignalRow+1][originCol] = true;
                this.map[orignalRow+2][originCol] = true;
                this.map[orignalRow+1][originCol+1] = true;
                this.map[orignalRow+2][originCol+1] = true;
                break;
            case Shape.ShapeType.I:
                this.map[orignalRow+1][originCol] = true;
                this.map[orignalRow+1][originCol+1] = true;
                this.map[orignalRow+1][originCol+2] = true;
                this.map[orignalRow+1][originCol+3] = true;
                break;
            case Shape.ShapeType.L:
                this.map[orignalRow+1][originCol] = true;
                this.map[orignalRow+2][originCol] = true;
                this.map[orignalRow+3][originCol] = true;
                this.map[orignalRow+1][originCol+1] = true;
                break;
            case Shape.ShapeType.J:
                this.map[orignalRow+1][originCol] = true;
                this.map[orignalRow+1][originCol+1] = true;
                this.map[orignalRow+2][originCol+1] = true;
                this.map[orignalRow+3][originCol+1] = true;
                break;
            case Shape.ShapeType.T:
                this.map[orignalRow+1][originCol] = true;
                this.map[orignalRow+1][originCol+1] = true;
                this.map[orignalRow+1][originCol+2] = true;
                this.map[orignalRow][originCol+1] = true;
                break;
        }

    }

    /**
     * Lock down the block, by transfer the block's content to the matrix.
     * Also clear filled lines, if any.

     * @return the number of rows removed in the range of [0, 4]
     */
    public int lockDown() {
        // Block at bottom, lock down by transferring the block's content
        //  to the matrix
        for (int shapeRow = 0; shapeRow < shape.maxRows; shapeRow++) {
            for (int shapeCol = 0; shapeCol < shape.maxCols; shapeCol++) {
                if (shape.map[shapeRow][shapeCol]) {
                    this.map[shapeRow + shape.y][shapeCol + shape.x] = true;
                }
            }
        }
        // Process the filled row(s) and update the score
        return clearLines();
    }

    /**
     * Process the filled rows in the game board and remove them.
     * The filled rows need not be at the bottom.
     *
     * @return the number of rows removed in the range of [0, 4]
     */
    public int clearLines() {
        // Starting from the last rows, check if a row is filled if so, move down
        // the occupied square. Need to check all the way to the top-row
        int row = ROWS - 1;
        int rowsRemoved = 0;
        boolean removeThisRow;

        while (row >= 0) {
            removeThisRow = true;
            for (int col = 0; col < COLS; col++) {
                if (!map[row][col]) {
                    removeThisRow = false;
                    break;
                }
            }

            if (removeThisRow) {
                // delete the row by moving down the occupied slots.
                for (int row1 = row; row1 > 0; row1--) {
                    for (int col1 = 0; col1 < COLS; col1++) {
                        map[row1][col1] = map[row1 - 1][col1];
                    }
                }
                rowsRemoved++;
                // The top row shall be empty now.
                for (int col = 0; col < COLS; col++)
                    map[0][col] = false;

                // No change in row number. Check this row again (recursion).
            } else {
                // next row on top
                row--;
            }
        }
        return rowsRemoved;
    }

    /**
     * Paint itself via the Graphics context.
     * The JFrame's repaint() in GameMain class callbacks paintComponent(Graphics)
     * This shape.paint(Graphics) shall be placed in paintComponent(Graphics).

     * @param g - the drawing Graphics object
     */
    public void paint(Graphics g) {
//        int yOffet = 1;   // apply a small y offset for nicer display?!
//        for (int row = 0; row < ROWS; row++) {
//            for (int col = 0; col < COLS; col++) {
//                g.setColor(map[row][col] ? COLOR_OCCUPIED : COLOR_EMPTY);
//                g.fill3DRect(col*CELL_SIZE, row*CELL_SIZE+yOffet,
//                        CELL_SIZE, CELL_SIZE, true);
//            }
//        }
//
//        // Also paint the Shape encapsulated
//        shape.paint(g);
    }
}