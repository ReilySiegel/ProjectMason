package edu.wpi.teamo.algos;
import javafx.util.*;
import java.text.*;
import java.util.*;

public class TextDirManager {

    //Re-check later
    private static final double PIXEL_TO_METER = 0.1275;
    private static final DecimalFormat format = new DecimalFormat("##.00");

    /**
     * Provides a string that entails textual directions for a given path of nodes
     * @param pathToParse path of nodes to dictate a path for
     * @return the directions string
     */
    public static List<String> getTextualDirections(List<AlgoNode> pathToParse) {
        double accDistance = 0.0;
        List<String> directions = new LinkedList<>();
        Pair<Integer, Double> lastQuadrant = null;
        for(int i = 0; i < pathToParse.size(); i++) {
            //If user has arrived to destination
            if(i == pathToParse.size() - 1) {
                directions.add("You have arrived at your destination.");
                continue;
            }
            //If still travelling (there is a next node)

            //Calculate/update distance
            double currentDistance = (Math.pow((Math.pow(pathToParse.get(i + 1).getY() - pathToParse.get(i).getY(),2) +
                    Math.pow(pathToParse.get(i + 1).getX() - pathToParse.get(i).getX(),2)),0.5)) * PIXEL_TO_METER;
            currentDistance = Double.parseDouble(format.format(currentDistance));
            accDistance += currentDistance;

            //If we are changing floors
            if(!pathToParse.get(i + 1).getFloor().equals(pathToParse.get(i).getFloor())){
                directions.add("Proceed to floor " + pathToParse.get(i + 1).getFloor() + " and head to " + pathToParse.get(i + 1).getLongName());
                continue;
            }

            //If we have just started travelling (no initial facing direction)
            if(lastQuadrant == null) {
                directions.add("Proceed " + currentDistance+ " meters towards " + pathToParse.get(i + 1).getLongName());
                //Find next quadrant
                lastQuadrant = getQuadrant(pathToParse.get(i), pathToParse.get(i + 1));
                System.out.println("Quadrant: "+ lastQuadrant.getKey()+ " Angle: " + lastQuadrant.getValue());
                continue;
            }
            Pair<Integer,Double> currentQuadrant = getQuadrant(pathToParse.get(i), pathToParse.get(i + 1));
            System.out.println("Quadrant: "+ currentQuadrant.getKey()+ " Angle: " + currentQuadrant.getValue());
            //Determine direction based on quadrant and angle
            switch(lastQuadrant.getKey()){
                case 0:{
                    directions.add("Proceed " + currentDistance+ " meters towards " + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 1:{
                    if(currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 5 || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) > 0 && currentQuadrant.getKey() == 1))
                        directions.add("Proceed " + currentDistance+ " meters leftwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 6 || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) < 0 && currentQuadrant.getKey() == 1))
                        directions.add("Proceed " + currentDistance+ " meters rightwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 3)
                        directions.add("Proceed " + currentDistance+ " meters backwards to " + pathToParse.get(i + 1).getLongName());
                    else directions.add("Proceed " + currentDistance+ " meters forward to " + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 2:{
                    if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 6 || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) < 0 && currentQuadrant.getKey() == 2))
                        directions.add("Proceed " + currentDistance+ " meters leftwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 7 || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) > 0 && currentQuadrant.getKey() == 2))
                        directions.add("Proceed " + currentDistance+ " meters rightwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 4)
                        directions.add("Proceed " + currentDistance+ " meters backwards to " + pathToParse.get(i + 1).getLongName());
                    else directions.add("Proceed " + currentDistance+ " meters forward to " + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 3:{
                    if(currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 7 || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) > 0 && currentQuadrant.getKey() == 3))
                        directions.add("Proceed " + currentDistance+ " meters leftwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 8 || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) < 0 && currentQuadrant.getKey() == 3))
                        directions.add("Proceed " + currentDistance+ " meters rightwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 1)
                        directions.add("Proceed " + currentDistance+ " meters backwards to " + pathToParse.get(i + 1).getLongName());
                    else directions.add("Proceed " + currentDistance+ " meters forward to " + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 4:{
                    if(currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 8 || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) < 0 && currentQuadrant.getKey() == 4))
                        directions.add("Proceed " + currentDistance+ " meters leftwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 5 || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) > 0 && currentQuadrant.getKey() == 4))
                        directions.add("Proceed " + currentDistance+ " meters rightwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 2)
                        directions.add("Proceed " + currentDistance+ " meters backwards to " + pathToParse.get(i + 1).getLongName());
                    else directions.add("Proceed " + currentDistance+ " meters forward to " + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 5:{
                    if(currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 8 || currentQuadrant.getKey() == 3)
                        directions.add("Proceed " + currentDistance+ " meters leftwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 6 || currentQuadrant.getKey() == 2)
                        directions.add("Proceed " + currentDistance+ " meters rightwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 7)
                        directions.add("Proceed " + currentDistance+ " meters backwards to " + pathToParse.get(i + 1).getLongName());
                    else directions.add("Proceed " + currentDistance+ " meters forward to " + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 6:{
                    if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 5 || currentQuadrant.getKey() == 4)
                        directions.add("Proceed " + currentDistance+ " meters leftwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 7 || currentQuadrant.getKey() == 3)
                        directions.add("Proceed " + currentDistance+ " meters rightwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 8)
                        directions.add("Proceed " + currentDistance+ " meters backwards to " + pathToParse.get(i + 1).getLongName());
                    else directions.add("Proceed " + currentDistance+ " meters forward to " + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 7:{
                    if(currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 6 || currentQuadrant.getKey() == 1)
                        directions.add("Proceed " + currentDistance+ " meters leftwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 8 || currentQuadrant.getKey() == 4)
                        directions.add("Proceed " + currentDistance+ " meters rightwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 5)
                        directions.add("Proceed " + currentDistance+ " meters backwards to " + pathToParse.get(i + 1).getLongName());
                    else directions.add("Proceed " + currentDistance+ " meters forward to " + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 8:{
                    if(currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 7 || currentQuadrant.getKey() == 2)
                        directions.add("Proceed " + currentDistance+ " meters leftwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 5 || currentQuadrant.getKey() == 1)
                        directions.add("Proceed " + currentDistance+ " meters rightwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 6)
                        directions.add("Proceed " + currentDistance+ " meters backwards to " + pathToParse.get(i + 1).getLongName());
                    else directions.add("Proceed " + currentDistance+ " meters forward to " + pathToParse.get(i + 1).getLongName());
                    break;
                }
            }
            lastQuadrant = currentQuadrant;
        }
        return directions;
    }

    /**
     * Returns the quadrant the line segment is angled at
     * Quadrant Enumeration: 1: bottom right, 2: bottom left, 3: top left, 4: top right
     * Boundary Enumeration: 5: East, 6: South, 7: West, 8: North, 0: same point
     * @param current first node
     * @param next next 2nd node
     * @return a pair consisting of the quadrant enumeration and angle in radians
     */
    public static Pair<Integer, Double> getQuadrant(AlgoNode current, AlgoNode next) {
        System.out.println("Current LongName: "+current.getLongName() + "Next LongName: "+next.getLongName() +" Current X: " +current.getX() + " Next X: "+next.getX()+" Current Y: "+current.getY()+" Next Y: "+next.getY());
        double oppAdj = (double) (next.getY() - current.getY()) / (double) (next.getX() - current.getX());
        double angle = Math.atan(oppAdj);
        //Round Angle to zero if near boundary
        if(Math.abs(angle) < 0.1 || Math.abs(angle) > 1.5) angle = 0;
        //Check if the angle is less than pi/2 or greater than pi
        if(Math.abs(angle) % Math.PI < Math.PI / 2 && angle != 0) {
            //Quadrants 1 or 3
            if (angle > 0) {
                if (next.getY() - current.getY() > 0 || next.getX() - current.getX() > 0) return new Pair<>(1, angle);
                else return new Pair<>(3, angle);
            }
            //Quadrants 2 or 4
            else {
                if (next.getY() - current.getY() > 0 || next.getX() - current.getX() < 0) return new Pair<>(2, angle);
                else return new Pair<>(4, angle);
            }
        }
        //A boundary (angle default to 0)
        else {
            boolean vertical = Math.abs(next.getY() - current.getY()) > Math.abs(next.getX() - current.getX());
            if (next.getY() - current.getY() > 0 && vertical) return new Pair<>(6, 0.0);
            else if (next.getY() - current.getY() < 0 && vertical) return new Pair<>(8, 0.0);
            else if (next.getX() - current.getX() > 0 && !vertical) return new Pair<>(5, 0.0);
            else if (next.getX() - current.getX() < 0 && !vertical) return new Pair<>(7, 0.0);
            else return new Pair<>(0, 0.0);
        }

    }

}
