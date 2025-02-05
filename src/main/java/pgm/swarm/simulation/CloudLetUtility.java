package pgm.swarm.simulation;

import java.util.ArrayList;
import java.util.List;
import org.cloudsimplus.cloudlets.CloudletSimple;

/**
 * Utility class for handling Cloudlets in a cloud simulation environment using CloudSim.
 * Provides methods for creating and managing cloudlets.
 *
 * @author Lennart Hahner
 * @version 1.1.0
 */
public class CloudLetUtility {
    
    /** List containing all cloudlets */
    private ArrayList<CloudletSimple> cloudletList;

    /**
     * Default constructor initializing an empty cloudlet list.
     */
    public CloudLetUtility() {
        this.cloudletList = new ArrayList<>();
    }

    /**
     * Returns the current list of cloudlets.
     * 
     * @return List of CloudletSimple objects
     */
    public ArrayList<CloudletSimple> getCloudletList() {
        return cloudletList;
    }

    /**
     * Sets a new cloudlet list.
     * 
     * @param cloudletList List of CloudletSimple objects
     */
    public void setCloudletList(ArrayList<CloudletSimple> cloudletList) {
        this.cloudletList = cloudletList;
    }

    /**
     * Adds a new Cloudlet to the cloudlet list.
     * 
     * @param id Unique identifier of the cloudlet
     * @param length Computational length of the cloudlet (in MI)
     * @param pesNumber Number of processing elements required
     */
    public void addCloudlet(long id, long length, long pesNumber) {
        cloudletList.add(new CloudletSimple(id, length, pesNumber));
    }

    /**
     * Generates a specified number of cloudlets with given length and required PEs.
     * 
     * @param length Computational length of each cloudlet (in MI)
     * @param pesNumber Number of processing elements required per cloudlet
     * @param count Number of cloudlets to generate
     */
    public void generateCloudlets(int length, int pesNumber, int count) {
        for (int i = 0; i < count; i++) {
            cloudletList.add(new CloudletSimple(length, pesNumber));
        }
    }

    /**
     * Calculates the required computational resources for a cloudlet based on its index.
     * 
     * @param index Index of the cloudlet in the list
     * @return Computation requirement (length * number of PEs) or -1 if index is invalid
     */
    public double getTaskResourceRequirement(int index) {
        if (index < 0 || index >= cloudletList.size()) {
            throw new IndexOutOfBoundsException("Invalid cloudlet index.");
        }
        CloudletSimple cloudlet = cloudletList.get(index);
        return cloudlet.getLength() * cloudlet.getPesNumber();
    }
}
