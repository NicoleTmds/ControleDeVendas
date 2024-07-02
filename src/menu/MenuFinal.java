package menu;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

public class MenuFinal {
  
    
    public static void main(String[] args) throws SQLException, ParseException {
        
        int opcao;
        
        Scanner teclado = new Scanner(System.in);
 
        System.out.println("");
        System.out.println("MENU DE SELEÇÃO");
        System.out.println("Digite uma das opções para iniciar ");
        System.out.println("1: Operações envolvendo cliente");
        System.out.println("2: Operação envolvendo produto");
        System.out.println("3: Operação envolvendo venda ");
        System.out.println("4: Operação envolvendo Pagamento");
        System.out.print("Opção: ");
        opcao = teclado.nextInt();
        System.out.println("\n \n");
       
        
        switch(opcao){
            
            case 1: // PRONTO
                
                MenuCliente tc = new MenuCliente();
                tc.opcaoCliente();
                break;
                
            case 2: // PRONTO
                MenuProduto tp = new MenuProduto();
                tp.opcaoProduto();
                break;
                

            case 3: // PRONTO
                MenuVenda tv = new MenuVenda();
                tv.opcaoVenda();
                break;
            
            case 4:
                MenuPagamento tpag = new MenuPagamento();
                tpag.opcaoPagamento();
                break;
                

            default:
                System.out.println("Opção incorreta.");
 
            
                       
        }
    }

}    

