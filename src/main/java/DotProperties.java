import java.io.*;
import java.util.NoSuchElementException;

/**
 * @author kebritam
 * Project nooberty
 * Created on 11/01/2021
 */

public final class DotProperties extends Properties {

    private final File dotProperties;

    private DotProperties(File dotProperties) {
        this.dotProperties = dotProperties;
    }

    public static Properties getDotProperties(String propertyName) throws IOException {
        File file = Properties.findDotProperties(propertyName);

        if (file != null) {
            return new DotProperties(file);
        } else {
            throw new FileNotFoundException("properties file " + propertyName + " doesn't exist.");
        }
    }

    public static Properties getDotProperties() throws IOException {
        return getDotProperties("");
    }

    @Override
    public String getValueOf(String key) {
        String value = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(dotProperties))) {
            value = iterateLines(reader, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    private String iterateLines(BufferedReader reader, String key) throws IOException {
        String line;
        boolean secured = false;
        while (((line = reader.readLine()) != null)) {
            if ((secured = isSecure(line, secured)))
                continue;

            String[] side = line.split("=");
            if (side[0].equalsIgnoreCase(key)) {
                return side[1];
            }
        }
        throw new NoSuchElementException("property " + key + " was not found");
    }
}
