
/**
 * Write a description of class Item here.
 *
 * @author  Kaminski
 * @version Nov 1 2021
 */
public class Item
{
    // instance variables - replace the example below with your own
    private String description;
    private String weight;

    /**
     * Constructor for objects of class Item
     */
    public Item(String description, String weight)
    {
        // initialise instance variables
        this.description = description;
        this.weight = weight;
    }
    /**
     * Get item information
     */
    public String getItemInfo(){
        if(description.equals(""))
            return "";
        return " there is " + description + ", weight = " + weight;
    }

}
