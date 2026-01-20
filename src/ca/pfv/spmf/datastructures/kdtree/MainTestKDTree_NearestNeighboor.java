package ca.pfv.spmf.datastructures.kdtree;

import ca.pfv.spmf.patterns.cluster.DoubleArray;

import java.util.ArrayList;
import java.util.List;

/**
 * This test show how to use the KDTree structure to find
 * the nearest neighboor to a given point and is intended for testing the KDtree structure
 * by developers.
 *
 * @author Philippe Fournier-Viger
 */
class MainTestKDTree_NearestNeighboor {

    public static void main(String[] args) {
        // create an empty kd tree
        KDTree tree = new KDTree();

        // Use a list of point to create the kd-tree
        List<DoubleArray> points = new ArrayList<DoubleArray>();
        points.add(new DoubleArray(new double[]{2d, 3d}));
        points.add(new DoubleArray(new double[]{5d, 4d}));
        points.add(new DoubleArray(new double[]{9d, 6d}));
        points.add(new DoubleArray(new double[]{4d, 7d}));
        points.add(new DoubleArray(new double[]{8d, 1d}));
        points.add(new DoubleArray(new double[]{7d, 2d}));

        // insert the points into the tree.
        tree.buildtree(points);

        // print the tree for debugging purposes
        System.out.println("\nTREE: \n" + tree.toString() + "  \n\n Number of elements in tree: " + tree.size());

        DoubleArray query = new DoubleArray(new double[]{7.9d, 4d});
        DoubleArray nearestpoint = tree.nearest(query);
        System.out.println("The nearest neighboor is: :" + nearestpoint);

//		// find the best answer by brute force to verify the result
//		double min = Double.MAX_VALUE;
//		double[] closest = null;
//		for(int i=0; i< points.length; i++){
//			double dist = distance(query, points[i]);
//			if( dist < min){
//				min = dist;
//				closest = points[i];
//			}
//		}		
//		System.out.println(" good answer :" + toString(closest));
//		System.out.println();
//		
//		
    }

    public static String toString(double[] values) {
        StringBuilder buffer = new StringBuilder();
        for (Double element : values) {
            buffer.append("   " + element);
        }
        return buffer.toString();
    }

    @SuppressWarnings("unused")
    private static double distance(double[] node1, double[] node2) {
        double sum = 0;
        for (int i = 0; i < node1.length; i++) {
            sum += Math.pow(node1[i] - node2[i], 2);
        }
        return Math.sqrt(sum);
    }

}
