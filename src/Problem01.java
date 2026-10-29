public class Problem01 {
    public int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        boolean[] visited = new boolean[n];
        int provinces = 0;
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                provinces++;
                dfs(isConnected, visited, i);
            }
        }
        return provinces;
    }

    private void dfs(int[][] g, boolean[] visited, int u) {
        visited[u] = true;
        for (int v = 0; v < g.length; v++) {
            if (g[u][v] == 1 && !visited[v]) {
                dfs(g, visited, v);
            }
        }
    }
}
