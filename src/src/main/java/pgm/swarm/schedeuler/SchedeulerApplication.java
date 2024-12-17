package pgm.swarm.schedeuler;

import java.util.ArrayList;
import java.util.List;

public class SchedeulerApplication {
	
	static ArrayList<Particle> particles;
	
	public static void main(String[] args) {
		
		initParticles();
		
		for(Particle particle : particles) {
			System.out.println(particle.toString());
		}
	}
	
	public static void initParticles() {
		
		particles = new ArrayList<Particle>();
		
		for(int i = 0; 5>i; i++) {
			particles.add((Particle)new ParticleFactory()
					.getParticle(Math.random(), Math.random()));
		}
	}

}
