/*
    Name: J. Anton Strickland
    Course: CNT 4714 Summer 2021
    Assignment title: Project 1 - Multi-threaded programming in Java
    Date: June 6, 2021

    Class: Conveyor
*/

public class Conveyor {

    private int conveyorID;
    private int stationID;
    private boolean inUse;

    public Conveyor(int id) {
        this.conveyorID = id;
        this.inUse = false;
    }

    public int getID() {
        return this.conveyorID;
    }

    public boolean isUsed() {
        return this.inUse;
    }

    public boolean load(Station station) {
        if (station.getID() == stationID) {
            System.out.println("Routing Station " + this.stationID + ": ...Active..."
                               + " moving packages into station on input conveyor "
                               + "C" + this.conveyorID + ".");
            return true;
        }

        else return false;
    }

    public boolean unload(Station station) {
        if (station.getID() == stationID) {
            System.out.println("Routing Station " + this.stationID + ": ...Active..."
                               + " moving packages out of station on output conveyor"
                               + " C" + this.conveyorID + ".");
            return true;
        }

        else return false;
    }
}
