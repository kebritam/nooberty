import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * @author kebritam
 * Project nooberty
 * Created on 16/03/2021
 */

public abstract class Properties {
    abstract public String getValueOf(String key) throws NoSuchElementException;

    static File findDotProperties(String propertiesName) {
        File[] fileArray = getFiles();
        for(File file: fileArray) {
            if (file.getName().matches(".*" + propertiesName + ".*\\.properties")) {
                return file;
            }
        }
        return null;
    }

    private static File[] getFiles() {
        Path root = Paths.get("src");
        File[] files = new File[0];
        try (Stream<Path> fileStream = Files.walk(root)) {
            files = fileStream
                    .map(Path::toFile)
                    .toArray(File[]::new);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    boolean isSecure(String line, boolean secured) {
        if (secured) {
            return !line.contains("#</secure>");
        } else {
            return line.contains("#<secure>");
        }
    }

}
