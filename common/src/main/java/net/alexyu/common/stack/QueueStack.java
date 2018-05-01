package net.alexyu.common.stack;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by alex on 4/7/17.
 *
 * Implement Stack using Queues (Java)
 */
public class QueueStack<T> {

    Queue<T> queue1 = new LinkedList<T>();
    Queue<T> queue2 = new LinkedList<T>();

    // Push element x onto stack.
    public void push(T x) {
        if(empty()){
            queue1.offer(x);
        }else{
            if(queue1.size()>0){
                queue2.offer(x);
                int size = queue1.size();
                while(size>0){
                    queue2.offer(queue1.poll()); // get head of queue1 and insert into the end of queue2
                    size--;
                }
            }else if(queue2.size()>0){
                queue1.offer(x);
                int size = queue2.size();
                while(size>0){
                    queue1.offer(queue2.poll()); // get head of queue2 and insert into the end of queue1
                    size--;
                }
            }
        }
    }

    // Removes the element on top of the stack.
    public T pop() {
        if(queue1.size()>0){
            return queue1.poll();
        }else if(queue2.size()>0){
            return queue2.poll();
        }
        return null;
    }

    // Get the top element.
    public T top() {
        if(queue1.size()>0){
            return queue1.peek();
        }else if(queue2.size()>0){
            return queue2.peek();
        }
        return null;
    }

    // Return whether the stack is empty.
    public boolean empty() {
        return queue1.isEmpty() & queue2.isEmpty();
    }
    public int size() {
        return queue1.size() + queue2.size();
    }


    public static void main(String[] args) {
        QueueStack<String> stack = new QueueStack<String>();
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
    }

}
