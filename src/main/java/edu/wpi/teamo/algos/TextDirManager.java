package edu.wpi.teamo.algos;
import edu.wpi.teamo.App;
import javafx.util.*;
import java.text.*;
import java.util.*;

public class TextDirManager {

    private static final DecimalFormat FORMAT = new DecimalFormat("###");

    //Strings from Locale


    /**
     * Provides a string that entails textual directions for a given path of nodes.
     * @param pathToParse path of nodes to dictate a path for
     * @return the list of direction strings
     */
    public static List<String> getTextualDirections(List<AlgoNode> pathToParse, boolean metric) {
        List<String> directions = new LinkedList<>();
        Pair<Integer, Double> lastQuadrant = null;
        double pixel_to_unit;
        String unit, nextNode, dist;

        //If null input:
        if (pathToParse == null) {
            directions.add(App.resourceBundle.getString("key.no_path_provided"));
            return directions;
        }
        //If there is no path:
        if(pathToParse.size() == 0) {
            directions.add(App.resourceBundle.getString("key.no_path_exists"));
            return directions;
        }

        //Set units
        if(metric) {
            unit = App.resourceBundle.getString("key.meters");
            pixel_to_unit = 0.1275;
        }
        else {
            unit = App.resourceBundle.getString("key.feet");
            pixel_to_unit = 0.4183;
        }

        //If there is a path:
        for(int i = 0; i < pathToParse.size(); i++) {
            //If user has arrived to destination
            if(i == pathToParse.size() - 1) {
                directions.add(App.resourceBundle.getString("key.destination_arrival"));
                continue;
            }
            //If still travelling (there is a next node)

            //Calculate/update distance
            double currentDistance = (Math.pow((Math.pow(pathToParse.get(i + 1).getY() - pathToParse.get(i).getY(),2) +
                    Math.pow(pathToParse.get(i + 1).getX() - pathToParse.get(i).getX(),2)),0.5)) * pixel_to_unit;
            currentDistance = Double.parseDouble(FORMAT.format(currentDistance));

            if(currentDistance < 1) dist = App.resourceBundle.getString("key.short_distance");
            else dist = String.valueOf(currentDistance);

            if(currentDistance == 1 && !metric) unit = App.resourceBundle.getString("key.foot");

            //If we are changing floors
            if(!pathToParse.get(i + 1).getFloor().equals(pathToParse.get(i).getFloor())){
                directions.add(App.resourceBundle.getString("key.floor_switch_1") + pathToParse.get(i + 1).getFloor() + " " + App.resourceBundle.getString("key.floor_switch_2") + pathToParse.get(i + 1).getLongName() + ".");
                lastQuadrant = getQuadrant(pathToParse.get(i), pathToParse.get(i + 1));
                System.out.println("Quadrant: "+ lastQuadrant.getKey()+ " Angle: " + lastQuadrant.getValue());
                continue;
            }

            //If we have just started travelling (no initial facing direction)
            if(lastQuadrant == null) {
                directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.towards") + pathToParse.get(i + 1).getLongName() + ".");
                //Find next quadrant
                lastQuadrant = getQuadrant(pathToParse.get(i), pathToParse.get(i + 1));
                System.out.println("Quadrant: "+ lastQuadrant.getKey()+ " Angle: " + lastQuadrant.getValue());
                continue;
            }

            Pair<Integer,Double> currentQuadrant = getQuadrant(pathToParse.get(i), pathToParse.get(i + 1));
            System.out.println("Quadrant: "+ currentQuadrant.getKey()+ " Angle: " + currentQuadrant.getValue());

            //Determine if next node is intermediate, omit from directions if it is
            if(!pathToParse.get(i + 1).getLongName().toLowerCase().contains("intermediate"))
                nextNode = " " + App.resourceBundle.getString("key.to_2") + pathToParse.get(i + 1).getLongName() + ".";
            else nextNode = ".";

            //Determine direction based on quadrant and angle
            switch(lastQuadrant.getKey()){
                case 0:{
                    directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.towards") + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 1:{
                    //Slight turns
                    if(Math.abs(Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue())) <= 0.2 && lastQuadrant.getKey().equals(1)){
                        if(Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) > 0 && currentQuadrant.getKey() == 1)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.slight_left_turn") + nextNode);
                        else if(Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) < 0 && currentQuadrant.getKey() == 1)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.slight_right_turn") + nextNode);
                        else directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.forwards") + nextNode);
                    }
                    //Backwards turns
                    else if(currentQuadrant.getKey().equals(3) || currentQuadrant.getKey().equals(7) || currentQuadrant.getKey().equals(8)) {
                        if(currentQuadrant.getKey().equals(8) || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) < 0 && currentQuadrant.getKey().equals(3)))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.back_left_turn") + nextNode);
                        else if(currentQuadrant.getKey().equals(7) || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) > 0 && currentQuadrant.getKey().equals(3)))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.back_right_turn") + nextNode);
                        else directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.backwards") + nextNode);
                    }
                    //Normal Turns
                    else{
                        if(currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 5 || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) > 0 && currentQuadrant.getKey() == 1))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.left_turn") + nextNode);
                        else if(currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 6 || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) < 0 && currentQuadrant.getKey() == 1))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.right_turn") + nextNode);
                        else directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.forwards") + nextNode);
                    }
                    break;
                }
                case 2:{
                    //Slight turns
                    if((Math.abs(Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue())) <= 0.2 || Math.abs(Math.abs(lastQuadrant.getValue()) - (Math.PI / 2)) <= 0.2) && lastQuadrant.getKey().equals(2)){
                        if(Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) < 0 && currentQuadrant.getKey() == 2)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.slight_left_turn") + nextNode);
                        else if(Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) > 0 && currentQuadrant.getKey() == 2)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.slight_right_turn") + nextNode);
                        else directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.forwards") + nextNode);
                    }
                    //Backwards turns
                    else if(currentQuadrant.getKey().equals(4) || currentQuadrant.getKey().equals(8) || currentQuadrant.getKey().equals(5)){
                        if(currentQuadrant.getKey().equals(5) || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) < 0 && currentQuadrant.getKey().equals(4)))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.back_left_turn") + nextNode);
                        else if(currentQuadrant.getKey().equals(8) || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) > 0 && currentQuadrant.getKey().equals(4)))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.back_right_turn") + nextNode);
                        else directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.backwards") + nextNode);
                    }
                    //Normal turns
                    else {
                        if (currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 6 || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) < 0 && currentQuadrant.getKey() == 2))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.left_turn") + nextNode);
                        else if (currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 7 || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) > 0 && currentQuadrant.getKey() == 2))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.right_turn") + nextNode);
                        else
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.forwards") + nextNode);
                    }
                    break;
                }
                case 3:{
                    //Slight turns
                    if(Math.abs(Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue())) <= 0.2 && lastQuadrant.getKey().equals(3)){
                        if(Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) > 0 && currentQuadrant.getKey() == 3)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.slight_left_turn") + nextNode);
                        else if(Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) < 0 && currentQuadrant.getKey() == 3)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.slight_right_turn") + nextNode);
                        else directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.forwards") + nextNode);
                    }
                    //Backwards turns
                    else if(currentQuadrant.getKey().equals(1) || currentQuadrant.getKey().equals(6) || currentQuadrant.getKey().equals(5)){
                        if(currentQuadrant.getKey().equals(6) || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) < 0 && currentQuadrant.getKey().equals(1)))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.back_left_turn") + nextNode);
                        else if(currentQuadrant.getKey().equals(5) || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) > 0 && currentQuadrant.getKey().equals(1)))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.back_right_turn") + nextNode);
                        else directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.backwards") + nextNode);
                    }
                    //Normal turns
                    else {
                        if (currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 7 || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) > 0 && currentQuadrant.getKey() == 3))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.left_turn") + nextNode);
                        else if (currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 8 || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) < 0 && currentQuadrant.getKey() == 3))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.right_turn") + nextNode);
                        else
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.forwards") + nextNode);
                    }
                    break;
                }
                case 4:{
                    //Slight turns
                    if((Math.abs(Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue())) <= 0.2 || Math.abs(Math.abs(lastQuadrant.getValue()) - (Math.PI / 2)) <= 0.2) && lastQuadrant.getKey().equals(4)){
                        if(Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) < 0 && currentQuadrant.getKey() == 4)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.slight_left_turn") + nextNode);
                        else if(Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) > 0 && currentQuadrant.getKey() == 4)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.slight_right_turn") + nextNode);
                        else directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.forwards") + nextNode);
                    }
                    //Backwards turns
                    else if(currentQuadrant.getKey().equals(2) || currentQuadrant.getKey().equals(6) || currentQuadrant.getKey().equals(7)){
                        if(currentQuadrant.getKey().equals(7) || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) < 0 && currentQuadrant.getKey().equals(2)))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.back_left_turn") + nextNode);
                        else if(currentQuadrant.getKey().equals(6) || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) > 0 && currentQuadrant.getKey().equals(2)))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.back_right_turn") + nextNode);
                        else directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.backwards") + nextNode);
                    }
                    //Normal turns
                    else {
                        if (currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 8 || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) < 0 && currentQuadrant.getKey() == 4))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.left_turn") + nextNode);
                        else if (currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 5 || (Math.abs(lastQuadrant.getValue()) - Math.abs(currentQuadrant.getValue()) > 0 && currentQuadrant.getKey() == 4))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.right_turn") + nextNode);
                        else
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.forwards") + nextNode);
                    }
                    break;
                }
                case 5:{
                    //Slight turns
                    if(Math.abs(currentQuadrant.getValue()) < 0.2 && Math.abs(currentQuadrant.getValue()) != 0){
                        if(currentQuadrant.getKey() == 4)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.slight_left_turn") + nextNode);
                        else if(currentQuadrant.getKey() == 1)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.slight_right_turn") + nextNode);
                        else
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.forwards") + nextNode);
                    }
                    //Backwards
                    else if(currentQuadrant.getKey().equals(2) || currentQuadrant.getKey().equals(3) || currentQuadrant.getKey().equals(7)){
                        if(currentQuadrant.getKey().equals(3))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.back_left_turn") + nextNode);
                        else if(currentQuadrant.getKey().equals(2))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.back_right_turn") + nextNode);
                        else directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.backwards") + nextNode);
                    }
                    //Normal turns
                    else{
                        if(currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 8)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.left_turn") + nextNode);
                        else if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 6)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.right_turn") + nextNode);
                        else directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.forwards") + nextNode);
                    }

                    break;
                }
                case 6:{
                    //Slight turns
                    if(Math.abs(currentQuadrant.getValue()) > 1.3){
                        if(currentQuadrant.getKey() == 1)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.slight_left_turn") + nextNode);
                        else if(currentQuadrant.getKey() == 2)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.slight_right_turn") + nextNode);
                        else
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.forwards") + nextNode);
                    }
                    //Backwards
                    else if(currentQuadrant.getKey().equals(4) || currentQuadrant.getKey().equals(3) || currentQuadrant.getKey().equals(8)){
                        if(currentQuadrant.getKey().equals(4))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.back_left_turn") + nextNode);
                        else if(currentQuadrant.getKey().equals(3))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.back_right_turn") + nextNode);
                        else directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.backwards") + nextNode);
                    }
                    //Normal turns
                    else {
                        if (currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 5)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.left_turn") + nextNode);
                        else if (currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 7)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.right_turn") + nextNode);
                        else
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.forwards") + nextNode);
                    }
                    break;
                }
                case 7:{
                    //Slight turns
                    if(Math.abs(currentQuadrant.getValue()) < 0.2 && Math.abs(currentQuadrant.getValue()) != 0){
                        if(currentQuadrant.getKey() == 2)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.slight_left_turn") + nextNode);
                        else if(currentQuadrant.getKey() == 3)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.slight_right_turn") + nextNode);
                        else
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.forwards") + nextNode);
                    }
                    //Backwards
                    else if(currentQuadrant.getKey().equals(4) || currentQuadrant.getKey().equals(1) || currentQuadrant.getKey().equals(5)){
                        if(currentQuadrant.getKey().equals(1))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.back_left_turn") + nextNode);
                        else if(currentQuadrant.getKey().equals(4))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.back_right_turn") + nextNode);
                        else directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.backwards") + nextNode);
                    }
                    //Normal turns
                    else {
                        if (currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 6)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.left_turn") + nextNode);
                        else if (currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 8)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.right_turn") + nextNode);
                        else
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.forwards") + nextNode);
                    }
                    break;
                }
                case 8:{
                    //Slight turns
                    if(Math.abs(currentQuadrant.getValue()) > 1.3){
                        if(currentQuadrant.getKey() == 3)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.slight_left_turn") + nextNode);
                        else if(currentQuadrant.getKey() == 4)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.slight_right_turn") + nextNode);
                        else
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.forwards") + nextNode);
                    }
                    //Backwards
                    else if(currentQuadrant.getKey().equals(2) || currentQuadrant.getKey().equals(1) || currentQuadrant.getKey().equals(6)){
                        if(currentQuadrant.getKey().equals(2))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.back_left_turn") + nextNode);
                        else if(currentQuadrant.getKey().equals(1))
                            directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.back_right_turn") + nextNode);
                        else directions.add(App.resourceBundle.getString("key.proceed") + dist+ " " + unit + App.resourceBundle.getString("key.backwards") + nextNode);
                    }
                    //Normal turns
                    else {
                        if (currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 7)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.left_turn") + nextNode);
                        else if (currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 5)
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.right_turn") + nextNode);
                        else
                            directions.add(App.resourceBundle.getString("key.proceed") + dist + " " + unit + App.resourceBundle.getString("key.forwards") + nextNode);
                    }
                    break;
                }
            }
            lastQuadrant = currentQuadrant;
        }
        return directions;
    }

    /**
     * Returns the quadrant the line segment is angled at.
     * Quadrant Enumeration: 1: bottom right, 2: bottom left, 3: top left, 4: top right.
     * Boundary Enumeration: 5: East, 6: South, 7: West, 8: North, 0: same point.
     * @param current first node
     * @param next next 2nd node
     * @return a pair consisting of the quadrant enumeration and angle in radians
     */
    public static Pair<Integer, Double> getQuadrant(AlgoNode current, AlgoNode next) {
        System.out.println("Current LongName: "+current.getLongName() + "Next LongName: "+next.getLongName() +" Current X: " +current.getX() + " Next X: "+next.getX()+" Current Y: "+current.getY()+" Next Y: "+next.getY());
        double oppAdj = (double) (next.getY() - current.getY()) / (double) (next.getX() - current.getX());
        double angle = Math.atan(oppAdj);
        //Round Angle to zero if near boundary
        if(Math.abs(angle) < 0.13 || Math.abs(angle) > 1.47) angle = 0;
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
//App.resourceBundle.getString("key.backwards")