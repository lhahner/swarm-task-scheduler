package pgm.swarm.visualization;

import java.util.ArrayList;

import pgm.swarm.schedeuler.PSO.Particle;

public class BubbleVisualizationStrategy implements VisualizationStrategy{

	private BubbleChart bubbleChart;
	
	@Override
	public void visualize(ArrayList<Particle> particles, int dimension) {
		bubbleChart = new BubbleChart("Particle Swarm Optimization", "Task", "VM", dimension);

      int k = 0;
      for(Particle particle : particles) {
    	  bubbleChart.addBubble("Particle " + k, particle.getPos()[0], particle.getPos()[1]);
      	k++;
      }
      bubbleChart.display();	
	}

	@Override
	public void updateAndVisualize(ArrayList<Particle> particles) {
      int z = 0;
      for(Particle particle : particles) {
      	
      	try {
  			Thread.sleep(60);
  		} catch (InterruptedException e) {
  			e.printStackTrace();
  		}
      	bubbleChart.updateBubble("Particle " + z, particle.getPos()[0], particle.getPos()[1]);
      	z++;
      }
	}
	
	
}
