public class Problem7 {
    public static void pairSums(int k, int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++)
                if (arr[i] + arr[j] == k) {
                    System.out.println(arr[i] + " + " + arr[j] + " = " + k);
                }
        }
    }

    public static void main(String[] args) {
        int[] arr = {10, 4, 7, 7, 8, 5, 15};

        pairSums(12, arr);

    }

    // Nested for-loops - outer loop runs n times, inner runs n - 1; arithmetic sequence, essentially comes out to O(n^2).
}
