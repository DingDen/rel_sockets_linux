// Client.java
import java.io.*;
import java.net.*;

public class Client {
  public static void main(String[] args) throws Exception {
    try (Socket s = new Socket("127.0.0.1", 6000);
         BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
         BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {

      out.write("public class Main {\n");
      out.write("  public static void main(String[] args){ System.out.println(\"Oi\"); }\n");
      out.write("}\n");
      out.write("__END__\n");
      out.flush();

      String line;
      while ((line = in.readLine()) != null) {
        System.out.println(line);
      }
    }
  }
}
