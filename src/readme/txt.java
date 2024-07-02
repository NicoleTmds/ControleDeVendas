/*
VendaDao.consultarPorId(int vendaId):

Recebe o ID da venda como parâmetro.
Busca os dados da venda na tabela venda usando o ID.
Cria um objeto Cliente buscando seus detalhes pelo ID do cliente associado à venda (usando ClienteDao.consultarPorId).
Cria um objeto Venda e preenche seus atributos com os dados obtidos do banco de dados.
Busca os itens da venda (objetos VendaProduto) na tabela venda_produto usando o ID da venda.
Para cada item, cria um objeto Produto buscando seus detalhes pelo ID do produto (usando ProdutoDao.consultarPorId).
Cria um objeto VendaProduto e o adiciona à lista de itens da venda.
Retorna o objeto Venda completo, com seus itens.
VendaService.consultarPorId(int vendaId):

Simplesmente chama o método consultarPorId de VendaDao e retorna o resultado.



*/