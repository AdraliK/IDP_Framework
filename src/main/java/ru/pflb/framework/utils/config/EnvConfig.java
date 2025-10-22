package ru.pflb.framework.utils.config;
import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "classpath:application.properties",
        "classpath:secrets.properties"
})
public interface EnvConfig extends Config {

    @Key("base.url")
    String baseUrl();

    @Key("citilink.url")
    String citilinkUrl();
}
