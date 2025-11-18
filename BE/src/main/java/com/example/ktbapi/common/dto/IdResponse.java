package com.example.ktbapi.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IdResponse(
        @JsonProperty("id") Long id
) {}
