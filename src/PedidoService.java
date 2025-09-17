import java.util.*;

public class PedidoService {
    private String arquivo;
    private ClienteService clienteService;
    private ProdutoService produtoService;

    public PedidoService(String arquivo, ClienteService clienteService, ProdutoService produtoService) {
        this.arquivo = arquivo;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    public Pedido criarPedido(Cliente cliente) {
        return new Pedido(UUID.randomUUID(), cliente);
    }

    public void salvarPedido(Pedido pedido) {
        ArquivoUtil.salvarLinha(arquivo, pedido.toLinhaArquivo());
    }

    public Pedido buscarPorId(UUID id) {
        List<String> linhas = ArquivoUtil.lerLinhas(arquivo);
        for (String linha : linhas) {
            String[] partes = linha.split(";");
            UUID pedidoId = UUID.fromString(partes[0]);
            if (pedidoId.equals(id)) {
                Cliente cliente = clienteService.buscarPorId(UUID.fromString(partes[1]));
                String status = partes[2];
                double totalArquivo = Double.parseDouble(partes[3]);
                String criadoEm = partes[4];

                Pedido pedido = new Pedido(pedidoId, cliente);
                // sobrescreve o status e data de criação salvos
                pedido.setStatus(status);
                pedido.setCriadoEm(criadoEm);

                // reconstruir os itens
                for (int i = 5; i < partes.length; i += 3) {
                    UUID produtoId = UUID.fromString(partes[i]);
                    int qtd = Integer.parseInt(partes[i + 1]);
                    double precoVenda = Double.parseDouble(partes[i + 2]);

                    Produto produto = produtoService.buscarPorId(produtoId);
                    if (produto != null) {
                        pedido.adicionarItem(produto, qtd, precoVenda);
                    }
                }

                // recalcula total de verdade
                pedido.recalcularTotal();

                return pedido;
            }
        }
        return null;
    }

    public List<Pedido> listarPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        List<String> linhas = ArquivoUtil.lerLinhas(arquivo);

        for (String linha : linhas) {
            String[] partes = linha.split(";");
            UUID pedidoId = UUID.fromString(partes[0]);
            Cliente cliente = clienteService.buscarPorId(UUID.fromString(partes[1]));
            String status = partes[2];
            double totalArquivo = Double.parseDouble(partes[3]);
            String criadoEm = partes[4];

            Pedido pedido = new Pedido(pedidoId, cliente);
            pedido.setStatus(status);
            pedido.setCriadoEm(criadoEm);


            pedido.setTotal(totalArquivo);

            pedidos.add(pedido);
        }
        return pedidos;
    }
}