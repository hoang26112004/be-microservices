package com.bemicroservices.user_service.constant;



public interface Endpoint {
    String PREFIX = "/api/v1";

    public interface User {
        String PREFIX = Endpoint.PREFIX + "/users";
        String ME = "/me";
        String UPDATE = "/{addressId}";
        String UPLOAD = "/upload";
    }

    public interface Profile {
        String PREFIX = Endpoint.PREFIX + "/profiles";
        String GET_BY_ID = "/{profileId}";
        String UPDATE_BY_ID = "/{profileId}";
    }

    public interface Address {
        String PREFIX = Endpoint.PREFIX + "/addresses";
        String ADDRESS_ID = "/{addressId}";
    }
}
