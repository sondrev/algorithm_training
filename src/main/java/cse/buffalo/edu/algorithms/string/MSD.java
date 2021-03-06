package cse.buffalo.edu.algorithms.string;

import cse.buffalo.edu.algorithms.stdlib.StdIn;
import cse.buffalo.edu.algorithms.stdlib.StdOut;

public class MSD {

  private static final int R = 256;  // extended ASCII alphabet size
  private static final int CUTOFF = 15;

  public static void sort(String[] a) {
    int N = a.length;
    String[] aux = new String[N];
    sort(a, 0, N-1, 0, aux);
  }

  // Return dth character of s, -1 if d = length of string
  private static int charAt(String s, int d) {
    if (d == s.length()) return -1;
    return s.charAt(d);
  }

  // d means dth character
  private static void sort(String[] a, int lo, int hi, int d, String[] aux) {
    if (hi <= lo + CUTOFF) {
      insertion(a, lo, hi, d);
      return;
    }

    // This place is not R+1 but R+2, because there is one more end character -1
    int[] count = new int[R+2];
    for (int i = lo; i <= hi; i++) {
      int c = charAt(a[i], d);
      count[c+2]++;
    }

    for (int r = 0; r < R+1; r++) {
      count[r+1] += count[r];
    }

    // Move data
    for (int i = lo; i <= hi; i++) {
      int c = charAt(a[i], d);
      // c+1 here is in case the end character -1, count[-1] is not correct
      aux[count[c+1]++] = a[i];
    }

    // Copy back
    for (int i = lo; i <= hi; i++) {
      a[i] = aux[i - lo];
    }

    // Recursively sort for each character
    for (int r = 0; r < R; r++) {
      sort(a, lo + count[r], lo + count[r+1] - 1, d+1, aux);
    }
  }

  private static void insertion(String[] a, int lo, int hi, int d) {
    for (int i = lo; i <= hi; i++) {
      for (int j = i; j > lo && less(a[j], a[j-1], d); j--) {
        exch(a, j, j-1);
      }
    }
  }

  private static void exch(String[] a, int i, int j) {
    String temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }

  private static boolean less(String v, String w, int d) {
    for (int i = d; i < Math.min(v.length(), w.length()); i++) {
      if (v.charAt(i) < w.charAt(i)) return true;
      if (v.charAt(i) > w.charAt(i)) return false;
    }
    return v.length() < w.length();
  }

  public static void main(String[] args) {
    String[] a = StdIn.readStrings();
    int N = a.length;
    sort(a);
    for (int i = 0; i < N; i++) {
      StdOut.println(a[i]);
    }
  }
}
