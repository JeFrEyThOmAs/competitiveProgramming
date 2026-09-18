# Philaland Coin — Problem Solving Notes

## Problem

Given the maximum price `N` of an item, we need to find the **minimum number of different coin denominations** required so that we can purchase any amount from `1` to `N`.

For example:

- If `N = 5`, denominations `{1, 2, 3}` are enough.
- If `N = 10`, denominations `{1, 2, 3, 4}` are enough.

The goal is to find the minimum number of denominations.

---

## My Thought Process

I first tried to understand the pattern instead of immediately looking for a formula.

I wrote down how many denominations are needed for different maximum prices:

```text
1  → {1}
2  → {1, 2}
3  → {1, 2}
4  → {1, 2, 3}
5  → {1, 2, 3}
6  → {1, 2, 3}
7  → {1, 2, 3, 4}
8  → {1, 2, 3, 4}
9  → {1, 2, 3, 4}
10 → {1, 2, 3, 4}
```

Then I noticed that every time we add one more denomination, the maximum amount we can cover increases according to:

```text
1
1 + 2
1 + 2 + 3
1 + 2 + 3 + 4
...
```

So the important sequence becomes:

```text
1
3
6
10
15
21
...
```

These are triangular numbers.

---

## The Mathematical Observation

If we have `x` denominations, the maximum value we can cover is:

```text
1 + 2 + 3 + ... + x
```

Using the sum of the first `x` natural numbers:

\[
1 + 2 + 3 + \dots + x = \frac{x(x+1)}{2}
\]

Therefore, I need to find the **smallest `x`** for which:

\[
\frac{x(x+1)}{2} \geq N
\]

That became the core of my solution.

---

## Checking the Example

### N = 5

```text
x = 1 → 1
x = 2 → 3
x = 3 → 6
```

Since:

```text
6 >= 5
```

the answer is:

```text
3
```

### N = 10

```text
x = 1 → 1
x = 2 → 3
x = 3 → 6
x = 4 → 10
```

Since:

```text
10 >= 10
```

the answer is:

```text
4
```

---

## Visualizing the Pattern

I also plotted the triangular-number function in Desmos:

\[
y = \frac{x(x+1)}{2}
\]

The graph helped me visualize how the maximum covered value grows as the number of denominations increases.

The important points are:

```text
x = 1 → y = 1
x = 2 → y = 3
x = 3 → y = 6
x = 4 → y = 10
x = 5 → y = 15
```

---

## My Java Solution

```java
import java.util.*;

public class PhilalandCoin {

    static int function(int x) {
        return (x * (x + 1)) / 2;
    }

    static int PhilalandCoin(int maxPrice) {
        int x = 1;

        while (function(x) < maxPrice) {
            x++;
        }

        return x;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int T = sc.nextInt();

        while (T-- > 0) {

            int maxPrice = sc.nextInt();

            System.out.println(PhilalandCoin(maxPrice));
        }

        sc.close();
    }
}
```

---

## How the Code Works

The helper function:

```java
static int function(int x) {
    return (x * (x + 1)) / 2;
}
```

calculates the triangular number:

\[
\frac{x(x+1)}{2}
\]

Then:

```java
while (function(x) < maxPrice) {
    x++;
}
```

keeps increasing the number of denominations until the maximum value that can be covered becomes at least `maxPrice`.

The first `x` that satisfies the condition is the answer.

---

## Complexity

The loop increases `x` until:

\[
\frac{x(x+1)}{2} \geq N
\]

which means `x` grows approximately as `√N`.

Therefore:

- **Time:** `O(√N)`
- **Space:** `O(1)`

---

## Final Takeaway

What initially looked like a coin-distribution problem turned into a simple mathematical pattern.

The main progression was:

```text
Understand the examples
        ↓
Write down the pattern
        ↓
Notice 1 + 2 + 3 + ... + x
        ↓
Recognize triangular numbers
        ↓
Use x(x+1)/2
        ↓
Find the smallest x ≥ N
        ↓
Implement with a simple loop
```

The biggest takeaway for me was:

> **Sometimes the key to solving a problem isn't writing more code — it's finding the pattern before writing the code.**
