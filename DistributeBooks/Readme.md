# Distribute Books — Derangement + Modular Arithmetic

## 1. Problem

A class has `N` students and `N` books.

Initially, student `i` has book `Bi`.

The teacher wants to exchange the books so that **every student receives a different book from the one they originally had**.

In other words, after the exchange:

- Student 1 must NOT get `B1`
- Student 2 must NOT get `B2`
- ...
- Student N must NOT get `BN`

We need to find the **number of possible exchanges**.

The answer must be printed modulo:

```text
1,000,000,007
```

### Constraint

```text
1 <= N <= 1,000,000
```

### Example

For:

```text
N = 4
```

there are `9` valid exchanges.

So:

```text
Input:
4

Output:
9
```

---

# 2. Recognizing the Problem

This is a classic **Derangement** problem.

A derangement is a permutation in which **no element remains in its original position**.

For example, with:

```text
[1, 2, 3, 4]
```

this is a valid derangement:

```text
[2, 1, 4, 3]
```

because nobody received their original book.

But:

```text
[2, 1, 3, 4]
```

is invalid because student 3 still has book 3 and student 4 still has book 4.

So the problem is asking for:

```text
D(N) = number of derangements of N elements
```

---

# 3. Derangement Recurrence

Let:

```text
D[n] = number of derangements of n books
```

Base cases:

```text
D[0] = 1
D[1] = 0
```

Why?

### D[1]

With one student and one book:

```text
[1]
```

There is no way to give the student a different book.

Therefore:

```text
D[1] = 0
```

### D[0]

The empty arrangement is conventionally counted as one valid arrangement:

```text
D[0] = 1
```

This is useful for the recurrence.

---

# 4. Recurrence

The standard derangement recurrence is:

```text
D[n] = (n - 1) * (D[n - 1] + D[n - 2])
```

For example:

```text
D[2]
= 1 * (D[1] + D[0])
= 1 * (0 + 1)
= 1
```

```text
D[3]
= 2 * (D[2] + D[1])
= 2 * (1 + 0)
= 2
```

```text
D[4]
= 3 * (D[3] + D[2])
= 3 * (2 + 1)
= 9
```

Therefore:

```text
D[4] = 9
```

which matches the sample.

---

# 5. Why Do We Need Modulo?

The constraint is:

```text
N <= 1,000,000
```

Derangement numbers grow extremely quickly.

Even relatively small values of `N` produce numbers much larger than Java's:

```text
int
long
```

can store.

The problem therefore asks us to return:

```text
D[N] % 1,000,000,007
```

Let:

```java
MOD = 1_000_000_007L;
```

We can apply modulo during the recurrence instead of allowing the numbers to become enormous.

---

# 6. Modular Arithmetic Basics Used Here

For this problem we mainly need:

## Addition

```java
(a + b) % MOD
```

## Multiplication

```java
(a * b) % MOD
```

Because multiplication can become large, use `long`.

For example:

```java
long result = (a * b) % MOD;
```

## Subtraction

When subtraction is needed in CP, a common safe pattern is:

```java
(a - b + MOD) % MOD
```

This problem does not need modular subtraction.

## Division

Ordinary division does NOT generally work under modulo.

For example:

```text
a / b
```

cannot simply be implemented as:

```text
(a % MOD) / (b % MOD)
```

For modular division, we generally need a **modular inverse**.

This problem does NOT contain division, so modular inverse is not needed.

---

# 7. Why Can We Take Modulo During the Calculation?

Suppose:

```text
D[n] = (n - 1) * (D[n-1] + D[n-2])
```

We only care about:

```text
D[n] % MOD
```

Modulo works nicely with addition and multiplication.

Therefore, instead of calculating the enormous exact value, we can calculate:

```text
D[n] =
((n - 1) * ((D[n-1] + D[n-2]) % MOD)) % MOD
```

This keeps all values manageable.

---

# 8. Memoization / Top-Down DP

The recurrence can first be implemented using memoization.

```java
class Solution {

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
```

### Complexity

Time:

```text
O(N)
```

Each state `D[n]` is calculated only once.

Space:

```text
O(N)
```

for the DP array.

There is also recursion-stack space.

---

# 9. Why Memoization Is Not Ideal for N = 1,000,000

The constraint allows:

```text
N = 1,000,000
```

A recursive call chain can become roughly:

```text
solve(1000000)
    ↓
solve(999999)
    ↓
solve(999998)
    ↓
...
```

Java's call stack cannot safely handle such a deep recursion.

This can result in:

```text
StackOverflowError
```

Therefore, although memoization is useful for understanding the recurrence, we should use **bottom-up DP** for the actual submission.

---

# 10. Bottom-Up DP

We know:

```text
D[0] = 1
D[1] = 0
```

Then calculate:

```text
D[2]
D[3]
D[4]
...
D[N]
```

The recurrence only needs the previous two values:

```text
D[n-1]
D[n-2]
```

Therefore, we don't even need an entire `dp[]` array.

We can keep only two variables.

```java
static final long MOD = 1_000_000_007L;

long prev2 = 1; // D[0]
long prev1 = 0; // D[1]

for (int i = 2; i <= n; i++) {

    long curr =
        ((i - 1L) * ((prev1 + prev2) % MOD)) % MOD;

    prev2 = prev1;
    prev1 = curr;
}

System.out.println(prev1);
```

---

# 11. Why `i - 1L`?

`i` is an `int`.

We want the multiplication to happen using `long`.

```java
(i - 1L)
```

makes the expression a `long`.

This avoids integer overflow during the multiplication.

For example:

```java
long curr = ((i - 1L) * value) % MOD;
```

is safer than doing the multiplication entirely as `int`.

---

# 12. Space Optimization

The normal bottom-up DP would look like:

```java
long[] dp = new long[n + 1];

dp[0] = 1;
dp[1] = 0;

for (int i = 2; i <= n; i++) {
    dp[i] = ((i - 1L) *
            ((dp[i - 1] + dp[i - 2]) % MOD))
            % MOD;
}
```

But notice:

```text
dp[i]
```

only depends on:

```text
dp[i - 1]
dp[i - 2]
```

So older values are never needed.

We can replace the entire array with:

```text
prev2 = D[i-2]
prev1 = D[i-1]
curr  = D[i]
```

and then shift:

```text
prev2 = prev1
prev1 = curr
```

This reduces space from:

```text
O(N)
```

to:

```text
O(1)
```

---

# 13. Final Optimized Solution

```java
class Solution {

    static final long MOD = 1_000_000_007L;

    public long derangement(int n) {

        if (n == 0) return 1;
        if (n == 1) return 0;

        long prev2 = 1; // D[0]
        long prev1 = 0; // D[1]

        for (int i = 2; i <= n; i++) {

            long curr =
                ((i - 1L) *
                ((prev1 + prev2) % MOD))
                % MOD;

            prev2 = prev1;
            prev1 = curr;
        }

        return prev1;
    }
}
```

If the platform expects `int`, return:

```java
return (int) prev1;
```

provided the required output range is modulo `1e9+7`.

---

# 14. Example: N = 4

Start:

```text
D[0] = 1
D[1] = 0
```

### i = 2

```text
D[2]
= (2 - 1)(D[1] + D[0])
= 1(0 + 1)
= 1
```

Now:

```text
prev2 = 0
prev1 = 1
```

### i = 3

```text
D[3]
= (3 - 1)(D[2] + D[1])
= 2(1 + 0)
= 2
```

Now:

```text
prev2 = 1
prev1 = 2
```

### i = 4

```text
D[4]
= (4 - 1)(D[3] + D[2])
= 3(2 + 1)
= 9
```

Answer:

```text
9
```

---

# 15. CP Pattern Learned From This Problem

This problem teaches several reusable patterns.

## Pattern 1 — Recognize Derangement

If a problem says:

> Rearrange/permutate objects so that nobody gets their original object.

Think:

```text
DERANGEMENT
```

and remember:

```text
D[n] = (n-1)(D[n-1] + D[n-2])
```

---

## Pattern 2 — Large Counting Answer

If the problem asks:

> Return the number of ways modulo `1e9+7`.

Think:

```text
Huge combinatorial answer
        ↓
Find recurrence/formula
        ↓
Perform calculation modulo MOD
```

---

## Pattern 3 — Modular Addition/Multiplication

Safe operations:

```java
(a + b) % MOD
(a * b) % MOD
```

Use `long` for multiplication.

---

## Pattern 4 — Division Is Special

If your recurrence/formula contains division:

```text
a / b
```

stop and think:

```text
Do I need modular inverse?
```

Do NOT blindly do integer division.

---

## Pattern 5 — DP Space Optimization

If:

```text
dp[i]
```

only depends on:

```text
dp[i-1]
dp[i-2]
```

you can usually reduce:

```text
O(N) space
```

to:

```text
O(1) space
```

using two variables.

---

# 16. Final Takeaway

For this problem, the complete thought process is:

```text
Problem
  ↓
Rearrange N books
  ↓
Nobody gets original book
  ↓
Derangement
  ↓
D[n] = (n-1)(D[n-1] + D[n-2])
  ↓
N can be 1,000,000
  ↓
Answer becomes enormous
  ↓
Calculate modulo 1,000,000,007
  ↓
Use long for multiplication
  ↓
Only previous 2 DP states are needed
  ↓
O(N) time
O(1) space
```

### Complexity

```text
Time  : O(N)
Space : O(1)
```

This is the version you would want for the given constraint.
