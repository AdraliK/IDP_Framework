package ru.pflb.framework.client;

public class AuthorizedApiClient extends BaseApiClient {

    public AuthorizedApiClient(String authToken) {
        super(authorizedSpec(authToken));
    }

}
