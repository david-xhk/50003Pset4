package esc.problemset4.week10.cohortexercise2;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class DiningPhilFixed2
{
    private static int N = 5;
    
    public static void main (String[] args)
        throws Exception
    {
        Philosopher2[] phils = new Philosopher2[N];
        Fork2[] forks = new Fork2[N];
        
        for (int i = 0; i < N; i++) {
            forks[i] = new Fork2(i);
        }
        
        for (int i = 0; i < N; i++) {
            phils[i] = new Philosopher2(i, forks[i], forks[(i+N-1)%N]);
            phils[i].start();
        }
    }
}

/*
 * This solution is based on try-locking. When a philosopher picks up a fork, he
 * will not succeed after a certain timeout has elapsed.
 * 
 * All philosophers will pick up the left fork first, followed by the right fork. 
 * However, the philosophers are polite. If he is unable to pick his right fork up 
 * after picking up his left fork, he will put his left fork back down to allow 
 * another philosopher to use it.
 * 
 * The reason why this solution will be deadlock-free is because in a situation
 * where all philosophers are holding onto their left fork and are waiting for
 * their right fork, the first philosopher to put down his left fork will allow
 * another philosophers to pick up their right fork and eat.
 * 
 * In order to prevent live-lock (where all philosophers pick up and put down their
 * left fork endlessly), the timeout used for picking up any fork is randomized.
 * 
 * Furthermore, to prevent starvation (where one philosopher keeps holding onto his 
 * forks which prevents other philosophers from eating), after a philosopher eats, 
 * he will wait for the maximum timeout after putting down his forks. This is to
 * ensure that other philosophers will have the chance to pick up their own forks.
 */
class Philosopher2 extends Thread
{
    private final int index;
    private final Fork2 left;
    private final Fork2 right;
    private final Random randomGenerator = ThreadLocalRandom.current();
    public static int maxTimeout = 1000;
    
    public Philosopher2 (int index, Fork2 left, Fork2 right)
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
    
    private boolean pickUpLeftFork()
        throws InterruptedException
    {
        int timeoutMillis = randomGenerator.nextInt(maxTimeout);
        
        boolean pickedUp = left.pickup(timeoutMillis); 
        
        if (pickedUp)
            System.out.println("Phil " + index + " picks up left fork.");
        
        return pickedUp;
    }
    
    private boolean pickUpRightFork()
        throws InterruptedException
    {
        int timeoutMillis = randomGenerator.nextInt(maxTimeout);
        
        boolean pickedUp = right.pickup(timeoutMillis); 
        
        if (pickedUp)
            System.out.println("Phil " + index + " picks up right fork.");
        
        return pickedUp;
    }
    
    private void pickUpForks()
        throws InterruptedException
    {
        boolean leftForkPickedUp = false;
        
        boolean rightForkPickedUp = false;
        
        while (true) {
            
            while (!leftForkPickedUp)
                leftForkPickedUp = pickUpLeftFork();
            
            rightForkPickedUp = pickUpRightFork();
            
            if (rightForkPickedUp)
                return;
            
            putDownLeftFork();
            
            leftForkPickedUp = false;
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
        
        Thread.sleep(randomGenerator.nextInt(maxTimeout)); // to let others pick up their forks
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

class Fork2
{
    private final int index;
    private boolean isAvailable = true;
    private final Lock lock = new ReentrantLock();
    
    public Fork2 (int index)
    {
        this.index = index;
    }
    
    public boolean pickup(long timeoutMillis)
        throws InterruptedException
    {
        boolean locked = lock.tryLock(timeoutMillis, TimeUnit.MILLISECONDS);
        
        if (locked)
            isAvailable = false;
        
        return locked;
    }
    
    public void putdown()
        throws InterruptedException
    {
        lock.unlock();
        
        isAvailable = true;
    }
    
    public String toString()
    {
        if (isAvailable)
            return "Fork " + index + " is available.";
        
        else
            return "Fork " + index + " is NOT available.";
    }
}
