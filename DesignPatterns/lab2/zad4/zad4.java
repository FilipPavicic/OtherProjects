package lab2.zad4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class zad4 {
    static interface IGeneratorNumber{
        public List<Integer> generator();
    }
    static class SlijednoGenerator implements IGeneratorNumber {
        int max, min, step;

        public SlijednoGenerator(int min, int max, int step){
            this.min = min;
            this.max = max;
            this.step = step;
        }
        
        @Override
        public List<Integer> generator() {
            List<Integer> lista  = new ArrayList<>();
            for(int i = min; i< max; i += step){
                lista.add(i);
            }
            return lista;
        }
    }
    static class SlucajniGenerator implements IGeneratorNumber {
        int n;
        int mean;
        int std;
        SlucajniGenerator(int mean, int std, int n) {
            this.n = n;
            this.mean = mean;
            this.std = std;
        }
        @Override
        public List<Integer> generator() {
            List<Integer> lista  = new ArrayList<>();
            for(int i = 0; i < n; i++) {
                lista.add((int) Math.round(new Random().nextGaussian() * std + mean));
            }  
            return lista;
        }
    }
    static class FibonaccijevGenerator implements IGeneratorNumber {
        int n;
        private static final Double PHI = (1 + Math.sqrt(5.0)) / 2.0;
        private static final Double _1_PHI = (1 - Math.sqrt(5.0)) / 2.0;
        FibonaccijevGenerator(int n) {
            this.n = n;

        }
        @Override
        public List<Integer> generator() {
            List<Integer> lista  = new ArrayList<>();
            for(int i = 1; i < n + 1; i++) {
                lista.add((int) ((Math.pow(PHI, i * 1.0) - Math.pow(_1_PHI, i *1.0)) / Math.sqrt(5.0)));
            }  
            return lista;
        }
    }
    static interface I_p_Percentil{
        public default int calculate(List<Integer> lista,int p){
            if(p < 0 || p > 100) throw new IllegalArgumentException("Percentil mora biti između 1 i 100");
            return Internalcalculate(lista, p);
        }
        public int Internalcalculate(List<Integer> lista,int p);
    }
    static class NearestRank implements I_p_Percentil{

        @Override
        public int Internalcalculate(List<Integer> lista, int p) {
            Collections.sort(lista);
            int position = (int) Math.round(p * lista.size() / 100.0 + 0.5) - 1;
            return lista.get(position);

        }

    }
    static class Interpolation implements I_p_Percentil{

        @Override
        public int Internalcalculate(List<Integer> lista, int p) {
            Integer i_max = null;
            Collections.sort(lista);
            Integer i_min = null;
            int N = lista.size();
            for (int i = 0; i < N; i++){
                if(p < p_value(i+1, N)){
                    i_max = i;
                    break;
                }
                i_min = i;

            }
            if(i_max == null) return lista.get(N);
            if(i_min == null) return lista.get(0);
            return (int) Math.round(lista.get(i_min) + N * (p - p_value(i_min + 1, N))*(lista.get(i_max)-lista.get(i_min))/100);
        }
        public double p_value(int i, int N){
            return (100.0*((i) - 0.5)/N);
        }
    }
    static class DistributionTester{
        List<Integer> list;
        I_p_Percentil p;
        DistributionTester(IGeneratorNumber generator, I_p_Percentil percentil){
            list = generator.generator();
            p = percentil;
        }
        
        public Integer getPercentil(Integer percentil){
            return p.calculate(list, percentil);
        }
        public void printEvery10thPercentil(){
            for(int i = 10; i < 100; i+=10){
                System.out.println(i+ " percentil: " +getPercentil(i));
            }
        }

    }

    public static void main(String[] args) {
        new DistributionTester(new SlucajniGenerator(100, 10, 100_000), new Interpolation()).printEvery10thPercentil();
    }
}
