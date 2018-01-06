import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class MCHA{
    static final double a = 1.0;
    static final double b = 1.5;
    static final int N = 11;
    static final double x0 = 1;
    static final double y0 = 0;
    static final double eps = Math.pow(10, -5);
    static double[] yExc;
    static double[] x;
    static double h;
    static double f(double x, double y){
        return (y + Math.pow(x*x+y*y,0.5))/x;
    }
    static double fdifx(double x, double y){
        return (Math.pow(x*x+y*y, -0.5) - (Math.pow(x*x+y*y,0.5)+y)/(x*x));
    }
    static double fdify(double x, double y){
        return (1.0/x + (y/(x*Math.pow(x*x+y*y,0.5))));
    }
    static double y(double x){
        return (x*x-1)/2;
    }
    static protected class Teylor{
        static double y(double x0, double y0,double x){
          return y0 + (x - x0) * f(x0, y0) + Math.pow(x - x0, 2) * (fdifx(x0, y0) + f(x0, y0) * fdify(x0, y0)) / 2;
        }
        static double[] teylor(){
           double [] yTeylor = new double[N];
           yTeylor[0] = y0;
           for (int i = 1; i <= N/2; i++){
               yTeylor[i] = y(x0, y0, x[i]);
           }
           for (int i = N/2 + 1; i < N; i++){
               yTeylor[i] = y(x[i - 1], yTeylor[i - 1], x[i]);
           }
           return yTeylor;
        }
    }
    static protected class Eiler{
        static final double eps = Math.pow(h, 2);
        static double[] EilerExplicit (){
            double[] yExp = new double[N];
            yExp[0] = y0;
            for (int i = 0; i < N - 1; i++){
                yExp[i+1] = yExp[i] + h*f(x[i], yExp[i]);
            }
            return yExp;
        }
        static double[] EilerImplicit (){
            double [] yImp = new double[N];
            yImp[0] = y0;
            double yprev, ynext, numerator, denominator, xnext;
            for (int i = 0; i < N - 1; i++){
                ynext = yImp[i];
                xnext = x[i + 1];
                do{
                    yprev = ynext;
                    numerator = yprev - yImp[i] - h * f(xnext, yprev);
                    denominator = 1 - h * fdify(xnext, yprev);
                    ynext = yprev - numerator / denominator;
                } while (Math.abs(ynext - yprev) > eps);
                yImp[i + 1] = ynext;
            }
            return yImp;
        }
    }
    static protected class ConsecutivePrecisionRaise{
        static double consecutiveStep(double x, double y){
            return (y + h*f(x+h/2, y + (h/2)*f(x, y)));
        }
        static double[] consecutive(){
            double [] yCons = new double[N];
            yCons[0] = y0;
            for (int i = 1; i < N; i++){
                yCons[i] = consecutiveStep(x[i - 1], yCons[i - 1]);
            }
            return yCons;
        }
    }
    static protected class RungeKutta{
        static double fi0(double x, double y){
            return h*f(x, y);
        }
        static double fi1(double x, double y){
            return h*f(x + h/2, y + fi0(x, y)/2);
        }
        static double fi2(double x, double y){
            return h*f(x+h, y - fi0(x, y) + 2*fi1(x, y));
        }
        static double newY(double x, double y){
            return y + (fi0(x, y) + 4*fi1(x, y) + fi2(x, y))/6;
        }
        static double[] rk(){
            double [] yRK = new double[N];
            yRK[0] = y0;
            for (int i = 1; i < N; i++){
                yRK[i] = newY(x[i - 1], yRK[i - 1]);
            }
            return yRK;
        }
    }
    static protected class Adams{
        static final double eps = Math.pow(h, 2);
        static double [] extr(){
            double [] yavY = new double[N];
            double [] yRK = RungeKutta.rk();
            for (int i = 0; i < 3; i++){
                yavY[i] = yRK[i];
            }
            for (int i = 2; i < N - 1; i++){
                yavY[i + 1] = yavY[i] + h*((23.0/12)*f(x[i],yavY[i]) - (4.0/3)*f(x[i - 1], yavY[i - 1]) + (5.0/12)*f(x[i - 2], yavY[i - 2]));
            }
            return yavY;
        }
        static double[] inter() {
            double[] nY = RungeKutta.rk();
            double prev;
            for (int i = 0; i < N - 1; i++) {
               // nY[i + 1] = nY[i];
                do {
                    prev = nY[i + 1];
                    nY[i + 1] = nY[i] + (h / 2) * (f(x[i + 1], nY[i + 1]) + f(x[i], nY[i]));

                } while (Math.abs(nY[i + 1] - prev) > eps);

            }
            return nY;
        }
    }
    static protected class Systems{
        static double [] y;
        static double [] z;
        static final double x0 = 0;
        static final double a = 0;
        static final double b = 0.5;
        static final double y0 = 0;
        static final double z0 = 0;
        static final double h = (b - a)/N;
        static final double eps = Math.pow(h, 4);
        static double fy(double x, double y, double z){
            return z - Math.cos(x);
        }
        static double fz(double x, double y, double z){
            return y + Math.sin(x);
        }
        static double FZ(double x){
            return 0;
        }
        static double FY(double x){
            return (-1)*Math.sin(x);
        }
        static void exc(){
            y = new double[N];
            z = new double[N];
            double x = x0;
            for (int i = 0; i < N; i++, x += h) {
                y[i] = FY(x);
                z[i] = FZ(x);
            }
            System.out.println(DoubleStream.of(y).boxed().collect(Collectors.toList()));
            System.out.println(DoubleStream.of(z).boxed().collect(Collectors.toList()));
        }
        static void AdamsIntSystem () {
            y = new double[N];
            z = new double[N];
            y[0] = y0;
            z[0] = z0;
            double x = x0, yprev, zprev;
            for (int i = 0; i < N - 1; i++, x += h) {
                y[i+1] = y[i];
                z[i+1] = z[i];
                do {
                    yprev = y[i+1];
                    zprev = z[i+1];
                    y[i+1] = y[i] + 0.5 * h * (fy(x + h, y[i+1], z[i+1]) + fy(x, y[i], z[i]));
                    z[i+1] = z[i] + 0.5 * h * (fz(x + h, y[i+1], z[i+1]) + fz(x, y[i], z[i]));
                } while (Math.abs(yprev - y[i+1]) > eps || Math.abs(zprev - z[i+1]) > eps);

            }
            System.out.println(DoubleStream.of(y).boxed().collect(Collectors.toList()));
            System.out.println(DoubleStream.of(z).boxed().collect(Collectors.toList()));
        }
        static double fi0_y(double x, double y, double z){
            return h*fy(x, y, z);
        }
        static double fi0_z(double x, double y, double z){
            return h*fz(x, y, z);
        }
        static double fi1_y(double x, double y, double z){
            return h*fy(x + h/2, y, z + fi0_z(x, y, z)/2);
        }
        static double fi1_z(double x, double y, double z){
            return h*fz(x + h/2, y + fi0_y(x, y, z)/2, z);
        }
        static double fi2_y(double x, double y, double z){
            return h*fy(x+h, y, z - fi0_z(x, y,z) + 2*fi1_y(x, y,z));
        }
        static double fi2_z(double x, double y, double z){
            return h*fz(x+h, y - fi0_y(x, y,z) + 2*fi1_y(x, y,z), z);
        }
        static double newY(double x, double y, double z){
            return y + (fi0_y(x, y, z) + 4*fi1_y(x, y, z) + fi2_y(x, y, z))/6;
        }
        static double newZ(double x, double y, double z){
            return z + (fi0_z(x, y, z) + 4*fi1_z(x, y, z) + fi2_z(x, y, z))/6;
        }
        static void rk(){
            y = new double[N];
            z = new double[N];
            y[0] = y0;
            z[0] = z0;
            for (int i = 1; i < N; i++){
                y[i] = newY(x0 + (i - 1)*h, y[i - 1], z[i - 1]);
                z[i] = newZ(x0 + (i - 1)*h, y[i - 1], z[i - 1]);
            }
            System.out.println(DoubleStream.of(y).boxed().collect(Collectors.toList()));
            System.out.println(DoubleStream.of(z).boxed().collect(Collectors.toList()));
        }

    }
    static void inizialize(){
        h = (b - a)/(N - 1);
        x = new double[N];
        x[0] = x0;
        for (int i = 1; i < N; i++){
            x[i] = x[i - 1] + h;
        }
        yExc = new double[N];
        yExc[0] = y0;
        for (int i = 1; i < N; i++){
            yExc[i] = y(x[i]);
        }
    }
    static public void main(String [] args){
        inizialize();

        System.out.println("Tochki: "+DoubleStream.of(x).boxed().collect(Collectors.toList()));
        System.out.println();
        System.out.println("Tochny resultat: "+DoubleStream.of(yExc).boxed().collect(Collectors.toList()));
        System.out.println();
        System.out.println("Neylor: "+DoubleStream.of(Teylor.teylor()).boxed().collect(Collectors.toList()));
        System.out.println();
        System.out.println("Eiler yavny: "+DoubleStream.of(Eiler.EilerExplicit()).boxed().collect(Collectors.toList()));
        System.out.println();
        System.out.println("Eiler neyavny: "+DoubleStream.of(Eiler.EilerImplicit()).boxed().collect(Collectors.toList()));
        System.out.println();
        System.out.println("Povyshenie porydka tochnosty: "+DoubleStream.of(ConsecutivePrecisionRaise.consecutive()).boxed().collect(Collectors.toList()));
        System.out.println();
        System.out.println("Runge-Kutta: "+DoubleStream.of(RungeKutta.rk()).boxed().collect(Collectors.toList()));
        System.out.println();
        System.out.println("Adams yavny: "+DoubleStream.of(Adams.extr()).boxed().collect(Collectors.toList()));
        System.out.println();
        System.out.println("Adams neyvny: "+DoubleStream.of(Adams.inter()).boxed().collect(Collectors.toList()));
        System.out.println();
        System.out.print("Tochnay systema: ");Systems.exc();
        System.out.println();
        System.out.print("Adams: ");Systems.AdamsIntSystem();
        System.out.println();
        System.out.print("Runge-Kutta: ");Systems.rk();
    }
}