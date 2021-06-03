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
        while (workload > 0) {
            if (!doWork()) {
                try {
                    wait(100);
                }

                catch (InterruptedException ex) {
                    System.out.println();
                }
            }
        }
    }

    public int getID() {
        return this.stationID;
    }

    public void setInput(Conveyor c) {
        this.input = c;
        System.out.println("Routing Station " + this.stationID + ": Input connection "
                           + "is set to conveyor number C" + this.input.getID() + ".");
    }

    public void setOutput(Conveyor c) {
        this.output = c;
        System.out.println("Routing Station " + this.stationID + ": Output connection "
                           + "is set to conveyor number C" + this.output.getID() + ".");
    }

    public void setWorkload(int w) {
        this.workload = w;
        System.out.println("Routing Station " + this.stationID + ": Workload set. "
                           + "Station " + this.stationID + " has a total of "
                           + this.workload + " package groups to move.");
    }

    private boolean doWork() {
        try {
            wait(100);
        }
        catch (Exception ex) {}

        workload--;
        
        System.out.println("Routing Station " + stationID + ": Number of package " 
                           + "groups left to move is: " + workload + ".");
        return true;
    }

}
