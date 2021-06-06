/*
    Name: J. Anton Strickland
    Course: CNT 4714 Summer 2021
    Assignment title: Project 1 - Multi-threaded programming in Java
    Date: June 6, 2021

    Class: Station
    Description: Station.java contains code responsible accessing Conveyor instances in a
                 synchronous manner without using the java synchronized method. The work-
                 load 'w' of a given station is assigned by the driver class, resulting
                 in doWork() being called w times alongside other Station instances.
                 (launch.json defaults output to out.txt)
                 
*/

import java.util.Random;

public class Station implements Runnable {
/*====================================== Class Variables =====================================
        input     - The assigned input Conveyor of an instance. Assigned by the driver
                    class (Conveyor)
        output    - The assigned output Conveyor of an instance. Assigned by the driver
                    class (Conveyor)
                    
        stationID - A unique identifier for an instance (Int)
        workload  - The current number of items to process. Assigned by the driver class
                    (Int)

        generator - RNG used to simulate unpredictable delays (Random)
*/
    private Conveyor input;
    private Conveyor output;

    private int stationID;
    private int workload;

    private static Random generator = new Random();
//=================================== End Class Variables ==================================//

/*======================================= Base Methods =====================================*/
public Station (int id) {
    this.stationID = id; // Add conveyors after all have been initialized
}
//===================================== End Base Methods ===================================//

/*====================================== Public Methods ======================================
        run - inherited from Runnable. First displays basic info on the instance, then
              attempts to doWork until workload = 0. Sends notification on shutdown.
              INPUTS : VOID
              RETURN : VOID
*/
    public void run() {
        displayReport(); // Book keeping

        while (workload > 0) {
            doWork(); // Includes locking 
        }

        displayShutdown(); // Book keeping
    }
//==================================== End Public Methods ==================================//

/*====================================== Private Methods =====================================
        doWork - Main function. 
                 INPUTS : VOID
                 RETURN : VOID
                 
*/

/**
 * Attempts to aqcuire locks for input, THEN output to avoid
 * gridlock. If output is locked by another station, input is unlocked and
 * Thread sleeps for 0-200 nanos. Work times vary from 0-100 nanos.
 */
    private void doWork() {
        //-------------------------- Attempt Locks --------------------------//
        try {
            //--------------------- Successful 1st Lock ---------------------//
            if (input.getLock()) {
                displayLock(input, "input");
                //--------------------- Successful 2nd Lock -----------------//
                if (output.getLock()) {
                    //                *   Pre Pause Work   *                 //
                    displayLock(output, "output");
                    displayLoad(input, "into", "input");
                    input.load();
                    displayLoad(output, "out of", "output");
                    output.unload();

                    //              *   Post Pause Work   *                  //
                    displayWorkload();
                    displayUnlock(output, "output");
                    output.releaseLock();
                    displayUnlock(input, "input");
                    input.releaseLock();
                    workload--;
                } // End Successful 2nd Lock
                //--------------------- Failed 2nd Lock ---------------------//
                else {
                    displayFailedLock();
                    input.releaseLock();
                    Thread.sleep(generator.nextInt(200));
                } // End Failed 2nd Lock
            } // End Successful 1st Lock
        }  // End Attempt Locks

        catch (Exception ex) {
            System.out.println("Attempted to lock an already locked conveyor");
        }
    }
    //================================= End Private Methods ================================//
    
    /*=================================== Helper Functions ===================================
        - getX() functions are public instance functions which return the 
          specified variable
        
        - setX() functions are public instance functions which set the
          specified variable

        - displayX() functions write messages to stdout (out.txt using launch.json). Some
          take a string to allow important words to be specified.
    */
    public int getID() {
        return this.stationID;
    }

    public void setOutput(Conveyor c) {
        this.output = c;
    }

    public void setWorkload(int w) {
        this.workload = w;
    }

    public void setInput(Conveyor c) {
        this.input = c;
    }

    private void displayFailedLock() {
        System.out.println("Routing Station " + stationID + ": Unable to lock"
                    + " output conveyor C" + output.getID() + " - releasing lock on input"
                    + " conveyor C" + input.getID() + ".");
    }

    private void displayLoad(Conveyor c, String intoOutof, String io) {
        System.out.println("Routing Station " + stationID + ": ...Active... moving "
                               + "packages "+ intoOutof + " station on " + io
                               +" conveyor C" + c.getID() + ".");
    }

    private void displayLock(Conveyor c, String io) {
        System.out.println("Routing Station " + stationID + ": LOCK ACQUIRED! Now "
                           + "holding lock on " + io + " conveyor C" + c.getID() + ".");
    }

    private void displayReport() {
        System.out.println("Routing Station " + this.stationID + ": Input connection "
                           + "is set to conveyor number C" + this.input.getID() + ".");

        System.out.println("Routing Station " + this.stationID + ": Output connection "
                           + "is set to conveyor number C" + this.output.getID() + ".");

        System.out.println("Routing Station " + this.stationID + ": Workload set. "
                           + "Station " + this.stationID + " has a total of "
                           + this.workload + " package groups to move.");
    }

    private void displayShutdown() {
        System.out.println("* * Station " + stationID + ": Workload successfully "
                           + "completed. * * Going Idle!\n");
    }

    private void displayUnlock(Conveyor c, String io) {
        System.out.println("Station " + stationID + ": Unlocks " + io + " conveyor C" 
                           + c.getID() + "." );
    }

    private void displayWorkload() {
        System.out.println("Routing Station " + stationID + ": Number of package " 
        + "groups left to move is: " + workload + ".\n");
    }
    //================================ End Helper Functions ================================//
}
