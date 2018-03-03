package tech.tkys.fft.main;

import java.util.HashMap;

/**
 * Stores service objects.
 */
public class ServiceContainer {
    /**
     * Stores objects with their key string.
     */
    private static HashMap<String, Object> serviceObjectMap = new HashMap<String, Object>();

    /**
     * Registers a object.
     * @param key      The string which corresponds to paired object.
     * @param instance The object instance to register.
     */
    public static void registerService(String key, Object instance) {
        if (key == null) {
            return;
        }

        if (serviceObjectMap.containsKey(key)) {
            return;
        }

        serviceObjectMap.put(key, instance);
    }

    /**
     * Gets the service object.
     * @param key The key string.
     * @return    The object instance which corresponds to the key, or null if not found.
     */
    public static Object getService(String key) {
        if (key == null) {
            return null;
        }

        return serviceObjectMap.get(key);
    }
}
