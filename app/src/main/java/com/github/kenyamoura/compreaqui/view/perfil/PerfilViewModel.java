package com.github.kenyamoura.compreaqui.view.perfil;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.github.kenyamoura.compreaqui.dominio.Cliente;
import com.github.kenyamoura.compreaqui.model.repositorio.ClienteRepositorio;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.github.kenyamoura.compreaqui.utils.StringUtils.isBlank;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static io.reactivex.schedulers.Schedulers.io;

public class PerfilViewModel {

    private final List<String> estados;
    public ObservableField<String> nomeField = new ObservableField<>();
    public ObservableField<String> cpfField = new ObservableField<>();
    public ObservableField<String> emailField = new ObservableField<>();
    public ObservableField<String> enderecoField = new ObservableField<>();
    public ObservableInt estadoField = new ObservableInt();
    public ObservableField<String> municipioField = new ObservableField<>();
    public ObservableField<String> telefoneField = new ObservableField<>();
    public ObservableField<String> senhaField = new ObservableField<>();

    private final ClienteRepositorio clienteRepositorio;
    private final PerfilViewCallback viewCallback;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String emailClienteAutenticado;

    public PerfilViewModel(ClienteRepositorio clienteRepositorio, PerfilViewCallback viewCallback, List<String> estados) {
        this.clienteRepositorio = clienteRepositorio;
        this.viewCallback = viewCallback;
        this.estados = estados;
    }

    public void carregarPerfil(String email) {
        this.emailClienteAutenticado = email;
        Disposable subscribe = clienteRepositorio.buscar(email)
                .observeOn(mainThread())
                .subscribeOn(io())
                .subscribe(this::onPerfilCarregado, viewCallback::onErro);
        compositeDisposable.add(subscribe);
    }

    private void onPerfilCarregado(Cliente cliente) {
        carregarCliente(cliente);
        viewCallback.onPerfilCarregado();
    }

    private void carregarCliente(Cliente cliente) {
        nomeField.set(cliente.getNome());
        cpfField.set(cliente.getCpf());
        emailField.set(cliente.getEmail());
        enderecoField.set(cliente.getEndereco());
        estadoField.set(getPosicaoEstado(cliente.getEstado()));
        municipioField.set(cliente.getMunicipio());
        telefoneField.set(cliente.getTelefone());
    }

    private Integer getPosicaoEstado(String estadoProcurado) {
        if (estadoProcurado == null) {
            return 0;
        }
        Integer posicao = 0;
        for (String estado : estados) {
            if (estado.equalsIgnoreCase(estadoProcurado)) {
                return posicao;
            }
            posicao += 1;
        }
        return 0;
    }

    public void salvarPerfil() {
        String email = emailField.get();
        if (isBlank(email)) {
            viewCallback.onErro(new RuntimeException("Preencha o email."));
            return;
        }

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

        Cliente cliente = new Cliente(email, senha, cpf, nome, endereco, estado, municipio, telefone);
        Disposable disposable = clienteRepositorio.atualizar(emailClienteAutenticado, cliente)
                .observeOn(mainThread())
                .subscribeOn(io())
                .subscribe(c -> onPerfilSalvo(), viewCallback::onErro);
        compositeDisposable.add(disposable);
    }

    private void onPerfilSalvo() {
        viewCallback.onPerfilSalvoComSucesso();
        senhaField.set("");
    }

    private String recuperarNomeEstado(int estadoId) {
        if (estadoId < 1) {
            return null;
        }
        return estados.get(estadoId);
    }

    public void destruirConta() {
        Disposable disposable = clienteRepositorio.destruirConta(emailClienteAutenticado)
                .observeOn(mainThread())
                .subscribeOn(io())
                .subscribe(viewCallback::onPerfilDestruido, viewCallback::onErro);
        compositeDisposable.add(disposable);
    }

    public void onFinalizar() {
        compositeDisposable.clear();
    }
}
