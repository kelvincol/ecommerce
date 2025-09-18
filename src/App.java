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
            System.out.println("6 - Consultar Pedido Por Nome De Cliente");
            System.out.println("7 - Listar Todos os Pedidos");
            System.out.println("8 - Atualizar Cliente");
            System.out.println("9 - Atualizar Produto");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> cadastrarCliente();
                case 2 -> cadastrarProduto();
                case 3 -> criarPedido();
                case 4 -> consultarCliente();
                case 5 -> consultarProduto();
                case 6 -> consultarPedidosPorCliente();
                case 7 -> listarPedidos();
                case 8 -> atualizarCliente();
                case 9 -> atualizarProduto();
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
        System.out.println("Pedido criado com sucesso para "  + cliente.getNome());

        Notificador.enviar(cliente,
                "Pedido criado",
                "Olá " + cliente.getNome() + ", seu pedido foi criado com sucesso.");

        Notificador.enviar(cliente,
                "Pagamento confirmado",
                "Olá " + cliente.getNome() + ", o pagamento do seu pedido foi confirmado.");

        Notificador.enviar(cliente,
                "Pedido finalizado",
                "Olá " + cliente.getNome() + ", seu pedido já foi entregue. Obrigado pela compra!");

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

    private static void consultarPedidosPorCliente() {
        System.out.print("Digite o nome do cliente: ");
        String nome = scanner.nextLine();

        Cliente cliente = clienteService.buscarPorNome(nome);
        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        List<Pedido> pedidosDoCliente = pedidoService.listarPorCliente(cliente.getId(), clienteService, produtoService);

        if (pedidosDoCliente.isEmpty()) {
            System.out.println("Nenhum pedido encontrado para este cliente.");
        } else {
            System.out.println("Pedidos do cliente " + cliente.getNome() + ":");
            for (Pedido pedido : pedidosDoCliente) {
                System.out.println("---------------------------------------");
                System.out.println("ID do Pedido: " + pedido.getId());
                System.out.println("Data de criação: " + pedido.getCriadoEm());
                System.out.println("Status: " + pedido.getStatus());
                System.out.println("Valor total da venda: R$ " + pedido.getTotal());
                System.out.println("Itens:");
                for (ItemPedido item : pedido.getItens()) {
                    System.out.println(" - " + item.getProduto().getNome() + " | Quantidade: " + item.getQuantidade() + " | Valor Unitário Do Produto: R$ " + item.getProduto().getPreco() + " | Valor de venda: R$ " + item.getPrecoVenda());
                }
            }
        }
    }

    private static void listarPedidos() {
        List<Pedido> pedidos = pedidoService.listar(clienteService, produtoService);

        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido encontrado.");
            return;
        }

        System.out.println("=== Todos os Pedidos ===");
        for (Pedido p : pedidos) {
            System.out.printf("ID: %s | Cliente: %s | Data: %s | Total da venda: R$ %.2f%n",
                    p.getId(), p.getCliente().getNome(), p.getCriadoEm(), p.getTotal());
        }
    }

    private static void atualizarCliente() {
        System.out.print("Digite o nome do cliente que deseja atualizar: ");
        String nome = scanner.nextLine();

        Cliente cliente = clienteService.buscarPorNome(nome);

        if (cliente == null) {
            System.out.println("Cliente não encontrado!");
            return;
        }

        System.out.println("Cliente encontrado: ");
        System.out.println("Nome atual: " + cliente.getNome());
        System.out.println("Documento atual: " + cliente.getDocumento());
        System.out.println("E-mail atual: " + cliente.getEmail());

        System.out.print("Novo nome (ou Enter para manter): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isBlank()) cliente.setNome(novoNome);

        System.out.print("Novo documento (ou Enter para manter): ");
        String novoDocumento = scanner.nextLine();
        if (!novoDocumento.isBlank()) cliente.setDocumento(novoDocumento);

        System.out.print("Novo e-mail (ou Enter para manter): ");
        String novoEmail = scanner.nextLine();
        if (!novoEmail.isBlank()) cliente.setEmail(novoEmail);

        clienteService.atualizar(cliente);
        System.out.println("Cliente atualizado com sucesso!");
    }

    private static void atualizarProduto() {
        System.out.print("Digite o nome do produto que deseja atualizar: ");
        String nome = scanner.nextLine();

        Produto produto = produtoService.buscarPorNome(nome);

        if (produto == null) {
            System.out.println("Produto não encontrado!");
            return;
        }

        System.out.println("Produto encontrado: ");
        System.out.println("Nome atual: " + produto.getNome());
        System.out.println("Preço atual: R$ " + produto.getPreco());

        System.out.print("Novo nome (ou Enter para manter): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isBlank()) produto.setNome(novoNome);

        System.out.print("Novo preço (ou Enter para manter): ");
        String novoPrecoStr = scanner.nextLine();
        if (!novoPrecoStr.isBlank()) {
            try {
                double novoPreco = Double.parseDouble(novoPrecoStr);
                produto.setPreco(novoPreco);
            } catch (NumberFormatException e) {
                System.out.println("Preço inválido, mantendo valor antigo.");
            }
        }

        produtoService.atualizar(produto);
        System.out.println("Produto atualizado com sucesso!");
    }
}
