package com.spring.jpastudy.event.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.jpastudy.event.entity.Event;
import lombok.*;

import javax.annotation.processing.Generated;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventSaveDto {

    private String title;
    private String desc;
    private String imageUrl;
    @JsonFormat(pattern = "yyy-MM-dd")
    private LocalDate beginDate;

    // 엔터티로 변경하는 유틸 메서드
    public Event toEntity() {
        return Event.builder()
                .title(this.title)
                .description(this.desc)
                .image(this.imageUrl)
                .date(this.beginDate)
                .build();
    }
}
