package es.iesclaradelrey.dm2e.ut02.actividad.entities;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlayListTrack {

    private Integer playListTrackId;
    private Integer trackId;
    private String trackName;

    public PlayListTrack(Integer trackId, String trackName) {
        this.trackId = trackId;
        this.trackName = trackName;
    }
}
