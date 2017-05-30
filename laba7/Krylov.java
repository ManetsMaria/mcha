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
public class Krylov {
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
    public static double[][] plus(double[][] Ax, double [][]b){
        int n=Ax.length;
        double [][] f=new double [n][1];
        for (int i=0; i<Ax.length; i++)
            f[i][0]=Ax[i][0]+b[i][0];
        return f;
    }
    public static double[][] multDouble(double[][] r, double n){
        int N=r.length;
        double [][] f=new double [N][1];
        for (int i=0; i<r.length; i++)
            f[i][0]=r[i][0]*n;
        return f;
    }
    public static double[][] Gauss(double [][]AI, double[][]b){
        int N=AI.length;
        double [][]A=new double[N][N];
        for (int i=0; i<N; i++)
            for (int j=0; j<N; j++)
                A[i][j]=AI[i][j];
        for (int i=0; i<N; i++){
            b[i][0]/=A[i][i];
            for (int k=N-1; k>=0; k--){ // делим строку на найденный элемент
                 A[i][k]/=A[i][i];
            }
             for (int k=i+1; k<N; k++) //прямой ход
            {
                double div=A[k][i];
                b[k][0]-=b[i][0]*div;
                for (int z=i; z<N; z++)
                {
                    A[k][z]-=A[i][z]*div;
                }
            }
        }
        for(int i=N-1; i>0; i--) //обратный ход
            for (int k=i-1; k>=0; k--)
            {
                    double div=A[k][i];
                    b[k][0]-=b[i][0]*div;
                    for (int z=N-1; z>=i; z--)
                    {
                        A[k][z]-=A[i][z]*div;
                    }
            }
        return b;
    }
    public static void Krylov(double [][] A)throws Ex{
        int n=A.length;
        double [][]C=new double[n][n];
        double [][]cd =new double[n][1];
        double [][]c=new double[n][1];
        c[0][0]=1;
        C[0][n-1]=1;
        for (int i=1; i<n; i++){
            print(c);
            cd=mult(A, c);
            for (int j=0; j<n; j++){
                C[j][n-1-i]=cd[j][0];
            }
            c=cd;
            //print(c);
        }
        print(C);
        double [][]an=mult(A,c);
        print(an);
        double[][]q=Gauss(C,an);
        print(transpon(q));
        double max=0.8624765295908;
        double [][]b=new double[n][1];
        b[0][0]=1;
        for (int i=1; i<n; i++){
            b[i][0]=max*b[i-1][0]-q[i-1][0];
        }
        double[][] x=new double[n][1];
        for (int i=0; i<n; i++){
            double [][]y=new double[n][1];
            for (int j=0; j<n; j++)
                y[j][0]=C[j][i];
            x=plus(x, multDouble(y,b[i][0]));
        }
        x=multDouble(x,1.0/x[n-1][0]);
        print(x);
        print(plus(mult(A,x),multDouble(x,max*(-1))));
       
    }
    public static void main(String[] args) throws Ex{
        double [][]A=readMatrix("A1.txt");
        //A=mult(transpon(A),A);
        Krylov(A);
    }
}