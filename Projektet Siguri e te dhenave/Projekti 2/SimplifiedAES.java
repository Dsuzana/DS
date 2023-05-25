import java.util.*;
import java.io.*;

public class SimplifiedAES 
{
    static String[] W = new String[16];

    //setting the sbox for nibble substitution
    static String[] sbox = {"0110", "1011", "0000", "0100", 
                            "0111", "1110", "0010", "1111", 
                            "1001", "1000", "1010", "1100", 
                            "0011", "0001", "0101", "1101"};

    //setting the inverse sbox for inverse nibble substitution
    static String[] sbox_inverse = {"0010", "1101", "0110", "1100", 
                                    "0011", "1110", "0000", "0100", 
                                    "1001", "1000", "1010", "0001", 
                                    "1011", "1111", "0101", "0111"};

    public static void main(String[] args) throws IOException 
    {
        Scanner s = new Scanner(System.in);
        BufferedReader ob = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Press 1 for encryption");
        System.out.println("Press 2 for decryption");
        System.out.println("Press 3 for brute force attack");
        int ch = s.nextInt();
        if (ch == 1) 
           {
            System.out.println("Enter 16 bit binary plaintext");
            String plaintext = ob.readLine();
            System.out.println("Enter 16 bit binary key");
            String key = ob.readLine();
            keyGeneration(key);
            System.out.println("The corresponding ciphertext:");
            encrypt(plaintext);
           } 
        else if (ch == 2) 
           {
            System.out.println("Enter 16 bit binary ciphertext");
            String ciphertext = ob.readLine();
            System.out.println("Enter 16 bit binary key");
            String key = ob.readLine();
            keyGeneration(key);
            System.out.println("The corresponding plaintext:");
            decrypt(ciphertext);
           } 
        else if (ch == 3)
           {
            System.out.println("Enter 16 bit binary encrypted message you're trying to break");
            String encMsg = ob.readLine();
            for(int i = 0; i < Math.pow(2,16); i++){
                for(int j = 0; j < Math.pow(2,16); j++)
                {
                 String s1 = String.format("%" + 16 + "s",
                             Integer.toBinaryString(i)).replaceAll(" ", "0");
                 String s2 = String.format("%" + 16 + "s",
                             Integer.toBinaryString(j)).replaceAll(" ", "0");
                        
                 //the code for round one
                 String temp_value = xor2(s1, s2);
                 String s3 = nibbleSubstitution(temp_value.substring(0, 8));
                 String s4 = nibbleSubstitution(temp_value.substring(8));
                 temp_value = s3 + s4;
                 temp_value = shiftRow(temp_value);
                 temp_value = mixColumn(temp_value);
                 keyGeneration(s2);
                 temp_value = xor2(temp_value, W[4] + W[5] + W[6] + W[7]);
            
                 if(temp_value.equals(encMsg)){
                    System.out.println("Plain text: " + s1 + 
                                       "\nKey:      " + s2);
                 }
               }
             }
             System.out.println("Encrypted message: " + encMsg);
             System.out.println("Generations ");
           }
        else 
           {
            System.out.println("Invalid Choice! Run again");
           }
    }

    public static void encrypt(String plaintext) 
    {
        //adding Round 0 key
        String temp_value = xor2(plaintext, W[0] + W[1] + W[2] + W[3]);

        //nibble substitution using SBOX-es
        String s1 = nibbleSubstitution(temp_value.substring(0, 8));
        String s2 = nibbleSubstitution(temp_value.substring(8));
        temp_value = s1 + s2;

        //shift row function, swapping 2nd row values
        temp_value = shiftRow(temp_value);

        //calling the mix column function
        temp_value = mixColumn(temp_value);

        //adding round 1 key
        temp_value = xor2(temp_value, W[4] + W[5] + W[6] + W[7]);

        //nibble substitution
        temp_value = nibbleSubstitution(temp_value.substring(0, 8)) +
                     nibbleSubstitution(temp_value.substring(8));

        //performing shift row again
        temp_value = shiftRow(temp_value);
        
        //calling the mix column function
        temp_value = mixColumn(temp_value);
        
        //adding round 2 key
        temp_value = xor2(temp_value, W[8] + W[9] + W[10] + W[11]);
        
        //FINAL ROUND
        //nibble substitution
        temp_value = nibbleSubstitution(temp_value.substring(0, 8)) +
                     nibbleSubstitution(temp_value.substring(8));

        //performing shift row again
        temp_value = shiftRow(temp_value);

        //ciphertext obtained after adding round 3 key
        String cipher_text = xor2(temp_value, W[12] + W[13] + W[14] + W[15]);
        System.out.println(cipher_text);
    }

    public static void decrypt(String ciphertext) {
        //adding round 3 key
        String temp_value = xor2(ciphertext, W[12] + W[13] + W[14] + W[15]);

        //performing inverse shift row
        temp_value = shiftRow(temp_value);

        //inverse nibble substitution
        temp_value = inverseNibbleSubsitution(temp_value.substring(0, 8)) +
                     inverseNibbleSubsitution(temp_value.substring(8));
        
        //adding round 2 key
        temp_value = xor2(temp_value, W[8] + W[9] + W[10] + W[11]);

        //performing inverse mix column
        temp_value = inverseMixColumn(temp_value);

        //inverse shift row again
        temp_value = shiftRow(temp_value);

        //inverse nibble substitution
        temp_value = inverseNibbleSubsitution(temp_value.substring(0, 8)) +
                     inverseNibbleSubsitution(temp_value.substring(8));

        //adding round 1 key
        temp_value = xor2(temp_value, W[4] + W[5] + W[6] + W[7]);

        //performing inverse mix column
        temp_value = inverseMixColumn(temp_value);

        //inverse shift row again
        temp_value = shiftRow(temp_value);

        //inverse nibble substitution
        temp_value = inverseNibbleSubsitution(temp_value.substring(0, 8)) +
                     inverseNibbleSubsitution(temp_value.substring(8));

        //adding Round 0 key to get the plaintext
        String plaintext = xor2(temp_value,  W[0] + W[1] + W[2] + W[3]);
        System.out.println(plaintext);
    }

    public static String shiftRow(String str) {
        String s1 = str.substring(0, 4);
        String s2 = str.substring(4, 8);
        String s3 = str.substring(8, 12);
        String s4 = str.substring(12);
        return s1 + s4 + s3 + s2;
    }

    public static void keyGeneration(String key) {
        W[0] = key.substring(0, 4);
        W[1] = key.substring(4, 8);
        W[2] = key.substring(8, 12);
        W[3] = key.substring(12);
         
        W[4] = xor1(W[0], keyFunction(W[3],1));
        W[5] = xor1(W[0], W[1]);
        W[6] = xor1(W[1], W[2]);
        W[7] = xor1(W[2], W[3]);
        
        W[8] = xor1(W[4], keyFunction(W[7],2));
        W[9] = xor1(W[4], W[5]);
        W[10] = xor1(W[5], W[6]);
        W[11] = xor1(W[6], W[7]);
        
        W[12] = xor1(W[8], keyFunction(W[11],3));
        W[13] = xor1(W[8], W[9]);
        W[14] = xor1(W[9], W[10]);
        W[15] = xor1(W[10], W[11]);
    }
    
    public static String keyFunction(String key, int round) {
       char a = key.charAt(0);
       char b = key.charAt(1);
       char c = key.charAt(2);
       char d = key.charAt(3);
       String s = ""+ b + c + d + a;
       s = sbox[Integer.parseInt(s, 2)];
       
       if (round == 1){
           s = xor1(s, "0001");
          }
          
       else if (round == 2){
                s = xor1(s, "0010");
               }
               
       else if (round == 3){
                s = xor1(s, "0100");
               }
       return s;
    }

    public static String nibbleSubstitution(String key) {
        //using sbox to substitute nibbles
        String s1 = sbox[Integer.parseInt(key.substring(0, 4), 2)];
        String s2 = sbox[Integer.parseInt(key.substring(4, 8), 2)];
        return s1 + s2;
    }

    public static String inverseNibbleSubsitution(String key) {
        //using inverse sbox to substitute nibbles
        String s1 = sbox_inverse[Integer.parseInt(key.substring(0, 4), 2)];
        String s2 = sbox_inverse[Integer.parseInt(key.substring(4, 8), 2)];
        return s1 + s2;
    }

    public static String mixColumn(String str) {
        String s1 = xor1(str.substring(0, 4), str.substring(4, 8));
        String s2 = xor1(str.substring(0, 4), multiplication("0010", str.substring(4, 8)));
        String s3 = xor1(str.substring(8, 12), str.substring(12));
        String s4 = xor1(str.substring(8, 12), multiplication("0010", str.substring(12)));
        return s1 + s2 + s3 + s4;
    }

    public static String inverseMixColumn(String str) {
        String s1 = xor1(multiplication("1111", str.substring(0, 4)),
                         multiplication("1110", str.substring(4, 8)));

        String s2 = xor1(multiplication("1110", str.substring(0, 4)),
                         multiplication("1110", str.substring(4, 8)));

        String s3 = xor1(multiplication("1111", str.substring(8, 12)),
                         multiplication("1110", str.substring(12)));

        String s4 = xor1(multiplication("1110", str.substring(8, 12)),
                         multiplication("1110", str.substring(12)));

        return s1 + s2 + s3 + s4;
    }

    public static String multiplication(String s1, String s2) {
        // multiplication of two polynomials provided in the mix column
        int t1 = Integer.parseInt(s1, 2);
        int t2 = Integer.parseInt(s2, 2);
        int m = t1 * t2;
        if (m>19)
           {
            m = m ^ 19;
           }
        String ans = String.format("%" + 4 + "s",Integer.toBinaryString(m)).replaceAll(" ", "0");
        return ans;
    }
    
    public static String xor1(String a, String b) {
        int t1 = Integer.parseInt(a, 2);
        int t2 = Integer.parseInt(b, 2);
        int i = t1 ^ t2;
        if (i>19)
           {
            i = i ^ 19;
           }
        String s = String.format("%" + 4 + "s",Integer.toBinaryString(i)).replaceAll(" ", "0");
        return s;
    }
    
    public static String xor2(String a, String b) {
        int t1 = Integer.parseInt(a, 2);
        int t2 = Integer.parseInt(b, 2);
        int i = t1 ^ t2;
        if (i>19)
           {
            i = i ^ 19;
           }
        String s = String.format("%" + 16 + "s",Integer.toBinaryString(i)).replaceAll(" ", "0");
        return s;
    }
}