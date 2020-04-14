package esc.problemset4.week10.examples;

import java.util.Map;

class Test extends Thread
{
    Tracker tracker;
    
    public Test (Tracker tracker)
    {
        this.tracker = tracker;
    }
    
    public void run()
    {
        Tracker.MutablePoint location = tracker.getLocation("somestring");
        location.x = -1212000;
    }
}

//is this class thread-safe?
public class Tracker
{
    // @guarded by ???
    private final Map<String, MutablePoint> locations;
    
    // the reference locations, is it going to be an escape?
    public Tracker(Map<String, MutablePoint> locations)
    {
        this.locations = locations;
    }
    
    // is this an escape?
    public Map<String, MutablePoint> getLocations()
    {
        return locations;
    }
    
    // is this an escape?
    public MutablePoint getLocation(String id)
    {
        return locations.get(id);
    }
    
    public void setLocation(String id, int x, int y)
    {
        MutablePoint loc = locations.get(id);
        
        if (loc == null) {
            throw new IllegalArgumentException ("No such ID: " + id);
        }
        
        loc.x = x;
        loc.y = y;
    }
    
    //this class is not thread-safe (why?) and keep it unmodified.
    class MutablePoint
    {
        public int x, y;
        
        public MutablePoint (int x, int y)
        {
            this.x = x;
            this.y = y;
        }
        
        public MutablePoint (MutablePoint p)
        {
            this.x = p.x;
            this.y = p.y;
        }
    }
}
