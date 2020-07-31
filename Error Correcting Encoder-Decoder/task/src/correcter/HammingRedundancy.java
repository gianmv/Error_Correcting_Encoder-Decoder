package correcter;

import java.util.Arrays;

public class HammingRedundancy extends Redundancy{
    BitStream bitStream;
    byte[] hamRedun;
    HammingRedundancy(byte[] phrase) {
        super(Arrays.copyOf(phrase,phrase.length));
        bitStream = new BitStream(this.phrase);
        hamRedun = new byte[phrase.length * 2];
    }

    @Override
    public byte[] addRedundancy() {
        formatForRedundancy();
        calculateRedundancy();
        return this.hamRedun;
    }

    /** 12345678
     *  ..X.XXX.
     *  The dot show a bit of
     *  parity
     */
    private void formatForRedundancy() {

        for (int i = 0; i < this.hamRedun.length;i++) {
            byte temp = 0;
            temp = (byte) (temp | bitStream.nextBit(2));
            temp = (byte) (temp | bitStream.nextBit(4));
            temp = (byte) (temp | bitStream.nextBit(5));
            temp = (byte) (temp | bitStream.nextBit(6));
            hamRedun[i] = temp;
        }
    }

    private void calculateRedundancy() {
        for (int i = 0; i < this.hamRedun.length;i++) {
            this.hamRedun[i] = calculateRedundancy(this.hamRedun[i]);
        }
    }

    private byte calculateRedundancy(byte b) {
        byte ONE = (byte)   0b1010_1010;
        byte TWO = (byte)   0b0110_0110;
        byte FOUR = (byte)  0b0001_1110;
        byte EIGHT = (byte) 0b0000_0001;

        byte temp = (byte) (b & ONE);
        int numBits = numberOfBitsSet(temp);

        byte a1 = (byte) (numBits % 2 == 0 ? 0 : 0b1000_0000);

        temp = (byte) (b & TWO);
        numBits = numberOfBitsSet(temp);

        byte a2 = (byte) (numBits % 2 == 0 ? 0 : 0b0100_0000);

        temp = (byte) (b & FOUR);
        numBits = numberOfBitsSet(temp);

        byte a4 = (byte) (numBits % 2 == 0 ? 0 : 0b0001_0000);

        temp = (byte) (b & EIGHT);
        numBits = numberOfBitsSet(temp);

        byte a8 = (byte) (numBits % 2 == 0 ? 0 : 0b0000_0001);

        b = (byte) (b | a1 | a2 | a4 | a8);
        return b;
    }

    private int numberOfBitsSet(byte b) {
        byte[] BITS = {0x01,0x02,0x04,0x08,0x10,0x20,0x40, (byte) 0x80};
        int cont = 0;
        for (int i = 0; i < BITS.length; i++) {
            if ((b & BITS[i]) != 0) {
                cont++;
            }
        }
        return cont;
    }
}
