/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
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
import java.util.NoSuchElementException;

import org.apache.http.config.MessageConstraints;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TODO
 * 
 * @author ： <a href="https://github.com/vindell">vindell</a>
 */
@ConfigurationProperties(prefix = HttpClientConnectionProperties.PREFIX)
public class HttpClientConnectionProperties {

	public final static String PREFIX = "httpclient.connection";

	public static enum CodingErrorActionEnum {

		IGNORE("IGNORE"), 
		REPLACE("REPLACE"), 
		REPORT("REPORT");

		private final String name;

		CodingErrorActionEnum(String name) {
			this.name = name;
		}

		public String value() {
			return name;
		}
		
		public boolean equals(CodingErrorActionEnum name){
			return this.compareTo(name) == 0;
		}
		
		public boolean equals(String name){
			return this.compareTo(CodingErrorActionEnum.valueOfIgnoreCase(name)) == 0;
		}
		
		public static CodingErrorActionEnum valueOfIgnoreCase(String name) {
			for (CodingErrorActionEnum type : CodingErrorActionEnum.values()) {
				if(type.value().equalsIgnoreCase(name)) {
					return type;
				}
			}
	    	throw new NoSuchElementException("Cannot found CodingErrorAction with key '" + name + "'.");
	    }
		
	}
	
	private int bufferSize;
	private int fragmentSizeHint;
	private Charset charset;
	private CodingErrorActionEnum malformedInputAction = CodingErrorActionEnum.IGNORE;
	private CodingErrorActionEnum unmappableInputAction = CodingErrorActionEnum.IGNORE;
	private MessageConstraints messageConstraints;
	
	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getFragmentSizeHint() {
		return fragmentSizeHint;
	}

	public void setFragmentSizeHint(int fragmentSizeHint) {
		this.fragmentSizeHint = fragmentSizeHint;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public CodingErrorActionEnum getMalformedInputAction() {
		return malformedInputAction;
	}

	public void setMalformedInputAction(CodingErrorActionEnum malformedInputAction) {
		this.malformedInputAction = malformedInputAction;
	}

	public CodingErrorActionEnum getUnmappableInputAction() {
		return unmappableInputAction;
	}

	public void setUnmappableInputAction(CodingErrorActionEnum unmappableInputAction) {
		this.unmappableInputAction = unmappableInputAction;
	}

	public MessageConstraints getMessageConstraints() {
		return messageConstraints;
	}

	public void setMessageConstraints(MessageConstraints messageConstraints) {
		this.messageConstraints = messageConstraints;
	}

}
