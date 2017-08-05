package com.golomonitor.externalservices.utils;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Created by abelov on 05/08/17.
 */
public class JerseyClientCreator {
    private static ClientConfig config;

    private static Client client = ClientBuilder.newClient(getJacksonConfig());


    public static Client createClient() {
        return client;
    }

    private JerseyClientCreator() {
    }


    private static ClientConfig getJacksonConfig() {
        config = new ClientConfig();
        return config.register(JacksonJsonProvider.class);
    }
}
