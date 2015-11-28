package br.edu.ufabc.pizzaria;

import java.io.Serializable;

public class Cliente implements Serializable {

	public static final long serialVersionUID = 1L;

	private int id;
	private String nome;
	private String cpf;
	private String telefone;
	private Endereco endereco;

	public Cliente(){

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

	public void setCpf(String cpf){
		this.cpf = cpf;
	}

	public String getCpf(){
		return this.cpf;
	}

	public void setTelefone(String telefone){
		this.telefone = telefone;
	}

	public String getTelefone(){
		return this.telefone;
	}

	public void setEndereco(Endereco endereco){
		this.endereco = endereco;
	}

	public Endereco getEndereco(){
		return this.endereco;
	}

}
