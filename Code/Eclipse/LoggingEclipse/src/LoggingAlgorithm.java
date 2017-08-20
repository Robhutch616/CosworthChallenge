import java.io.*;
import java.lang.Math;
import java.util.*;

class LoggingAlgorithm{

    // This is a list of channels that is known 
    // these all have channelId and frequency
    private ChannelSet channels;

    // This is a list of a list of channels. EAch element of this
    // will be a list of channels with the same frequency and acsending order
    private List<ChannelSet> frequencySet;

    public LoggingAlgorithm(){
        channels = new ChannelSet();
        frequencySet = new ArrayList<>();
        channels.add(new Channel(0, 1));
        channels.add(new Channel(1, 1));
        channels.add(new Channel(2, 20));
        channels.add(new Channel(3, 50));
        channels.add(new Channel(4, 20));
        channels.add(new Channel(5, 100));
        channels.add(new Channel(6, 200));
        channels.add(new Channel(7, 100));
        channels.add(new Channel(8, 20));
        channels.add(new Channel(9, 2));
        channels.add(new Channel(10, 2));
    }


    public void sortChannelsIntoFrequencySets(ChannelSet channelSet, List<ChannelSet> frequencySet){
        // iterate over channels
        for(int i=0;i<channels.size();i++){
            boolean addedChannelToSet = false;
            for(int j=0;j<frequencySet.size(); j++){
                if(frequencySet.get(j).get(0).)
            }
        }
    }

    public void runAlgorithm(){
        sortChannelsIntoFrequencySets(channels, frequencySet);
    }


}