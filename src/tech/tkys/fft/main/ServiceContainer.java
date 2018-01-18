package tech.tkys.fft.main;

import java.util.HashMap;

/**
 * アプリケーション全体で使うサービスオブジェクトを保持する
 */
public class ServiceContainer {
    private static HashMap<String, Object> serviceObjectMap = new HashMap<String, Object>();

    public static void registerService(String key, Object instance) {
        if (key == null) {
            return;
        }

        if (serviceObjectMap.containsKey(key)) {
            return;
        }

        serviceObjectMap.put(key, instance);
    }

    public static Object getService(String key) {
        if (key == null) {
            return null;
        }

        return serviceObjectMap.get(key);
    }
}
