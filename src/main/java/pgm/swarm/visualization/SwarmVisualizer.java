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

/**
 * The Class should be used to visualize the 
 * Swarm and the containing particles.
 * 
 * @author lennart.hahner
 * @version 1.0.0 
 */
public class SwarmVisualizer {
	
	private static ArrayList<XYSeries> dataSerieslist = new ArrayList<XYSeries>();
	
	/**
	 * This method will set the
	 * data series list which tracks
	 * all the postions the Agent was
	 * at in computation time.
	 *  
	 * @param swarm The AgentSwarm used
	 * @param i The number of iteration where die Particles were computed
	 */
	public void setDataSerieslist(ParticleSwarm swarm, int i) {
		//Limiting
		if(dataSerieslist.size() > 100) {
			return;
		}
		XYSeries series = new XYSeries("Iteration" + i);
		
		for(Particle particle : swarm.getParticles()) {
			series.add(particle.getPos()[0], particle.getPos()[1]);
		}
		dataSerieslist.add(series);
	}
	
	/**
	 * This method will get the DataSeriesList
	 * which keept track of the postions of the agents.
	 * 
	 * @return the dataserieslist
	 */
	public ArrayList<XYSeries> getDataSerieslist() {
		return this.dataSerieslist;
	}
	
	/**
	 * Creates scattered plot to visualize one iteration
	 * of a swarm in Java Swing.
	 * 
	 * @param title The title of the Graph
	 * @param swarm The swarm to visualiue
	 * @param xAxisName The title of the x-Axis
	 * @param yAxisName The title of the y-Axis
	 * @param dataset The 
	 * @return
	 */
	public JFreeChart setScatterChartForPSO(String title, ArrayList<XYSeries> serieslist, String xAxisName, String yAxisName) {
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		for(XYSeries series: serieslist) {
			dataset.addSeries(series);
		}
		
		return ChartFactory.createScatterPlot(title, xAxisName, yAxisName, dataset);
	}
	
	/**
	 * This methods sets up the frame based upon 
	 * the chart.
	 * 
	 * @param chart JFreeChart which should be displayed.
	 * @return The JFrame in which the JFreeChart is included.
	 */
	public JFrame setupFrame() throws NullPointerException{
		
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
