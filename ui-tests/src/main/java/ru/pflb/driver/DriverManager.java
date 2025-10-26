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
        String driverPath = new File("src/main/resources/drivers/chromedriver.exe")
                .getAbsolutePath();
        System.setProperty("webdriver.chrome.driver", driverPath);

        Configuration.browser = "chrome";
        Configuration.timeout = 10000;
        Configuration.pageLoadStrategy = "none";
        Configuration.screenshots = true;
        Configuration.savePageSource = true;
        Configuration.reportsFolder = "build/allure-results";
        Configuration.browserSize = "1920x1080";
        Configuration.headless = true;

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        Configuration.browserCapabilities = options;

        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
                        .includeSelenideSteps(true)
        );
    }

}
