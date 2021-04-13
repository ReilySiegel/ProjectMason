package edu.wpi.teamo.algos;
import java.util.LinkedList;

public class Node {
    private String longName, shortName, ID, floor;
    private  int xCoordinate, yCoordinate;
    private NodeType type;
    private  LinkedList<Node> adjacencies;


    /**
     * adding gCost and hCost(initialize them to 0) for the use of A* Algorithm
     * adding parent Node to indicate the direction of the pathFinding
     */
    private int gCost = 0;
    private int hCost = 0;
    private Node parent;

    public Node(String ID, int xCoord, int yCoord, String Floor, NodeType nodeType, String longName, String shortName) {
        this.ID = ID;
        this.xCoordinate = xCoord;
        this.yCoordinate = yCoord;
        this.type = nodeType;
        this.floor = Floor;
        this.longName = longName;
        this.shortName = shortName;
        this.adjacencies = new LinkedList<Node>();
    }

    /**
     * node constructor that is essentially the barebones information a node needs to have to work in the Astar algorithim
     * @param ID  Node ID is used as a key in the
     * @param xCoord X coordinate locational Data
     * @param yCoord Y coordinate locational data
     */
    public Node(String ID, int xCoord, int yCoord) {
        this.ID = ID;
        this.xCoordinate = xCoord;
        this.yCoordinate = yCoord;
        this.type = null;
        this.longName = null;
        this.shortName = null;
        this.adjacencies = new LinkedList<Node>();

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
    public LinkedList<Node> getAdjacencies() {return adjacencies;}


    /**
     * adding getters and setters for gCost, hCost, and parent, a getter for fCost(sum of gCost and hCost)
     */
    public void set_gCost(int g){gCost = g;}
    public int get_gCost(){return gCost;}

    public void set_hCost(int h){hCost = h;}
    public int get_hCost(){return hCost;}

    public void setParent(Node n){parent = n;}
    public Node getParent(){return parent;}

    public int get_fCost(){return gCost + hCost;}


    /**
     * adds a node to the adjacecny node ID list
     * @param ID
     */
     public void addAdjacencyByNodeId(String ID) {
         //make a node based on the information received from the database
         Node adjacencyNode = null;
         adjacencyNode.ID = ID;

         this.adjacencies.addLast(adjacencyNode);
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
