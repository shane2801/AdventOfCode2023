import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

public class Test {
    final static int[][] dirs = new int[][] {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // down right up left
//    final static int mapSize = 13; // test input
    final static int mapSize = 141;

    static class Node {
        int count, x, y, consec;
        int[] dir;
        public Node(int _count, int _x, int _y, int[] _dir, int _consec) {
            count = _count; x = _x; y = _y; dir = _dir; consec = _consec;
        }
    }

    static class Key {
        private int x, y;
        private int[] dir;
        public Key(int _x, int _y, int[] _dir) {
            x = _x; y = _y; dir = _dir;
        }
        @Override
        public boolean equals(Object o) {
            Key k = (Key)o;
            return x == k.x && y == k.y && dir == k.dir;
        }
        @Override
        public int hashCode() {
            return (x * mapSize + y) * 9 + (dir[0] + 1) * 3 + (dir[1] + 1);
        }
    }

    public static void main(String[] args) throws IOException {
        File f = new File("puzzles/puzzle17.txt");
        Scanner s = new Scanner(f);
        int out = Integer.MAX_VALUE, out2 = Integer.MAX_VALUE;
        final int[][] map = new int[mapSize][mapSize];
        for (int line = 0; s.hasNextLine(); line++) {
            String l = s.nextLine();
            for (int i = 0; i < l.length(); i++) map[i][line] = l.charAt(i) - '0';
        }
        s.close();

        // part 1
        final ArrayList<Node> open = new ArrayList<Node>();
        open.add(new Node(0, 0, 0, dirs[1], 0));
        final HashMap<Key, int[]> closed = new HashMap<Key, int[]>(); // Key -> {count, consec}
        closed.put(new Key(0, 0, dirs[1]), new int[] {0, 0});
        while (open.size() > 0) {
            int minIdx = 0;
            int minScore = Integer.MAX_VALUE;
            for (int i = open.size()-1; i >= 0; i--) {
                Node n = open.get(i);
                int score = n.count;
                if (score < minScore) {
                    minScore = score;
                    minIdx = i;
                }
            }
            Node n = open.remove(minIdx);
            int count = n.count, x = n.x, y = n.y, consec = n.consec;
            int[] dir = n.dir;
            dirLoop:
            for (int[] i : dirs) {
                int X = x + i[0], Y = y + i[1];
                if (X < 0 || Y < 0 || X >= mapSize || Y >= mapSize || dir[0] * i[0] == -1 || dir[1] * i[1] == -1 || consec == 3 && dir == i) continue;
                int newCount = count + map[X][Y];
                if (newCount >= out) continue;
                int newConsec = dir == i ? consec + 1 : 1;
                Key k = new Key(X, Y, i);
                if (X == mapSize-1 && Y == mapSize-1) {
                    if (newCount < out) out = newCount;
                    closed.put(k, new int[] {newCount, newConsec});
                } else {
                    int[] closedNode = closed.getOrDefault(k, null);
                    if (closedNode != null && closedNode[0] <= newCount && closedNode[1] <= newConsec) continue;
                    for (int j = open.size() - 1; j >= 0; j--) {
                        Node openNode = open.get(j);
                        if (openNode.x == X && openNode.y == Y && openNode.count <= newCount && openNode.consec <= newConsec && openNode.dir == i) continue dirLoop;
                    }
                    open.add(new Node(newCount, X, Y, i, newConsec));
                }
            }
            closed.put(new Key(x, y, dir), new int[] {count, consec});
        }
        System.out.println(out);

        // part 2
        open.clear();
        open.add(new Node(0, 0, 0, dirs[1], 0));
        closed.clear();
        closed.put(new Key(0, 0, dirs[1]), new int[] {0, 0});
        while (open.size() > 0) {
            int minIdx = 0;
            int minScore = Integer.MAX_VALUE;
            for (int i = open.size()-1; i >= 0; i--) {
                Node n = open.get(i);
                int score = n.count;
                if (score < minScore) {
                    minScore = score;
                    minIdx = i;
                }
            }
            Node n = open.remove(minIdx);
            int count = n.count, x = n.x, y = n.y, consec = n.consec;
            int[] dir = n.dir;
            dirLoop:
            for (int[] i : dirs) {
                boolean turn = i != dir || consec < 4;
                int X, Y, newCount, newConsec;
                if (!turn) {
                    X = x + i[0]; Y = y + i[1];
                    if (X < 0 || Y < 0 || X >= mapSize || Y >= mapSize || consec == 10) continue;
                    newCount = count + map[X][Y];
                    if (newCount >= out2) continue;
                    newConsec = consec + 1;
                } else {
                    if (consec >= 4 && (dir[0] == i[0] || dir[1] == i[1])) continue;
                    X = x; Y = y;
                    newCount = count;
                    newConsec = 0;
                    for (int j = 0; j < 4; j++) {
                        X += i[0]; Y += i[1];
                        newConsec++;
                        if (X < 0 || Y < 0 || X >= mapSize || Y >= mapSize) continue dirLoop;
                        newCount += map[X][Y];
                        if (newCount >= out2) continue dirLoop;
                    }
                }
                Key k = new Key(X, Y, i);
                if (X == mapSize-1 && Y == mapSize-1) {
                    if (newCount < out2) out2 = newCount;
                    closed.put(k, new int[] {newCount, newConsec});
                } else {
                    int[] closedNode = closed.getOrDefault(k, null);
                    if (closedNode != null && closedNode[0] <= newCount && closedNode[1] <= newConsec) continue;
                    for (int j = open.size() - 1; j >= 0; j--) {
                        Node openNode = open.get(j);
                        if (openNode.x == X && openNode.y == Y && openNode.count <= newCount && openNode.consec <= newConsec && openNode.dir == i) continue dirLoop;
                    }
                    open.add(new Node(newCount, X, Y, i, newConsec));
                }
            }
            closed.put(new Key(x, y, dir), new int[] {count, consec});
        }
        System.out.println(out2);
    }
}