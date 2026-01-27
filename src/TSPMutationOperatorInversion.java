public class TSPMutationOperatorInversion implements ITSPMutationOperator{
    @Override
    public int[] getMutation(int[] k) {
        int i = (int) (Math.random()*k.length);
        int j = (int) (Math.random()*(k.length-i))+i;
       // System.out.println("i: "+i + " j: "+j);

        int aux = 0;
        while(i < (i+j)/2){
            aux = k[i];
            k[i] = k[j];
            k[j] = aux;
            i++;
            j--;
        }

        return k;
    }
}
