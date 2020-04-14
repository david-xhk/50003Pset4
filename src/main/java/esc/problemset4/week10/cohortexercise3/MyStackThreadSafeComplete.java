package esc.problemset4.week10.cohortexercise3;


/*
 * MyStackThreadSafeComplete implements the stack operations push, pop, and peek,
 * as well as methods isEmpty and isFull. It can only hold longs.
 * 
 * The longs are stored in `stackArray`, and `top` is an index pointing to the
 * item that was most recently pushed to the stack. When accessing `stackArray`
 * and `top`, the intrinsic lock of the current instance must first be acquired
 * by executing `synchronized (this)`.
 */
public class MyStackThreadSafeComplete
{
    private final int maxSize;
    private long[] stackArray; // guarded by "this"
    private int top; // invariant: top < stackArray.length && top >= -1; guarded by "this"
    
    // pre-condition: s > 0
    // post-condition: maxSize == s && top == -1 && stackArray != null
    // "synchronized" not needed because instantiation only involves one thread
    public MyStackThreadSafeComplete(int s)
    {
        maxSize = s;
        stackArray = new long[maxSize];
        top = -1;
    }
    
    // pre-condition: top < maxSize - 1 (stack is not full)
    // post-condition: element j is added into stackArray[top] and top is incremented
    public synchronized void push(long j)
    {
        while (isFull()) {
            try {
                wait();
            }
            
            catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
        
        stackArray[++top] = j;
        notifyAll();
    }
    
    // pre-condition: top >= 0 (stack is not empty)
    // post-condition: the top element is removed and returned
    public synchronized long pop()
    {
        long toReturn; 
        
        while (isEmpty()) {
            try {
                wait();
            }
            
            catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
        
        toReturn = stackArray[top--];
        notifyAll();
        
        return toReturn;
    }
    
    // pre-condition: top >= 0 (stack is not empty)
    // post-condition: the elements are un-changed. the top element is returned.
    public synchronized long peek()
    {
        while (isEmpty()) {
            try {
                wait();
            }
            
            catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
        
        return stackArray[top];
    }
    
    // pre-condition: true (no pre-condition)
    // post-condition: the elements are un-changed. the return value is true iff the stack is empty.
    public synchronized boolean isEmpty()
    {
        return (top == -1);
    }
    
    // pre-condition: true (no pre-condition)
    // post-condition: the elements are un-changed. the return value is true iff the stack is full.
    public synchronized boolean isFull()
    {
        return (top == maxSize - 1);
    }
}