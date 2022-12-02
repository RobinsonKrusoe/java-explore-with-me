package ru.practicum.explore.ewm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.client.RestTemplate;

import ru.practicum.explore.ewm.dto.HitDto;
import ru.practicum.explore.ewm.dto.ViewDto;
import ru.practicum.explore.ewm.model.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class StatsServiceImpl implements StatsService {
    private final RestTemplate rest;

    @Autowired
    public StatsServiceImpl(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        rest = builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl)).build();
    }

    /**
     * @param hitDto
     * @return
     */
    @Override
    public HitDto add(HitDto hitDto) {
        return rest.postForObject("/hit", hitDto, HitDto.class);
    }

    /**
     * @param start
     * @param end
     * @param uris
     * @param unique
     * @return
     */
    @Override
    public Collection<ViewDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique)
            throws JsonProcessingException {
        StringBuilder sb = new StringBuilder();
        sb.append("/stats?start=").append(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
          .append("&end=").append(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        if (uris != null) {
            sb.append("&uris=").append(String.join(",", uris));
        }

        if (unique != null) {
            sb.append("&unique=").append(unique);
        }

        ResponseEntity<ViewDto[]> response = rest.getForEntity(sb.toString(), ViewDto[].class);

        List<ViewDto> views = List.of(response.getBody());

        return views;
    }

    @Override
    public Event fillViews(Event event) {
        try {
            List<ViewDto> stat = new ArrayList<>(findStats(LocalDateTime.now().minusDays(100),
                    LocalDateTime.now(),
                    List.of("/events/" + event.getId()),
                    true));

            if (stat.size() > 0) {
                ViewDto view = stat.get(0);
                event.setViews(view.getHits());
            }
        } catch (JsonProcessingException e) {
            return event;
        }

        return event;
    }
}
