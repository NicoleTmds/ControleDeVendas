package model;

public class VendaProduto {
    private int id;
    private Venda venda;
    private Produto produto;
    private int quantidade;
    private Produto valorUnitario;
    private double valorTotalItem;

    public VendaProduto(Venda venda, Produto produto, int quantidade, Produto valorUnitario, double valorTotalItem) {
        this.venda = venda;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorTotalItem = valorTotalItem;
    }

    public VendaProduto(Venda venda, Produto produto, int quantidade, double valorTotalItem) {
        this.venda = venda;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorTotalItem = valorTotalItem;
    }
    
    //public double CalcularValorTotalItem(){
    //    return quantidade * valorUnitario;
    //}
    
    public void CalcularValorTotalItem() {
        this.valorTotalItem = (quantidade * produto.getValorUnidade());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Produto getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Produto valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public double getValorTotalItem() {
        return valorTotalItem;
    }

    public void setValorTotalItem(double valorTotalItem) {
        this.valorTotalItem = valorTotalItem;
    }
 
}
