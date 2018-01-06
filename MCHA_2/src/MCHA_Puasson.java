public class MCHA_Puasson {
    static double h = 0.05;
    static int N = (int) (1 / h);
    static double eps = Math.pow(h, 3);
    static double w = 1.5;

    static double f(double x, double y) {
        return Math.abs(x*x-y*y);
    }

    static double psi1(double x) {
        return 1 - x*x;
    }

    static double psi2(double x) {
        return Math.exp(x)*1 - x*x;
    }

    static double psi3_4(double x) {
        return 1 - x*x;
    }

    public static void main(String[] args) {
        {
            double x[] = new double[N + 1];
            double y[] = new double[N + 1];
            double u[][] = new double[N + 1][N + 1];
            for (int i = 0; i < N + 1; i++) {
                x[i] = -1 + i * h;
                y[i] = i * h;
                u[i][N] = u[i][0] = psi3_4(x[i]);
                u[0][i] = psi1(y[i]);
                u[N][i] = psi2(y[i]);
            }

            double temp, max;
            int k = 0;
            do {
                k++;
                max = 0;
                for (int i = 1; i < N; i++) {
                    for (int j = 1; j < N; j++) {
                        temp = 0.25 * (u[i - 1][j] + u[i + 1][j] + u[i][j - 1] + u[i][j + 1] + h * h * f(x[i], y[j]));
                        max = Math.max(Math.abs(u[i][j] - temp), max);
                        u[i][j] = temp + (w - 1) * (u[i][j] - temp);
                    }
                }
            } while(max > eps);

            System.out.println(k);
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    System.out.print(String.format("%.4f",u[i][j]) + " ");
                }
                System.out.println();
            }
        }

    }
}
