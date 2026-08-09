package org.apache.http.spring.boot.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ResponseContent Tests")
class ResponseContentTest {

    @Test
    @DisplayName("Default constructor creates non-null instance")
    void testDefaultInstance() {
        ResponseContent content = new ResponseContent();
        assertThat(content).isNotNull();
    }

    @Test
    @DisplayName("encoding getter/setter")
    void testEncoding() {
        ResponseContent content = new ResponseContent();
        content.setEncoding("UTF-8");
        assertThat(content.getEncoding()).isEqualTo("UTF-8");
    }

    @Test
    @DisplayName("contentBytes getter/setter")
    void testContentBytes() {
        ResponseContent content = new ResponseContent();
        byte[] bytes = "hello".getBytes();
        content.setContentBytes(bytes);
        assertThat(content.getContentBytes()).isEqualTo(bytes);
    }

    @Test
    @DisplayName("statusCode getter/setter")
    void testStatusCode() {
        ResponseContent content = new ResponseContent();
        content.setStatusCode(200);
        assertThat(content.getStatusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("contentText getter/setter")
    void testContentText() {
        ResponseContent content = new ResponseContent();
        content.setContentText("response body");
        assertThat(content.getContentText()).isEqualTo("response body");
    }

    @Test
    @DisplayName("contentType getter/setter")
    void testContentType() {
        ResponseContent content = new ResponseContent();
        content.setContentType("application/json");
        assertThat(content.getContentType()).isEqualTo("application/json");
    }

    @Test
    @DisplayName("contentTypeString getter/setter")
    void testContentTypeString() {
        ResponseContent content = new ResponseContent();
        content.setContentTypeString("text/html; charset=UTF-8");
        assertThat(content.getContentTypeString()).isEqualTo("text/html; charset=UTF-8");
    }

    @Test
    @DisplayName("content getter/setter")
    void testContent() {
        ResponseContent content = new ResponseContent();
        InputStream is = new ByteArrayInputStream("test".getBytes());
        content.setContent(is);
        assertThat(content.getContent()).isEqualTo(is);
    }

    @Test
    @DisplayName("allHeaders getter/setter")
    void testAllHeaders() {
        ResponseContent content = new ResponseContent();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Request-Id", "12345");
        content.setAllHeaders(headers);
        assertThat(content.getAllHeaders()).containsEntry("Content-Type", "application/json")
                .containsEntry("X-Request-Id", "12345");
    }
}
