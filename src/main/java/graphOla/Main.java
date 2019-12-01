package graphOla;
public class Main {
    public static void main(String[] args) throws MaxIterationException {
        double e = 0.2;
        double a=1000;
//        TrajectoryXY xy = new TrajectoryXY(100,0.2,a,0.01,"fixedpoint");
//        System.out.println(xy.getX().size());
//        System.out.println(xy.getX());
//        System.out.println(xy.getY());
        double m=2.1363;
        Solver solver = new Solver((eks) -> m + e * Math.sin(eks) - eks);
        double w1 = solver.bisection(m-1,m+1,0.001 ,50);
        System.out.println(w1);
        double m2=3.1416;
        Solver solver2 = new Solver((eks) -> m2 + e * Math.sin(eks) - eks);
        double w2 = solver2.bisection(m2-1,m2+1,0.001 ,50);
        System.out.println(w2);


    }


}

