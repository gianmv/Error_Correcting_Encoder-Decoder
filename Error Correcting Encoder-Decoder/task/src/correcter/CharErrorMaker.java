package correcter;

import java.util.Random;

public class CharErrorMaker extends ErrorMaker {
    protected static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 ";
    String phrase;
    int perCharacter;

    CharErrorMaker(byte[] phrase, int perCharacter) {
        super(phrase);
        this.perCharacter = perCharacter;
    }

    @Override
    public byte[] addError() {
        Random rn = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < phrase.length(); i++) {
            if (i % perCharacter == 0) {
                char noise = alphabet.charAt(rn.nextInt(alphabet.length()));
                sb.append(noise);
            } else {
                sb.append(phrase.charAt(i));
            }
        }
        return sb.toString().getBytes();
    }
}
