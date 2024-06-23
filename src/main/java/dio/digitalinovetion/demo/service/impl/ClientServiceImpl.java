package dio.digitalinovetion.demo.service.impl;

import dio.digitalinovetion.demo.model.Client;
import dio.digitalinovetion.demo.model.ClientRepository;
import dio.digitalinovetion.demo.model.Endereco;
import dio.digitalinovetion.demo.model.EnderecoRepository;
import dio.digitalinovetion.demo.service.ClientService;
import dio.digitalinovetion.demo.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    // Singleton: Injetar os componentes do Spring com @Autowired.
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    // Strategy: Implementar os métodos definidos na interface.
    // Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

    @Override
    public Iterable<Client> buscarTodos() {
        // Buscar todos os Clientes.
        return clientRepository.findAll();
    }

    @Override
    public Client buscarPorId(Long id) {
        Client client = clientRepository.findById(id).get();
        return client;
    }

    @Override
    public void inserir(Client client) {
        preencherClienteSalvando(client);
    }


    @Override
    public void atualizar(Long id, Client client) {
        // Buscar Cliente por ID, caso exista:
        Optional<Client> clientId = clientRepository.findById(id);
        if (clientId.isPresent()){
            preencherClienteSalvando(client);
        }
    }

    @Override
    public void deletar(Long id) {
        // Deletar Cliente por ID.
        clientRepository.deleteById(id);
    }

    private void preencherClienteSalvando(Client client) {
        // Verificar se o Endereco do Cliente já existe (pelo CEP).
        String cep = client.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            // Caso não exista, integrar com o ViaCEP e persistir o retorno.
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        client.setEndereco(endereco);
        // Inserir Cliente, vinculando o Endereco (novo ou existente).
        clientRepository.save(client);
    }
}
