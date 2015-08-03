import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * A class for rendering graphs.
 */
public class Grapher {

    /**
     * The height, in pixels, of the graph to be generated.
     * A minimum value of
     */
    private int height;

    /**
     * The width, in pixels, of the graph to be generated.
     * A minimum value of
     */
    private int width;

    /**
     * The lowest y-coordinate displayed on the graph.
     * Must be less than yMax. Considered a "window setting"
     * because it defines the range of the graph.
     */
    private long yMin;

    /**
     * The highest y-coordinate displayed on the graph.
     * Must be greater than yMin. Considered a "window setting"
     * because it defines the range of the graph.
     */
    private long yMax;

    /**
     * The lowest x-coordinate displayed on the graph.
     * Must be less than xMax. Considered a "window setting"
     * because it defines the range of the graph.
     */
    private long xMin;

    /**
     * The highest x-coordinate displayed on the graph.
     * Must be greater than xMin. Considered a "window setting"
     * because it defines the range of the graph.
     */
    private long xMax;

    /**
     *
     */
    private boolean drawGridlines;

    /**
     *
     */
    private boolean drawTicks;

    /**
     * Color of axis to be drawn.
     */
    private Color graphColor;
    
    private Color gridColor;

    private Color backgroundColor;

    // todo: check that range makes sense; Add documentation

    /**
     * Default constructor. Sets all values to default values.
     */
    public Grapher(int height, int width) {
        this.height = height;
        this.width = width;
    }

    /**
     *
     * @return
     */
    public BufferedImage drawGrid() {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        drawBackground(result);
        if(drawGridlines) {
            result = drawGridLines(result);
        }
        result = drawAxis(result);
    }

    /**
     * Sets background color of graph using the backgroundColor
     * field.
     * @param graph BufferedImage of graph being drawn
     */
    private void drawBackground(BufferedImage graph) {
        graph.createGraphics().setBackground(backgroundColor);
    }

    /**
     * Uses class-defined settings to draw horizontal and vertical
     * grid lines on the BufferedImage.
     * @param graph
     * @return
     */
    private void drawGridLines(BufferedImage graph) {
        /* First, calculate distance between gridlines using image
         * height and width and range to display. */
        int spacing_x = graph.getWidth() / 10;
        int spacing_y = graph.getHeight() / 10;
        Graphics2D grid_lines = graph.createGraphics();
        grid_lines.setStroke(new BasicStroke(10));

        /* Draw vertical grid lines */
        for(int i = spacing_x; i < graph.getWidth(); i += spacing_x) {
            grid_lines.draw(new Line2D.Double(i, 0, i, graph.getHeight()));
        }

        /* Draw horizontal grid lines */
        for(int i = spacing_y; i < graph.getHeight(); i += spacing_y) {
            
        }
    }

    public long calculate(long x) {

    }

    private boolean validateWindow() {

    }

    /**
     *
     * @param values if null then uses calculate
     * @param rangeLow
     * @param rangeHigh
     * @return
     */
    public BufferedImage drawGraph(long[][] values, long rangeLow, long rangeHigh) {

    }

    /**
     * Draws graph on the specified grid using window values specified
     * (Y_MIN, Y_MAX, X_MIN, X_MAX).
     *
     * Use values to specify individual points to plot, where first index
     * is x-coordinate and second index is y-coordinate.
     *
     * @param grid
     * @param values
     * @return
     */
    public Graphics drawGraph(Graphics grid, long[][] values) {

    }

    public Graphics drawGraph(Graphics grid, long rangeLow, long rangeHigh) {

    }

    public Graphics emphasizePoint() {

    }

    public Graphics labelPoint() {

    }

    /**
     * Renders Graphics object to specified file. // todo: how trivial is it for the user to do this themselves?
     * @param g
     * @param toSave
     * @param extension
     * @return
     */
    public File renderFile(Graphics g, File toSave, String extension) {

    }
}
