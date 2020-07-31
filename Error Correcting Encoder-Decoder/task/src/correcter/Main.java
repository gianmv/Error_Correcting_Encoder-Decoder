package correcter;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner console = new Scanner(System.in);
        System.out.print("Write a mode: ");
        String mode = console.nextLine();
        System.out.println();

        ByteWrapper formatter = new ByteWrapper();

        byte[] text;
        switch (mode) {
            case "encode":
                System.out.println("send.txt");
                text = readFile("send.txt");
                byte[] textWithRedundancy = new HammingRedundancy(text).addRedundancy();
                formatter.setData(text);
                System.out.println("text view:\n" + formatter.stringRepresentation());
                System.out.println("hex view:\n" + formatter.hexadecimalRepresentation(8));
                System.out.println("bin view:\n" + formatter.binaryRepresentation(8));
                System.out.println();
                formatter.setData(textWithRedundancy);
                System.out.println("expand and parity:\n" + formatter.binaryRepresentation(8));
                System.out.println("hex view:\n" + formatter.hexadecimalRepresentation(8));
                writeFile("encoded.txt",textWithRedundancy);
                break;
            case "send":
                System.out.println("encoded.txt");
                text = readFile("encoded.txt");
                formatter.setData(text);
                System.out.println("hex view:\n" + formatter.hexadecimalRepresentation(8));
                System.out.println("bin view:\n" + formatter.binaryRepresentation(8));
                System.out.println();
                byte[] textWithError = new BitErrorMaker(text).addError();
                System.out.println("received.txt");
                formatter.setData(textWithError);
                System.out.println("bin view:\n" + formatter.binaryRepresentation(8));
                System.out.println("hex view:\n" + formatter.hexadecimalRepresentation(8));

                writeFile("received.txt",textWithError);
                break;
            case "decode":
                System.out.println("received.txt");
                text = readFile("received.txt");
                formatter.setData(text);
                System.out.println("hex view:\n" + formatter.hexadecimalRepresentation(8));
                System.out.println("bin view:\n" + formatter.binaryRepresentation(8));
                System.out.println();
                System.out.println("decoded.txt");
                byte[] textDecoded = new HammingErrorCorrecter(text).corrected();
                formatter.setData(textDecoded);
                System.out.println("bin:\n" + formatter.binaryRepresentation(8));
                System.out.println("text: " + formatter.stringRepresentation());
                writeFile("decoded.txt",textDecoded);
                break;

        }

    }

    public static byte[] readFile(String path) throws IOException {
        File file = new File(path);
        byte[] ans = {0x00};
        try(BufferedInputStream input = new BufferedInputStream(new FileInputStream(file))) {
            int numBytes = input.available();
            ans = new byte[numBytes];
            input.read(ans);
        }
        return ans;
    }

    public static void writeFile(String path, byte[] content) throws IOException {
        File file = new File(path);
        try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file))) {
            for (byte x: content) {
                output.write(x);
            }
        }
    }

}