package esc.problemset4.week10.examples;

public class NoVisibility extends Thread {
    boolean keepRunning = true;

    public static void main(String[] args) throws InterruptedException {
        NoVisibility t = new NoVisibility();
        t.start();
        Thread.sleep(1000);
        t.keepRunning = false;
        System.out.println(System.currentTimeMillis() + ": keepRunning is false");
    }
    public void run() {
        int x = 10;
        while (keepRunning)
        {
            //System.out.println("If you uncomment this line, the code will work without the visibility issue");
            x++;

        }
        System.out.println("x:"+x);
    }
}