package edu.wpi.teamo.algos;
import javafx.util.*;
import java.util.*;

//Temporary, refactor later if needed
public class TextDirManager {

    /**
     * Provides a string that entails textual directions for a given path of nodes
     * TODO: break this function down?
     * @param pathToParse path of nodes to dictate a path for
     * @return the directions string
     */
    public static String getTextualDirections(List<AlgoNode> pathToParse) {
        StringBuilder builder = new StringBuilder();
        Pair<Integer, Double> lastQuadrant = null;
        for(int i = 0; i < pathToParse.size(); i++) {
            //If user has arrived to destination
            if(i == pathToParse.size() - 1) {
                builder.append("You have arrived at your destination.");
                continue;
            }
            //If still travelling (there is a next node)
            //If we have just started travelling (no initial facing direction)
            if(lastQuadrant == null) {
                builder.append("Proceed towards ")
                        .append(pathToParse.get(i + 1).getID())
                        .append("\n");
                //Find next quadrant
                lastQuadrant = getQuadrant(pathToParse.get(i), pathToParse.get(i + 1));
                continue;
            }
            Pair<Integer,Double> currentQuadrant = getQuadrant(pathToParse.get(i), pathToParse.get(i + 1));

            //Determine direction based on quadrant and angle
            switch(lastQuadrant.getKey()){
                case 0:{
                    builder.append("Proceed towards ")
                            .append(pathToParse.get(i + 1).getID())
                            .append("\n");
                    break;
                }
                case 1:{
                    if(currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 5 || (lastQuadrant.getValue() - currentQuadrant.getValue() > 0 && currentQuadrant.getKey() == 1))
                        builder.append("Proceed leftwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else if(currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 6 || (lastQuadrant.getValue() - currentQuadrant.getValue() < 0 && currentQuadrant.getKey() == 1))
                        builder.append("Proceed rightwards to ")
                            .append(pathToParse.get(i + 1).getID())
                            .append("\n");
                    else if(currentQuadrant.getKey() == 3)
                        builder.append("Proceed backwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else builder.append("Proceed forward to ")
                            .append(pathToParse.get(i + 1).getID())
                            .append("\n");
                    break;
                }
                case 2:{
                    if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 6 || (lastQuadrant.getValue() - currentQuadrant.getValue() < 0 && currentQuadrant.getKey() == 2))
                        builder.append("Proceed leftwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else if(currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 7 || (lastQuadrant.getValue() - currentQuadrant.getValue() > 0 && currentQuadrant.getKey() == 2))
                        builder.append("Proceed rightwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else if(currentQuadrant.getKey() == 4)
                        builder.append("Proceed backwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else builder.append("Proceed forward to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    break;
                }
                case 3:{
                    if(currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 7 || (lastQuadrant.getValue() - currentQuadrant.getValue() > 0 && currentQuadrant.getKey() == 3))
                        builder.append("Proceed leftwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else if(currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 8 || (lastQuadrant.getValue() - currentQuadrant.getValue() < 0 && currentQuadrant.getKey() == 3))
                        builder.append("Proceed rightwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else if(currentQuadrant.getKey() == 1)
                        builder.append("Proceed backwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else builder.append("Proceed forward to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    break;
                }
                case 4:{
                    if(currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 8 || (lastQuadrant.getValue() - currentQuadrant.getValue() < 0 && currentQuadrant.getKey() == 4))
                        builder.append("Proceed leftwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 5 || (lastQuadrant.getValue() - currentQuadrant.getValue() > 0 && currentQuadrant.getKey() == 4))
                        builder.append("Proceed rightwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else if(currentQuadrant.getKey() == 2)
                        builder.append("Proceed backwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else builder.append("Proceed forward to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    break;
                }
                case 5:{
                    if(currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 8)
                        builder.append("Proceed leftwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 6)
                        builder.append("Proceed rightwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else if(currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 7)
                        builder.append("Proceed backwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else builder.append("Proceed forward to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    break;
                }
                case 6:{
                    if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 5)
                        builder.append("Proceed leftwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else if(currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 7)
                        builder.append("Proceed rightwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else if(currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 8)
                        builder.append("Proceed backwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else builder.append("Proceed forward to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    break;
                }
                case 7:{
                    if(currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 6)
                        builder.append("Proceed leftwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else if(currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 8)
                        builder.append("Proceed rightwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 5 || currentQuadrant.getKey() == 4)
                        builder.append("Proceed backwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else builder.append("Proceed forward to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    break;
                }
                case 8:{
                    if(currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 7)
                        builder.append("Proceed leftwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else if(currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 5)
                        builder.append("Proceed rightwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 6)
                        builder.append("Proceed backwards to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    else builder.append("Proceed forward to ")
                                .append(pathToParse.get(i + 1).getID())
                                .append("\n");
                    break;
                }
            }
            lastQuadrant = currentQuadrant;
        }
        return builder.toString();
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

        double angle = Math.tan((double) (next.getY() - current.getY()) / (double) (next.getX() - current.getX()));
        //Quadrants 1 or 3
        if(angle > 0) {
            if(next.getY() - current.getY() > 0 || next.getX() - current.getX() > 0) return new Pair<>(1,angle);
            else return new Pair<>(3,angle);
        }
        //Quadrants 2 or 4
        else if(angle < 0) {
            if(next.getY() - current.getY() > 0 || next.getX() - current.getX() < 0) return new Pair<>(2,angle);
            else return new Pair<>(4,angle);
        }
        //A boundary (angle default to 0)
        else {
            if(next.getY() - current.getY() > 0) return new Pair<>(6,0.0);
            else if(next.getY() - current.getY() < 0) return new Pair<>(8,0.0);
            else if(next.getX() - current.getX() > 0) return new Pair<>(5,0.0);
            else if(next.getX() - current.getX() < 0) return new Pair<>(7,0.0);
            else return new Pair<>(0,0.0);
        }
    }

}
