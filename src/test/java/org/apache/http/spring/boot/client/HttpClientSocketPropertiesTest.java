package org.apache.http.spring.boot.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HttpClientSocketProperties Tests")
class HttpClientSocketPropertiesTest {

    @Test
    @DisplayName("Default constructor creates non-null instance")
    void testDefaultInstance() {
        HttpClientSocketProperties props = new HttpClientSocketProperties();
        assertThat(props).isNotNull();
    }

    @Test
    @DisplayName("soTimeout getter/setter")
    void testSoTimeout() {
        HttpClientSocketProperties props = new HttpClientSocketProperties();
        props.setSoTimeout(5000);
        assertThat(props.getSoTimeout()).isEqualTo(5000);
    }

    @Test
    @DisplayName("soReuseAddress getter/setter")
    void testSoReuseAddress() {
        HttpClientSocketProperties props = new HttpClientSocketProperties();
        props.setSoReuseAddress(true);
        assertThat(props.isSoReuseAddress()).isTrue();
    }

    @Test
    @DisplayName("soLinger getter/setter")
    void testSoLinger() {
        HttpClientSocketProperties props = new HttpClientSocketProperties();
        props.setSoLinger(30);
        assertThat(props.getSoLinger()).isEqualTo(30);
    }

    @Test
    @DisplayName("soKeepAlive getter/setter")
    void testSoKeepAlive() {
        HttpClientSocketProperties props = new HttpClientSocketProperties();
        props.setSoKeepAlive(true);
        assertThat(props.isSoKeepAlive()).isTrue();
    }

    @Test
    @DisplayName("tcpNoDelay getter/setter")
    void testTcpNoDelay() {
        HttpClientSocketProperties props = new HttpClientSocketProperties();
        props.setTcpNoDelay(true);
        assertThat(props.isTcpNoDelay()).isTrue();
    }

    @Test
    @DisplayName("sndBufSize getter/setter")
    void testSndBufSize() {
        HttpClientSocketProperties props = new HttpClientSocketProperties();
        props.setSndBufSize(8192);
        assertThat(props.getSndBufSize()).isEqualTo(8192);
    }

    @Test
    @DisplayName("rcvBufSize getter/setter")
    void testRcvBufSize() {
        HttpClientSocketProperties props = new HttpClientSocketProperties();
        props.setRcvBufSize(16384);
        assertThat(props.getRcvBufSize()).isEqualTo(16384);
    }

    @Test
    @DisplayName("backlogSize getter/setter")
    void testBacklogSize() {
        HttpClientSocketProperties props = new HttpClientSocketProperties();
        props.setBacklogSize(128);
        assertThat(props.getBacklogSize()).isEqualTo(128);
    }
}
