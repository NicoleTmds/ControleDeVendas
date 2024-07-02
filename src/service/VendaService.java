package service;

import dao.VendaDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import model.Venda;

public class VendaService {
    
    private VendaDao vendaDao;
    
    public VendaService(){
        vendaDao = new VendaDao();
    }
    
    public ResultSet listar(){
        return vendaDao.listar();
    }
    
    public ResultSet listarProdutoVenda(Venda venda){
        return vendaDao.listarProdutosVenda(venda);
    }
    
    public Venda consultarPorId(int vendaId) {
        return vendaDao.consultarPorId(vendaId);
    }
    
    public int getNextVendaId() throws SQLException{
        return vendaDao.getNextVendaId();
    }
    
    public int retornarVendasPorDia(Date data) {
        try {
            ResultSet rs = vendaDao.retornarVendasPorDia(data);
            if (rs.next()) { // Move o cursor para a primeira linha do ResultSet
                return rs.getInt(1); // Retorna o valor inteiro da primeira coluna
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Indica erro na consulta
    }
    
    public boolean inserir(Venda venda){
        
        if (venda.getCliente().getClienteId() == 0 || venda.getData() == null || venda.getValorTotal() == 0)
            return false;
        
        vendaDao.inserir(venda);
        return true;
    }   

    public boolean excluir(Venda venda) {
        if (venda.getVendaId() == 0) {
            return false;
        }

        vendaDao.excluir(venda);
        return true;
    }
    
    public boolean editar(Venda venda){
        if (venda.getCliente().getClienteId() == 0 || venda.getData() == null || venda.getValorTotal() == 0)
            return false;
        
        vendaDao.editar(venda);
        return true;
    } 

}


    
