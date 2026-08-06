<a id="readme-top"></a>

<div align="center">

# httpclient-spring-boot-starter

**Spring Boot Starter for httpclient**

[![Maven Central](https://img.shields.io/maven-central/v/io.github.easy4j/httpclient-spring-boot-starter)](https://github.com/easy-4-java/httpclient-spring-boot-starter)
[![Java](https://img.shields.io/badge/Java-17-orange)](#3-requirements-and-compatibility)
[![License](https://img.shields.io/badge/license-Apache-2.0-green)](https://www.apache.org/licenses/LICENSE-2.0)

[简体中文](./README.zh-CN.md) | [English](./README.md)

[Positioning](#1-positioning) · [Capabilities](#2-core-capabilities) ·
[Dependency](#5-dependency) · [Quick Start](#6-quick-start) ·
[Configuration](#7-configuration-reference) · [Versions](#9-version-lines-and-compatibility) ·
[Build](#10-build-and-test) · [License](#12-license)

</div>

---

> **Current Version**：`3.0.x.20260527-SNAPSHOT`<br>
> **JDK Baseline**：`17`<br>
> **Group ID**：`io.github.easy4j`<br>
> **Artifact ID**：`httpclient-spring-boot-starter`<br>
> **License**：Apache License 2.0<br>

## 1. Positioning

**httpclient-spring-boot-starter** is a Spring Boot starter that integrates **httpclient** for applications using httpclient. It provides auto-configuration, property binding, and ready-to-use beans so that applications can consume httpclient capabilities with minimal setup.

| Dimension | Description |
|---|---|
| Type | Spring Boot Starter |
| Consumers | Spring Boot applications using httpclient |
| Core Capabilities | auto-configuration, property binding, ready-to-use beans for httpclient |
| JDK | `17` |
| Coordinates | `io.github.easy4j:httpclient-spring-boot-starter:3.0.x.20260527-SNAPSHOT` |
| Config Prefix | `httpclient` |

## 2. Core Capabilities

| Capability | Status | Description |
|---|:---:|---|
| Auto-configuration | ✅ Stable | Registers httpclient beans automatically |
| Property Binding | ✅ Stable | Binds `httpclient.*` to `HttpClientMetricProperties` |
| `HttpClientConnectionManagerBuilder` bean | ✅ Stable | Auto-registered via HttpClientBuilderAutoConfiguration, HttpClientDependsOnAutoConfiguration, HttpClientMetricAutoConfiguration |

## 3. Requirements and Compatibility

| Dependency | Minimum | Evidence |
|---|---:|---|
| JDK | `17` | `pom.xml` |
| Spring Boot | `3.0.13` | `pom.xml` parent |
| Maven | `3.6+` | Maven Enforcer |

## 4. Auto-configuration

The starter auto-configures the following beans:

| Bean | Condition | Missing Behavior |
|---|---|---|
| `HttpClientConnectionManagerBuilder` | classpath + property | not created |
| `HttpClientConnectionManager` | classpath + property | not created |
| `HttpClientBuilder` | classpath + property | not created |
| `CloseableHttpClient` | classpath + property | not created |
| `HttpComponentsClientHttpRequestFactory` | classpath + property | not created |
| `ConnectionConfig` | classpath + property | not created |
| `SocketConfig` | classpath + property | not created |
| `RequestConfig` | classpath + property | not created |
| `ConnectionBackoffStrategy` | classpath + property | not created |
| `ConnectionReuseStrategy` | classpath + property | not created |
| `CookieStore` | classpath + property | not created |
| `DnsResolver` | classpath + property | not created |
| `ConnectionKeepAliveStrategy` | classpath + property | not created |
| `AuthenticationStrategy` | classpath + property | not created |
| `PublicSuffixMatcher` | classpath + property | not created |
| `HttpRequestExecutor` | classpath + property | not created |
| `RedirectStrategy` | classpath + property | not created |
| `HttpRequestRetryHandler` | classpath + property | not created |
| `SchemePortResolver` | classpath + property | not created |
| `ServiceUnavailableRetryStrategy` | classpath + property | not created |
| `AuthenticationStrategy` | classpath + property | not created |
| `UserTokenHandler` | classpath + property | not created |
| `HostnameVerifier` | classpath + property | not created |
| `TrustStrategy` | classpath + property | not created |
| `TrustManager` | classpath + property | not created |
| `SSLContext` | classpath + property | not created |
| `LayeredConnectionSocketFactory` | classpath + property | not created |
| `HttpRequestInterceptor` | classpath + property | not created |
| `HttpRequestInterceptor` | classpath + property | not created |
| `HttpRequestInterceptor` | classpath + property | not created |
| `HttpResponseInterceptor` | classpath + property | not created |
| `MetricRegistry` | classpath + property | not created |
| `HttpClientMetricNameStrategy` | classpath + property | not created |
| `HttpRequestExecutor` | classpath + property | not created |
| `HttpClientConnectionManagerBuilder` | classpath + property | not created |

Auto-configuration registration:

- `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` (Spring Boot 2.7+ / 3.x / 4.x)
- `META-INF/spring.factories` (Spring Boot 2.x legacy)

## 5. Dependency

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>httpclient-spring-boot-starter</artifactId>
    <version>3.0.x.20260527-SNAPSHOT</version>
</dependency>
```

No additional easy4j component dependencies.

## 6. Quick Start

### 6.1 Add dependency

Add the dependency above to your `pom.xml`.

### 6.2 Configure

```yaml
httpclient:
  enabled: true
```

### 6.3 Use the bean

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

Then inject the auto-configured bean in your code:

```java
@Autowired
private HttpClientConnectionManagerBuilder connectionManagerBuilder;
```

## 7. Configuration Reference

### 7.1 Config Prefix

`httpclient`

### 7.2 Configuration Items

| Property | Type | Default | Required | Description | Sensitive |
|---|---|---|:---:|---|:---:|
| `httpclient.enabled` | boolean | `true` | No | Enable the starter | No |
<!-- additional properties below -->

## 8. Version Lines and Compatibility

| Branch | JDK | Spring Boot | Component Version | Status |
|---|---:|---:|---|:---:|
| `2.3.x` / `2.7.x` | `8+` | 2.3.x / 2.7.x | `1.0.x` | Maintenance |
| `3.0.x` ~ `3.5.x` | `17` | 3.x | `2.0.x` | Maintenance |
| `4.0.x` / `4.1.x` | `17+` | 4.x | `3.0.x` | Active |

## 9. Build and Test

```bash
mvn clean verify
mvn -pl httpclient-spring-boot-starter -am test
```

## 10. Troubleshooting

| Symptom | Diagnosis | Resolution |
|---|---|---|
| Bean not created | Check auto-configuration report | Verify `httpclient.enabled=true` and classpath |
| `ClassNotFoundException` | Missing dependency | Add the required module |
| Version conflict | `mvn dependency:tree` | Use BOM for version alignment |

## 11. Contribution

1. Fork the repository.
2. Create a feature branch.
3. Run `mvn clean verify` before submitting.
4. Submit a pull request.

## 12. License

This project is licensed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).

---

<div align="center">

[Back to top](#readme-top) · [Issues](https://github.com/easy-4-java/httpclient-spring-boot-starter/issues) · [Repository](https://github.com/easy-4-java/httpclient-spring-boot-starter)

</div>
