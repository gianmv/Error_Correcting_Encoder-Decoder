package correcter;

public abstract class ErrorCorrecter {
    byte[] received;

    ErrorCorrecter(byte[] received) {
        this.received = received;
    }

    abstract byte[] corrected();

    public char[] toChar(byte[] word) {
        char[] ans = new char[word.length/2];
        int cont = 0;
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (char) word[cont++];
            ans[i] = (char) (ans[i] << 8);
            ans[i] = (char) (ans[i] | word[cont++]);
        }
        return ans;
    }
}
