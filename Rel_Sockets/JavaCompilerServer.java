import java.io.*;
import java.net.*;
import java.nio.file.*;

public class JavaCompilerServer {

  public static void main(String[] args) {
    int port = 5000;

    System.out.println("Servidor Java rodando na porta " + port);

    try (ServerSocket serverSocket = new ServerSocket(port)) {

      while (true) {
        Socket socket = serverSocket.accept();
        System.out.println("Cliente conectado!");

        new Thread(() -> JavaCompilerServer.handleClient(socket)).start();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void handleClient(Socket socket) {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

      // 1) Receber código enviado pelo cliente
      StringBuilder builder = new StringBuilder();
      String line;

      while ((line = in.readLine()) != null && !line.equals("__END__")) {
        builder.append(line).append("\n");
      }

      String source = builder.toString();

      // 2) Salvar em arquivo
      Files.writeString(Path.of("Program.java"), source);

      // 3) Compilar
      Process compile = Runtime.getRuntime().exec("javac Program.java");
      compile.waitFor();

      String compileErrors = new String(compile.getErrorStream().readAllBytes());

      if (!compileErrors.isEmpty()) {
        out.println("ERRO DE COMPILAÇÃO:\n" + compileErrors);
        return;
      }

      // 4) Executar
      Process run = Runtime.getRuntime().exec("java Program");
      run.waitFor();

      String output = new String(run.getInputStream().readAllBytes());
      String runtimeErrors = new String(run.getErrorStream().readAllBytes());

      if (!runtimeErrors.isEmpty()) {
        out.println("ERRO EM TEMPO DE EXECUÇÃO:\n" + runtimeErrors);
      } else {
        out.println("SAÍDA DO PROGRAMA:\n" + output);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
