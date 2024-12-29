# Swarm Task Scheduler: Project Description
## Target & Objectives

This project aims to implement a swarm optimization algorithm and 
compare it to one or two other swarm optimization algorithms discussed
in the paper "Exploring Swarm Intelligence-Optimization Techniques 
for Task Scheduling in Cloud Computing: Algorithms, Performance 
Analysis, and Future Prospects" by Farida Siddiqi Prity, 
K. M. Aslam Uddin, and Nishu Nath.

The paper provides examples of algorithms and mentions the resources 
where they were implemented and tested. This project will utilize the 
results and references from the mentioned paper for further analysis, 
evaluation criteria, and proposed technologies to use.

The research and implementation process will be thoroughly documented 
in a written report and presented through a formal presentation.
  
## Methods & Technologies

The approach involves applying the Particle Swarm Optimization (PSO) 
algorithm in a program. The primary objective is to identify the 
global minimum of a given fitness function, which involves the 
scheduling of tasks in a cloud environment to keep the usage of 
performance indicators low, e.g., resource cost or memory consumption.

The program will be developed in either C or Java, as these 
programming languages offer familiarity and reliability. After 
implementation, the application will be tested in a cloud simulation 
environment, such as CloudSim. The algorithm’s performance will then 
be evaluated, with a focus on cost efficiency, as recommended in the 
mentioned main source.

## Implementing the PSO in task schedueling

The target should be to optimze the assignment of tasks to VMs based
on criteria like makespan and energy-efficincy.
The evaluation of a particle at a certain position will apply the
considered task handeled by a VM and afterwards calculate the 
criteria like makespan etc.
If the criteria is larger then the current which is the global
or local best than the assignment stays in the current state.

To really use the PSO Algorithm each position of the particles needs
to be considered a certain mapping which results in different VM and 
task combinations. Position 0,1 could symbolize a certain Vm or task
to optimize. Either way to number of particles is always the same 
length as the tasks or vms depending on the design decsisions made.
