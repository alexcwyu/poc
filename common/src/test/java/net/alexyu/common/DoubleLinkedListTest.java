package net.alexyu.common;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by alexc on 5/4/2017.
 */
public class DoubleLinkedListTest {

    @Test
    public void should_get_null_for_empty_list() {
        DoubleLinkedList list = new DoubleLinkedList();
        (new LinkedList<Integer>()).addLast(1);

        //(new Stack()).
        (new ArrayDeque<>()).add(1);
        (new HashMap<>()).put(1,1);
        assertTrue(list.isEmpty());
        assertTrue(list.size() == 0);
        assertTrue(list.peekFirst() == null);
        assertTrue(list.peekLast() == null);
        assertTrue(list.removeFirst() == null);
        assertTrue(list.removeLast() == null);
    }

    @Test
    public void should_add_first() {
        DoubleLinkedList list = new DoubleLinkedList();
        list.addFirst(1);
        assertFalse(list.isEmpty());
        assertTrue(list.size() == 1);
        assertEquals(1, list.peekFirst());

        list.addFirst(2);
        assertTrue(list.size() == 2);
        assertEquals(2, list.peekFirst());

        list.addFirst(3);
        assertTrue(list.size() == 3);
        assertEquals(3, list.peekFirst());

    }

    @Test
    public void should_add_last() {
        DoubleLinkedList<Integer> list = new DoubleLinkedList();
        list.addLast(1);
        assertFalse(list.isEmpty());
        assertTrue(list.size() == 1);
        assertEquals(1, list.peekLast(), 0);

        list.addLast(2);
        assertTrue(list.size() == 2);
        assertEquals(2, list.peekLast(), 0);

        list.addLast(3);
        assertTrue(list.size() == 3);
        assertEquals(3, list.peekLast(), 0);

        for(Integer i : list){
            System.out.println(i);
        }
    }

    @Test
    public void should_remove_first_item() {
        DoubleLinkedList list = new DoubleLinkedList();
        list.addFirst(1);
        list.addFirst(2);
        list.addFirst(3);


        assertEquals(3, list.removeFirst());
        assertTrue(list.size() == 2);

        assertEquals(2, list.removeFirst());
        assertTrue(list.size() == 1);

        assertEquals(1, list.removeFirst());
        assertTrue(list.size() == 0);
    }


    @Test
    public void should_remove_last_item() {
        DoubleLinkedList list = new DoubleLinkedList();
        list.addFirst(1);
        list.addFirst(2);
        list.addFirst(3);


        assertEquals(1, list.removeLast());
        assertTrue(list.size() == 2);

        assertEquals(2, list.removeLast());
        assertTrue(list.size() == 1);

        assertEquals(3, list.removeLast());
        assertTrue(list.size() == 0);
    }
}
