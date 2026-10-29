import java.util.ArrayList;
import java.util.List;

public class Problem06 {
    public int countDependencyGroups(int n, int[][] dependencies) {
        List<Integer>[] g = new List[n];
        for (int i = 0; i < n; i++) g[i] = new ArrayList<>();
        for (int[] d : dependencies) g[d[0]].add(d[1]);

        int[] disc = new int[n];   // discovery time, 0 = unvisited
        int[] low = new int[n];
        boolean[] onStack = new boolean[n];
        int[] sccStack = new int[n];
        int sccTop = 0;
        int[] callStack = new int[n];
        int[] idx = new int[n];
        int timer = 0, groups = 0;

        for (int s = 0; s < n; s++) {
            if (disc[s] != 0) continue;
            int top = 0;
            callStack[top++] = s;
            disc[s] = low[s] = ++timer;
            sccStack[sccTop++] = s;
            onStack[s] = true;

            while (top > 0) {
                int u = callStack[top - 1];
                if (idx[u] < g[u].size()) {
                    int v = g[u].get(idx[u]++);
                    if (disc[v] == 0) {
                        disc[v] = low[v] = ++timer;
                        sccStack[sccTop++] = v;
                        onStack[v] = true;
                        callStack[top++] = v;
                    } else if (onStack[v]) {
                        low[u] = Math.min(low[u], disc[v]);
                    }
                } else {
                    if (low[u] == disc[u]) { // u is the root of an SCC
                        groups++;
                        int w;
                        do {
                            w = sccStack[--sccTop];
                            onStack[w] = false;
                        } while (w != u);
                    }
                    top--;
                    if (top > 0) {
                        int parent = callStack[top - 1];
                        low[parent] = Math.min(low[parent], low[u]);
                    }
                }
            }
        }
        return groups;
    }
}
