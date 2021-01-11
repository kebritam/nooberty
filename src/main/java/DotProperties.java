import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author kebritam
 * Project nooberty
 * Created on 11/01/2021
 */

public class DotProperties {

    private final File dotProperties;

    private DotProperties(File dotProperties) {
        this.dotProperties = dotProperties;
    }

    public static DotProperties getDotProperties(String propertyName) throws IOException {
        Optional<File> optionalFile = findDotProperties(propertyName);

        if (optionalFile.isPresent()) {
            return new DotProperties(optionalFile.get());
        } else {
            throw new FileNotFoundException("properties file: " + propertyName +" doesn't exist.");
        }
    }

    public static DotProperties getDotProperties() throws IOException {
        Optional<File> optionalFile = findDotProperties("");

        if (optionalFile.isPresent()) {
            return new DotProperties(optionalFile.get());
        } else {
            throw new FileNotFoundException("properties file does not exist");
        }
    }

    private static Optional<File> findDotProperties(String propertiesName) throws IOException {

        File[] fileArray = getFiles().toArray(File[]::new);
        for(File file: fileArray) {
            if (file.getName().matches(".*" + propertiesName + ".*\\.properties")) {
                return Optional.of(file);
            }
        }

        return Optional.empty();
    }

    private static Stream<File> getFiles() throws IOException {
        Path root = Paths.get("src");
        return Files.walk(root)
                .map(Path::toFile);
    }
}
