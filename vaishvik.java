
class GFG { 

	static int countDivisibleSubseq(String str, int n) { 
		int len = str.length(); 

		int dp[][] = new int[len][n]; 

		dp[0][(str.charAt(0) - '0') % n]++; 

		for (int i = 1; i < len; i++) { 
			// start a new subsequence with index i 
			dp[i][(str.charAt(i) - '0') % n]++; 

			for (int j = 0; j < n; j++) { 
				// exclude i'th character from all the 
				// current subsequences of string [0...i-1] 
				dp[i][j] += dp[i - 1][j]; 

				// include i'th character in all the current 
				// subsequences of string [0...i-1] 
				dp[i][(j * 10 + (str.charAt(i) - '0')) % n] += dp[i - 1][j]; 
			} 
		} 

		return dp[len - 1][0]; 
	} 

// Driver code 
	public static void main(String[] args) { 
		String str = "1234"; 
		int n = 4; 
		System.out.print(countDivisibleSubseq(str, n)); 
	} 
} 
