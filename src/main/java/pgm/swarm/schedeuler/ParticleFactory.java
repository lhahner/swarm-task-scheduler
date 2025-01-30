package pgm.swarm.schedeuler;

/**
 * Factory class responsible for creating new Particle instances with specified initial values.
 * 
 * @author Lennart Hahner
 */
public class ParticleFactory {
    
    /**
     * Creates a new Particle with the specified initial position and velocity.
     * 
     * @param x The initial x-coordinate of the particle's position.
     * @param y The initial y-coordinate of the particle's position.
     * @param velo_x The initial x-component of the particle's velocity.
     * @param velo_y The initial y-component of the particle's velocity.
     * @return A new Particle instance if the provided values are valid, otherwise null.
     */
    public Agent getParticle(double x, double y, double velo_x, double velo_y) {
        // Ensure the position and velocity are non-negative
        if ((x >= 0 && y >= 0) && (velo_x >= 0 && velo_y >= 0)) {
            Particle particle = new Particle();
            particle.setPos(x, y);
            particle.setVelo(velo_x, velo_y);
            return particle;
        }
        return null;
    }
}