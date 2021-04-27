package edu.wpi.teamo.algos;

import edu.wpi.teamo.database.map.Edge;

import java.util.LinkedList;

public class BFS {

        private LinkedList<AlgoNode> nodes;
        private LinkedList<Edge> edges;
        private String startID;
        private String endID;

        public BFS(LinkedList<AlgoNode> nodes, LinkedList<Edge> edges, String startNodeID, String endNodeID) {
        this.startID = startNodeID;
        this.endID = endNodeID;
        this.nodes = nodes;
        this.edges = edges;
    }




}
