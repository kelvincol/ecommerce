import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class PedidoService {
    private static final String ARQUIVO_PEDIDOS = "pedidos.csv";

    public PedidoService() {
        inicializarArquivo();
    }

    private void inicializarArquivo() {
        File file = new File(ARQUIVO_PEDIDOS);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("id;idCliente;dataCriacao;status;valorTotal;itens");
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void salvar(Pedido pedido) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_PEDIDOS, true))) {
            // itens no formato: idProduto:quantidade:preco | idProduto:quantidade:preco ...
            String itensStr = pedido.getItens().stream()
                    .map(i -> i.getProduto().getId() + ":" + i.getQuantidade() + ":" + i.getPrecoVenda())
                    .reduce((a, b) -> a + "|" + b)
                    .orElse("");

            writer.write(pedido.getId() + ";" +
                    pedido.getCliente().getId() + ";" +
                    pedido.getCriadoEm() + ";" +
                    pedido.getStatus() + ";" +
                    pedido.getTotal() + ";" +
                    itensStr);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Pedido> listar(ClienteService clienteService, ProdutoService produtoService) {
        List<Pedido> pedidos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_PEDIDOS))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }
                String[] partes = linha.split(";");
                UUID id = UUID.fromString(partes[0]);
                UUID clienteId = UUID.fromString(partes[1]);
                LocalDateTime criadoEm = LocalDateTime.parse(partes[2]);
                StatusPedido status = StatusPedido.valueOf(partes[3]);
                double total = Double.parseDouble(partes[4]);
                String itensStr = partes.length > 5 ? partes[5] : "";

                Cliente cliente = clienteService.listar().stream()
                        .filter(c -> c.getId().equals(clienteId))
                        .findFirst().orElse(null);

                Pedido pedido = new Pedido(id, cliente, criadoEm, status);

                if (!itensStr.isEmpty()) {
                    String[] itensArray = itensStr.split("\\|");
                    for (String i : itensArray) {
                        String[] campos = i.split(":");
                        UUID produtoId = UUID.fromString(campos[0]);
                        int qtd = Integer.parseInt(campos[1]);
                        double precoVenda = Double.parseDouble(campos[2]);

                        Produto produto = produtoService.listar().stream()
                                .filter(p -> p.getId().equals(produtoId))
                                .findFirst().orElse(null);

                        if (produto != null) {
                            pedido.adicionarItem(produto, qtd, precoVenda);
                        }
                    }
                }
                pedidos.add(pedido);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pedidos;
    }
}