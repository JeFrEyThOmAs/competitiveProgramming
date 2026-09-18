public class KingPlacement{
    static void fillKnights(int[][] chessboard, int[][] knights) {
        int[] dr = {-2, -2, 2, 2, -1, -1, 1, 1};
        int[] dc = {-1, 1, -1, 1, -2, 2, -2, 2};
        for (int[] coordinates : knights) {
            int x = coordinates[0];
            int y = coordinates[1];
            chessboard[x][y] = 1;
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
    static void fillRooks(int[][] chessboard, int[][] pieces, int[][] rooks) {

        for (int[] coordinates : rooks) {

            int x = coordinates[0];
            int y = coordinates[1];

            for (int i = x - 1; i >= 0; i--) {
                chessboard[i][y] = 1;

                if (pieces[i][y] != 0) {
                    break;
                }
            }

            for (int i = x + 1; i < chessboard.length; i++) {
                chessboard[i][y] = 1;

                if (pieces[i][y] != 0) {
                    break;
                }
            }

            // LEFT
            for (int j = y - 1; j >= 0; j--) {
                chessboard[x][j] = 1;

                if (pieces[x][j] != 0) {
                    break;
                }
            }

            // RIGHT
            for (int j = y + 1; j < chessboard.length; j++) {
                chessboard[x][j] = 1;

                if (pieces[x][j] != 0) {
                    break;
                }
            }
        }
    }
    static void fillBishops(int[][] chessboard, int[][] pieces, int[][] bishops) {

        for (int[] coordinates : bishops) {

            int x = coordinates[0];
            int y = coordinates[1];

            // UP-LEFT
            for (int i = x - 1, j = y - 1;
                 i >= 0 && j >= 0;
                 i--, j--) {

                chessboard[i][j] = 1;

                if (pieces[i][j] != 0) {
                    break;
                }
            }

            // UP-RIGHT
            for (int i = x - 1, j = y + 1;
                 i >= 0 && j < chessboard.length;
                 i--, j++) {

                chessboard[i][j] = 1;

                if (pieces[i][j] != 0) {
                    break;
                }
            }

            // DOWN-LEFT
            for (int i = x + 1, j = y - 1;
                 i < chessboard.length && j >= 0;
                 i++, j--) {

                chessboard[i][j] = 1;

                if (pieces[i][j] != 0) {
                    break;
                }
            }

            // DOWN-RIGHT
            for (int i = x + 1, j = y + 1;
                 i < chessboard.length && j < chessboard.length;
                 i++, j++) {

                chessboard[i][j] = 1;

                if (pieces[i][j] != 0) {
                    break;
                }
            }
        }
    }
    static void fillQueens(int[][] chessboard, int[][] pieces, int[][] queens) {

        for (int[] coordinates : queens) {

            int x = coordinates[0];
            int y = coordinates[1];

            // UP
            for (int i = x - 1; i >= 0; i--) {
                chessboard[i][y] = 1;

                if (pieces[i][y] != 0) {
                    break;
                }
            }

            // DOWN
            for (int i = x + 1; i < chessboard.length; i++) {
                chessboard[i][y] = 1;

                if (pieces[i][y] != 0) {
                    break;
                }
            }

            // LEFT
            for (int j = y - 1; j >= 0; j--) {
                chessboard[x][j] = 1;

                if (pieces[x][j] != 0) {
                    break;
                }
            }

            // RIGHT
            for (int j = y + 1; j < chessboard.length; j++) {
                chessboard[x][j] = 1;

                if (pieces[x][j] != 0) {
                    break;
                }
            }

            // UP-LEFT
            for (int i = x - 1, j = y - 1;
                 i >= 0 && j >= 0;
                 i--, j--) {

                chessboard[i][j] = 1;

                if (pieces[i][j] != 0) {
                    break;
                }
            }

            // UP-RIGHT
            for (int i = x - 1, j = y + 1;
                 i >= 0 && j < chessboard.length;
                 i--, j++) {

                chessboard[i][j] = 1;

                if (pieces[i][j] != 0) {
                    break;
                }
            }

            // DOWN-LEFT
            for (int i = x + 1, j = y - 1;
                 i < chessboard.length && j >= 0;
                 i++, j--) {

                chessboard[i][j] = 1;

                if (pieces[i][j] != 0) {
                    break;
                }
            }

            // DOWN-RIGHT
            for (int i = x + 1, j = y + 1;
                 i < chessboard.length && j < chessboard.length;
                 i++, j++) {

                chessboard[i][j] = 1;

                if (pieces[i][j] != 0) {
                    break;
                }
            }
        }
    }

    static int KingPlacement2(int n, int[][] knights, int[][] rooks, int[][] bishops, int[][] queens) {

        // Board that stores whether a square is attacked
        int[][] chessboard = new int[n][n];
        int[][] pieces = new int[n][n];

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

        fillKnights(chessboard, knights);
        fillRooks(chessboard, pieces, rooks);
        fillBishops(chessboard, pieces, bishops);
        fillQueens(chessboard, pieces, queens);

        int count = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                if (chessboard[i][j] == 0 && pieces[i][j] == 0) {
                    count++;
                }
            }
        }

        return count;
    }
}