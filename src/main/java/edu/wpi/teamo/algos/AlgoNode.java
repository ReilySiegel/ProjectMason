package edu.wpi.teamo.algos;
import java.util.LinkedList;

public class AlgoNode {
    private String longName, shortName, ID, floor;
    private  int xCoordinate, yCoordinate;
    private NodeType type;
    private  LinkedList<String> adjacencies;


    /**
     * adding gCost and hCost(initialize them to 0) for the use of A* Algorithm
     * adding parent AlgoNode to indicate the direction of the pathFinding
     */
    private int gCost = 0;
    private int hCost = 0;
    private AlgoNode parent;

    public AlgoNode(String ID, int xCoord, int yCoord, String Floor, NodeType nodeType, String longName, String shortName) {
        this.ID = ID;
        this.xCoordinate = xCoord;
        this.yCoordinate = yCoord;
        this.type = nodeType;
        this.floor = Floor;
        this.longName = longName;
        this.shortName = shortName;
        this.adjacencies = new LinkedList<>();
    }

    /**
     * node constructor that is essentially the barebones information a node needs to have to work in the Astar algorithim
     * @param ID  AlgoNode ID is used as a key in the
     * @param xCoord X coordinate locational Data
     * @param yCoord Y coordinate locational data
     */
    public AlgoNode(String ID, int xCoord, int yCoord) {
        this.ID = ID;
        this.xCoordinate = xCoord;
        this.yCoordinate = yCoord;
        this.type = null;
        this.longName = null;
        this.shortName = null;
        this.adjacencies = new LinkedList<>();

    }

    public String getID() {
        return ID;
    }
    public String getFloor() {
        return floor;
    }
    public NodeType getType() {
        return type;
    }
    public String getShortName() {
        return shortName;
    }
    public String getLongName() {
        return longName;
    }
    public LinkedList<String> getAdjacencies() {return adjacencies;}


    /**
     * adding getters and setters for gCost, hCost, and parent, a getter for fCost(sum of gCost and hCost)
     */
    public void set_gCost(int g){gCost = g;}
    public int get_gCost(){return gCost;}

    public void set_hCost(int h){hCost = h;}
    public int get_hCost(){return hCost;}

    public void setParent(AlgoNode n){parent = n;}
    public AlgoNode getParent(){return parent;}

    public int get_fCost(){return gCost + hCost;}


    /**
     * adds a node to the adjacency node ID list
     * @param ID the ID of the node to add
     */
     public void addAdjacencyByNodeId(String ID) {
         //make a node based on the information received from the database
         this.adjacencies.addLast(ID);
    }

     public void setX(int x)
     {
         this.xCoordinate = x;
     }
     public void setY(int y)
     {
         this.yCoordinate = y;
     }
     public int getX() { return xCoordinate; }
     public int getY(){ return yCoordinate; }


}
