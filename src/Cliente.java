import java.util.UUID;

public class Cliente {
    private UUID id;
    private String nome;
    private String documento;
    private String email;

    public Cliente(UUID id, String nome, String documento, String email) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
        this.email = email;
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getDocumento() { return documento; }
    public String getEmail() { return email; }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        this.nome = nome;
    }

    public void setDocumento(String documento) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("Documento de identificação é obrigatório.");
        }
        this.documento = documento;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail é obrigatório.");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("Cliente{id=%s, nome='%s', doc='%s', email='%s'}", id, nome, documento, email);
    }

    public String toLinhaArquivo() {
        return id + ";" + nome + ";" + documento + ";" + email;
    }

    public static Cliente fromLinhaArquivo(String linha) {
        String[] partes = linha.split(";");
        return new Cliente(UUID.fromString(partes[0]), partes[1], partes[2], partes[3]);
    }


}
