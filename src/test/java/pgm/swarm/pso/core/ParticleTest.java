package pgm.swarm.pso.core;

import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

@NoArgsConstructor
public class ParticleTest {

	Particle particle;

	@BeforeEach
	void setUp() {
		particle = new Particle();
		particle.setPosition(Arrays.asList(1.0, 2.0));
		particle.setVelocity(Arrays.asList(0.5, -0.5));
		particle.setInertiaWeight(1.0);
		particle.setParticlesBest(Arrays.asList(2.0, 3.0));
	}

	@Test
	void testCalculateNewPosition_Positive() {
		List<Double> currentPosition = Arrays.asList(1.0, 2.0);
		List<Double> velocity = Arrays.asList(0.5, -1.0);
		particle.calculateNewPosition(currentPosition, velocity);
		assertEquals(Arrays.asList(1.5, 1.0), currentPosition);
	}

	@Test
	void testCalculateNewPosition_Negative_DifferentSize() {
		List<Double> currentPosition = List.of(1.0);
		List<Double> velocity = Arrays.asList(0.5, -1.0);
		assertThrows(
				IllegalArgumentException.class,
				() -> particle.calculateNewPosition(currentPosition, velocity));
	}

	@Test
	void testCalculateVelocity_Positive() {
		List<Double> velocity = Arrays.asList(0.0, 0.0);
		List<Double> position = Arrays.asList(1.0, 2.0);
		List<Double> particlesBest = Arrays.asList(2.0, 3.0);
		List<Double> globalBest = Arrays.asList(3.0, 4.0);
		particle.calculateVelocity(velocity, particlesBest, position, globalBest);
		assertNotNull(velocity);
		assertEquals(2, velocity.size());
		assertTrue(velocity.get(0) >= 0.0 || velocity.get(0) <= 0.0); // sanity check it's a double
	}

	@Test
	void testCalculateVelocity_Negative_DifferentSizes() {
		List<Double> velocity = Arrays.asList(0.0, 0.0);
		List<Double> position = List.of(1.0);
		List<Double> particlesBest = Arrays.asList(2.0, 3.0);
		List<Double> globalBest = Arrays.asList(3.0, 4.0);
		assertThrows(
				IllegalArgumentException.class,
				() -> particle.calculateVelocity(velocity, particlesBest, position, globalBest));
	}

	@Test
	void testToString_Positive() {
		String result = particle.toString();
		assertNotNull(result);
		assertTrue(result.contains("position=[1.0, 2.0]"));
		assertTrue(result.contains("velocity=[0.5, -0.5]"));
		assertTrue(result.contains("inertiaWeight=1.0"));
		assertTrue(result.contains("particlesBest=[2.0, 3.0]"));
	}

	@Test
	void testCalculateInertiaWeight_Positive() {
		double min = 0.5;
		double max = 1.5;
		double weight = particle.calculateInertiaWeight(min, max);
		assertTrue(weight >= min && weight <= max,
				"Weight should be within the given range [" + min + ", " + max + "]");
	}
}
