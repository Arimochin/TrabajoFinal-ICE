import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.IOException;

public class LogWriter {
    public static void writeInfo(String fileName, String content) {
        content += System.lineSeparator();
        try {
            // "Paths.get" define la ruta, "write" crea el archivo o lo sobreescribe
            Files.write(Paths.get(fileName), content.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        }
    }

    public static void resetLogFile(String fileName) {
        try {
            // Escribe un array de bytes vacío, truncando el archivo existente
            Files.write(Paths.get(fileName), new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("No se pudo limpiar el archivo: " + e.getMessage());
        }
    }
}