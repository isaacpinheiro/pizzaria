package br.edu.ufabc.pizzaria;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class App implements Watcher {

	static ZooKeeper zk = null;
	static Integer mutex;
	static String host = "127.0.0.1";
	static int clienteId = 1;
	static int pedidoId = 1;

	String root;

	App(String address){
		try{
			zk = new ZooKeeper(address, 3000, this);
			mutex = new Integer(-1);
		}catch(IOException e){
			System.out.println(e.toString());
		}
	}

	synchronized public void process(WatchedEvent event){
		synchronized (mutex){
			mutex.notify();
		}
	}

	// Enfileira os processos (Pedidos).
	static public class Queue extends App {

		Queue(String address, String name){

			super(address);
			this.root = name;

			// Cria o znode raíz caso não tenha.
			if(zk != null){
				try{
					zk.create("/"+root, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}catch(KeeperException e){
					System.out.println("Keeper exception when instantiating queue: " + e.toString());
				}catch(InterruptedException e){
					System.out.println("Interrupted exception.");
				}
			}

		}

		// Adiciona o processo (pedido) na fila.
		boolean produce(Pedido pedido) throws KeeperException, InterruptedException {

			byte b[] = null;

			try{

				// Converte o objeto Pedido em array de bytes
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
				objectOutputStream.writeObject(pedido);
				objectOutputStream.close();
				byteArrayOutputStream.close();
				b = byteArrayOutputStream.toByteArray();

			}catch(IOException e){
				e.printStackTrace();
			}

			zk.create("/"+root+"/pedido", b, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

			return true;

		}

		// Remove o processo (pedido) da fila, calcular o preço total e exibe os dados do pedido.
		Pedido consume() throws KeeperException, InterruptedException {

			Pedido pedido = null;
			Stat stat = null;

			while(true){

				synchronized(mutex){

					List<String> list = zk.getChildren("/"+root, true);

					if(list.size() == 0){
						System.out.println("Não temos pedidos no momento.");
						mutex.wait();
					}else{

						Integer min = new Integer(list.get(0).substring(7));
						String minString = list.get(0);
						for(String s : list){
							Integer tempValue = new Integer(s.substring(7));
							if(tempValue < min){
								min = tempValue;
								minString = s;
							}
						}

						byte b[] = zk.getData("/"+root+"/"+minString, false, stat);
						zk.delete("/"+root+"/"+minString, 0);

						try{

							// Converte o array de bytes no object Pedido.
							ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b);
							ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
							pedido = (Pedido) objectInputStream.readObject();
							objectInputStream.close();
							byteArrayInputStream.close();
							return pedido;

						}catch(IOException e){
							e.printStackTrace();
						}catch(ClassNotFoundException e){
							e.printStackTrace();
						}

					}

				}

			}

		}

	}

	public static void main(String args[]) throws Exception {

		Produto cardapio[] = new Produto[10];

		cardapio[0] = new Produto();
		cardapio[0].setId(1);
		cardapio[0].setNome("Pizza de Calabreza");
		cardapio[0].setDescricao("Pizza com calabreza, cebola e azeitona");
		cardapio[0].setPreco(23.55);

		cardapio[1] = new Produto();
		cardapio[1].setId(2);
		cardapio[1].setNome("Pizza de Mussarela");
		cardapio[1].setDescricao("Pizza com mussarela, tomate e azeitona");
		cardapio[1].setPreco(23.55);

		cardapio[2] = new Produto();
		cardapio[2].setId(3);
		cardapio[2].setNome("Pizza de Quatro Queijos");
		cardapio[2].setDescricao("Pizza com queijo e azeitona");
		cardapio[2].setPreco(30.90);

		cardapio[3] = new Produto();
		cardapio[3].setId(4);
		cardapio[3].setNome("Pizza de Atum");
		cardapio[3].setDescricao("Pizza com atum, milho, cebola e azeitona");
		cardapio[3].setPreco(30.90);

		cardapio[4] = new Produto();
		cardapio[4].setId(5);
		cardapio[4].setNome("Pizza de Frango com Catupiri");
		cardapio[4].setDescricao("Pizza com frango, catupiri, milho e azeitona");
		cardapio[4].setPreco(23.55);

		cardapio[5] = new Produto();
		cardapio[5].setId(6);
		cardapio[5].setNome("Pizza de Portuguesa");
		cardapio[5].setDescricao("Pizza com calabreza, ovo, queijo, cebola e azeitona");
		cardapio[5].setPreco(35.99);

		cardapio[6] = new Produto();
		cardapio[6].setId(7);
		cardapio[6].setNome("Coca-Cola");
		cardapio[6].setDescricao("Refrigerante de 3,3 Litros");
		cardapio[6].setPreco(6.50);

		cardapio[7] = new Produto();
		cardapio[7].setId(8);
		cardapio[7].setNome("Suco Del Valle");
		cardapio[7].setDescricao("Suco de frutas de 350ml");
		cardapio[7].setPreco(3.65);

		cardapio[8] = new Produto();
		cardapio[8].setId(9);
		cardapio[8].setNome("Ciroc");
		cardapio[8].setDescricao("Vodka de 750ml");
		cardapio[8].setPreco(110.75);

		cardapio[9] = new Produto();
		cardapio[9].setId(10);
		cardapio[9].setNome("Skol");
		cardapio[9].setDescricao("Cerveja de 1 Litro");
		cardapio[9].setPreco(7.00);

		Queue queue = new Queue(host, "pizzaria");
		Scanner in = new Scanner(System.in);
		int op  = 0;

		while(op != 3){

			System.out.printf("\033[2J");
			System.out.println("**************************************************************");
			System.out.println("*                                                            *");
			System.out.println("*                         Pizzaria                           *");
			System.out.println("*                                                            *");
			System.out.println("*      1) Cadastrar Pedido  2) Entregar Pedido  3) Sair      *");
			System.out.println("*                                                            *");
			System.out.println("**************************************************************");
			System.out.print("///:> ");
			op = Integer.parseInt(in.nextLine());

			if(op == 1){

				System.out.printf("\033[2J");
				System.out.println("******* Informações do Cliente \n");

				Cliente cliente = new Cliente();
				cliente.setId(clienteId);

				System.out.print("Nome: ");
				cliente.setNome(in.nextLine());

				System.out.print("CPF: ");
				cliente.setCpf(in.nextLine());

				System.out.print("Telefone: ");
				cliente.setTelefone(in.nextLine());

				System.out.println("******* Endereço do Cliente \n");

				Endereco endereco = new Endereco();

				System.out.print("Rua: ");
				endereco.setRua(in.nextLine());

				System.out.print("Número: ");
				endereco.setNumero(in.nextLine());

				System.out.print("CEP: ");
				endereco.setCep(in.nextLine());

				System.out.print("Bairro: ");
				endereco.setBairro(in.nextLine());

				System.out.print("Complemento: ");
				endereco.setComplemento(in.nextLine());
				cliente.setEndereco(endereco);

				Pedido pedido = new Pedido();
				pedido.setId(pedidoId);
				pedido.setCliente(cliente);
				pedido.setProdutos(new ArrayList<Produto>());
				pedido.setPrecoTotal(0);

				String op2 = "";

				while(!op2.equals("f") && !op2.equals("F")){

					System.out.printf("\033[2J");
					System.out.println("******* Pedido \n");
					System.out.println("Informe o número do produto á ser adicionado no pedido: ");

					for(int i=0;i<10;i++){
						System.out.println("\tID: "+cardapio[i].getId()+" Nome: "+cardapio[i].getNome()
							+" Descrição: "+cardapio[i].getDescricao()+" Preço: "+cardapio[i].getPreco());
					}

					System.out.println("Digite (F) para finalizar o pedido.");
					System.out.print("///:> ");
					op2 = in.nextLine();

					if(!op2.equals("f") && !op2.equals("F")){
						pedido.getProdutos().add(cardapio[Integer.parseInt(op2)-1]);
					}

				}

				System.out.printf("\033[2J");
				System.out.print("Deseja efetivar esta operação? (S) ou (N): ");

				String op3 = in.nextLine();

				if(op3.equals("s") || op3.equals("S")){
					clienteId++;
					pedidoId++;
					queue.produce(pedido);
				}

			}else if(op == 2){

				System.out.printf("\033[2J");
				System.out.println("******* Pedido Entregue \n\n");

				Pedido pedido = queue.consume();

				System.out.println("ID do Pedido: "+pedido.getId());
				System.out.println("Nº Cliente: "+pedido.getCliente().getId());
				System.out.println("Nome: "+pedido.getCliente().getNome());
				System.out.println("CPF: "+pedido.getCliente().getCpf());
				System.out.println("Telefone: "+pedido.getCliente().getTelefone());
				System.out.println("Rua: "+pedido.getCliente().getEndereco().getRua());
				System.out.println("Número: "+pedido.getCliente().getEndereco().getNumero());
				System.out.println("CEP: "+pedido.getCliente().getEndereco().getCep());
				System.out.println("Bairro: "+pedido.getCliente().getEndereco().getBairro());
				System.out.println("Complemento: "+pedido.getCliente().getEndereco().getComplemento());
				System.out.println("Produtos: ");

				for(Produto p : pedido.getProdutos()){

					System.out.println("\tID: "+p.getId()+" Nome: "+p.getNome()
						+" Descrição: "+p.getDescricao()+" Preço: "+p.getPreco());

					pedido.setPrecoTotal(pedido.getPrecoTotal() + p.getPreco());

				}

				System.out.println("Preço Total: "+pedido.getPrecoTotal());
				in.nextLine();

			}

		}

		System.out.println("Sair");

	}

}
