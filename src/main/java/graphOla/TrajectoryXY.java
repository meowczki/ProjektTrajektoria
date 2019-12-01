package graphOla;

import java.util.ArrayList;
import java.util.List;

public class TrajectoryXY {
    private List<Double> x;
    private List<Double> y;
    public List<Double> getX() {
        return x;
    }
    public List<Double> getY() {
        return y;
    }



    public TrajectoryXY(int liczbaPkt, double e, double a, double relError, String method) throws IllegalArgumentException {
        if (e < 0 || e > 1) {
            throw new IllegalArgumentException("nieprawidłowa wartość e");
        }
        x = new ArrayList<>();
        y = new ArrayList<>();

        double m = 0;
        Solver solver = null;
        double w1=0;
        for (int i = 1; i <= liczbaPkt; i++) {

            double finalM = m;
            solver = new Solver((eks) -> finalM + e * Math.sin(eks) - eks);
            try {
                switch (method) {
                    case "stycznych":
                        w1 = solver.metodaStycznych(m - 1, relError, 50, eks -> e * Math.cos(eks) - 1);
                        break;
                    case "bisekcji":
                        w1 = solver.bisection(m - 1, m+1, relError, 50);
                        break;
                    case "fixedpoint":
                        w1 = solver.fixedPoint(m -1,relError, (eks) -> finalM + e * Math.sin(eks),50);
                        break;
                    case "siecznych":
                        w1 = solver.metodaSiecznych(m - 1, m+1, relError, 50);
                        break;
                    case "regularfalsi":
                        w1 = solver.regularFalsi(m - 1, m+1, relError, 50);
                        break;
                }

            } catch (MaxIterationException ex) {
                System.out.println(m);
            }

            x.add(a * Math.cos(w1 - e));
            y.add(a * Math.sqrt(1 - Math.pow(e, 2)) * Math.sin(w1));
            m += 2 * Math.PI / liczbaPkt;
        }
    }
}
