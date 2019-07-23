import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.math.*;
import java.lang.*;

public class CF1144F implements Runnable {
    boolean vis[];
    ArrayList<Integer>[] ad;
    int fd[];
    public void run() {
        InputReader c = new InputReader(System.in);
        PrintWriter w = new PrintWriter(System.out);
        int n=c.nextInt();
        vis=new boolean[n+1];
        HashMap<pair,Integer> hm=new HashMap<>();
        fd=new int[n+1];
        int m=c.nextInt();
        ad=new ArrayList[n+1];
        for(int i=0;i<n+1;i++){
            ad[i]=new ArrayList();
        }
        int[][] ed=new int[m][2];
        for(int i=0;i<m;i++){
            int a=c.nextInt()-1;
            int b=c.nextInt()-1;
            ed[i][0]=a;
            ed[i][1]=b;
            hm.put(new pair(a,b),i);
            ad[a].add(b);
            ad[b].add(a);
        }
        Bipartite b=new Bipartite(ad,n);
        if(b.isBipartite()){
            w.println("YES");
            for(int i=0;i<m;i++){
                w.print(b.colorArr[ed[i][0]]);
            }
        }
        else{
            w.println("NO");
        }
        w.close();
    }
    class pair{
        int a,b;
        pair(int x,int y){
            a=x;
            b=y;
        }
    }
    void dsf(int h,int j){
        for(int i=0;i<ad[h].size();i++){
            int gg=ad[h].get(i);
            if(vis[gg]) continue;
            if(j==0)
                fd[gg] = i;
            else
                fd[i] = gg;
            dsf(gg,j==1?0:1);
        }
    }

    class Bipartite {
        int V;
        int src=1;
        int colorArr[];
        ArrayList<Integer>[] ad;
        Bipartite(ArrayList[] a,int h){
            V=h;
            ad=a;
        }
        boolean isBipartite() {
            colorArr = new int[V];
            for (int i = 0; i < V; ++i)
                colorArr[i] = -1;

            // Assign first color to source
            colorArr[src] = 1;
            LinkedList<Integer> q = new LinkedList<Integer>();
            q.add(src);

            while (q.size() != 0) {
                // Dequeue a vertex from queue
                int u = q.poll();

                for (int v = 0; v < ad[u].size(); ++v) {
                    if (colorArr[ad[u].get(v)] == -1) {
                        colorArr[ad[u].get(v)] = 1 - colorArr[u];
                        q.add(ad[u].get(v));
                    }
                    else if (colorArr[ad[u].get(v)] == colorArr[u])
                        return false;
                }
            }
            return true;
        }
    }

        class DisJointSet {
        int[] table, count;
        int size, mn;
        boolean add = false;

        DisJointSet(int size) {
            this.table = new int[size];
            this.count = new int[size];
            this.size = size;
            for (int i = 0; i < size; i++) {
                this.table[i] = i;
                this.count[i] = 1;
            }
        }

        boolean isSame(int x, int y) {
            return root(x) == root(y);
        }

        int root(int node) {
            return table[node] == node ? node : root(table[node]);
        }

        void union(int x, int y) {
            x = root(x);
            y = root(y);
            if (x != y) {
                this.size--;
                if (count[x] < count[y]) {
                    x = x ^ y;
                    y = x ^ y;
                    x = x ^ y;
                }
                table[y] = x;
                count[x] += count[y];
            }
        }
    }
    int[][] packU(int n, int[] from, int[] to) {
        int[][] g = new int[n][];
        int[] p = new int[n];
        for (int f : from)
            p[f]++;
        for (int t : to)
            p[t]++;
        for (int i = 0; i < n; i++)
            g[i] = new int[p[i]];
        for (int i = 0; i < from.length; i++) {
            g[from[i]][--p[from[i]]] = to[i];
            g[to[i]][--p[to[i]]] = from[i];
        }
        return g;
    }
    static long gcd(long a, long b)
    {
        if (b == 0)
            return a;
        return gcd(b, a % b);
    }
    class Graph{
        private final int v;
        private List<List<Integer>> adj;
        Graph(int v){
            this.v = v;
            adj = new ArrayList<>(v);
            for(int i=0;i<v;i++){
                adj.add(new LinkedList<>());
            }
        }
        private void addEdge(int a,int b){
            adj.get(a).add(b);
        }
        private boolean isCyclic()
        {
            boolean[] visited = new boolean[v];
            boolean[] recStack = new boolean[v];
            for (int i = 0; i < v; i++)
                if (isCyclicUtil(i, visited, recStack))
                    return true;

            return false;
        }
        private boolean isCyclicUtil(int i, boolean[] visited, boolean[] recStack)
        {
            if (recStack[i])
                return true;
            if (visited[i])
                return false;
            visited[i] = true;
            recStack[i] = true;
            List<Integer> children = adj.get(i);
            for (Integer c: children)
                if (isCyclicUtil(c, visited, recStack))
                    return true;
            recStack[i] = false;
            return false;
        }
    }
    public static class DJSet {
        public int[] upper;

        public DJSet(int n) {
            upper = new int[n];
            Arrays.fill(upper, -1);
        }

        public int root(int x) {
            return upper[x] < 0 ? x : (upper[x] = root(upper[x]));
        }

        public boolean equiv(int x, int y) {
            return root(x) == root(y);
        }

        public boolean union(int x, int y) {
            x = root(x);
            y = root(y);
            if (x != y) {
                if (upper[y] < upper[x]) {
                    int d = x;
                    x = y;
                    y = d;
                }
                upper[x] += upper[y];
                upper[y] = x;
            }
            return x == y;
        }
    }
    public static int[] radixSort(int[] f)
    {
        int[] to = new int[f.length];
        {
            int[] b = new int[65537];
            for(int i = 0;i < f.length;i++)b[1+(f[i]&0xffff)]++;
            for(int i = 1;i <= 65536;i++)b[i]+=b[i-1];
            for(int i = 0;i < f.length;i++)to[b[f[i]&0xffff]++] = f[i];
            int[] d = f; f = to;to = d;
        }
        {
            int[] b = new int[65537];
            for(int i = 0;i < f.length;i++)b[1+(f[i]>>>16)]++;
            for(int i = 1;i <= 65536;i++)b[i]+=b[i-1];
            for(int i = 0;i < f.length;i++)to[b[f[i]>>>16]++] = f[i];
            int[] d = f; f = to;to = d;
        }
        return f;
    }
    static class InputReader {
        private InputStream stream;
        private byte[] buf = new byte[1024];
        private int curChar;
        private int numChars;
        private SpaceCharFilter filter;
        private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        public InputReader(InputStream stream) {
            this.stream = stream;
        }

        public int read() {
            if (numChars==-1)
                throw new InputMismatchException();

            if (curChar >= numChars) {
                curChar = 0;
                try {
                    numChars = stream.read(buf);
                }
                catch (IOException e) {
                    throw new InputMismatchException();
                }

                if(numChars <= 0)
                    return -1;
            }
            return buf[curChar++];
        }

        public String nextLine() {
            String str = "";
            try {
                str = br.readLine();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
        public int nextInt() {
            int c = read();

            while(isSpaceChar(c))
                c = read();

            int sgn = 1;

            if (c == '-') {
                sgn = -1;
                c = read();
            }

            int res = 0;
            do {
                if(c<'0'||c>'9')
                    throw new InputMismatchException();
                res *= 10;
                res += c - '0';
                c = read();
            }
            while (!isSpaceChar(c));

            return res * sgn;
        }

        public long nextLong() {
            int c = read();
            while (isSpaceChar(c))
                c = read();
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            long res = 0;

            do {
                if (c < '0' || c > '9')
                    throw new InputMismatchException();
                res *= 10;
                res += c - '0';
                c = read();
            }
            while (!isSpaceChar(c));
            return res * sgn;
        }

        public double nextDouble() {
            int c = read();
            while (isSpaceChar(c))
                c = read();
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            double res = 0;
            while (!isSpaceChar(c) && c != '.') {
                if (c == 'e' || c == 'E')
                    return res * Math.pow(10, nextInt());
                if (c < '0' || c > '9')
                    throw new InputMismatchException();
                res *= 10;
                res += c - '0';
                c = read();
            }
            if (c == '.') {
                c = read();
                double m = 1;
                while (!isSpaceChar(c)) {
                    if (c == 'e' || c == 'E')
                        return res * Math.pow(10, nextInt());
                    if (c < '0' || c > '9')
                        throw new InputMismatchException();
                    m /= 10;
                    res += (c - '0') * m;
                    c = read();
                }
            }
            return res * sgn;
        }

        public String readString() {
            int c = read();
            while (isSpaceChar(c))
                c = read();
            StringBuilder res = new StringBuilder();
            do {
                res.appendCodePoint(c);
                c = read();
            }
            while (!isSpaceChar(c));

            return res.toString();
        }

        public boolean isSpaceChar(int c) {
            if (filter != null)
                return filter.isSpaceChar(c);
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public String next() {
            return readString();
        }

        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);
        }
    }
    public static void main(String args[]) throws Exception {
        new Thread(null, new CF1144F(),"CF1144F",1<<26).start();
    }
}