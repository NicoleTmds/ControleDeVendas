package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Produto;
import conf.Conexao;

public class ProdutoDao {  
    
    private Conexao conexao;
    private PreparedStatement ps;
    
    public ProdutoDao(){
        conexao = new Conexao();        
    }
    
    public ResultSet listar(){
        try {
            return conexao.getConn() // uma linha só
                    .createStatement().executeQuery("SELECT * FROM produto");   // uma linha só
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public Produto consultarPorId(int produtoId) {
        Produto produto = null;
        try {
            String SQL = "SELECT * FROM produto WHERE produto_id = ?";
            PreparedStatement ps = conexao.getConn().prepareStatement(SQL);
            ps.setInt(1, produtoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                produto = new Produto(
                    rs.getInt("produto_id"),
                    rs.getString("nome"),
                    rs.getInt("quantidade_de_estoque"),
                    rs.getDouble("valor_unidade")
                );
            }
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return produto;
    }
    
    public void inserir(Produto produto) {
        try {
            String SQL = "INSERT INTO produto(nome, quantidade_de_estoque, valor_unidade) " +
                     "VALUES (?, ?, ?)";
            PreparedStatement ps = conexao.getConn().prepareStatement(SQL);
            ps.setString(1, produto.getNome());
            ps.setInt(2, produto.getQuantidadeEstoque());
            ps.setDouble(3, produto.getValorUnidade()); // Insere o valor unitário
            
            ps.executeUpdate();
            
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    
    public void excluir(Produto produto){
        try {
            String SQL = "DELETE FROM produto WHERE produto_id = ?";
            
            ps = conexao.getConn().prepareStatement(SQL);
            
            ps.setInt(1, produto.getProdutoId());           
                        
            ps.executeUpdate();
                        
            ps.close(); 
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
    }
    
    public void editar(Produto produto){
        try {
            String SQL = "UPDATE produto SET " +
                        "nome= ?, quantidade_de_estoque= ?, valor_unidade= ? " + 
                        "WHERE produto_id=?";
            
            ps = conexao.getConn().prepareStatement(SQL);
            
            ps.setString(1, produto.getNome());
            ps.setInt(2, produto.getQuantidadeEstoque()); 
            ps.setDouble(3, produto.getValorUnidade());

            ps.executeUpdate();
                        
            ps.close(); 
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public ResultSet retornarProdutoMaisVendido() {
        try {
            String SQL = "SELECT p.nome, vp.produto_id, SUM(quantidade) as quantidade_total " +
                        "FROM venda_produto vp " +
                        "INNER JOIN produto p ON vp.produto_id = p.produto_id " +
                        "GROUP BY p.nome, vp.produto_id " +
                        "ORDER BY quantidade_total DESC " +
                        "LIMIT 1";
            ps = conexao.getConn().prepareStatement(SQL);
            return ps.executeQuery();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

