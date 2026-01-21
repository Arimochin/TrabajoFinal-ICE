public class TSPMutationOperatorInsertion implements ITSPMutationOperator{
    @Override
    public int[] getMutation(int[] k) {
        int i = (int) (Math.random()*k.length);
        int j = (int) (Math.random()*(k.length-i))+i;
        System.out.println("i: "+i + " j: "+j);

        int ogValueJ = k[j];

        for (int m = j; m >= i+2; m--){
            k[m] = k[m-1];
        }

        k[i+1] = ogValueJ;

        return k;
    }
}
