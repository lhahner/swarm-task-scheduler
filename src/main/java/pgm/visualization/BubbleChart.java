package pgm.visualization;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;


/**
 * This class is responsible for visualizing the Particle Swarm Optimization (PSO) swarm
 * and the particles contained within it. It provides methods for creating data series,
 * generating scatter plots, and displaying the results using Java Swing.
 * 
 * @author lennart.hahner
 * @version 2.0.0
 */
public class BubbleChart {
	
	private XYChart chart;
	private SwingWrapper<XYChart> sw;
	private XYSeries series;
    
	/**
	 * Builds a basic Bubble chart for visualization of several Algorithms.
	 * 
	 * @param title Define the Title of the graph.
	 * @param XAxisTitle Define which values are mapped on the X-Axis
	 * @param YAxisTitle Define which values are mapped on the Y-Axis
	 * @param chartSize Define the max-values for X- and Y-Axis
	 */
	public BubbleChart(String title, String XAxisTitle, String YAxisTitle, int chartSize) {
		// Create Chart
    	chart = new XYChartBuilder().width(600).height(500).title(title).xAxisTitle(XAxisTitle).yAxisTitle(YAxisTitle).build();
    	// Customize Chart
    	this.customizeChart(chartSize);
	}
	
    /**
     * This setup the default propreties for the chart.
     * 
     * @param chartSize Define the size of the chart so max for X-Axis and Y-Axis
     */
    public void customizeChart(int chartSize) {
    	chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
    	chart.getStyler().setChartTitleVisible(true);
    	chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
    	chart.getStyler().setMarkerSize(16);
    	chart.getStyler().setLegendVisible(false);
    	chart.getStyler().setXAxisMin(0.0);  // X-Axis Start
    	chart.getStyler().setXAxisMax((double)chartSize); // X-Axis End
    	chart.getStyler().setYAxisMax(0.0); // X-Axis End
    	chart.getStyler().setYAxisMax((double)chartSize); // X-Axis End
    	chart.getStyler().setXAxisDecimalPattern("#"); // Integer formatting for X-axis
    	chart.getStyler().setYAxisDecimalPattern("#"); // Integer formatting for Y-axis
    	chart.getStyler().setPlotGridLinesVisible(true);   // Enable grid
    	chart.getStyler().setChartBackgroundColor(java.awt.Color.WHITE);
    	chart.getStyler().setXAxisTickMarkSpacingHint(chartSize);
    	chart.getStyler().setYAxisTickMarkSpacingHint(chartSize);
    	chart.getStyler().setXAxisTicksVisible(false);
    }
   
    /**
     * Adds a new bubble to the chart.
     * 
     * @param name Define a name for the bubble, should be unqiue
     * @param posX, define the Positon X for the Bubble.
     * @param posY, define the Postion Y for the Bubble.
     */
    public void addBubble(String name, double posX, double posY) {
    	series = chart.addSeries(name, new double[]{posX, posX}, new double[]{posY, posY});
    	series.setMarker(SeriesMarkers.CIRCLE);
    }
    
    /**
     * Updates the values for the bubble to compute.
     * 
     * @param name Unique identfier for the bubble
     * @param posX The new position of the bubble at the X-Axis
     * @param posY The new postion of the bubble at the Y-Axis
     */
    public void updateBubble(String name, double posX, double posY) {
    	chart.updateXYSeries(name, new double[]{posX, posX}, new double[]{posY, posY}, null);
        sw.repaintChart();
    }
    
    /**
     * Display the Graph and Frame.
     */
    public void display() {
    	sw = new SwingWrapper<XYChart>(chart);
    	sw.displayChart();
    }
}