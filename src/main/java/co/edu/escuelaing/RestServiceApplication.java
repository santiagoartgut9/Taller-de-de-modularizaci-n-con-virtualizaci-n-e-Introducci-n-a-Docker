package co.edu.escuelaing;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class RestServiceApplication {

    private static final int DEFAULT_PORT = 5000;
    private final HttpServer server;
    private final ExecutorService executor;

    public RestServiceApplication(int port, int threadPoolSize) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        // Manejar concurrentemente con un thread pool
        executor = Executors.newFixedThreadPool(threadPoolSize);
        server.setExecutor(executor);

        // Registrar handlers
        server.createContext("/greeting", new GreetingHandler());
        // Puedes agregar más contexts aquí
    }

    public void start() {
        server.start();
        System.out.printf("Server started on %s%n", server.getAddress());
        addShutdownHook();
    }

    private void addShutdownHook() {
        // Hook para apagado elegante
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown hook triggered: stopping server...");
            server.stop(1); // deja 1 segundo para conexiones actuales; ajusta según necesidad
            executor.shutdown(); // deja de aceptar nuevas tareas
            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.out.println("Forcing executor shutdown...");
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted during shutdown, forcing shutdownNow");
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            System.out.println("Server stopped gracefully.");
        }, "graceful-shutdown-hook"));
    }

    public static void main(String[] args) throws IOException {
        int port = getPortFromEnvOrDefault();
        int threads = getThreadsFromEnvOrDefault();
        RestServiceApplication app = new RestServiceApplication(port, threads);
        app.start();
    }

    private static int getPortFromEnvOrDefault() {
        String p = System.getenv("PORT");
        if (p != null) {
            try {
                return Integer.parseInt(p);
            } catch (NumberFormatException ignored) {}
        }
        return DEFAULT_PORT;
    }

    private static int getThreadsFromEnvOrDefault() {
        String t = System.getenv("HTTP_THREADS");
        if (t != null) {
            try {
                return Integer.parseInt(t);
            } catch (NumberFormatException ignored) {}
        }
        return Math.max(2, Runtime.getRuntime().availableProcessors()); // default
    }
}
