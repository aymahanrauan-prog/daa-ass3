### DAA Assignment3 - Report and Defense (50 points)

All solutions are in `src/`. Every solution was compiled and checked against the examples from the statements, edge cases (single point, no edges, cycles) and 2000 random cross-checks (DFS vs DSU, Kruskal vs Prim, Kosaraju vs Tarjan, DFS vs Kahn). Notation: `V` = vertices, `E` = edges.

---

##### Problem 1 and 2 - Number of Provinces (10 points)

A province is a connected component of an undirected graph given as an adjacency matrix `isConnected`. The answer is the number of connected components.

**Problem 1 - DFS (`Problem01.java`)**
- Keep a `visited[]` array. For each city `i` that is not visited yet, increase the counter and run DFS from `i`, which marks the whole province (all cities reachable from `i`).
- Each DFS call scans the whole row of the matrix, so the total work is `n` rows of `n` cells.
- **Time:** O(n²) (matrix representation). **Space:** O(n) for `visited` and the recursion stack (n ≤ 200, so recursion is safe).

**Problem 2 - DSU (`Problem02.java`)**
- Start with `n` components, each city in its own set. For every pair `(i, j)` with `isConnected[i][j] == 1` call `union(i, j)`. Every successful union (roots differ) decreases the component counter by 1.
- Optimizations: **union by rank** and **path compression (halving)**, which make `find` / `union` amortized O(α(n)) ≈ O(1).
- **Time:** O(n² · α(n)), dominated by reading the matrix. **Space:** O(n).

**Comparison:** both are O(n²) because the input is a matrix. DFS is simpler for a static graph; DSU is better when edges arrive online or when we need to merge sets dynamically.

---

##### Problem 3 and 4 - Min Cost Spanning Tree (10 points)

Points form a complete graph, the weight of edge `(i, j)` is the Manhattan distance. The answer is the weight of the Minimum Spanning Tree (MST) (a tree connecting all points has exactly one simple path between any two points).

**Problem 3 - Kruskal (`Problem03.java`)**
- Build all `n(n-1)/2` edges, sort them by weight, then go through them in increasing order. An edge is taken if it connects two different components (checked with DSU: `find(u) != find(v)`); otherwise it would create a cycle and is skipped. Stop after `n-1` edges are taken.
- Correctness: the cut property - the cheapest edge that crosses between two different components is always safe to add.
- **Time:** O(E log E) with E = O(n²), i.e. O(n² log n). **Space:** O(n²) for the edge list.

**Problem 4 - Prim (`Problem04.java`)**
- Grow one tree starting from point 0. `minDist[v]` is the cheapest known edge from the tree to `v`. At each step pick the vertex outside the tree with the smallest `minDist`, add it to the tree (add `minDist` to the answer), and relax distances of the remaining vertices.
- Since the graph is **dense** (complete), I use the array version without a priority queue, and compute distances on the fly instead of storing edges.
- **Time:** O(n²). **Space:** O(n).

**Comparison:** for a complete graph Prim O(n²) is better than Kruskal O(n² log n) and uses much less memory (measured on n = 1000: Prim about 11 ms, Kruskal about 243 ms, same answer). Kruskal is better for sparse graphs.

---

##### Problem 5 and 6 - Software Dependency Cycles (10 points)

A circular dependency group is a **strongly connected component (SCC)** of the directed graph `a -> b`. The answer is the number of SCCs (a single module without a cycle is its own SCC). Because `n ≤ 10^5`, both solutions use **iterative DFS** with explicit stacks to avoid `StackOverflowError`.

**Problem 5 - Kosaraju (`Problem05.java`)**
1. Run DFS on the original graph and record vertices in order of **finishing time**.
2. Build the **reversed** graph.
3. Process vertices in **decreasing finishing time**; each DFS on the reversed graph from an unvisited vertex visits exactly one SCC, so count one group per such start.
- Why it works: the vertex with the latest finish time lies in a source SCC of the condensation DAG; in the reversed graph this SCC has no outgoing edges to unvisited SCCs, so DFS cannot leave it.
- **Time:** O(V + E) (two passes). **Space:** O(V + E) (graph + reversed graph).

**Problem 6 - Tarjan (`Problem06.java`)**
- One DFS pass. Each vertex gets a discovery time `disc[u]` and `low[u]` = the smallest discovery time reachable through the DFS subtree plus at most one back edge to a vertex that is still on the SCC stack. Vertices are pushed onto an SCC stack.
- When a vertex finishes with `low[u] == disc[u]`, it is the root of an SCC; pop the stack down to `u` and count one group. After a child finishes, the parent updates `low[parent] = min(low[parent], low[child])`.
- **Time:** O(V + E). **Space:** O(V + E) (only the one graph, no reversed copy).

**Comparison:** same complexity. Tarjan needs one DFS and no reverse graph; Kosaraju is conceptually simpler (two plain DFS). Results of the two were compared on random graphs and a 100 000-vertex cycle.

---

##### Problem 7 and 8 - Course Schedule (10 points)

Courses and prerequisites form a directed graph `b -> a` ("take b before a"). All courses can be finished **iff the graph has no cycle** (it is a DAG).

**Problem 7 - DFS with cycle detection (`Problem07.java`)**
- Three states per vertex: `0` unvisited, `1` on the current DFS path, `2` completely processed. If DFS reaches a vertex with state `1`, there is a back edge, so there is a cycle and the answer is `false`. Vertices with state `2` are skipped (already proven safe).
- **Time:** O(V + E). **Space:** O(V + E); recursion depth ≤ 2000 as per constraints.

**Problem 8 - Kahn's algorithm (`Problem08.java`)**
- Compute the in-degree of every course; put all courses with in-degree 0 into a queue. Repeatedly remove a course, count it, and decrease the in-degree of its neighbours, pushing those that become 0.
- If all `numCourses` were processed, there is no cycle (`true`); otherwise some vertices on a cycle never reach in-degree 0 (`false`).
- **Time:** O(V + E). **Space:** O(V + E).

---

##### Problem 9 and 10 - Course Schedule II (10 points)

Same graph as above, but we must return a valid order (a **topological order**) or an empty array if a cycle exists.

**Problem 9 - DFS with reverse finishing order (`Problem09.java`)**
- DFS with the same three states as in Problem 7. When a vertex is finished (all its dependants processed) it is written into the result array **from the end** (index `n-1` down to `0`). This is exactly the reverse of the finishing order: a vertex finishes after everything reachable from it, so it lands before all of them.
- If a back edge is found, return `new int[0]`.
- **Time:** O(V + E). **Space:** O(V + E).

**Problem 10 - Kahn (`Problem10.java`)**
- Same as Problem 8, but the order in which vertices are removed from the queue is the answer. If fewer than `numCourses` vertices were output, there is a cycle, so return an empty array.
- **Time:** O(V + E). **Space:** O(V + E).

**Comparison:** both give a valid (not necessarily identical) topological order, so any answer satisfying all prerequisites is accepted. Kahn is iterative (no recursion limit) and detects the cycle naturally; DFS-based version is shorter and reuses the cycle-detection idea.

---

### Summary table

| # | Problem | Algorithm | Time | Space |
|---|---------|-----------|------|-------|
| 1 | Number of Provinces | DFS | O(n²) | O(n) |
| 2 | Number of Provinces | DSU (rank + path compression) | O(n² α(n)) | O(n) |
| 3 | Min Cost Connect Points | Kruskal + DSU | O(n² log n) | O(n²) |
| 4 | Min Cost Connect Points | Prim (array, dense) | O(n²) | O(n) |
| 5 | Dependency Cycles | Kosaraju | O(V + E) | O(V + E) |
| 6 | Dependency Cycles | Tarjan | O(V + E) | O(V + E) |
| 7 | Course Schedule | DFS, 3-state cycle detection | O(V + E) | O(V + E) |
| 8 | Course Schedule | Kahn | O(V + E) | O(V + E) |
| 9 | Course Schedule II | DFS, reverse finishing order | O(V + E) | O(V + E) |
| 10 | Course Schedule II | Kahn | O(V + E) | O(V + E) |
