package org.apache.http.spring.boot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HttpClientMetricProperties Tests")
class HttpClientMetricPropertiesTest {

    @Test
    @DisplayName("Default constructor creates non-null instance")
    void testDefaultInstance() {
        HttpClientMetricProperties props = new HttpClientMetricProperties();
        assertThat(props).isNotNull();
    }

    @Test
    @DisplayName("PREFIX constant")
    void testPrefix() {
        assertThat(HttpClientMetricProperties.PREFIX).isEqualTo("httpclient.metrics");
    }

    @Test
    @DisplayName("enabled getter/setter")
    void testEnabled() {
        HttpClientMetricProperties props = new HttpClientMetricProperties();
        props.setEnabled(true);
        assertThat(props.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("name getter/setter")
    void testName() {
        HttpClientMetricProperties props = new HttpClientMetricProperties();
        props.setName("myHttpClient");
        assertThat(props.getName()).isEqualTo("myHttpClient");
    }

    @Test
    @DisplayName("waitForContinue getter/setter")
    void testWaitForContinue() {
        HttpClientMetricProperties props = new HttpClientMetricProperties();
        props.setWaitForContinue(5000);
        assertThat(props.getWaitForContinue()).isEqualTo(5000);
    }
}
