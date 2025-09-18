import java.io.*;
import java.util.*;

public class ProdutoService {
    private static final String ARQUIVO_PRODUTOS = "produtos.csv";

    public ProdutoService() {
        inicializarArquivo();
    }

    private void inicializarArquivo() {
        File file = new File(ARQUIVO_PRODUTOS);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("id;nome;preco");
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void salvar(Produto produto) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_PRODUTOS, true))) {
            writer.write(produto.getId() + ";" + produto.getNome() + ";" + produto.getPreco());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Produto> listar() {
        List<Produto> produtos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_PRODUTOS))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }
                String[] partes = linha.split(";");
                Produto produto = new Produto(UUID.fromString(partes[0]), partes[1], Double.parseDouble(partes[2]));
                produtos.add(produto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return produtos;
    }

    public Produto buscarPorNome(String nome) {
        return listar().stream()
                .filter(p -> p.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    public void atualizar(Produto produtoAtualizado) {
        List<Produto> produtos = listar();
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getId().equals(produtoAtualizado.getId())) {
                produtos.set(i, produtoAtualizado);
                break;
            }
        }
        salvarTodos(produtos);
    }

    private void salvarTodos(List<Produto> produtos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_PRODUTOS))) {
            writer.write("id;nome;preco");
            writer.newLine();
            for (Produto p : produtos) {
                writer.write(p.getId() + ";" + p.getNome() + ";" + p.getPreco());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
