
public class Channel {
	private int channelId;
    private int frequency;

    public Channel(int channelId, int frequency){
        this.channelId = channelId;
        this.frequency = frequency;
    }
    public int getChannelId(){
        return channelId;
    }
    public int getFrequency(){
        return frequency;
    }
}
