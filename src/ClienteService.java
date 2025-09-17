import java.util.*;

public class ClienteService {
    private String arquivo;

    public ClienteService(String arquivo) {
        this.arquivo = arquivo;
    }

    public Cliente criarCliente(String nome, String doc, String email) {
        Cliente c = new Cliente(UUID.randomUUID(), nome, doc, email);
        ArquivoUtil.salvarLinha(arquivo, c.toLinhaArquivo());
        return c;
    }

    public List<Cliente> listarClientes() {
        List<String> linhas = ArquivoUtil.lerLinhas(arquivo);
        List<Cliente> clientes = new ArrayList<>();
        for (String linha : linhas) {
            clientes.add(Cliente.fromLinhaArquivo(linha));
        }
        return clientes;
    }

    public Cliente buscarPorId(UUID id) {
        return listarClientes().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Cliente buscarPorNome(String nome) {
        return listarClientes().stream()
                .filter(c -> c.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }
}