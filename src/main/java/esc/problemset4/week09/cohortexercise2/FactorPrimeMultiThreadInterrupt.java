package esc.problemset4.week09.cohortexercise2;

import java.math.BigInteger;


public class FactorPrimeMultiThreadInterrupt
{
    public static void main (String[] args)
    {
        BigInteger n = new BigInteger("4294967297");
        
        Integer numberOfThreads = 200;
        
        Thread[] threads = new Thread[numberOfThreads];
        FactorPrimeWorker[] workers = new FactorPrimeWorker[numberOfThreads];
        
        BigInteger one = BigInteger.ONE;
        BigInteger start = BigInteger.TWO;
        BigInteger step = BigInteger.valueOf(numberOfThreads);
        
        for (int i = 0; i < numberOfThreads; i++) {            
            FactorPrimeWorker worker = new FactorPrimeWorker(start, step, n);
            workers[i] = worker;
            
            Thread thread = new Thread(worker);
            threads[i] = thread;
            
            start = start.add(one);
        }
        
        System.out.println("Starting threads in: " + Thread.currentThread().getName());
        
        for (Thread thread : threads)
            thread.start();
        
        for (int i = 0; i < numberOfThreads; i++) {
            
            try {
                threads[i].join();
                
                //System.out.println("Thread " + i + " completed");
                
                if (workers[i].getAnswer() != null) {
                    //System.out.println("In: " + Thread.currentThread().getName());
                    
                    System.out.println("Factor found: " + workers[i].getAnswer());
                }
            }
            
            catch (InterruptedException exception) {
                System.out.println("Thread " + i+1 + " was interrupted");
            }
        }
    }
}

/*
 * FactorPrimeWorker will search for a prime factor of n from start incrementing by step
 */
class FactorPrimeWorker
    implements Runnable
{
    private BigInteger start;
    private BigInteger step;
    private BigInteger n;
    private BigInteger answer = null;
    static volatile boolean answerFound = false;
    
    public FactorPrimeWorker(BigInteger start, BigInteger step, BigInteger n)
    {
        //System.out.println("Instantiating FactorPrimeWorker with start: " + start.toString() +
        //    ", step: " + step.toString() + ", n: " + n);
        
        this.start = start;
        this.step = step;
        this.n = n;
    }
    
    @Override
    public void run()
    {
        BigInteger i = start;
        BigInteger zero = BigInteger.ZERO;
        
        while (i.compareTo(n) < 0) {
            
            if (answerFound) {
                //System.out.println("Stopping run in: " + Thread.currentThread().getName());
                
                break;
            }
            
            if (n.remainder(i).compareTo(zero) == 0) {
                
                //System.out.println("Found answer in: " + Thread.currentThread().getName());
                
                answer = i;
                answerFound = true;
                break;
            }
            
            i = i.add(step);
        }
    }
    
    public BigInteger getAnswer()
    {
        return answer;
    }
}