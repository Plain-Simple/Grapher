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
    private double yMin;

    /**
     * Value of y-coordinate at the top edge of the graph.
     * Must be greater than yMin. Considered a "window setting"
     * because it defines the range of the graph.
     */
    private double yMax;

    /**
     * Value of x-coordinate at the left edge of the graph.
     * Must be less than xMax. Considered a "window setting"
     * because it defines the range of the graph.
     */
    private double xMin;

    /**
     * Value of x-coordinate at the right edge of the graph.
     * Must be greater than xMin. Considered a "window setting"
     * because it defines the range of the graph.
     */
    private double xMax;

    /**
     * Whether or not to draw grid lines in the graph's background,
     * behind the axis. Grid lines are drawn vertically and
     * horizontally across the graph. Note that no lines are drawn
     * along the edges of the graph.
     */
    private boolean drawGridlines;

    /**
     * Spacing of grid lines to be drawn if drawGridLines = true.
     * For example, a gridLineSpacing of 1.0 will draw grid lines
     * at one unit intervals. Because ticks are only drawn where
     * there are grid lines, this setting also determines the spacing
     * of ticks.
     */
    private double gridLineSpacing;

    /**
     * Color used to draw grid lines.
     */
    private Color gridLineColor;

    /**
     * BasicStroke used to draw grid lines and ticks.
     */
    private BasicStroke gridLineStroke;

    /**
     * Whether or not to draw ticks along the x- and y-axis.
     * Ticks are small lines drawn along the axis using
     * the same spacing and stroke as grid lines and the same
     * color as the axis. Ticks extend to the right of the y-axis
     * and up from the x-axis.
     */
    private boolean drawTicks;

    /**
     * Length, in pixels, of ticks to be drawn perpendicularly
     * from the x- and y-axis.
     */
    private int tickLength;

    /**
     * Whether or not to label ticks with their graphical
     * coordinates along the axis. Labels are printed to
     * the left of the y-axis and below the x-axis.
     * Spacing and precision of the labels is defined by
     * how much space there is between grid lines.
     */
    private boolean labelTicks;

    /**
     * Background color of graph.
     */
    private Color backgroundColor;

    /**
     * Color used to draw axis and ticks
     */
    private Color axisColor;

    /**
     * BasicStroke style used for drawing the axis
     */
    private BasicStroke axisStroke;

    /**
     * Thickness, in pixels, of points and lines
     * drawn on the graph. // todo: individual points thicker than sets
     */
    private int plotWidth;

    /**
     * Color used to draw points on the graph.
     */
    private Color plotColor;

    /**
     * Font used when labeling ticks and specified points.
     */
    private Font labelFont;

    // todo: allow user to set BasicStrokes for components
    /**
     * Default constructor. Sets window range from -10 to 10
     * on the x-axis and -10 to 10 on the y-axis. Sets
     * drawGridLines and drawTicks to true. gridLineSpacing is
     * set to 1, gridLineStroke and axisStroke to 1 pixel thick,
     * and plotWidth to 2. Default colors are white for background,
     * black for plot and axis, and grey for grid lines and ticks.
     */
    public Grapher() { // todo: constructor for setting line thicknesses directly, not just through basicstroke
        yMin = -10;    // todo: specify default values
        yMax = 10;     // todo: remember precision of each value. Label with lowest precision
        xMin = -10;
        xMax = 10;

        drawGridlines = true;
        gridLineSpacing = 1;
        gridLineStroke = new BasicStroke(1);
        gridLineColor = new Color(192, 192, 192);

        drawTicks = true;
        tickLength = 4;
        labelTicks = false;

        backgroundColor = Color.WHITE;

        axisColor = Color.BLACK;
        axisStroke = new BasicStroke(1);

        plotWidth = 2;
        plotColor = Color.BLACK;
    }

    private void setWindow(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    private void setColors(Color backgroundColor, Color gridLineColor,
                           Color axisColor, Color plotColor) {
        this.backgroundColor = backgroundColor;
        this.gridLineColor = gridLineColor;
        this.axisColor = axisColor;
        this.plotColor = plotColor;
    }

    private void setStrokes(BasicStroke gridLineStroke, BasicStroke axisStroke) {
        this.gridLineStroke = gridLineStroke;
        this.axisStroke = axisStroke;
    }

    private void setStrokes(int gridLineThickness, int axisThickness) {
        this.gridLineStroke = new BasicStroke(gridLineThickness);
        this.axisStroke = new BasicStroke(axisThickness);
    }

    private void setSettings(boolean drawGridlines, boolean drawTicks,
                             boolean labelTicks) {
        this.drawGridlines = drawGridlines;
        this.drawTicks = drawTicks;
        this.labelTicks = labelTicks;
    }

    /**
     * Draws the grid, or background, for the function/points
     * to be plotted on the graph. Fills in the background color
     * of the graph, draws grid lines (if drawGridLines = true),
     * draws axis, and draws ticks (if drawTicks = true).
     *
     * @param blank_image BufferedImage for grid to be drawn on
     * @return the BufferedImage with a grid drawn on it
     */
    public BufferedImage drawGrid(BufferedImage blank_image) {
        setHeightWidth(blank_image);

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
     * Draws a solid background on the Graphics2D object of
     * color backgroundColor.
     *
     * @param graph Graphics2D object with background filled in
     */
    private void drawBackground(Graphics2D graph) {
        graph.setColor(backgroundColor);
        graph.fillRect(0, 0, width, height);
    }

    /**
     * Draws horizontal and vertical grid lines on the Graphics2D
     * object using gridLineStroke and gridLineColor, at intervals
     * specified by gridLineSpacing.
     *
     * @param graph Graphics2D object for grid lines to be drawn on
     */
    private void drawGridLines(Graphics2D graph) {
        graph.setStroke(gridLineStroke);
        graph.setColor(gridLineColor);

        int start_x = getFirstXGridLine();
        int spacing_x = getXGridLineSpacing();

        /* Draw horizontal grid lines starting from start_x and moving down */
        for(int i = start_x; i < height; i += spacing_x) {
            graph.draw(new Line2D.Double(0, i, width, i));
        }

        int start_y = getFirstYGridLine();
        int spacing_y = getYGridLineSpacing();

        /* Draw vertical grid lines starting from start_y and moving right */
        for(int i = start_y; i < width; i += spacing_y) {
            graph.draw(new Line2D.Double(i, 0, i, height));
        }
    }

    /**
     * Calculates y-coordinate in user-space of the top-most horizontal grid line
     * in the graph's range. This can also be used for ticks because
     * they use the same spacing and rules as grid lines
     *
     * @return the y-coordinate in user-space of the top-most horizontal grid line
     * or tick seen on the graph. // todo: what if no grid line is on the graph?
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
     * Calculates x-coordinate in user-space of left-most vertical grid line
     * in the graph's range. This can also be used for ticks because
     * they use the same spacing and rules as grid lines
     *
     * @return the x-coordinate in user-space of the left-most horizontal
     * grid line or tick seen on the graph.
     */
    private int getFirstYGridLine() {
        /* Calculate how many "periods" from the minimum values of the graph to // todo: make a function?
         * the first gridline shown ON THE GRAPH. e.g. xMin = 10.8,
         * gridLineSpacing = 1 -> 0.2 periods to first gridline */
        double y_period_left = 1 - yMin % gridLineSpacing;

        /* Calculate where first gridlines will be */
        return (int) (y_period_left * getYGridLineSpacing());
    }

    /**
     * Calculates the distance, in pixels, between vertical grid lines
     * using gridLineSpacing and window ranges.
     *
     * @return spacing, in pixels, between vertical grid lines
     */
    private int getYGridLineSpacing() {
        /* gridLineSpacing (units) * pixels per unit */
        return (int) (gridLineSpacing * (width / (xMax - xMin)));
    }

    /**
     * Calculates distance, in pixels, between horizontal grid lines
     * using gridLineSpacing and window ranges.
     *
     * @return spacing, in pixels, between vertical grid lines // todo: could this be one pixel too large?
     */
    private int getXGridLineSpacing() {
        /* gridLineSpacing (units) * pixels per unit */
        return (int) (gridLineSpacing * (height / (yMax - yMin)));
    }

    /**
     * Draws axis and ticks (if drawTicks = true) on the Graphics2D
     * object using axisStroke, axisColor, gridLineStroke, and
     * gridLineSpacing if axis passes through the graph's window.
     *
     * @param graph Graphics2D object for axis to be drawn on
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

            if(drawTicks) {
                graph.setStroke(gridLineStroke);
                /* Get starting coordinate of first horizontal gridline and spacing */
                int start_y = getFirstXGridLine();
                int spacing_x = getXGridLineSpacing();

                double first_tick = pixelToCoordinate(start_y, start_x[1])[1];

                for(int i = start_y, j = 1; i < height; i += spacing_x, j++) {
                    graph.draw(new Line2D.Double(start_x[0], i, start_x[0] + tickLength, i));

                    /* Label every other tick, except zero (zero is labeled on the x-axis) */
                    if(j % 2 == 0 && first_tick - j * gridLineSpacing != 0 && labelTicks == true)
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

            if(drawTicks) {
                graph.setStroke(gridLineStroke);
                /* Get starting x-coordinate of first vertical gridline and spacing */
                int start_x = getFirstYGridLine();
                int spacing_y = getYGridLineSpacing();

                double first_tick = pixelToCoordinate(start_x, start_y[1])[0];

                for(int i = start_x, j = 1; i < width; i += spacing_y, j++) {
                    graph.draw(new Line2D.Double(i, start_y[1], i, start_y[1] - tickLength));

                    if(j % 2 == 0 && drawTicks == true)
                        drawCenteredString(graph, Double.toString(first_tick + j * gridLineSpacing), i, start_y[1]);
                }
            }
        }
    }

    /**
     * Draws a String using labelFont just below and centered to
     * given user-space coordinates.
     *
     * @param g Graphics2D object for String to be drawn on
     * @param s String to be drawn
     * @param x x-coordinate in user-space to draw String below
     * @param y y-coordinate in user-space to draw String below
     */
    private void drawCenteredString(Graphics g, String s, int x, int y) {
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(s, x - fm.stringWidth(s) / 2, y + fm.getHeight());
    }

    /**
     * Draws a String using labelFont to the left and centered
     * vertically to given user-space coordinates.
     *
     * @param g Graphics2D object for String to be drawn on
     * @param s String to be drawn
     * @param x x-coordinate in user-space to draw String below
     * @param y y-coordinate in user-space to draw String below
     */
    private void drawLeftJustifiedString(Graphics g, String s, int x, int y) {
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(s, x - fm.stringWidth(s), y + fm.getAscent() / 2 - 1); // todo: why is -1 needed?
    }

    /**
     * "y = " or "f(x)" function used for graphing a continuous
     * or piecewise function defined by the user. By default, returns x.
     * To change this the user must enter their own expression.
     *
     * @param x x-value used to calculate f(x)
     * @return f(x) using expression
     */
    public double calculate(double x) {
        return Math.pow(x, 2) + 4 * x + 4;
    }

    /**
     * Checks to make sure that settings will not cause an
     * error when the graph is drawn. To be valid, xMax must
     * be greater than xMin and yMax must be greater than yMin.
     * height and width must be greater than zero.
     *
     * @return whether all settings are valid and graphing can proceed
     * @throws IndexOutOfBoundsException if a graph cannot be drawn
     * using the current settings
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
     * Draws graph and plots specified points on the BufferedImage.
     * Points outside window range will not be plotted.
     * Coordinates of points to draw on graph are passed in 2d array
     * where double[0][index] gives the x-coordinate of a point and
     * double[1][index] gives the corresponding y-coordinate. Points are
     * drawn as circles with radius plotWidth and plotColor. Passing
     * labelPoints as true will label each point with comma-separated
     * coordinates in parentheses next to the point (e.g. (x,y)).
     *
     * @param blank_image BufferedImage on which to draw graph
     * @param points x- and y-values of points to plot
     * @param labelPoints whether or not to label points with their coordinates
     * @return BufferedImage with graph drawn on it
     * @throws IndexOutOfBoundsException if double[0] is a different
     * size than double[1]
     */
    public BufferedImage drawGraph(BufferedImage blank_image, double[][] points, boolean labelPoints) throws IndexOutOfBoundsException {
        setHeightWidth(blank_image);

        if(validateSettings()) {
            return drawGraphOnGrid(drawGrid(blank_image), points, labelPoints);
        }

        return blank_image;
    }

    /**
     * Plots points on the specified BufferedImage,
     * which is assumed to be a pre-rendered grid with the same
     * window values as the current graph being drawn on it. Points
     * outside window range will not be plotted. Coordinates of points
     * to draw on graph are passed in 2d array where double[0][index]
     * gives the x-coordinate of a point and double[1][index] gives
     * the corresponding y-coordinate. Points are drawn as circles
     * with radius plotWidth and plotColor. Passing labelPoints as
     * true will label each point with comma-separated coordinates
     * in parentheses next to the point (e.g. (x,y)).
     *
     * @param grid BufferedImage on which to plot points
     * @param points x- and y-values of points to plot
     * @param labelPoints whether or not to label points with their coordinates
     * @return BufferedImage with points plotted on it
     * @throws IndexOutOfBoundsException if double[0] is a different
     * size than double[1]
     */
    public BufferedImage drawGraphOnGrid(BufferedImage grid,
             double[][] points, boolean labelPoints) throws IndexOutOfBoundsException { // todo: catch exception and throw one with message
        setHeightWidth(grid);
        if(validateSettings()) {
            Graphics2D graph = grid.createGraphics();
            graph.setColor(plotColor);
            graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            for(int i = 0; i < points[0].length; i++) {
                drawPoint(graph, points[0][i], points[1][i]);
                if(labelPoints) {
                    int[] coordinates = coordinateToPixel(points[0][i], points[1][i]);
                    drawCenteredString(graph, "(" + points[0][i] + "," + points[1][i] + ")", coordinates[0], coordinates[1]);
                }
            }
        }

        return grid;
    }

    /**
     * Draws graph and uses f(x) function in calculate(double x)
     * to plot points continuously from rangeLow to rangeHigh
     * on the graph. Points outside window range will not be plotted.
     * Points are drawn as circles with diameter plotWidth and plotColor. // todo: smarter algorithm?
     *
     * @param blank_image BufferedImage on which to draw the graph
     * @param rangeLow lowest x-value, inclusive, to use in calculating f(x) values
     * @param rangeHigh highest x-value, inclusive, to use in calculating f(x) values
     * @return BufferedImage with graph drawn on it
     */
    public BufferedImage drawGraph(BufferedImage blank_image, double rangeLow, double rangeHigh) { // todo: rename param blank_image?
        setHeightWidth(blank_image);
        blank_image = drawGrid(blank_image);
        return drawGraphOnGrid(blank_image, rangeLow, rangeHigh);
    }

    /**
     * Plots points on the specified BufferedImage using f(x)
     * function in calculate(double x) to plot points continuosly
     * form rangeLow to rangeHigh on the graph. Points outside
     * window range will not be plotted. Points are drawn as
     * circles with diameter plotWidth and plotColor.
     *
     * @param grid BufferedImage on which to plot points
     * @param rangeLow lowest x-value, inclusive, to use in calculating f(x) values
     * @param rangeHigh highest x-value, inclusive, to use in calculating f(x) values
     * @return BufferedImage with points plotted on it
     */
    public BufferedImage drawGraphOnGrid(BufferedImage grid, double rangeLow, double rangeHigh) { // todo: exclusive v. inclusive points
        setHeightWidth(grid);
        Graphics2D graph = grid.createGraphics();
        graph.setColor(plotColor);

        /* Calculate interval between pixels */
        float units_per_pxl = (float) (xMax - xMin) / width;

        int[] point0 = coordinateToPixel(rangeLow, calculate(rangeLow));

        /* Move from left to right, drawing lines between pixels */
        for(double i = rangeLow + units_per_pxl; i <= rangeHigh; i += units_per_pxl) {
            int[] point1 = coordinateToPixel(i, calculate(i));
            graph.draw(new Line2D.Double(point0[0], point0[1], point1[0], point1[1]));
            point0 = point1;
        }

        return grid;
    }

    /**
     * Draws point at coordinates (x,y) in graph space (not
     * user space) with radius plotWidth. Points outside of
     * window settings will not be plotted.
     *
     * @param graph Graphics2D object of graph being drawn on
     * @param x x-coordinate of point being plotted
     * @param y y-coordinate of point being plotted
     */
    private void drawPoint(Graphics2D graph, double x, double y) {
        /* Check to see if coordinates fall in graph range */
        if((x >= xMin && x <= xMax) && (y >= yMin && y <= yMax)) {
            /* Convert number coordinates to a coordinate on graph's user space */
            int[] px_coordinates = coordinateToPixel(x, y);
            /* Draw a point with diameter = plotWidth at specified coordinates in userspace.
             * Coordinates must be adjusted because filloval draws the shape in a box that
             * starts at the specified coordinates and goes down and right */
            graph.fillOval(px_coordinates[0] - plotWidth / 2, px_coordinates[1] - plotWidth / 2, plotWidth, plotWidth);
        }
    }

    /**
     * Converts coordinates of a point on the graph to coordinates of // todo: make sure ratio of pixels to units isn't > int limit
     * that pixel on the userspace of the Graphics2D object where drawing
     * takes place. Performs calculations based on current window settings.
     * Errors will occur if these are not up to date with the current graph.
     *
     * @param x x-coordinate of point in user space
     * @param y y-coordinate of point in user space
     * @return int[] where int[0] is x-coordinate and int[1] is y-coordinate of
     * point's location in userspace
     */
    private int[] coordinateToPixel(double x, double y) {
        /* Calculate pixels per unit using the formula
         * axis width (pixels) / axis range (units) */
        double x_px_unit = width / (xMax - xMin);
        double y_px_unit = height / (yMax - yMin);

        /* For each dimension, calculate distance away from min value on the axis
         * and multiply by the pixels per unit ratio */
        return new int[] {(int) ((x - xMin) * x_px_unit), (int) (height - (y - yMin) * y_px_unit)};
    }

    /**
     * Converts coordinates of a pixel in the userspace of
     * the Graphics2D object where drawing takes place to
     * coordinates of the pixel in graph space. Performs
     * calculations based on current window settings.
     * Errors will occur if these are not up to date with
     * the current graph.
     *
     * @param x x-coordinate of pixel in graph space
     * @param y y-coordinate of point in graph space
     * @return double[] where double[0] is x-coordinate
     * and double[1] is y-coordinate of point's location
     * in userspace
     */
    private double[] pixelToCoordinate(int x, int y) {
        /* Calculate units per pixel using the formula
         * axis range (units) / axis width (pixels)*/
        double x_unit_px = (xMax - xMin) / width;
        double y_unit_px = (yMax - yMin) / height;

        return new double[] { xMin + (x * x_unit_px), yMax - (y * y_unit_px) };
    }

    /**
     * Sets height and width fields based on height and width of
     * BufferedImage. This is used to keep the width and height
     * fields up to date with the BufferedImage being drawn on.
     *
     * @param to_draw BufferedImage upon which the graph will be drawn
     */
    private void setHeightWidth(BufferedImage to_draw) {
        width = to_draw.getWidth();
        height = to_draw.getHeight();
    }
}