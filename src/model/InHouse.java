//Author: Matthew Manning
package model;


public class InHouse extends Part {
    
    //Machine ID non-inhertied attribute
    private int machineId;
    
    public InHouse(int id, int stock, int min, int max, String name, double price, int machineId) {
        super(id, stock, min, max, name, price);
        this.machineId = machineId;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
    
}
