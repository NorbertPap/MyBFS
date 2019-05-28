package com.codecool.bfsexample;

import com.codecool.bfsexample.model.UserNode;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BFSService {
    public int getShortestDistanceBetween(UserNode origin, UserNode destination) {
        if(origin==null || destination == null) return -1;
        if(origin==destination) return 0;

        //I use a linked hash map instead of a queue, just so that I can map every node to its depth in the search
        //To get the depth of the node, write: nodesAndDepths.get(currentNode)
        LinkedHashMap<UserNode, Integer> nodesAndDepths = new LinkedHashMap<>();
        //Keeping track of all nodes that have been in the hash map at some point
        List<UserNode> checkedPeople = new ArrayList<>();
        //Setting up the starting point
        nodesAndDepths.put(origin, 0);
        UserNode currentNode = origin;

        while(!nodesAndDepths.isEmpty()){
            //Checks if the current node is the destination, if it is, then returns its value from the hash map(its depth)
            if(currentNode == destination){
                return nodesAndDepths.get(currentNode);
            }
            //Else, it puts every adjacent node into the hash map(already checked elements can't be put in, and also ones that are already in, to account for circles in the graph)
            for (UserNode adjacentNode: getUncheckedFriends(currentNode, checkedPeople)) {
                nodesAndDepths.putIfAbsent(adjacentNode, nodesAndDepths.get(currentNode)+1);
            }
            //After checking the current node and putting its friends and their depths into the hash map,
            //removes the current node from the hash map, puts it into the checked list, and moves the current node to the next element in the hash map
            checkedPeople.add(currentNode);
            nodesAndDepths.remove(currentNode);
            currentNode = nodesAndDepths.keySet().iterator().next();
        }
        return -1;
    }

    private Set<UserNode> getUncheckedFriends(@NotNull UserNode currentNode, List<UserNode> checkedPeople){
        Set<UserNode> uncheckedFriends = new HashSet<>();
        for (UserNode friend: currentNode.getFriends()) {
            if(!checkedPeople.contains(friend)) uncheckedFriends.add(friend);
        }
        return uncheckedFriends;
    }

    public Set<UserNode> getFriendsOfFriends(UserNode origin, int depth){
        LinkedHashMap<UserNode, Integer> nodesAndDepths = new LinkedHashMap<>();
        Set<UserNode> returnSet = new HashSet<>();
        nodesAndDepths.put(origin, 0);
        UserNode currentNode = origin;

        while(currentNode!=null && nodesAndDepths.get(currentNode)<=depth-1){
            //Puts every friend of currentNode into the hash map
            for (UserNode friend: currentNode.getFriends()) {
                nodesAndDepths.putIfAbsent(friend, nodesAndDepths.get(currentNode)+1);
            }
            //Moves currentNode from the hash map into the set, moves currentNode to the next element in line
            returnSet.add(currentNode);
            nodesAndDepths.remove(currentNode);
            currentNode = nodesAndDepths.keySet().iterator().next();
        }

        //Return set contains every node that was in the hash map once, including origin, so it removes itself from its set of friends before returning
        returnSet.remove(origin);
        return returnSet;
    }

    public List<List<UserNode>> getShortestPathBetween(UserNode origin, UserNode destination){
        LinkedHashMap<UserNode, List<List<UserNode>>> nodesAndShortestPathsToThem = new LinkedHashMap<>();
        UserNode currentNode = origin;

        nodesAndShortestPathsToThem.put(origin, new ArrayList<>());
        nodesAndShortestPathsToThem.get(origin).add(new ArrayList<>());
        nodesAndShortestPathsToThem.get(origin).get(0).add(origin);

        while (currentNode!=null && currentNode!=destination) {
            for (UserNode adjacentNode: currentNode.getFriends()) {
                //If the adjacent node to the current node doesn't have a path to it, or its existing path is as long as the current path to it
                //Note that there is no way that in a BFS search we find paths later that are shorter than the first path we registered(at least not in undirected, unweighted graphs)
                if(nodesAndShortestPathsToThem.get(adjacentNode) == null || nodesAndShortestPathsToThem.get(adjacentNode).get(0).size()-1==nodesAndShortestPathsToThem.get(currentNode).get(0).size()){
                    //Creating the first path if it doesn't exist
                    List<List<UserNode>> outerList = new ArrayList<>();
                    List<UserNode> innerList = new ArrayList<>();
                    outerList.add(innerList);
                    if(nodesAndShortestPathsToThem.get(adjacentNode)==null){
                        nodesAndShortestPathsToThem.put(adjacentNode, outerList);
                    } else {
                        nodesAndShortestPathsToThem.get(adjacentNode).add(innerList);
                    }
                    //Adding all the paths of the current node to the paths of its adjacent node plus the current node
                    for (List<UserNode> path: nodesAndShortestPathsToThem.get(currentNode)) {
                        nodesAndShortestPathsToThem.get(adjacentNode).get(nodesAndShortestPathsToThem.get(adjacentNode).size()-1).add(adjacentNode);
                        nodesAndShortestPathsToThem.get(adjacentNode).get(nodesAndShortestPathsToThem.get(adjacentNode).size()-1).addAll(path);
                    }
                }
            }
            //Moving currentNode forward in the linked hash map
            boolean canStop = false;
            for (UserNode userNode: nodesAndShortestPathsToThem.keySet()){
                if(canStop){
                    currentNode = userNode;
                    break;
                }
                if(userNode == currentNode) canStop = true;
            }
        }
        return nodesAndShortestPathsToThem.get(destination);
    }
}
