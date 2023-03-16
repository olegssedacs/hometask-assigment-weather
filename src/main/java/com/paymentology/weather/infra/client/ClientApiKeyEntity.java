package com.paymentology.weather.infra.client;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "client_api_keys")
public class ClientApiKeyEntity {

    @Id
    @Column(name = "id")
    Long id;

    @Column(name = "api_key")
    String apiKey;

    @Column(name = "revoked")
    boolean revoked;

    @Column(name = "created_at")
    Instant createdAt;
}
