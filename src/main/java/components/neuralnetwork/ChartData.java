package components.neuralnetwork;

import java.util.ArrayList;
import java.util.Arrays;

public class ChartData {
    
    final static double[] percentData = {5.8, 8.7, 10.5, 12.6, 15.4, 15.7, 20.4, 23.5, 25.4, 28.8, 31.1, 32.6, 33.7, 37.9, 40.6, 40.6, 44.5, 44.8, 46.6, 46.5, 51.6, 52.9, 55.1, 56.1, 56.8, 58.8, 60.2, 61.6, 63.6, 64.5, 66.5, 68.0, 68.6, 69.0, 70.9, 72.6, 73.5, 74.2, 75.1, 75.7, 76.6, 76.2, 76.9, 79.1, 79.5, 81.7, 81.8, 83.7, 84.6, 85.9, 86.6, 87.8, 88.7, 89.3};
    final static int[] epochData = {1320, 1432, 1538, 1663, 1783, 1887, 2011, 2143, 2267, 2395, 2528, 2661, 2808, 2999, 3198, 3412, 3598, 3789, 3956, 4189, 4368, 4607, 4819, 5112, 5329, 5592, 5901, 6203, 6569, 6934, 7248, 7821, 8221, 8603, 9084, 9782, 10353, 11159, 11690, 12419, 13084, 13913, 14621, 15337, 16381, 17741, 19130, 21144, 23820, 26982, 30294, 37924, 45132, 50811};

    public static void main(String[] args){
        System.out.println(getPercentData().size());
        System.out.println(getEpochData().size());
    }

    public static ArrayList<Double> getPercentData(){
        ArrayList<Double> data = new ArrayList<>();
        for(int i = 0; i < percentData.length; i++){
            data.add(percentData[i]);
        }
        return data;
    }

    public static ArrayList<Integer> getEpochData(){
        ArrayList<Integer> data = new ArrayList<>();
        for(int i = 0; i < epochData.length; i++){
            data.add(epochData[i]);
        }
        return data;
    }

}
