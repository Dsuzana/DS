import java.util.*;

/*This class implements a simplified SHA-1 algorithm
*/
public class Simplified_SHA_1 
{
    public static int[] multiplesOf32 = {32, 64, 96, 128, 160, 192, 224, 256, 288, 320, 352, 384, 416, 448, 480, 512};

    public static void main(String[] args) {

        //Getting the word
        System.out.println("Insert a word or phrase to be hashed");
        Scanner sc = new Scanner(System.in);
        String word = sc.nextLine();
        System.out.println("Plain Text: " + word);

        //Converting the word to binary
        String binary = convertToBinary(word);
        System.out.println("String in binary form: " + binary);
        
        calculate(word, binary);

    }

    public static String convertToBinary(String word) {

        byte[] bytes = word.getBytes();
        StringBuilder binary = new StringBuilder();

        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }

        return binary.toString();

    }
    
    public static void calculate(String word, String binary) {

        //Padding
        int binaryMessageLength = word.length() * 8;
        System.out.println("Binary message length: " + binaryMessageLength);
        String endBitLength = calculateMessageLength(binaryMessageLength);
        System.out.println("Last 16 bits: " + endBitLength);
        int paddingLength = 0;
        
        for(int i = 0; i < multiplesOf32.length; i++)
           {
            if (multiplesOf32[i] > binaryMessageLength+16)
               {
                paddingLength = multiplesOf32[i];
                break;
               }
           }
        
        System.out.println("Padding Length: " + paddingLength);
        int temp = paddingLength - (binaryMessageLength+16);   
        int binaryZeros = temp - 1;
        System.out.println("The k-zeros: " + binaryZeros);
        String onePadded = "1";
        binary = binary.replaceAll("\\s+", "");
        System.out.println("Word in binary without spaces " + binary);
        
        //Calling the method for creating the 32*n bit message
        createMessageLength(binary, onePadded, binaryZeros, endBitLength);

    }

    public static String calculateMessageLength(int bitLength) {

        String tempBitsLength = Integer.toBinaryString(bitLength);
        StringBuilder sb = new StringBuilder(tempBitsLength);
        int temp = 16 - tempBitsLength.length();

        while (temp > 0) {
              sb.insert(0, 0);
              temp--;
        }

        return sb.toString();

    }

    public static String createMessageLength(String message, String paddedOne, int zeros, String endLength) {

        //Creating the complete message
        StringBuilder messageBinary = new StringBuilder(message);
        messageBinary.insert(messageBinary.toString().length(), paddedOne);

        while (zeros > 0) {
            messageBinary.insert(messageBinary.toString().length(), 0);
            zeros--;
        }

        messageBinary.insert(messageBinary.toString().length(), endLength);
        
        String m = printMessage(messageBinary.toString());
        m = m.replaceAll("\\s+", "");
        int[] mArray = new int[m.toString().length()/8];

        for (int i = 0; i < m.toString().length(); i+=8) {
            mArray[i/8] = Integer.valueOf(m.substring(i+1, i+8),2);
            if(m.charAt(i) == '1'){
               mArray[i/8] |= 0X80;
            }
            System.out.printf("Decimal(Iterator), String(Binary), Hex values of input: %d %s %x\n", i, m.substring(i, i+8), mArray[i/8]);
        }

        //Calling the method that hashes the message
        hash(mArray);
        return messageBinary.toString();

    }

    public static String printMessage(String message) {

        StringBuilder sb = new StringBuilder(message);
        int num = message.length();

        while (num > 0) {
              if (num % 8 == 0) {
                  sb.insert(num, " ");
              }
              num--;
        }

        return sb.toString();

    }

    private static int leftrotate(int x, int shift) {

        StringBuilder binary = new StringBuilder();
        
        for (int i = 0; i < 8; i++) 
            {
             binary.append((x & 128) == 0 ? 0 : 1);
             x <<= 1;
            }
  
        String s = binary.toString();    
        
        for (int i = 0; i < shift; i++)
            {
             s = s.charAt(1) + s.substring(2, s.length()) +s.charAt(0);
            }
            
        int rotation = Integer.parseInt(s, 2);
        return rotation;

    }
    
    private static int add(int x, int y) {
    
        int sum = x + y;
        for (int i = 0; i <= 10; i++)
            {
             if (sum > 255)
                {
                 sum = sum - 256;
                }
            }
        return sum;
    
    }

    //Creating the instance variables
    private static int h1 = 0x45;
    private static int h2 = 0xAF;
    private static int h3 = 0xAC;
    private static int h4 = 0xFE;
    private static int k1 = 0x5A;
    private static int k2 = 0xE7;
    private static int k3 = 0x8C;
    private static int k4 = 0xBD;


    private static String hash(int[] z) {

        //Extending the four 8-bit words into sixteen 8-bit words
        int integer_count = z.length;
        int[] intArray = new int[16];
        int j = 0;

        for(int i = 0; i < integer_count; i += 4) {
            for(j = 0; j <= 3; j++)
                intArray[j] = z[j+i];
            for ( j = 4; j <= 15; j++ ) {
                intArray[j] = leftrotate(intArray[j - 4] ^ intArray[j - 2], 2);
            }

            //Calculating A,B,C,D
            int A = h1;
            int B = h2;
            int C = h3;
            int D = h4;
            
            //Variable t changes the value based on the function
            int t = 0;

            for ( int x = 0; x <= 3; x++ ) {
                t = add(add(add(D,(B&C)),leftrotate(A,3)),add(intArray[x],k1));
                B=A; D=C; C=leftrotate(B,7); A=t;
            }
            
            for ( int b = 4; b <= 7; b++ ) {
                t = add(add(add(D,(B^C)),leftrotate(A,3)),add(intArray[b],k2));
                B=A; D=C; C=leftrotate(B,7); A=t;
            }
            for (int c = 8; c <= 11; c++ ) {
                t = add(add(add(D,(B^(~C))),leftrotate(A,3)),add(intArray[c],k3));
                B=A; D=C; C=leftrotate(B,7); A=t;
            }
            for ( int d = 12; d <= 15; d++ ) {
                t = add(add(add(D,(B^C)),leftrotate(A,3)),add(intArray[d],k4));
                B=A; D=C; C=leftrotate(B,7); A=t;
            }

            h1 = add(h1,A);
            h2 = add(h2,B);
            h3 = add(h3,C);
            h4 = add(h4,D);

        }

        String h1Length = Integer.toHexString(h1);
        String h2Length = Integer.toHexString(h2);
        String h3Length = Integer.toHexString(h3);
        String h4Length = Integer.toHexString(h4);

        if(h1Length.length() < 2) {
            StringBuilder h1L = new StringBuilder(h1Length);
            h1L.insert(0,0);
            h1Length = h1L.toString();
        } else if(h2Length.length() < 2) {
            StringBuilder h2L = new StringBuilder(h2Length);
            h2L.insert(0,0);
            h2Length = h2L.toString();
        } else if(h3Length.length() < 2) {
            StringBuilder h3L = new StringBuilder(h3Length);
            h3L.insert(0,0);
            h3Length = h3L.toString();
        } else if(h4Length.length() < 2) {
            StringBuilder h4L = new StringBuilder(h4Length);
            h4L.insert(0,0);
            h4Length = h4L.toString();
        }

        //Printing the hashing result
        String hh = h1Length + h2Length + h3Length + h4Length;
        System.out.println("Result: " + hh);

        return null;
    }
    
}