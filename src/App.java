import java.util.*;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ClienteService clienteService = new ClienteService();
    private static final ProdutoService produtoService = new ProdutoService();
    private static final PedidoService pedidoService = new PedidoService();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== MENU E-COMMERCE ===");
            System.out.println("1 - Cadastrar Cliente");
            System.out.println("2 - Cadastrar Produto");
            System.out.println("3 - Criar Pedido");
            System.out.println("4 - Consultar Cliente");
            System.out.println("5 - Consultar Produto");
            System.out.println("6 - Consultar Pedido por ID");
            System.out.println("7 - Listar Todos os Pedidos");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> cadastrarCliente();
                case 2 -> cadastrarProduto();
                case 3 -> criarPedido();
                case 4 -> consultarCliente();
                case 5 -> consultarProduto();
                case 6 -> consultarPedidoPorId();
                case 7 -> listarPedidos();
                case 0 -> {
                    System.out.println("Encerrando...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void cadastrarCliente() {
        System.out.print("Nome do cliente: ");
        String nome = scanner.nextLine();
        System.out.print("Documento do cliente: ");
        String documento = scanner.nextLine();
        System.out.print("E-mail do cliente: ");
        String email = scanner.nextLine();

        Cliente cliente = new Cliente(UUID.randomUUID(), nome, documento, email);
        clienteService.salvar(cliente);
        System.out.println("Cliente cadastrado com sucesso!");
    }

    private static void cadastrarProduto() {
        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine();
        System.out.print("Preço do produto: ");
        double preco = Double.parseDouble(scanner.nextLine());

        Produto produto = new Produto(UUID.randomUUID(), nome, preco);
        produtoService.salvar(produto);
        System.out.println("Produto cadastrado com sucesso!");
    }

    private static void criarPedido() {
        System.out.print("Nome do cliente: ");
        String nomeCliente = scanner.nextLine();
        Cliente cliente = clienteService.buscarPorNome(nomeCliente);

        if (cliente == null) {
            System.out.println("Cliente não encontrado!");
            return;
        }

        Pedido pedido = new Pedido(UUID.randomUUID(), cliente);

        while (true) {
            System.out.print("Nome do produto (ou 'fim' para encerrar): ");
            String nomeProduto = scanner.nextLine();
            if (nomeProduto.equalsIgnoreCase("fim")) break;

            Produto produto = produtoService.buscarPorNome(nomeProduto);
            if (produto == null) {
                System.out.println("Produto não encontrado!");
                continue;
            }

            System.out.print("Quantidade: ");
            int qtd = Integer.parseInt(scanner.nextLine());
            System.out.print("Preço de venda: ");
            double precoVenda = Double.parseDouble(scanner.nextLine());

            pedido.adicionarItem(produto, qtd, precoVenda);
        }

        if (pedido.getItens().isEmpty()) {
            System.out.println("Pedido não pode ser criado sem itens!");
            return;
        }

        pedidoService.salvar(pedido);
        System.out.println("Pedido criado com sucesso!");
    }

    private static void consultarCliente() {
        System.out.print("Nome do cliente: ");
        String nome = scanner.nextLine();
        Cliente cliente = clienteService.buscarPorNome(nome);

        if (cliente == null) {
            System.out.println("Cliente não encontrado!");
        } else {
            System.out.println("ID: " + cliente.getId());
            System.out.println("Nome: " + cliente.getNome());
            System.out.println("Documento: " + cliente.getDocumento());
            System.out.println("E-mail: " + cliente.getEmail());
        }
    }

    private static void consultarProduto() {
        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine();
        Produto produto = produtoService.buscarPorNome(nome);

        if (produto == null) {
            System.out.println("Produto não encontrado!");
        } else {
            System.out.println("ID: " + produto.getId());
            System.out.println("Nome: " + produto.getNome());
            System.out.println("Preço: R$ " + produto.getPreco());
        }
    }

    private static void consultarPedidoPorId() {
        System.out.print("ID do pedido: ");
        String id = scanner.nextLine();

        List<Pedido> pedidos = pedidoService.listar(clienteService, produtoService);
        Pedido pedido = pedidos.stream()
                .filter(p -> p.getId().toString().equals(id))
                .findFirst()
                .orElse(null);

        if (pedido == null) {
            System.out.println("Pedido não encontrado!");
            return;
        }

        System.out.println("=== Pedido ===");
        System.out.println("ID: " + pedido.getId());
        System.out.println("Cliente: " + pedido.getCliente().getNome());
        System.out.println("Data de criação: " + pedido.getCriadoEm());
        System.out.println("Status: " + pedido.getStatus());
        System.out.printf("Valor total: R$ %.2f%n", pedido.getTotal());

        System.out.println("Itens:");
        pedido.getItens().forEach(item ->
                System.out.println(item.getProduto().getNome() +
                        " - Qtd: " + item.getQuantidade() +
                        " - Preço: R$ " + item.getPrecoVenda()));
    }

    private static void listarPedidos() {
        List<Pedido> pedidos = pedidoService.listar(clienteService, produtoService);

        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido encontrado.");
            return;
        }

        System.out.println("=== Todos os Pedidos ===");
        for (Pedido p : pedidos) {
            System.out.printf("ID: %s | Cliente: %s | Data: %s | Total: R$ %.2f%n",
                    p.getId(), p.getCliente().getNome(), p.getCriadoEm(), p.getTotal());
        }
    }
}
