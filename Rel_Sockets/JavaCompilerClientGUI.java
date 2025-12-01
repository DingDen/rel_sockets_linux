import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class JavaCompilerClientGUI extends JFrame {

  private JTextArea codeArea;
  private JTextArea outputArea;

  public JavaCompilerClientGUI() {
    this.setTitle("Cliente — Compilador Java Remoto");
    this.setSize(800, 600);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());

    // Área de código
    this.codeArea = new JTextArea();
    this.codeArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
    JScrollPane codeScroll = new JScrollPane(this.codeArea);

    // Área de saída
    this.outputArea = new JTextArea();
    this.outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
    this.outputArea.setEditable(false);
    JScrollPane outputScroll = new JScrollPane(this.outputArea);

    JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, codeScroll, outputScroll);
    split.setDividerLocation(300);
    this.add(split, BorderLayout.CENTER);

    // Botões
    JPanel buttons = new JPanel();

    JButton runButton = new JButton("Compilar e Executar");
    runButton.addActionListener(e -> this.sendCode());

    JButton clearButton = new JButton("Limpar Saída");
    clearButton.addActionListener(e -> this.outputArea.setText(""));

    buttons.add(runButton);
    buttons.add(clearButton);

    this.add(buttons, BorderLayout.SOUTH);

    this.setVisible(true);
  }

  private void sendCode() {
    try (Socket socket = new Socket("localhost", 5000);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

      // Envia código linha a linha
      String[] linhas = this.codeArea.getText().split("\n");
      for (String l : linhas) {
        out.println(l);
      }
      out.println("__END__");

      // Recebe resposta
      StringBuilder resposta = new StringBuilder();
      String line;
      while ((line = in.readLine()) != null) {
        resposta.append(line).append("\n");
      }

      this.outputArea.setText(resposta.toString());

    } catch (Exception e) {
      this.outputArea.setText("Erro ao conectar ao servidor:\n" + e.getMessage());
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(JavaCompilerClientGUI::new);
  }
}
