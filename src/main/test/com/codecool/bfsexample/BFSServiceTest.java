package com.codecool.bfsexample;

import com.codecool.bfsexample.model.UserNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class BFSServiceTest {
    @Test
    public void testSimpleGraph(){
        UserNode origin = new UserNode("Polli", "Pudlipötty");
        UserNode destination = new UserNode("Clyde", "Béka");
        UserNode intermediary1 = new UserNode("Medve", "Uraság");
        UserNode intermediary2 = new UserNode("Eric", "Cartman");
        UserNode random1 = new UserNode("random", "1");
        UserNode random2 = new UserNode("random", "2");
        origin.addFriend(intermediary1);
        intermediary1.addFriend(intermediary2);
        intermediary1.addFriend(random1);
        intermediary1.addFriend(random2);
        intermediary2.addFriend(destination);

        assertEquals(3, new BFSService().getShortestDistanceBetween(origin, destination));
    }

    @Test
    public void testFriendOfFriends(){
        BFSService service = new BFSService();
        UserNode origin = new UserNode("origin", "node");
        origin.addFriend(new UserNode("node", "1"));
        origin.addFriend(new UserNode("node", "2"));
        origin.addFriend(new UserNode("node", "3"));
        origin.addFriend(new UserNode("node", "4"));
        Integer i = 5;
        for (UserNode userNode: origin.getFriends()) {
            userNode.addFriend(new UserNode("node", i.toString()));
            i++;
        }

        assertEquals(8, service.getFriendsOfFriends(origin, 4).size());
    }

    @Test
    public void testFindPathInSimpleGraph(){
        BFSService service = new BFSService();

        UserNode origin = new UserNode("1", "1");
        UserNode node2 = new UserNode("2", "2");
        UserNode node3 = new UserNode("3", "3");
        UserNode node4 = new UserNode("4", "4");
        origin.addFriend(node2);
        origin.addFriend(node3);
        origin.addFriend(node4);
        node2.addFriend(node3);
        UserNode node5 = new UserNode("5", "5");
        UserNode node6 = new UserNode("6", "6");
        UserNode node7 = new UserNode("7", "7");
        UserNode node8 = new UserNode("8", "8");
        UserNode node9 = new UserNode("9", "9");
        node3.addFriend(node5);
        node3.addFriend(node6);
        node5.addFriend(node6);
        node6.addFriend(node8);
        node4.addFriend(node7);
        node7.addFriend(node8);
        node8.addFriend(node9);

        List<List<UserNode>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.get(0).add(node8);
        expected.get(0).add(node7);
        expected.get(0).add(node4);
        expected.get(0).add(origin);
        expected.add(new ArrayList<>());
        expected.get(1).add(node8);
        expected.get(1).add(node6);
        expected.get(1).add(node3);
        expected.get(1).add(origin);
        assertEquals(expected, service.getShortestPathBetween(origin, node8));
    }

}