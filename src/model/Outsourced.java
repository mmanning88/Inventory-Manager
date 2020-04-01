//Author: Matthew Manning
package model;


public class Outsourced extends Part{
    
    //Company name non-inherited attribute
    private String companyName;
    
    public Outsourced(int id, int stock, int min, int max, String name, double price, String companyName) {
        super(id, stock, min, max, name, price);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    
}
