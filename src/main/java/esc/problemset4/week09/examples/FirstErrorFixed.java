package esc.problemset4.week09.examples;

import java.util.concurrent.atomic.AtomicInteger;

public class FirstErrorFixed {
	public static AtomicInteger count = new AtomicInteger(0);
	
	public static void main(String args[]){   	
		int numberofThreads = 10000;
		B[] threads = new B[numberofThreads];
	
		for (int i = 0; i < numberofThreads; i++) {
			threads[i] = new B();
			threads[i].start();
		}
		
		try {
			for (int i = 0; i < numberofThreads; i++) {
				threads[i].join();
			}
		} catch (InterruptedException e) {
			System.out.println("some thread is not finished");
		}
		
		System.out.print("The result is ... ");
		System.out.print("wait for it ... ");
		System.out.println(count);		
	}
}

class B extends Thread {
	public void run () {
        try {
            Thread.sleep(400);
            FirstErrorFixed.count.incrementAndGet();
        }
        
        catch (InterruptedException e) {
            System.out.println("interrupted");
        }
    }   
}

