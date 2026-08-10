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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for HttpClient sockets, bound to the {@code httpclient.socket.*} prefix.
 * <p>Maps directly to {@link org.apache.http.config.SocketConfig}.</p>
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = HttpClientSocketProperties.PREFIX)
public class HttpClientSocketProperties {

	public final static String PREFIX = "httpclient.socket";

	/** Socket read timeout in milliseconds; default is 5000. */
	private int soTimeout = Integer.parseInt(HttpClientParams.HTTP_SOCKET_SO_TIMEOUT.getDefault());
	private boolean soReuseAddress;
	private int soLinger;
	private boolean soKeepAlive;
	/**
	 * Whether HttpClient uses the NoDelay (TCP_NODELAY) strategy. When enabled, data is flushed from the
	 * send buffer as early as possible at the expense of bandwidth utilisation, which suits
	 * latency-sensitive scenarios. When disabled, data is sent using Nagle's algorithm, which favours
	 * bandwidth utilisation over latency.
	 */
	private boolean tcpNoDelay = Boolean.parseBoolean(HttpClientParams.HTTP_SOCKET_TCPNODELAY.getDefault());
	private int sndBufSize;
	private int rcvBufSize;
	private int backlogSize;
	
	public int getSoTimeout() {
		return soTimeout;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public boolean isSoReuseAddress() {
		return soReuseAddress;
	}

	public void setSoReuseAddress(boolean soReuseAddress) {
		this.soReuseAddress = soReuseAddress;
	}

	public int getSoLinger() {
		return soLinger;
	}

	public void setSoLinger(int soLinger) {
		this.soLinger = soLinger;
	}

	public boolean isSoKeepAlive() {
		return soKeepAlive;
	}

	public void setSoKeepAlive(boolean soKeepAlive) {
		this.soKeepAlive = soKeepAlive;
	}

	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public int getSndBufSize() {
		return sndBufSize;
	}

	public void setSndBufSize(int sndBufSize) {
		this.sndBufSize = sndBufSize;
	}

	public int getRcvBufSize() {
		return rcvBufSize;
	}

	public void setRcvBufSize(int rcvBufSize) {
		this.rcvBufSize = rcvBufSize;
	}

	public int getBacklogSize() {
		return backlogSize;
	}

	public void setBacklogSize(int backlogSize) {
		this.backlogSize = backlogSize;
	}

}
