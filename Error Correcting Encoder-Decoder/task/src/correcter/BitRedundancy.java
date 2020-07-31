package correcter;

import java.util.Arrays;

public class BitRedundancy extends Redundancy {

    byte[] triadBit;
    BitStream bitStream;

    BitRedundancy(byte[] phrase) {
        super(Arrays.copyOf(phrase, phrase.length));
        this.bitStream = new BitStream(this.phrase);
    }

    private void onlyThreeBits() {
        int numBytes = this.phrase.length*8/3 + (this.phrase.length*8%3 == 0 ? 0 : 1);
        this.triadBit = new byte[numBytes];
        short ans = 0;
        for (int i = 0; i < this.triadBit.length; i++) {
            byte word = 0;
            for (int j = 0; j < 3; j++) {
                ans = this.bitStream.nextBit(j);
                word = (byte) (word | ans);
            }
            this.triadBit[i] = word;
        }
    }

    private void triplicateBits() {
        for (int i = 0; i < this.triadBit.length; i++) {
            this.triadBit[i] = triplicateBits(this.triadBit[i]);
        }
    }

    private byte triplicateBits(byte word) {
        byte ans = 0;
        byte MASK = (byte) 0x80;
        byte temp = (byte) (MASK & word);
        ans = (byte) (temp | ans);
        temp = (byte) ((temp >> 1) & 1 << 6);
        ans = (byte) (ans | temp);

        MASK = 0x40;
        temp = (byte) (word & MASK);
        temp = (byte) ((temp >> 1) & (1 << 5));
        ans = (byte) (ans | temp);
        temp = (byte) ((temp >> 1) & (1 << 4));
        ans = (byte) (ans | temp);

        MASK = 0x20;
        temp = (byte) (word & MASK);
        temp = (byte) ((temp >> 2) & (1 << 3));
        ans = (byte) (ans | temp);
        temp = (byte) ((temp >> 1) & (1 << 2));
        ans = (byte) (ans | temp);

        return ans;
    }

    @Override
    public byte[] addRedundancy() {
        onlyThreeBits();
        triplicateBits();
        addParity();
        return this.triadBit;
    }

    private void addParity() {
        for (int i = 0; i < this.triadBit.length; i++) {
            this.triadBit[i] = addParity(this.triadBit[i]);
        }
    }

    private byte addParity(byte word) {
        byte ans = 0;
        byte MASK = (byte) 0xC0;
        byte a = (byte) (word & MASK);
        MASK = 0x30;
        byte b = (byte) (word & MASK);
        MASK = 0x0C;
        byte c = (byte) (word & MASK);

        a = (byte) ((a >> 6) & (0x03));
        b = (byte) ((b >> 4) & (0x03));
        c = (byte) ((c >> 2) & (0x03));

        ans = (byte) (a ^ b ^ c);

        return (byte) (ans | word);
    }
}


