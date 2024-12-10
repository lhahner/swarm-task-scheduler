/**
 * @file pso.h
 * @author Lennart Hahner (lennart.hahner@gmail.com)
 * @brief This program will implement the particle-swarm-optimization
 * algorihtm.
 * @version 0.1
 * @date 2024-12-10
 * 
 * @copyright Copyright (c) 2024
 * 
 */
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define DIM 2
#define PARTICLES 5

/**
 * @brief This structure represents one particle, with a position as 
 * dimensional depended array and it's current velocity.
 * 
 */
typedef struct particle{
    double particle_positon[DIM];
    double velocity;
} Particle;

/**
 * @brief This example fitness function should be optimized.
 * 
 * @param x the parameter of the fitness function.
 * @return double the value of the y koordinate of a point.
 */
double fitness_function(float x);

/**
 * @brief This function will contain the code which mainly implements
 * the particle-swarm-optimization algorihtm.
 * 
 * @param fitness_function ...uncertain...
 */
int apply_pso(double *fitness_function);

/**
 * @brief Updates the position of the particle.
 * 
 * @param pos The current position of the particle.
 * @param v_p The current velocity.
 * @return double* The changed poistion of the particle.
 */
double* update_position(double pos[DIM], double* v_p);

/**
 * @brief Each particle should update its velocity and apply it 
 * on further postionalm changes of the particles while trying to
 * solve the problem.
 * 
 * @param v_p Current value of the velocity.
 * @param c_1 Confidence of a particle.
 * @param c_2 Confidence of the particle in tis neigbours.
 * @param r_1 First random number. 
 * @param r_2 Second random number.
 * @param pbest The best solution provided by the particle.
 * @param gbest The best solution of the swarm (global).
 * @param particles The particles to change the velocity.
 * @return double 
 */
double* update_velocity(double v_p, double c_1, double c_2, double r_1, double r_2, 
                        double pbest[DIM], double gbest[DIM], Particle particle);

/**
 * @brief Initalize standard valzes for the particles array.
 * 
 * @return Particle* the array of particles.
 */
Particle* init_particles();

/**
 * @brief Prints the array that contains all particles from session.
 * 
 * @param particles The particle array that should be displayed.
 * @return void* Returns null if the particles size is 0.
 */
void* print_particles(Particle* particles);
