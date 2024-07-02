package teste;


import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import model.Produto;
import org.junit.Assert;
import org.junit.Test;
import service.ClienteService;
import service.ProdutoService;
import service.VendaService;


public class TesteUnitario{

    @Test
    public void testRetornarClienteVenda() throws SQLException {
        ClienteService clienteService = new ClienteService();
        ResultSet rs = clienteService.retornarClienteVenda(14);

        Assert.assertTrue(rs.next());
        int clienteId = rs.getInt("cliente_id");    
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        String endereco = rs.getString("endereco");
        String cpf = rs.getString("cpf");
        String telefone = rs.getString("telefone");

        Assert.assertEquals(2, clienteId);
        Assert.assertEquals("Thiago", nome);
        Assert.assertEquals("thi@825", email);
        Assert.assertEquals("Rua lala", endereco);
        Assert.assertEquals("85416974754", cpf);
        Assert.assertEquals("(82)85842-5744", telefone);
    }
    
    
    @Test
    public void testRetornarProdutoMaisVendido() throws SQLException {
        ProdutoService produtoService = new ProdutoService();
        Produto produto = produtoService.retornarProdutoMaisVendido();

        Assert.assertNotNull(produto);
        Assert.assertEquals(7, produto.getProdutoId());
        Assert.assertEquals("Caneta", produto.getNome());
    }

    @Test
    public void testRetornarQuantidadeVendasPorDia() throws SQLException, ParseException {
        VendaService vendaService = new VendaService();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date data = new Date(dateFormat.parse("2024-06-30").getTime());
        
        int quantidadeVendas = vendaService.retornarVendasPorDia(data);
        Assert.assertEquals(5, quantidadeVendas);
    }
}

/*
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import model.Produto;
import model.Venda;
import model.VendaProduto;
import org.junit.Before;   
import service.ClienteService;
import service.ProdutoService;
import service.VendaService;


    Venda venda;
    VendaProduto vendaProduto;
    int i, vendaId;
    
    @Before
    public void setUp() throws SQLException {

        ClienteService clienteService = new ClienteService();
        Cliente cliente = new Cliente("João Silva", "joao@email.com", "Rua A, 123", "12345678901", "(11) 98765-4321");
        clienteService.inserir(cliente);

        ProdutoService produtoService = new ProdutoService();
        Produto produtoA = new Produto("Produto A", 10, 15.0);
        produtoService.inserir(produtoA);
        Produto produtoB = new Produto("Produto B", 5, 20.0);
        produtoService.inserir(produtoB);

        VendaService vendaService = new VendaService();
        List<VendaProduto> itensVenda = new ArrayList<>();

        // Cria a venda fora do loop
        vendaId = vendaService.getNextVendaId();
        Venda venda = new Venda(vendaId,cliente, new Date(System.currentTimeMillis()), 0.0); 
        for (int i = 0; i < 2; i++) { 
            VendaProduto vendaProduto;
        if (i == 0) {
            vendaProduto = new VendaProduto(venda, produtoA, 2, 0.0); // Produto A
        } else {
            vendaProduto = new VendaProduto(venda, produtoB, 3, 0.0); // Produto B
        }
        vendaProduto.CalcularValorTotalItem(); // Calcula o valor total do item
        itensVenda.add(vendaProduto); // Adiciona o item à lista

        // Atualiza o valor total da venda a cada iteração
        venda.setValorTotal(venda.getValorTotal() + vendaProduto.getValorTotalItem()); 
        }

        venda.setItensVendidos(itensVenda); // Associa os itens à venda
        vendaService.inserir(venda);
    
------------------------------------------------------------------------------------------------------------
    /*private void inserirVendaTeste() throws SQLException {
        Cliente cliente = new Cliente("Maria Joaquina", "maria@email.com", "Rua B, 456", "98765432109", "(22) 91234-5678");

        Produto produtoC = new Produto("Produto C", 8, 12.5);
        Produto produtoD = new Produto("Produto D", 15, 8.0);

        VendaDao vendaDao = new VendaDao();
        List<VendaProduto> itensVenda = new ArrayList<>();
        Venda venda = new Venda(cliente, new Date(System.currentTimeMillis()), 0.0); // Cria a venda fora do loop

    for (int i = 0; i < 2; i++) { // Loop para adicionar os dois produtos
        VendaProduto vendaProduto;
        if (i == 0) {
            vendaProduto = new VendaProduto(venda, produtoA, 2, 30.0); // Produto A
        } else {
            vendaProduto = new VendaProduto(venda, produtoB, 3, 60.0); // Produto B
        }
        vendaProduto.CalcularValorTotalItem(); // Calcula o valor total do item
        itensVenda.add(vendaProduto); // Adiciona o item à lista

        venda.setValorTotal(venda.getValorTotal() + vendaProduto.getValorTotalItem()); // Atualiza o valor total da venda
    }
    venda.setItensVendidos(itensVenda); // Associa os itens à venda
    vendaDao.inserir(venda); // Insere a venda e seus itens no banco de dados
}
    }
        @Test
        public void testRetornarClienteVenda() throws SQLException {
        ClienteDao clienteDao = new ClienteDao();
        ResultSet rs = clienteDao.retornarClienteVenda(venda.getVendaId()); // ID da venda de teste

        Assert.assertTrue(rs.next());
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        String endereco = rs.getString("endereco");
        String cpf = rs.getString("cpf");
        String telefone = rs.getString("telefone");

        Assert.assertEquals("João Silva", nome);
        Assert.assertEquals("joao@email.com", email);
        Assert.assertEquals("Rua A, 123", endereco);
        Assert.assertEquals("12345678901", cpf);
        Assert.assertEquals("(11) 98765-4321", telefone);
    }

    @Test
    public void testRetornarProdutoMaisVendido() throws SQLException {
        ProdutoDao produtoDao = new ProdutoDao();
        ResultSet rs = produtoDao.retornarProdutoMaisVendido(); // Sem filtro de data

        Assert.assertTrue(rs.next());
        int produtoId = rs.getInt("produto_id");
        int quantidadeTotal = rs.getInt("quantidade_total");
        String nomeProduto = rs.getString("nome");

        Assert.assertEquals(2, produtoId);
        Assert.assertEquals(3, quantidadeTotal);
        Assert.assertEquals("Produto B", nomeProduto); 
    }
    

    @Test
    public void testRetornarQuantidadeVendasPorDia() throws SQLException, ParseException {
        VendaDao vendaDao = new VendaDao();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date data = new Date(dateFormat.parse("2024-06-30").getTime());

        ResultSet rs = vendaDao.retornarVendasPorDia(data);
        Assert.assertTrue(rs.next());
        int quantidadeVendas = rs.getInt(1);

        Assert.assertEquals(1, quantidadeVendas);
    }
    */