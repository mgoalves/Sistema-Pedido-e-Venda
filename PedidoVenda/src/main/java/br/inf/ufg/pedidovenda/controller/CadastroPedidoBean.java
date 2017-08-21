package br.inf.ufg.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.inf.ufg.pedidovenda.model.Usuario;
import br.inf.ufg.pedidovenda.model.cliente.Cliente;
import br.inf.ufg.pedidovenda.model.pedido.EnderecoEntrega;
import br.inf.ufg.pedidovenda.model.pedido.FormaPagamento;
import br.inf.ufg.pedidovenda.model.pedido.Pedido;
import br.inf.ufg.pedidovenda.repository.Clientes;
import br.inf.ufg.pedidovenda.repository.Usuarios;
import br.inf.ufg.pedidovenda.service.CadastroPedidoService;
import br.inf.ufg.pedidovenda.util.jsf.FacesUtil;


@Named
@ViewScoped
public class CadastroPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	//Injeções
	@Inject
	private Usuarios usuarios;
	@Inject
	private Clientes clientes;
	@Inject
	private CadastroPedidoService cadService;
	
	//Variaveis
	private Pedido pedido;
	private List<Usuario> vendedores;



	public CadastroPedidoBean() {
		
		limpar();
	}
	
	public void limpar() {
		
		pedido = new Pedido();
		pedido.setEnderecoEntrega(new EnderecoEntrega());
	}

	public void salvar() {
	
		this.pedido = this.cadService.salvar(this.pedido);
		limpar();
		FacesUtil.addInfoMessage("Pedido salvo com sucesso.");
	}

	public void inicializar() {
		
		if(FacesUtil.isPostNotBack()) {
			
			this.vendedores = this.usuarios.buscarPorNome();
		}
	}
	
	public FormaPagamento[] getFormasPagamento() {
		
		return FormaPagamento.values();
	}
	
	public List<Cliente> completarCliente(String nome) {
		return this.clientes.buscarPorNome(nome);
	}

	//Getter and Setters
	public List<Usuario> getVendedores() {
		return vendedores;
	}
	public Pedido getPedido() {
		return pedido;
	}

}
