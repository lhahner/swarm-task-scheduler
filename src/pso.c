/**
 * @file pso.c
 * @author your name (you@domain.com)
 * @brief 
 * @version 0.1
 * @date 2024-12-10
 * 
 * @copyright Copyright (c) 2024
 * 
 */
#include "./includes/pso.h"

int main(int argc, char **argv){

    Particle* particles = malloc(sizeof(Particle) * PARTICLES);
    particles = init_particles();
    print_particles(particles);

    double pbest[DIM] = { 0, 0 };
    double gbest[DIM] = { 0, 0 };
    double* vel = update_velocity(particles[0].velocity, 2, 2, rand(), rand(), 
                                  pbest, gbest, particles[0]);
    printf("[%f %f]", vel[0], vel[1]);

    free(particles);
    return 0;
}

int apply_pso(double *fitness_function){
    return 0;
}

double* update_position(double pos[DIM], double* v_p){
    double* tmp_pos = malloc(sizeof(double)*DIM);
    
    for(int i = 0; i<DIM; i++){
        tmp_pos[i] = pos[i] + v_p[i];
    }
    
    return tmp_pos;
}

double* update_velocity(double v_p, double c_1, double c_2, double r_1, double r_2, 
                        double pbest[DIM], double gbest[DIM], Particle particle){

    double* tmp_vel = malloc(sizeof(double)*DIM);

    for(int i = 0; i<DIM; i++){
        tmp_vel[i] = v_p + c_1*r_1*(pbest[i]-particle.particle_positon[i])+
        c_2*r_2*(gbest[i]-particle.particle_positon[i]);
    }

    return tmp_vel;
}

double example_fitness_function(float x){
    return pow(x, 2.0) + x + 40;
}

Particle* init_particles(){
    Particle* tmp_particles = malloc(sizeof(Particle) * PARTICLES);

    for(int i = 0; i<PARTICLES; i++){
        for(int j = 0; j<DIM; j++){
            tmp_particles[i].particle_positon[j] = random();
        } 
        tmp_particles[i].velocity = random();
    }
    return tmp_particles;
}

void* print_particles(Particle* particles){
    if(sizeof(particles) == 0){
        return NULL;
    }
    fprintf(stdout, "{ \n");
    for(int i = 0; i<PARTICLES; i++){
        fprintf(stdout, "[ %f, %f ]; %f \n", 
            particles[i].particle_positon[0],
            particles[i].particle_positon[1],
            particles[i].velocity);
    }
    fprintf(stdout, "} \n");
    return NULL;
}