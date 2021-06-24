package Project_1;
/*
    Name: J. Anton Strickland
    Course: CNT 4714 Summer 2021
    Assignment title: Project 1 - Multi-threaded programming in Java
    Date: June 6, 2021

    Class: Driver
    Description: Driver.java contains code responsible for FileIO and high-level control
                 of the simulated factory floor as outlined in project1.pdf. The Scale of
                 a Driver instance is variable, containing n instances of the Stations
                 class, and n-1 instances of the Conveyor class. n is defined as the number
                 found on the first line of config.txt
*/

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public class Driver {
/*====================================== Class Variables =====================================
        conveyors  - contains n instances of the Conveyor class. n is defined in the 
                     first line in config.txt (ArrayList)
        stations   - contains n instances of the Stations class. n is defined in the 
                     first line in config.txt. (ArrayList)
        
        threads    - used in an enhanced for loop to run all generated instances of
                     Station. (ExecutorService)

        configFile - a file containing n + 1 integers separated by newline characters.
                     n is defined as the integer on line 0, each following line
                     contains the workload for stations[line - 1]
        
*/
    private ArrayList<Conveyor> conveyors = new ArrayList<Conveyor>();
    private ArrayList<Station> stations = new ArrayList<Station>();

    private ExecutorService threads = Executors.newCachedThreadPool();

    private File configFile;

//=================================== End Class Variables ==================================//

/*======================================= Base Methods =====================================*/
    public static void main(String[] args) {
        new Driver("config.txt");
    }
/**
 * Runs simulation based on provided config when instantiated.
 * @param configDirectory Directory of desired configFile
 */
    public Driver (String configDirectory) {
        //------------------------ Load Config ----------------------- 
        loadConfig(configDirectory);

        //--------------------- Start Simulation ---------------------
        for (Station s : stations) {
            threads.execute(s);
        }

        //--------------------- Proper Shutdown ----------------------
        threads.shutdown();
    }
//===================================== End Base Methods ===================================//

/*====================================== Private Methods ===================================*/

/**
* loads input config contained within a specified directory and creates the n
* stations and conveyors detailed within.
* @param configDirectory Directory of desired configFile
*/
    private void loadConfig(String configDirectory) {
        try {
            this.configFile = new File(configDirectory);

            Scanner scan = null;
            scan = new Scanner(configFile);

            //------------------ Create Stations/Conveyors ------------------//
            int numStations = scan.nextInt();

            for (int i = 0; i < numStations; i++) {
                stations.add(new Station(i));
                conveyors.add(new Conveyor(i));
            }

            //------------------------ Configuration ------------------------//
            Station currStation = null;
            Conveyor currInput = null;
            Conveyor currOutput = null;
            for (int i = 0; i < numStations; i++) {
                currStation = stations.get(i);
                currOutput = conveyors.get(i);
                currInput = conveyors.get((i + numStations - 1) % numStations);

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
//==================================== End Private Methods =================================//
}

