import java.util.ArrayList;

import com.sun.corba.se.impl.ior.FreezableList;

/**
 * this class contains the data output for a specific channelId
 */
class ChannelData{
    private int channelId;
    private int frequency;
    private List<DataPoint> data;

    // IT SHOULD BE NOTED - That by the point this object needs to be created, one could calulate how many bits of
    // data should be expected over the blocks provided to the program. If given more time I would add this in.

    public ChannelData(int channelId, int frequency){
        this.channelId = channelId;
        this.frequency = frequency;
        this.data = new ArrayList<>();
    }

    public void addDataToChannel(DataPoint dataPoint){
        data.add(dataPoint);
    }

    public int getChannelId(){
        return channelId;
    }

    public int getFrequency(){
        return frequency;
    }
}