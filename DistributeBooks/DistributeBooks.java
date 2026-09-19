public class DistributeBooks {
    static final long MOD = 1_000_000_007L;
    long[] dp;
    public long solve(int n) {
        if (n == 0) return 1;
        if (n == 1) return 0;

        if (dp[n] != -1) {
            return dp[n];
        }
        dp[n] = ((n - 1L) *
                ((solve(n - 1) + solve(n - 2)) % MOD))
                % MOD;
        return dp[n];
    }
    public long derangement(int n) {
        dp = new long[n + 1];
        Arrays.fill(dp, -1);

        return solve(n);
    }
}