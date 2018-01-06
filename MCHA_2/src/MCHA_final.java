public class MCHA_final {

    public static class WorkWithMatrix {
        Double value[][];
        int size;

        WorkWithMatrix(int n){
            size = n;
            value=new Double[size][size];
            for (int i=0; i<size; i++){
                for (int j=0; j<size; j++)
                    value[i][j]=0.0;
            }
        }
        public void solveShooing(Double vector[]){
            for (int i=0; i<size; i++){
                for (int j=0; j<size; j++){
                    if (Math.abs(j-i)>1)
                        value[i][j]=0.0;
                }
            }
            Double alpha[]=new Double[size], beta[]=new Double[size];
            beta[size-1]=vector[size-1]/value[size-1][size-1];
            alpha[size-1]=-value[size-1][size-2]/value[size-1][size-1];
            for (int i=size-2; i>0; i--){
                alpha[i]=-value[i][i-1]/(value[i][i]-alpha[i+1]*(-value[i][i+1]));
                beta[i]=(vector[i]+(-value[i][i+1])*beta[i+1])/(value[i][i]-alpha[i+1]*(-value[i][i+1]));
            }
            alpha[0]=0.0;
            beta[0]=(vector[0]+(-value[0][1])*beta[1])/(value[0][0]-alpha[1]*(-value[0][1]));
            vector[0]=beta[0];
            for (int i=1; i<size;i++)
                vector[i]=alpha[i]*vector[i-1]+beta[i];
        }

        public void show(){
            for (int i=0; i<size; i++) {
                for (int j = 0; j < size; j++) {
                    System.out.print(" " + value[i][j]);
                }
                System.out.println();
            }
        }
    }

    static class DifferenceScheme {

        static double leftBorder = 0.0, rightBorder = 1.0;
        static double step = 0.05;
        static int amountOfPoints = (int) ((rightBorder - leftBorder) / step) + 1;

        static double y[][] = new double[amountOfPoints][amountOfPoints];

        static double sigma = 0.5;

        static double f(int i, int j) {
            double x = leftBorder + step * i, t = leftBorder + step * j;
            return Math.pow(x, 2) - 2 * t;
        }

        static void count() {
            WorkWithMatrix matr;
            Double vect[] = new Double[amountOfPoints];

            for (int j = 1; j < amountOfPoints; j++) {
                matr = new WorkWithMatrix(amountOfPoints);

                matr.value[0][0] = step-1.0;
                matr.value[0][1] = 1.0;
                vect[0] = 0.0;

                for (int i = 1; i < amountOfPoints-1; i++) {
                    matr.value[i][i]=-2* sigma -step;
                    matr.value[i][i+1] =matr.value[i][i-1] = sigma;
                    vect[i] = y[i + 1][j-1] * (sigma - 1)
                            + y[i][j-1] * (2-2* sigma -step)
                            + y[i-1][j-1] * (sigma -1)
                            - f(i, j) * Math.pow(step, 2);
                }

                matr.value[amountOfPoints-1][amountOfPoints-2] = -1.0;
                matr.value[amountOfPoints-1][amountOfPoints-1] = 1.0;
                vect[amountOfPoints-1] = 2*(double)j*Math.pow(step,2);

                matr.solveShooing(vect);
                for (int i=0; i<amountOfPoints; i++)
                    y[i][j] = vect[i];
            }

            /*for (int i=0; i<amountOfPoints; i++){
                for (int j=0; j<amountOfPoints; j++)
                    System.out.print(y[i][j]+" ");
                System.out.print("\n");
            }*/
            for (int i = amountOfPoints- 5; i<amountOfPoints; i++){
                for (int j = 0; j < amountOfPoints; j++){
                    System.out.print(String.format("%.3f",y[i][j])+" ");
                }
                System.out.println();
            }

        }

    }

    public static void main(String[] args) {
        DifferenceScheme.count();
    }
}