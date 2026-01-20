import java.util.Arrays;

public class TSPHelper {
    public static boolean exists(int v, int[] a){
        for(int i : a){
            if(i == v){
                return true;
            }
        }
        return false;
    }

    public static int searchIndex(int value, int[] array) {
        int index = -1;
        for(int i = 0; i < array.length; i++){
            if (array[i] == value) {
                index = i;
            }
        }
        return index;
    }
}
