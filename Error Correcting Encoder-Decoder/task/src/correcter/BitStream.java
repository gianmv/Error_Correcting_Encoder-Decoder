package correcter;

import java.util.Arrays;

class BitStream {

    byte[] source;
    int numBits;
    BitStream(byte[] source) {
        this.source = Arrays.copyOf(source,source.length);
        this.numBits = this.source.length * 8;
    }

    public void setSource(byte[] source) {
        this.source = Arrays.copyOf(source,source.length);
        this.numBits = this.source.length * 8;
    }

    /**
     * @param rightShift is the number of bits shifted
     *                   from left to right
     * @return pop the first bit of the stream shifted by
     * the parameter if there is not any bit lef return
     * Short.MIN_VALUE
     */
    public short nextBit(int rightShift) {
        if (this.numBits > 0) {
            byte ans = 0;
            byte MASK = -128;
            byte temp = this.source[0];
            ans = (byte) (MASK & temp);
            ans = (byte) (ans >>> rightShift);
            ans = (byte) (ans & (1<< 7 - rightShift));
            update();
            return ans;
        } else {
            return Short.MIN_VALUE;
        }
    }

    private void update() {
        byte carry = 0;
        int numBytes = numBits /8 + (numBits % 8 != 0 ? 1 : 0);
        for (int i = numBytes - 1; i >= 0; i--) {
            byte temp = (byte) (this.source[i] & -128);
            this.source[i] = (byte) (this.source[i] << 1);
            this.source[i] = (byte) (this.source[i] | carry);
            carry = (byte) ((temp >> 7) & 0x01);
        }
        this.numBits--;
    }
}