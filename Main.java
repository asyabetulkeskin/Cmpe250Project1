//Asya Betul Keskin, 2022400021

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AvlTree avlTreeMain = new AvlTree();
        File inFile = new File(args[0]);
        File outFile = new File(args[1]);
        PrintStream outStream;
        try {
            outStream = new PrintStream(outFile);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            return;
        }
        Scanner reader;
        try {
            reader = new Scanner(inFile);
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find input file");
            return;
        }
        while (reader.hasNextLine()){
            String line = reader.nextLine();
            String[] parts = line.split(" ");
            if (parts[0].equals("create_parking_lot")){
                int capacityConstraint = Integer.parseInt(parts[1]);
                int truckLimit = Integer.parseInt(parts[2]);
                avlTreeMain.createNewParkingLot(capacityConstraint, truckLimit);
            }
            else if (parts[0].equals("delete_parking_lot")){
                int capacityConstraint = Integer.parseInt(parts[1]);
                avlTreeMain.deleteParkingLot(capacityConstraint);
            }
            else if (parts[0].equals("load")){
                int capacity = Integer.parseInt(parts[1]);
                int loadAmount = Integer.parseInt(parts[2]);
                avlTreeMain.load(capacity, loadAmount, outStream);

            }
            else if (parts[0].equals("add_truck")){
                int truckID = Integer.parseInt(parts[1]);
                int capacity = Integer.parseInt(parts[2]);
                outStream.println(avlTreeMain.addingNewTruck(truckID,capacity));
            }
            else if (parts[0].equals("ready")){
                int capacity = Integer.parseInt(parts[1]);
                AvlTree.Node ready = avlTreeMain.readyCommand(capacity);
                if (ready==null){outStream.println(-1);}
                else {outStream.println(ready.parkingLot.readySection.getLast().ID+" "+avlTreeMain.getCapacityConstraint(ready));}
            }
            else if (parts[0].equals("count")){
                int capacity = Integer.parseInt(parts[1]);
                int sum = avlTreeMain.truckCount(capacity);
                outStream.println(sum);
            }

        }

        reader.close();
        outStream.close();
    }
}
