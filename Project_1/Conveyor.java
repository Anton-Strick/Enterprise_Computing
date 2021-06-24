package Project_1;
/*
    Name: J. Anton Strickland
    Course: CNT 4714 Summer 2021
    Assignment title: Project 1 - Multi-threaded programming in Java
    Date: June 6, 2021

    Class: Conveyor
*/

import java.util.Random;
import java.util.concurrent.locks.*;

public class Conveyor {
    private int conveyorID;
    private int stationID;

    private Lock lock = new ReentrantLock();
    private Random generator = new Random();

    public Conveyor(int id) {
        conveyorID = id;
    }

    public int getID() {
        return this.conveyorID;
    }
    public int getStation() {
        return stationID;
    }

    public boolean getLock() {
        return lock.tryLock();
    }

    public void releaseLock() {
        lock.unlock();
    }

    public void load() {
        try {
            Thread.sleep(generator.nextInt(100));
        }

        catch (Exception ex) {}
    }

    public void unload() {
        try {
            Thread.sleep(generator.nextInt(100));
        }

        catch (Exception ex) {}
    }
}
