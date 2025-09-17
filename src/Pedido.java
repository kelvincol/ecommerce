import java.time.LocalDateTime;
import java.util.*;

public class Pedido {
    private UUID id;
    private Cliente cliente;
    private List<ItemPedido> itens = new ArrayList<>();
    private String status;
    private double total;
    private LocalDateTime criadoEm; // <-- novo campo

    public Pedido(UUID id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.status = "ABERTO";
        this.criadoEm = LocalDateTime.now(); // registra a data de criação
    }

    public UUID getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public List<ItemPedido> getItens() { return itens; }
    public String getStatus() { return status; }
    public double getTotal() { return total; }
    public LocalDateTime getCriadoEm() { return criadoEm; } // <-- getter novo

    public void adicionarItem(Produto produto, int quantidade, double precoVenda) {
        itens.add(new ItemPedido(produto, quantidade, precoVenda));
    }

    public void finalizarPedido() {
        this.total = itens.stream().mapToDouble(ItemPedido::getTotal).sum();
        this.status = "FINALIZADO";
    }

    public String toLinhaArquivo() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(";")
                .append(cliente.getId()).append(";")
                .append(status).append(";")
                .append(total).append(";")
                .append(criadoEm); // grava a data também
        for (ItemPedido item : itens) {
            sb.append(";").append(item.toLinhaArquivo());
        }
        return sb.toString();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCriadoEm(String criadoEmStr) {
        this.criadoEm = LocalDateTime.parse(criadoEmStr);
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void recalcularTotal() {
        this.total = itens.stream().mapToDouble(ItemPedido::getTotal).sum();
    }
}

