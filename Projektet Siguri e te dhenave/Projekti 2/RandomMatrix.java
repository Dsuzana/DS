import java.util.*;
public class RandomMatrix 
{
    public static void main (String  [] args) {
            
        System.out.println("Matrica A");
        
        int [][] A = generateA();
        
        System.out.println();
        
        int[][] matrix_1 = firstGeneration();
        if (findDeterminant(matrix_1) == 0)
           {firstGeneration();}
                 
        int[][] matrix_2 = secondGeneration(matrix_1);
        if (findDeterminant(matrix_2) == 0)
           {secondGeneration(matrix_1);}
        
        System.out.println();
        System.out.println("Matrica MDS");
        
        int [][] MDS = generateMDS(matrix_1, matrix_2);
        if (findDeterminant(MDS) == 0)
           {generateMDS(matrix_1, matrix_2);}
        
        System.out.println();
        System.out.println();
        System.out.println("Matrica B = MDS * A");
        
        int [][] B = multiply(A, MDS);
    }
    
    public static int[][] generateA()
    {
        int [][] A = new int [4][4];

        for (int i=0; i<A.length; i++) {
             System.out.printf("\n");
             for (int j=0; j<A[i].length; j++) {
                 A[i][j] = (int) (Math.random()*255);
                 System.out.printf("%-5d", A [i][j]);
                 }           
            }
       return A;
    }
    
    public static int[][] firstGeneration()
    {
        int [][] matrix_1 = new int [2][2];

        for (int i=0; i<matrix_1.length; i++) {
             for (int j=0; j<matrix_1[i].length; j++) {
                 matrix_1[i][j] = (int) (Math.random()*255);           
             }
        }
        return matrix_1;
    }
        
    public static int[][] secondGeneration(int[][] matrix_1)
    { 
        int [][] matrix_2 = new int [3][3];

        for (int i=0; i<matrix_2.length; i++) {
             for (int j=0; j<matrix_2[i].length; j++) {
                 matrix_2[i][j] = (int) (Math.random()*255);
                 matrix_2[1][0] = matrix_1[0][0];
                 matrix_2[1][1] = matrix_1[0][1];
                 matrix_2[2][0] = matrix_1[1][0];
                 matrix_2[2][1] = matrix_1[1][1];
                 }           
             }
        return matrix_2;
    }
    
    public static int[][] generateMDS(int[][] matrix_1, int[][] matrix_2)
    {
        int [][] matrix_3 = new int [4][4];

        for (int i=0; i<matrix_3.length; i++) {
             System.out.printf("\n");
             for (int j=0; j<matrix_3[i].length; j++) {
                 matrix_3[i][j] = (int) (Math.random()*255);
                 matrix_3[2][0] = matrix_1[0][0];
                 matrix_3[2][1] = matrix_1[0][1];
                 matrix_3[3][0] = matrix_1[1][0];
                 matrix_3[3][1] = matrix_1[1][1];
                 matrix_3[1][0] = matrix_2[0][0];
                 matrix_3[1][1] = matrix_2[0][1];
                 matrix_3[1][2] = matrix_2[0][2];
                 matrix_3[2][2] = matrix_2[1][2];
                 matrix_3[3][2] = matrix_2[2][2];

                 System.out.printf("%-5d", matrix_3 [i][j]);
                 }           
             }
        return matrix_3;
    }
    
    public static int findDeterminant(int[][] matrix)
    {
        int determinant = 0;
        if (matrix.length == 2)
           {
            determinant = matrix[0][0]*matrix[1][1]-matrix[0][1]*matrix[1][0];
           }
           
        else if (matrix.length == 3)
           {
            determinant = matrix[0][0]*(matrix[1][1]*matrix[2][2]-matrix[1][2]*matrix[2][1])
                        - matrix[0][1]*(matrix[1][0]*matrix[2][2]-matrix[1][2]*matrix[2][0])
                        + matrix[0][2]*(matrix[1][0]*matrix[2][1]-matrix[1][1]*matrix[2][0]);
           }
        else if (matrix.length == 4)
           {
            determinant = matrix[0][0]*(matrix[1][1]*(matrix[2][2]*matrix[3][3]-matrix[2][3]*matrix[3][2])
                                      - matrix[1][2]*(matrix[2][1]*matrix[3][3]-matrix[2][3]*matrix[3][1])
                                      + matrix[1][3]*(matrix[2][1]*matrix[3][2]-matrix[2][2]*matrix[3][1]))
                        - matrix[0][1]*(matrix[1][0]*(matrix[2][2]*matrix[3][3]-matrix[2][3]*matrix[3][2])
                                      - matrix[1][2]*(matrix[2][0]*matrix[3][3]-matrix[2][3]*matrix[3][0])
                                      + matrix[1][3]*(matrix[2][0]*matrix[3][2]-matrix[2][2]*matrix[3][0]))
                        + matrix[0][2]*(matrix[1][0]*(matrix[2][1]*matrix[3][3]-matrix[2][3]*matrix[3][1])
                                      - matrix[1][1]*(matrix[2][0]*matrix[3][1]-matrix[2][3]*matrix[3][0])
                                      + matrix[1][3]*(matrix[2][0]*matrix[3][1]-matrix[2][1]*matrix[3][0]))
                        - matrix[0][3]*(matrix[1][0]*(matrix[2][1]*matrix[3][2]-matrix[2][2]*matrix[3][1])
                                      - matrix[1][1]*(matrix[2][0]*matrix[3][2]-matrix[2][2]*matrix[3][0])
                                      + matrix[1][2]*(matrix[2][0]*matrix[3][1]-matrix[2][1]*matrix[3][0]));
           }
        return determinant;
    }
    
    public static int[][] multiply(int[][] a, int[][] b)
    {
     int [][] c = new int[4][4];
     for (int i = 0; i < a.length; i++) {
          System.out.printf("\n"); 
          for (int j = 0; j < b[0].length; j++) { 
               for (int k = 0; k < a[0].length; k++) {
                    int t; 
                    while(a[i][k] != 0)
                         {
                          if((a[i][k] & 1) != 0)
                            {
                             c[i][j] = c[i][j] ^ b[k][j];
                            }
                          t = b[k][j] & 0x80;
                          b[k][j] = b[k][j] << 1;
                          if(t != 0)
                            {
                             b[k][j] = b[k][j] ^ 0x11b;
                            }
                          a[i][k] = a[i][k] >> 1;
                         }
                    
                System.out.printf("%-5d", c [i][j]);
               }
          }
       }
       return c;
    }
}