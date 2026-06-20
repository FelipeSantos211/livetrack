package com.felipesantos.livetrack.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.felipesantos.livetrack.model.TrackingEvent;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TrackingWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendLocation(Long trackingId, TrackingEvent event) {

        messagingTemplate.convertAndSend(
                "/topic/tracking/" + trackingId,
                event
        );
    }
}
