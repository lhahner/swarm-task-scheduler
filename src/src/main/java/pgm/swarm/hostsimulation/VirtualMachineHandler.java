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
			vmlist.add(new VmSimple(mips, pes_number, new CloudletSchedulerTimeShared()));
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
}
