/**
 * Simple class that stores the value and time of a datapoint
 */

 class DataPoint{
     private int value;
     private long time;
     public DataPoint(int value, long time){
         this.value = value;
         this.time = time;
     }

     public int getValue(){
         return value;
     }
     public long getTime(){
         return time;
     } 
 }