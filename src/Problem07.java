import java.util.ArrayList;
import java.util.List;

public class Problem07 {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<Integer>[] g = new List[numCourses];
        for (int i = 0; i < numCourses; i++) g[i] = new ArrayList<>();
        for (int[] p : prerequisites) g[p[1]].add(p[0]); // b -> a

        int[] state = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            if (state[i] == 0 && hasCycle(g, state, i)) return false;
        }
        return true;
    }

    private boolean hasCycle(List<Integer>[] g, int[] state, int u) {
        state[u] = 1;
        for (int v : g[u]) {
            if (state[v] == 1) return true;                       // back edge
            if (state[v] == 0 && hasCycle(g, state, v)) return true;
        }
        state[u] = 2;
        return false;
    }
}
