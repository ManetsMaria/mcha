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
public class Pr {
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
    public static void main(String[] args) throws Ex{
        double [][]A=readMatrix("A.txt");
        int n=A.length;
        double [][]f=readMatrix("b.txt");
        double [] a=new double[n];
        a[0]=0;
        double [] b=new double[n];
        b[n-1]=0;
        double [] c=new double[n];
        c[n-1]=A[n-1][n-1];
        for (int i=0; i<n-1; i++){
            a[i+1]=A[i+1][i];
            b[i]=A[i][i+1];
            c[i]=A[i][i];
        }
        double [] e = new double[n];
        double [] m=new double[n];
        double [][] x= new double[1][n];
        for (int i=n-2; i>=0; i--){
            e[i]=((-1)*a[i+1])/(c[i+1]+b[i+1]*e[i+1]);
            m[i]=(f[0][i+1]-b[i+1]*m[i+1])/(c[i+1]+b[i+1]*e[i+1]);
        }
        
        x[0][0]=(f[0][0]-b[0]*m[0])/(b[0]*e[0]+c[0]);
        for (int i=1; i<n; i++){
            x[0][i]=e[i-1]*x[0][i-1]+m[i-1];
        }
        x=transpon(x);
        print(x);
        A=mult(A, x);
        printR(A, transpon(f));
    }
}