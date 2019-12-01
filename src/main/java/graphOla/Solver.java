package graphOla;


import java.util.ArrayList;
import java.util.List;

public class Solver {
    private Function function;
    private List<Double> epsilonA;
    private List<Double> wyniki;

    public Solver(Function function) {
        this.function = function;
    }

    public double bisection(double a, double b, double blad, int maxIter) throws MaxIterationException {
        double c = a;
        InitalieListAndAddFirstValues(c);
        int i = 0;
        if (function.function(a) * function.function(b) >= 0) {
            System.out.println("nieprawidłowy przedział");
            return 0;
        }
        while (blad < Math.abs(epsilonA.get(i))) {
            c = (a + b) / 2;
            if (function.function(c) == 0.0) {
                break;
            }
            double f1=getF(c);
            double f2=getF(a);
            if (function.function(c) * function.function(a) < 0)
                b = c;
            else {
                a = c;
            }
            wyniki.add(c);
            addBlad(i);
            i++;
            if (i > maxIter) {
                throw new MaxIterationException("przekroczono max Iteracji");
            }
        }
        return c;
    }



    public double fixedPoint(double x0, double relError,Function f, int maxIter) throws MaxIterationException {
        InitalieListAndAddFirstValues(x0);
        int i = 0;
        while (relError < Math.abs(epsilonA.get(i))) {
            wyniki.add(f.function(wyniki.get(i)));
            if(getF(wyniki.get(i))==0){
                break;
            }
            addBlad(i);
            i++;
            if (i > maxIter) {
                throw new MaxIterationException("przekroczono max Iteracji");
            }
        }

        return wyniki.get(wyniki.size() - 1);

    }

    public double metodaSiecznych(double x0, double x1, double relError, int maxIter) throws MaxIterationException {
        InitalieListAndAddFirstValues(x0);
        wyniki.add(x1);
        epsilonA.add((x1 - x0) / x1);
        int i = 1;
        while (relError < Math.abs(epsilonA.get(i))) {
            double x = wyniki.get(i) - (function.function(wyniki.get(i)) * (wyniki.get(i) - wyniki.get(i - 1)) / (function.function(wyniki.get(i)) - function.function(wyniki.get(i - 1))));
            wyniki.add(x);
            if(getF(wyniki.get(i))==0){
                break;
            }
            addBlad(i);
            i++;
            if (i > maxIter) {
                throw new MaxIterationException("przekroczono max Iteracji");
            }
        }

        return wyniki.get(wyniki.size() - 1);

    }



    public double regularFalsi(double a, double b, double relError, int maxIter) throws MaxIterationException {
        double c = a;
        InitalieListAndAddFirstValues(c);
        int i = 0;
        while (relError < Math.abs(epsilonA.get(i))) {
            if (function.function(a) * function.function(b) >= 0) {
                System.out.println("nieprawidłowy przedział");
                return 0;
            }
            c = b - (function.function(b) * (a - b)) / (function.function(a) - function.function(b));
            if (function.function(c) == 0.0)
                break;
            else if (function.function(c) * function.function(a) < 0)
                b = c;
            else {
                a = c;
            }
            wyniki.add(c);
            addBlad(i);
            i++;
            if (i > maxIter) {

                throw new MaxIterationException("przekroczono max Iteracji");
            }
        }

        return wyniki.get(wyniki.size() - 1);
    }

public double metodaStycznych(double x0, double relError, int maxIter, Function dFunction) throws MaxIterationException {
    InitalieListAndAddFirstValues(x0);
    int i = 0;
    while (relError < Math.abs(epsilonA.get(i))) {
        wyniki.add(wyniki.get(i) - (function.function(wyniki.get(i)) / dFunction.function(wyniki.get(i))));
        if(getF(wyniki.get(i))==0){
            break;
        }
        addBlad(i);
        i++;
        if (i > maxIter) {
            throw new MaxIterationException("przekroczono max Iteracji");
        }
    }

    return wyniki.get(wyniki.size() - 1);

}

    private void addBlad(int i) {

            if (wyniki.get(i + 1) == 0) {
                epsilonA.add(1.);
            } else {
                epsilonA.add((wyniki.get(i + 1) - wyniki.get(i)) / wyniki.get(i + 1));
            }
    }
    private void InitalieListAndAddFirstValues(double c) {
        epsilonA = new ArrayList<>();
        wyniki = new ArrayList<>();
        wyniki.add(c);
        epsilonA.add(1.);

    }
    public double getF(double x){
        return function.function(x);
    }


}
