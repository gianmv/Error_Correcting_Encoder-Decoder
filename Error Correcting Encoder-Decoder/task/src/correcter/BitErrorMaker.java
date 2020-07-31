package correcter;

import java.util.Arrays;
import java.util.Random;

public class BitErrorMaker extends ErrorMaker {

    Random rn;
    BitErrorMaker(byte[] phrase) {
        super(Arrays.copyOf(phrase, phrase.length));
        this.rn = new Random();
    }

    @Override
    public byte[] addError() {
        byte[] phraseBytes = new byte[this.phrase.length];
        for (int i = 0; i < this.phrase.length; i++) {
            byte MASK = (byte) (1 << rn.nextInt(8));
            phraseBytes[i] = (byte) (this.phrase[i] ^ MASK);
        }
        return phraseBytes;
    }
}