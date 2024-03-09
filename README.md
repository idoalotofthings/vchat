# VChat Backend

**Basic `javax.servlet.http.HttpServlet` which provides a reliable backend to the chatbot.**

Architecture has been paid well attention to, and strict implementation of the repository pattern has been followed
Currently, three data sources exist for providing data:
1. Remote data source, utilising `Retrofit` in [NetworkQueryRepository](src/main/kotlin/io/github/idoalotofthings/vchat/repository/NetworkQueryRepository.kt)
2. Local data source, looking up queries from  `WEB-INF/queries.json` in [LocalQueryRepository](src/main/kotlin/io/github/idoalotofthings/vchat/repository/LocalQueryRepository.kt)
3. Cache, both repositories cache the received data to be as independent of the data sources in `WEB-INF/vchat_cache.json`

However, the query tree is always retained in memory for fast access.
The data is refreshed every hour

## Build Information
* Build System: Gradle 8.5
* Java target: 21
* Kotlin version: 1.9.21

### Libraries used
1. JavaX Servlet API 4.0.1: `javax.servlet:javax.servlet-api:4.0.1`
2. Apache Log4J 2.23.0: `org.apache.logging.log4j:log4j-core:2.23.0`
3. KotlinX Serialization 1.6.3: `org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3`
4. KotlinX Coroutines 1.8.0: `org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0`
5. Retrofit 2.9.0: `com.squareup.retrofit2:retrofit:2.9.0`
6. Retrofit Converter Factory for KotlinX Serialization: `com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0`

### Testing frameworks
1. Kotlin Test: `org.jetbrains.kotlin:kotlin-test`
2. Mockito: `org.mockito:mockito-core:5.11.0`
