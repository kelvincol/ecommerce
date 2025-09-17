public class ItemPedido {
    private Produto produto;
    private int quantidade;
    private double precoVenda;

    public ItemPedido(Produto produto, int quantidade, double precoVenda) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoVenda = precoVenda;
    }

    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public double getPrecoVenda() { return precoVenda; }

    public double getTotal() {
        return quantidade * precoVenda;
    }

    @Override
    public String toString() {
        return String.format("Item{produto=%s, qtd=%d, preco=%.2f}", produto.getNome(), quantidade, precoVenda);
    }

    public String toLinhaArquivo() {
        return produto.getId() + ";" + quantidade + ";" + precoVenda;
    }
}
