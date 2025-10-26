# IDP_Framework

## Описание проекта

Данный проект представляет собой внутренний assessment-проект, разработанный для демонстрации навыков тестирования UI и API.

---

## Стек

- **Язык:** Java 22
- **UI:** [Selenide](https://selenide.org/)
- **API:** [RestAssured](https://rest-assured.io/)
- **Фреймворки и отчетность:** [JUnit 5](https://docs.junit.org/5.0.0/user-guide/), [Allure Report](https://allurereport.org/docs/), Allure Rest-Assured, Allure Selenide
- **Сборка и управление зависимостями:** Maven
- **Логирование:** SLF4J + Logback
- **Работа с JSON:** Jackson, Gson, Jsoup
- **Конфигурации:** Owner

---

## Поддерживаемые типы тестирования

- **UI тесты:**  
  Тестирование web-интерфейса интернет-магазина Citilink

- **API тесты:**  
  Проверка эндпоинтов на тестовом стенде PFLB

---

## Структура проекта

```project
src/
├─ main/
│   ├─ java/ru/pflb/framework/
│   │   ├─ pages/         — Page Object классы
│   │   ├─ client/        — API-клиенты
│   │   ├─ steps/api/     — API шаги
│   │   ├─ core/          — DriverManager и базовая инфраструктура
│   │   ├─ dto/           — DTO модели
│   │   └─ utils/         — Утилиты
│   └─ resources/
│       ├─ application.properties
│       ├─ secrets.properties — требуется заполнить перед запуском
│       └─ drivers/ (chromedriver.exe и др.)
└─ test/
    ├─ java/ru/pflb/tests/
    │   ├─ ui/   — UI тесты
    │   └─ api/  — API тесты
    └─ resources/data/ —  тестовые данные
```
---

## Особенности реализации

- Используется паттерн **Page Object** для поддержки читаемости и переиспользуемости кода.
- **Allure Steps** применяются для ведения подробной отчётности и визуализации действий в тестах.
- Автоматизация покрывает интеграционные и end-to-end тесты
- API и UI шаги организованы для повторного использования.

---

## Настройка доступов к стенду тестирования API

Для запуска тестов необходимо заполнить файл `src/main/resources/secrets.properties`.  
Данные от учётных записей можно запросить у коллег.

**Пример структуры файла secrets.properties:**
```properties
user1.username=login
user1.password=password
```

---

## Запуск тестов

**Чистка проекта**
```bash
mvn clean
```
**Запуск всех тестов**
```bash
mvn test
```
**Запуск конкретного класса**
```bash
mvn test -Dtest=UserTests
```
**Запуск всех тестов из пакета или директории**
```bash
mvn test -Dtest="ru.pflb.tests.api.**"
```
### Запуск по тегам (JUnit 5)

**Включение тегов:**
```bash
mvn test -Dgroups="smoke"
```

**Исключение тегов:**
```bash
mvn test -DexcludedGroups="ui"
```

**Формирование Allure отчета**
```bash
mvn allure:serve
```
### Параллельный выполнение тестов

Для параллельного запуска стоит обратить внимание на файл `src/test/resources/junit-platform.properties`.
* Параметр `junit.jupiter.execution.parallel.enabled` отвечает за ВКЛ/ВЫКЛ параллельного запуска (по умолчанию стоит значение `false`)
* Параметр `junit.jupiter.execution.parallel.config.strategy` позволяет выбрать стратегию параллельного выполнения тестов:
  * `dynamic` для использования всего ЦП
  * `fixed` для фиксированного значения потоков (кол-во потоков узаывается в поле `junit.jupiter.execution.parallel.config.fixed.parallelism`)
