import java.util.ArrayList;
import java.util.List;

public class Problem09 {
    private int idx;

    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List<Integer>[] g = new List[numCourses];
        for (int i = 0; i < numCourses; i++) g[i] = new ArrayList<>();
        for (int[] p : prerequisites) g[p[1]].add(p[0]);

        int[] state = new int[numCourses];
        int[] order = new int[numCourses];

        for (int i = 0; i < numCourses; i++) {
            if (state[i] == 0 && !dfs(g, state, order, i)) return new int[0];
        }
        return order;
    }

    private boolean dfs(List<Integer>[] g, int[] state, int[] order, int u) {
        state[u] = 1;
        for (int v : g[u]) {
            if (state[v] == 1) return false;
            if (state[v] == 0 && !dfs(g, state, order, v)) return false;
        }
        state[u] = 2;
        order[idx--] = u;
        return true;
    }
}
