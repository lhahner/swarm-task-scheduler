package pgm.swarm.pso.core.enhanced;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import pgm.swarm.Agent;
import pgm.swarm.pso.core.Particle;

import java.util.List;

/**
 * A Multi-objective model for optimizing task scheduling considering three
 * aspects of task scheduling optimization.
 */
@Getter
@Setter
@AllArgsConstructor
@Log4j2
public class ParticleMultiobject extends Particle implements Agent {

    /**
     * This Method will return a pareto random optimal solution from the pareto
     * archive, used for the particle to follow. In Multi Objective Optimization,
     * all Pareto optimal solutions are stored in an archive and global best is
     * chosen from the Archive.
     *
     * @param paretoFront The Archive which stores the pareto optimal solution.
     * @param swarmSize The size of a swarm.
     * @return The position as array.
     */
   public double[] getParetoOptimalSolution(List<double[]> paretoFront, int swarmSize){
       return paretoFront.get(Math.round((long)Math.random()*swarmSize));
   }
}
