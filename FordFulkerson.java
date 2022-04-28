import java.util.*;

public class FordFulkerson{
    public static int capacity[][];
    public static int flow[][];
    public static int path[]; //
    public static boolean visited[];
    public static LinkedList<Integer> graph[];

    public static boolean dfs(int start) {
        if (start == Sink) {
            return true;
        }

        visited[start] = true;
        LinkedList<Integer> nexts = graph[start];
        for (int next: nexts) {
            if ( !visited[next] && capacity[start][next] - flow[start][next] > 0) {
                path[next] = start;

                if (dfs(next)) { // 경로를 끝까지 찾으면 탈출, 아니면, 끝까지 찾기 재시도
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean bfs() {
        Arrays.fill(path, -1);
        Queue<Integer> q = new LinkedList<Integer>();
        q.add(Source);

        while (!q.isEmpty()) {
            int from = q.poll();
            LinkedList<Integer> nexts = graph[from];

            for (int next: nexts) {
                if ( path[next] == -1 && (capacity[from][next] - flow[from][next]) > 0 ) {
                    path[next] = from;
                    q.add(next);

                    if (next == Sink) {
                        break;
                    }
                }
            }
        }

        if (path[Sink] == -1) {
            return false;
        }

        return true;
    }

    // O(VE^2)
    public static int EdmondsKarp() {
        int total = 0;
        while (bfs()) {
            // flow값 찾기
            int flowNum = Integer.MAX_VALUE;
            for(int i = Sink; i != Source; i = path[i]) {
                int from = path[i];
                int to = i;
                flowNum = Math.min(flowNum, (capacity[from][to]) - flow[from][to]);
            }

            // flow흘려 보내기, 역방향도 반드시!!!!
            for(int i = Sink; i != Source; i = path[i]) {
                int from = path[i];
                int to = i;

                flow[from][to] += flowNum;
                flow[to][from] -= flowNum;
            }

            total += flowNum;
        }
        return total;
    }

    // O((V+E)F)
    public static int FordFulkerson() {
        int total = 0;
        while (dfs(Source)) { // dfs로 경로 찾기(증가경로), 경로가 더이상 없으면 종료임.
            // 찾은 경로에서 차단 간선 찾기 min (capacity[u][v] - flow[u][v])
            // 결국 의미는 경로에서 흘릴수 있는 최대의 유량(flow)을 찾기
            int flowNum = Integer.MAX_VALUE;
            for(int i = Sink; i != Source; i = path[i]) {
                int from = path[i];
                int to = i;
                flowNum = Math.min(flowNum, (capacity[from][to]) - flow[from][to]);
            }
            // 찾은 경로에 유량을 흘려보내기, 역방향도 반드시 진행한다.
            for(int i = Sink; i != Source; i = path[i]) {
                int from = path[i];
                int to = i;

                flow[from][to] += flowNum;
                flow[to][from] -= flowNum;
            }

            total += flowNum;

            // 찾은 경로를 초기화해서 dfs로 경로 찾기를 Source > Sink 까지 다시 할 수 있게 함.
            Arrays.fill(path, -1);
            Arrays.fill(visited, false);
        }
        return total;
    }
}
