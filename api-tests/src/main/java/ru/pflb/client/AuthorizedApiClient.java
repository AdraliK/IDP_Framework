package ru.pflb.client;

public class AuthorizedApiClient extends BaseApiClient {

    public AuthorizedApiClient(String authToken) {
        super(authorizedSpec(authToken));
    }

}
