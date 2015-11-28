package br.edu.ufabc.pizzaria;

import java.io.Serializable;

public class Endereco implements Serializable {

	public static final long serialVersionUID = 1L;

	private String rua;
	private String numero;
	private String cep;
	private String bairro;
	private String complemento;

	public Endereco(){

	}

	public void setRua(String rua){
		this.rua = rua;
	}

	public String getRua(){
		return this.rua;
	}

	public void setNumero(String numero){
		this.numero = numero;
	}

	public String getNumero(){
		return this.numero;
	}

	public void setCep(String cep){
		this.cep = cep;
	}

	public String getCep(){
		return this.cep;
	}

	public void setBairro(String bairro){
		this.bairro = bairro;
	}

	public String getBairro(){
		return this.bairro;
	}

	public void setComplemento(String complemento){
		this.complemento = complemento;
	}

	public String getComplemento(){
		return this.complemento;
	}

}
