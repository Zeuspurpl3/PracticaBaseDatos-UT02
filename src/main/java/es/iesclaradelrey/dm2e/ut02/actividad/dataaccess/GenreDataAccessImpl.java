package es.iesclaradelrey.dm2e.ut02.actividad.dataaccess;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.Genre;

import es.iesclaradelrey.dm2e.ut02.actividad.util.Pool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenreDataAccessImpl implements GenreDataAccess {

    @Override
    public List<Genre> findAll() {
        String sql = "SELECT genreid, name FROM genre ORDER BY genreid";
        List<Genre> result = new ArrayList<>();
        try (Connection conn = Pool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new Genre(rs.getInt("genreid"), rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando todos los géneros: " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<Genre> findByName(String name) {
        String sql = "SELECT genreid, name FROM genre WHERE LOWER(name) LIKE ? ORDER BY genreid";
        List<Genre> result = new ArrayList<>();
        try (Connection conn = Pool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new Genre(rs.getInt("genreid"), rs.getString("name")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando géneros por nombre: " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Optional<Genre> findById(int id) {
        String sql = "SELECT genreid, name FROM genre WHERE genreid = ?";
        try (Connection conn = Pool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Genre(rs.getInt("genreid"), rs.getString("name")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando género por id: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM genre WHERE genreid = ?";
        try (Connection conn = Pool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error comprobando existencia de género: " + e.getMessage(), e);
        }
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getGenreId() == null) {
            // inserta
            String sql = "INSERT INTO genre(name) VALUES(?)";
            try (Connection conn = Pool.getInstance().getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, genre.getName());
                int affected = ps.executeUpdate();
                if (affected == 0) throw new RuntimeException("No se pudo insertar el género.");
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        genre.setGenreId(keys.getInt(1));
                    }
                }
                return genre;
            } catch (SQLException e) {
                throw new RuntimeException("Error insertando género: " + e.getMessage(), e);
            }
        } else {
            // actualiza
            String sql = "UPDATE genre SET name = ? WHERE genreid = ?";
            try (Connection conn = Pool.getInstance().getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, genre.getName());
                ps.setInt(2, genre.getGenreId());
                int affected = ps.executeUpdate();
                if (affected == 0) throw new RuntimeException("No se encontró el género a actualizar.");
                return genre;
            } catch (SQLException e) {
                throw new RuntimeException("Error actualizando género: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM genre WHERE genreid = ?";
        try (Connection conn = Pool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando género: " + e.getMessage(), e);
        }
    }
}
