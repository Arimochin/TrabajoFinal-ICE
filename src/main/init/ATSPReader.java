package main.init;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ATSPReader {

    public static int[][] init(String filePath) {


        try {
            int[][] matrix = parseATSP(filePath);

            return matrix;

        } catch (FileNotFoundException e) {
            System.err.println("Couldn't find file: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Couldn't read file: " + e.getMessage());
            return null;
        }

    }

    public static int[][] parseATSP(String path) throws FileNotFoundException {


        File file = new File(path);
        Scanner scanner = new Scanner(file);

        int dimension = 0;
        int[][] matrix = null;
        boolean readingMatrix = false;


        while (scanner.hasNext()) {
            if (readingMatrix) {

                for (int i = 0; i < dimension; i++) {
                    for (int j = 0; j < dimension; j++) {
                        if (scanner.hasNextInt()) {
                            matrix[i][j] = scanner.nextInt();
                        }
                    }
                }

                break;
            } else {

                String token = scanner.next();


                if (token.contains("DIMENSION")) {


                    if (token.endsWith(":")) {
                        dimension = scanner.nextInt();
                    } else {

                        String next = scanner.next();
                        if (next.equals(":")) {
                            dimension = scanner.nextInt();
                        } else {

                            try {
                                dimension = Integer.parseInt(next);
                            } catch (NumberFormatException e) {

                            }
                        }
                    }

                    if (dimension > 0) {
                        matrix = new int[dimension][dimension];

                    }
                }


                if (token.equals("EDGE_WEIGHT_SECTION")) {
                    readingMatrix = true;
                }
            }
        }

        scanner.close();

        if (matrix == null) {
            throw new RuntimeException("Couldn't read the matrix, check the file format.");
        }

        return matrix;
    }



}
