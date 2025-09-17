import java.util.*;

public class ProdutoService {
    private String arquivo;

    public ProdutoService(String arquivo) {
        this.arquivo = arquivo;
    }

    public Produto criarProduto(String nome, double preco) {
        Produto p = new Produto(UUID.randomUUID(), nome, preco);
        ArquivoUtil.salvarLinha(arquivo, p.toLinhaArquivo());
        return p;
    }

    public List<Produto> listarProdutos() {
        List<String> linhas = ArquivoUtil.lerLinhas(arquivo);
        List<Produto> produtos = new ArrayList<>();
        for (String linha : linhas) {
            produtos.add(Produto.fromLinhaArquivo(linha));
        }
        return produtos;
    }

    public Produto buscarPorId(UUID id) {
        return listarProdutos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Produto buscarPorNome(String nome) {
        return listarProdutos().stream()
                .filter(p -> p.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }
}
