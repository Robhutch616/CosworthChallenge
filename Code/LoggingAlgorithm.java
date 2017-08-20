import java.io.*;
import java.lang.Math;
import java.nio.channels.Channels;
import java.util.*;

import sun.security.krb5.internal.crypto.dk.ArcFourCrypto;
class LoggingAlgorithm{

    // This is a list of channels that is known 
    // these all have channelId and frequency
    private ChannelSet channels;

    // This is a list of a list of channels. EAch element of this
    // will be a list of channels with the same frequency and acsending order
    private List<ChannelSet> frequencySet;

    // This will eventually store the pattern of data that will be present in each second
    private Pattern algPattern;
    // Dummy example object of a block of data
    private Block exampleBlock;
    // So that algorithm can work on multiple blocks
    private List<Block> blockList;
    // To store orderedData at end of algorithm
    private List<ChannelData> orderedData;


    private static final int SECOND = 1000;

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

        // This is dummy data - todo if time randomly generate
        exampleBlock = new Block(257, 3000, new int[3000]);
        blockList = new ArrayList<>();
        blockList.add(exampleBlock);
         // Create objects to store orderedData
         orderedData = createStorageForOrderedData(channels);
    }

    private List<ChannelData> createStorageForOrderedData(ChannelSet originalSet){
        List<ChannelData> data = new ArrayList<>();
        for(Channel ch : originalSet.getChannelList()){
            data.add(new ChannelData(ch.getChannelId(), ch.getFrequency())); 
            // ASSUMPTION : Under this current implementation the index of data will be the same as channelId
            // This will not be the case all the time, but for the sake of this challenge, due to time constraints
            // I will assume it is.
        }
        return data;
    }


    /**
     * After this function is ran frequncySet contains a list of lists of channels that are grouped by 
     * Frequency and are in ascending order of channelId.
     */
    public void sortChannelsIntoFrequencySets(ChannelSet channelSet, List<ChannelSet> frequencySet){
        // iterate over channels
        for(int i=0;i<channelSet.size();i++){
            boolean addedChannelToSet = false;
            Channel channelI = channels.get(i);
            for(int j=0;j<frequencySet.size(); j++){
                List<ChannelSet> channelOfFrequency = frequencySet.get(j);
                // For the list to exist it has at least one entry
                if(channelOfFrequency.get(0).getFrequency() == channelI.getFrequency() ){
                    channelOfFrequency.add(channelI);
                    addedChannelToSet = true;
                    break; // breaking for loop going over FrequencySet
                }
            }
            // check if channel was added to any ChannelSet organised in frequencySet
            if(!addedChannelToSet){
                // if no channel was added then its part of a new set
                frequencySet.add(new ChannelSet());
                frequencySet.get(frequencySet.size()-1).add(channelI);
            }
        }
    }

    /**
     * Once the channels are grouped in collections of frequency in order than one needs to know 
     * which ticks will fire these collections and for how many data points.
     */
    private void calculateTickValuesForCollections(List<ChannelSet> frequencySet){
        int frequencySetSize = frequencySet.size();
        for(int j=0;j<frequencySetSize;j++){
            ChannelSet set = frequencySet.get(j);
            // Calculate the tick values and frequency range of each set
            set.calculateOrderedTotal();
        }
    } 

    /**
     * Some simple sort algorithm to get in descending ordered list of freqencies
     */
    private List<ChannelSet> reOrderFrequencySetsInDescendingOrder(List<ChannelSet> sets){
        int setLength = sets.size();
        List<ChannelSet> orderedList = new ArrayList<>();
        for (int i=0; i <setLength; i++){
            int highestFrequencyFound = 0;
            int highestFrequencyIndex = -1;
            for(int j=0; j< sets.size();j++){
                ChannelSet channelSet = sets.get(j);
                if(channelSet.getFrequencyOfSet() > highestFrequencyFound){
                    highestFrequencyFound = channelSet.getFrequencyOfSet();
                    highestFrequencyIndex = j;
                }
            }
            if(highestFrequencyFound != 0 && highestFrequencyIndex >=0){
                // Found highest frequency value set
                orderedList.add(sets.get(highestFrequencyIndex));
                // remove from parent set so that you dont iterate over needless values
                sets.remove(highestFrequencyIndex);
            }
        }
        // now orderedList contains the frequencySet in ordered fashion
        return orderedList;
    }

    /** 
     * This function calculates the pattern of data that will be observed every second. This data is stored in a list
     * where each element contains the tick number and the channelId that is fired.
     */
    private Pattern calculateRecurrsiveTickPatternInData(List<ChannelSet> sets){
        Pattern pattern = new Pattern();
        // loop over all ticks in a second
        for(int t=0; t<SECOND ; t++){
            // First look in each frequencyset and check if t appears in the tick array
            for(ChannelSet set : sets){
                if(!set.doesTickAppearInThisSet(t)){
                    // if current tick does not appear in the channelSet then move to next channelSet
                    continue;
                }
                // channelSet 'set' will read out on this tick value
                // Therefore fill pattern with tick and channel values in ascending order over channelSet
                for(Channel ch : set.getChannelList()){
                    // The Channel list will be in ascending order so simple read off the channelIds
                    pattern.add(new TickPattern(t, ch.getChannelId()));
                }
            }
        }
        // Pattern contains recurrsive list over a second
        return pattern;
    }

    public void runAlgorithm(){
        // Take the given set of channels that contain channelId and frequency and group them accordingly
        sortChannelsIntoFrequencySets(channels, frequencySet);
        // Calculate which ticks will fire the frequencySets
        calculateTickValuesForCollections(frequencySet);
        // Order the frequencySets in descending order
        frequencySet = reOrderFrequencySetsInDescendingOrder(frequencySet);
        // Now that the sets are ordered in descending order and we know which ticker values will fire each frequency set
        // One can now calcualte a recurring pattern of channelIds that will be fired each second.
        algPattern = calculateRecurrsiveTickPatternInData(frequencySet);
        // ASSUMPTION = I will assume that the total number of channels in the complete system have 
        //          channelIds that are always consequtive and in ascending order from 0.
        //          If this wasn't the case I would be tempted to make a HashMap<Integer, ChannelData>
        // Where Integer would be the channelData channelId
        for(Block block : blockList){
            sortDataFromSingleBlock(block, orderedData, algPattern);
        }
        // DONE!
    }

    /**
     * This sorts out data for one block and adds the data to the orderedData list
     */
    private void sortDataFromSingleBlock(Block block, List<ChannelData> data, Pattern pattern){
        // Calculate offset of block in a second
        final long blockStartTime = block.getStart();
        // Find the index of the first tick that will be present in the data block
        pattern.setStartIndex(blockStartTime);
        // Now interate over all data in the block
        for(int i=0 ; i<block.getLength();i++){
            // Get information needed for current index
            ChannelTime channelTime = pattern.getInfoForCurrentIndex();
            // Add the data to the ordered sets - NOTE - THIS IS WHERE I USE THE ASSUMPTION ABOUT CONTINUOUS DATA
            ChannelData channelData = orderedData.get(channelTime.getChannelId());
            channelData.addDataToChannel(new DataPoint(block.getDataAtIndex(i), channelTime.getTime()));
            //Increment index of pattern array
            pattern.incrementIndex();
        }
    }


}