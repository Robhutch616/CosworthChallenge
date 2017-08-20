import com.sun.org.apache.regexp.internal.recompile;

/**
 * This object stores the tick number in the second and a channelid that is fired on that tick
 */

 class TickPattern{
     private int tick; // in range from 0->999
     private int channelId; // this is fired
     public TickPattern(int tick, int channelId){
         this.tick = tick;
         this.channelId = channelId;
     }

     public int getTick(){
         return tick;
     }

     public int getChannelId(){
         return channelId;
     }
 }