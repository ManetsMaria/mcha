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
public class SqRoMe {
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
    public static void main(String[] args) throws Ex{
        double [][]A=readMatrix("A.txt");
        double [][]AFir=A;
        int n=A.length;
        double [][]At=transpon(A);
        double [][]D=getE(n);
        A=mult(At, A);
        double [][] b=readMatrix("b.txt");
        b=transpon(b);
        double [][]b1=b;
        b=mult(At, b);
        double [][]S=new double [n][n];
        for (int i=0; i<n; i++){
        	S[i][i]=A[i][i];
        	for (int j=0; j<i; j++)
        	{
        		S[i][i]-=S[j][i]*S[j][i]*D[j][j];

        	}
        	D[i][i]=S[i][i]/Math.abs(S[i][i]);
        	S[i][i]=Math.sqrt(Math.abs(S[i][i]));
       		for (int j=i+1;j<n; j++){
       			S[i][j]=A[i][j];
       			for (int k=0; k<i; k++){

       				S[i][j]-=S[k][i]*D[k][k]*S[k][j];	
  
       			}
       			S[i][j]=S[i][j]/(S[i][i]*D[i][i]);
       		}
        }
        double [][] y=new double[1][n];
        for (int i=0; i<n; i++){
        	y[0][i]=b[i][0];
        	for(int j=0; j<i; j++)
        		y[0][i]-=S[j][i]*y[0][j];
        	y[0][i]=y[0][i]/S[i][i];
        }
        double [][] x=new double[1][n];
        for (int j=n-1; j>=0; j--){
        	x[0][j]=y[0][j];
        	for (int k=j+1; k<n; k++)
        		x[0][j]-=D[k][k]*S[j][k]*x[0][k];
        	x[0][j]=x[0][j]/(D[j][j]*S[j][j]);
        }
        x=transpon(x);
        print(x);
        A=mult(AFir, x);
        for (int i=0; i<n; i++)
        	A[i][0]-=b1[i][0];
        print(A);
        double norma=Math.abs(A[0][0]);
        for (int i=1; i<n; i++)
        	if (Math.abs(A[i][0])>norma)
        		norma=Math.abs(A[i][0]);
        System.out.println(norma);
    }
}