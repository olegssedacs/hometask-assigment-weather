package com.paymentology.weather.domain.client;

import java.util.Optional;

public interface ClientKeyRepository {

    Optional<ClientApiKey> find(String apiKey);

}
