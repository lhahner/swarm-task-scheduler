package pgm.swarm.pso.core.evaluations;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

/**
 * Implements diverse types of evaluations functions to use.
 */
@Log4j2
public class Evaluation {
    /* Will provide the end result of the evaluation */
    private double objective;

    /**
     * The lower value of this criterion means that the
     * algorithm has been able to distribute the load better. Load balancing
     * value is obtained using Makespan/Avg, where Avg is the ratio of the total
     * processing of each processor.
     *
     * @param currentPosition The position of the particle representing a task-VM mapping.
     * @param cloudTasks The list of cloudTasks to be scheduled.
     * @param cloudVms The list of available VMs.
     * @return Impact on System performance.
     */
    public double evaluateLoadBalancing(double[] currentPosition, ArrayList<CloudletSimple> cloudTasks, ArrayList<Vm> cloudVms) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Calculate the latency, so the Task-Vm Assignment needs
     * to schedule the tasks, probably.
     *
     * @param currentPosition The position of the particle representing a task-VM mapping.
     * @param cloudTasks The list of cloudTasks to be scheduled
     * @param cloudVms The list of available VMs.
     * @return Latency of the message.
     */
    public double evaluateLatency(double[] currentPosition, ArrayList<CloudletSimple> cloudTasks, ArrayList<Vm> cloudVms){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Evaluates the scheduling quality by calculating the makespan for a given task-VM assignment.
     * A lower makespan indicates a better assignment.
     *
     * @param currentPosition The position of the particle representing a task-VM mapping.
     * @param cloudTasks The list of cloudTasks to be scheduled.
     * @param cloudVms The list of available VMs.
     * @return The calculated makespan value (lower is better). Returns a high default value if the assignment is invalid.
     */
    public double evaluateMakespan(List<Double> currentPosition, ArrayList<CloudletSimple> cloudTasks, ArrayList<Vm> cloudVms){
        if (Math.abs((int) Math.round(currentPosition.stream().mapToDouble(Double::doubleValue).sum())) >= cloudVms.size()
        || Math.abs((int) Math.round(currentPosition.stream().mapToDouble(Double::doubleValue).sum())) >= cloudTasks.size()) {
            return 10.0;
        }
        Vm vm;
        Cloudlet task;
        double makespan = 0;
        for(int i = 0; i < cloudTasks.size(); i++) {
            vm = cloudVms.get((Math.abs((int)(Math.round(currentPosition.get(i))))) >= cloudVms.size() ? cloudVms.size()-1 : (Math.abs((int)(Math.round(currentPosition.get(i))))));
            task = cloudTasks.get(i);
            makespan = makespan + (task.getLength() / (vm.getMips() * vm.getFreePesNumber()));
            }
        return makespan;
    }

    /**
     * Evaluates Costs whenever assigning a Task to a VM.
     *
     * @param currentPosition The position of the particle representing a task-VM mapping.
     * @param cloudTasks The list of cloudTasks to be scheduled.
     * @param cloudVms The list of available VMs.
     * @return The calculated makespan value (lower is better). Returns a high default value if the assignment is invalid.
     */
    public double evaluateCost(double[] currentPosition, ArrayList<CloudletSimple> cloudTasks, ArrayList<Vm> cloudVms){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Evaluates the time tasks need to execute by calculating the execution time for a given task-VM assignment. Here
     * we are minimizing.
     *
     * @param currentPosition The position of the particle representing a task-VM mapping.
     * @param cloudTasks The list of cloudTasks to be scheduled.
     * @param cloudVms The list of available VMs.
     * @return The calculated makespan value (lower is better). Returns a high default value if the assignment is invalid.
     */
    public double taskExecutionTime(List<Double> currentPosition, ArrayList<CloudletSimple> cloudTasks, ArrayList<Vm> cloudVms){
        if (Math.abs((int) Math.round(currentPosition.stream().mapToDouble(Double::doubleValue).sum())) >= cloudVms.size()
                || Math.abs((int) Math.round(currentPosition.stream().mapToDouble(Double::doubleValue).sum())) >= cloudTasks.size()) {
            return 10.0;
        }
        Vm vm;
        Cloudlet task;
        double executionTime=0;
        for(int i = 0; i < cloudTasks.size(); i++) {
            vm = cloudVms.get((Math.abs((int)(Math.round(currentPosition.get(i))))) >= cloudVms.size() ? cloudVms.size()-1 : (Math.abs((int)(Math.round(currentPosition.get(i))))));
            task = cloudTasks.get(i);
        }
        return executionTime;
    }

    /**
     * Will sum all task execution times for a VM and return the
     * total Task Execution time for that VM.
     *
     * @param taskExecutionTimes An Array of Size n which is the number of task for VM i
     * @return the total Execution time for that VM.
     */
    public double totalTaskExecutionTime(List<Double> taskExecutionTimes){
        return taskExecutionTimes.stream().mapToDouble(Double::doubleValue).sum();
    }

    /**
     * Calculates the task transferring time for a task to a VM.
     * If summed up for all tasks on a VM results in total task execution time.
     *
     * @param taskAmountData The amount of data that task i assigns to the VM k
     * @param vmBandwidthBetweenCenter The bandwidth between center and VM
     * @return Task transferring time
     */
    public double transferringTime(double taskAmountData, double vmBandwidthBetweenCenter){
        return taskAmountData/(vmBandwidthBetweenCenter);
    }

    /**
     * Summing all given transferring times.
     *
     * @param taskTransferringTimes An Array of Size n which is the number of task for VM i
     * @return The sum over all Tasks to get the transferring times
     */
    public double totalTransferringTime(List<Double> taskTransferringTimes){
        return taskTransferringTimes.stream().mapToDouble(Double::doubleValue).sum();
    }

    /**
     * Calculates the execution costs in USD per hour for one VM in a fixed period of time.
     *
     * @param costsForOneVm The cost of one unit VM for jth provider (USD per hour)
     * @param numberOfVms The total number of VMs supplied by provider k that have executed tasks in the period time
     * @param totalTaskExecutionTime The total Execution time for that VM.
     * @return The task execution cost for providers (USD per hour) is
     */
    public double executionCosts(double costsForOneVm, int numberOfVms, double totalTaskExecutionTime){
        return costsForOneVm *  numberOfVms * totalTaskExecutionTime;
    }

    /**
     * Sums over all individually calculated execution costs resulting in the total execution costs of that tasks.
     *
     * @param executionCosts number of calculated execution costs
     * @return sum of all execution costs
     */
    public double totalExecutionCosts(List<Double> executionCosts){
        return executionCosts.stream().mapToDouble(Double::doubleValue).sum();
    }
}
