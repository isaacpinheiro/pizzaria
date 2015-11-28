package br.edu.ufabc.pizzaria;

import java.io.Serializable;

public class Produto implements Serializable {

	public static final long serialVersionUID = 1L;

	private int id;
	private String nome;
	private String descricao;
	private double preco;

	public Produto(){

	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return this.id;
	}

	public void setNome(String nome){
		this.nome = nome;
	}

	public String getNome(){
		return this.nome;
	}

	public void setDescricao(String descricao){
		this.descricao = descricao;
	}

	public String getDescricao(){
		return this.descricao;
	}

	public void setPreco(double preco){
		this.preco = preco;
	}

	public double getPreco(){
		return this.preco;
	}

}
