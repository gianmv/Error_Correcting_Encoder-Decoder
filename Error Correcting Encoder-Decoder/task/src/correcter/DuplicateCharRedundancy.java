package correcter;

public class DuplicateCharRedundancy extends Redundancy {

    int numRedundancy;

    DuplicateCharRedundancy(byte[] phrase, int numRedundancy) {
        super(phrase);
        this.numRedundancy = numRedundancy;
    }

    @Override
    public byte[] addRedundancy() {
        StringBuilder sb = new StringBuilder();
        char[] temp = toChar(this.phrase);
        for (char x: temp) {
            for (int i = 0; i < numRedundancy; i++) {
                sb.append(x);
            }
        }

        return sb.toString().getBytes();
    }
}

