public class TSPMutationOperatorInsertion implements ITSPMutationOperator, IPrintable{
    @Override
    public int[] getMutation(int[] k) {
        int i = (int) (Math.random()*k.length);
        int j = (int) (Math.random()*(k.length-i))+i;
        //System.out.println("i: "+i + " j: "+j);

        int ogValueJ = k[j];

        if (j - i >= 2) {
            for (int m = j; m >= i + 2; m--) {
                k[m] = k[m - 1];
            }
            k[i + 1] = ogValueJ;
        }

        //if ( i != k.length) {

        //}

        return k;
    }

    @Override
    public String getStringValue() {
        return "Mutation Operator: Insertion";
    }
}
