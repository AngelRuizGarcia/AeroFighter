package org.arg.engine;

public enum GameStatus {
    MENU,       // Estado inicial: esperando a que el usuario pulse una tecla
    PLAYING,    // Jugando activamente
    PAUSED,     // Juego pausado en medio de la partida
    GAME_OVER   // Fin de partida
}
