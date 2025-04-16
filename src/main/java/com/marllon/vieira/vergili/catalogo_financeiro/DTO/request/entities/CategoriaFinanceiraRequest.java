package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;



/**Record para o usuário, ao enviar uma requisicao pela categoria de contas,
Irá solicitar que ele coloque o tipo de categoria, e a descrição da mesma.(Somente para envio de dados pela Entidade)
 */
public record CategoriaFinanceiraRequest(CategoriaFinanceira tipoCategoria) {
}

