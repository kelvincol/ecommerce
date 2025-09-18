import java.io.*;
import java.util.*;

public class ClienteService {
    private static final String ARQUIVO_CLIENTES = "clientes.csv";

    public ClienteService() {
        inicializarArquivo();
    }

    private void inicializarArquivo() {
        File file = new File(ARQUIVO_CLIENTES);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("id;nome;documento");
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void salvar(Cliente cliente) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CLIENTES, true))) {
            writer.write(cliente.getId() + ";" + cliente.getNome() + ";" + cliente.getDocumento() + ";" + cliente.getEmail());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CLIENTES))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = reader.readLine()) != null) {
                if (primeiraLinha) { // pula o cabeçalho
                    primeiraLinha = false;
                    continue;
                }
                String[] partes = linha.split(";");
                UUID id = UUID.fromString(partes[0]);
                String nome = partes[1];
                String documento = partes[2];
                String email = partes[3];
                clientes.add(new Cliente(id, nome, documento, email));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public Cliente buscarPorNome(String nome) {
        return listar().stream()
                .filter(c -> c.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    public void atualizar(Cliente clienteAtualizado) {
        List<Cliente> clientes = listar();
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId().equals(clienteAtualizado.getId())) {
                clientes.set(i, clienteAtualizado);
                break;
            }
        }
        salvarTodos(clientes);
    }

    private void salvarTodos(List<Cliente> clientes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CLIENTES))) {
            writer.write("id;nome;documento;email");
            writer.newLine();
            for (Cliente c : clientes) {
                writer.write(c.getId() + ";" + c.getNome() + ";" + c.getDocumento() + ";" + c.getEmail());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}