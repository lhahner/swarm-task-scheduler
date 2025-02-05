package pgm.swarm.visualization;

import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import pgm.swarm.schedeuler.Particle;
import pgm.swarm.schedeuler.ParticleSwarm;
import pgm.swarm.schedeuler.Swarm;

/**
 * This class is responsible for visualizing the Particle Swarm Optimization (PSO) swarm
 * and the particles contained within it. It provides methods for creating data series,
 * generating scatter plots, and displaying the results using Java Swing.
 * 
 * @author lennart.hahner
 * @version 1.0.0
 */
public class SwarmVisualizer {
    
    private static ArrayList<XYSeries> dataSerieslist = new ArrayList<XYSeries>();

    /**
     * Sets the list of data series that track the positions of the particles
     * during each iteration of the swarm optimization process.
     * 
     * @param swarm The ParticleSwarm instance representing the current swarm.
     * @param i The iteration number of the swarm optimization.
     */
    public void setDataSerieslist(Swarm<Particle> swarm, int i) {
        // Limit the number of stored iterations to avoid excessive memory usage
        if (dataSerieslist.size() > 100) {
            return;
        }

        XYSeries series = new XYSeries("Iteration " + i);

        for (Particle particle : swarm.getAgents()) {
            series.add(particle.getPos()[0], particle.getPos()[1]);
        }
        dataSerieslist.add(series);
    }

    /**
     * Retrieves the list of data series, each representing the positions of the particles
     * during one iteration of the swarm optimization process.
     * 
     * @return The list of XYSeries objects representing the positions of particles over time.
     */
    public ArrayList<XYSeries> getDataSerieslist() {
        return this.dataSerieslist;
    }

    /**
     * Creates a scatter plot chart to visualize the positions of the particles
     * during a specific iteration of the Particle Swarm Optimization.
     * 
     * @param title The title of the chart.
     * @param serieslist The list of XYSeries objects containing the data to be plotted.
     * @param xAxisName The label for the x-axis.
     * @param yAxisName The label for the y-axis.
     * @return A JFreeChart object representing the scatter plot.
     */
    public JFreeChart setScatterChartForPSO(String title, ArrayList<XYSeries> serieslist, String xAxisName, String yAxisName) {

        XYSeriesCollection dataset = new XYSeriesCollection();

        for (XYSeries series : serieslist) {
            dataset.addSeries(series);
        }

        return ChartFactory.createScatterPlot(title, xAxisName, yAxisName, dataset);
    }

    /**
     * Sets up a JFrame to display the scatter plot chart.
     * 
     * @return A JFrame containing the chart.
     * @throws NullPointerException If the chart is null.
     */
    public JFrame setupFrame() throws NullPointerException {

        JFreeChart chart = setScatterChartForPSO("PSO", this.getDataSerieslist(), "VM", "Task");

        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setContentPane(chartPanel);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        return frame;
    }
}