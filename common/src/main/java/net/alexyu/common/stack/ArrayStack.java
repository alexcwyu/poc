package net.alexyu.common.stack;

/**
 * Created by alexc on 5/4/2017.
 */
public class ArrayStack<T> {
    private T[] items = null;
    private int capacity;
    private int idx = -1;

    public ArrayStack(int capacity){
        this.capacity = capacity;
        this.items = (T[]) new Object[capacity];
    }

    public T pop(){
        if (this.idx == -1)
            return null;

        T item = items[idx];
        items[idx]= null;
        idx--;
        return item;
    }

    public boolean push(T item){
        if (isFull())
            return false;

        this.items[++idx] = item;
        return true;
    }

    public int size(){
        return idx + 1;
    }

    public boolean isFull(){
        return ((idx + 1) == capacity);
    }



    public static void main(String[] args) {
        ArrayStack<String> stack = new ArrayStack<String>(11);
        stack.push("hello");
        stack.push("world");
        System.out.println(stack.size());

        System.out.println(stack.pop());
        System.out.println(stack.size());
        System.out.println(stack);

        System.out.println(stack.pop());
        System.out.println(stack.size());
        System.out.println(stack);

        System.out.println(stack.pop());
        System.out.println(stack.size());
        System.out.println(stack);
    }
}
