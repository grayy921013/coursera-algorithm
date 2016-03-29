import edu.princeton.cs.algs4.Picture;

import java.awt.*;

/**
 * Created by Zhehui Zhou on 3/29/16.
 */
public class SeamCarver {
    private Picture picture;

    public SeamCarver(Picture picture) {
        // create a seam carver object based on the given picture
        checkNull(picture);
        this.picture = new Picture(picture);
    }

    public Picture picture() {
        // current picture
        return new Picture(picture);
    }

    public int width() {
        // width of current picture
        return picture.width();
    }

    public int height() {
        // height of current picture
        return picture.height();
    }

    public double energy(int x, int y) {
        // energy of pixel at column x and row y
        if (x < 0 || x >= width() || y < 0 || y >= height()) throw new IndexOutOfBoundsException();
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) return 1000;
        return Math.sqrt(getEnergy(picture.get(x - 1, y), picture.get(x + 1, y)) +
                getEnergy(picture.get(x, y - 1), picture.get(x, y + 1)));
    }

    private double getEnergy(Color c1, Color c2) {
        return (c1.getBlue() - c2.getBlue()) * (c1.getBlue() - c2.getBlue()) +
                (c1.getGreen() - c2.getGreen()) * (c1.getGreen() - c2.getGreen()) +
                (c1.getRed() - c2.getRed()) * (c1.getRed() - c2.getRed());
    }

    public int[] findHorizontalSeam() {
        // sequence of indices for horizontal seam
        int[] seam = new int[width()];
        double[][] energy = new double[width()][height()];
        int[][] prev = new int[width()][height()];
        for (int i = 0; i < energy.length; i++) {
            for (int j = 0; j < energy[0].length; j++) {
                energy[i][j] = energy(i, j);
            }
        }
        return findSeam(energy, prev, seam);
    }

    public int[] findVerticalSeam() {
        // sequence of indices for vertical seam
        int[] seam = new int[height()];
        double[][] energy = new double[height()][width()];
        int[][] prev = new int[height()][width()];
        for (int i = 0; i < energy.length; i++) {
            for (int j = 0; j < energy[0].length; j++) {
                energy[i][j] = energy(j, i);
            }
        }
        return findSeam(energy, prev, seam);
    }

    private int[] findSeam(double[][] energy, int[][] prev, int[] seam) {
        for (int i = 1; i < energy.length; i++) {
            for (int j = 0; j < energy[0].length; j++) {
                double value1 = j - 1 >= 0 ? energy[i - 1][j - 1] : Double.MAX_VALUE;
                double value2 = energy[i - 1][j];
                double value3 = j + 1 < energy[0].length ? energy[i - 1][j + 1] : Double.MAX_VALUE;
                if (value1 <= value2 && value1 <= value3) {
                    energy[i][j] += value1;
                    prev[i][j] = j - 1;
                } else if (value2 <= value1 && value2 <= value3) {
                    energy[i][j] += value2;
                    prev[i][j] = j;
                } else {
                    energy[i][j] += value3;
                    prev[i][j] = j + 1;
                }
            }
        }
        int last = energy.length - 1;
        double minVal = energy[last][0];
        int min = 0;
        for (int i = 1; i < energy[0].length; i++) {
            if (energy[last][i] < minVal) {
                minVal = energy[last][i];
                min = i;
            }
        }
        seam[energy.length - 1] = min;
        for (int i = energy.length - 1; i > 0; i--) {
            seam[i - 1] = prev[i][seam[i]];
        }
        return seam;
    }

    public void removeHorizontalSeam(int[] seam) {
        // remove horizontal seam from current picture
        checkNull(seam);
        if (seam.length != width() || picture.height() <= 1) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (i < seam.length - 1 && Math.abs(seam[i] - seam[i + 1]) > 1) throw new IllegalArgumentException();
            if (seam[i] < 0 || seam[i] >= height()) throw new IllegalArgumentException();
        }
        Picture picture = new Picture(width(), height() - 1);
        for(int i = 0; i < width(); i++) {
            int cnt = 0;
            for(int j = 0; j < height(); j++) {
                if (seam[i] == j) continue;
                picture.set(i, cnt, this.picture.get(i, j));
                cnt++;
            }
        }
        this.picture = picture;
    }

    public void removeVerticalSeam(int[] seam) {
        // remove vertical seam from current picture
        checkNull(seam);
        if (seam.length != height() || picture.width() <= 1) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (i < seam.length - 1 && Math.abs(seam[i] - seam[i + 1]) > 1) throw new IllegalArgumentException();
            if (seam[i] < 0 || seam[i] >= width()) throw new IllegalArgumentException();
        }
        Picture picture = new Picture(width() - 1, height());
        for(int i = 0; i < height(); i++) {
            int cnt = 0;
            for(int j = 0; j < width(); j++) {
                if (seam[i] == j) continue;
                picture.set(cnt, i, this.picture.get(j, i));
                cnt++;
            }
        }
        this.picture = picture;
    }

    private void checkNull(Object o) {
        if (o == null) throw new NullPointerException();
    }
}
