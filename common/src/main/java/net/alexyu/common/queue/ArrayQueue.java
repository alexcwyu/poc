package net.alexyu.common.queue;

/**
 * Created by alexc on 5/4/2017.
 *
 * Implement a Queue using an Array in Java
 */
public class ArrayQueue<T> {

    private T[] items;
    private int capacity;
    private int head = -1;
    private int tail = -1;
    private int size;

    public ArrayQueue(int capacity){
        this.capacity = capacity;
        this.items = (T[]) new Object[capacity];
    }

    public int size(){
        return size;
    }

    public boolean push(T item){
        if (isFull())
            return false;

        head = (head + 1) % capacity;
        items[head] = item;
        size++;

        if (tail == -1)
            tail = head;
        return true;
    }

    public T pop(){
        if (size == 0)
            return null;

        T item = items[tail];
        items[tail] = null;
        size --;

        tail = (tail +1) % capacity;

        if (size == 0){
            head = -1;
            tail = -1;
        }

        return item;


    }

    public T peek(){
        if (size == 0)
            return null;
        return items[tail];
    }

    public boolean isFull(){
        return size == capacity;
    }




    public static void main(String[] args) {
        ArrayQueue<String> stack = new ArrayQueue<String>(10);
        stack.push("1");
        stack.push("2");
        stack.push("3");
        stack.push("4");
        System.out.println(stack.size());

        System.out.println("######");
        System.out.println(stack.pop());
        System.out.println(stack.size());

        System.out.println("######");
        System.out.println(stack.pop());
        System.out.println(stack.size());

        System.out.println("######");
        System.out.println(stack.pop());
        System.out.println(stack.size());

        System.out.println("######");
        System.out.println(stack.pop());
        System.out.println(stack.size());
    }
}
