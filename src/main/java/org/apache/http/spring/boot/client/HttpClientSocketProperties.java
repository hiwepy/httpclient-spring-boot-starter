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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TODO
 * 
 * @author ： <a href="https://github.com/vindell">vindell</a>
 */
@ConfigurationProperties(prefix = HttpClientSocketProperties.PREFIX)
public class HttpClientSocketProperties {

	public final static String PREFIX = "httpclient.socket";
	
	/**
	 * 连接读取数据超时时间；单位毫秒，默认5000
	 */
	private int soTimeout = Integer.parseInt(HttpClientParams.HTTP_SOCKET_SO_TIMEOUT.getDefault());
	private boolean soReuseAddress;
	private int soLinger;
	private boolean soKeepAlive;
	/**
	 * 设置httpclient是否使用NoDelay策略。如果启用了NoDelay策略，httpclient和站点之间传输数据时将会尽可能及时地将发送缓冲区中的数据发送出去、
	 * 而不考虑网络带宽的利用率，这个策略适合对实时性要求高的场景。而禁用了这个策略之后，数据传输会采用Nagle's
	 * algorithm发送数据，该算法会充分顾及带宽的利用率，而不是数据传输的实时性
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
