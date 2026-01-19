import java.util.List;

public class TSPCrossoverOperatorOrder implements ITSPCrossoverOperator{
    @Override
    public int[] getCrossover(int[] p1, int[] p2) {
        int[] k1 = new int[p1.length];
        for (int i = 0; i < k1.length; i++){
            k1[i] = -1;
        }

        int i = (int) (Math.random()*p1.length);
        int j = (int) (Math.random()*(p1.length-i))+i;

        System.out.println("i: "+i + " j: "+j);

        for(int k = i; k <= j; k++){
            k1[k] = p1[k];
        }


        int k = 0;
        if (j+1 == k1.length) {
            k = 0;
        } else {
            k = j+1;
        }

        while(k != i) {
            // w recorre al padre 2
            // k recorre al hijo
            int w = k;

            boolean found = false;
            while(!found){
                if (!exists(p2[w], k1)){
                    k1[k] = p2[w];
                    found = true;
                }
                if(w == p1.length-1){
                    w=0;
                } else {
                    w++;
                }
            }

            if(k == p1.length-1){
                k=0;
            } else {
                k++;
            }
        }

        return k1;
    }

    private boolean exists(int v, int[] a){
        for(int i : a){
            if(i == v){
                return true;
            }
        }
        return false;
    }
}
