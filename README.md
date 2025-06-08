Paper-link: https://www.researchgate.net/publication/392096816_Application_of_Swarm_Intelligence_in_Cloud_Task_Scheduling
# Swarm Task Scheduler: Project Description
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

## Install and Use `Swarm-Task-Scheduler`

To use the application these steps are recommended and provide possible 
further implementations.

1. Download the IDE Eclipse, if already installed proceed.
2. Import the directory where the `pom.xml`resides in Eclipse, there follow these steps:
   1. `File`
   2. `Import...`
   3. `Existing Maven Projects...`
   4. *Select the folder where the `pom.xml` resides.*
   5. *Press `import`*
3. After importing *right click on the project on the left-hand sied* 
4. Click `Run As`
5. Then select `Maven install` or `Maven build` to install all dependecies.
6. To test the application run `SchedulerApplication.java` by Eclipse or CLI.
