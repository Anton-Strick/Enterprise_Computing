/*
    Name: J. Anton Strickland
    Course: CNT 4714 Summer 2021
    Assignment title: Project 1 - Multi-threaded programming in Java
    Date: June 6, 2021

    Class: Conveyor
*/

import java.util.concurrent.locks.*;

public class Conveyor {
    
    private boolean inUse;
    private int conveyorID;
    private int stationID;

    private Lock lock = new ReentrantLock();
    private Condition canLoad = lock.newCondition();
    private Condition canUnload = lock.newCondition();
    

    public Conveyor(int id) {
        conveyorID = id;
        inUse = false;
    }

    public int getID() {
        return this.conveyorID;
    }

    public boolean getLoadLock(Station station) {
        lock.lock();
        return true;
    }

    public boolean releaseLoadLock(Station station) {
        lock.unlock();
        return true;
    }

    public boolean load(Station station) {
        try {
            System.out.println("Routing Station " + this.stationID + ": ...Active..."
                               + " moving packages into station on input conveyor "
                               + "C" + this.conveyorID + ".");
            return true;
        }

        catch (Exception ex) { 
            return false; 
        }
    }

    public boolean unload(Station station) {
        try {
            System.out.println("Routing Station " + this.stationID + ": ...Active..."
                               + " moving packages out of station on output conveyor"
                               + " C" + this.conveyorID + ".");
            return true;
        }

        catch (Exception ex) {
            return false;
        }
    }
}
