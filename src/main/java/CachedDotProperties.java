import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author kebritam
 * Project nooberty
 * Created on 16/03/2021
 */

public final class CachedDotProperties implements Properties {

    private final ConcurrentMap<String, String> map;
    private final File dotProperties;

    private CachedDotProperties(File dotProperties) {
        this.dotProperties = dotProperties;
        this.map = new ConcurrentHashMap<>();
        addToMap();
    }

    public static Properties getCachedDotProperties(String propertyName) throws IOException {
        File file = Properties.findDotProperties(propertyName);

        if (file != null) {
            return new CachedDotProperties(file);
        } else {
            throw new FileNotFoundException("properties dotProperties " + propertyName + " doesn't exist.");
        }
    }

    public static Properties getCachedDotProperties() throws IOException {
        return getCachedDotProperties("");
    }


    private void addToMap() {
        try (BufferedReader reader = new BufferedReader(new FileReader(dotProperties))) {
            iterateLines(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iterateLines(BufferedReader reader) throws IOException {
        String line;
        boolean secured = false;
        while ((line = reader.readLine()) != null) {
            if ((secured = isSecure(line, secured)))
                continue;

            String[] part = line.split("=");
            if (!part[1].equals(""))
                map.put(part[0], part[1]);
        }
    }

    @Override
    public final String getValueOf(String key) {
        return map.get(key);
    }
}
