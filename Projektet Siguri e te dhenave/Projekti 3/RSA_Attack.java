import java.math.BigInteger;

public class RSA_Attack
{
 public static void main (String[] args)
 {
        BigInteger n = new BigInteger(args[0]); // 797306344204135429053419
        BigInteger e = new BigInteger(args[1]); // 920419823
        BigInteger a = bigIntSqRootCeil(n);
        BigInteger x = bigIntSqRootCeil(a.multiply(a).subtract(n));
        BigInteger p = a.subtract(x);
        BigInteger q = a.add(x);
        System.out.println("p = " + p + " dhe q = " + q);
        BigInteger one = new BigInteger("1");
        BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));
        BigInteger d = e.modInverse(phi);
        System.out.println("d = " + d);
 }
 
 public static BigInteger bigIntSqRootCeil(BigInteger x) throws IllegalArgumentException 
 {
        if (x.compareTo(BigInteger.ZERO) < 0) 
        {
            throw new IllegalArgumentException("Negative argument.");
        }
        
        // The square roots of 0 and 1 are trivial and
        // y == 0 will cause a divide-by-zero exception
        
        if (x == BigInteger.ZERO || x == BigInteger.ONE) 
        {
            return x;
        }
        
        BigInteger two = BigInteger.valueOf(2L);
        BigInteger y;
        
        // Starting with y = x / 2 avoids magnitude issues with x squared
        
        for (y = x.divide(two);
             y.compareTo(x.divide(y)) > 0;
             y = ((x.divide(y)).add(y)).divide(two));
             
        if (x.compareTo(y.multiply(y)) == 0) 
        {
            return y;
        } 
        else 
        {   
            return y.add(BigInteger.ONE);
        }
    }
}