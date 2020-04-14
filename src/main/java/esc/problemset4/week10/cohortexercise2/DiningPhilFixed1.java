package esc.problemset4.week10.cohortexercise2;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class DiningPhilFixed1
{
    private static int N = 5;
    
    public static void main (String[] args)
        throws Exception
    {
        Philosopher[] phils = new Philosopher[N];
        Fork[] forks = new Fork[N];
        
        for (int i = 0; i < N; i++) {
            forks[i] = new Fork(i);
        }
        
        for (int i = 0; i < N; i++) {
            phils[i] = new Philosopher(i, forks[i], forks[(i+N-1)%N]);
            phils[i].start();
        }
    }
}

/*
 * This solution is based on fixed ordering. All philosophers will always pick up the
 * fork with the smaller index first. 
 * 
 * The reason why this solution will be deadlock-free is because there will not be a
 * situation where one philosopher holding on to fork A tries to pick up fork B, while
 * another philosopher holding on to fork B tries to pick up fork A. 
 * 
 * If fork A has the lower index, both philosophers will try to pick up fork A first,
 * and the first one who picks up fork A will then be able to pick up fork B.
 * 
 * If fork A and fork B happen to have the same index, then a tie lock will be used to
 * synchronize two philosophers trying to pick them up.
 */
class Philosopher extends Thread
{
    private final int index;
    private final Fork left;
    private final Fork right;
    private final Random randomGenerator = ThreadLocalRandom.current();
    private final Object tieLock = new Object();
    
    public Philosopher (int index, Fork left, Fork right)
    {
        this.index = index;
        this.left = left;
        this.right = right;
    }
    
    private void startThinking()
        throws InterruptedException
    {
        Thread.sleep(randomGenerator.nextInt(100)); // not sleeping but thinking
        
        System.out.println("Phil " + index + " finishes thinking.");
    }
    
    private void startEating()
        throws InterruptedException
    {
        Thread.sleep(randomGenerator.nextInt(1000)); // eating
        
        System.out.println("Phil " + index + " finishes eating.");
    }
    
    private void pickUpLeftFork()
        throws InterruptedException
    {
        left.pickup();
        
        System.out.println("Phil " + index + " picks up left fork.");
    }
    
    private void pickUpRightFork()
        throws InterruptedException
    {
        right.pickup();
        
        System.out.println("Phil " + index + " picks up right fork.");
    }
    
    private void pickUpForks()
        throws InterruptedException
    {
        if (left.getIndex() < right.getIndex()) {
            pickUpLeftFork();
            
            pickUpRightFork();
        }
        
        else if (right.getIndex() < left.getIndex()) {
            pickUpRightFork();
            
            pickUpLeftFork();
        }
        
        else {
            synchronized (tieLock) {
                pickUpLeftFork();
                
                pickUpRightFork();
            }
        }
    }
    
    private void putDownLeftFork()
        throws InterruptedException
    {
        left.putdown();
        
        System.out.println("Phil " + index + " puts down left fork.");
    }
    
    private void putDownRightFork()
        throws InterruptedException
    {
        right.putdown();
        
        System.out.println("Phil " + index + " puts down right fork.");
    }
    
    private void putDownForks()
        throws InterruptedException
    {
        putDownLeftFork();
        
        putDownRightFork();
    }
    
    public void run()
    {
        try {
            while (true) {
                startThinking();
                
                pickUpForks();
                
                startEating();
                
                putDownForks();
            }
        }
        
        catch (InterruptedException exception) {
            System.out.println("Don't disturb me while I am sleeping, well, thinking.");
        } 
    }
}

class Fork
{
    private final int index;
    private boolean isAvailable = true;
    
    public Fork (int index)
    {
        this.index = index;
    }
    
    public int getIndex()
    {
        return index;
    }
    
    public synchronized void pickup()
        throws InterruptedException
    {
        while (!isAvailable)
            wait();
        
        isAvailable = false;
        notifyAll();
    }
    
    public synchronized void putdown()
        throws InterruptedException
    {
        while (isAvailable)
            wait();
        
        isAvailable = true;
        notifyAll();
    }
    
    public String toString()
    {
        if (isAvailable)
            return "Fork " + index + " is available.";
        
        else
            return "Fork " + index + " is NOT available.";
    }
}
