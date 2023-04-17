## Введение
**Тестирование ПО** - это процесс испытания программы, целью которого является определить соответствие между **ожидаемым** поведением и **актуальным** с помощью набора тестов

Тестирование необходимо не только для проверки нового функционала, но и работоспособности старого (**регрессивное теситрование**)

Уровни тестирования:

1. Unit testing
2. Integration testing
3. Acceptance testing

**Unit testing** - тестирование маленького компонента приложения (функции), т.е. этот **unit** должен правильно отрабатывать в изоляции от других компонентов.

**Integration testing** - тестирование нескольких компонентов приложения (функции), т.е. как маленькие units работают вместе как один большой unit.

**Acceptance testing** - тестирование всего приложения в целом, т.е. как оно работает со стороны пользователя (**функциональное тестирование**).

JUnit5 - это один из самых распространенных java фреймворков, который предназначе для написания в основном Unit и Integration тестов

Для написания **Acceptance** тестов обычно используют другие тест фреймворки, например: JBehave, TestNG.

Junit5 разбит на несколько основных зависимостей

- JUnit Platform - запуск тестов на JVM, интеграция с Maven, Gradle, IDE, фреймворками.

- JUnit Jupiter - предоставляет набор классов, которые разработчики используют для написания тестов. API для программистов.

- JUnit Vintage - интеграция с предыдущими версиями JUnit3, JUnit4

Так же нужна Java выше 1.8

mvn -N io.takari:maven:0.7.7:wrapper -Dmaven=3.6.3 - для установки maven wrapper.

## Аннотация Test-Assertion

Все тесты, согласно конвенции должны находиться в папке src/test

Название тест-классов должно заканчиваться на Test, так maven-surefire-plugin поймёт, что их нужно отслеживать.

В JUnit5 классы тестов могут быть не public (как в JUnit4), а package-private.

maven-surefire-plugin раньше искал методы с постфиксом Test, теперь же достаточно поставить аннотацию @Test.

TDD - Test Driven Development - сначала пишем тест, потом функционал.

Название теста должно отображать функционал. Можно использовать snake-case.

В пакете Assertions есть много разных aasertов: assertTrue, assertFalse, assertEquals, AssertListEquals и так далее.

Но их бывает недостаточно, тогда можно подключить сторонние библиотеки Assertj, Hamcrest

## Test LyfeCycle

![alt text](lifecycle.png "Launcher API")

Порядок вызова тестов не гарантирован, поэтому они должны быть независимыми.

Методы @BeforeAll и @AfterAll должны быть static. Так как они выполняются только один раз для всех тестов.

По умолчанию у нас есть жизненный цикл тестов, его можно задавать через @TestInstance. Для LifeCycle.PER_METHOD 
@BeforeAll и @AfterAll должны быть static т.к. на каждый тест создаётся новый объект UserServiceTest.

Для LifeCycle.PER_CLASS создаётся один объект UserServiceTest и нет необходимость делать @BeforeAll и @AfterAll static.

## Запуск тестов Launcher API

![alt text](launcherApi.png "Launcher API")

При подключении зависимости junit-jupiter-engine транзитивно предоставляется junit5-API.

Так же транзитивно подключается общий API для всех тестов JUnit Engine API (junit-platform-engine).

Тесты запускаются в Idea, либо в maven(urefire-plugin - goal test), либо в gradle(task - test). <br>
Они используют JUnit Launcher (junit-platform-launcher).

JUnit Launcher в свою очередь запускает JUnit Engine API для проверки тестов

#TDD - Test Driven Development

![alt text](tdd.png "Launcher API")

В процессе разработки ПО сначала пишем тесты, а затем функционал, который покрываем этими тестами.

Аннотация @Value lombok переопределяет equals() и hashcode().

## Assertj and Hamcrest

Executable - функциональный интерфейс аналогичный Runnable, но пробрасывает exception.

assertAll - проверяет все assertы, в отличии от набора, когда если один assert падает, то до других проверка не доходит.

В hamcrest в assertThat передается объект и matcher<br>
MatcherAssert.assertThat(users, IsMapContaining.hasKey(IVAN.getId()));

## Testing exceptions

В junit4 пробрасывание exceptions проверялось аннотацией<br>
@org.junit.Test(excepted = IllegalArgumentException.class)

