package com.marllon.vieira.vergili.catalogo_financeiro.unit.restController;


import com.marllon.vieira.vergili.catalogo_financeiro.repository.PagamentosRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.PagamentosService;
import jakarta.transaction.Transactional;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class PagamentoRestControllerTest {

    @Mock
    private PagamentosService pagamentoService;

    @Autowired
    private PagamentosRepository pagamentosRepository;


}
