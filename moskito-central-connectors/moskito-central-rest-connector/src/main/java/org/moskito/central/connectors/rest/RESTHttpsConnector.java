package org.moskito.central.connectors.rest;

import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.security.KeyStore;

/**
 * REST connector implementation to the Central with HTTPS support.
 *
 * @author Vladyslav Bezuhlyi
 */
public class RESTHttpsConnector extends RESTConnector {

    /**
     * Logger instance.
     */
    private final static Logger log = LoggerFactory.getLogger(RESTHttpsConnector.class);


    /**
     * Default constructor.
     */
    public RESTHttpsConnector() {
        super();
    }

    @Override
    protected ClientConfig getClientConfig() {
        ClientConfig clientConfig = super.getClientConfig();

        /* Adding HTTPS-specific properties to config */
        clientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, getHttpsProperties());

        return clientConfig;
    }

    private HTTPSProperties getHttpsProperties() {
        return new HTTPSProperties(getHostnameVerifier(), getSslContext());
    }

    /**
     * During handshaking, if the URL's hostname and the server's identification hostname mismatch,
     * the verification mechanism can call back to this verifier to make a decision.
     *
     * @return {@link javax.net.ssl.HostnameVerifier} implementation instance according to connector's config.
     */
    private HostnameVerifier getHostnameVerifier() {
        if (connectorConfig.isHostVerificationEnabled()) {
            return new BrowserCompatHostnameVerifier();
        }
        return new AllowAllHostnameVerifier();
    }

    /**
     * Builds {@link javax.net.ssl.SSLContext} instance according to connector's config.
     *
     * @return Configured {@link javax.net.ssl.SSLContext} instance or null.
     */
    private SSLContext getSslContext() {
        SSLContext sslContext = null;
        SSLContextBuilder builder = new SSLContextBuilder();
        File storeFile = null;
        FileInputStream storeStream = null;

        try {
            if (StringUtils.isNotEmpty(connectorConfig.getTrustStoreFilePath())) {
                storeFile = new File(connectorConfig.getTrustStoreFilePath());
            }

            if(storeFile.exists()) {
                storeStream = new FileInputStream(storeFile);
                KeyStore trustStore = KeyStore.getInstance("JKS");
                trustStore.load(storeStream, connectorConfig.getTrustStorePassword().toCharArray());

                if (connectorConfig.isTrustSelfSigned()) {
                    builder.loadTrustMaterial(trustStore, new TrustSelfSignedStrategy());
                } else {
                    builder.loadTrustMaterial(trustStore);
                }
            } else { /* default TrustStore will be used */
                if (connectorConfig.isTrustSelfSigned()) {
                    builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
                } else {
                    builder.loadTrustMaterial(null);
                }
            }

            sslContext = builder.useTLS().build();
        } catch (Exception e) {
            log.error("Error while initializing SSL context: " + e.getMessage(), e);
        } finally {
            if (storeStream != null) {
                try {
                    storeStream.close();
                } catch (IOException ignored) {}
            }
        }

        return sslContext;
    }

    @Override
    protected URI getBaseURI() {
        return UriBuilder.fromUri("https://" + connectorConfig.getHost() + connectorConfig.getResourcePath()).port(connectorConfig.getPort()).build();
    }

}
