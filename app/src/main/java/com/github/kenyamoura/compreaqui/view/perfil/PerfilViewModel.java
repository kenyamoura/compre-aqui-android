package com.github.kenyamoura.compreaqui.view.perfil;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.github.kenyamoura.compreaqui.dominio.Cliente;
import com.github.kenyamoura.compreaqui.model.repositorio.ClienteRepositorio;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.github.kenyamoura.compreaqui.utils.StringUtils.isBlank;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static io.reactivex.schedulers.Schedulers.io;

public class PerfilViewModel {

    // dependências da classe
    private final ClienteRepositorio clienteRepositorio;
    private final PerfilViewCallback viewCallback;
    private final List<String> estados;

    // observables usados na activity
    public ObservableField<String> nomeField = new ObservableField<>();
    public ObservableField<String> cpfField = new ObservableField<>();
    public ObservableField<String> emailField = new ObservableField<>();
    public ObservableField<String> enderecoField = new ObservableField<>();
    public ObservableInt estadoField = new ObservableInt();
    public ObservableField<String> municipioField = new ObservableField<>();
    public ObservableField<String> telefoneField = new ObservableField<>();
    public ObservableField<String> senhaField = new ObservableField<>();

    // objeto utilizado pelo rxJava para automaticamente se desfazer das chamadas
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    // guardamos o email do usuário para ações futuras como apagar e salvar
    private String emailClienteAutenticado;

    public PerfilViewModel(ClienteRepositorio clienteRepositorio, PerfilViewCallback viewCallback, List<String> estados) {
        this.clienteRepositorio = clienteRepositorio;
        this.viewCallback = viewCallback;
        this.estados = estados;
    }

    /*
     * Método utilizado pela view para carregar o perfil do usuário na tela.
     * Solicita ao backend pelo e-mail.
     */
    public void carregarPerfil(String email) {
        this.emailClienteAutenticado = email;
        Disposable subscribe = clienteRepositorio.buscar(email)
                .observeOn(mainThread()) // Executa na thread principal do Android
                .subscribeOn(io()) // Aguarda em background
                .subscribe(this::onPerfilCarregado, // Caso sucesso, chama o método passando o cliente.
                        viewCallback::onErro); // Caso erro, chama o callback passando o erro.
        compositeDisposable.add(subscribe);
    }

    /*
     * Método utilizado pelo ViewModel para carregar o perfil retornado e avisar a tela que o perfil foi carregado.
     */
    private void onPerfilCarregado(Cliente cliente) {
        carregarCliente(cliente);
        viewCallback.onPerfilCarregado();
    }

    // Carrega a tela com informações vindas do backend. Não preenche o e-mail.
    private void carregarCliente(Cliente cliente) {
        nomeField.set(cliente.getNome());
        cpfField.set(cliente.getCpf());
        emailField.set(cliente.getEmail());
        enderecoField.set(cliente.getEndereco());
        estadoField.set(getPosicaoEstado(cliente.getEstado()));
        municipioField.set(cliente.getMunicipio());
        telefoneField.set(cliente.getTelefone());
    }

    /*
     * Converte o texto do estado em ID. A primeira posição da lista de estados é considerada "Nulo".
     */
    private Integer getPosicaoEstado(String estadoProcurado) {
        // Retorna 0 caso o texto seja vazio.
        if (isBlank(estadoProcurado)) {
            return 0;
        }
        Integer posicao = 0;
        // Procura na lista de estados
        for (String estado : estados) {
            if (estado.equalsIgnoreCase(estadoProcurado)) { // se o texto for igual, retorna posicao
                return posicao;
            }
            posicao += 1; // Se nao, incrementa posicao e tenta novamente
        }
        return 0; // Se nenhum for encontrado, retorna a opção padrão "0"
    }

    /*
     * Método utilizado pela View para solicitar que o perfil seja salvo.
     */
    public void salvarPerfil() {
        String email = emailField.get();
        // validamos que o email foi preenchido.
        if (isBlank(email)) {
            viewCallback.onErro(new RuntimeException("Preencha o email."));
            return;
        }

        // validamos que a senha foi preenchida
        String senha = senhaField.get();
        if (isBlank(senha)) {
            viewCallback.onErro(new RuntimeException("Preencha a senha."));
            return;
        }
        String nome = nomeField.get();
        String cpf = cpfField.get();
        String endereco = enderecoField.get();
        String estado = recuperarNomeEstado(estadoField.get());
        String municipio = municipioField.get();
        String telefone = telefoneField.get();

        // montamos um objeto Cliente com as informações da tela
        Cliente cliente = new Cliente(email, senha, cpf, nome, endereco, estado, municipio, telefone);

        // Atualizamos o cliente assíncronamente passando o email anterior e as novas informações do Cliente.
        Disposable disposable = clienteRepositorio.atualizar(emailClienteAutenticado, cliente)
                .observeOn(mainThread()) // observa o resultado na thread principal do Android
                .subscribeOn(io()) // Aguarda em background
                .subscribe(clienteSalvo -> onPerfilSalvo(),  // caso salve com sucess, chama o método onPerfilSalvo
                        viewCallback::onErro); // caso erro, chama callback repassando o erro.
        compositeDisposable.add(disposable); // adiciona a chamada acima para ser recolhida posteriormente
    }

    /*
     * Método utilizado para avisar a View que o perfil foi salvo.
     * Limpa a senha da tela.
     */
    private void onPerfilSalvo() {
        viewCallback.onPerfilSalvoComSucesso();
        senhaField.set("");
    }

    /**
     * Método utilizado para converter Id em texto.
     *
     * @param estadoId Id do estado que se deseja converter.
     */
    private String recuperarNomeEstado(int estadoId) {
        // O primeiro elemento de id 0 é o texto "Selecione", então retornamos nulo neste caso.
        if (estadoId < 1) {
            return null;
        }

        // Caso o id seja maior que zero, recupera da lista de estados.
        return estados.get(estadoId);
    }

    /*
     * Método utilizado pela View para solicitar que a conta seja destruída.
     */
    public void destruirConta() {
        Disposable disposable = clienteRepositorio.destruirConta(emailClienteAutenticado)
                .observeOn(mainThread()) // observa o resultado na thread principal do Android
                .subscribeOn(io()) // Aguarda em background
                .subscribe(viewCallback::onPerfilDestruido, // caso destrua com sucesso, chama o callback onPerfilDestruido
                        viewCallback::onErro); // caso erro, chama callback repassando o erro.
        compositeDisposable.add(disposable); // adiciona a chamada para remoção futura.
    }

    /*
     * Método chamado pela View para que o ViewModel execute tarefas necessárias antes de fechar a tela.
     */
    public void onFinalizar() {
        compositeDisposable.clear();
    }
}
