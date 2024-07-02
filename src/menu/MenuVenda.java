package menu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import model.Cliente;
import model.Venda;
import service.VendaService;
import model.Produto;
import model.VendaProduto;
import service.ProdutoService;

public class MenuVenda {

    public void opcaoVenda() throws SQLException, ParseException{
        int nova_opcao, idCliente, idVenda, idProduto;
        double valorTotal;
        Date data;
        
        Scanner teclado = new Scanner(System.in);
        VendaService vendaService = new VendaService();
        ProdutoService produtoService = new ProdutoService();
        Cliente c = new Cliente("", "", "", "", "");
        List<VendaProduto> itensVendidos;
        
        System.out.println("1: Listar VENDAS");
        System.out.println("2: Adicionar uma VENDA");
        System.out.println("3: Editar uma VENDA");
        System.out.println("4: Deletar uma VENDA");
        System.out.println("5: Verificar quantidade de VENDAS em um dia. Data: ano/mes/dia");
        nova_opcao = teclado.nextInt();
        
        
        switch (nova_opcao){
            case 1:
                System.out.println("VAI LISTAR VENDAS");
                
                ResultSet rs = vendaService.listar();
                
                while(rs.next()){
                    System.out.print("Nº identificador: ");
                    System.out.println(rs.getString("venda_id"));
                    System.out.print("Data da venda: ");
                    System.out.println(rs.getDate("data"));
                    System.out.print("Valor da venda: R$");
                    System.out.println(rs.getDouble("valor_total"));
                    System.out.println("---------");
                }
                        
                break;

            case 2:
                System.out.println("VAI ADICIONAR VENDA");

                System.out.println("OK! Vamos precisar dos dados da Venda");
                System.out.print("Qual o id do cliente que efetuara a compra? ");
                idCliente = teclado.nextInt();
                c.setClienteId(idCliente);
                data = new Date();

                ResultSet rsp = produtoService.listar();
                    while(rsp.next()){
                        System.out.print("Nº identificador: ");
                        System.out.println(rsp.getString("produto_id"));
                        System.out.print("Nome: ");
                        System.out.println(rsp.getString("nome"));
                        System.out.print("Quantidade em estoque: ");
                        System.out.println(rsp.getString("quantidade_de_estoque"));
                        System.out.print("Valor da Unidade: ");
                        System.out.println(rsp.getString("valor_unidade"));
                        System.out.println("---------");
                        }
                        
                itensVendidos = new ArrayList<>();
                char maisItens;
                do {
                    idVenda = vendaService.getNextVendaId();
                    Venda v = new Venda(idVenda,c,null, 0.0);
                    
                    System.out.print("Digite o ID do Produto: ");
                    idProduto = teclado.nextInt();

                    // Busca o produto pelo ID
                    Produto p = produtoService.consultarPorId(idProduto);

                    System.out.print("Digite a quantidade: ");
                    int quantidade = teclado.nextInt();

                    double valorTotalItem = quantidade * p.getValorUnidade();

                    VendaProduto vp = new VendaProduto(v, p, quantidade, valorTotalItem);
                    vp.CalcularValorTotalItem(); // Garante que o valor total do item esteja correto
                    itensVendidos.add(vp);

                    System.out.print("Deseja adicionar mais itens? (s/n): ");
                    maisItens = teclado.next().charAt(0);
                } while (maisItens == 's' || maisItens == 'S');

                    valorTotal = itensVendidos.stream()
                        .mapToDouble(VendaProduto::getValorTotalItem)
                        .sum();

                Venda v = new Venda(c, data, valorTotal);
                v.setItensVendidos(itensVendidos); // Adiciona os itens à venda

                vendaService.inserir(v);

                teclado.close(); 
                
                break;
                
            case 3:  
                System.out.print("Digite o ID da venda a ser editada: ");
                idVenda = teclado.nextInt();
                teclado.nextLine(); // Limpar o buffer

                // Buscar a venda pelo ID
                Venda venda = vendaService.consultarPorId(idVenda);

                if (venda != null) {
                    System.out.println("Venda encontrada:");
                    System.out.println(venda); // Imprime os detalhes da venda
                        
                // Editar detalhes da venda (cliente, data)
                System.out.print("Novo ID do cliente (ou 0 para manter o mesmo): ");
                idCliente = teclado.nextInt();
                teclado.nextLine(); // Limpar o buffer
                
                if (idCliente != 0) {
                    Cliente novoCliente = new Cliente("", "", "", "", "");
                    novoCliente.setClienteId(idCliente);
                    venda.setCliente(novoCliente);
                }

                System.out.print("Nova data da venda (formato dd/MM/yyyy ou 0 para manter a mesma): ");
                String novaDataStr = teclado.nextLine();
                if (!novaDataStr.equals("0")) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date novaData = dateFormat.parse(novaDataStr);
                    venda.setData(novaData);
                }
                
                ResultSet rspv = vendaService.listarProdutoVenda(venda);
                
                while(rspv.next()){
                    System.out.print("Produtos no carrinho: ");
                    System.out.println("---------");
                    System.out.print("Nº identificador: ");
                    System.out.println(rspv.getInt("produto_id"));
                    System.out.print("Quantidade: ");
                    System.out.println(rspv.getInt("quantidade"));
                    System.out.print("Valor por unidade: R$");
                    System.out.println(rspv.getDouble("valor_unidade"));
                    System.out.print("Valor total do item: R$");
                    System.out.println(rspv.getDouble("valor_total_item"));
                    System.out.println("---------");
                }
                
                // Editar itens da venda
                itensVendidos = venda.getItensVendidos();
                if (itensVendidos == null) {
                    itensVendidos = new ArrayList<>();
                    venda.setItensVendidos(itensVendidos);
                }

                char maisItensE = 0;
                do {
                    System.out.print("Digite o ID do Produto (ou 0 para finalizar): ");
                    idProduto = teclado.nextInt();
                    if (idProduto == 0) {
                        break;
                    }

                    Produto produto = produtoService.consultarPorId(idProduto);
                    if (produto == null) {
                        System.out.println("Produto não encontrado.");
                        continue;
                    }

                    System.out.print("Digite a nova quantidade: ");
                    int quantidade = teclado.nextInt();

                    double valorTotalItem = quantidade * produto.getValorUnidade();
                    VendaProduto vp = new VendaProduto(venda, produto, quantidade, valorTotalItem);
                    vp.CalcularValorTotalItem();

                    // Verifica se o item já existe na lista para atualizar ou adicionar
                    boolean itemExistente = false;
                    for (int i = 0; i < itensVendidos.size(); i++) {
                        if (itensVendidos.get(i).getProduto().getProdutoId() == idProduto) {
                            itensVendidos.set(i, vp); // Atualiza o item existente
                            itemExistente = true;
                            break;
                        }
                    }
                    if (!itemExistente) {
                        itensVendidos.add(vp); // Adiciona um novo item
                    }

                    System.out.print("Deseja editar mais itens? (s/n): ");
                    maisItensE = teclado.next().charAt(0);
                    teclado.nextLine(); // Limpar o buffer
                } while (maisItensE == 's' || maisItensE == 'S');

                    // Recalcular o valor total da venda
                    valorTotal = itensVendidos.stream()
                        .mapToDouble(VendaProduto::getValorTotalItem)
                        .sum();
                    venda.setValorTotal(valorTotal);
                    // Atualizar a venda no banco de dados
                    vendaService.editar(venda);
                    System.out.println("Venda atualizada com sucesso!");
                    } else {
                    System.out.println("Venda não encontrada.");
                    }  
                    
                break;
                
            case 4:
                System.out.println("VAI DELETAR UMA VENDA DO REGISTRO");
                System.out.println("Qual o id da venda que deseja deletar? ");
                idVenda = teclado.nextInt();
                
               // Venda vendaDel = new Venda();
                //vendaDel.setVendaId(id);
                Venda vendaDel = vendaService.consultarPorId(idVenda);
                
                vendaService.consultarPorId(idVenda);

                if (vendaDel != null) {
                // Excluir a venda
                vendaService.excluir(vendaDel);
                System.out.println("Venda excluída com sucesso!");
                } else {
                System.out.println("Venda não encontrada.");
                }
                
                break;
    
            case 5:
                System.out.println("VAI RETORNAR QUANTIDADE VENDAS DO DIA");
                teclado.nextLine(); // limpar o buffer
                System.out.println("Digite a data (formato dd/MM/yyyy): ");
                String Data = teclado.nextLine();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date buscarData = dateFormat.parse(Data);
                
                int quantidadeVendas = vendaService.retornarVendasPorDia(buscarData);

                if (quantidadeVendas >= 0) { // Verifica se a consulta retornou um valor válido
                System.out.println("Quantidade de vendas em " + Data + ": " + quantidadeVendas);
                } else {
                System.out.println("Erro ao buscar a quantidade de vendas.");
                }
                
                break;
                
                // ... (código do default) ...
                default:
                System.out.println("OPÇÃO INVÁLIDA");

        }              
    }
      
}
