import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

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
     * Value of y-coordinate at the bottom edge of the graph.
     * Must be less than yMax. Considered a "window setting"
     * because it defines the range of the graph.
     */
    private long yMin;

    /**
     * Value of y-coordinate at the top edge of the graph.
     * Must be greater than yMin. Considered a "window setting"
     * because it defines the range of the graph.
     */
    private long yMax;

    /**
     * Value of x-coordinate at the left edge of the graph.
     * Must be less than xMax. Considered a "window setting"
     * because it defines the range of the graph.
     */
    private long xMin;

    /**
     * Value of x-coordinate at the right edge of the graph.
     * Must be greater than xMin. Considered a "window setting"
     * because it defines the range of the graph.
     */
    private long xMax;

    /**
     * Whether or not to draw grid lines in the graph's background,
     * behind the axis.
     */
    private boolean drawGridlines;

    /**
     * space between gridlines = img.width() * gridLineSpacing
     * also space between ticks
     */
    private float gridLineSpacing;

    /**
     * relative width of each gridline and tick
     */
    private float gridLineWidth;

    private Color gridLineColor;

    private BasicStroke gridLineStroke;

    /**
     *
     */
    private boolean drawTicks; // todo: add label ticks option

    private BasicStroke tickStroke;

    private float tickLength;
    /**
     * Background color of graph.
     */
    private Color backgroundColor;

    /**
     * Color of axis, and ticks, if drawTicks = true.
     */
    private Color axisColor;

    /**
     * BasicStroke style used for drawing axis.
     */
    private BasicStroke axisStroke;

    /**
     * Relative width of axis (i.e. 0.05f will draw
     * an axis with a width equal to 5% of the image's
     * width). The same value is used for drawing x- and y-axis,
     * regardless of width and height being different values.
     */
    private float axisWidth;

    // todo: check that range makes sense; Add documentation
    // todo: allow user to set BasicStrokes for components
    // todo: set gridlines using relative value (e.g. 10% of width)
    /**
     * Default constructor. Sets all values to default values.
     */
    public Grapher(int height, int width) {
        this.height = height;
        this.width = width;
    }

    /**
     * Draws the grid, or background, for the function/points
     * to be plotted on graph. Fills in background color
     * of graph, draws grid lines (if drawGridLines = true),
     * draws axis, and draws ticks (if drawTicks = true) using
     * styles.
     * @param graph BufferedImage object of graph being drawn
     */
    public void drawGrid(BufferedImage graph) {
        drawBackground(graph);
        if(drawGridlines) {
            drawGridLines(graph);
        }
        drawAxis(graph);
    }

    /**
     * Sets background color of graph using the backgroundColor
     * field.
     * @param graph BufferedImage object of graph being drawn
     */
    private void drawBackground(BufferedImage graph) { // todo: createGraphics() in the function calling this function?
        graph.createGraphics().setBackground(backgroundColor);
    }

    /**
     * Uses class-defined settings to draw horizontal and vertical
     * grid lines on the BufferedImage.
     * @param graph BufferedImage object of graph being drawn
     */
    private void drawGridLines(BufferedImage graph) {
        /* First, calculate distance between gridlines using image
         * height and width and range to display. */
        int spacing_x = (int) (graph.getWidth() * gridLineSpacing);
        int spacing_y = (int) (graph.getHeight() * gridLineSpacing);
        Graphics2D grid_lines = graph.createGraphics();
        grid_lines.setStroke(gridLineStroke);
        grid_lines.setColor(gridLineColor);

        /* Draw vertical grid lines */
        for(int i = spacing_x; i < graph.getWidth(); i += spacing_x) {
            grid_lines.draw(new Line2D.Double(i, 0, i, graph.getHeight()));
        }

        /* Draw horizontal grid lines */
        for(int i = spacing_y; i < graph.getHeight(); i += spacing_y) {
            grid_lines.draw(new Line2D.Double(0, i, 0, graph.getWidth()));
        }
    }

    /**
     *
     * @param graph
     */
    private void drawAxis(BufferedImage graph) {
        Graphics2D axis = graph.createGraphics();
        axis.setStroke(axisStroke);
        axis.setColor(axisColor);

        axis.draw(new Line2D.Double(graph.getWidth() / 2, 0, graph.getWidth() / 2, graph.getHeight()));
        axis.draw(new Line2D.Double(0, graph.getHeight() / 2, graph.getWidth(), graph.getHeight() / 2));
    }

    /**
     * Draws ticks along x- and y-axis.
     * Uses gridLineSpacing to determine space between ticks,
     * axisColor to determine color of ticks that are drawn,
     * and tickStroke to style the ticks.
     * @param graph
     */
    private void drawTicks(BufferedImage graph) {
        Graphics2D ticks = graph.createGraphics();
        ticks.setStroke(tickStroke);
        ticks.setColor(axisColor);

        /* Calculate spacing along x- and y-axis between individual
         * ticks */
        int spacing_x = (int) (graph.getWidth() * gridLineSpacing);
        int spacing_y = (int) (graph.getHeight() * gridLineSpacing);

        /* Calculate length of each tick based on image width */
        int tick_length = (int) (graph.getWidth() * tickLength);

        /* Calculate x-start and x-end coordinates of ticks on the y-axis */
        int tick_start = graph.getWidth() / 2 - tick_length / 2;
        int tick_end = graph.getWidth() / 2 + tick_length / 2;

        for(int i = spacing_x; i < graph.getWidth(); i += spacing_x) { // todo: refactoring
            ticks.draw(new Line2D.Double(tick_start, i, tick_end, i));
        }

        /* Calculate y-start and y-end coordinates of ticks on the x-axis */
        tick_start = graph.getHeight() / 2 + tick_length / 2;
        tick_end = graph.getHeight() / 2 - tick_length / 2;

        for(int i = spacing_y; i < graph.getHeight(); i += spacing_y) {
            ticks.draw(new Line2D.Double(i, tick_start, i, tick_end));
        }
    }

    /**
     * "y = " or "f(x)" function used for graphing user-specified
     * function. By default, returns x.
     * To change this the user must enter their own expression.
     * @param x x-value used to calculate f(x)
     * @return f(x) using expression
     */
    public long calculate(long x) {
        return x;
    }

    /**
     * Checks to make sure that the values defining window range
     * are valid. xMax must be greater than xMin and yMax must
     * be greater than yMin.
     * @return whether window range is valid
     */
    private boolean validateWindow() {
        if(xMax <= xMin)
            return false;
        if(yMax <= yMin)
            return false;
        return true;
    }

    /**
     * Renders graph from scratch and draws and emphasizes specified
     * points on the graph (as long as they are in the graph's range).
     * Coordinates of points to draw on graph are passed in 2d array
     * where long[0][index] gives the x-coordinate of a point and
     * long[1][index] gives the coresponding y-coordinate. Points are
     * emphasized using the // todo: how to draw points?
     * @param points x- and y-values of points to plot and emphasize
     * @return
     */
    public BufferedImage drawGraph(long[][] points) {

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

    /**
     * drawGraph() uses calculate and graph range
     * @return
     */
    public BufferedImage drawGraph() {

    }

    public BufferedImage drawGraph(Graphics grid) {

    }

    /**
     * uses calculate and given range (piecewise functions)
     * @param grid
     * @param rangeLow
     * @param rangeHigh
     * @return
     */
    public Graphics drawGraph(Graphics grid, long rangeLow, long rangeHigh) {

    }

    public Graphics emphasizePoints(long[][] coordinates) {

    }

    public Graphics labelPoint(long xCoordinate, long yCoordinate) {

    }
}
