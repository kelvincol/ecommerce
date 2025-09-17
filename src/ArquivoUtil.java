import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ArquivoUtil {
    public static void salvarLinha(String nomeArquivo, String linha) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivo, true))) {
            bw.write(linha);
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar no arquivo: " + nomeArquivo, e);
        }
    }

    public static List<String> lerLinhas(String nomeArquivo) {
        try {
            Path path = Paths.get(nomeArquivo);
            if (!Files.exists(path)) return new ArrayList<>();
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo: " + nomeArquivo, e);
        }
    }
}