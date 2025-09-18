import java.util.UUID;

public class Produto {
    private UUID id;
    private String nome;
    private double preco;

    public Produto(UUID id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório.");
        }
        this.nome = nome;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }
        this.preco = preco;
    }

    @Override
    public String toString() {
        return String.format("Produto{id=%s, nome='%s', preco=%.2f}", id, nome, preco);
    }

    public String toLinhaArquivo() {
        return id + ";" + nome + ";" + preco;
    }

    public static Produto fromLinhaArquivo(String linha) {
        String[] partes = linha.split(";");
        return new Produto(UUID.fromString(partes[0]), partes[1], Double.parseDouble(partes[2]));
    }
}
