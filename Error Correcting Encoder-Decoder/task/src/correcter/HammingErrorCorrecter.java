package correcter;
import java.util.Arrays;

public class HammingErrorCorrecter extends ErrorCorrecter{

    BitStream bitStream;
    byte[] decoded;
    HammingErrorCorrecter(byte[] received) {
        super(Arrays.copyOf(received, received.length));
        bitStream = new BitStream(this.received);
        decoded = new byte[received.length / 2];
    }

    @Override
    byte[] corrected() {
        checkRedundancy();
        eliminateRedundancy();
        return this.decoded;
    }

    private void checkRedundancy() {
        for (int i = 0; i < this.received.length; i++) {
            this.received[i] = checkRedundancy(this.received[i]);
        }
        bitStream.setSource(this.received);
    }

    private byte checkRedundancy(byte b) {
        byte ONE = (byte)   0b1010_1010;
        byte TWO = (byte)   0b0110_0110;
        byte FOUR = (byte)  0b0001_1110;
        byte EIGHT = (byte) 0b0000_0001;

        byte temp = (byte) (b & ONE);
        int numBits = numberOfBitsSet(temp);

        byte a1 = (byte) (numBits % 2 == 0 ? 0 : 1);

        temp = (byte) (b & TWO);
        numBits = numberOfBitsSet(temp);

        byte a2 = (byte) (numBits % 2 == 0 ? 0 : 1);

        temp = (byte) (b & FOUR);
        numBits = numberOfBitsSet(temp);

        byte a4 = (byte) (numBits % 2 == 0 ? 0 : 1);

        temp = (byte) (b & EIGHT);
        numBits = numberOfBitsSet(temp);

        byte a8 = (byte) (numBits % 2 == 0 ? 0 : 1);

        byte correctBit = (byte) (a1 + a2*2 + a4*2*2 + a8*2*2*2);

        b = correctBit == 0 ? b : (byte) (b ^ (0b1000_0000 >>> (correctBit - 1)));

        return b;
    }

    private void eliminateRedundancy() {
        for (int i = 0; i < this.decoded.length; i++) {
            byte ans = 0;
            bitStream.nextBit(0);
            bitStream.nextBit(0);

            ans = (byte) (ans | bitStream.nextBit(0));

            bitStream.nextBit(0);

            ans = (byte) (ans | bitStream.nextBit(1));
            ans = (byte) (ans | bitStream.nextBit(2));
            ans = (byte) (ans | bitStream.nextBit(3));

            bitStream.nextBit(0);

            bitStream.nextBit(0);
            bitStream.nextBit(0);

            ans = (byte) (ans | bitStream.nextBit(4));

            bitStream.nextBit(0);

            ans = (byte) (ans | bitStream.nextBit(5));
            ans = (byte) (ans | bitStream.nextBit(6));
            ans = (byte) (ans | bitStream.nextBit(7));

            bitStream.nextBit(0);

            this.decoded[i] = ans;
        }
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
