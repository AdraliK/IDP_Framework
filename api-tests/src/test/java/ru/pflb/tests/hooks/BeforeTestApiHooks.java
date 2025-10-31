package ru.pflb.tests.hooks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.pflb.database.DatabaseClient;
import ru.pflb.database.DatabaseInitializer;
import ru.pflb.database.DatabaseManager;
import ru.pflb.tests.BaseApiTests;
import ru.pflb.utils.DataKeys;
import ru.pflb.utils.DataStorage;

import java.util.UUID;

import static ru.pflb.steps.technical.ApiSteps.login;

public class BeforeTestApiHooks extends BaseApiTests {

    protected static DatabaseClient dbClient;
    protected DatabaseManager dbData;

    @BeforeAll
    static void initDatabase() {
        dbClient = DatabaseInitializer.createDatabaseClient();
        DatabaseInitializer.createTables(dbClient);
    }

    @BeforeEach
    void beforeLogin() {
        DataStorage.put(DataKeys.AUTH_TOKEN, login("user"));

        DataStorage.put(DataKeys.THREAD_ID, UUID.randomUUID().toString());
        dbData = new DatabaseManager(dbClient);
    }

}
