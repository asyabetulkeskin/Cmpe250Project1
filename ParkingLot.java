import java.util.ArrayList;

public class ParkingLot {
    public int capacityConstraint;
    public ArrayList<Truck> waitingSection =new ArrayList<>();
    public ArrayList<Truck> readySection =new ArrayList<>();
    public int truckLimit;
    ParkingLot(int capacityConstraint, int truckLimit){
        this.capacityConstraint=capacityConstraint;
        this.truckLimit=truckLimit;
    }
    public boolean empty(){
        return waitingSection.size()+readySection.size()!=truckLimit;
    }

    //for adding a new truck
    public void adding(int ID, int maxCapacity){
        Truck truck = new Truck(ID, maxCapacity);
        waitingSection.add(truck);
    }
}
