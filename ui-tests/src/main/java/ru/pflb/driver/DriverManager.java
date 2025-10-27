package ru.pflb.driver;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

public class DriverManager {

    private DriverManager() {

    }

    public static void initOptions() {
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(false)
                        .includeSelenideSteps(true)
        );

        Configuration.browser = "chrome";
        Configuration.timeout = 30000;
        Configuration.pageLoadTimeout = 45000;
        Configuration.pageLoadStrategy = "eager";
        Configuration.screenshots = true;
        Configuration.savePageSource = false;
        Configuration.reportsFolder = "build/allure-results";
        Configuration.browserSize = "1920x1080";
        Configuration.headless = true;

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        Configuration.browserCapabilities = options;
    }

}
