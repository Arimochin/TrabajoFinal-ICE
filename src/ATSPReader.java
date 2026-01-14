import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ATSPReader {

    public static int[][] init(String filePath) {


        try {
            int[][] matrix = parseATSP(filePath);
            printMatrix(matrix);
            return matrix;

        } catch (FileNotFoundException e) {
            System.err.println("No se encontró el archivo: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
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
                        System.out.println("Dimensión detectada: " + dimension);
                    }
                }

                // Detectar inicio de datos
                if (token.equals("EDGE_WEIGHT_SECTION")) {
                    readingMatrix = true;
                }
            }
        }

        scanner.close();

        if (matrix == null) {
            throw new RuntimeException("No se pudo leer la matriz. Verifique el formato del archivo.");
        }

        return matrix;
    }

    // Método auxiliar para imprimir la matriz
    public static void printMatrix(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        System.out.println("--- Matriz de Costos ---");

        int limit = Math.min(rows, 10);

        for (int i = 0; i < limit; i++) {
            for (int j = 0; j < limit; j++) {
                System.out.printf("%4d ", matrix[i][j]);
            }
            System.out.println();
        }
        if (rows > 10) System.out.println("... (matriz truncada para visualización)");
    }
}
