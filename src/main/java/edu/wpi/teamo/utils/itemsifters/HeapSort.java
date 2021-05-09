package edu.wpi.teamo.utils.itemsifters;

public class HeapSort {
    public static void sort(Comparable[] a)
    {
        int n = a.length;
        for (int k = n/2; k >= 1; --k)
            sink(a, k, n);
        if (1 < n) do
        {
            swap(a, 1, n);
            sink(a, 1, --n);
        } while (1 < n);
    }

    private static int iCompare(Comparable[] a, int i, int j) {
        return a[j-1].compareTo(a[i-1]);
    }

    private static void swap(Comparable[] a, int x, int y) {
        Comparable t = a[x-1];
        a[x-1] = a[y-1];
        a[y-1] = t;
    }

    private static void sink(Comparable[] a, int k, int n)
    {
        while ( k*2 <= n ) {
            int j = 2 * k;
            if ( j < n && iCompare( a, j, j+1 ) > 0 ) j++;
            if ( (iCompare( a, k, j ) <= 0) ) break;
            swap( a, k, j );
            k = j;
        }
    }
}
