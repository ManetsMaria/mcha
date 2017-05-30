#include <iostream>
#include<algorithm>
#include<utility>
#include <cmath>
#include <fstream>
using namespace std;
ifstream in("A.txt");
ifstream vin("b.txt");
ofstream out("rezult.txt");
void multiply(double **A, double **B, int N, int i1, int j2) //для умножения двумерных массивов
{
    double **rez = new double *[i1];

    for (int i = 0; i < i1; i++)
        rez[i] = new double [j2];
    for(int i = 0; i < i1; i++)
    	 for(int j = 0; j < j2; j++)
    	 {
    	  rez[i][j] = 0;
          for(int k = 0; k < N; k++)
           rez[i][j] += (A[i][k] * B[k][j]);
          }
    for (int i = 0; i < i1; i++)
        for (int j = 0; j < j2; j++)
            A[i][j] = rez[i][j];
}
void getA(double **A) //считываем расширеную матрицу
{
    for (int i = 0; i < 5; i++)
        for (int j = 0; j < 5; j++)
        {
            in >> A[i][j];
        }
    for (int i=0; i<5; i++)
        vin>>A[i][5];
}
void copyMatr(double **A, double**B, int iFromA, int jFromA) // копируем матрицу А в матрицу В
{
    for (int i=0; i<iFromA; i++)
        for (int j=0; j<jFromA; j++)
            B[i][j]=A[i][j];
}
void getE(double **E, int N) //генерируем единичную матрицу
{
   for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
        {
            E[i][j] = 0.0;

            if (i == j)
                E[i][j] = 1.0;
        }
}
pair <int, int> maximum(double **A, int k) // находим максимума и его строку и столбец
{
    double m=fabs(A[k][0]);
    pair <int, int> i_j(k,0);
    for (int i=k; i<5; i++)
        for (int j=k; j<5; j++)
            if (fabs(A[i][j])>m)
            {
                i_j.first=i;
                i_j.second=j;
                m=fabs(A[i][j]);
            }
    return i_j;
}
double Gauss(double **A, int N)
{
    double x[N];
    int order[N];
    for (int i=0; i<N; i++)
        order[i]=i;
    double detA=1;
    for(int i=0; i<N; i++) // метод Гаусса с поиском главного элемента по всей матрице
    {
        pair<int, int> place=maximum(A,i);
        int t=order[i];
        order[i]=place.first; // "подписываем" перемещённые столбцы
        order[place.first]=t;
        if (place.first!=i && place.second==i || place.first==i && place.second!=i) //проверяем, меняется ли знак
           detA*=-1;
        for(int k=0; k<N+1; k++) // меняем строки
        {
            double p=A[place.first][k];
            A[place.first][k]=A[i][k];
            A[i][k]=p;
        }
        for(int k=0; k<N; k++) //меняем столбцы
        {
            double p=A[k][place.second];
            A[k][place.second]=A[k][i];
            A[k][i]=p;;
        }
        detA*=A[i][i];
    for (int k=N; k>=0; k--){ // делим строку на найденный элемент
         A[i][k]/=A[i][i];
    }
        for (int k=i+1; k<N; k++) //прямой ход
        {
            double div=A[k][i];
            for (int z=i; z<N+1; z++)
            {
                A[k][z]-=A[i][z]*div;
            }
        }
    }
    for(int i=N-1; i>0; i--) //обратный ход
        for (int k=i-1; k>=0; k--)
            {
                double div=A[k][i];
                for (int z=N; z>=i; z--)
                {
                    A[k][z]-=A[i][z]*div;
                }
            }
    for (int i=0; i<N; i++)
        x[order[i]]=A[i][N];
    for (int i=0; i<N; i++)
        A[i][N]=x[i];
    return detA;
}
void inversion(double **A, int N) //находим обратную матрицу через расширенную матрицу A|E
{
    double **time = new double *[N];

    for (int i = 0; i < N; i++)
        time[i] = new double [N+1];
    copyMatr(A, time, N, N+1);
    double **E = new double *[N];

    for (int i = 0; i < N; i++)
        E[i] = new double [N+1];
    getE(E, N);
    for (int i=0; i<N; i++)
    {
        for (int j=0; j<N; j++){
            A[j][N]=E[j][i];
    }

        Gauss(A, N);
        for (int j=0; j<N; j++)
            E[j][i]=A[j][N];
        copyMatr(time, A, N, N+1);

    }
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            A[i][j] = E[i][j];

    for (int i = 0; i < N; i++)
    {
        delete [] E[i];
        delete [] time[i];
    }

    delete [] E;
    delete [] time;
}

double matrixNorm(double **A, int i1, int j1) // Ищем I норму матрицы (максимум суммы строк)
{
    double r[i1];
    double summa=0;
    for (int i=0; i<i1; i++)
    {
        summa=0;
        for (int j=0; j<j1; j++)
            summa+=fabs(A[i][j]);
        r[i]=summa;
    }
    return max(r[0], r[i1-1]);
}
int main()
{
    int N=5;
    double x[N];
    double **A = new double *[N];
    double **E = new double *[N];
    double **matrix = new double *[N];
    for (int i = 0; i < 5; i++){
        A[i] = new double [N+1];
        E[i] = new double [N+1];
        matrix[i] = new double [N+1];
    }
    getA(A);
    double Sp=0;
    for (int i=0; i<N; i++)
        Sp+=A[i][i];
    copyMatr(A,matrix, N, N);
    inversion(matrix, N);
    for (int i = 0; i < N; i++)
    {
        for (int j = 0; j < N; j++)
            out << matrix[i][j] << "  ";

        out <<endl;
    }
    out<<endl;
    copyMatr(A,E,N, N);
    multiply(matrix, E, N, N, N);
    getE(E,N);
    for (int i = 0; i < N; i++) //находим невязку для полученных результатов обратной матрицы
    {
        for (int j = 0; j < N; j++)
        {
            E[i][j]=matrix[i][j]-E[i][j];
            out << E[i][j] << "  ";
        }

        out <<endl;
    }
    out<<endl;
    out<<matrixNorm(E, N, N)<<endl;
    out<<endl;
    out<<matrixNorm(matrix, N, N)*matrixNorm(A, N, N)<<endl; // число обусловленности матрицы
    out<<endl;
    copyMatr(A, matrix, N, N+1);
    double detA=Gauss(A, N);
    out<<detA<<endl;
    out<<endl;
    for (int i=0; i<N; i++)
        x[i]=A[i][N];
    for (int i=0; i<N; i++)
        out<<x[i]<<" ";
    out<<endl;
    out<<endl;
    double r[N];
    copyMatr(matrix, A, N, N+1);
    for(int i=0; i<N; i++) { //ищем вектор невязки для результатов решения слау
        double s = 0;
        for(int j=0; j<N; j++) {
             s += A[i][j]*x[j];
        r[i]= s-A[i][N];
        }
    }
    for (int i=0; i<N; i++)
        out<<r[i]<<" ";
    out<<endl;
    out<<endl;
    for (int i=0; i<N; i++)
        r[i]=fabs (r[i]);
    double norma_r=max(r[0], r[N-1]);
    out<<norma_r<<endl;
    cout<<Sp;

    for (int i = 0; i < N; i++){ // чистим память
        delete [] matrix[i];
        delete [] A[i];
        delete [] E[i];
    }

    delete [] matrix;
    delete [] A;
    delete [] E;
}

