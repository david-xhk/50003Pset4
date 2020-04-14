package esc.problemset4.week10.cohortexercise1;


public class ExperimentFixed2
{
    private static int MY_INT = 0;
    private static final int MAX_INT = 10;
    
    public static void main(String[] args)
        throws InterruptedException
    {
        new ChangeListener().start();
        
        System.out.println("Waiting two seconds so the JIT will probably optimize ChangeListener");
        Thread.sleep(2000);
        
        new ChangeMaker().start();
    }
    
    private static synchronized int getMyInt()
    {
        return MY_INT;
    }
    
    private static synchronized void setMyInt(int newInt)
    {
        MY_INT = newInt;
    }
    
    static class ChangeListener extends Thread
    {
        public void run()
        {
            int local_value = getMyInt();
            
            while (local_value < MAX_INT) {
                
                int current_value = getMyInt();
                
                if (local_value != current_value) {
                    System.out.println("Got Change for MY_INT : " + current_value);
                    local_value = current_value;
                }
            }
        }
    }
    
    static class ChangeMaker extends Thread
    {
        public void run()
        {
            int local_value = getMyInt();
            
            while (getMyInt() < MAX_INT) {
                System.out.println("Incrementing MY_INT to " + (local_value + 1));
                
                local_value++;
                
                setMyInt(local_value);
                
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
