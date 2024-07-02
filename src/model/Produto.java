package model;

public class Produto{
    private int produtoId;
    private String nome;
    private int quantidadeEstoque;
    private double valorUnidade;

    public Produto(int produtoId, String nome, int quantidadeEstoque, double valorUnidade) {
        this.produtoId = produtoId;
        this.nome = nome;
        this.quantidadeEstoque = quantidadeEstoque;
        this.valorUnidade = valorUnidade;
    }

    public Produto(String nome, int quantidadeEstoque, double valorUnidade) {
        this.nome = nome;
        this.quantidadeEstoque = quantidadeEstoque;
        this.valorUnidade = valorUnidade;
    }

    public Produto() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public double getValorUnidade() {
        return valorUnidade;
    }

    public void setValorUnidade(double valorUnidade) {
        this.valorUnidade = valorUnidade;
    }
    
    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    
}



