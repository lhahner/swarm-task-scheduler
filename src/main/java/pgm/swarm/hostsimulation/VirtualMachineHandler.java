package pgm.swarm.hostsimulation;

import java.util.ArrayList;

import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudsimplus.vms.*;

/**
 * Class used for virtual-machine handling inside
 * the application.
 * 
 * @author lennart.hahner
 * @version 1.0.0
 */
public class VirtualMachineHandler {
	
	/**
	 * The list of virtual-machines used.
	 */
	ArrayList<Vm> vmlist = new ArrayList<Vm>();
	
	/**
	 * Get the VMlist.
	 * 
	 * @return The VMlist of the current object.
	 */
	public ArrayList<Vm> getVmlist(){
		return vmlist;
	}
	
	/**
	 * Assign a different VMlist to the current one.
	 * 
	 * @param vmlist
	 */
	public void setVmlist(ArrayList<Vm> vmlist) {
		this.vmlist = vmlist;
	}
	
	/**
	 * Add a new Virtual machine with a MIPS and PES_NUMBER.
	 * 
	 * @param mips 
	 * @param pes_number
	 */
	public void addVmToList(int mips, long pes_number) {
		if(mips > 0 && pes_number > 0) {
			vmlist.add(new VmSimple(mips, pes_number));
		}
		else {
			return;
		}
	}
	
	/**
	 * Add a new Virtual machine with a specific id, MIPS and PES_NUMBER
	 * 
	 * @param vm_id Identification number to identify the VM.
	 * @param mips The number of Millions instructions per second assigned.
	 * @param pes_number The number of CPUs.
	 */
	public void addVmToList(long vm_id, double mips, long pes_number) {
		if(vm_id >= 0 && mips > 0 && pes_number > 0) {
			vmlist.add(new VmSimple(vm_id, mips, pes_number));
		}
		else {
			return;
		}
	}
	
	/**
	 * Deletes a certain Virtual Machine identified by the
	 * Id from the current objects VMlist.
	 * 
	 * @param vm_id The Id of the Virtual Machine to delete.
	 */
	public void delVmByIdFromList(long vm_id) {
		for(Vm vm : this.vmlist) {
			if(vm.getId() == vm_id) {
				this.vmlist.remove(vm);
			}
		}
	}
	
	/**
	 * Returns the current available Bandwidth of the VM.
	 * 
	 * @param index The VM from the list.
	 * @return The available Bandwidth.
	 */
	public long getAvailableBandwidth(int index) {
		return vmlist.get(index).getBw().getAllocatedResource();
	}
	
	/**
	 * Returns the current available Storage of the VM.
	 * 
	 * @param index The VM from the list.
	 * @return The current available storage.
	 */
	public long getAvailableStorage(int index) {
		return vmlist.get(index).getStorage().getAvailableResource();
	}
	
	/**
	 * Returns the current available ram of the VM.
	 * 
	 * @param index The VM from the list.
	 * @return The current available RAM.
	 */
	public long getAvailableRam(int index) {
		return vmlist.get(index).getRam().getAvailableResource();
	}
	
	/**
	 * TODO write a function to get the important stats of a VM.
	 */
	public void printAllVmStats() {
		for(Vm vm : this.vmlist) {
			System.out.println("CPU % Utilization: " + vm.getHost().getCpuUtilizationStats().getMean());
		}
	}
	
	/**
	 * Equivalent to the cloudlet statistics provider
	 * 
	 * @return mips * numbers of PEs used
	 */
	public double getVmProvidedResources(int index) {
		return this.vmlist.get(index).getMips() * this.vmlist.get(index).getPesNumber();
	}
	
}
