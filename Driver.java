/*
    Name: J. Anton Strickland
    Course: CNT 4714 Summer 2021
    Assignment title: Project 1 - Multi-threaded programming in Java
    Date: June 6, 2021

    Class: Driver
    Description: Driver.java contains code responsible for FileIO and high-level
                 control of the simulated factory floor as outlined in project1.pdf.
                 The scale of a Driver instance is variable, containing n instances of
                 the Stations class, and n-1 instances of the Conveyor class. n is defined
                 as the number found on the first line of config.txt
*/

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

public class Driver {
/*===================================== Class Variables =====================================      
        conveyors - contains n instances of the Conveyor class. n is defined in the first
                    line in config.txt (ArrayList)

        stations  - contains n instances of the Stations class. n is defined in the first
                    line in config.txt. (ArrayList)
        
*/
    private ArrayList<Conveyor> conveyors = new ArrayList<Conveyor>();
    private ArrayList<Station> stations = new ArrayList<Station>();

    private ExecutorService threads = Executors.newCachedThreadPool();

    private File configFile;

//=================================== End Class Variables ====================================

/*====================================== Public Methods ======================================
        main      - contains code regarding class variable init and fileIO
                    INPUTS : String[] args
                    RETURNS: VOID
        
        getOutput - 
                    INPUTS : int outputID
                    RETURNS: String
*/

    public static void main(String[] args) {
        new Driver("config.txt");
    }

    public Driver (String configDirectory) {
        //------------------------ Load Config ----------------------- 
        loadConfig(configDirectory);

        //--------------------- Start Simulation ---------------------
        for (Station s : stations) {
            threads.execute(s);
        }
    }
//==================================== End Public Methods ====================================

/*====================================== Private Methods =====================================
        loadConfig   - loads input config contained with config.txt and creates the n stations
                       and conveyors detailed within.
                       INPUTS : 
                            String configDirectory - Directory of a file containing n + 1 
                                                     lines, where n is the number of elements 
                                                     to be simulated. n is found on the first 
                                                     line of the file. Each line contains the
                                                     workload for the station at that index.
                       RETURNS: VOID

*/
    private void loadConfig(String configDirectory) {
        try {
            this.configFile = new File(configDirectory);

            Scanner scan = null;
            scan = new Scanner(configFile);

            //------------ Create Stations/Conveyors ------------
            int numStations = scan.nextInt();

            for (int i = 0; i < numStations; i++) {
                stations.add(new Station(i));
                conveyors.add(new Conveyor(i));
            }

            //------------------ Configuration ------------------
            Station currStation = null;
            Conveyor currInput = null;
            Conveyor currOutput = null;
            for (int i = 0; i < numStations; i++) {
                currStation = stations.get(i);
                currInput = conveyors.get(i);
                currOutput = conveyors.get((i + numStations - 1) % numStations);

                currStation.setInput(currInput);
                currStation.setOutput(currOutput);
                currStation.setWorkload(scan.nextInt());
            }
            
            scan.close();
        }
        catch (Exception IOException) {
            System.out.println("Error reading config in " + configDirectory);
        }
    }
//==================================== End Private Methods ===================================
}

