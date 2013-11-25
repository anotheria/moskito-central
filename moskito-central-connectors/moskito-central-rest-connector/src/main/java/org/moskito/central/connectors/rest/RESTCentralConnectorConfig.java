package org.moskito.central.connectors.rest;

import org.configureme.annotations.ConfigureMe;

/**
 * REST config bean.
 * 
 * @author dagafonov
 * 
 */
@ConfigureMe(allfields = true)
public class RESTCentralConnectorConfig {

	/**
	 * HTTP server host.
	 */
	private String host;

	/**
	 * HTTP server port.
	 */
	private int port;

	/**
	 * HTTP server path.
	 */
	private String resourcePath;

    /**
     * Is HTTP basic auth enabled.
     */
    private boolean basicAuthEnabled;

    /**
     * Basic auth login.
     */
    private String login;

    /**
     * Basic auth password.
     */
    private String password;


	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

    public boolean isBasicAuthEnabled() {
        return basicAuthEnabled;
    }

    public void setBasicAuthEnabled(boolean basicAuthEnabled) {
        this.basicAuthEnabled = basicAuthEnabled;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RESTCentralConnectorConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", resourcePath='" + resourcePath + '\'' +
                ", basicAuthEnabled=" + basicAuthEnabled +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
