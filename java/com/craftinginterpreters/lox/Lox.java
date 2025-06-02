package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        /*
         * Esta función puede lanzar IOException si falla el acceso al archivo.
         * No lo gestiona automáticamente, pero obliga al llamador a manejarlo.
         */

        byte[] bytes = Files.readAllBytes(Paths.get(path));
        // Lee todo el archivo como arreglo de bytes (equiv. a malloc + fread en C).

        run(new String(bytes, Charset.defaultCharset()));
        /*
         * new reserva memoria en el heap para una nueva instancia de String.
         * El GC liberará esa memoria cuando ya no se necesite.
         */

        // Indicate an error in the exit code.
        if (hadError) {
            System.exit(65);
        }
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        // Crea un lector de caracteres desde System.in (similar a stdin en C).
        // Esto traduce bytes en caracteres según la codificación.

        BufferedReader reader = new BufferedReader(input);
        // Agrega un buffer al lector para poder usar readLine().
        // Es como FILE* en C, pero con soporte para strings multibyte y sin \n
        // manuales.

        for (;;) {
            System.out.print("> ");

            String line = reader.readLine();
            // Lee una línea de la entrada estándar (bloqueante).
            // Si el usuario aprieta Ctrl+D (EOF), readLine() devuelve null.

            if (line == null)
                break;

            run(line);
            hadError = false;
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        // Crea un analizador léxico con el código fuente como input.
        // Scanner no es java.util.Scanner.

        List<Token> tokens = scanner.scanTokens();
        // Llama al método que genera la lista de tokens (lexer completo).

        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
