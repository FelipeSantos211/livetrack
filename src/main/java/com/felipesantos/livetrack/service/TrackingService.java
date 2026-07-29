package com.felipesantos.livetrack.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.felipesantos.livetrack.dto.TrackingDTO;
import com.felipesantos.livetrack.dto.TrackingEventDTO;
import com.felipesantos.livetrack.model.Tracking;
import com.felipesantos.livetrack.model.TrackingEvent;
import com.felipesantos.livetrack.repository.TrackingEventRepository;
import com.felipesantos.livetrack.repository.TrackingRepository;
import com.felipesantos.livetrack.websocket.TrackingWebSocketController;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final TrackingEventRepository eventRepository;
    private final TrackingRepository trackingRepository;
    private final TrackingWebSocketController wsController;

    public TrackingDTO createTracking() {
        Tracking tracking = new Tracking();
        tracking.setStatus("ACTIVE");
        tracking.setCreatedAt(LocalDateTime.now());
        trackingRepository.save(tracking);
        return new TrackingDTO(
                tracking.getId(),
                tracking.getStatus(),
                tracking.getCreatedAt().toString());
    }

    public TrackingEventDTO addEvent(Long trackingId, TrackingEvent event) {
        Tracking tracking = trackingRepository.findById(trackingId).orElse(null);
        if (tracking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tracking not found");
        }

        event.setTracking(tracking);
        event.setEventTime(LocalDateTime.now());

        TrackingEvent saved = eventRepository.save(event);

        wsController.sendLocation(trackingId, saved);
        trackingRepository.save(tracking);

        return new TrackingEventDTO(
                saved.getId(),
                saved.getTracking().getId(),
                saved.getLatitude(),
                saved.getLongitude(),
                saved.getEventTime().toString());
    }

    public TrackingEventDTO getLastLocation(Long trackingId) {
        TrackingEvent lastEvent = eventRepository.findTopByTracking_IdOrderByEventTimeDesc(trackingId);
        if (lastEvent == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No events found for tracking ID: " + trackingId);
        }
        return new TrackingEventDTO(
                lastEvent.getId(),
                lastEvent.getTracking().getId(),
                lastEvent.getLatitude(),
                lastEvent.getLongitude(),
                lastEvent.getEventTime().toString());
    }

    public List<TrackingEventDTO> getHistory(Long trackingId) {
        List<TrackingEvent> events = eventRepository.findByTrackingIdOrderByEventTimeAsc(trackingId);
        if (events.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No events found for tracking ID: " + trackingId);
        }
        return events.stream().map(event -> new TrackingEventDTO(
                event.getId(),
                event.getTracking().getId(),
                event.getLatitude(),
                event.getLongitude(),
                event.getEventTime().toString())).toList();
    }
}
