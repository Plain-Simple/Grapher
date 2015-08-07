import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 * A class for rendering graphs.
 */
public class Grapher {

    /**
     * The height, in pixels, of the graph to be generated.
     * Must be greater than zero.
     */
    private int height;

    /**
     * The width, in pixels, of the graph to be generated.
     * Must be greater than zero.
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
     * behind the axis. Grid lines are drawn vertically and
     * horizontally across the graph. Note that no lines are drawn
     * along the edges of the graph.
     */
    private boolean drawGridlines;

    /**
     * Units between gridlines, if drawGridLines = true.
     * Because ticks are only drawn where there are grid lines,
     * this setting also determines the spacing between ticks.
     */
    private double gridLineSpacing;

    /**
     * Color of grid lines. // todo: specify default values
     */
    private Color gridLineColor;

    /**
     * BasicStroke used when drawing grid lines with the Graphics
     * API
     */
    private BasicStroke gridLineStroke;

    /**
     * Whether or not to draw ticks along the x- and y-axis.
     *
     */
    private boolean drawTicks; // todo: add label ticks option

    private BasicStroke tickStroke;

    private int tickLength;

    private boolean labelTicks;

    /**
     * Background color of graph.
     */
    private Color backgroundColor;

    /**
     * Color of axis, and ticks drawn along axis
     * (if drawTicks = true).
     */
    private Color axisColor;

    /**
     * BasicStroke style used for drawing the axis with the
     * Graphics API
     */
    private BasicStroke axisStroke;

    private int plotWidth;

    private Color plotColor;

    private Font labelFont;

    // todo: check that range makes sense; Add documentation
    // todo: allow user to set BasicStrokes for components
    /**
     * Default constructor. Sets all values to default values.
     */
    public Grapher() { // todo: constructor for setting line thicknesses directly, not just through basicstroke
        yMin = -10;
        yMax = 10;
        xMin = -10;
        xMax = 10;

        drawGridlines = true;
        gridLineSpacing = 1;
        gridLineStroke = new BasicStroke(1);
        gridLineColor = new Color(192, 192, 192);

        drawTicks = true;
        tickLength = 4;
        tickStroke = new BasicStroke(1);
        labelTicks = false;

        backgroundColor = Color.WHITE;

        axisColor = Color.BLACK;
        axisStroke = new BasicStroke(1);

        plotWidth = 6;
        plotColor = Color.BLACK;
    }

    /**
     * Draws the grid, or background, for the function/points
     * to be plotted on graph. Fills in background color
     * of graph, draws grid lines (if drawGridLines = true),
     * draws axis, and draws ticks (if drawTicks = true).
     * @param blank_image blank BufferedImage for grid to be drawn on
     * @return blank_image with grid drawn on it
     */
    public BufferedImage drawGrid(BufferedImage blank_image) {
        setWidthHeight(blank_image);

        Graphics2D graphics = blank_image.createGraphics();

        if(validateSettings()) {
            drawBackground(graphics);
            if (drawGridlines)
                drawGridLines(graphics);
            drawAxis(graphics);
        }

        return blank_image;
    }

    /**
     * Sets background color of graph using the backgroundColor
     * field.
     * @param graph Graphics2D object of graph being drawn
     */
    private void drawBackground(Graphics2D graph) {
        graph.setColor(backgroundColor);
        graph.fillRect(0, 0, width, height);
    }

    /**
     * Uses class-defined settings to draw horizontal and vertical
     * grid lines on the BufferedImage.
     * @param graph Graphics2D object of graph being drawn
     */
    private void drawGridLines(Graphics2D graph) {
        graph.setStroke(gridLineStroke);
        graph.setColor(gridLineColor);

        int start_x = getFirstXGridLine();
        int spacing_x = getXGridLineSpacing();

        /* Draw horizontal grid lines starting from start_x and moving down */
        for(int i = start_x; i < height; i += spacing_x) {
            graph.draw(new Line2D.Double(0, i, width, i));
            System.out.println("Drawing gridLine from (0," + i + ") to (" + width + "," + i + ")");
        }

        int start_y = getFirstYGridLine();
        int spacing_y = getYGridLineSpacing();

        /* Draw vertical grid lines starting from start_y and moving right */
        for(int i = start_y; i < width; i += spacing_y) {
            graph.draw(new Line2D.Double(i, 0, i, height));
            System.out.println("Drawing gridLine from (" + i + ",0) to (" + i + "," + height + ")");
        }
    }

    /**
     * Calculates y-coordinate in user-space of top-most horizontal gridline
     * in the graph's range. This can also be used for ticks because
     * they use the same spacing and rules as gridlines
     * @return
     */
    private int getFirstXGridLine() {
        /* Calculate how many "periods" from the minimum values of the graph to
         * the first gridline shown ON THE GRAPH. e.g. xMin = 10.8,
         * gridLineSpacing = 1 -> 0.2 periods to first gridline */
        double x_period_left = 1 - xMin % gridLineSpacing;

        /* Calculate where first gridline will be */
        return (int) (x_period_left * getXGridLineSpacing());
    }

    /**
     * Calculates x-coordinate in user-space of left-most vertical gridline
     * in the graph's range. This can also be used for ticks because
     * they use the same spacing and rules as gridlines
     * @return
     */
    private int getFirstYGridLine() {
        /* Calculate how many "periods" from the minimum values of the graph to // todo: make a function?
         * the first gridline shown ON THE GRAPH. e.g. xMin = 10.8,
         * gridLineSpacing = 1 -> 0.2 periods to first gridline */
        double y_period_left = 1 - yMin % gridLineSpacing;

        /* Calculate where first gridlines will be */
        return (int) (y_period_left * getYGridLineSpacing());
    }

    /* Calculates distance between vertical gridlines (px) */
    private int getYGridLineSpacing() {
        /* gridLineSpacing (units) * pixels per unit */
        return (int) (gridLineSpacing * (width / (xMax - xMin)));
    }

    /* Calculates distance between horizontal gridlines (px) */
    private int getXGridLineSpacing() {
        /* gridLineSpacing (units) * pixels per unit */
        return (int) (gridLineSpacing * (height / (yMax - yMin)));
    }

    /**
     *
     * @param graph
     * @return
     */
    private void drawAxis(Graphics2D graph) {
        graph.setStroke(axisStroke);
        graph.setColor(axisColor);

        /* Draw y-axis if x = 0 is found on the graph */
        if(xMin <= 0 && xMax >= 0) {
            /* Get start and end coordinates of y-axis on the graph */
            int[] start_x = coordinateToPixel(0, yMax);
            int[] end_x = coordinateToPixel(0, yMin);

            /* Draw y-axis */
            graph.draw(new Line2D.Double(start_x[0], start_x[1], end_x[0], end_x[1]));
            System.out.println("\nDrawing axis from (" + start_x[0] + "," + start_x[1] + ") to (" + end_x[0] + "," + end_x[1] + ")");

            if(drawTicks) {
                /* Get starting coordinate of first horizontal gridline and spacing */
                int start_y = getFirstXGridLine();
                int spacing_x = getXGridLineSpacing();

                double first_tick = pixelToCoordinate(start_y, start_x[1])[1];
                System.out.println("First y tick at " + first_tick);

                for(int i = start_y, j = 1; i < height; i += spacing_x, j++) {
                    graph.draw(new Line2D.Double(start_x[0], i, start_x[0] + tickLength, i));
                    System.out.println("Drawing tick from (" + start_x[0] + "," + i + ") to (" + start_x[0] + tickLength + "," + i + ")");
                    /* Label every other tick, except zero (zero is labeled on the x-axis) */
                    if(j % 2 == 0 && first_tick - j * gridLineSpacing != 0)
                        drawLeftJustifiedString(graph, Double.toString(first_tick - j * gridLineSpacing), start_x[0], i);
                }
            }
        }

        /* Draw x-axis if y = 0 is found on the graph */
        if(yMin <= 0 && yMax >= 0) {
            /* Get start and end coordinates of x-axis on the graph */
            int[] start_y = coordinateToPixel(xMin, 0);
            int[] end_y = coordinateToPixel(xMax, 0);

            /* Draw x-axis */
            graph.draw(new Line2D.Double(start_y[0], start_y[1], end_y[0], end_y[1]));
            System.out.println("\nDrawing axis from (" + start_y[0] + "," + start_y[1] + ") to (" + end_y[0] + "," + end_y[1] + ")");

            if(drawTicks) {
                /* Get starting x-coordinate of first vertical gridline and spacing */
                int start_x = getFirstYGridLine();
                int spacing_y = getYGridLineSpacing();

                double first_tick = pixelToCoordinate(start_x, start_y[1])[0];

                for(int i = start_x, j = 1; i < width; i += spacing_y, j++) {
                    graph.draw(new Line2D.Double(i, start_y[1], i, start_y[1] - tickLength));
                    System.out.println("Drawing tick from (" + i + "," + start_y[1] + ") to (" + i + "," + (start_y[1] - tickLength) + ")");
                    if(j % 2 == 0)
                        drawCenteredString(graph, Double.toString(first_tick + j * gridLineSpacing), i, start_y[1]);
                }
            }
        }
    }

    private void drawCenteredString(Graphics g, String s, int xCoordinate, int yCoordinate) {
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(s, xCoordinate - fm.stringWidth(s) / 2, yCoordinate + fm.getHeight());
    }

    private void drawLeftJustifiedString(Graphics g, String s, int xCoordinate, int yCoordinate) {
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(s, xCoordinate - fm.stringWidth(s), yCoordinate + fm.getAscent() / 2 - 1);
    }

    /**
     * "y = " or "f(x)" function used for graphing user-specified
     * function. By default, returns x.
     * To change this the user must enter their own expression.
     * @param x x-value used to calculate f(x)
     * @return f(x) using expression
     */
    public double calculate(double x) {
        return x;
    }

    /**
     * Checks to make sure that all settings are valid.
     * xMax must be greater than xMin and yMax must
     * be greater than yMin. Height and Width must
     * be greater than zero. Also makes sure that all
     * settings relative to height and width are less
     * than or equal to one.
     * @return whether all settings are valid
     * @throws IndexOutOfBoundsException - if settings break the rules
     */
    private boolean validateSettings() throws IndexOutOfBoundsException { // todo: improve. what is necessary, what will be strange but not cause a fatal error?
        if(xMax <= xMin)
            throw new IndexOutOfBoundsException("xMax cannot be less than or equal to xMin");
        if(yMax <= yMin)
            throw new IndexOutOfBoundsException("yMax cannot be less than or equal to yMin");
        if(height <= 0)
            throw new IndexOutOfBoundsException("height cannot be less than or equal to zero");
        if(width <= 0)
            throw new IndexOutOfBoundsException("width cannot be less than or equal to zero");
        return true;
    }

    /**
     * Renders graph from scratch and draws and emphasizes specified
     * points on the graph (as long as they are in the graph's range).
     * Coordinates of points to draw on graph are passed in 2d array
     * where double[0][index] gives the x-coordinate of a point and
     * double[1][index] gives the coresponding y-coordinate. Points are
     * drawn as circles using plotStroke, plotWidth, and plotColor
     * properties. // todo: properties or fields?
     * @param points x- and y-values of points to plot and emphasize // todo: update
     * @return
     * @throws IndexOutOfBoundsException - if double[0] is a different
     * size than double[1]
     */
    public BufferedImage drawGraph(BufferedImage blank_image, double[][] points) throws IndexOutOfBoundsException {
        setWidthHeight(blank_image);

        if(validateSettings()) {
            blank_image = drawGrid(blank_image);
            System.out.println();
            return drawGraphOnGrid(blank_image, points);
        }

        return blank_image;
    }

    /**
     * Draws graph on the specified grid using window values specified
     * (Y_MIN, Y_MAX, X_MIN, X_MAX).
     *
     * Use values to specify individual points to plot, where first index
     * is x-coordinate and second index is y-coordinate.
     *
     * @param grid
     * @param points
     * @return
     */
    public BufferedImage drawGraphOnGrid(BufferedImage grid, double[][] points) throws IndexOutOfBoundsException {
        setWidthHeight(grid);
        if(validateSettings()) {
            Graphics2D graph = grid.createGraphics();
            graph.setColor(plotColor);
            graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            for(int i = 0; i < points[0].length; i++) {
                drawPoint(graph, points[0][i], points[1][i]);
            }
        }

        return grid;
    }

    /**
     * drawGraph() uses calculate and graph range
     * @return
     */
    public BufferedImage drawGraph(BufferedImage blank_image, double rangeLow, double rangeHigh) {
        setWidthHeight(blank_image);
        drawBackground(blank_image.createGraphics());
        return drawGraphOnGrid(blank_image, rangeLow, rangeHigh);
    }

    public BufferedImage drawGraphOnGrid(BufferedImage grid, double rangeLow, double rangeHigh) { // todo: exclusive v. inclusive points
        setWidthHeight(grid);
        Graphics2D graph = grid.createGraphics();
        graph.setColor(plotColor);
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        float units_per_pxl = (xMax - xMin) / width;

        for(double i = rangeLow; i <= rangeHigh; i += units_per_pxl) {
            drawPoint(graph, i, calculate(i));
        }

        return grid;
    }

    /**
     * Converts xCoordinate and yCoordinate to a coordinate
     * on the userspace of graph and draws a filled circle
     * of radius (width * graphWidth) / 2 and color graphColor. // todo: missing anything?
     * Note: if coordinates are out of graph's range they will
     * not be plotted.
     * @param graph Graphics2D object of graph being drawn on
     * @param xCoordinate x-coordinate of point being plotted
     * @param yCoordinate y-coordinate of point being plotted
     * @return
     */
    private Graphics2D drawPoint(Graphics2D graph, double xCoordinate, double yCoordinate) {
        /* Check to see if coordinates fall in graph range */
        if((xCoordinate >= xMin && xCoordinate <= xMax) && (yCoordinate >= yMin && yCoordinate <= yMax)) {
            /* Convert number coordinates to a coordinate on graph's user space */
            int[] px_coordinates = coordinateToPixel(xCoordinate, yCoordinate);

            /* Draw a point with diameter = plotWidth at specified coordinates in userspace.
             * Coordinates must be adjusted because filloval draws the shape in a box that
             * starts at the specified coordinates and goes down and right */
            graph.fillOval(px_coordinates[0] - plotWidth / 2, px_coordinates[1] - plotWidth / 2, plotWidth, plotWidth);

            System.out.println("Drawing point at (" + px_coordinates[0] + "," + px_coordinates[1] + ")");
        }

        return graph;
    }

    /**
     * Converts coordinates of a point on the graph to coordinates of // todo: make sure ratio of pixels to units isn't > int limit (32765)
     * that pixel on the userspace of the Graphics2D object where drawing
     * takes place.
     * Uses window settings and height and width fields. Errors will occur
     * if these settings are not up to date.
     * @param xCoordinate
     * @param yCoordinate
     * @return int[] where int[0] is x-coordinate and int[1] is y-coordinate of
     * point's location in userspace
     */
    private int[] coordinateToPixel(double xCoordinate, double yCoordinate) {
        /* Calculate pixels per unit using the formula
         * axis width (pixels) / axis range (units) */
        double x_px_unit = width / (xMax - xMin);
        double y_px_unit = height / (yMax - yMin);

        /* For each dimension, calculate distance away from min value on the axis
         * and multiply by the pixels per unit ratio */
        return new int[] {(int) ((xCoordinate - xMin) * x_px_unit), (int) (height - (yCoordinate - yMin) * y_px_unit)};
    }

    /**
     * Converts coordinates of a pixel on the userspace to the
     * coordinates of the point on that pixel on the graph.
     * @param xPixel
     * @param yPixel
     * @return
     */
    private double[] pixelToCoordinate(int xPixel, int yPixel) {
        /* Calculate units per pixel using the formula
         * axis range (units) / axis width (pixels)*/
        double x_unit_px = (xMax - xMin) / width;
        double y_unit_px = (yMax - yMin) / height;

        return new double[] { xMin + (xPixel * x_unit_px), yMax - (yPixel * y_unit_px) };
    }

    /**
     * Sets width and height fields based on specifications of
     * to_draw.
     * This is used to keep the width and height fields up to date
     * with the BufferedImage being drawn on.
     * @param to_draw BufferedImage upon which the graph will be drawn
     */
    private void setWidthHeight(BufferedImage to_draw) {
        width = to_draw.getWidth();
        height = to_draw.getHeight();
    }

    //public Graphics labelPoint(double xCoordinate, double yCoordinate) {

    //}
}
