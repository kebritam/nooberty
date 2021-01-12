import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
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

    public String getValueOf(String key) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dotProperties))) {
            String line;
            boolean secured = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains("#<secure>")) secured = true;
                else if (secured && line.contains("#</secure>")) secured = false;

                if (secured) continue;
                String[] side = line.split("=");
                if (side[0].equalsIgnoreCase(key)) {
                    return side[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new NoSuchElementException("property " + key + " was not found");
    }

    public void setKeyAndValue(String key, String value, boolean secure) {

        StringBuilder injectLine = new StringBuilder();
        injectLine.append("\n");
        if (secure) injectLine.append("#<secure>\n");
        injectLine.append(key).append("=").append(value);
        if (secure) injectLine.append("\n#</secure>");

        try (BufferedReader reader = new BufferedReader(new FileReader(dotProperties))) {

            StringBuilder dotFile = new StringBuilder();
            String fileLine;
            while ((fileLine = reader.readLine()) != null) {
                String[] part = fileLine.split("=");
                if (part[0].equals(key)) {
                    dotFile.append(injectLine.toString()).append("\n");
                } else {
                    dotFile.append(fileLine).append("\n");
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dotProperties))) {
                writer.write(dotFile.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
