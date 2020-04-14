package esc.problemset4.week10.cohortexercise4;

import java.util.Vector;


public class FirstExampleFixed
{
    public static Object getLast(Vector list)
    {
        // synchronize on list while getting list size and getting item
        synchronized (list) {
            int lastIndex = list.size()-1;
            
            return list.get(lastIndex);
        }
    }
    
    public static void deleteLast(Vector list)
    {
        // synchronize on list while getting list size and removing item
        synchronized (list) {
            int lastIndex = list.size()-1;
            
            list.remove(lastIndex);
        }
    }
    
    public static boolean contains(Vector list, Object obj)
    {
        // synchronize on list while making a copy of list
        Vector listCopy;
        
        synchronized (list) {
            listCopy = new Vector(list);
        }
        
        // check membership on copy so list will not be locked
        for (int i = 0; i < listCopy.size(); i++) {
            
            if (listCopy.get(i).equals(obj)) // assume obj is immutable
                return true;
        }
        
        return false;
    }
}
