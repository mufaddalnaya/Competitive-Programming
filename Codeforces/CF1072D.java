import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.math.*;
import java.lang.*;
import static java.lang.Math.*;

public class CF1072D implements Runnable {
    PrintWriter w ;
    InputReader c;
    public void run() {
        c = new InputReader(System.in);
        w = new PrintWriter(System.out);
        int n = c.nextInt(), k = c.nextInt();
        char[][] s = new char[n][];
        for(int i=0;i<n;i++)
            s[i] = c.next().toCharArray();
        int dp[][] = new int[n][n];
        for(int i=0;i<n;i++) Arrays.fill(dp[i],Integer.MAX_VALUE);
        dp[0][0] = 0;
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(i!=0) dp[i][j] = min(dp[i][j],dp[i-1][j]);
                if(j!=0) dp[i][j] = min(dp[i][j],dp[i][j-1]);
                if(s[i][j]!='a') dp[i][j]++;
            }
        }
//        for(int i=0;i<n;i++)printArray(dp[i],0);
        int max = 0;String ans = "";
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++) {
                if (dp[i][j]<=k && i+j+1>max){
                    max = i+j+1;
                    ans+="a";
                }
            }
        }
        //for(int i=0;i<max;i++) ans+='a';
//        w.println(ans+" "+max);
        if(max == (n-1)+(n-1)+1)
        {
            w.println(ans);
            w.close();
            return;
        }
        HashSet<pair> hs = new HashSet<>();
        char minchar = 'z';
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(i+j+1==max && dp[i][j]<=k){
                    if(i!=n-1 && s[i+1][j]<minchar){
                        hs = new HashSet<>();
                        minchar = s[i+1][j];
                    }
                    if(j!=n-1 && s[i][j+1]<minchar){
                        hs = new HashSet<>();
                        minchar = s[i][j+1];
                    }
                    if(i!=n-1 && s[i+1][j]==minchar)
                        hs.add(new pair(i+1,j));
                    if(j!=n-1 && s[i][j+1]==minchar)
                        hs.add(new pair(i,j+1));
                }
            }
        }
//        w.println(minchar);
//        for(pair p:hs) w.println(p.x+" "+p.y);

        if(hs.size()==0){
            hs.add(new pair(0,0));
            ans+=s[0][0];
        }else ans += minchar;
        while (hs.size()!=0){
            char minch = 'z';
            HashSet<pair> h = new HashSet<>();
            for(pair p:hs ){
                if(p.x!=n-1 && minch>s[p.x+1][p.y]) {
                    minch = s[p.x + 1][p.y];
                    h = new HashSet<>();
                }
                if(p.y!=n-1 && minch>s[p.x][p.y+1]){
                    minch = s[p.x][p.y+1];
                    h = new HashSet<>();
                }
                if(p.x!=n-1 && minch==s[p.x+1][p.y])
                    h.add(new pair(p.x+1,p.y));
                if(p.y!=n-1 && minch==s[p.x][p.y+1])
                    h.add(new pair(p.x,p.y+1));
            }
            if(h.size()==0) break;
            ans += minch;
            hs = h;
        }
        w.println(ans);
        w.close();
    }
    class pair{
        int x,y;

        public pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            pair pair = (pair) o;
            return x == pair.x &&
                    y == pair.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
    public static void sortbyColumn(int arr[][], int col){

        Arrays.sort(arr, new Comparator<int[]>()
        {
            public int compare(int[] o1, int[] o2){
                return (Integer.valueOf(o1[col]).compareTo(o2[col]));
            }
        });

    }

    static long gcd(long a, long b)    {
        if (b == 0)
            return a;
        return gcd(b, a % b);
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
    public static int[] radixSort(int[] f)    {
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
    public void printArray(int[] a,int x){
        for(int i=0;i<a.length;i++)
            w.print((a[i]+x)+" ");
        w.println();
    }
    public int[] scanArrayI(int n){
        int a[] = new int[n];
        for(int i=0;i<n;i++)
            a[i] = c.nextInt();
        return a;
    }
    public long[] scanArrayL(int n){
        long a[] = new long[n];
        for(int i=0;i<n;i++)
            a[i] = c.nextLong();
        return a;
    }
    public void printArray(long[] a,long x) {
        for(int i=0;i<a.length;i++)
            w.print((a[i]+x)+" ");
        w.println();
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
        new Thread(null, new CF1072D(),"CF1072D",1<<26).start();
    }
}