package edu.wpi.teamo.algos;
import java.util.HashMap;
import java.util.LinkedList;


public class MapSection {



        private HashMap<String,Node> map;
        private LinkedList<String> connectorIDs;
        public MapSection(int inititalCapacity, float LoadFactor)
        {
            map = new HashMap(inititalCapacity,LoadFactor);
            connectorIDs = new LinkedList();
        }
        public MapSection(int inititalCapacity)
        {
            map = new HashMap(inititalCapacity);
            connectorIDs = new LinkedList();
        }

    /**
     * adds a connector to the connector list  if it is already in the hashmap
     * @param ID
     * @return whether or not the nodeID  points to a node is in the hash map
     */
    public boolean addConnectorID(String ID)
        {
        return false;
        }
        public void addNode(Node node)
        {
            map.put(node.getID(),node);
        }

        public Node get(String id)
        {
            return map.get(id);
        }


}
