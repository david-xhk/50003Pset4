package esc.problemset4.week09.cohortexercise4;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class BufferFixed
{
	public static void main (String[] args)
	    throws Exception
	{
		int bufferSize = 1;
		int numberOfProducers = 100;
		int numberOfConsumers = 100;
		
		Buffer buffer = new Buffer(bufferSize);
		
		for (int i = 0; i < numberOfProducers; i++) {
		    MyProducer producer = new MyProducer(buffer);
		    producer.start();
		}
		
		for (int i = 0; i < numberOfConsumers; i++) {
            MyConsumer consumer = new MyConsumer(buffer);
            consumer.start();
        }
	}
}

class MyProducer extends Thread
{
	private Buffer buffer;
	
	public MyProducer(Buffer buffer)
	{
		this.buffer = buffer;
	}
	
	public void run()
	{
		Random random = new Random();
		
		try {
			Thread.sleep(random.nextInt(1000));
		}
		
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		buffer.addItem(new Object());
	}
}

class MyConsumer extends Thread
{
	private Buffer buffer;
	
	public MyConsumer(Buffer buffer)
	{
		this.buffer = buffer;
	}
	
	public void run()
	{
		Random random = new Random();
		
		try {
			Thread.sleep(random.nextInt(1000));
		}
		
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Object object = buffer.removeItem();
		
		assert object != null;
	}
}

class Buffer
{
	public final int SIZE;	
	private volatile Object[] objects;
	private volatile int count = 0;
	private Lock lock = new ReentrantLock();
	private Condition spaceAvailable = lock.newCondition();
	private Condition objectsAvailable = lock.newCondition();
	
	public Buffer(int size)
	{
		SIZE = size;
		objects = new Object[SIZE];
	}
	
	public void addItem(Object object)
	{
	    try {
	        lock.lock();
	        
	        while (count == SIZE)
                spaceAvailable.await();
	        
	        objects[count] = object;
	        System.out.println("Producer added an object to the buffer");
	        
	        count++;
	        objectsAvailable.signal();
	    }
        
        catch (InterruptedException exception) {
            System.out.println("Producer interrupted while waiting for space");
        }
	    
	    finally {
	        lock.unlock();
	    }
	}
	
	public synchronized Object removeItem()
	{
        try {
            lock.lock();
            
            while (count == 0)
                objectsAvailable.await();
            
            count--;
            System.out.println("Consumer got an object from the buffer");
            
            spaceAvailable.signal();
            return objects[count];
        }
        
        catch (InterruptedException exception) {
            System.out.println("Consumer interrupted while waiting for object");
            
            return null;
        }
        
        finally {
            lock.unlock();
        }	
	}
}