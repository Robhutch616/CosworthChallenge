
import java.util.ArrayList;

public class ChannelSet {
	/**
     * This is a list of Channels that have the same Frequency
     */

     private List<Channels> channels;

     public ChannelSet(){
         channels = new ArrayList<>();
     }

     public void add(Channel channel){
         channels.add(channel);
     }
}
