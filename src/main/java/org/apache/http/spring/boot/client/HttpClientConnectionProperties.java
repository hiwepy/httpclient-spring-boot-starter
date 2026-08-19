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
import java.util.NoSuchElementException;

import org.apache.http.config.MessageConstraints;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = HttpClientConnectionProperties.PREFIX)
public class HttpClientConnectionProperties {

	public final static String PREFIX = "httpclient.connection";

	/**
	 * Enumeration mirroring {@link java.nio.charset.CodingErrorAction} values, resolved case-insensitively
	 * when binding connection properties.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
	 */
	public static enum CodingErrorActionEnum {

		IGNORE("IGNORE"),
		REPLACE("REPLACE"),
		REPORT("REPORT");

		private final String name;

		CodingErrorActionEnum(String name) {
			this.name = name;
		}

		/** Return the string value of this action. @return the action name */
		public String value() {
			return name;
		}

		/** Compare this enum to another instance. @param name the enum to compare @return true if equal */
		public boolean equals(CodingErrorActionEnum name){
			return this.compareTo(name) == 0;
		}

		/** Compare this enum to the action resolved from the given string. @param name the action name @return true if equal */
		public boolean equals(String name){
			return this.compareTo(CodingErrorActionEnum.valueOfIgnoreCase(name)) == 0;
		}

		/** Resolve an action case-insensitively by name. @param name the action name @return the matching enum */
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
	/** Gets the buffer size. */
	
	public int getBufferSize() {
		return bufferSize;
	}
	/** Sets the buffer size. */

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}
	/** Gets the fragment size hint. */

	public int getFragmentSizeHint() {
		return fragmentSizeHint;
	}
	/** Sets the fragment size hint. */

	public void setFragmentSizeHint(int fragmentSizeHint) {
		this.fragmentSizeHint = fragmentSizeHint;
	}
	/** Gets the charset. */

	public Charset getCharset() {
		return charset;
	}
	/** Sets the charset. */

	public void setCharset(Charset charset) {
		this.charset = charset;
	}
	/** Gets the malformed input action. */

	public CodingErrorActionEnum getMalformedInputAction() {
		return malformedInputAction;
	}
	/** Sets the malformed input action. */

	public void setMalformedInputAction(CodingErrorActionEnum malformedInputAction) {
		this.malformedInputAction = malformedInputAction;
	}
	/** Gets the unmappable input action. */

	public CodingErrorActionEnum getUnmappableInputAction() {
		return unmappableInputAction;
	}
	/** Sets the unmappable input action. */

	public void setUnmappableInputAction(CodingErrorActionEnum unmappableInputAction) {
		this.unmappableInputAction = unmappableInputAction;
	}
	/** Gets the message constraints. */

	public MessageConstraints getMessageConstraints() {
		return messageConstraints;
	}
	/** Sets the message constraints. */

	public void setMessageConstraints(MessageConstraints messageConstraints) {
		this.messageConstraints = messageConstraints;
	}

}
