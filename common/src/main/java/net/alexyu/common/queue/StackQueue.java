package net.alexyu.common.queue;

import java.util.Stack;

/**
 * Created by alexc on 5/4/2017.
 *
 * LeetCode – Implement Queue using Stacks (Java)
 *
 * Implement the following operations of a queue using stacks.

 * push(x) -- Push element x to the back of queue.
 * pop() -- Removes the element from in front of queue.
 * peek() -- Get the front element.
 * empty() -- Return whether the queue is empty.
 *
 * http://www.programcreek.com/2014/07/leetcode-implement-queue-using-stacks-java/
 *
 */
public class StackQueue<T> {
    Stack<T> temp = new Stack<T>();
    Stack<T> value = new Stack<T>();

    // Push element x to the back of queue.
    public void push(T x) {
        if(value.isEmpty()){
            value.push(x);
        }else{
            while(!value.isEmpty()){
                temp.push(value.pop()); //pop from top of value stack and insert to top of temp stack (reverse the order)
            }

            value.push(x);  // insert the new item to value stack

            while(!temp.isEmpty()){
                value.push(temp.pop()); //pop from top of temp stack and insert to top of value stack (reverse the reversed order)
            }
        }
    }

    // Removes the element from in front of queue.
    public T pop() {
        return value.pop();
    }

    // Get the front element.
    public T peek() {
        return value.peek();
    }

    // Return whether the queue is empty.
    public boolean empty() {
        return value.isEmpty();
    }

    public int size(){
        return value.size();
    }

    public static void main(String[] args) {
        StackQueue<String> stack = new StackQueue<String>();
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
