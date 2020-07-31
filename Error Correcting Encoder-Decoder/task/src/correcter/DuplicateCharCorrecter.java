package correcter;

import java.util.HashMap;

public class DuplicateCharCorrecter extends ErrorCorrecter {

    int numRedundancy;

    DuplicateCharCorrecter(byte[] received, int numRedundancy) {
        super(received);
        this.numRedundancy = numRedundancy;
    }

    @Override
    byte[] corrected() {
        StringBuilder sb = new StringBuilder();
        char[] rec = toChar(this.received);
        HashMap<Character, Integer> characterCount = new HashMap<>();
        for (int i = 0; i < rec.length; i += numRedundancy) {
            for (int j = i; j < i + numRedundancy; j++) {
                characterCount.put(rec[j],0);
            }

            for (int j = i; j < i + numRedundancy; j++) {
                char value = rec[j];
                characterCount.put(value, characterCount.get(value) + 1);
            }

            int count = -1;
            char ans = ' ';
            for (char x: characterCount.keySet()) {
                if (characterCount.get(x) > count) {
                    ans = x;
                    count = characterCount.get(x);
                }
            }
            sb.append(ans);
            characterCount.clear();
        }

        return sb.toString().getBytes();
    }
}
