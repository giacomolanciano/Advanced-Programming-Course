public class BinomialTest {
	
	private static long binomial(int n, int k) {
		if (k>n-k)
			k=n-k;
 
		long b=1;
		for (int i=1, m=n; i<=k; i++, m--)
			b=b*m/i;
		return b;
	}
	
	public static void main(String[] args) throws Exception {
		int n = 4;
		int k = 2;
		System.out.println("("+n+" chooses "+k+") = "+ binomial(n,k));
		
	}

}
