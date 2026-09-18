# King Placement — Problem Solving Notes

## Problem

Given an `N × N` chessboard with some **Knights, Rooks, Bishops and Queens** already placed, find the number of squares where a **King can be placed safely**.

A square is safe if:

1. It is **not under attack** by any of the given pieces.
2. It is **not already occupied** by a piece.

The input gives the coordinates of every piece.

---

## Understanding the Input

For the example:

```text
4

2
0 0
1 1

1
2 2

0

1
3 3
```

This means:

```text
Board size = 4 × 4

Knights:
(0,0)
(1,1)

Rooks:
(2,2)

Bishops:
none

Queen:
(3,3)
```

So the board looks like:

```text
      0   1   2   3

0     N   .   .   .
1     .   N   .   .
2     .   .   R   .
3     .   .   .   Q
```

---

## My Thought Process

I first broke the problem down by asking:

> How can I know whether a particular square is safe?

There are two things I need to know:

```text
Is the square attacked?
        +
Is the square already occupied?
```

This led me to the idea of maintaining **two separate boards**.

---

## Two Boards

### 1. `pieces[][]`

This board stores the actual pieces.

I used:

```text
0 → empty
1 → Knight
2 → Rook
3 → Bishop
4 → Queen
```

For the example:

```text
1 0 0 0
0 1 0 0
0 0 2 0
0 0 0 4
```

This board is important because Rooks, Bishops and Queens can be **blocked by other pieces**.

For example, if a Rook is moving upward and encounters another piece, it cannot attack squares beyond that piece.

---

### 2. `chessboard[][]`

This board stores whether a square is attacked.

```text
0 → not attacked
1 → attacked
```

Every piece's attack function marks the squares it can attack.

At the end, I can simply scan the board.

---

# Breaking the Problem by Piece

Instead of trying to handle every piece in one huge function, I separated the problem into:

```text
fillKnights()
fillRooks()
fillBishops()
fillQueens()
```

Each function is responsible for marking the squares attacked by that particular type of piece.

---

## 1. Knight

A Knight moves in an `L` shape.

Its possible movements can be represented using:

```text
dr = {-2,-2,2,2,-1,-1,1,1}
dc = {-1,1,-1,1,-2,2,-2,2}
```

For every Knight:

```text
newRow = row + dr[k]
newCol = col + dc[k]
```

I check whether the new position is inside the board.

Unlike Rooks, Bishops and Queens, a Knight can jump over pieces, so there is **no need to check for blocking**.

---

## 2. Rook

A Rook moves horizontally and vertically:

```text
UP
DOWN
LEFT
RIGHT
```

So from `(x,y)` I traverse in four directions.

For example:

```java
for (int i = x - 1; i >= 0; i--) {
    chessboard[i][y] = 1;

    if (pieces[i][y] != 0) {
        break;
    }
}
```

The important part is:

```java
if (pieces[i][y] != 0) {
    break;
}
```

This means:

> Mark the square containing the blocking piece as attacked, then stop moving in that direction.

---

## 3. Bishop

A Bishop moves diagonally.

There are four directions:

```text
UP-LEFT
UP-RIGHT
DOWN-LEFT
DOWN-RIGHT
```

For example:

```java
for (int i = x - 1, j = y - 1;
     i >= 0 && j >= 0;
     i--, j--) {

    chessboard[i][j] = 1;

    if (pieces[i][j] != 0) {
        break;
    }
}
```

Again, I stop when another piece blocks the diagonal.

---

## 4. Queen

A Queen combines the movement of a Rook and a Bishop.

Therefore it can move in:

```text
UP
DOWN
LEFT
RIGHT

UP-LEFT
UP-RIGHT
DOWN-LEFT
DOWN-RIGHT
```

So I essentially implemented all **8 directions** for the Queen.

The same blocking rule applies:

```java
chessboard[i][j] = 1;

if (pieces[i][j] != 0) {
    break;
}
```

---

# Why Do I Need the `pieces[][]` Board?

This was an important part of understanding the problem.

Suppose we only had:

```text
chessboard:
0 → safe
1 → attacked
```

Then while moving a Rook, if I encountered:

```text
1
```

I wouldn't know whether that `1` means:

```text
"This square is already attacked"
```

or:

```text
"There is an actual piece here"
```

But for chess movement, I need to know whether an **actual piece** is blocking the path.

That's why I maintain:

```text
pieces[][]     → actual board occupancy
chessboard[][] → attack information
```

---

# Building the `pieces[][]` Board

First I place all the given pieces into the board.

```java
for (int[] coordinates : knights) {
    int x = coordinates[0];
    int y = coordinates[1];
    pieces[x][y] = 1;
}

for (int[] coordinates : rooks) {
    int x = coordinates[0];
    int y = coordinates[1];
    pieces[x][y] = 2;
}

for (int[] coordinates : bishops) {
    int x = coordinates[0];
    int y = coordinates[1];
    pieces[x][y] = 3;
}

for (int[] coordinates : queens) {
    int x = coordinates[0];
    int y = coordinates[1];
    pieces[x][y] = 4;
}
```

After this, the board knows where every actual piece is located.

---

# Marking Attacked Squares

Now I call:

```java
fillKnights(chessboard, knights);
fillRooks(chessboard, pieces, rooks);
fillBishops(chessboard, pieces, bishops);
fillQueens(chessboard, pieces, queens);
```

Each function modifies the same `chessboard`.

If multiple pieces attack the same square, that is completely fine.

For example:

```text
chessboard[i][j] = 1;
```

just remains `1`.

I don't need to know **which** piece attacked it.

I only care whether it is attacked by **at least one** piece.

---

# Final Counting

Once every attack has been marked, I scan the entire board.

A square is safe only when:

```java
chessboard[i][j] == 0
```

AND

```java
pieces[i][j] == 0
```

So the final condition is:

```java
if (chessboard[i][j] == 0 && pieces[i][j] == 0) {
    count++;
}
```

This means:

```text
Not attacked
     +
Actually empty
     =
King can be placed
```

---

# Example

For the example:

```text
Knights:
(0,0), (1,1)

Rook:
(2,2)

Queen:
(3,3)
```

The squares where the King can actually be placed are:

```text
(0,1)
(1,0)
```

Therefore:

```text
Answer = 2
```

---

# Java Implementation

```java
static void fillKnights(int[][] chessboard, int[][] knights) {

    int[] dr = {-2,-2,2,2,-1,-1,1,1};
    int[] dc = {-1,1,-1,1,-2,2,-2,2};

    for (int[] coordinates : knights) {

        int x = coordinates[0];
        int y = coordinates[1];

        for (int k = 0; k < 8; k++) {

            int newX = x + dr[k];
            int newY = y + dc[k];

            if (newX >= 0 && newX < chessboard.length &&
                newY >= 0 && newY < chessboard.length) {

                chessboard[newX][newY] = 1;
            }
        }
    }
}
```

```java
static void fillRooks(int[][] chessboard, int[][] pieces, int[][] rooks) {

    for (int[] coordinates : rooks) {

        int x = coordinates[0];
        int y = coordinates[1];

        // UP
        for (int i = x - 1; i >= 0; i--) {
            chessboard[i][y] = 1;
            if (pieces[i][y] != 0) break;
        }

        // DOWN
        for (int i = x + 1; i < chessboard.length; i++) {
            chessboard[i][y] = 1;
            if (pieces[i][y] != 0) break;
        }

        // LEFT
        for (int j = y - 1; j >= 0; j--) {
            chessboard[x][j] = 1;
            if (pieces[x][j] != 0) break;
        }

        // RIGHT
        for (int j = y + 1; j < chessboard.length; j++) {
            chessboard[x][j] = 1;
            if (pieces[x][j] != 0) break;
        }
    }
}
```

```java
static void fillBishops(int[][] chessboard, int[][] pieces, int[][] bishops) {

    for (int[] coordinates : bishops) {

        int x = coordinates[0];
        int y = coordinates[1];

        // UP-LEFT
        for (int i = x - 1, j = y - 1;
             i >= 0 && j >= 0;
             i--, j--) {

            chessboard[i][j] = 1;
            if (pieces[i][j] != 0) break;
        }

        // UP-RIGHT
        for (int i = x - 1, j = y + 1;
             i >= 0 && j < chessboard.length;
             i--, j++) {

            chessboard[i][j] = 1;
            if (pieces[i][j] != 0) break;
        }

        // DOWN-LEFT
        for (int i = x + 1, j = y - 1;
             i < chessboard.length && j >= 0;
             i++, j--) {

            chessboard[i][j] = 1;
            if (pieces[i][j] != 0) break;
        }

        // DOWN-RIGHT
        for (int i = x + 1, j = y + 1;
             i < chessboard.length && j < chessboard.length;
             i++, j++) {

            chessboard[i][j] = 1;
            if (pieces[i][j] != 0) break;
        }
    }
}
```

```java
static void fillQueens(int[][] chessboard, int[][] pieces, int[][] queens) {

    for (int[] coordinates : queens) {

        int x = coordinates[0];
        int y = coordinates[1];

        // UP
        for (int i = x - 1; i >= 0; i--) {
            chessboard[i][y] = 1;
            if (pieces[i][y] != 0) break;
        }

        // DOWN
        for (int i = x + 1; i < chessboard.length; i++) {
            chessboard[i][y] = 1;
            if (pieces[i][y] != 0) break;
        }

        // LEFT
        for (int j = y - 1; j >= 0; j--) {
            chessboard[x][j] = 1;
            if (pieces[x][j] != 0) break;
        }

        // RIGHT
        for (int j = y + 1; j < chessboard.length; j++) {
            chessboard[x][j] = 1;
            if (pieces[x][j] != 0) break;
        }

        // UP-LEFT
        for (int i = x - 1, j = y - 1;
             i >= 0 && j >= 0;
             i--, j--) {

            chessboard[i][j] = 1;
            if (pieces[i][j] != 0) break;
        }

        // UP-RIGHT
        for (int i = x - 1, j = y + 1;
             i >= 0 && j < chessboard.length;
             i--, j++) {

            chessboard[i][j] = 1;
            if (pieces[i][j] != 0) break;
        }

        // DOWN-LEFT
        for (int i = x + 1, j = y - 1;
             i < chessboard.length && j >= 0;
             i++, j--) {

            chessboard[i][j] = 1;
            if (pieces[i][j] != 0) break;
        }

        // DOWN-RIGHT
        for (int i = x + 1, j = y + 1;
             i < chessboard.length && j < chessboard.length;
             i++, j++) {

            chessboard[i][j] = 1;
            if (pieces[i][j] != 0) break;
        }
    }
}
```

```java
static int KingPlacement2(
        int n,
        int[][] knights,
        int[][] rooks,
        int[][] bishops,
        int[][] queens) {

    int[][] chessboard = new int[n][n];
    int[][] pieces = new int[n][n];

    // Store actual pieces
    for (int[] coordinates : knights) {
        pieces[coordinates[0]][coordinates[1]] = 1;
    }

    for (int[] coordinates : rooks) {
        pieces[coordinates[0]][coordinates[1]] = 2;
    }

    for (int[] coordinates : bishops) {
        pieces[coordinates[0]][coordinates[1]] = 3;
    }

    for (int[] coordinates : queens) {
        pieces[coordinates[0]][coordinates[1]] = 4;
    }

    // Mark attacked squares
    fillKnights(chessboard, knights);
    fillRooks(chessboard, pieces, rooks);
    fillBishops(chessboard, pieces, bishops);
    fillQueens(chessboard, pieces, queens);

    // Count safe and empty squares
    int count = 0;

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {

            if (chessboard[i][j] == 0 &&
                pieces[i][j] == 0) {

                count++;
            }
        }
    }

    return count;
}
```

---

# Main Method

The input is read piece-type by piece-type and then passed to `KingPlacement2()`.

```java
public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);

    int n = sc.nextInt();

    int k = sc.nextInt();
    int[][] knights = new int[k][2];

    for (int i = 0; i < k; i++) {
        knights[i][0] = sc.nextInt();
        knights[i][1] = sc.nextInt();
    }

    int r = sc.nextInt();
    int[][] rooks = new int[r][2];

    for (int i = 0; i < r; i++) {
        rooks[i][0] = sc.nextInt();
        rooks[i][1] = sc.nextInt();
    }

    int b = sc.nextInt();
    int[][] bishops = new int[b][2];

    for (int i = 0; i < b; i++) {
        bishops[i][0] = sc.nextInt();
        bishops[i][1] = sc.nextInt();
    }

    int q = sc.nextInt();
    int[][] queens = new int[q][2];

    for (int i = 0; i < q; i++) {
        queens[i][0] = sc.nextInt();
        queens[i][1] = sc.nextInt();
    }

    System.out.println(
        KingPlacement2(n, knights, rooks, bishops, queens)
    );

    sc.close();
}
```

---

# Overall Approach

The complete reasoning can be summarized as:

```text
Read the board and piece coordinates
              ↓
Create pieces[][]
              ↓
Store where the actual pieces are
              ↓
Create chessboard[][]
              ↓
Mark Knight attacks
              ↓
Mark Rook attacks
              ↓
Mark Bishop attacks
              ↓
Mark Queen attacks
              ↓
Scan every square
              ↓
Is it NOT attacked?
        AND
Is it EMPTY?
              ↓
          count++
```

---

## Complexity

Let:

```text
N = board dimension
P = total number of pieces
```

For each Rook, Bishop or Queen, we may traverse up to `O(N)` squares in each direction, while Knights check only 8 positions.

So the overall approach is roughly:

```text
Time:  O(N² + P × N)
Space: O(N²)
```

Since the board itself is `N × N`, the `O(N²)` space is used by the two boards.

---

## Final Takeaway

The interesting part of this problem wasn't the individual chess movements.

The main problem-solving step was realizing that I could separate the problem into two concepts:

```text
Where are the actual pieces?
              +
Which squares are attacked?
```

Once that was separated, each chess piece could be handled independently, and the final answer became a simple scan of the board.

> **Break a complicated simulation into smaller independent rules, then combine their results.**
