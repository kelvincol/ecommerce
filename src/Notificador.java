public class Notificador {
    public static void enviar(Cliente cliente, String assunto, String mensagem) {
        System.out.println("--------------------------------------------------");
        System.out.println("Envio de e-mail para: " + cliente.getEmail());
        System.out.println("Assunto: " + assunto);
        System.out.println("Mensagem: " + mensagem);
        System.out.println("--------------------------------------------------");
    }
}
