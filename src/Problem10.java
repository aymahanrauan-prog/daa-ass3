import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Problem10 {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List<Integer>[] g = new List[numCourses];
        for (int i = 0; i < numCourses; i++) g[i] = new ArrayList<>();
        int[] indegree = new int[numCourses];
        for (int[] p : prerequisites) {
            g[p[1]].add(p[0]);
            indegree[p[0]]++;
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) queue.add(i);
        }

        int[] order = new int[numCourses];
        int cnt = 0;
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order[cnt++] = u;
            for (int v : g[u]) {
                if (--indegree[v] == 0) queue.add(v);
            }
        }
        return cnt == numCourses ? order : new int[0];
    }
}
