package com.felipesantos.livetrack.dto;

public record TrackingEventDTO(Long id, Long trackingId, Double latitude, Double longitude, String eventTime) {

}
