package es.iesclaradelrey.dm2e.ut02.actividad.dataaccess;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayList;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayListTrack;
import es.iesclaradelrey.dm2e.ut02.actividad.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class PlayListDataAccessImpl implements PlayListDataAccess {

    @Override
    public Optional<PlayList> findById(int id) {
        String sqlPlayList = "SELECT playlist_id, name FROM playlist WHERE playlist_id = ?";
        String sqlTracks = "SELECT pt.playlist_id, pt.track_id, t.name AS track_name " +
                "FROM playlist_track pt " +
                "JOIN track t ON pt.track_id = t.track_id " +
                "WHERE pt.playlist_id = ?";

        try(Connection conn = ConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlPlayList)){

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()){
                if(!rs.next()){
                    return Optional.empty();
                }

                PlayList playList = PlayList.builder()
                        .playListId(rs.getInt("playlist_id"))
                        .name(rs.getString("name"))
                        .playListTracks(new ArrayList<>())
                        .build();

                try(PreparedStatement pstmt = conn.prepareStatement(sqlTracks)) {
                    pstmt.setInt(1, id);

                    try(ResultSet rsTracks = pstmt.executeQuery()){
                        while(rsTracks.next()){
                            PlayListTrack track = PlayListTrack.builder()
                                    .playListTrackId(rsTracks.getInt("playlist_track_id"))
                                    .trackId(rsTracks.getInt("track_id"))
                                    .trackName(rsTracks.getString("track_name"))
                                    .build();
                        }
                    }
                }
                return Optional.of(playList);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los datos de la playlist", e);
        }
    }

    @Override
    public PlayList save(PlayList playlist) {
        String sqlInsertPlaylist = "INSERT INTO playlist (name) VALUES (?)";
        String sqlInsertTrack = "INSERT INTO playlist_track (playlist_id, track_id) VALUES (?, ?)";

        try (Connection conn = ConnectionPool.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            // Insertar playlist
            try (PreparedStatement stmt = conn.prepareStatement(sqlInsertPlaylist, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, playlist.getName());
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        playlist.setPlayListId(generatedKeys.getInt(1));
                    } else {
                        conn.rollback();
                        throw new RuntimeException("No se pudo obtener el ID generado para la playlist");
                    }
                }
            }

            // Insertar tracks asociados
            try (PreparedStatement stmtTracks = conn.prepareStatement(sqlInsertTrack, Statement.RETURN_GENERATED_KEYS)) {
                for (PlayListTrack track : playlist.getPlayListTracks()) {
                    stmtTracks.setInt(1, playlist.getPlayListId());
                    stmtTracks.setInt(2, track.getTrackId());
                    stmtTracks.executeUpdate();

                    // Obtener playlist_track_id generado
                    try (ResultSet rsKeys = stmtTracks.getGeneratedKeys()) {
                        if (rsKeys.next()) {
                            track.setPlayListTrackId(rsKeys.getInt(1));
                        }
                    }
                }
            }

            conn.commit();
            return playlist;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la playlist " + playlist.getName(), e);
        }


    }

    @Override
    public boolean delete(int id) {
        String sqlDeleteTracks = "DELETE FROM playlist_track WHERE playlist_id = ?";
        String sqlDeletePlaylist = "DELETE FROM playlist WHERE playlist_id = ?";

        try (Connection conn = ConnectionPool.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            // Borrar tracks asociados
            try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteTracks)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            // Borrar playlist
            int rowsDeleted;
            try (PreparedStatement stmt = conn.prepareStatement(sqlDeletePlaylist)) {
                stmt.setInt(1, id);
                rowsDeleted = stmt.executeUpdate();
            }

            if (rowsDeleted == 0) {
                conn.rollback();
                return false; // no existía la playlist
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar la playlist con id " + id, e);
        }
    }


}
