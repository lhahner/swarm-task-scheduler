package pgm.swarm.simulation;

import java.util.ArrayList;

import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudsimplus.vms.*;

/**
 * This class manages virtual machine (VM) operations within the application.
 * It provides methods for adding, retrieving, and managing VMs.
 * 
 * @author Lennart Hahner
 * @version 1.0.0
 */
public class VirtualMachineUtility {
    
    /**
     * List of virtual machines managed by this utility.
     */
    private ArrayList<Vm> vmlist = new ArrayList<>();
    
    /**
     * Retrieves the list of virtual machines.
     * 
     * @return The list of VMs.
     */
    public ArrayList<Vm> getVmlist(){
        return vmlist;
    }
    
    /**
     * Sets a new list of virtual machines.
     * 
     * @param vmlist The new list of VMs.
     */
    public void setVmlist(ArrayList<Vm> vmlist) {
        this.vmlist = vmlist;
    }
    
    /**
     * Adds a new virtual machine with specified MIPS and number of PEs.
     * 
     * @param mips The processing power in MIPS.
     * @param pesNumber The number of processing elements (CPUs).
     */
    public void addVm(int mips, long pesNumber) {
        if(mips > 0 && pesNumber > 0) {
            vmlist.add(new VmSimple(mips, pesNumber));
        } else {
        	throw new NumberFormatException("MIPS and PesNumber needs to be larger than 0.");
        }

    }
    
    /**
     * Adds a new virtual machine with a specific ID, MIPS, and number of PEs.
     * 
     * @param vmId The unique identifier for the VM.
     * @param mips The processing power in MIPS.
     * @param pesNumber The number of processing elements (CPUs).
     */
    public void addVm(long vmId, double mips, long pesNumber) {
        if(vmId >= 0 && mips > 0 && pesNumber > 0) {
            vmlist.add(new VmSimple(vmId, mips, pesNumber));
        } else {
        	throw new NumberFormatException("MIPS and PesNumber needs to be larger than 0.");
        }
    }
    
    /**
     * Removes a virtual machine from the list using its ID.
     * 
     * @param vmId The ID of the VM to be removed.
     */
    public void removeVm(long vmId) {
        vmlist.removeIf(vm -> vm.getId() == vmId);
    }
    
    /**
     * Retrieves the available bandwidth of a specified VM.
     * 
     * @param index The index of the VM in the list.
     * @return The available bandwidth in MB/s.
     */
    public long getAvailableBandwidth(int index) {
    	if(vmlist.get(index).getBw().equals(null)) {
    		throw new NullPointerException("VM not found for index " + index);
    	}
        return vmlist.get(index).getBw().getAllocatedResource();
    }
    
    /**
     * Retrieves the available storage of a specified VM.
     * 
     * @param index The index of the VM in the list.
     * @return The available storage in MB.
     */
    public long getAvailableStorage(int index) {
    	if(vmlist.get(index).getBw().equals(null)) {
    		throw new NullPointerException("VM not found for index " + index);
    	}
        return vmlist.get(index).getStorage().getAvailableResource();
    }
    
    /**
     * Retrieves the available RAM of a specified VM.
     * 
     * @param index The index of the VM in the list.
     * @return The available RAM in MB.
     */
    public long getAvailableRam(int index) {
    	if(vmlist.get(index).getBw().equals(null)) {
    		throw new NullPointerException("VM not found for index " + index);
    	}
        return vmlist.get(index).getRam().getAvailableResource();
    }
    
    /**
     * Calculates the total processing power provided by a VM.
     * 
     * @param index The index of the VM in the list.
     * @return The total computational power (MIPS * number of PEs).
     */
    public double getVmProvidedResources(int index) {
        return this.vmlist.get(index).getMips() * this.vmlist.get(index).getPesNumber();
    }
    
    /**
     * Prints statistics for all virtual machines.
     * Currently, it prints CPU utilization statistics.
     */
    @Override
    public String toString() {
    	String vmStats = "";
        for(Vm vm : this.vmlist) {
        	vmStats = vmStats + "CPU % Utilization: " + vm.getHost().getCpuUtilizationStats().getMean();
        }
        return vmStats;
    }	
}
