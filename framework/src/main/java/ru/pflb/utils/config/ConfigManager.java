package ru.pflb.utils.config;

import org.aeonbits.owner.ConfigFactory;

public class ConfigManager {

    private static EnvConfig config;

    private ConfigManager() {}

    public static EnvConfig get() {
        if (config == null) {
            config = ConfigFactory.create(EnvConfig.class);
        }
        return config;
    }

}
