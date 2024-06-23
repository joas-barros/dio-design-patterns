package dio.digitalinovetion.demo.service;

import dio.digitalinovetion.demo.model.Client;
import org.springframework.context.annotation.Bean;

public interface ClientService {
    Iterable<Client> buscarTodos();
    Client buscarPorId(Long id);
    void inserir(Client client);
    void atualizar(Long id, Client client);
    void deletar(Long id);

}
