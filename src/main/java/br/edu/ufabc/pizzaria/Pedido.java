package br.edu.ufabc.pizzaria;

import java.util.List;

public class Pedido {

	private int id;
	private Cliente cliente;
	private List<Produto> produtos;
	private double precoTotal;

	public Pedido(){

	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return this.id;
	}

	public void setCliente(Cliente cliente){
		this.cliente = cliente;
	}

	public Cliente getCliente(){
		return this.cliente;
	}

	public void setProdutos(List<Produto> produtos){
		this.produtos = produtos;
	}

	public List<Produto> getProdutos(){
		return this.produtos;
	}

	public void setPrecoTotal(double precoTotal){
		this.precoTotal = precoTotal;
	}

	public double getPrecoTotal(){
		return this.precoTotal;
	}

}
