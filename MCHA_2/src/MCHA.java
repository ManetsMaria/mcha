public class MCHA {
    static double f(double x){
        return Math.exp(x);
    }
    static double k(double x){
        return Math.sin(x)*Math.sin(x) + 1;
    }
    static double dk(double x){
        return Math.sin(x)+Math.cos(x);
    }
    static double q(double x){
        return Math.cos(x);
    }
    static final double a = 0;
    static final double b = 1;

    static int N = 10;

    static double h = (b - a)/(1.0*N);
    static int n = 5;

    static class Rits{
        static double commF(int i, int k, double x){
            return k(x)*getDFi(i, x)*getDFi(k, x)+q(x)*getFi(i, x)*getFi(k, x);
        }
        static double commF2(int k, double x){
            return commF(0, k, x) - f(x)*getFi(k, x);
        }
        static double[][] A;
        static void Rits(){
            A = new double[n][n + 1];
            for (int i = 0; i < n; i++){
                for (int k = 0; k < n; k++){
                    double sum = 0.0;
                    for (int j = 0; j < N; j++){
                        sum += ((h)/(6.0))*(commF(i, k, j*h)+4*commF(i, k, (j*h+(j+1)*h)/2)+commF(i, k, (j+1)*h));
                    }
                    A[k][i] = sum;
                }
            }
            for (int i = 0; i < n; i++){
                double sum = 0;
                for (int j = 0; j < N; j++){
                    sum -= ((h)/(6.0))*(commF2(i, j*h)+4*commF2(i, (j*h+(j+1)*h)/2)+commF2(i, (j+1)*h));
                }
                A[i][n] = sum;
            }
            Gauss();
            double sum = 0.0;
            for (int i = 0; i < N; i++){
                sum = getFi(0, i*h);
                for (int j = 0; j < n; j++){
                    sum += A[j][n]*getFi(j,i*h);
                }
                System.out.println(sum);
            }
        }
        public static void Gauss() {
            for (int i = 0;i < n; i++) {
                double q = A[i][i];
                for(int h = i;h < n + 1; h++) {
                    A[i][h] /=q;
                }
                for (int k = i + 1; k < n; k++) {
                    double c = -A[k][i] / A[i][i];
                    for (int j = i; j < n + 1; j++) {
                        A[k][j] += c * A[i][j];
                    }
                }
            }
            for (int i = n - 1; i >= 0;i--) {
                for (int k = i - 1; k >= 0; k--) {
                    double c = -A[k][i] / A[i][i];
                    A[k][n] += c* A[i][n];
                    A[k][i] += c* A[i][i];
                }
            }
        }
        static double getFi(int i, double x){
            if (i == 0)
                return Math.sin(1)*Math.sin(1);
            return Math.pow(x - 1, 2) * Math.pow(x, (double) i + 1);
        }
        static double getDFi(int i, double x){
            if (i == 0)
                return 0;
            return 2 * (x - 1) * Math.pow(x, (double) i) + (double) (i+1) * Math.pow(x - 1, 2) *Math.pow(x, (double) (i));
        }
    }
    static class Setki {
        static double alpha[];
        static double[] beta;
        static double[][] C = new double[N + 1][N + 2];
        static double[] F = new double[N+1];

        static double realK(double x){
            return dk(x)/k(x);
        }
        static double realQ(double x){
            return (q(x)/k(x))*(-1.0);
        }
        static double realF(double x){
            return (f(x)/k(x))*(-1.0);
        }
        static void setki() {
            alpha = new double[3];
            alpha[0] = -1;
            alpha[1] = k(0);
            alpha[2] = -1;
            beta = new double[3];
            beta[0] = -1;
            beta[1] = k(1) * (-1.0);
            beta[2] = -1;
            double x = a + h;
            for (int i = 0; i <= N; i++)
                for (int j = 0; j <= N; j++)
                    C[i][j] = 0;
            C[0][0] = h * h * (realQ(a) - realK(a) * alpha[0] / alpha[1]) / 2 + h * alpha[0] / alpha[1] - 1;
            C[0][1] = 1;
            F[0] = h * h * (realF(a) - realK(a) * alpha[2] / alpha[1]) / 2 + h * alpha[2] / alpha[1];
            for (int i = 1; i < N; i++, x += h) {
                C[i][i - 1] = 1 - h * realK(x) / 2;
                C[i][i] = h * h * realQ(x) - 2;
                C[i][i + 1] = 1 + h * realK(x) / 2;
                F[i] = h * h * realF(x);
            }
            C[N][N - 1] = 1;
            C[N][N] = h * h * (realQ(b) - realK(b) * beta[0] / beta[1]) / 2 - h * beta[0] / beta[1] - 1;
            F[N] = h * h * (realF(b) - realK(b) * beta[2] / beta[1]) / 2 - h * beta[2] / beta[1];
            for (int i = 0; i <N+1; i++)
                C[i][N+1] = F[i];
            progonka();
            for (int i = 0; i < N+1; i++)
                System.out.println(C[i][N+1]);
        }

        public static void progonka() {
            for (int i = 0; i < N + 1; i++) {
                double q = C[i][i];
                for (int h = i; h < N + 2; h++) {
                    C[i][h] /= q;
                }
                for (int k = i + 1; k < N + 1; k++) {
                    double c = -C[k][i] / C[i][i];
                    for (int j = i; j < N + 2; j++) {
                        C[k][j] += c * C[i][j];
                    }
                }
            }
            for (int i = N; i >= 0; i--) {
                for (int k = i - 1; k >= 0; k--) {
                    double c = -C[k][i] / C[i][i];
                    C[k][N + 1] += c * C[i][N + 1];
                    C[k][i] += c * C[i][i];
                }
            }
        }
    }
    static public void main(String[] args){
        System.out.println("Rits");
        Rits.Rits();
        System.out.println("Setki");
        Setki.setki();
    }
}
