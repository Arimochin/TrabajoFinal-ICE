import java.util.List;

public class TSPCrossoverOperatorPMX implements ITSPCrossoverOperator, IPrintable{
    @Override
    public int[] getCrossover(int[] p1, int[] p2) {
        int[] k1 = new int[p1.length];
        for (int i = 0; i < k1.length; i++){
            k1[i] = -1;
        }

        int i = (int) (Math.random()*p1.length);
        int j = (int) (Math.random()*(p1.length-i))+i;



        for(int k = i; k <= j; k++){
            k1[k] = p1[k];
        }

        for (int itp2 = i; itp2 <= j; itp2++){
            int aux = itp2;
            int valueToAllocate = p2[aux];
            if (!TSPHelper.exists(valueToAllocate, k1)){
                boolean done = false;
                int p2Pos = -1;
                while(!done){
                    int valueInP1 = p1[aux];
                    p2Pos = TSPHelper.searchIndex(valueInP1,p2);
                    if(k1[p2Pos] == -1){
                        k1[p2Pos] = valueToAllocate;
                        done = true;
                    } else {
                        aux = p2Pos;
                    }
                }
            }
        }

        for (int m = 0; m < k1.length; m++){
            if (k1[m] == -1) {
                k1[m] = p2[m];
            }
        }

        return k1;
    }

    @Override
    public String getStringValue() {
        return "CrossOver Operator: PMX";
    }
}
