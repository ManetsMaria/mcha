import java.io.File;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Collections;
import java.io.IOException;
class Ex extends Exception
{
	Ex(String message)
	{
		super(message);
	}
}
public class Vr {
	public static double[][] readMatrix(String filename) throws Ex{
        double [][] k= new double[10][10];
        int checkLength=-1;
        int row=0;
        try{
        Scanner in = new Scanner(new File(filename));
        while (in.hasNextLine()){
            String line = in.nextLine();
            String [] c=line.split("\\s+");
            if (checkLength==-1){
                checkLength=c.length;
            }
            else{
                if (c.length!=checkLength) throw new Ex("check numbs of args in this line");
            }
            try{
                for (int i=0; i<c.length; i++){
                    k[row][i]= Double.valueOf(c[i]);
                }
            }
            catch(NumberFormatException e){
                System.err.println("Some args are not numbers in this line "+e.getMessage());
                System.exit(0);
            }
            row++;
        }
        }
        catch(IOException u){
            System.err.println("don't find file"+u.getMessage());
            System.exit(0); 
        }
        double [][] matrix=new double[row][checkLength];
        for (int i=0; i<row; i++)
            for (int j=0; j<checkLength; j++)
                matrix[i][j]=k[i][j];
        return matrix;
    }
    public static double[][] mult(double [][] m1, double [][] m2) throws Ex{
        if (m1[0].length!=m2.length) throw new Ex("Can't multiply it");
        int raw=m1.length, len=m2[0].length, o=m2.length;
        double [][] answer=new double[raw][len];
        for (int i=0; i<raw; i++)
            for (int j=0; j<len; j++)
                for (int t=0; t<o; t++)
                    answer[i][j] += m1[i][t] * m2[t][j];
        return answer;
    }
    public static void print(double [][] m){
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                System.out.print(m[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }
    public static double[][] transpon(double [][] m){
    	double buffer[][] = new double [m[0].length][m.length];
		for (int i=0; i<m.length; i++)
		{
			for (int j=0; j<m[i].length; j++)
			{
				buffer[j][i]=m[i][j];
			}
		}
    	return buffer;
    }
    public static void printR(double [][] A, double [][] b){
        int n=A.length;
        for (int i=0; i<n; i++)
            A[i][0]-=b[i][0];
        print(A);
        double norma=Math.abs(A[0][0]);
        for (int i=1; i<n; i++)
            if (Math.abs(A[i][0])>norma)
                norma=Math.abs(A[i][0]);
        System.out.println(norma);
    }
    public static double[][] upD(double [][] A, double [][]f){
        int n=A.length;
        for (int i=0; i<n-1; i++){
            for (int j=i+1; j<n; j++){
                double c=A[i][i]/Math.sqrt(A[i][i]*A[i][i]+A[j][i]*A[j][i]);
                double s=A[j][i]/Math.sqrt(A[i][i]*A[i][i]+A[j][i]*A[j][i]);
                double time=f[0][i];
                f[0][i]=c*f[0][i]+s*f[0][j];
                f[0][j]=(-1)*s*time+c*f[0][j];
                double [] timeA=new double[n];
                System.arraycopy(A[i], 0, timeA, 0, n); 
                for (int o=i; o<n; o++){
                    A[i][o]=c*A[i][o]+s*A[j][o];
                    A[j][o]=(-1)*s*timeA[o]+c*A[j][o];
                }
            }
        }
        double [][] upM=new double[n][n+1];
        for (int i=0; i<n; i++){
            for (int j=0; j<n; j++)
                upM[i][j]=A[i][j];
            upM[i][n]=f[0][i];
        }
        print(upM);
        return upM;
    }
    public static double [][] oG(double [][] upM){
        int N=upM.length;
        for(int i=N-1; i>=0; i--) //обратный ход
        for (int k=N; k>=0; k--)
            upM[i][k]=upM[i][k]/upM[i][i];
        for(int i=N-1; i>0; i--)
        for (int k=i-1; k>=0; k--)
            {
                double div=upM[k][i];
                for (int z=N; z>=i; z--)
                {
                    upM[k][z]-=upM[i][z]*div;
                }
            }
        double [][]x=new double [N][1];
        for (int i=0; i<N; i++)
            x[i][0]=upM[i][N];
        return x;
    }
    public static void main(String[] args) throws Ex{
        double [][]A=readMatrix("A.txt");
        double [][]f=readMatrix("b.txt");
        double [][]upM=upD(A, f);
        //double [][]x=oG(upM);
        //print(x);
        //printR(mult(A,x),transpon(f));
    }
}