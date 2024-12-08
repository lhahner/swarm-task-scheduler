# *Swarm Task Scheduler*: Project Description

## Target & Objectives
This project will compare some of the algorithms discussed in the paper *"Exploring Swarm Intelligence Optimization Techniques for Task Scheduling in Cloud Computing: Algorithms, Performance Analysis, and Future Prospects"* by Farida Siddiqi Prity, K. M. Aslam Uddin, and Nishu Nath. The main goal is to analyze the resource costs involved in task scheduling in cloud computing.

The paper provides examples of algorithms and refers to resource where they were implemented and tested. This project will review those results and examine how the algorithms were applied. Based on this analysis, one of the approaches will be chosen for implementation. The selected algorithm will be used to simulate a task-scheduling application to better understand its performance in practice.

The research and implementation process will be documented in a written report and presented through a formal presentation.

## Methods & Technologies

The approach for the project is to apply the Particle Swarm Optimization(PSO) Algorithm in a progam. 
Here the main focus is first to find a global minimum of a certain fitness function. The program
will be written in C or Java, since I am more familiar with these programming languages. After
implementation the application should be expanded to a testing cloud enviroment, e.g. CloudSim.
Here the Algorithm should be checked against cost-effiency. This is based on a recommandation
from the main-source mentioned in the beginning. Here the author refers to different Simulation
tools, also to CloudSim. 

Besides they propose some literature resource where the PSO was applied
with using the same simulation tools as this project does. *[p. 350]* So I will have some resources
to which I can refer whenever evaluating the usage of the Algorithm in different aspects besides
cost-effincy, e.g.: Reliabillity, Makespan, Cost, Security, Load balancing and Energy efficiency
which are also proposed by the author of the main-source. *[p. 349]*