


public class Notificador {
    public static void notificar(Cliente cliente, String mensagem) {
        System.out.println("[EMAIL -> " + cliente.getEmail() + "] " + mensagem);
    }
}
