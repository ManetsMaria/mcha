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
public class Degree {
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
        System.out.println(norma(A));
        System.out.println();
    }
    public static double norma(double [][]r){
        double norma=Math.abs(r[0][0]);
        for (int i=1; i<r.length; i++)
            if (Math.abs(r[i][0])>norma)
                norma=Math.abs(r[i][0]);
        return norma;
    }
    public static double scalarPr(double [][] x1, double [][] x2){
        double answer=0;
        for (int i=0; i<x1.length; i++)
            answer+=x1[i][0]*x2[i][0];
        return answer;
    }
    public static double[][] minus(double[][] Ax, double [][]b){
        int n=Ax.length;
        double [][] f=new double [n][Ax[0].length];
        for (int i=0; i<n; i++)
            for (int j=0; j<Ax[0].length; j++)
            f[i][j]=Ax[i][j]-b[i][j];
        return f;
    }
    public static double[][] getE(int n){
        double [][] E =new double[n][n];
        for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
        {
            E[i][j] = 0.0;

            if (i == j)
                E[i][j] = 1.0;
        }
        return E;
    }
    public static double[][] multDouble(double[][] r, double n){
        int N=r.length;
        double [][] f=new double [N][r[0].length];
        for (int i=0; i<r.length; i++)
            for (int j=0; j<r[0].length; j++)
            f[i][j]=r[i][j]*n;
        return f;
    }
    public static void Degr(double [][] A)throws Ex{
        int n=A.length;
        int it=0;
        double sootn=1;
        double razn=0;
        double[][]y=new double [n][1];
        for (int i=0; i<n; i++){
            y[i][0]=1;
        }
        while(Math.abs(razn-sootn)>0.1){
            y=mult(A,y);
            print(y);
            y=multDouble(y, (1/norma(y)));
            print(y);
            razn=sootn;
            it++;
            sootn=mult(A,y)[0][0]/y[0][0];
            System.out.println(sootn);
            System.out.println();
        }
        System.out.println(it);
        System.out.println();
        System.out.println(sootn);
        System.out.println();
        print(y);
        print(minus(mult(A,y),multDouble(y,sootn)));
    }
    public static void main(String[] args) throws Ex{
        double [][]A=readMatrix("A1.txt");
        //A=mult(transpon(A),A);
        print(A);
        Degr(A);
    }
}