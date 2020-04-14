package esc.problemset4.week10.cohortexercise1;


public class ExperimentFixed1
{
    private static volatile int MY_INT = 0;
    private static final int MAX_INT = 10;
    
    public static void main(String[] args)
        throws InterruptedException
    {
        new ChangeListener().start();
        
        System.out.println("Waiting two seconds so the JIT will probably optimize ChangeListener");
        Thread.sleep(2000);
        
        new ChangeMaker().start();
    }
    
    static class ChangeListener extends Thread
    {
        public void run()
        {
            int local_value = MY_INT;
            
            while (local_value < MAX_INT) {
                
                if (local_value != MY_INT) {
                    System.out.println("Got Change for MY_INT : " + MY_INT);
                    local_value = MY_INT;
                }
            }
        }
    }
    
    static class ChangeMaker extends Thread
    {
        public void run()
        {
            int local_value = MY_INT;
            
            while (MY_INT < MAX_INT) {
                System.out.println("Incrementing MY_INT to " + (local_value + 1));
                MY_INT = ++local_value;
                
                try {
                    Thread.sleep(2000);
                }
                
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
