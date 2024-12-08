/********************************************************************/
/*                                                                  */
/********************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

/**
 * A particle which is used to find the optimal solution.
 */
typedef struct particle{
    Positon particle_positon;
    double velocity;
} Particle;

/**
 * The position of the particle in the system of the fitness function.
 */
typedef struct postion{
    int x,y;
} Positon;

/**
 * Default fintess function to test PSO.
 * @param x value to be caluclated for y
 * @return y value to be calculated to x
 */
double fitness_function(float x);

int main(int argc, char **argv){
    return 0;
}

int apply_pso(double *fitness_function){
    return 0;
}

double example_fitness_function(float x){
    return pow(x, 2.0) + x + 40;
}
