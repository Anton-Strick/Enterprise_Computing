/*
    Name: J. Anton Strickland
    Course: CNT 4714 Summer 2021
    Assignment title: Project 1 - Multi-threaded programming in Java
    Date: June 6, 2021

    Class: Station
*/


public class Station implements Runnable {

    private Conveyor input;
    private Conveyor output;
    private int stationID;
    private int workload;

    public Station (int id) {
        this.stationID = id;
    }

    public void run() {
        displayReport();

        while (workload > 0) {
            doWork();
        }

        shutdown();
    }

    public int getID() {
        return this.stationID;
    }

    public void setInput(Conveyor c) {
        this.input = c;
        
    }

    public void displayReport() {
        System.out.println("Routing Station " + this.stationID + ": Input connection "
                           + "is set to conveyor number C" + this.input.getID() + ".");

        System.out.println("Routing Station " + this.stationID + ": Output connection "
                           + "is set to conveyor number C" + this.output.getID() + ".");

        System.out.println("Routing Station " + this.stationID + ": Workload set. "
                           + "Station " + this.stationID + " has a total of "
                           + this.workload + " package groups to move.");
    }

    public void setOutput(Conveyor c) {
        this.output = c;
    }

    public void setWorkload(int w) {
        this.workload = w;
    }

    private boolean getLocks() {
        
        return true;
    }

    private boolean doWork() {
        try {
            input.load(this);
        }

        catch (Exception ex) {

        }

        try {
            output.unload(this);
        }

        catch (Exception ex) {
            System.out.println("Station " + stationID + ": Unable to lock output "
                               + "conveyor C " + this.output.getID() 
                               + " - releasing lock on input conveyor C" 
                               + this.input.getID() + ".");
        }

        workload--;
        
        System.out.println("Routing Station " + stationID + ": Number of package " 
                           + "groups left to move is: " + workload + ".");
        return true;
    }

    private void shutdown() {
        System.out.println("* *Station " + stationID + ": Workload successfully "
                           + "completed. * * Going Idle!");
    }

}
