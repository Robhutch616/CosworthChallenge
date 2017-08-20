/** 
 * This is an example class of a block of data
 */

class Block{
    private long blockStartTime;
    private int blockLength;
    private int[] blockData;
    public Block(long startTime, int length, int[] data){
        this.blockStartTime = startTime;
        this.blockLength = length;
        this.blockData = data;
    }
    public long getStart(){
        return blockStartTime;
    }
    public int getLength(){
        return blockLength;
    }
    public int[] getData(){
        return blockData;
    }

    public int getDataAtIndex(int index){
        return blockData[index];
    }
}