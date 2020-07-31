package correcter;

import java.util.Arrays;

public class BitErrorCorrecter extends ErrorCorrecter{

    byte[] decoded;
    BitStream bitStream;

    BitErrorCorrecter(byte[] received) {
        super(Arrays.copyOf(received,received.length));
        this.bitStream = new BitStream(this.received);
        int num = received.length*3/8;
        this.decoded = new byte[num];
    }

    /**
     * Check if the bits in X positions
     * are equals
     * XX000000
     * @return true if XX are equal else false
     */
    private boolean checkRedundancyBits(byte word) {
        byte a = (byte) (word & 0x80);
        byte b = (byte) (word & 0x40);
        b = (byte) (b << 1);

        byte ans = (byte) (a ^ b);

        return ans == 0;
    }

    @Override
    byte[] corrected() {
        checkRedundancy();
        eliminateRedundancy();
        return this.decoded;
    }

    private void eliminateRedundancy() {
        int temp = 0;
        for (int i = 0; i < this.decoded.length; i++) {
            int numBits = 7;
            byte ans = 0;
            while (numBits >= 0) {
                if (temp < 3) {
                    byte a = ReadTwoBits(7 - numBits);
                    ans = (byte) (ans | a);
                    numBits--;
                    temp++;
                } else {
                    temp = 0;
                    ReadTwoBits(0);
                }
            }
            this.decoded[i] = ans;
        }
    }

    /**
     * Read two bits of the
     * stream, because it is expected
     * that for redundancy every two bits,
     * we have a significant bit
     * @param rightOffset
     * @return
     */
    private byte ReadTwoBits(int rightOffset) {
        byte ans = (byte) bitStream.nextBit(rightOffset);
        bitStream.nextBit(0);
        return ans;
    }
    private void checkRedundancy() {
        for (int i = 0; i < this.received.length; i++) {
            byte temp = this.received[i];
            byte MASK = (byte) 0x03;
            byte redundancy = (byte) (temp & MASK);
            redundancy = (byte) (redundancy << 6);
            if (checkRedundancyBits(redundancy)) {
                byte index = getIndexBitError(temp);
                if (index != -1) {
                    temp = (byte) (temp & ~(0x03 << index));
                    byte sum = getSumBits(index, temp);
                    temp = (byte) (temp | sum);

                    this.received[i] = temp;
                }
            }
        }
        this.bitStream = new BitStream(this.received);
    }

    private byte getSumBits(byte index, byte temp) {
        byte ans = 0;
        byte MASK = 0x03;
        for (byte i = 0; i < 8; i += 2) {
            if (i == index) {
                continue;
            }
            MASK = (byte) (MASK << i);
            byte a = (byte) (temp & MASK);
            a = (byte) (a >> i);
            MASK = 0x03;
            a = (byte) (a & MASK);
            ans = (byte) (ans ^ a);
        }

        ans = (byte) (ans << index);
        return ans;
    }

    private byte getIndexBitError(byte temp) {
        byte MASK = 0x03;
        for (byte i = 2; i < 8; i += 2) {
            MASK = (byte) (MASK << 2);
            byte x = (byte) (temp & MASK);
            x = (byte) (x << (6 - i));
            if (!checkRedundancyBits(x)) {
                return i;
            }
        }
        return -1;
    }

}

