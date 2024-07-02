package menu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import model.Pagamento;
import model.Venda;
import service.PagamentoService;
import service.VendaService;

public class MenuPagamento {

    public void opcaoPagamento() throws SQLException{
        int novaOpcao, idPagamento, idVenda;
        String tipoPagamento, statusPagamento;
       
    
        Scanner teclado = new Scanner(System.in);
        PagamentoService pagamentoService = new PagamentoService();  

        VendaService vendaService = new VendaService();
        Venda v = new Venda();

        Pagamento p = new Pagamento(v,null,null); // se for deletar não precisará instânciar
        
        
            System.out.println("1: Listar Pagamentos");
            System.out.println("2: Adicionar um Pagamento");
            System.out.println("3: Editar um Pagamento");
            System.out.println("4: Deletar um Pagamento");
            System.out.print("Opção: ");
            novaOpcao = teclado.nextInt();
            
            
                switch(novaOpcao){
                    case 1:
                        System.out.println("Vai listar");

                        ResultSet rs = pagamentoService.listar();

                        while(rs.next()){
                            System.out.print("Nº identificador: ");
                            System.out.println(rs.getInt("pagamento_id"));
                            System.out.print("Nº identificador da Venda: ");
                            System.out.println(rs.getInt("venda_id"));
                            System.out.print("Tipo de Pagamento: ");
                            System.out.println(rs.getString("tipo_pagamento"));
                            System.out.print("Status de Pagamento: ");
                            System.out.println(rs.getString("status_pagamento"));

                            System.out.println("---------");
                        }
                        
                        break;
                     
                    case 2:   // LEMBRAR DE TROCAR POR nextLine
                        System.out.println("Vai inserir");
                        
                        System.out.println("OK! Vamos precisar dos dados do Pagamento");
                        System.out.println("Nº identificador da Venda: ");
                        idVenda = teclado.nextInt();
                        v.setVendaId(idVenda);
                        //teclado.next();
                        System.out.println("Qual o tipo de Pagamento: ");
                        tipoPagamento = teclado.next();
                        System.out.println("Qual o status de Pagamento: ");
                        statusPagamento = teclado.next();
                        
                        p = new Pagamento(v, tipoPagamento, 
                                statusPagamento);
                        
                        pagamentoService.inserir(p);
                        break;
                                  
       
                    case 3: // concluído
                        System.out.println("Vai editar");
                        System.out.println("Qual o id do pagamento que deseja editar? ");
                        idPagamento = teclado.nextInt();
                        System.out.print("Novo ID de Venda(ou 0 para manter o mesmo): ");
                        idVenda = teclado.nextInt();
                        teclado.nextLine(); // Limpar o buffer
                
                        if (idVenda != 0) {
                        Venda novaVenda = new Venda();
                        novaVenda.setVendaId(idVenda);
                        p.setVenda(novaVenda);
                        }
  
                        System.out.println("Tipo de Pagamento: ");
                        tipoPagamento = teclado.next();
                        System.out.println("Status de Pagamento: ");
                        statusPagamento = teclado.next();                 
                        
                        p.setStatus_pagamento(statusPagamento);
                        p.setTipoPagamento(tipoPagamento);
                        p.setPagamento_id(idPagamento);
                        pagamentoService.editar(p);
                        
                        break;
                        
                        
                    case 4:
                        System.out.println("Vai deletar Pagamento");
                        
                        System.out.println("Qual o id do Pagamento que deseja deletar? ");
                        idPagamento = teclado.nextInt();
                        p.setPagamento_id(idPagamento);
                        pagamentoService.excluir(p);
                        
                        break;
                        
                        
                    default:
                        System.out.println("Opcao inválida");
                }
  
    }   
}
    
