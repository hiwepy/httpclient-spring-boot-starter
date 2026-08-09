/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.http.spring.boot.client;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.http.config.MessageConstraints;
import org.apache.http.spring.boot.client.HttpClientConnectionProperties.CodingErrorActionEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link HttpClientConnectionProperties }}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("HttpClientConnectionProperties Tests")
class HttpClientConnectionPropertiesTest {

    @Test
    @DisplayName("Default constructor creates non-null instance")
    void testDefaultInstance() {
        HttpClientConnectionProperties props = new HttpClientConnectionProperties();
        assertThat(props).isNotNull();
    }

    @Test
    @DisplayName("bufferSize getter/setter")
    void testBufferSize() {
        HttpClientConnectionProperties props = new HttpClientConnectionProperties();
        assertThat(props.getBufferSize()).isEqualTo(0);
        props.setBufferSize(4096);
        assertThat(props.getBufferSize()).isEqualTo(4096);
    }

    @Test
    @DisplayName("fragmentSizeHint getter/setter")
    void testFragmentSizeHint() {
        HttpClientConnectionProperties props = new HttpClientConnectionProperties();
        assertThat(props.getFragmentSizeHint()).isEqualTo(0);
        props.setFragmentSizeHint(1024);
        assertThat(props.getFragmentSizeHint()).isEqualTo(1024);
    }

    @Test
    @DisplayName("charset getter/setter")
    void testCharset() {
        HttpClientConnectionProperties props = new HttpClientConnectionProperties();
        assertThat(props.getCharset()).isNull();
        props.setCharset(StandardCharsets.UTF_8);
        assertThat(props.getCharset()).isEqualTo(StandardCharsets.UTF_8);
    }

    @Test
    @DisplayName("malformedInputAction getter/setter")
    void testMalformedInputAction() {
        HttpClientConnectionProperties props = new HttpClientConnectionProperties();
        assertThat(props.getMalformedInputAction()).isEqualTo(CodingErrorActionEnum.IGNORE);
        props.setMalformedInputAction(CodingErrorActionEnum.REPLACE);
        assertThat(props.getMalformedInputAction()).isEqualTo(CodingErrorActionEnum.REPLACE);
    }

    @Test
    @DisplayName("unmappableInputAction getter/setter")
    void testUnmappableInputAction() {
        HttpClientConnectionProperties props = new HttpClientConnectionProperties();
        assertThat(props.getUnmappableInputAction()).isEqualTo(CodingErrorActionEnum.IGNORE);
        props.setUnmappableInputAction(CodingErrorActionEnum.REPORT);
        assertThat(props.getUnmappableInputAction()).isEqualTo(CodingErrorActionEnum.REPORT);
    }

    @Test
    @DisplayName("messageConstraints getter/setter")
    void testMessageConstraints() {
        HttpClientConnectionProperties props = new HttpClientConnectionProperties();
        assertThat(props.getMessageConstraints()).isNull();
        MessageConstraints constraints = MessageConstraints.custom().setMaxHeaderCount(100).setMaxLineLength(2000).build();
        props.setMessageConstraints(constraints);
        assertThat(props.getMessageConstraints()).isEqualTo(constraints);
    }

    @Test
    @DisplayName("CodingErrorActionEnum value() returns name")
    void testCodingErrorActionEnumValue() {
        assertThat(CodingErrorActionEnum.IGNORE.value()).isEqualTo("IGNORE");
        assertThat(CodingErrorActionEnum.REPLACE.value()).isEqualTo("REPLACE");
        assertThat(CodingErrorActionEnum.REPORT.value()).isEqualTo("REPORT");
    }

    @Test
    @DisplayName("CodingErrorActionEnum equals(CodingErrorActionEnum)")
    void testCodingErrorActionEnumEqualsEnum() {
        assertThat(CodingErrorActionEnum.IGNORE.equals(CodingErrorActionEnum.IGNORE)).isTrue();
        assertThat(CodingErrorActionEnum.IGNORE.equals(CodingErrorActionEnum.REPLACE)).isFalse();
    }

    @Test
    @DisplayName("CodingErrorActionEnum equals(String)")
    void testCodingErrorActionEnumEqualsString() {
        assertThat(CodingErrorActionEnum.IGNORE.equals("IGNORE")).isTrue();
        assertThat(CodingErrorActionEnum.IGNORE.equals("ignore")).isTrue();
        assertThat(CodingErrorActionEnum.IGNORE.equals("REPLACE")).isFalse();
    }

    @Test
    @DisplayName("CodingErrorActionEnum valueOfIgnoreCase")
    void testCodingErrorActionEnumValueOfIgnoreCase() {
        assertThat(CodingErrorActionEnum.valueOfIgnoreCase("IGNORE")).isEqualTo(CodingErrorActionEnum.IGNORE);
        assertThat(CodingErrorActionEnum.valueOfIgnoreCase("replace")).isEqualTo(CodingErrorActionEnum.REPLACE);
        assertThat(CodingErrorActionEnum.valueOfIgnoreCase("Report")).isEqualTo(CodingErrorActionEnum.REPORT);
    }

    @Test
    @DisplayName("PREFIX constant")
    void testPrefix() {
        assertThat(HttpClientConnectionProperties.PREFIX).isEqualTo("httpclient.connection");
    }
}
