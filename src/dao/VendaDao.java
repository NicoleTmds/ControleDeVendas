package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Venda;
import conf.Conexao;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Cliente;
import model.Produto;
import model.VendaProduto;

public class VendaDao {
    
    public Conexao conexao;
    public PreparedStatement ps;
    
    public VendaDao(){
        conexao = new Conexao();        
    }
    
    public ResultSet listar(){
        try {
            return conexao.getConn() // uma linha só
                    .createStatement().executeQuery("SELECT * FROM venda");   // uma linha só
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    public ResultSet listarProdutosVenda(Venda venda) {
        ResultSet rs = null;
        try {
            String SQL = "SELECT produto_id, quantidade, valor_unidade, valor_total_item FROM venda_produto WHERE venda_id = ?";
            ps = conexao.getConn().prepareStatement(SQL);
            ps.setInt(1, venda.getVendaId());
            rs = ps.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return rs;
    }
    
    public Venda consultarPorId(int vendaId) {
        Venda venda = null;
        try {
            String SQL = "SELECT * FROM venda WHERE venda_id = ?";
            ps = conexao.getConn().prepareStatement(SQL);
            ps.setInt(1, vendaId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Cria o objeto Cliente (você precisará buscar os detalhes do cliente)
                Cliente cliente = new ClienteDao().consultarPorId(rs.getInt("cliente_id"));

                venda = new Venda();
                venda.setVendaId(vendaId);
                venda.setCliente(cliente);
                venda.setData(rs.getDate("data"));
                venda.setValorTotal(rs.getDouble("valor_total"));

                // Buscar os itens da venda (VendaProduto)
                String sqlItens = "SELECT * FROM venda_produto WHERE venda_id = ?";
                PreparedStatement psItens = conexao.getConn().prepareStatement(sqlItens);
                psItens.setInt(1, vendaId);
                ResultSet rsItens = psItens.executeQuery();

                List<VendaProduto> itensVendidos = new ArrayList<>();
                while (rsItens.next()) {
                    Produto produto = new ProdutoDao().consultarPorId(rsItens.getInt("produto_id"));
                    VendaProduto vp = new VendaProduto(venda, produto, rsItens.getInt("quantidade"), rsItens.getDouble("valor_total_item"));
                    vp.setId(rsItens.getInt("venda_id"));
                    itensVendidos.add(vp);
                }
                venda.setItensVendidos(itensVendidos);

                rsItens.close();
                psItens.close();
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return venda;
    }
    
    public void inserir(Venda venda) {
        try {
            String SQL = "INSERT INTO venda(cliente_id, data, valor_total) "
                    + "VALUES (?, ?, ?)";

            ps = conexao.getConn().prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, venda.getCliente().getClienteId());
            ps.setDate(2, new java.sql.Date(venda.getData().getTime()));
            ps.setDouble(3, venda.getValorTotal());
            ps.executeUpdate();


            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int vendaId = rs.getInt(1);
            

            // Inserir itens da venda
            for (VendaProduto vp : venda.getItensVendidos()) {
                
                vp.getVenda().setVendaId(vendaId); // Define o ID da venda no item
                VendaProdutoDao vendaProdutoDao = new VendaProdutoDao();
                vendaProdutoDao.inserir(vp);
            }

            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public int getNextVendaId() throws SQLException {
        int nextId = 1; // Default value in case there are no records in the table
        String SQL = "SELECT MAX(venda_id) AS max_id FROM venda";
        
        ps = conexao.getConn().prepareStatement(SQL);
      
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nextId = rs.getInt("max_id") + 1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return nextId;
    }

    public void excluir(Venda venda) {
        try {
            // Começa excluindo os itens da venda (VendaProduto)
            String deleteItensSQL = "DELETE FROM venda_produto WHERE venda_id = ?";
            ps = conexao.getConn().prepareStatement(deleteItensSQL);
            ps.setInt(1, venda.getVendaId());
            ps.executeUpdate();
            ps.close();
            
            // Exclui a venda da tabela "venda"
            String deleteVendaSQL = "DELETE FROM venda WHERE venda_id = ?";
            ps = conexao.getConn().prepareStatement(deleteVendaSQL);
            ps.setInt(1, venda.getVendaId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void editar(Venda venda) {
        try {
            String SQL = "UPDATE venda SET cliente_id = ?, data = ?, valor_total = ? WHERE venda_id = ?";
            
            ps = conexao.getConn().prepareStatement(SQL);
            ps.setInt(1, venda.getCliente().getClienteId());
            ps.setDate(2, new java.sql.Date(venda.getData().getTime()));
            ps.setDouble(3, venda.getValorTotal());
            ps.setInt(4, venda.getVendaId());
            ps.executeUpdate();
            ps.close();

            // Excluir itens de venda existentes
            String deleteItensSQL = "DELETE FROM venda_produto WHERE venda_id = ?";
            ps = conexao.getConn().prepareStatement(deleteItensSQL);
            ps.setInt(1, venda.getVendaId());
            ps.executeUpdate();
            ps.close();

            // Inserir/Atualizar itens de venda
            VendaProdutoDao vendaProdutoDao = new VendaProdutoDao();
            for (VendaProduto vp : venda.getItensVendidos()) {
                vp.getVenda().setVendaId(venda.getVendaId()); // Garante que o ID da venda esteja correto no item

                // Verifica se o item já existe (tem ID) para atualizar ou inserir
                if (vp.getId() != 0) {
                    vendaProdutoDao.editar(vp); // Atualiza item existente
                } else {
                    vendaProdutoDao.inserir(vp); // Insere novo item
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet retornarVendasPorDia(Date data) {
        try {
            String sql = "SELECT COUNT(*) FROM venda WHERE data = ? ";
            PreparedStatement ps = conexao.getConn().prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(data.getTime())); // Conversão para java.sql.Date
            return ps.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
}