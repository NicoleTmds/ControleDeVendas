package service;

import dao.ProdutoDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Produto;

public class ProdutoService {
    
    private ProdutoDao produtoDao;
    
    public ProdutoService(){
        produtoDao = new ProdutoDao();
    }
    
        public ResultSet listar(){
        return produtoDao.listar();
    }
        
    public Produto consultarPorId(int produtoId) {
        return produtoDao.consultarPorId(produtoId);
    }
    
    public Produto retornarProdutoMaisVendido() throws SQLException {
        ResultSet rs = produtoDao.retornarProdutoMaisVendido();
        if (rs.next()) {
            // Cria um objeto Produto com os dados do ResultSet
            Produto produto = new Produto(
                rs.getInt("produto_id"),
                rs.getString("nome"),
                0, // A quantidade em estoque não é relevante aqui
                0  // O valor unitário não é relevante aqui
            );
            return produto;
        } else {
            return null; // Ou lançar uma exceção, dependendo da sua lógica de negócio
        }
    }

    
    public boolean inserir(Produto produto){
        
        if (produto.getNome() == "" || produto.getQuantidadeEstoque() == 0)
            return false;
        
        produtoDao.inserir(produto);
        return true;
    }
    
    public boolean excluir(Produto produto){
        if (produto.getProdutoId() == -1)
            return false;
        
        produtoDao.excluir(produto);
        return true;
    }
    
    public boolean editar(Produto produto){
        if (produto.getNome() == "" || produto.getQuantidadeEstoque() == 0)
            return false;
        
        produtoDao.editar(produto);
        return true;
    }
}
