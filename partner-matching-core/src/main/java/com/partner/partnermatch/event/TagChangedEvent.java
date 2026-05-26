package com.partner.partnermatch.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TagChangedEvent extends ApplicationEvent {
    private final Long userId;

    public TagChangedEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
    }
}
