/**
 * Another simple class to help pass data from Pattern class to LoggingAlgorithm
 */

 class ChannelTime{
     private int channelId;
     private long time;

     public ChannelTime(int channelId, long time){
         this.channelId = channelId;
         this.time = time;
     }

     public int getChannelId(){
         return channelId;
     }

     public long getTime(){
         return time;
     }
 }