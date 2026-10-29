import java.util.ArrayList;
import java.util.List;

public class Problem05 {
    public int countDependencyGroups(int n, int[][] dependencies) {
        List<Integer>[] g = new List[n];
        List<Integer>[] rg = new List[n];
        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<>();
            rg[i] = new ArrayList<>();
        }
        for (int[] d : dependencies) {
            g[d[0]].add(d[1]);
            rg[d[1]].add(d[0]);
        }

        boolean[] visited = new boolean[n];
        int[] order = new int[n];
        int cnt = 0;
        int[] stack = new int[n];
        int[] idx = new int[n];
        for (int s = 0; s < n; s++) {
            if (visited[s]) continue;
            int top = 0;
            stack[top++] = s;
            visited[s] = true;
            while (top > 0) {
                int u = stack[top - 1];
                if (idx[u] < g[u].size()) {
                    int v = g[u].get(idx[u]++);
                    if (!visited[v]) {
                        visited[v] = true;
                        stack[top++] = v;
                    }
                } else {
                    order[cnt++] = u; // u finished
                    top--;
                }
            }
        }

        boolean[] seen = new boolean[n];
        int groups = 0;
        for (int i = n - 1; i >= 0; i--) {
            int s = order[i];
            if (seen[s]) continue;
            groups++;
            int top = 0;
            stack[top++] = s;
            seen[s] = true;
            while (top > 0) {
                int u = stack[--top];
                for (int v : rg[u]) {
                    if (!seen[v]) {
                        seen[v] = true;
                        stack[top++] = v;
                    }
                }
            }
        }
        return groups;
    }
}
