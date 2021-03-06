package tl15.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * A bit input stream.
 */
public class BitInputStream extends InputStream {
    private final InputStream ins;

    /**
     * Position in the current byte. 8 indicates a new byte must be read from ins.
     */
    private int bytePos = 8;

    /**
     * The current byte. bytePos is a 0-based index into this.
     */
    private int curByte = 0;

    /**
     * 
     * @param ins The stream to convert into a bit stream.
     */
    public BitInputStream(InputStream ins) {
        this.ins = ins;
    }

    /**
     * Read bits from the stream.
     * @param bitsToRead How many bits to read. Can be between 0..32.
     * @return The bits read are the last n bits of the return value, in the order
     *         they were in the stream. The other bits of the return value are zero.
     *         Null is returned in case there were not enough bits in the stream.
     *         In this case the last bits of the stream are lost forever.
     * @throws java.io.IOException
     */
    public Integer readBits(int bitsToRead) throws IOException {
        int result = 0;
        while (bitsToRead > 0) {
            if (bytePos == 8) {
                curByte = ins.read();
                if (curByte == -1) {
                    curByte = 0;
                    return null;
                }
                bytePos = 0;
            }
            if ((curByte & (0x80 >> bytePos)) != 0) {
                result |= 1 << (bitsToRead - 1);
            }
            ++bytePos;
            --bitsToRead;
        }
        return result;
    }

    @Override
    public int read() throws IOException {
        Integer result = readBits(8);
        return (result == null)? -1 : result;
    }
}
