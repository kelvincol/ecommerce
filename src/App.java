import java.util.*;

public class App {
    private static final Scanner sc = new Scanner(System.in);
    private static final ClienteService clienteService = new ClienteService("clientes.txt");
    private static final ProdutoService produtoService = new ProdutoService("produtos.txt");
    private static final PedidoService pedidoService = new PedidoService("pedidos.txt", clienteService, produtoService);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== MENU E-COMMERCE ===");
            System.out.println("1 - Cadastrar cliente");
            System.out.println("2 - Listar clientes");
            System.out.println("3 - Cadastrar produto");
            System.out.println("4 - Listar produtos");
            System.out.println("5 - Criar pedido");
            System.out.println("6 - Consultar pedido");
            System.out.println("7 - Listar pedidos");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            int opcao = Integer.parseInt(sc.nextLine());
            switch (opcao) {
                case 1 -> cadastrarCliente();
                case 2 -> listarClientes();
                case 3 -> cadastrarProduto();
                case 4 -> listarProdutos();
                case 5 -> criarPedido();
                case 6 -> consultarPedido();
                case 7 -> listarPedidos();
                case 0 -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void cadastrarCliente() {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Documento: ");
        String doc = sc.nextLine();
        System.out.print("E-mail: ");
        String email = sc.nextLine();
        Cliente c = clienteService.criarCliente(nome, doc, email);
        System.out.println("Cliente cadastrado: " + c);
    }

    private static void listarClientes() {
        clienteService.listarClientes().forEach(System.out::println);
    }

    private static void cadastrarProduto() {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Preço: ");
        double preco = Double.parseDouble(sc.nextLine());
        Produto p = produtoService.criarProduto(nome, preco);
        System.out.println("Produto cadastrado: " + p);
    }

    private static void listarProdutos() {
        produtoService.listarProdutos().forEach(System.out::println);
    }

    private static void criarPedido() {
        System.out.print("Nome do cliente: ");
        String nomeCliente = sc.nextLine();

        Cliente cliente = clienteService.buscarPorNome(nomeCliente);
        if (cliente == null) {
            System.out.println("Cliente não encontrado!");
            return;
        }

        Pedido pedido = pedidoService.criarPedido(cliente);
        while (true) {
            System.out.println("Adicionar item? (s/n)");
            if (!sc.nextLine().equalsIgnoreCase("s")) break;

            System.out.print("Nome do produto: ");
            String nomeProduto = sc.nextLine();
            Produto produto = produtoService.buscarPorNome(nomeProduto);
            if (produto == null) {
                System.out.println("Produto não encontrado!");
                continue;
            }

            System.out.print("Quantidade: ");
            int qtd = Integer.parseInt(sc.nextLine());
            System.out.print("Preço venda: ");
            double precoVenda = Double.parseDouble(sc.nextLine());

            pedido.adicionarItem(produto, qtd, precoVenda);
        }
        pedido.finalizarPedido();
        pedidoService.salvarPedido(pedido);
        System.out.println("Pedido criado e finalizado: " + pedido.getId());
    }

    private static void consultarPedido() {
        System.out.print("ID do pedido: ");
        UUID id = UUID.fromString(sc.nextLine());
        Pedido pedido = pedidoService.buscarPorId(id);
        if (pedido == null) {
            System.out.println("Pedido não encontrado!");
            return;
        }
        imprimirPedido(pedido);
    }

    private static void imprimirPedido(Pedido pedido) {
        System.out.println("=== Detalhes do Pedido ===");
        System.out.println("Cliente: " + pedido.getCliente().getNome());
        System.out.println("Status: " + pedido.getStatus());
        pedido.getItens().forEach(item ->
                System.out.printf("Produto: %s | Qtd: %d | Preço: %.2f | Total: %.2f%n",
                        item.getProduto().getNome(),
                        item.getQuantidade(),
                        item.getPrecoVenda(),
                        item.getTotal()));
        System.out.printf("Total do pedido: %.2f%n", pedido.getTotal());
    }

    private static void listarPedidos() {
        List<Pedido> pedidos = pedidoService.listarPedidos();
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido encontrado.");
            return;
        }

        System.out.println("=== LISTA DE PEDIDOS ===");
        for (Pedido p : pedidos) {
            System.out.printf(
                    "ID: %s | Cliente: %s | Total: R$ %.2f | Status: %s | Criado em: %s%n",
                    p.getId(),
                    p.getCliente().getNome(),
                    p.getTotal(),
                    p.getStatus(),
                    p.getCriadoEm()
            );
        }
    }
}