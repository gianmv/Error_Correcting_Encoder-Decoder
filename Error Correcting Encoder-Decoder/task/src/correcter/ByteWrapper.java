package correcter;

public class ByteWrapper {
    protected byte[] data;

    ByteWrapper(byte[] data) {
        this.data = data;
    }

    ByteWrapper() {
        this.data = new byte[]{0x00};
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String binaryRepresentation(int columns) {
        StringBuilder sb = new StringBuilder();
        int cont = 1;
        for (byte x: this.data) {
            sb.append(String.format("%8s", Integer.toBinaryString(x & 0xFF)).replace(' ', '0'));
            sb.append(' ');
            if (cont % columns == 0) {
                sb.append("\n");
            }
            cont++;
        }

        if (sb.charAt(sb.length() - 1) == '\n') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    public String hexadecimalRepresentation(int columns){
        StringBuilder sb = new StringBuilder();
        int cont = 1;
        for (byte x: this.data) {
            sb.append(String.format("0x%02x ", x & 0xFF));
            if (cont % columns == 0) {
                sb.append("\n");
            }
            cont++;
        }
        if (sb.charAt(sb.length() - 1) == '\n') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public String stringRepresentation() {
        return new String(this.data);
    }
}
