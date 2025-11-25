package es.iesclaradelrey.dm2e.ut02.actividad;


import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.GenreDataAccess;
import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.GenreDataAccessImpl;
import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.PlayListDataAccess;
import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.PlayListDataAccessImpl;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.Genre;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayList;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayListTrack;
import es.iesclaradelrey.dm2e.ut02.actividad.services.GenreService;
import es.iesclaradelrey.dm2e.ut02.actividad.services.GenreServiceImpl;
import es.iesclaradelrey.dm2e.ut02.actividad.services.PlayListService;
import es.iesclaradelrey.dm2e.ut02.actividad.services.PlayListServiceImpl;
import es.iesclaradelrey.dm2e.ut02.actividad.util.Pool;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static GenreService genreService;
    private static PlayListService playListService;

    public static void main(String[] args) {
        GenreDataAccess genreDataAccess = new GenreDataAccessImpl();
        genreService = new GenreServiceImpl(genreDataAccess);
        PlayListDataAccess  playListDataAccess = new PlayListDataAccessImpl();
        playListService = new PlayListServiceImpl(playListDataAccess);

        runMenu();
    }

    private static void runMenu() {
        Scanner sc = new Scanner(System.in);
        int option;

        do {
            printMenu();
            System.out.print("Seleccione una opción: ");

            try {
                option = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                option = -1;
            }

            switch (option) {
                case 1 -> listAllGenres();
                case 2 -> findGenreById(sc);
                case 3 -> findGenresByName(sc);
                case 4 -> createGenre(sc);
                case 5 -> updateGenre(sc);
                case 6 -> deleteGenre(sc);
                case 7 -> createPlaylist(sc);
                case 8 -> findPlaylistById(sc);
                case 9 -> deletePlaylist(sc);
                case 0 -> { System.out.println("Saliendo..."); return; }
                default -> System.out.println("Opción no reconocida.");
            }

        } while (option != 0);
    }


    private static void printMenu() {
        System.out.println("\nGESTIÓN DE GÉNEROS Y LISTAS DE REPRODUCCIÓN");
        System.out.println("-------------------------------------------------------");
        System.out.println("1. Buscar todos los géneros");
        System.out.println("2. Buscar género por ID");
        System.out.println("3. Buscar géneros por nombre");
        System.out.println("4. Crear un nuevo género");
        System.out.println("5. Modificar un género existente");
        System.out.println("6. Eliminar un género por ID");
        System.out.println("7. Crear nueva lista de reproducción");
        System.out.println("8. Buscar lista de reproducción por ID");
        System.out.println("9. Eliminar lista de reproducción por ID");
        System.out.println("0. Salir");
    }

    private static int readInt(Scanner sc, String prompt, int min, int max) {
        int value;
        boolean ok = false;

        while (!ok) {
            System.out.print(prompt);
            String line = sc.nextLine();

            try {
                value = Integer.parseInt(line.trim());
                if (value < min || value > max) {
                    System.out.println("Valor fuera de rango. Intenta de nuevo.");
                } else {
                    ok = true;
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Debe ser un número.");
            }
        }

        return min;
    }

// ----------------------------------------------------------
    private static void listAllGenres() {
        List<Genre> list = genreService.findAll();

        if (list.size() == 0) {
            System.out.println("No hay géneros.");
        } else {
            for (Genre g : list) {
                System.out.println(g.toString());
            }
        }
    }

// ----------------------------------------------------------

    private static void createGenre(Scanner sc) {
        System.out.print("Nombre del nuevo género: ");
        String name = sc.nextLine();

        Genre g = new Genre(name);
        Genre created = genreService.save(g);

        System.out.println("Se ha creado el género " + created.getName() + " con id " + created.getGenreId());

    }

// ----------------------------------------------------------

    private static void findGenreById(Scanner sc) {
        int id = readInt(sc, "Introduzca id: ", 1, Integer.MAX_VALUE);
        Optional<Genre> opt = genreService.findById(id);

        if (opt.isPresent()) {
            Genre g = opt.get();
            System.out.println("El género con id " + id + " es " + g.getName());
        } else {
            System.out.println("No se encuentra el género con id " + id);
        }
    }

// ----------------------------------------------------------

    private static void findGenresByName(Scanner sc) {
        System.out.print("Introduzca texto a buscar: ");
        String text = sc.nextLine();

        List<Genre> list = genreService.findByName(text);

        if (list.size() == 0) {
            System.out.println("No se encuentran géneros que contengan " +
                    text + " en el nombre.");
        } else {
            for (Genre g : list) {
                System.out.println(g.toString());
            }
        }
    }

// ----------------------------------------------------------

    private static void updateGenre(Scanner sc) {
        int id = readInt(sc, "Introduzca id del género a modificar: ", 1, Integer.MAX_VALUE);

        if (!genreService.existsById(id)) {
            System.out.println("No existe el género con id " + id);
            return;
        }

        System.out.print("Introduzca nuevo nombre: ");
        String nuevoNombre = sc.nextLine();

        Genre antiguo = genreService.findById(id).get();
        String nombreViejo = antiguo.getName();

        Genre nuevo = new Genre(id, nuevoNombre);
        Genre actualizado = genreService.save(nuevo);

        System.out.println("Se ha modificado el género " + nombreViejo +
                ". Su nuevo nombre es " + actualizado.getName());
    }

// ----------------------------------------------------------

    private static void deleteGenre(Scanner sc) {
        int id = readInt(sc, "Introduzca id del género a eliminar: ", 1, Integer.MAX_VALUE);

        if (!genreService.existsById(id)) {
            System.out.println("No existe el género con id " + id);
            return;
        }

        boolean ok = genreService.delete(id);

        if (ok) {
            System.out.println("Se ha eliminado el género con id " + id);
        } else {
            System.out.println("No se pudo eliminar el género con id " + id);
        }
    }

// ----------------------------------------------------------

    private static void createPlaylist(Scanner sc) {
        System.out.print("Nombre de la playlist: ");
        String name = sc.nextLine();

        System.out.print("IDs de tracks separados por coma: ");
        String entrada = sc.nextLine().replace(" ", "");

        List<PlayListTrack> tracks = new ArrayList<>();

        if (!entrada.isBlank()) {
            String[] partes = entrada.split(",");
            for (String p : partes) {
                try {
                    int trackId = Integer.parseInt(p);
                    tracks.add(
                            PlayListTrack.builder()
                                    .trackId(trackId)
                                    .build()
                    );
                } catch (NumberFormatException e) {
                    System.out.println("ID inválido ignorado: " + p);
                }
            }
        }

        PlayList pl = PlayList.builder()
                .name(name)
                .playListTracks(tracks)
                .build();

        PlayList created = playListService.save(pl);

        System.out.println("Playlist creada con ID " + created.getPlayListId());
    }
// ----------------------------------------------------
    private static void findPlaylistById(Scanner sc) {
        System.out.print("ID de playlist: ");
        int id = Integer.parseInt(sc.nextLine());

        Optional<PlayList> opt = playListService.findById(id);

        if (opt.isEmpty()) {
            System.out.println("No existe esa playlist.");
            return;
        }

        PlayList pl = opt.get();
        System.out.println("Playlist: " + pl.getName());
        System.out.println("Tracks:");
        pl.getPlayListTracks().forEach(
                t -> System.out.println(t.getTrackId() + " - " + t.getTrackName())
        );
    }
// ----------------------------------------------------------
    private static void deletePlaylist(Scanner sc) {
        System.out.print("ID de playlist a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());

        boolean ok = playListService.delete(id);

        System.out.println(ok ? "Playlist eliminada." : "No existe esa playlist.");
    }
}